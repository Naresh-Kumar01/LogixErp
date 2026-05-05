# 🚀 LogixERP Automation Framework

## 📌 Overview

This project is a scalable and maintainable **Test Automation Framework** built using **Java, Selenium, TestNG, and Maven**.
It follows a **Hybrid Framework Design (Page Object Model + Data Driven + Utility-based approach)**.

The framework is designed with industry best practices to support:

* Cross-browser testing
* Parallel execution
* CI/CD integration
* Detailed reporting

---

## 🏗️ Framework Architecture

```
src
 ├── main/java
 │    ├── base            → Test setup & teardown
 │    ├── pageobjects     → Page classes (POM)
 │    ├── utilities       → Reusable helpers
 │    ├── constants       → Framework constants
 │
 ├── test/java
 │    ├── testcases       → Test scripts
 │    ├── dataproviders   → Test data handling
 │    ├── listeners       → TestNG listeners
 │
 ├── test/resources
 │    ├── config.properties
 │    ├── testdata files
```

---

## ⚙️ Tech Stack

* Language: Java
* Automation: Selenium WebDriver
* Test Runner: TestNG
* Build Tool: Maven
* Reporting: Extent Reports / Allure
* CI/CD: Jenkins
* Version Control: GitHub

---

## ▶️ How to Run Tests

### 🔹 Run via Maven

```
mvn clean test
```

### 🔹 Run specific suite

```
mvn clean test -DsuiteXmlFile=testng.xml
```

---

## 🌐 Cross Browser Execution

Set browser in `testng.xml`:

```xml
<parameter name="browser" value="chrome"/>
```

Supported:

* Chrome
* Firefox

---

## 📊 Test Execution Strategy

### ✔️ Sanity Suite

* Covers critical functionalities
* Fast execution
* Used for build validation

### ✔️ Regression Suite

* Full application coverage
* Executed before release
* Ensures stability

---

## 📂 Data Driven Testing

* Test data managed via Excel / external files
* DataProviders used for parameterization

---

## 📸 Reporting

* Extent Reports for HTML reporting
* Screenshots captured on failure

---

## 🔁 Parallel Execution

* Enabled via TestNG
* Thread-safe driver using ThreadLocal

---

## 🔄 CI/CD Integration

* Integrated with Jenkins pipeline
* Automated test execution on code commit

---

## 🧠 Key Features

* Thread-safe WebDriver management
* Reusable utilities
* Scalable architecture
* Clean separation of concerns
* Easy maintenance & extension

---

## 🚧 Future Enhancements

* Docker + Selenium Grid integration
* API automation support
* Cloud execution (AWS)
* Advanced reporting dashboards

---

## 👨‍💻 Author

Naresh Kumar
Automation Test Engineer

---
