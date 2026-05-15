# brain-models

Java models for digital notetaking and knowledge management.

## Purpose

Defines the metadata types used by the brain knowledge workspace — note kinds,
statuses, templates, and metadata records. These are standalone data models with
no external dependencies.

## Package Structure

```text
digitalnotetaking/
├── NoteKind.java        Enum: book, article, reference, session, etc.
├── NoteMetadata.java    Record: title, kind, status, tags, timestamps
├── NoteStatus.java      Enum: draft, published, archived
├── NoteTemplate.java    Enum: template types for note creation
└── Main.java            Entry point for standalone usage
```

## Dependencies

None — pure Java 21+.

## Build

```bash
./gradlew :modules:brain-models:build
```
