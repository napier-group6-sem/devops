# USE CASE: 11 Produce Country Master Report
## CHARACTERISTIC INFORMATION
### Goal in Context
As a Data Engineer I want to generate a country report so that users can see key information for each country: Code, Name, Continent, Region, Population, Capital.
### Scope
Population Analytics System.
### Level
Primary task.
### Preconditions
Current, validated datasets for countries, country→continent/region taxonomy, population, and country↔capital mapping.
Actor is authenticated with Data Engineer permissions.
### Success End Condition
A report listing all countries in the world with the required columns (Code, Name, Continent, Region, Population, Capital) is available.
### Failed End Condition
No report is produced (missing data, validation failure).
### Primary Actor
Data Engineer.
### Trigger
Stakeholder requests a master country listing, or the actor runs/schedules the report.

### MAIN SUCCESS SCENARIO
Actor opens Country Master Report.


System loads the current reference datasets (countries, taxonomy, capitals, population) and validates version consistency.


System joins country data with continent/region, population, and capital.


System renders a table with columns: Code, Name, Continent, Region, Population, Capital for all countries.


System applies stable sorting (e.g., Name A→Z or Population desc as configured).


System provides export (CSV/PDF) and stores metadata (timestamp, dataset version). Use case ends successfully.



### EXTENSIONS
E1 — Missing capital for some countries:
System leaves Capital blank for those rows, flags them in a Notes column/footer, and proceeds.


E2 — Missing population value:
System marks population as N/A and flags data quality; proceeds with other rows.


E3 — Version mismatch between datasets:
System blocks output, asks to align datasets to the same version; resume at Step 2 after correction.


E4 — Export failure:
System logs error and offers retry or alternative format.



### SUB-VARIATIONS
Optional secondary columns: ISO3, area (km²), density, currency (if available).


Optional filters: by continent/region (default is World = all countries).



### SCHEDULE
DUE DATE: Release 1.0
