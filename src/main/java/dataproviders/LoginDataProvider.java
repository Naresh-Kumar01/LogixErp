package dataproviders;

import org.testng.annotations.DataProvider;
import utilities.CsvDataReader;
import utilities.JsonDataReader;

public class LoginDataProvider {

    @DataProvider(name = "loginJsonData")
    public Object[][] loginJsonData() {
        return JsonDataReader.readTwoDimensionalArray("testdata/login.json", "scenarios");
    }

    @DataProvider(name = "loginCsvData")
    public Object[][] loginCsvData() {
        return CsvDataReader.read("testdata/login.csv");
    }

    @DataProvider(name = "invalidLoginCases")
    public Object[][] invalidLoginCases() {
        return new Object[][]{
                {"invalid_user", "logixerp", "Invalid username"},
                {"admin", "wrong_pass", "Invalid password"},
                {"", "", "Blank credentials"},
                {"admin", "", "Empty password"},
                {"", "logixerp", "Empty username"}
        };
    }

    @DataProvider(name = "roleBasedUsers")
    public Object[][] roleBasedUsers() {
        return new Object[][]{
                {"admin", "admin", "logixerp"},
                {"wms_operator", "wms.user", "logixerp"},
                {"picker", "picker.user", "logixerp"}
        };
    }
}
