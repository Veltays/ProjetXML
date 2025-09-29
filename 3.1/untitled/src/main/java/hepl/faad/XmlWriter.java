package hepl.faad;

import java.io.FileWriter;
import java.io.IOException;

public class XmlWriter {

    private final FileWriter PadXmlFile;

    public XmlWriter(String fileXmlPath) throws IOException {
        this.PadXmlFile = new FileWriter(fileXmlPath);
    }

    public void EcritureEnTete() {
        EcritureDuXML("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n");
    }

    public void EcritureRacine() {
        EcritureDuXML("<Images>\n");
    }

    public void EcrireFinRacine() {
        EcritureDuXML("</Images>\n");
    }

    // Fidèle à ta signature d'origine (sans guillemets ajoutés)
    public void EcrireBaliseEntranteElement(String ligne) {
        EcritureDuXML("\t<Image Identifiant=" + ligne + "> \n");
    }

    public void EcrireBaliseFermanteElement() {
        EcritureDuXML("\t</Image> \n");
    }

    public void EcritureDuXML(String FinaleLine) {
        try {
            PadXmlFile.write(FinaleLine);
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    public void close() throws IOException {
        PadXmlFile.close();
    }
}
