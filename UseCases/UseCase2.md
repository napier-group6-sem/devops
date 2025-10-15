# USE CASE: 2 Produce Country Population Reports
## CHARACTERISTIC INFORMATION

### Goal in Context

As a Data Engineer I want to create reports on countries organised by population so that users can easily view and compare population data by world, continent, and region.
### Scope

Population Analytics System.

### Level

Primary task.

### Preconditions
We have a current, validated country–population dataset and geography taxonomy (world → continents → regions).
User is authenticated with Data Engineer permissions.

### Success End Condition

A country list/report is available, ordered from largest to smallest population for world, any selected continent, or any selected region.

### Failed End Condition

No report is produced or the list is unavailable for the requested scope.

### Primary Actor

Data Engineer.

### Trigger

A stakeholder requests ranked country population data, or the Data Engineer initiates an ad-hoc/scheduled generation.

### MAIN SUCCESS SCENARIO

Data Engineer opens the reporting module and selects Country Population Report.


Data Engineer chooses the scope: World, Continent, or Region.


(If Continent/Region) Data Engineer selects the target continent or region from the reference list.


System validates the parameters (scope exists, names are valid, dataset is current).


System retrieves the relevant country records for the chosen scope.


System sorts countries descending by population (largest → smallest).


System renders the report (country name, code, continent, region, population), matching the acceptance criteria.


System offers search/pagination and optional export (CSV/PDF).


System stores the report artifact with metadata (scope, timestamp, dataset version) and presents a link for viewing/downloading.


Use case ends successfully.


### EXTENSIONS
Continent not found / invalid name:
System shows a validation error and suggests valid continents; user reselects (returns to Step 3).


Region not found / invalid name:
System shows a validation error and suggests valid regions; user reselects (returns to Step 3).


No countries for selected scope (empty result):
System shows “no data for scope” message; user may change filters or cancel.


Dataset not current / unavailable:
System prompts to refresh datasets; after successful refresh, resume at Step 5.


Export failure (I/O/network):
System logs error, offers retry or alternative format; report remains viewable online.


Population ties:
System applies a stable secondary sort (e.g., alphabetical by country) and notes the rule in the footer.


### SUB-VARIATIONS
Export format: CSV or PDF.


Optional top-N filter (Top 10/25/50) or “All” countries.


Optional extra columns (capital, % of world/continent total).


### SCHEDULE
DUE DATE: Release 1.0
