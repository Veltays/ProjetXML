package hepl.faad;

import javax.swing.*;
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
        // Cas simple : pas de liste -> on renvoie la ligne formatée
        if (!line.contains("[")) {
            return "<" + Header + ">" + line + "</" + Header + ">\n";
        }

        String elem = HandleSubElement(line,Header);

        return elem;

    }


    public String HandleSubElement(String fieldValue, String tagName) {
        // Ouvrir la balise principale
        EcrireBaliseEntranteElement(tagName);


        // Extraire le contenu entre [ ... ]
        int startIdx = fieldValue.indexOf('[');
        int endIdx   = fieldValue.lastIndexOf(']');

        if (startIdx == -1 || endIdx == -1 || endIdx <= startIdx) {
            EcrireBaliseFermanteElement(tagName);
            return "";
        }


        // trim le tableaux
        String arrayText = fieldValue.substring(startIdx, endIdx + 1).trim();
        if ("[]".equals(arrayText)) {       // tableau vide
            EcrireBaliseFermanteElement(tagName);
            return "";
        }

        // Gére les tableaux de Tableaux
        if ("LabelsLocalizationsBySentence".equals(tagName)) {
            return handleTabOfTab(arrayText, tagName);
        }




        String subTag = GetSubHeader(tagName); // ex. "Labels" -> "Label"
        handleSimpleTab(arrayText, subTag);


        // 5) Fermer la balise principale
        EcrireBaliseFermanteElement(tagName);
        return "";
    }

    private void handleSimpleTab(String arrayText, String subTag) {
        boolean inQuote = false;
        StringBuilder token = new StringBuilder();

        for (int i = 0; i < arrayText.length(); i++) {
            char ch = arrayText.charAt(i);

            if (ch == '\'') {
                inQuote = !inQuote;
                continue;
            }

            if (ch == ',' && !inQuote) {
                String value = clean(token.toString());
                token.setLength(0);
                if (!value.isEmpty()) {
                    EcritureDuXML("<" + subTag + ">" + value + "</" + subTag + ">\n");
                }
                continue;
            }

            token.append(ch);
        }

        // flush du dernier item
        String last = clean(token.toString());
        if (!last.isEmpty()) {
            EcritureDuXML("<" + subTag + ">" + last + "</" + subTag + ">\n");
        }
    }
    private String handleTabOfTab(String arrayText, String tagName) {
        boolean inQuote = false;
        int bracketDepth = 0;
        StringBuilder currentToken = new StringBuilder();
        java.util.List<String> sentenceParts = new java.util.ArrayList<>();

        for (int i = 0; i < arrayText.length(); i++) {
            char ch = arrayText.charAt(i);

            // Toggle quotes (on ignore les quotes dans les tokens)
            if (ch == '\'') {
                inQuote = !inQuote;
                continue;
            }

            if (!inQuote) {
                if (ch == '[') {
                    bracketDepth++;
                    continue;
                }

                if (ch == ']') {
                    // Fin d'un sous-tableau: depth == 2 (car [[ ... ]])
                    if (bracketDepth == 2) {
                        String value = clean(currentToken.toString().trim());
                        currentToken.setLength(0);
                        if (!value.isEmpty()) sentenceParts.add(value);

                        // sentenceParts = [Label, Localization?, Localization?...]
                        if (!sentenceParts.isEmpty()) {
                            EcritureDuXML("<Sentence>\n");
                            // 1er = Label
                            EcritureDuXML("  <Label>" + sentenceParts.get(0) + "</Label>\n");
                            // suivants = Localization
                            for (int k = 1; k < sentenceParts.size(); k++) {
                                EcritureDuXML("  <Localization>" + sentenceParts.get(k) + "</Localization>\n");
                            }
                            EcritureDuXML("</Sentence>\n");
                        }
                        sentenceParts.clear();
                    }
                    bracketDepth--;
                    continue;
                }

                // Séparateur d'items à l'intérieur d'un sous-tableau (depth == 2)
                if (ch == ',' && bracketDepth == 2) {
                    String value = clean(currentToken.toString().trim());
                    currentToken.setLength(0);
                    if (!value.isEmpty()) sentenceParts.add(value);
                    continue;
                }
            }

            // On n'accumule que le contenu des sous-tableaux (depth == 2)
            if (bracketDepth == 2) currentToken.append(ch);
        }

        // Ferme la balise principale comme dans ton code d'origine
        EcrireBaliseFermanteElement(tagName);
        return "";
    }




    private String clean(String s) {
        String v = s.trim();
        if (v.startsWith("[")) v = v.substring(1);
        if (v.endsWith("]"))   v = v.substring(0, v.length() - 1);
        if (v.startsWith("'")) v = v.substring(1);
        if (v.endsWith("'"))   v = v.substring(0, v.length() - 1);
        v = v.trim();
        return "None".equalsIgnoreCase(v) ? "" : v;
    }
    public String GetSubHeader(String Header)
    {
        String subHeader ="";
        switch (Header) {
            case "Labels":                       subHeader = "Label"; break;
            case "Localizations":                subHeader =  "Localization"; break;
            case "LabelsLocalizationsBySentence":subHeader =  "Sentence"; break;
            case "labelCUIS":                    subHeader =  "labelCUI"; break;
            case "LocalizationsCUIS":            subHeader =  "LocalizationsCUI"; break;
        }

        return subHeader;
    }


    public String getIndent() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++)
        {
            sb.append('\t');
        }
        return sb.toString();
    }


    public void close() throws IOException {
        PadXmlFile.close();
    }
}
