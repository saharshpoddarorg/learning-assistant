# JQL & CQL Cheatsheet

Load this file when constructing JQL (Jira Query Language) or CQL (Confluence Query Language) queries.

---

## JQL — Jira Query Language

### My Work

```text
# My open issues
assignee = currentUser() AND status != Done ORDER BY updated DESC

# My issues updated today
assignee = currentUser() AND updated >= startOfDay() ORDER BY updated DESC

# Issues I reported
reporter = currentUser() AND status != Done ORDER BY created DESC

# Issues I'm watching
watcher = currentUser() ORDER BY updated DESC

# My issues due this week
assignee = currentUser() AND duedate >= startOfWeek() AND duedate <= endOfWeek() ORDER BY duedate ASC
```

### Sprint & Board

```text
# Current sprint backlog
sprint in openSprints() AND project = PROJ ORDER BY rank ASC

# Sprint backlog by status
sprint in openSprints() AND project = PROJ ORDER BY status ASC, rank ASC

# Unfinished items from closed sprints (spillover)
sprint in closedSprints() AND status != Done AND project = PROJ ORDER BY updated DESC

# Items not in any sprint (true backlog)
sprint is EMPTY AND project = PROJ AND status != Done ORDER BY priority ASC, created DESC

# Sprint burndown candidates (in progress)
sprint in openSprints() AND status = "In Progress" AND project = PROJ ORDER BY rank ASC
```

### Epic & Hierarchy

```text
# Issues in an epic
"Epic Link" = PROJ-100 ORDER BY rank ASC

# Subtasks of an issue
parent = PROJ-200 ORDER BY created ASC

# Epics with open issues
issuetype = Epic AND project = PROJ AND status != Done ORDER BY rank ASC

# All issues under an epic including subtasks
"Epic Link" = PROJ-100 OR parent in issuesFromEpic(PROJ-100) ORDER BY rank ASC
```

### Triage & Prioritization

```text
# Unassigned issues
assignee is EMPTY AND project = PROJ AND status != Done ORDER BY priority ASC, created DESC

# Overdue issues
duedate < now() AND status != Done ORDER BY duedate ASC

# Blockers and criticals
priority in (Blocker, Critical) AND status != Done ORDER BY priority ASC, updated DESC

# Bugs created this week
issuetype = Bug AND created >= startOfWeek() AND project = PROJ ORDER BY created DESC

# Issues with no labels (needs triage)
labels is EMPTY AND project = PROJ AND status != Done ORDER BY created DESC

# Issues without components
component is EMPTY AND project = PROJ AND status != Done ORDER BY created DESC

# Issues without estimates
originalEstimate is EMPTY AND project = PROJ AND status != Done ORDER BY priority ASC
```

### Time & Date

```text
# Created this week
created >= startOfWeek() AND project = PROJ ORDER BY created DESC

# Updated in last 24 hours
updated >= -24h AND project = PROJ ORDER BY updated DESC

# Created in last 30 days
created >= -30d AND project = PROJ ORDER BY created DESC

# Resolved this month
resolved >= startOfMonth() AND project = PROJ ORDER BY resolved DESC

# Due in next 7 days
duedate >= now() AND duedate <= 7d AND status != Done ORDER BY duedate ASC
```

### Search & Text

```text
# Text search in summary
summary ~ "deployment" AND project = PROJ ORDER BY updated DESC

# Text search in description
description ~ "API endpoint" AND project = PROJ ORDER BY updated DESC

# Summary or description contains text
text ~ "memory leak" AND project = PROJ ORDER BY updated DESC

# Issues with specific label
labels = "backend" AND project = PROJ AND status != Done ORDER BY priority ASC

# Issues with specific component
component = "API" AND project = PROJ ORDER BY updated DESC
```

### Release & Version

```text
# Issues in a release
fixVersion = "v2.1.0" AND project = PROJ ORDER BY issuetype ASC, priority ASC

# Issues not assigned to any release
fixVersion is EMPTY AND project = PROJ AND status != Done ORDER BY priority ASC

# Issues affecting a version
affectedVersion = "v2.0.0" AND project = PROJ ORDER BY priority ASC

# Release progress (done vs total)
fixVersion = "v2.1.0" AND project = PROJ ORDER BY status DESC, priority ASC
```

### Cross-Project & Advanced

