# LogixERP WMS Enterprise Automation Framework

Enterprise-grade **Selenium 4 + Java 17 + TestNG + Maven** automation framework for [LogixERP](https://logixerp.com) logistics, WMS, and supply chain flows.

## Application flow

`Login → WMS → Product Creation → Put Away → Picking → Inventory → Dispatch`

## Tech stack

| Layer | Tools |
|--------|--------|
| UI | Selenium WebDriver 4, Page Object Model |
| Tests | TestNG, parallel execution, retry analyzer |
| Build | Maven, Java 17 |
| Data | Excel (POI), JSON, CSV, JavaFaker |
| API | REST Assured |
| DB | JDBC (MySQL / PostgreSQL) |
| Reports | Extent Reports, Allure |
| Logging | Log4j2 |
| CI/CD | Jenkins, GitHub Actions, Docker |

## Project structure

```
src/main/java
├── annotations/     # @TestCase, @Role
├── api/             # APIUtility (REST Assured)
├── assertions/      # Soft assertions
├── base/            # BaseTest
├── config/          # ConfigReader, EnvironmentManager
├── constants/       # FrameworkConstants
├── database/        # DBUtility + sample SQL
├── dataproviders/   # TestNG data providers
├── drivers/         # Thread-safe DriverFactory
├── enums/           # Browser, Environment, UserRole
├── helpers/         # Wait, Screenshot, Assertion, Keyword engine
├── listeners/       # TestListener
├── locators/        # LocatorRepository (auto-heal aliases)
├── models/          # Product, Picklist DTOs
├── pages/           # Login, WMS, Product, Picking, Inventory, Dispatch
├── reports/         # ExtentManager
├── retry/           # RetryAnalyzer
└── utilities/       # Excel, JSON, CSV, Faker, Video, Log

src/test/java/tests
├── auth/            # 10 login scenarios
├── product/         # Product creation scenarios
├── picking/         # FIFO, FEFO, ASN, picking rules
├── serial/          # Serial product flows
├── batch/           # Batch product flows
├── negative/        # Negative / edge cases
├── api/             # REST API samples
└── database/        # JDBC validation samples
```

## Quick start

```bash
cd LogixErp-main
mvn clean test -DsuiteFile=testng.xml -Dbrowser=chrome -Denv=qa
```

### Run by group

```bash
mvn test -DsuiteFile=testng.xml -Dgroups=Sanity
mvn test -DsuiteFile=testng.xml -Dgroups=Login
mvn test -DsuiteFile=testng.xml -Dgroups=API
```

### Cross-browser

```bash
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
```

### Headless / remote grid

```bash
mvn test -Dheadless=true
mvn test -Dexecution_env=remote -DgridHubURL=http://localhost:4444/wd/hub
```

### Allure report

```bash
mvn test allure:report
mvn allure:serve
```

## Configuration

Edit `src/test/resources/config.properties`:

- `appURL`, `username`, `password`
- `execution_env` = `local` | `remote` | `cloud`
- `retry.count`, `video.recording.enabled`
- `db.enabled`, `api.baseURL`

Override at runtime: `-Denv=uat -Dusername=admin -Dpassword=***`

## CI/CD

- **Jenkins**: `Jenkinsfile` (scheduled + parameterized)
- **GitHub Actions**: `.github/workflows/enterprise-ci.yml`
- **Docker**: `docker build -t logixerp-automation .`

## Framework features

- Hybrid UI + API + DB validation
- Parallel TestNG execution
- Retry failed tests (`RetryAnalyzer`)
- Screenshot on failure (`TestListener`)
- Video recording (optional)
- Role-based and data-driven login
- Keyword-driven `KeywordEngine`
- Locator auto-heal via `LocatorRepository`
- Thread-safe `DriverFactory`

## Notes for enterprise environments

1. WMS locators are template XPath selectors—refine against your tenant UI (beta/alpha/UAT).
2. DB tests are **disabled by default** (`db.enabled=false`); enable after schema mapping.
3. API endpoints are samples; align paths with LogixERP OpenAPI/Swagger for your deployment.
4. Legacy tests remain under `testcases.*` and `pageobjects.*` for backward compatibility.

## Author

Built for logistics/WMS/ERP regression automation on LogixERP.
