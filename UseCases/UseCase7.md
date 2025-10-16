# USE CASE: 7 Produce Top-N Capital City Population Reports
## CHARACTERISTIC INFORMATION
### Goal in Context
As a Data Engineer I want to generate reports of the top N populated capital cities so that users can quickly see the largest capital cities globally, by continent, or by region.
### Scope
Population Analytics System.
### Level
Primary task.
### Preconditions
Current, validated datasets for countries, capital mappings, and city populations.
Actor is authenticated with Data Engineer permissions.
## Success End Condition
A ranked Top-N capital-cities report is available for the selected scope (World / Continent / Region).
### Failed End Condition
No report is produced (invalid N, invalid scope/value, or datasets unavailable/inconsistent).
### Primary Actor
Data Engineer.
### Trigger
Stakeholder asks for a Top-N capital-cities view, or the actor runs it ad-hoc/scheduled.

### MAIN SUCCESS SCENARIO
Actor opens Top-N Capital City Population report.


Actor selects scope (World / Continent / Region), provides the value if needed (e.g., “Europe”), and enters N (positive integer).


System validates parameters (N ≥ 1; scope/value exist) and confirms dataset currency.


System resolves capitals (country ↔ capital join) and retrieves matching capital cities for the chosen scope.


System sorts by population (desc), selects Top-N, and renders (capital, country, continent, region, population).


System offers export (CSV/PDF) and stores metadata (scope/value, N, timestamp, dataset version). Use case ends successfully.



### EXTENSIONS
E1 — Invalid N (≤0 / non-numeric / exceeds limit):
System displays allowed range (e.g., 1–500) → actor corrects → back to Step 2.


E2 — Invalid scope/value (unknown continent/region):
System suggests valid options → back to Step 2.


E3 — Missing/inconsistent capital mapping for some countries:
System excludes unresolved entries, flags them in a notice/footer, proceeds with available data (resume at Step 5).


E4 — Dataset not current/unavailable:
System prompts to refresh datasets; after success, resume at Step 4.


E5 — Fewer than N capital cities available in scope:
System returns all available capitals and notes the shortfall.



### SUB-VARIATIONS
Tie-break rule for equal population: secondary sort by capital name (A→Z).


Batch mode: Top-N per each continent/region (multiple sheets/files).


Optional columns: latitude/longitude, government type (if available).



### SCHEDULE
DUE DATE: Release 1.0
