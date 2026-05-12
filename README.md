# Test Automation Framework

A layered, multi-platform test automation framework supporting REST API and Mobile (Android/iOS) test execution with BDD-style scenarios.

## Tech Stack

| Component         | Technology                              |
|-------------------|-----------------------------------------|
| Language          | Java 17                                 |
| Build             | Maven 3.x                               |
| Test Runner       | TestNG 7.11 + Cucumber 7.12             |
| API Testing       | REST Assured 5.3                        |
| Mobile Testing    | Appium 9.4 + Selenium 4.33              |
| Device Farm       | BrowserStack (SDK 1.56.5)               |
| DI                | Spring Context 7                        |
| Reporting         | ExtentReports (Spark HTML)              |
| Logging           | Log4j2 2.20                             |
| Static Analysis   | SpotBugs, Checkstyle                    |
| CI/CD             | GitLab CI                               |

## Prerequisites

- **JDK 17** or higher
- **Maven 3.8+**
- **BrowserStack account** (for mobile execution) with `userName` and `accessKey`
- **JWT private key** exported as `JWT_PRIVAT_KEY` env var (Base64-encoded PKCS8 RSA key) for API tests against UAT

## Project Structure

```
project-root/
├── feature/                          # Cucumber .feature files
│   ├── api/                          #   API scenarios by domain
│   │   └── accounts/
│   └── mobile/                       #   Mobile scenarios by suite
│       └── login/
├── src/
│   ├── main/
│   │   ├── java/project/
│   │   │   ├── api/                  # API utilities (REST Assured, JWT, HMAC)
│   │   │   ├── common/              # Thread-safe Context & YAML config parser
│   │   │   ├── enums/               # Environments, Platforms, RunContexts
│   │   │   └── mobile/              # Appium DriverInstance (ThreadLocal)
│   │   └── resources/
│   │       ├── config.yml            # Local run configuration
│   │       ├── testdata_*.yml        # Per-environment test data (test/uat/preprod/prod)
│   │       ├── translations.yml      # i18n assertion strings
│   │       ├── requests/             # JSON request templates
│   │       ├── schemas/              # JSON schemas for validation
│   │       ├── log4j2.properties     # Logging configuration
│   │       ├── extent.properties     # Report output settings
│   │       └── spark-config.xml      # ExtentReports theme/layout
│   └── test/
│       └── java/project/
│           ├── api/
│           │   ├── helpers/          # JSONHelper, ParametersHelper
│           │   └── steps/common/     # Reusable API step definitions
│           ├── common/               # ConfigReader (TestNG XML params)
│           ├── config/               # Spring AppConfig (platform beans)
│           ├── flows/                # RunFlow interface, CapabilityBuilder, CI/Local impls
│           ├── mobile/
│           │   ├── base/             # BasePage, Pages factory
│           │   ├── listeners/        # Retry analyzer, cleanup listener
│           │   ├── pages/            # Page Objects (POM)
│           │   └── steps/            # Mobile step definitions + hooks
│           ├── platform/             # Abstract Platform + Android/iOS drivers
│           ├── plugins/              # Cucumber InitializePlugin
│           ├── runner/               # TestNG-Cucumber runner classes
│           └── testngConfig/         # TestNG suite XML files
├── checkstyle.xml                    # Checkstyle rules (style & complexity)
├── pom.xml
└── .gitlab-ci.yml
```

## Architecture Layers

```
┌─────────────────────────────────────────────────────┐
│  TestNG Suite XML  (device matrix, parallel config) │
├─────────────────────────────────────────────────────┤
│  Runners  (APIRegressionRunner, MobileRunner)       │
├─────────────────────────────────────────────────────┤
│  Cucumber Hooks & Plugins  (lifecycle, env setup)   │
├─────────────────────────────────────────────────────┤
│  Step Definitions  (thin delegation layer)          │
├─────────────────────────────────────────────────────┤
│  Page Objects / API Helpers  (element + data logic) │
├─────────────────────────────────────────────────────┤
│  Framework Core  (Context, YamlParser, DriverInst.) │
└─────────────────────────────────────────────────────┘
```

### Key Design Decisions

- **`RunFlow`** is an interface with `LocalRunFlow` and `CIRunFlow` implementations, selected at runtime based on `RUN_CONTEXT`
- **`Platform`** is an abstract class with `Android`/`iOS` subclasses, resolved via Spring IoC beans
- **`CapabilityBuilder`** extracts shared Appium/BrowserStack capability setup, eliminating duplication between flow implementations
- **`Context`** uses `ThreadLocal` for all shared state, enabling safe parallel execution

## Running Tests

### Local Execution

**Default suite** (API regression via `localRegression.xml`):

```bash
mvn test
```

**Specific suite**:

```bash
# API regression (parallel, thread-count=4)
mvn test -DsuiteXmlFile=src/test/java/project/testngConfig/apiRegression.xml

# Mobile regression (5 devices in parallel)
mvn test -DsuiteXmlFile=src/test/java/project/testngConfig/mobileRegression.xml
```

**Filter by Cucumber tags**:

```bash
mvn test -DsuiteXmlFile=src/test/java/project/testngConfig/apiRegression.xml \
         -Dcucumber.filter.tags="@API and not @KnownIssue"
```

### CI Execution (GitLab)

The pipeline is triggered via `SCHEDULED_JOB_TO_RUN` variable:

