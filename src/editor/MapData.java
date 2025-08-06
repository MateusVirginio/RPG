package editor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MapData {
    private final int[][] mapLayer1;
    private final int[][] mapLayer2;

    public MapData(int maxCol, int maxRow) {
        mapLayer1 = new int[maxCol][maxRow];
        mapLayer2 = new int[maxCol][maxRow];
        clearMap(maxCol, maxRow);
    }

    public void clearMap(int maxCol, int maxRow) {
        for (int i = 0; i < maxCol; i++) {
            for (int j = 0; j < maxRow; j++) {
                mapLayer1[i][j] = -1;
                mapLayer2[i][j] = -1;
            }
        }
    }

    public void setMapLayer1Tile(int col, int row, int tileIndex) {
        mapLayer1[col][row] = tileIndex;
    }

    public int[][] getMapLayer1() {
        return mapLayer1;
    }

    public void setMapLayer2Tile(int col, int row, int tileIndex) {
        mapLayer2[col][row] = tileIndex;
    }

    public int[][] getMapLayer2() {
        return mapLayer2;
    }

    public void loadMapFromCsv(String filePath, int layer) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            int[][] targetLayer = (layer == 1) ? mapLayer1 : mapLayer2;
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < mapLayer1[0].length) {
                String[] numbers = line.split(",");
                for (int col = 0; col < numbers.length && col < mapLayer1.length; col++) {
                    try {
                        int num = Integer.parseInt(numbers[col].trim());
                        targetLayer[col][row] = num;
                    } catch (NumberFormatException e) {
                        targetLayer[col][row] = -1;
                    }
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}