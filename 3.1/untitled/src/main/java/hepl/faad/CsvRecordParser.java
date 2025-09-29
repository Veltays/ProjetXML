package hepl.faad;

import java.util.ArrayList;

public class CsvRecordParser {

    // Fidèle à ta version: gestion des [] et de la virgule niveau 0
    // (pas d'autres ajouts)
    public static String[] ParseLine(String ligne) {
        ArrayList<String> champs = new ArrayList<>();
        String champ = "";
        int niveauCrochet = 0;

        for (int i = 0; i < ligne.length(); i++) {
            char c = ligne.charAt(i);

            if (c == '[') {
                niveauCrochet++;
                champ += c;
            } else if (c == ']') {
                niveauCrochet--;
                champ += c;
            } else if (c == ',' && niveauCrochet == 0) {
                champs.add(champ.trim());
                System.out.println("Champ trouvé: " + champ);
                champ = "";
            } else {
                champ += c;
            }
        }
        champs.add(champ.trim());

        return champs.toArray(new String[0]);
    }
}
