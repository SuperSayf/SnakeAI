import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Logger {

    private static int logCount = 0;

    public static void log(String string) {
        if (string != null) {
            BufferedWriter bw;
            try {
                LocalDateTime date = LocalDateTime.now();
                long seconds = date.toLocalTime().toSecondOfDay();
                File file = new File("logs/" + logCount + ".txt");
                logCount++;
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter writer = new FileWriter(file);
                bw = new BufferedWriter(writer);
                bw.write(string);
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}