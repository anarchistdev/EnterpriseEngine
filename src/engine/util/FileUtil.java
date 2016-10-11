package engine.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by anarchist on 6/14/16.
 */
public class FileUtil {

    public static String loadFromFile(String file) {
        StringBuilder source = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                source.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Could not read file");
            e.printStackTrace();
            System.exit(-1);
        }

        return source.toString();
    }

}
