# USE CASE: 13 Produce Capital City Master Report
## CHARACTERISTIC INFORMATION
### Goal in Context
As a Data Engineer I want to generate a capital city report so that users can see key information for each capital city: Name, Country, Population.
### Scope
Population Analytics System.
### Level
Primary task.
### Preconditions
Current, validated datasets for countries, country↔capital mapping, and city populations (same data version).
Actor is authenticated with Data Engineer permissions.
### Success End Condition
A report listing all capital cities in the world with columns Name, Country, Population is available.
### Failed End Condition
No report is produced (missing/inconsistent mappings or population data, validation failure).
### Primary Actor
Data Engineer.
### Trigger
Stakeholder requests a master capital-cities listing, or the actor runs/schedules the report.

### MAIN SUCCESS SCENARIO
Actor opens Capital City Master Report.


System loads current country list, capital mapping, and city population datasets and validates version consistency.


System resolves capitals via country↔capital mapping and joins population for each capital city.


System renders a table with columns: Name (Capital City), Country, Population for all capitals.


System applies a stable sort (e.g., Population desc or Name A→Z, per configuration).


System provides export (CSV/PDF) and stores metadata (timestamp, dataset version). Use case ends successfully.



### EXTENSIONS
E1 — Country has no resolvable capital (missing/invalid mapping):
System excludes that row, flags it in a Notes section/footer, and proceeds.


E2 — Missing population for a capital city:
System marks Population = N/A, flags data quality; proceeds with other rows.


E3 — Dataset version mismatch (countries vs capitals vs populations):
System blocks output and requests alignment to a single version; resume at Step 2 after correction.


E4 — Export failure:
System logs the error and offers retry or an alternative format.



### SUB-VARIATIONS
Optional columns: latitude/longitude, government seat indicator (if different from constitutional capital).


Optional filters: by continent/region (default is World = all capitals).



### SCHEDULE
DUE DATE: Release 1.0
