package editor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MapSaver {

    public static void saveMapToCsv(String filePath, int[][] mapData, int maxCol, int maxRow) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (int row = 0; row < maxRow; row++) {
                StringBuilder line = new StringBuilder();
                for (int col = 0; col < maxCol; col++) {
                    line.append(mapData[col][row]);
                    if (col < maxCol - 1) {
                        line.append(",");
                    }
                }
                bw.write(line.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar o mapa em CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
}