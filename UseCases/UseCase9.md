# USE CASE: 9 Produce Population-by-Level Reports
## CHARACTERISTIC INFORMATION
### Goal in Context
As a Data Engineer I want to generate reports showing the population at different levels so that users can see the population of the world, continents, regions, countries, districts, and cities.
### Scope
Population Analytics System.
### Level
Primary task.
### Preconditions
Current, validated population datasets exist for all supported levels (world → continents → regions → countries → districts → cities).
Actor is authenticated with Data Engineer permissions.
### Success End Condition
A report is available that lists population figures for the selected level (and item, if applicable).
### Failed End Condition
No report is produced or data is incomplete for the requested level.
### Primary Actor
Data Engineer.
### Trigger
A stakeholder requests population by level, or the actor runs the report ad-hoc/scheduled.

### MAIN SUCCESS SCENARIO
Actor opens Population by Level report.


Actor selects level: World / Continent / Region / Country / District / City (and the specific value if required).


System validates parameters and confirms dataset currency/coverage for that level.


System retrieves/aggregates population for the chosen level:


World: total world population.


Continent/Region: list each item with its population.


Country/District/City: list each item under the selected parent (if applicable).


System renders the results (name, code/parent where relevant, population), applying a stable sort (e.g., population desc, then name asc).


System offers export (CSV/PDF) and stores metadata (level, selection, timestamp, dataset version). Use case ends successfully.



### EXTENSIONS
E1 — Invalid level/value (unknown or not supported):
System shows valid options; actor corrects → back to Step 2.


E2 — Dataset not current/unavailable for level:
System prompts to refresh datasets; after success, resume at Step 4.


E3 — Partial coverage (some members missing data):
System lists missing members in a notice/footer and proceeds with available data.


E4 — Empty result (no members for selection):
System shows “no data for selection”; actor adjusts parameters.



### SUB-VARIATIONS
Optional columns: parent (e.g., country for a city), level path, % share of parent/world.


Optional Top-N/filtering per level (e.g., top 50 cities globally).


Batch mode: produce a single artifact containing multiple levels (separate sheets/sections).



### SCHEDULE
DUE DATE: Release 1.0
