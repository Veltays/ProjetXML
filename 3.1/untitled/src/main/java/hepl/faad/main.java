package hepl.faad;

public class main {
    public static void main(String[] args) {
        String FileNotXmlPath = "untitled/src/main/resources/PADCHEST_chest_x_ray_images_labels_160K_01.02.19.csv";
        String FileXmlPath    = "untitled/src/main/resources/PADCHEST.xml";

        PadChestTransformer transformer = new PadChestTransformer();
        transformer.transformer(FileNotXmlPath, FileXmlPath);
    }
}