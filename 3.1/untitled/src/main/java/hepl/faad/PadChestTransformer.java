package hepl.faad;

public class PadChestTransformer {


    public void transformer(String csvPath, String xmlPath) {
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

                xmlWriter.EcrireBaliseEntranteElement(ParsedLine[0]);

                for (int i = 1; i < ParsedLine.length; i++) {
                    FinaleLine = "\t\t<" + Header[i] + ">" + ParsedLine[i] + "</" + Header[i] + ">" + "\n";
                    xmlWriter.EcritureDuXML(FinaleLine);
                }

                xmlWriter.EcrireBaliseFermanteElement();
            }

            xmlWriter.EcrireFinRacine();

            csvReader.close();
            xmlWriter.close();

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
