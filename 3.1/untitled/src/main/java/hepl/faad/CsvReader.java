package hepl.faad;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CsvReader {

    private final FileReader fileReader;
    private final BufferedReader br;

    public CsvReader(String filePath) throws IOException {
        this.fileReader = new FileReader(filePath);
        this.br = new BufferedReader(fileReader);
    }

    public String[] RecuperationHeader() throws IOException {
        String ligne;
        if ((ligne = br.readLine()) != null) {
            return ligne.split(",");
        }
        return null;
    }

    public String readCsvRecord() throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        boolean inQuotes = false;

        while ((line = br.readLine()) != null) {
            if (sb.length() > 0) sb.append('\n'); // conserver les \n internes
            sb.append(line);

            int i = 0;
            while (i < line.length()) {
                char c = line.charAt(i);
                if (c == '"') {
                    // guillemet échappé ?
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        i += 2; // sauter ""
                        continue;
                    }
                    inQuotes = !inQuotes; // toggle
                }
                i++;
            }
            if (!inQuotes) break; // enregistrement complet
        }
        return sb.length() == 0 ? null : sb.toString();
    }

    public BufferedReader getBufferedReader() {
        return br;
    }

    public void close() throws IOException {
        br.close();
        fileReader.close();
    }
}
