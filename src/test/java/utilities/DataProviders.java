package utilities;

import java.io.IOException;

import org.testng.annotations.DataProvider;

public class DataProviders {
	// DataProvider 1

		@DataProvider
		public String[][] loginData() throws IOException {
			String path = ".\\testdata\\Data.xlsx";
			ExcelUtility xlutil = new ExcelUtility(path);
			String sheetName = "Sheet1";
			int totalrows = xlutil.getRowCount(sheetName);
			int totalcols = xlutil.getCellCount(sheetName, 1);
			String data[][] = new String[totalrows][totalcols];
			for (int i = 1; i <= totalrows; i++) {
				for (int j = 0; j < totalcols; j++) {
					data[i - 1][j] = xlutil.getCellData(sheetName, i, j);
				}
			}
			return data;

		}

		// DataProvider 2

		// DataProvider 3

		// DataProvider 4

}
