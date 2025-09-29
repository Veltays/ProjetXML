package hepl.faad;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class CsvReader {

    private final FileReader fileReader;
    private final BufferedReader br;

    public final static Set<Integer> EXCLUDEDITEM = Set.of(
            3,  // StudyDate_DICOM
            6,  // PatientSex_DICOM
            7,  // ViewPosition_DICOM
            11, // Modality_DICOM
            12, // Manufacturer_DICOM
            13, // PhotometricInterpretation_DICOM
            14, // PixelRepresentation_DICOM
            15, // PixelAspectRatio_DICOM
            16, // SpatialResolution_DICOM
            17, // BitsStored_DICOM
            18, // WindowCenter_DICOM
            19, // WindowWidth_DICOM
            20, // Rows_DICOM
            21, // Columns_DICOM
            22, // XRayTubeCurrent_DICOM
            23, // Exposure_DICOM
            24, // ExposureInuAs_DICOM
            25, // ExposureTime
            26  // RelativeXRayExposure_DICOM
    );

    public CsvReader(String filePath) throws IOException {
        this.fileReader = new FileReader(filePath);
        this.br = new BufferedReader(fileReader);
    }

    public String[] RecuperationHeader() throws IOException {
        String ligne;
        if ((ligne = br.readLine()) != null) {
            String [] Allheader = ligne.split(",");
            return DeleteExcludedDataFromHeader(Allheader);

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

    public static String[] DeleteExcludedDataFromHeader(String[] Allheader){

        ArrayList<String> HeaderWithExcluded = new ArrayList<>();


        for(int i = 0; i < Allheader.length; i++){
            if (!EXCLUDEDITEM.contains(i)) {
                HeaderWithExcluded.add(Allheader[i]);
            }
        }

        System.out.println("HeaderWithExcluded: " + HeaderWithExcluded);



        return HeaderWithExcluded.toArray(new String[0]);
    }


    public static String[] DeleteExcludedDataFromData(String[] AllData)
    {
        ArrayList<String> DataWithExcluded = new ArrayList<>();


        for(int i = 0; i < AllData.length; i++){
            if (!EXCLUDEDITEM.contains(i)) {
                DataWithExcluded.add(AllData[i]);
            }
        }

        System.out.println("HeaderWithExcluded: " + DataWithExcluded);

        return DataWithExcluded.toArray(new String[0]);
    }


    public void close() throws IOException {
        br.close();
        fileReader.close();
    }
}
