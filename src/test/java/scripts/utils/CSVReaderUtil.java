package scripts.utils;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CSVReaderUtil {
    public static Object[][] readCSV(String filePath) {
        List<Object[]> users = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            boolean firstLine = true;
            while ((line = reader.readNext()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                users.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return users.toArray(new Object[0][0]);
    }
    public static Object[] getRandomFromCSV(String filePath) {
        Object[][] allData = readCSV(filePath);
        if (allData.length == 0) {
            return new Object[]{}; 
        }
        Random random = new Random();
        return allData[random.nextInt(allData.length)];
    }
}
