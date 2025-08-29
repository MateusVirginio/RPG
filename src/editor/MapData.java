package editor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class MapData {
    private final int[][] mapLayer1;
    private final int[][] mapLayer2;
    private final int maxCol;
    private final int maxRow;

    public MapData(int maxCol, int maxRow) {
        this.maxCol = maxCol;
        this.maxRow = maxRow;
        mapLayer1 = new int[maxCol][maxRow];
        mapLayer2 = new int[maxCol][maxRow];
        clearMap();
    }

    public void clearMap() {
        for (int i = 0; i < maxCol; i++) {
            for (int j = 0; j < maxRow; j++) {
                mapLayer1[i][j] = -1;
                mapLayer2[i][j] = -1;
            }
        }
    }

    public void setMapLayer1Tile(int col, int row, int tileIndex) {
        if (col >= 0 && col < maxCol && row >= 0 && row < maxRow) {
            mapLayer1[col][row] = tileIndex;
        }
    }

    public int[][] getMapLayer1() {
        return mapLayer1;
    }

    public void setMapLayer2Tile(int col, int row, int tileIndex) {
        if (col >= 0 && col < maxCol && row >= 0 && row < maxRow) {
            mapLayer2[col][row] = tileIndex;
        }
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

    public void drawBrush(int startCol, int startRow, int tileIndex, int layer, int brushSize) {
        int halfSize = brushSize / 2;
        int startX = startCol - halfSize;
        int startY = startRow - halfSize;

        for (int c = startX; c < startX + brushSize; c++) {
            for (int r = startY; r < startY + brushSize; r++) {
                if (c >= 0 && c < maxCol && r >= 0 && r < maxRow) {
                    if (layer == 1) {
                        mapLayer1[c][r] = tileIndex;
                    } else {
                        mapLayer2[c][r] = tileIndex;
                    }
                }
            }
        }
    }

    private int[][] getActiveLayer(int layer) {
        return (layer == 1) ? mapLayer1 : mapLayer2;
    }

    public void fill(int startCol, int startRow, int newTileIndex, int layer) {
        int[][] currentLayer = getActiveLayer(layer);
        int targetTileIndex = currentLayer[startCol][startRow];

        if (targetTileIndex == newTileIndex) {
            return;
        }

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startCol, startRow});

        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int col = pos[0];
            int row = pos[1];

            if (col < 0 || col >= maxCol || row < 0 || row >= maxRow || currentLayer[col][row] != targetTileIndex) {
                continue;
            }

            currentLayer[col][row] = newTileIndex;

            queue.add(new int[]{col + 1, row});
            queue.add(new int[]{col - 1, row});
            queue.add(new int[]{col, row + 1});
            queue.add(new int[]{col, row - 1});
        }
    }
}