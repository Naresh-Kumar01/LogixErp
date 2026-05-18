package utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class JsonDataReader {

    private JsonDataReader() {
    }

    public static Object[][] readTwoDimensionalArray(String resourcePath, String arrayKey) {
        try (InputStream is = JsonDataReader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }
            JsonObject root = JsonParser.parseReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                    .getAsJsonObject();
            JsonArray array = root.getAsJsonArray(arrayKey);
            List<Object[]> rows = new ArrayList<>();
            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();
                rows.add(new Object[]{
                        obj.get("username").getAsString(),
                        obj.get("password").getAsString(),
                        obj.get("expected").getAsString()
                });
            }
            return rows.toArray(new Object[0][]);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to read JSON test data: " + resourcePath, ex);
        }
    }
}