```text
# Issues across multiple projects
project in (PROJ, TEAM, INFRA) AND status != Done ORDER BY updated DESC

# Issues linked to a specific issue
issueFunction in linkedIssuesOf("PROJ-100") ORDER BY priority ASC

# Recently transitioned issues
status CHANGED DURING (startOfWeek(), now()) AND project = PROJ ORDER BY updated DESC

# Issues assigned to a specific user
assignee = "john.doe" AND project = PROJ AND status != Done ORDER BY priority ASC

# Issues with worklogs this week
worklogDate >= startOfWeek() AND project = PROJ ORDER BY updated DESC
```

### JQL Functions Reference

| Function | Returns | Example |
|---|---|---|
| `currentUser()` | Authenticated user | `assignee = currentUser()` |
| `now()` | Current timestamp | `created >= now("-7d")` |
| `startOfDay()` | Midnight today | `updated >= startOfDay()` |
| `startOfWeek()` | Monday 00:00 | `created >= startOfWeek()` |
| `startOfMonth()` | 1st of month 00:00 | `resolved >= startOfMonth()` |
| `endOfWeek()` | Sunday 23:59 | `duedate <= endOfWeek()` |
| `openSprints()` | Active sprint IDs | `sprint in openSprints()` |
| `closedSprints()` | Completed sprint IDs | `sprint in closedSprints()` |
| `EMPTY` | Null / unset | `assignee is EMPTY` |

### JQL Operators

| Operator | Meaning | Example |
|---|---|---|
| `=` | Exact match | `status = "In Progress"` |
| `!=` | Not equal | `status != Done` |
| `~` | Contains text | `summary ~ "deploy"` |
| `!~` | Does not contain | `summary !~ "test"` |
| `in` | In list | `priority in (Blocker, Critical)` |
| `not in` | Not in list | `status not in (Done, Closed)` |
| `is EMPTY` | Field is null | `assignee is EMPTY` |
| `is not EMPTY` | Field is set | `duedate is not EMPTY` |
| `>=` / `<=` | Comparison | `created >= -7d` |
| `CHANGED` | Field changed | `status CHANGED DURING (-1d, now())` |
| `WAS` | Field had value | `status WAS "In Progress"` |

---

## CQL — Confluence Query Language

### Page Search

```text
# Full-text search
text ~ "deployment guide" AND space = "ENG"

# Title search
title = "Sprint 24 Review" AND space = "ENG"

# Title contains
title ~ "architecture" AND space = "ENG" ORDER BY lastModified DESC

# Pages by label
label = "architecture" AND space = "ENG" ORDER BY lastModified DESC

# Pages with multiple labels
label = "architecture" AND label = "approved" AND space = "ENG"
```

### Date & Author

```text
# Recently modified pages
lastModified >= now("-7d") AND space = "ENG" ORDER BY lastModified DESC

# Pages I modified recently
contributor = currentUser() AND lastModified >= now("-7d") ORDER BY lastModified DESC

# Pages created in last 30 days
created >= now("-30d") AND type = page ORDER BY created DESC

# Pages by a specific author
creator = "john.doe" AND space = "ENG" ORDER BY created DESC

# Pages modified today
lastModified >= now("-1d") AND space = "ENG" ORDER BY lastModified DESC
```

### Content Type

```text
# Blog posts in a space
type = blogpost AND space = "ENG" ORDER BY created DESC

# Pages only (no blogs)
type = page AND space = "ENG" ORDER BY lastModified DESC

# Comments containing text
type = comment AND text ~ "action item" ORDER BY created DESC

# Attachments in a space
type = attachment AND space = "ENG" ORDER BY created DESC
```

### Space & Structure

```text
# All content in a space
space = "ENG" AND type = page ORDER BY title ASC

# Content across multiple spaces
space in ("ENG", "TEAM", "ARCH") AND label = "decision" ORDER BY lastModified DESC

# Pages under a specific parent
ancestor = 12345678 AND type = page ORDER BY title ASC

# Root-level pages in a space
space = "ENG" AND type = page AND ancestor is null ORDER BY title ASC
```

### CQL Operators Reference

| Operator | Meaning | Example |
|---|---|---|
| `=` | Exact match | `space = "ENG"` |
| `!=` | Not equal | `type != blogpost` |
| `~` | Contains text | `title ~ "sprint"` |
| `in` | In list | `space in ("ENG", "ARCH")` |
| `>=` / `<=` | Date comparison | `lastModified >= now("-7d")` |
| `AND` / `OR` | Logical operators | `label = "x" AND space = "ENG"` |
| `ORDER BY` | Sort results | `ORDER BY lastModified DESC` |
| `now()` | Current time | `created >= now("-30d")` |
