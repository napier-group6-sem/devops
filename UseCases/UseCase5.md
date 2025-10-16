# USE CASE: 5 Produce Top-N City Population Reports
## CHARACTERISTIC INFORMATION
### Goal in Context
As a Data Engineer I want to generate reports showing the top N most populated cities so that users can quickly identify the largest urban areas at world, continent, region, country, or district levels.
### Scope
Population Analytics System.
### Level
Primary task.
### Preconditions
Current, validated city-population dataset and geo taxonomy (world → continents → regions → countries → districts).
Actor is authenticated with Data Engineer permissions.
### Success End Condition
A ranked Top-N cities report is available for the selected geographic level.
### Failed End Condition
No report produced (invalid N/level/value or dataset unavailable).
### Primary Actor
Data Engineer.
### Trigger
Stakeholder requests a Top-N view, or the Data Engineer initiates ad-hoc/scheduled generation.

### MAIN SUCCESS SCENARIO
Actor opens Top-N City Population report.


Actor selects level (World / Continent / Region / Country / District), specifies the value (if needed), and enters N (positive integer).


System validates parameters (N ≥ 1; level/value exist) and confirms dataset currency.


System retrieves matching cities and sorts descending by population.


System selects the top N and renders the list (city, country, region/district, population).


System provides view and export (CSV/PDF) and stores metadata (level, value, N, timestamp, dataset version). Use case ends successfully.



### EXTENSIONS
E1 — Invalid N (≤0 / non-numeric / exceeds limit):
System shows allowed range (e.g., 1–10 000) → actor corrects → back to step 2.


E2 — Invalid level or value (unknown continent/region/country/district):
System suggests valid options → back to step 2.


E3 — Dataset not current/unavailable:
System prompts to refresh datasets; after success, resume at step 4.


E4 — Fewer than N cities available in scope:
System returns all available cities and notes the shortfall in the footer.



### SUB-VARIATIONS
Tie-break rule for equal population: secondary sort by city name (ascending).


Optional columns: latitude/longitude, capital flag, share of country/region population.


Batch mode: Top N per each continent/region as multiple sheets/files.



### SCHEDULE
DUE DATE: Release 1.0
