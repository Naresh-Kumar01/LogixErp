# Signin Functionality - Test Cases Documentation

This document describes all positive and negative test cases for the Signin functionality.

## Test Class: `TC002_SigninPositiveNegativeTest`

---

## 📋 POSITIVE TEST CASES

### TC_SIGNIN_001: Valid Credentials Signin
**Priority:** 1  
**Groups:** Sanity, Regression, Positive  
**Description:** Verify successful signin with valid username and password

**Test Steps:**
1. Navigate to application URL
2. Enter valid username from config.properties
3. Enter valid password from config.properties
4. Click Sign in button
5. Verify home page is displayed
6. Verify URL changed from login page

**Expected Result:**
- User should be successfully signed in
- Home page should be displayed
- URL should not contain "login"

---

### TC_SIGNIN_002: Username Case Insensitive
**Priority:** 2  
**Groups:** Regression, Positive  
**Description:** Verify signin works with different case username (if supported)

**Test Steps:**
1. Enter username in uppercase
2. Enter valid password
3. Click Sign in button
4. Verify signin result

**Expected Result:**
- Signin may succeed (if case insensitive) or fail (if case sensitive)
- Appropriate behavior based on application requirements

---

### TC_SIGNIN_003: Signin Page Elements Displayed
**Priority:** 3  
**Groups:** Sanity, Positive  
**Description:** Verify all signin page elements are displayed correctly

**Test Steps:**
1. Verify username field is displayed
2. Verify password field is displayed
3. Verify signin button is displayed and enabled
4. Verify username field is empty initially

**Expected Result:**
- All elements should be visible and accessible
- Username field should be empty initially

---

## ❌ NEGATIVE TEST CASES

### TC_SIGNIN_NEG_001: Invalid Username
**Priority:** 10  
**Groups:** Regression, Negative  
**Description:** Verify error message when invalid username is entered

**Test Steps:**
1. Enter invalid username
2. Enter valid password
3. Click Sign in button
4. Verify error message is displayed
5. Verify still on login page

**Expected Result:**
- Error message should be displayed
- User should remain on login page
- Signin should fail

---

### TC_SIGNIN_NEG_002: Invalid Password
**Priority:** 11  
**Groups:** Regression, Negative  
**Description:** Verify error message when invalid password is entered

**Test Steps:**
1. Enter valid username
2. Enter invalid password
3. Click Sign in button
4. Verify error message is displayed
5. Verify still on login page

**Expected Result:**
- Error message should be displayed
- User should remain on login page
- Signin should fail

---

### TC_SIGNIN_NEG_003: Invalid Both Credentials
**Priority:** 12  
**Groups:** Regression, Negative  
**Description:** Verify error message when both credentials are invalid

**Test Steps:**
1. Enter invalid username
2. Enter invalid password
3. Click Sign in button
4. Verify error message is displayed
5. Verify still on login page

**Expected Result:**
- Error message should be displayed
- User should remain on login page
- Signin should fail

---

### TC_SIGNIN_NEG_004: Empty Username
**Priority:** 13  
**Groups:** Sanity, Regression, Negative  
**Description:** Verify validation when username is empty

**Test Steps:**
1. Leave username field empty
2. Enter valid password
3. Click Sign in button
4. Verify validation message or error

**Expected Result:**
- HTML5 validation or error message should be displayed
- User should remain on login page or form should not submit

---

### TC_SIGNIN_NEG_005: Empty Password
**Priority:** 14  
**Groups:** Sanity, Regression, Negative  
**Description:** Verify validation when password is empty

**Test Steps:**
1. Enter valid username
2. Leave password field empty
3. Click Sign in button
4. Verify validation message or error

**Expected Result:**
- HTML5 validation or error message should be displayed
- User should remain on login page or form should not submit

---

### TC_SIGNIN_NEG_006: Both Fields Empty
**Priority:** 15  
**Groups:** Regression, Negative  
**Description:** Verify validation when both fields are empty

**Test Steps:**
1. Clear both username and password fields
2. Click Sign in button
3. Verify validation message or error

**Expected Result:**
- HTML5 validation or error message should be displayed
- User should remain on login page or form should not submit

---

### TC_SIGNIN_NEG_007: SQL Injection Attempt
**Priority:** 16  
**Groups:** Security, Negative  
**Description:** Verify system handles SQL injection attempts

