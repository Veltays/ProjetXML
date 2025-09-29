package hepl.faad;

import java.io.FileWriter;
import java.io.IOException;


public class XmlWriter {

    private final FileWriter PadXmlFile;
    public final String RootIdentifier = "Images";

    public XmlWriter(String fileXmlPath) throws IOException {
        this.PadXmlFile = new FileWriter(fileXmlPath);
    }

    public int depth = 0;




    public void EcritureEnTete() {
        EcritureDuXML("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n");
    }

    public void EcritureRacine() {
        EcritureDuXML("<"+RootIdentifier+">\n");
        depth++;
    }

    public void EcrireFinRacine() {
        depth--;
        EcritureDuXML("</"+RootIdentifier+">\n");
    }

    // Fidèle à ta signature d'origine (sans guillemets ajoutés)
    public void EcrireBaliseEntranteElementAvecAttribut(String ElementIdentifier, String ligne) {
        EcritureDuXML("<"+ElementIdentifier +" Identifiant=" + "\"" +ligne +"\"" + "> \n");
        depth++;
    }

    public void EcrireBaliseEntranteElement(String ElementIdentifier) {
        EcritureDuXML("<"+ElementIdentifier + "> \n");
        depth++;
    }

    public void EcrireBaliseFermanteElement(String ElementIdentifier) {
        depth--;
        EcritureDuXML( "</"+ElementIdentifier +"> \n");
    }

    public void EcritureDuXML(String FinaleLine) {
        try {

            PadXmlFile.write(getIndent() + FinaleLine);

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }


    public String formatLineForXml(String line, String Header)
    {
//        if(line.contains("["))
//        {
//
//            // lecture caractere par caractere pour chercher chaque champ, les mettres dans une balise personalisé et apres fermer la dites balises
//            // donc la logique en dessous ne marche pas
//            EcrireBaliseEntranteElement(Header);
//            switch(Header)
//            {
//                case "Labels":
//                    Header = "Label";
//                    depth++;
//                    break;
//                case "Localizations":
//                    Header = "Localization";
//                    depth++;
//                    break;
//                case "LabelsLocalizationsBySentence":
//                    Header = "Sentence";
//                    depth++;
//                    break;
//                case "labelCUIS":
//                    Header = "labelCUI";
//                    depth++;
//                    break;
//                case "LocalizationsCUIS":
//                    Header = "LocalizationsCUI";
//                    depth++;
//                    break;
//
//            }
////        }
//
//        if(line.contains("]"))
//        {
//            depth--;
//            EcrireBaliseFermanteElement(Header);
//        }



        String FinaleLine = "<" + Header + ">" + line + "</" + Header + ">" + "\n";

        return FinaleLine ;
    }


    public String getIndent() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) sb.append('\t');
        return sb.toString();
    }


    public void close() throws IOException {
        PadXmlFile.close();
    }
}
