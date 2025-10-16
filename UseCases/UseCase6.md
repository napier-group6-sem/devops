# USE CASE: 6 Produce Capital City Population Reports
## CHARACTERISTIC INFORMATION
### Goal in Context
As a Data Engineer I want to create reports showing capital cities organized by population so that users can compare capital-city sizes across the world, continents, and regions.
### Scope
Population Analytics System.
### Level
Primary task.
### Preconditions
Current, validated datasets for countries, capitals, and city populations; consistent mapping country ↔ capital city.
Actor is authenticated with Data Engineer permissions.
### Success End Condition
A capital-cities report is available, ordered from largest to smallest population for the chosen scope (World / Continent / Region).
### Failed End Condition
No report is produced or the list is unavailable for the requested scope.
### Primary Actor
Data Engineer.
### Trigger
A stakeholder requests a comparative view of capital-city populations, or the actor runs the report ad-hoc/scheduled.

### MAIN SUCCESS SCENARIO
Actor opens Capital City Population Report.


Actor selects scope: World / Continent / Region (and the specific continent/region if applicable).


System validates parameters (scope/value exists) and dataset currency.


System resolves capitals by joining country↔capital mappings and the city population table.


System filters to capital cities for the chosen scope and sorts descending by population.


System renders the report (capital city, country, continent, region, population) and provides export (CSV/PDF).


System stores metadata (scope, value, timestamp, dataset version). Use case ends successfully.



### EXTENSIONS
E1 — Invalid scope/value (unknown continent/region):
System shows valid options → actor corrects → back to step 2.


E2 — Missing or inconsistent capital mapping:
System flags countries without a resolvable capital; excludes them and notes this in the report footer; continue from step 5.


E3 — Dataset not current/unavailable:
System prompts to refresh datasets (country list, capital mapping, city populations); after success, resume at step 4.


E4 — No capital cities for selection:
System shows “no data for selection”; actor adjusts scope/value.



### SUB-VARIATIONS
Tie-break rule for identical populations: secondary sort by capital name (A→Z).


Optional Top-N mode (10/25/50/All).


Optional extra columns: latitude/longitude, government type (if available).



### SCHEDULE
DUE DATE: Release 1.0