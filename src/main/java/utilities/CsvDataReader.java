package utilities;

import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class CsvDataReader {

    private CsvDataReader() {
    }

    public static Object[][] read(String resourcePath) {
        try (InputStream is = CsvDataReader.class.getClassLoader().getResourceAsStream(resourcePath);
             CSVReader reader = new CSVReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            List<Object[]> rows = new ArrayList<>();
            String[] line;
            boolean headerSkipped = false;
            while ((line = reader.readNext()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }
                rows.add(line);
            }
            return rows.toArray(new Object[0][]);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to read CSV: " + resourcePath, ex);
        }
    }
}
