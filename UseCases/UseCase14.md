# USE CASE: 14 Produce Population Breakdown Table (with Percentages)
## CHARACTERISTIC INFORMATION
### Goal in Context
As a Data Engineer I want to generate population reports so that users can see Total Population, Population Living in Cities (with %), and Population Not Living in Cities (with %) for continents, regions, and countries.
### Scope
Population Analytics System.
### Level
Primary task.
### Preconditions
Current, validated datasets providing total population and urban (in-cities) population per geography; taxonomy for continents → regions → countries.
Actor is authenticated with Data Engineer permissions.
### Success End Condition
A report is available for the selected level showing columns: Name (continent/region/country), Total Population, Population Living in Cities (%), Population Not Living in Cities (%).
### Failed End Condition
No report is produced (invalid parameters or unavailable/inconsistent data).
### Primary Actor
Data Engineer.
### Trigger
Stakeholder requests population composition by geography, or the actor runs/schedules the report.

### MAIN SUCCESS SCENARIO
Actor opens Population Breakdown (with %) report.


Actor selects level: Continent / Region / Country (and the specific selection if applicable).


System validates parameters and dataset currency/version alignment.


System aggregates for each listed item at the chosen level:


Total = total population.


In Cities = urban population.


Not in Cities = Total − In Cities (never negative).


% In Cities = (In Cities / Total) × 100; % Not in Cities = 100 − % In Cities.


System renders a table with columns: Name, Total Population, Population Living in Cities, % In Cities, Population Not Living in Cities, % Not in Cities.


System applies stable sorting (e.g., Total desc) and provides export (CSV/PDF) with metadata (level, selection, timestamp, dataset version). Use case ends successfully.



### EXTENSIONS
E1 — Missing urban figure for some items:
System flags rows, treats missing as N/A, and excludes them from % calculations (or uses configured imputation); note in footer.


E2 — Urban figure exceeds Total due to rounding/source error:
System clamps Not in Cities to 0 and recomputes %; records a data-quality warning.


E3 — Invalid level/selection:
System shows valid options and prompts correction → back to Step 2.


E4 — Dataset not current or version mismatch:
System prompts to refresh/align datasets; after success, resume at Step 4.


E5 — Empty result for selection:
System shows “no data for selection” and offers to change level/filters.



### SUB-VARIATIONS
Optional chart output (stacked bars by item).


Optional display of each item’s share of world (% of global population).


Batch mode: produce all continents and/or all regions in one export (multi-sheet).



### SCHEDULE
DUE DATE: Release 1.0
