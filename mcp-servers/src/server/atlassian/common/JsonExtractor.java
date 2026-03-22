package server.atlassian.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Lightweight utility for extracting values from JSON strings.
 *
 * <p>Provides string-parsing-based extraction without any external JSON library.
 * Designed specifically for the well-known JSON shapes returned by Atlassian APIs
 * (Jira REST API v3, Confluence REST API v2, Bitbucket Cloud API 2.0).
 *
 * <p><strong>Supported operations:</strong>
 * <ul>
 *   <li>{@link #string(String, String)} — extract a string field value</li>
 *   <li>{@link #intValue(String, String, int)} — extract a numeric field value</li>
 *   <li>{@link #boolValue(String, String, boolean)} — extract a boolean field value</li>
 *   <li>{@link #block(String, String)} — extract a nested JSON object or array block</li>
 *   <li>{@link #stringList(String, String)} — extract an array of string values</li>
 *   <li>{@link #arrayBlocks(String, String)} — extract an array of JSON object blocks</li>
 *   <li>{@link #navigate(String, String...)} — navigate a dotted path to a string value</li>
 *   <li>{@link #navigateBlock(String, String...)} — navigate a dotted path to a JSON block</li>
 *   <li>{@link #extractAdfText(String)} — extract readable text from Atlassian Document Format JSON</li>
 *   <li>{@link #isNull(String, String)} — check whether a field is JSON null</li>
 * </ul>
 *
 * <p><strong>Limitations:</strong> This is a heuristic parser, not a fully spec-compliant
 * JSON parser. It handles common Atlassian API response shapes reliably. Edge cases with
 * deeply nested or unusual JSON may require additional handling.
 */
public final class JsonExtractor {

    private JsonExtractor() {
        // Utility class — no instances
    }

    // ──────────────────────────────────────────────────────────────────────────
    // String extraction
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Extracts the string value for the given key.
     *
     * <p>Finds the first occurrence of {@code "key": "value"} in the JSON and returns
     * the unescaped value. Returns {@link Optional#empty()} if the key is absent or
     * the value is not a string (e.g., it is {@code null}, a number, or an object).
     *
     * @param json the JSON string to search
     * @param key  the field name to look up
     * @return the string value, or empty if not found or not a string
     */
    public static Optional<String> string(final String json, final String key) {
        if (json == null || json.isBlank() || key == null) {
            return Optional.empty();
        }

        final int keyPos = findKey(json, key, 0);
        if (keyPos < 0) {
            return Optional.empty();
        }

        final int valueStart = skipWhitespaceAndColon(json, keyPos + key.length() + 2);
        if (valueStart < 0 || valueStart >= json.length()) {
            return Optional.empty();
        }

        if (json.charAt(valueStart) != '"') {
            return Optional.empty(); // Not a string value
        }

        return Optional.of(readQuotedString(json, valueStart));
    }

    /**
     * Extracts the string value for the given key, returning a default
     * if not found.
     *
     * @param json         the JSON string to search
     * @param key          the field name
     * @param defaultValue fallback value
     * @return the extracted string or the default
     */
    public static String stringOrDefault(final String json, final String key,
                                         final String defaultValue) {
        return string(json, key).orElse(defaultValue);
    }

    /**
     * Extracts the raw JSON token for a key as a string.
     *
     * <p>Unlike {@link #string(String, String)}, this method handles any value type:
     * <ul>
     *   <li>String values — returned unquoted (e.g., {@code "hello"} → {@code hello})</li>
     *   <li>Number values — returned as-is (e.g., {@code 42})</li>
     *   <li>Boolean values — returned as-is ({@code true}, {@code false})</li>
     *   <li>Null values — returns the string {@code "null"}</li>
     * </ul>
     *
     * <p>Useful for JSON-RPC id fields which may be a number, string, or null.
     *
     * @param json the JSON string to search
     * @param key  the field name
     * @return the raw token as a string, or {@code "null"} if the key is absent
     */
    public static String rawValue(final String json, final String key) {
        if (json == null || json.isBlank() || key == null) return "null";

        final int keyPos = findKey(json, key, 0);
        if (keyPos < 0) return "null";

        final int valueStart = skipWhitespaceAndColon(json, keyPos + key.length() + 2);
        if (valueStart < 0 || valueStart >= json.length()) return "null";

        final char c = json.charAt(valueStart);
        if (c == '"') {
            // String — return unquoted content
            return "\"" + readQuotedString(json, valueStart) + "\"";
        } else if (c == '{' || c == '[') {
            return "null"; // Objects/arrays not supported as raw scalar
        } else {
            // Number, boolean, null — read until delimiter
            final var num = readNumber(json, valueStart);
            if (!num.isBlank()) return num;
            // Try boolean/null literals
            for (final var literal : new String[]{"null", "true", "false"}) {
                if (json.startsWith(literal, valueStart)) return literal;
            }
            return "null";
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Null check
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Returns {@code true} if the field exists and its value is JSON {@code null}.
     *
     * @param json the JSON string
     * @param key  the field name
     * @return {@code true} if the field value is null
     */
    public static boolean isNull(final String json, final String key) {
        if (json == null || key == null) return true;

        final int keyPos = findKey(json, key, 0);
        if (keyPos < 0) return true;

        final int valueStart = skipWhitespaceAndColon(json, keyPos + key.length() + 2);
        if (valueStart < 0 || valueStart >= json.length()) return true;

        return json.startsWith("null", valueStart);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Numeric extraction
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Extracts the integer value for the given key.
     *
     * @param json         the JSON string
     * @param key          the field name
     * @param defaultValue fallback if not found or not a number
     * @return the integer value or the default
     */
    public static int intValue(final String json, final String key, final int defaultValue) {
        if (json == null || key == null) return defaultValue;

        final int keyPos = findKey(json, key, 0);
        if (keyPos < 0) return defaultValue;

        final int valueStart = skipWhitespaceAndColon(json, keyPos + key.length() + 2);
        if (valueStart < 0 || valueStart >= json.length()) return defaultValue;

        final var numStr = readNumber(json, valueStart);
        if (numStr.isBlank()) return defaultValue;

        try {
            return Integer.parseInt(numStr);
        } catch (NumberFormatException ignored) {
            return defaultValue;
        }
    }

    /**
     * Extracts the long value for the given key.
     *
     * @param json         the JSON string
     * @param key          the field name
     * @param defaultValue fallback if not found or not a number
     * @return the long value or the default
     */
    public static long longValue(final String json, final String key, final long defaultValue) {
        if (json == null || key == null) return defaultValue;

        final int keyPos = findKey(json, key, 0);
        if (keyPos < 0) return defaultValue;

        final int valueStart = skipWhitespaceAndColon(json, keyPos + key.length() + 2);
        if (valueStart < 0 || valueStart >= json.length()) return defaultValue;

        final var numStr = readNumber(json, valueStart);
        if (numStr.isBlank()) return defaultValue;

        try {
            return Long.parseLong(numStr);
        } catch (NumberFormatException ignored) {
            return defaultValue;
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Boolean extraction
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Extracts the boolean value for the given key.
     *
     * @param json         the JSON string
     * @param key          the field name
     * @param defaultValue fallback if not found
     * @return the boolean value or the default
     */
    public static boolean boolValue(final String json, final String key,
                                     final boolean defaultValue) {
        if (json == null || key == null) return defaultValue;

        final int keyPos = findKey(json, key, 0);
        if (keyPos < 0) return defaultValue;

        final int valueStart = skipWhitespaceAndColon(json, keyPos + key.length() + 2);
        if (valueStart < 0 || valueStart >= json.length()) return defaultValue;

        if (json.startsWith("true", valueStart))  return true;
        if (json.startsWith("false", valueStart)) return false;
        return defaultValue;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Block extraction (objects and arrays)
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Extracts the raw JSON block (object {@code {...}} or array {@code [...]})
     * for the given key.
     *
     * @param json the JSON string
     * @param key  the field name
     * @return the raw JSON block, or empty if not found or value is not an object/array
     */
    public static Optional<String> block(final String json, final String key) {
        if (json == null || key == null) return Optional.empty();

        final int keyPos = findKey(json, key, 0);
        if (keyPos < 0) return Optional.empty();

        final int valueStart = skipWhitespaceAndColon(json, keyPos + key.length() + 2);
        if (valueStart < 0 || valueStart >= json.length()) return Optional.empty();

        final char opener = json.charAt(valueStart);
        if (opener != '{' && opener != '[') return Optional.empty();

        final int end = findMatchingBracket(json, valueStart);
        if (end < 0) return Optional.empty();

        return Optional.of(json.substring(valueStart, end + 1));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Array extraction
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Extracts an array of string values from a JSON string array field.
     *
     * <p>For example, given {@code "labels": ["bug","critical"]}, this method
     * returns {@code ["bug", "critical"]}.
     *
     * @param json the JSON string
     * @param key  the field name
     * @return list of extracted strings (may be empty)
     */
    public static List<String> stringList(final String json, final String key) {
        final var arrayBlock = block(json, key);
        if (arrayBlock.isEmpty()) return Collections.emptyList();

        final var block = arrayBlock.get().trim();
        if (!block.startsWith("[")) return Collections.emptyList();

        final List<String> result = new ArrayList<>();
        int pos = 1; // Skip opening '['
        while (pos < block.length()) {
            pos = skipWhitespace(block, pos);
            if (pos >= block.length() || block.charAt(pos) == ']') break;
            if (block.charAt(pos) == '"') {
                final var value = readQuotedString(block, pos);
                result.add(value);
                // Advance past the quoted string
                pos = advancePastQuotedString(block, pos);
            }
            pos = skipWhitespace(block, pos);
            if (pos < block.length() && block.charAt(pos) == ',') pos++;
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Extracts an array of raw JSON object blocks from a JSON array field.
     *
     * <p>For example, given {@code "issues": [{...}, {...}]}, this method
     * returns the raw JSON block for each issue object.
     *
     * @param json the JSON string
     * @param key  the array field name
     * @return list of raw JSON object strings (may be empty)
     */
    public static List<String> arrayBlocks(final String json, final String key) {
        final var arrayBlock = block(json, key);
        if (arrayBlock.isEmpty()) return Collections.emptyList();

        final var block = arrayBlock.get().trim();
        if (!block.startsWith("[")) return Collections.emptyList();

        final List<String> result = new ArrayList<>();
        int pos = 1; // Skip opening '['
        while (pos < block.length()) {
            pos = skipWhitespace(block, pos);
            if (pos >= block.length() || block.charAt(pos) == ']') break;
            if (block.charAt(pos) == '{' || block.charAt(pos) == '[') {
                final int end = findMatchingBracket(block, pos);
                if (end < 0) break;
                result.add(block.substring(pos, end + 1));
                pos = end + 1;
            } else {
                pos++; // skip unexpected characters
            }
            pos = skipWhitespace(block, pos);
            if (pos < block.length() && block.charAt(pos) == ',') pos++;
        }
        return Collections.unmodifiableList(result);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Path navigation
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Navigates a path of nested keys to extract a string value.
     *
     * <p>For example, {@code navigate(json, "fields", "status", "name")} is
     * equivalent to {@code json["fields"]["status"]["name"]}.
     *
     * @param json the root JSON string
     * @param keys the path components (first N-1 are block keys, last is the string key)
     * @return the string value at the path, or empty if not found
     */
    public static Optional<String> navigate(final String json, final String... keys) {
        if (json == null || keys == null || keys.length == 0) return Optional.empty();

        String current = json;
        for (int i = 0; i < keys.length - 1; i++) {
            final var nextBlock = block(current, keys[i]);
            if (nextBlock.isEmpty()) return Optional.empty();
            current = nextBlock.get();
        }

        return string(current, keys[keys.length - 1]);
    }

    /**
     * Navigates a path of nested keys to extract a JSON block.
     *
     * @param json the root JSON string
     * @param keys the path components (all are block keys)
     * @return the JSON block at the path, or empty if not found
     */
    public static Optional<String> navigateBlock(final String json, final String... keys) {
        if (json == null || keys == null || keys.length == 0) return Optional.empty();

        String current = json;
        for (final var key : keys) {
            final var nextBlock = block(current, key);
            if (nextBlock.isEmpty()) return Optional.empty();
            current = nextBlock.get();
        }

        return Optional.of(current);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Atlassian Document Format (ADF) text extraction
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Extracts plain text from an Atlassian Document Format (ADF) JSON document.
     *
     * <p>ADF is the rich text format used by Jira (Cloud) for issue descriptions,
     * comments, etc. This method extracts all {@code "text"} leaf values and
     * joins them into a readable string. Formatting is not preserved.
     *
     * @param adfJson the ADF JSON document (usually the value of a "description" field)
     * @return extracted plain text (may be blank if input has no text nodes)
     */
    public static String extractAdfText(final String adfJson) {
        if (adfJson == null || adfJson.isBlank()) return "";

        // Collect all "text": "..." values from the doc
        final var builder = new StringBuilder();
        int pos = 0;
        while (pos < adfJson.length()) {
            final int keyPos = findKey(adfJson, "text", pos);
            if (keyPos < 0) break;

            final int valueStart = skipWhitespaceAndColon(adfJson, keyPos + 7); // length("text") + 2 quotes
            if (valueStart < 0 || valueStart >= adfJson.length()) break;

            if (adfJson.charAt(valueStart) == '"') {
                final var text = readQuotedString(adfJson, valueStart);
                if (!text.isBlank()) {
                    if (!builder.isEmpty()) builder.append(" ");
                    builder.append(text);
                }
                pos = advancePastQuotedString(adfJson, valueStart);
            } else {
                pos = keyPos + 7;
            }
        }

        return builder.toString().trim();
    }

    // ──────────────────────────────────────────────────────────────────────────
    // JSON-RPC 2.0 helpers
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Extracts a flat map of argument key→value pairs from a JSON object block.
     *
     * <p>Used primarily for parsing MCP {@code tools/call} argument objects. Only
     * top-level string, number, and boolean values are extracted — nested objects
     * are extracted as raw JSON strings.
     *
     * @param json the JSON arguments object (e.g., {@code {"query":"bug","maxResults":"10"}})
     * @return a mutable map of extracted arguments
     */
    public static java.util.Map<String, String> extractArgumentMap(final String json) {
        final var result = new java.util.LinkedHashMap<String, String>();
        if (json == null || json.isBlank()) return result;

        int pos = 0;
        // Skip opening { if present
        if (json.charAt(0) == '{') pos = 1;

        while (pos < json.length()) {
            pos = skipWhitespace(json, pos);
            if (pos >= json.length() || json.charAt(pos) == '}') break;

            // Read key
            if (json.charAt(pos) != '"') { pos++; continue; }
            final var key = readQuotedString(json, pos);
            pos = advancePastQuotedString(json, pos);
            pos = skipWhitespaceAndColon(json, pos);
            if (pos < 0) break;

            // Read value
            if (pos >= json.length()) break;
            final char c = json.charAt(pos);
            if (c == '"') {
                final var val = readQuotedString(json, pos);
                result.put(key, val);
                pos = advancePastQuotedString(json, pos);
            } else if (c == '{' || c == '[') {
                final int end = findMatchingBracket(json, pos);
                if (end < 0) break;
                result.put(key, json.substring(pos, end + 1));
                pos = end + 1;
            } else if (json.startsWith("null", pos)) {
                result.put(key, "");
                pos += 4;
            } else if (json.startsWith("true", pos)) {
                result.put(key, "true");
                pos += 4;
            } else if (json.startsWith("false", pos)) {
                result.put(key, "false");
                pos += 5;
            } else {
                // Number or unknown
                final var num = readNumber(json, pos);
                if (!num.isBlank()) {
                    result.put(key, num);
                    pos += num.length();
                } else {
                    pos++;
                }
            }

            pos = skipWhitespace(json, pos);
            if (pos < json.length() && json.charAt(pos) == ',') pos++;
        }

        return result;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Internal parsing helpers
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Finds the position of {@code "key"} in the JSON string, starting at {@code fromIndex}.
     * Returns -1 if not found.
     */
    private static int findKey(final String json, final String key, final int fromIndex) {
        final var keyPattern = "\"" + key + "\"";
        int pos = fromIndex;
        while (pos < json.length()) {
            final int found = json.indexOf(keyPattern, pos);
            if (found < 0) return -1;
            // Verify the character after the key is ':' (with optional whitespace)
            int afterKey = found + keyPattern.length();
            afterKey = skipWhitespace(json, afterKey);
            if (afterKey < json.length() && json.charAt(afterKey) == ':') {
                return found;
            }
            pos = found + 1;
        }
        return -1;
    }

    /**
     * Skips whitespace and the colon separator after a key name.
     * Returns the position of the first character of the value.
     */
    private static int skipWhitespaceAndColon(final String json, final int from) {
        int pos = skipWhitespace(json, from);
        if (pos >= json.length()) return -1;
        if (json.charAt(pos) == ':') pos++; // Skip the colon
        return skipWhitespace(json, pos);
    }

    /**
     * Skips whitespace characters.
     */
    private static int skipWhitespace(final String json, final int from) {
        int pos = from;
        while (pos < json.length() && Character.isWhitespace(json.charAt(pos))) {
            pos++;
        }
        return pos;
    }

    /**
     * Reads a quoted JSON string starting at {@code pos} (which must point to opening {@code "}).
     * Returns the unescaped content (without outer quotes).
     */
    private static String readQuotedString(final String json, final int pos) {
        if (pos >= json.length() || json.charAt(pos) != '"') return "";

        final var builder = new StringBuilder();
        int i = pos + 1; // Skip opening quote
        while (i < json.length()) {
            final char c = json.charAt(i);
            if (c == '"') break; // End of string
            if (c == '\\' && i + 1 < json.length()) {
                final char escaped = json.charAt(i + 1);
                switch (escaped) {
                    case '"' -> builder.append('"');
                    case '\\' -> builder.append('\\');
                    case '/' -> builder.append('/');
                    case 'n' -> builder.append('\n');
                    case 'r' -> builder.append('\r');
                    case 't' -> builder.append('\t');
                    case 'b' -> builder.append('\b');
                    case 'f' -> builder.append('\f');
                    case 'u' -> {
                        // Unicode escape sequence (backslash-u XXXX)
                        if (i + 5 < json.length()) {
                            try {
                                final var hex = json.substring(i + 2, i + 6);
                                builder.append((char) Integer.parseInt(hex, 16));
                                i += 4;
                            } catch (NumberFormatException ignored) {
                                builder.append(escaped);
                            }
                        }
                    }
                    default -> builder.append(escaped);
                }
                i += 2;
            } else {
                builder.append(c);
                i++;
            }
        }
        return builder.toString();
    }

    /**
     * Returns the position just after a quoted string (past the closing {@code "}).
     */
    private static int advancePastQuotedString(final String json, final int pos) {
        if (pos >= json.length() || json.charAt(pos) != '"') return pos + 1;
        int i = pos + 1;
        while (i < json.length()) {
            final char c = json.charAt(i);
            if (c == '"') return i + 1;
            if (c == '\\') i++; // Skip escaped character
            i++;
        }
        return i;
    }

    /**
     * Reads a JSON number value at {@code pos}.
     * Returns the number string, or blank if not a number.
     */
    private static String readNumber(final String json, final int pos) {
        int end = pos;
        if (end < json.length() && (json.charAt(end) == '-' || json.charAt(end) == '+')) end++;
        while (end < json.length() && (Character.isDigit(json.charAt(end))
                || json.charAt(end) == '.' || json.charAt(end) == 'e'
                || json.charAt(end) == 'E' || json.charAt(end) == '+'
                || json.charAt(end) == '-')) {
            end++;
        }
        return end > pos ? json.substring(pos, end) : "";
    }

    /**
     * Finds the position of the matching closing bracket for the opening bracket at {@code pos}.
     * Handles nested objects, arrays, and string literals containing brackets.
     *
     * @param json the JSON string
     * @param pos  index of the opening bracket ({@code {}, {@code [})
     * @return index of the matching closing bracket, or -1 if not found
     */
    private static int findMatchingBracket(final String json, final int pos) {
        if (pos >= json.length()) return -1;
        final char opener = json.charAt(pos);
        final char closer = (opener == '{') ? '}' : ']';

        int depth = 0;
        int i = pos;
        while (i < json.length()) {
            final char c = json.charAt(i);
            if (c == '"') {
                // Skip string literal (it may contain brackets)
                i = advancePastQuotedString(json, i);
                continue;
            }
            if (c == opener) depth++;
            else if (c == closer) {
                depth--;
                if (depth == 0) return i;
            }
            i++;
        }
        return -1;
    }
}