**Test Steps:**
1. Enter SQL injection string in username: `admin' OR '1'='1`
2. Enter password
3. Click Sign in button
4. Verify signin failed

**Expected Result:**
- Signin should fail
- User should remain on login page
- System should be protected against SQL injection

---

### TC_SIGNIN_NEG_008: XSS Attempt
**Priority:** 17  
**Groups:** Security, Negative  
**Description:** Verify system handles XSS attempts

**Test Steps:**
1. Enter XSS attempt in username: `<script>alert('XSS')</script>`
2. Enter password
3. Click Sign in button
4. Verify signin failed

**Expected Result:**
- Signin should fail
- User should remain on login page
- System should be protected against XSS attacks

---

### TC_SIGNIN_NEG_009: Special Characters Username
**Priority:** 18  
**Groups:** Regression, Negative  
**Description:** Verify system handles special characters

**Test Steps:**
1. Enter special characters in username: `!@#$%^&*()`
2. Enter valid password
3. Click Sign in button
4. Verify error or validation

**Expected Result:**
- Error message should be displayed or validation should occur
- Special characters should be handled appropriately

---

### TC_SIGNIN_NEG_010: Very Long Username
**Priority:** 19  
**Groups:** Regression, Negative  
**Description:** Verify system handles very long input

**Test Steps:**
1. Enter very long username (500 characters)
2. Enter valid password
3. Click Sign in button
4. Verify error or validation

**Expected Result:**
- Error message should be displayed or validation should occur
- Very long input should be handled appropriately

---

### TC_SIGNIN_NEG_011: Whitespace Username
**Priority:** 20  
**Groups:** Regression, Negative  
**Description:** Verify system handles whitespace in username

**Test Steps:**
1. Enter username with leading/trailing spaces: `  admin  `
2. Enter valid password
3. Click Sign in button
4. Verify result

**Expected Result:**
- System may trim whitespace (acceptable) or show error
- Appropriate behavior based on application requirements

---

## 🚀 Running the Tests

### Run All Signin Tests
```bash
mvn clean test -Dsurefire.suiteXmlFiles=signin_test_suite.xml
```

### Run Only Positive Tests
```bash
mvn clean test -Dsurefire.suiteXmlFiles=signin_test_suite.xml -Dtest=TC002_SigninPositiveNegativeTest#test_Signin_With_Valid_Credentials,test_Signin_Username_Case_Insensitive,test_Signin_Page_Elements_Displayed
```

### Run Only Negative Tests
```bash
mvn clean test -Dsurefire.suiteXmlFiles=signin_test_suite.xml -Dtest=TC002_SigninPositiveNegativeTest#test_Signin_Invalid_*
```

### Run Specific Test Group
```bash
# Run Sanity tests
mvn clean test -Dsurefire.suiteXmlFiles=signin_test_suite.xml -Dgroups=Sanity

# Run Security tests
mvn clean test -Dsurefire.suiteXmlFiles=signin_test_suite.xml -Dgroups=Security

# Run Regression tests
mvn clean test -Dsurefire.suiteXmlFiles=signin_test_suite.xml -Dgroups=Regression
```

---

## 📊 Test Coverage Summary

| Category | Test Cases | Count |
|----------|-----------|-------|
| **Positive Tests** | TC_SIGNIN_001 to TC_SIGNIN_003 | 3 |
| **Negative Tests** | TC_SIGNIN_NEG_001 to TC_SIGNIN_NEG_011 | 11 |
| **Total** | | **14** |

### Test Groups Distribution
- **Sanity:** 4 tests
- **Regression:** 12 tests
- **Positive:** 3 tests
- **Negative:** 11 tests
- **Security:** 2 tests

---

## 📝 Notes

1. **Test Data:** Valid credentials are read from `config.properties` file
2. **Error Messages:** Error message verification uses flexible selectors to find error messages in various formats
3. **Video Recording:** All tests are automatically recorded (if video recording is enabled)
4. **Reports:** Test results are available in:
   - TestNG Reports: `target/surefire-reports/index.html`
   - Extent Reports: `reports/Extent-Report-*.html`
   - Video Recordings: `videos/TC002_SigninPositiveNegativeTest_*/`

---

## 🔧 Maintenance

When updating test cases:
1. Update this document with new test cases
2. Update `signin_test_suite.xml` if test structure changes
3. Ensure test data in `config.properties` is up to date
4. Review and update error message selectors if UI changes
