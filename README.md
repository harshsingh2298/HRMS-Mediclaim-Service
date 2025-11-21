# HRMS-Mediclaim-Service
Build a Mediclaim Enrollment module that integrates into onboarding and standalone access.
Overview & goals
Goal: Build a Mediclaim Enrollment module that integrates into onboarding and standalone access. Key flows:
•	User chooses Opt In or Opt Out.
•	If Opt In → show Policy Highlights; user must check “I have read” to continue → then open Enrollment Form pre-populated with user data.
•	If Opt Out → record selection immediately.
•	Add/manage dependents with strict policy rules (DOB checks, twins logic, parents vs in-laws, age limits).
•	Compute approximate premium on the fly and show before final submit.
•	Send notifications (email) from CMS Admin on confirmation and to HR when final submitted.
•	HR front-end for enrollment window control, upload new users, view reports, extend period, approve/reject.
•	Reports for HR (per slides 10–15 reference). (We’ll create the meaningful report endpoints.)
Non-functional: Secure, auditable, usable on mobile, accessible, covered by automated tests, deployable via CI/CD.

