# USE CASE: 3 Produce Top-N Country Population Reports

## CHARACTERISTIC INFORMATION
### Goal in Context
As a Data Engineer I want to generate reports showing the top N most populated countries so that users can focus on the largest countries by population globally, by continent, or by region.
### Scope

Population Analytics System.
### Level

Primary task.
### Preconditions

We have a current, validated country–population dataset and geography taxonomy (world → continents → regions).
Actor is authenticated with Data Engineer permissions.
### Success End Condition
A ranked report of the top N countries is available for the selected scope (World / a Continent / a Region).
### Failed End Condition
No report is produced (invalid N, invalid scope/value, or dataset unavailable).
### Primary Actor
Data Engineer.
### Trigger
A stakeholder requests a top-N view, or the Data Engineer initiates ad-hoc/scheduled generation.

### MAIN SUCCESS SCENARIO
Actor opens Top-N Country Population report.


Actor selects scope: World / Continent / Region and provides N (positive integer).


System validates parameters (N ≥ 1; scope/value exists) and dataset currency.


System retrieves matching countries and sorts by population (descending).


System selects top N and renders the list (country, code, continent, region, population).


System provides view, pagination (if needed), and export options (CSV/PDF). Use case ends successfully.



### EXTENSIONS
E1 — Invalid N (≤0 / non-numeric / too large):
System shows allowed range (e.g., 1–250) and asks to correct → back to step 2.


E2 — Invalid scope/value (continent/region not found):
System suggests valid options → back to step 2.


E3 — Dataset not current/unavailable:
System prompts to refresh datasets; after success, resume at step 4.


E4 — Fewer than N countries in scope:
System returns all available countries and notes the shortfall in the footer.



### SUB-VARIATIONS
Tie-break rule for equal population: secondary sort by country name (ascending).


Optional columns: capital, share of world/continent (%).


Optional “Top N per continent/region” batch mode (one artifact per scope).



### SCHEDULE
DUE DATE: Release 1.0
