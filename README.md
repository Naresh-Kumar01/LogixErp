LogixERP Automation Framework
Overview

This repository contains an Automation Testing Framework developed for testing the LogixERP application. The framework is built using Java, Selenium, and TestNG and is integrated with CI/CD using Jenkins and GitHub workflows.

The project includes automated Sign-in test cases, validation scenarios, and supporting configuration files to execute automated tests efficiently.

Tech Stack

Programming Language: Java

Automation Tool: Selenium WebDriver

Test Framework: TestNG

Build / CI-CD: Jenkins, GitHub Actions

Version Control: Git & GitHub

Project Structure
LogixERP
│
├── src/test
│   └── Test cases and automation scripts
│
├── .github/workflows
│   └── CI/CD pipeline configuration
│
├── Jenkinsfile
│   └── Jenkins pipeline configuration
│
├── config.properties
│   └── Environment and framework configuration
│
├── SIGNIN_TEST_CASES.md
│   └── Detailed documentation of login test scenarios
│
└── README_JENKINS.txt
    └── Instructions for Jenkins setup
Features

Automated Sign-in test scenarios

Test execution using TestNG

CI/CD integration with Jenkins

GitHub workflow support

Configurable test environment using properties file

Organized project structure for scalability

Test Scenarios Covered

Valid login

Invalid login

Empty username/password validation

UI validation on sign-in page

Error message verification

How to Run Tests
1. Clone the Repository
git clone https://github.com/Naresh-Kumar01/LogixErp.git
2. Open Project

Import the project into Eclipse / IntelliJ IDEA as a Maven or Java project.

3. Run Test Cases

Run the TestNG test classes from:

src/test

Or execute using TestNG suite.

CI/CD Integration

The project includes:

Jenkinsfile for automated pipeline execution

GitHub workflows for CI automation

These pipelines can automatically run tests on every commit or pull request.

Author

Naresh Kumar
Automation Test Engineer
Expertise in Selenium, API Testing, Mobile Testing, and CI/CD

If you want, I can also help you create a professional GitHub README (with badges, screenshots, and interview-ready structure) so your GitHub profile looks strong for SDET/Automation jobs.
