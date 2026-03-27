---
id: BLI-008
title: Scheduler, calendar, timeline & todo tracking
status: todo
priority: high
type: feature
created: 2026-03-28
updated: 2026-03-28
epic: EPIC-003
parent: null
sub-items: []
origin: null
tags: [scheduler, calendar, timeline, todo, tracking, productivity]
---

# BLI-008: Scheduler, calendar, timeline & todo tracking

## Description

Build scheduling and productivity tools for calendar management, timeline
visualization, todo tracking, and work progress monitoring. This could be an
MCP server, a standalone module, or a combination. Should integrate with
external calendar systems (Google Calendar, Outlook) and the backlog system
in this repo.

## Acceptance Criteria

- [ ] Calendar integration — read/write events from Google Calendar and/or Outlook
- [ ] Todo tracking system — create, update, complete, prioritize tasks
- [ ] Timeline/Gantt visualization — visual representation of scheduled work
- [ ] Work tracking dashboard — daily/weekly progress view across all tracked items
- [ ] Reminder/notification system — alert for upcoming deadlines and meetings
- [ ] Recurring task support — daily standup, weekly review, monthly planning
- [ ] Integration with backlog — sync BLI items as tracked todos
- [ ] Time blocking — schedule focused work sessions on calendar
- [ ] Daily/weekly review template — structured reflection on completed work
- [ ] Export capabilities — generate reports, summaries for sharing

## Notes

- Google Calendar API and Microsoft Graph API for calendar integration
- Could start as a simple CLI/MCP tool, evolve into a web dashboard
- Consider integration with the brain workspace for note-taking during reviews
- Pomodoro timer could be a nice sub-feature
- Look at Todoist API, TickTick API for inspiration
