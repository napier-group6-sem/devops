# USE CASE: 10 Produce Key-Language Speakers Report
## CHARACTERISTIC INFORMATION
### Goal in Context
As a Data Engineer I want to generate reports on the number of people speaking key languages so that users can see the number of speakers of Chinese, English, Hindi, Spanish, and Arabic, along with each language’s percentage of the world population.
### Scope
Population Analytics System.
### Level
Primary task.
### Preconditions
Current, validated datasets for language speaker counts and a world population baseline (same version/date).
Actor is authenticated with Data Engineer permissions.
### Success End Condition
A report is available listing the five key languages with speaker counts and % of world population, sorted descending by speakers.
### Failed End Condition
No report is produced (missing baseline or language data, validation failure).
### Primary Actor
Data Engineer.
### Trigger
Stakeholder requests key-language statistics, or the actor runs/schedules the report.

### MAIN SUCCESS SCENARIO
Actor opens Key-Language Speakers report.


System preloads the five languages (Chinese, English, Hindi, Spanish, Arabic) and the current world baseline.


System validates that speaker counts and baseline belong to the same data version.


System retrieves speaker counts for the five languages.


System computes percentages = (language speakers / world population) × 100.


System sorts languages by speakers (desc) and renders (language, speakers, % of world).


System offers export (CSV/PDF) and stores metadata (baseline version, timestamp). Use case ends successfully.



### EXTENSIONS
E1 — Missing world baseline:
System prompts to refresh datasets (UC-09) or select a baseline; after success, resume at Step 3.


E2 — Missing/invalid speaker count for a language:
System flags the language, omits or marks it as unavailable, and proceeds with remaining items; a notice is added.


E3 — Version mismatch (baseline vs speakers):
System blocks output, requests alignment to one version; resume at Step 3 after correction.


E4 — Rounding/overlap note:
System adds a footnote that percentages need not sum to 100% due to multilingual speakers and rounding.



### SUB-VARIATIONS
Optional include additional languages via parameter (kept separate from the “key five”).


Optional columns: data source, methodology, confidence/quality flags.


Optional chart output (bar chart) accompanying the table.



### SCHEDULE
DUE DATE: Release 1.0
