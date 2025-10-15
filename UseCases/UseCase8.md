# USE CASE: 8 Produce Population Breakdown Reports
## CHARACTERISTIC INFORMATION
### Goal in Context
As a Data Engineer I want to generate population-breakdown reports so that users can see total population, population living in cities, and population not living in cities for continents, regions, and countries.
### Scope
Population Analytics System.
### Level
Primary task.
### Preconditions
Current, validated datasets with fields for total population and population living in cities (or equivalent).
Actor is authenticated with Data Engineer permissions.
Geography taxonomy (continents → regions → countries) is available.
### Success End Condition
A report is available for the selected geographic level showing: Total, In Cities, Not in Cities, and (optionally) percentages.
### Failed End Condition
No report is produced (invalid parameters or datasets unavailable/inconsistent).
### Primary Actor
Data Engineer.
### Trigger
Stakeholder requests population breakdown or the actor runs/schedules the report.

### MAIN SUCCESS SCENARIO
Actor opens Population Breakdown report.


Actor selects level: Continent / Region / Country (and the specific item if applicable).


System validates parameters and confirms dataset currency.


System aggregates the selected scope:


Total = sum of total population.


In Cities = sum of urban population.


Not in Cities = Total − In Cities (floored at 0 if rounding).


System renders a table (name, Total, In Cities, Not in Cities, optional % in cities / % not in cities) and applies stable sorting (e.g., by Total desc).


System offers export (CSV/PDF) and stores metadata (level, selection, timestamp, dataset version). Use case ends successfully.



### EXTENSIONS
E1 — Missing urban component for some records:
System excludes or imputes (per configuration), flags affected rows in a notes column, and continues aggregation.


E2 — In Cities > Total due to source rounding:
System clamps Not in Cities to 0, logs a data-quality warning, and displays a footnote.


E3 — Invalid level/value:
System shows valid options; actor corrects → back to Step 2.


E4 — Dataset not current/unavailable:
System prompts to refresh datasets; after success, resume at Step 4.


E5 — Empty selection (no members in scope):
System shows “no data for selection”; actor adjusts level/value.



### SUB-VARIATIONS
Optional % columns: (In Cities / Total) and (Not in Cities / Total).


Optional charts (bar or stacked) per continent/region.


Batch mode: output all continents or all regions in one run (multi-sheet export).



### SCHEDULE
DUE DATE: Release 1.0