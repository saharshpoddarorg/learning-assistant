/**
 * Digital note-taking support for the Brain module.
 *
 * <p>Provides the data model and template engine that mirror the frontmatter schema
 * used by the {@code brain/ai-brain/} workspace (inbox, notes, archive tiers).
 *
 * <p>Core types:
 * <ul>
 *   <li>{@link digitalnotetaking.NoteKind} — enum for note kinds (note, decision,
 *       session, resource, snippet, ref)</li>
 *   <li>{@link digitalnotetaking.NoteMetadata} — immutable record for a note's
 *       frontmatter (date, kind, project, tags, status)</li>
 *   <li>{@link digitalnotetaking.NoteTemplate} — factory that generates Markdown
 *       note templates for each {@link digitalnotetaking.NoteKind}</li>
 * </ul>
 *
 * <p>Intended uses:
 * <ul>
 *   <li>Validate note frontmatter during a {@code brain publish} operation</li>
 *   <li>Generate templated notes via {@code brain new}</li>
 *   <li>Build a search index across inbox/notes/archive tiers</li>
 * </ul>
 *
 * <p>Documentation hub: {@code brain/digitalnotetaking/README.md}
 * <p>Knowledge workspace: {@code brain/ai-brain/}
 */
package digitalnotetaking;
