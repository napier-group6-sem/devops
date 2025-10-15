# USE CASE: 4 Produce City Population Reports
## CHARACTERISTIC INFORMATION
### Goal in Context
As a Data Engineer I want to create reports showing cities organized by population so that users can analyze population distribution across world, continent, region, country, and district levels.
### Scope
Population Analytics System.
### Level
Primary task.
### Preconditions
Current, validated city–population dataset and geo taxonomy (world → continents → regions → countries → districts).
Actor is authenticated with Data Engineer permissions.
### Success End Condition
A city list/report is available, ordered from largest to smallest population for the selected geographic level.
### Failed End Condition
No report is produced or data is unavailable for the requested level.
### Primary Actor
Data Engineer.
### Trigger
A stakeholder requests ranked city population data, or the Data Engineer runs the report ad-hoc/scheduled.

### MAIN SUCCESS SCENARIO
Actor opens City Population Report.


Actor selects level: World / Continent / Region / Country / District (and, if needed, the specific value).


System validates parameters (level/value exists) and dataset currency.


System retrieves cities matching the chosen level/value.


System sorts descending by population and renders the list (city, country, district/region, population).


System provides view & export (CSV/PDF) and records metadata (level, value, timestamp, dataset version). Use case ends successfully.



### EXTENSIONS
E1 — Invalid level or value (e.g., unknown region/district):
System shows valid options and prompts correction → back to Step 2.


E2 — Dataset not current/unavailable:
System prompts to refresh datasets; after success, resume at Step 4.


E3 — No cities found for the selection:
System displays “no data for selection” and allows the actor to adjust the level/value.



### SUB-VARIATIONS
Tie-breaker for equal population: secondary sort by city name (ascending).


Optional Top-N filter (10/25/50/All).


Optional extra columns (capital flag, latitude/longitude, % of country/region population).



### SCHEDULE
DUE DATE: Release 1.0
