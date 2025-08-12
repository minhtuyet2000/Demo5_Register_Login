package scripts.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReaderUtilForList {
    public static List<String> readCSVAsList(String filePath) {
        List<String> values = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                if (!line.trim().isEmpty()) {
                    values.add(line.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return values;
    }
}
