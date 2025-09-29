package hepl.faad;
import java.io.*;
import java.util.ArrayList;


public class LireFichier {

    static String FileNotXmlPath = "untitled/src/main/resources/PADCHEST_chest_x_ray_images_labels_160K_01.02.19.csv";
    static String FileXmlPath = "untitled/src/main/resources/PADCHEST.xml";
    static String[] Header;

    static FileReader PadFile;
    static FileWriter PadXmlFile;
    static BufferedReader br;


    public static void main(String[] args)
    {
        try
        {

            // Ouverture du fichier
            PadFile = new FileReader(FileNotXmlPath);
            PadXmlFile = new FileWriter(FileXmlPath);
            // Ouverture flux de lecture
            br = new BufferedReader(PadFile);




            Header = RecuperationHeader();

            String FinaleLine;
            String ligne;



            EcritureEnTete();
            EcritureRacine();

            while ((ligne = readCsvRecord(br)) != null)
            {
                String ParsedLine[] = ParseLine(ligne);
                EcrireBaliseEntranteElement(ParsedLine[0]);

                for (int i = 1; i < ParsedLine.length; i++)
                {
                    FinaleLine = "\t\t<" + Header[i] + ">" + ParsedLine[i] + "</" + Header[i] + ">" + "\n";
                    EcritureDuXML(FinaleLine);
                }

                EcrireBaliseFermanteElement();
            }

            EcrireFinRacine();
            br.close();


        }
        catch (Exception e)
        {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    public static void EcritureRacine()
    {
        EcritureDuXML("<Images>\n");
    }

    public static void EcrireFinRacine()
    {
        EcritureDuXML("</Images>\n");
    }
    public static void EcrireBaliseEntranteElement(String ligne)
    {
        EcritureDuXML("\t<Image Identifiant="+ligne+"> \n");
    }


    public static void EcrireBaliseFermanteElement()
    {
        EcritureDuXML("\t</Image> \n");
    }

    static String readCsvRecord(BufferedReader br) throws IOException {
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


    public static void  EcritureEnTete()
    {
            EcritureDuXML("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n");
    }

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
                // virgule "de niveau 0" => fin de champ
                champs.add(champ.trim());
                System.out.println("Champ trouvé: " + champ);
                champ = "";
            } else {
                champ += c;
            }
        }

        // dernier champ
        champs.add(champ.trim());

        return champs.toArray(new String[0]);
    }


    public static void  EcritureDuXML(String FinaleLine)
    {
        try {
            PadXmlFile.write(FinaleLine);
        }
        catch (Exception e)
        {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    public static String[] RecuperationHeader()
    {

        try
        {
            // Lecture de chaque ligne
            String ligne;

            // récupération Header()
            if ((ligne = br.readLine()) != null)
            {

                return ligne.split(",");
            }

            return null;
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            throw new RuntimeException(e);
        }


    }

}
