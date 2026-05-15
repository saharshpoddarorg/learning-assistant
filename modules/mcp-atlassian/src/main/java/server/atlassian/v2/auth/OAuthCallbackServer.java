package server.atlassian.v2.auth;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

/**
 * Lightweight localhost HTTP server that receives OAuth 2.0 authorization callbacks.
 *
 * <p>Starts on a random available port, listens for a single redirect callback
 * from the Atlassian OAuth consent page, extracts the authorization code,
 * and shuts down.
 *
 * <p>Uses the JDK built-in {@link com.sun.net.httpserver.HttpServer} — no external
 * dependencies required.
 */
public final class OAuthCallbackServer {

    private static final Logger LOGGER = Logger.getLogger(OAuthCallbackServer.class.getName());
    private static final String CALLBACK_PATH = "/callback";
    private static final int TIMEOUT_MINUTES = 5;

    private final int port;
    private HttpServer server;

    /**
     * Creates a callback server on the specified port.
     *
     * @param port the port to listen on (use 0 for any available port)
     */
    public OAuthCallbackServer(final int port) {
        this.port = port;
    }

    /**
     * Returns the full callback URL for OAuth redirect configuration.
     *
     * @return the localhost callback URL
     */
    public String getCallbackUrl() {
        final int actualPort = server != null
                ? server.getAddress().getPort()
                : port;
        return "http://localhost:" + actualPort + CALLBACK_PATH;
    }

    /**
     * Starts the callback server and waits for the authorization code.
     *
     * <p>Blocks until the OAuth redirect arrives or the timeout elapses.
     * The server automatically shuts down after receiving the callback.
     *
     * @return the authorization code from the OAuth callback
     * @throws IOException      if the server cannot start
     * @throws AuthException    if the callback indicates an error or times out
     */
    public String awaitAuthorizationCode() throws IOException, AuthException {
        final var codeFuture = new CompletableFuture<String>();

        server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
        final int actualPort = server.getAddress().getPort();
        LOGGER.info("OAuth callback server listening on port " + actualPort);

        server.createContext(CALLBACK_PATH, exchange -> {
            try {
                final var query = exchange.getRequestURI().getQuery();
                final var code = extractParam(query, "code");
                final var error = extractParam(query, "error");

                final String responseHtml;
                if (error != null) {
                    responseHtml = buildHtmlPage("Authentication Failed",
                            "Error: " + error + ". You can close this tab.");
                    codeFuture.completeExceptionally(
                            new AuthException("OAuth error: " + error));
                } else if (code != null) {
                    responseHtml = buildHtmlPage("Authentication Successful",
                            "You are now authenticated. You can close this tab and return to your editor.");
                    codeFuture.complete(code);
                } else {
                    responseHtml = buildHtmlPage("Authentication Failed",
                            "No authorization code received. Please try again.");
                    codeFuture.completeExceptionally(
                            new AuthException("No authorization code in callback"));
                }

                final var bytes = responseHtml.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
                exchange.sendResponseHeaders(200, bytes.length);
                try (var os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            } catch (Exception ex) {
                codeFuture.completeExceptionally(ex);
            } finally {
                // Shut down after handling the single callback
                server.stop(1);
            }
        });

        server.setExecutor(null);
        server.start();

        try {
            return codeFuture.get(TIMEOUT_MINUTES, TimeUnit.MINUTES);
        } catch (TimeoutException ex) {
            throw new AuthException("OAuth login timed out after " + TIMEOUT_MINUTES
                    + " minutes. Please try again.");
        } catch (java.util.concurrent.ExecutionException ex) {
            if (ex.getCause() instanceof AuthException authEx) {
                throw authEx;
            }
            throw new AuthException("OAuth callback failed: " + ex.getMessage());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new AuthException("OAuth login interrupted");
        } finally {
            server.stop(0);
        }
    }

    /**
     * Stops the callback server if it's running.
     */
    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    private static String extractParam(final String query, final String name) {
        if (query == null) return null;
        for (final var pair : query.split("&")) {
            final var parts = pair.split("=", 2);
            if (parts.length == 2 && parts[0].equals(name)) {
                return java.net.URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    private static String buildHtmlPage(final String title, final String message) {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head><meta charset="utf-8"><title>%s</title>
                <style>body{font-family:system-ui,sans-serif;display:flex;justify-content:center;\
                align-items:center;min-height:100vh;margin:0;background:#f5f5f5;}\
                .card{background:white;padding:2rem 3rem;border-radius:12px;box-shadow:0 2px 10px rgba(0,0,0,.1);\
                text-align:center;max-width:500px;}\
                h1{color:#0052CC;margin-bottom:.5rem;}\
                p{color:#505F79;line-height:1.6;}</style></head>
                <body><div class="card"><h1>%s</h1><p>%s</p></div></body></html>
                """.formatted(title, title, message);
    }
}
