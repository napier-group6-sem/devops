# USE CASE: 12 Produce City Master Report
## CHARACTERISTIC INFORMATION
### Goal in Context
As a Data Engineer I want to generate a city report so that users can see key information for each city: Name, Country, District, Population.
### Scope
Population Analytics System.
### Level
Primary task.
### Preconditions
Current, validated datasets for cities, country/district references, and city populations (same data version).
Actor is authenticated with Data Engineer permissions.
### Success End Condition
A report listing all cities in the world with the required columns (Name, Country, District, Population) is available.
### Failed End Condition
No report is produced (missing data, validation failure).
### Primary Actor
Data Engineer.
### Trigger
Stakeholder requests a master city listing, or the actor runs/schedules the report.

### MAIN SUCCESS SCENARIO
Actor opens City Master Report.


System loads current reference datasets (cities, countries, districts, populations) and validates version consistency.


System joins city data with its country and district, and attaches population.


System renders a table with columns: Name, Country, District, Population for all cities.


System applies a stable sort (e.g., Population desc or Name A→Z, per configuration).


System offers export (CSV/PDF) and stores metadata (timestamp, dataset version). Use case ends successfully.



### EXTENSIONS
E1 — Missing district for some cities:
System leaves District blank and flags the row in a Notes column/footer; proceeds.


E2 — Missing population value:
System marks Population = N/A, flags data quality; proceeds with other rows.


E3 — Version mismatch between datasets:
System blocks output, asks to align to a single version; resume at Step 2 after correction.


E4 — Export failure (I/O/network):
System logs error, offers retry or an alternative format.



### SUB-VARIATIONS
Optional columns: latitude/longitude, city type (capital flag), administrative level.


Optional filters: by country/region/continent (default is World = all cities).



### SCHEDULE
DUE DATE: Release 1.0