| Variable Value | What Runs                                                     |
|----------------|---------------------------------------------------------------|
| `QUALITY`      | Static analysis quality gate (SpotBugs + Checkstyle)          |
| `API`          | API regression suite                                          |
| `UI`           | Mobile regression suite                                       |

The `quality-gate` job also runs automatically on every **merge request**, ensuring no code style or bug pattern regressions are merged.

CI automatically sets `RUN_CONTEXT=CI` and reads environment-specific config from env vars instead of YAML files.

### Static Analysis

SpotBugs and Checkstyle are bound to the `verify` phase and enforced as a CI quality gate:

```bash
# SpotBugs (bytecode bug detection)
mvn spotbugs:spotbugs

# Checkstyle (source code style & complexity)
mvn checkstyle:check

# Both (bound to verify phase, fails build on violations)
mvn verify -DskipTests
```

Checkstyle rules are defined in `checkstyle.xml` at the project root. SpotBugs runs with `effort=Max` and `threshold=Medium`.

## Environment Configuration

The framework supports four environments, selected by the `ENV` variable (CI) or `General.env` in `config.yml` (local):

| Environment | Data File             | Typical Use           |
|-------------|-----------------------|-----------------------|
| `TEST`      | `testdata_test.yml`   | Development / local   |
| `UAT`       | `testdata_uat.yml`    | User acceptance       |
| `PREPROD`   | `testdata_preprod.yml`| Pre-production        |
| `PROD`      | `testdata_prod.yml`   | Production smoke      |

Each data file contains environment-specific values (base URLs, credentials) loaded at runtime by `YamlParser` and stored in thread-safe `Context`.

### Key Environment Variables (CI)

| Variable                  | Purpose                                    |
|---------------------------|--------------------------------------------|
| `RUN_CONTEXT`             | `CI` or absent for local                   |
| `ENV`                     | Target environment (`TEST`, `UAT`, etc.)   |
| `APIBaseUrlAccounts`      | Base URL for the accounts API              |
| `BROWSERSTACK_USERNAME`   | BrowserStack credentials                   |
| `BROWSERSTACK_ACCESS_KEY` | BrowserStack credentials                   |
| `JWT_PRIVAT_KEY`          | Base64-encoded RSA private key for JWT     |
| `app`                     | BrowserStack app identifier                |
| `build`                   | Build name for BrowserStack dashboard      |
| `session`                 | Session name for BrowserStack dashboard    |

## Tagging Strategy

Tags control which scenarios are included in a run and how they are categorized.

### Scope Tags

| Tag              | Purpose                                              |
|------------------|------------------------------------------------------|
| `@API`           | Marks a scenario as an API test                      |
| `@Mobile`        | Marks a scenario as a mobile UI test                 |

### Environment Tags

| Tag              | Purpose                                              |
|------------------|------------------------------------------------------|
| `@UAT`           | Scenario is eligible for UAT execution               |

### Suite/Priority Tags

| Tag              | Purpose                                              |
|------------------|------------------------------------------------------|
| `@MobileSmoke`   | Included in mobile smoke suite                       |
| `@APIRegression` | Included in API regression suite                     |

### Control Tags

| Tag              | Purpose                                              |
|------------------|------------------------------------------------------|
| `@KnownIssue`    | Excluded from all runs (linked to tracked defect)    |
| `@Ignore`        | Excluded from mobile runs (temporarily disabled)     |

### Traceability Tags

| Tag              | Purpose                                              |
|------------------|------------------------------------------------------|
| `@US-ID`         | Links to a user story (replace ID with story key)    |
| `@TestCaseKey=ID`| Links to a test case in test management tool         |

### Tag Usage Rules

1. Every scenario **must** have exactly one scope tag (`@API` or `@Mobile`).
2. Every scenario **should** have at least one suite tag to be included in scheduled runs.
3. Use `@KnownIssue` instead of deleting failing tests -- the tag auto-excludes from CI while preserving the scenario.
4. Feature-level tags are inherited by all scenarios in the file.

## Parallel Execution

Both API and Mobile suites are configured for parallel execution:

| Suite                  | Strategy           | Thread Count | Isolation Mechanism          |
|------------------------|--------------------|:------------:|------------------------------|
| `apiRegression.xml`    | `parallel="tests"` | 4            | `ThreadLocal` Context state  |
| `mobileRegression.xml` | `parallel="tests"` | 5            | `ThreadLocal` driver + state |

Thread safety is ensured by:

- `ThreadLocal<AppiumDriver>` in `DriverInstance`
- `ThreadLocal<T>` for all shared state in `Context` (response, request body, JWT payload, config data, environment)
- `ThreadLocal<String>` for API base URLs in `APIUtils`
- `ThreadLocal<Logger>` for per-thread logging
- Per-request `.baseUri()` on REST Assured `RequestSpecification` (avoids global `RestAssured.baseURI` mutation)

## Reporting

After execution, reports are generated at:

| Artifact                              | Description              |
|---------------------------------------|--------------------------|
| `test-output/reports/TestReports.html`| ExtentReports Spark HTML |
| `test-output/logs/`                   | Log4j2 rolling log files |
| `test-output/screenshots/`            | Failure screenshots      |

In CI, the entire `test-output/` directory is uploaded as a pipeline artifact retained for 5 days. Quality reports (`spotbugsXml.xml`, `checkstyle-result.xml`) are uploaded separately by the `quality-gate` job.

## Retry Mechanism

Failed mobile tests are automatically retried up to **2 times** via `RetryAnalyzer`. The `RetryCleanupListener` removes duplicate failure entries for tests that eventually pass on retry, ensuring clean reports.
