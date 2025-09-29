package hepl.faad;

public class PadChestTransformer {



    public static void main(String[] args) {
        String FileNotXmlPath = "untitled/src/main/resources/PADCHEST_chest_x_ray_images_labels_160K_01.02.19.csv";
        String FileXmlPath    = "untitled/src/main/resources/PADCHEST.xml";

        transformer(FileNotXmlPath, FileXmlPath);
    }
    public static void transformer(String csvPath, String xmlPath) {
        try {
            CsvReader csvReader = new CsvReader(csvPath);
            XmlWriter xmlWriter = new XmlWriter(xmlPath);

            String[] Header = csvReader.RecuperationHeader();

            xmlWriter.EcritureEnTete();
            xmlWriter.EcritureRacine();

            String ligne;
            String FinaleLine;

            while ((ligne = csvReader.readCsvRecord()) != null) {
                String[] ParsedLine = CsvRecordParser.ParseLine(ligne);

                String[]  ParsedLienWithoutExluded = csvReader.DeleteExcludedDataFromData(ParsedLine);

                xmlWriter.EcrireBaliseEntranteElementAvecAttribut("image",ParsedLienWithoutExluded[0]);



                for (int i = 1; i < ParsedLienWithoutExluded.length; i++) {

                    FinaleLine = xmlWriter.formatLineForXml(ParsedLienWithoutExluded[i],Header[i]);
                    xmlWriter.EcritureDuXML(FinaleLine);
                }


                xmlWriter.EcrireBaliseFermanteElement("image");

            }

            xmlWriter.EcrireFinRacine();

            csvReader.close();
            xmlWriter.close();

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
