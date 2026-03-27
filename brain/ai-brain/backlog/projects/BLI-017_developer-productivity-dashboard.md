---
id: BLI-017
title: Developer productivity dashboard
status: todo
priority: low
type: feature
created: 2026-03-28
updated: 2026-03-28
epic: EPIC-003
parent: null
sub-items: []
origin: null
tags: [project:dev-dashboard, productivity, metrics, github, coding-stats, dashboard]
---

# BLI-017: Developer productivity dashboard

## Description

Build a personal developer productivity dashboard that aggregates coding activity,
GitHub contributions, learning progress, build health, and personal KPIs into a
single view. Track time spent coding, commits per week, learning streak, books
read, resources consumed, and goals progress. Integrate with GitHub API, JIRA/ADO,
and the brain workspace.

## Acceptance Criteria

- [ ] GitHub activity feed — commits, PRs, issues, reviews aggregated per day/week/month
- [ ] Coding time tracking — integrate with WakaTime or custom IDE plugin
- [ ] Learning streak tracker — track daily learning activity (resources consumed, notes taken)
- [ ] Goal setting and progress — define quarterly/monthly goals with measurable KPIs
- [ ] Build health widget — show recent build pass/fail status from CI/CD
- [ ] Reading progress widget — books in-progress, recently completed (links to BLI-013)
- [ ] Brain workspace metrics — notes created, sessions captured, backlog items completed
- [ ] Weekly summary email/report — auto-generate a weekly productivity summary
- [ ] Heatmap visualization — GitHub-style contribution heatmap for all activities
- [ ] Customizable widgets — add/remove/rearrange dashboard components
- [ ] REST API backend — expose metrics as JSON for custom integrations
- [ ] Offline-first — works without internet, syncs when connected

## Notes

- GitHub REST/GraphQL API — contribution data, repo stats, PR metrics
- WakaTime API — coding time tracking (free tier: 2 weeks history)
- Consider Grafana + InfluxDB for time-series metrics and dashboard rendering
- Alternative: custom React dashboard with Recharts or Chart.js
- Privacy: all personal metrics stored locally, no third-party analytics
- Could integrate with Copilot — "how productive was I this week?"
