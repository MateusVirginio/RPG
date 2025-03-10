package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int mapTileNumLayer1[][][]; // Camada 1
    public int mapTileNumLayer2[][][]; // Camada 2

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[999];
        mapTileNumLayer1 = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow]; // Camada 1
        mapTileNumLayer2 = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow]; // Camada 2

        getTileImage();
        loadMaps(); // Carrega os mapas com base no currentMap
    }

    public void getTileImage() {
        setup(0, "grama_2", false);
        setup(25, "grama_1", false);
        setup(32, "caminho_pedra_1", false);
        setup(41, "caminho_pedra_2", false);
        setup(99, "arbusto", false);
        setup(450, "escada_direita_1-3", false);
        setup(434, "escada_direita_2-3", false);
        setup(418, "escada_direita_3-3", false);
        setup(449, "escada_esquerda_1-3", false);
        setup(433, "escada_esquerda_2-3", false);
        setup(417, "escada_esquerda_3-3", false);
        setup(53, "arvore_simples_esquerda_1-4", true);
        setup(37, "arvore_simples_esquerda_2-4", true);
        setup(21, "arvore_simples_esquerda_3-4", true);
        setup(5, "arvore_simples_esquerda_4-4", true);
        setup(70, "arvore_simples_meio_1-5", false);
        setup(54, "arvore_simples_meio_2-5", true);
        setup(38, "arvore_simples_meio_3-5", true);
        setup(22, "arvore_simples_meio_4-5", true);
        setup(6, "arvore_simples_meio_5-5", true);
        setup(55, "arvore_simples_direita_1-4", true);
        setup(39, "arvore_simples_direita_2-4", true);
        setup(23, "arvore_simples_direita_3-4", true);
        setup(7, "arvore_simples_direita_4-4", true);
        setup(169, "bloco_1-2", true);
        setup(153, "bloco_2-2", true);
        setup(65, "parede_frente_esquerda_1-4", true);
        setup(49, "parede_frente_esquerda_2-4", true);
        setup(33, "parede_frente_esquerda_3-4", true);
        setup(17, "parede_frente_esquerda_4-4", true);
        setup(66, "parede_frente_meio_1-5", true);
        setup(50, "parede_frente_meio_2-5", true);
        setup(114, "parede_frente_meio_3-5", true);
        //setup(99, "parede_frente_meio_4-5", true);
        setup(18, "parede_frente_meio_5-5", true);
        setup(67, "parede_frente_direita_1-4", true);
        setup(51, "parede_frente_direita_2-4", true);
        setup(35, "parede_frente_direita_3-4", true);
        setup(19, "parede_frente_direita_4-4", true);
        setup(94, "porta_pedra_direita_1-2", false);
        setup(78, "porta_pedra_direita_2-2", false);
        setup(29, "porta_pedra_meio", false);
        setup(93, "porta_pedra_esquerda_1-2", false);
        setup(77, "porta_pedra_esquerda_2-2", false);
        setup(95, "teto_pedra", true);
        setup(45, "estatua_esquerda", false);
        setup(46, "estatua_meio_1-3", true);
        setup(30, "estatua_meio_2-3", true);
        setup(14, "estatua_meio_3-3", true);
        setup(47, "estatua_direita", false);
        setup(248, "chao_pedra_3", false);
        setup(318, "preto", false);
        setup(61, "parede_lado_direito", true);
        setup(60, "parede_lado_esquerdo", true);
        setup(105, "tumulo_1-3", true);
        setup(89, "tumulo_2-3", true);
        setup(73, "tumulo_3-3", true);
        setup(138, "runa", false);
        setup(171, "altar_esquerda_1-3", false);
        setup(155, "altar_esquerda_2-3", false);
        setup(139, "altar_esquerda_3-3", false);
        setup(172, "altar_meio_1-3", false);
        setup(156, "altar_meio_2-3", false);
        setup(140, "altar_meio_3-3", false);
        setup(173, "altar_direita_1-3", false);
        setup(157, "altar_direita_2-3", false);
        setup(141, "altar_direita_3-3", false);


    }

    public void setup(int index, String imagePath, boolean collision) {
        UtilityTool uTool = new UtilityTool();

        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imagePath + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath, int map, int[][][] mapTileNumLayer) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (row < gp.maxWorldRow) {
                String line = br.readLine();
                if (line == null) break;

                String[] numbers = line.split(",");
                for (col = 0; col < gp.maxWorldCol && col < numbers.length; col++) {
                    int num = Integer.parseInt(numbers[col].trim());
                    if (num != -1) {
                        mapTileNumLayer[map][col][row] = num;
                    }
                }
                row++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMaps() {
        switch (gp.currentMap) {
            case 0:
                loadMap("/maps/mapa01_Camada de Blocos 1.csv", 0, mapTileNumLayer1); // Carrega a primeira camada
                loadMap("/maps/mapa01_Camada de Blocos 2.csv", 0, mapTileNumLayer2); // Carrega a segunda camada
                break;
            case 1:
                loadMap("/maps/mapa02_Camada de Blocos 1.csv", 1, mapTileNumLayer1); // Carrega a primeira camada
                loadMap("/maps/mapa02_Camada de Blocos 2.csv", 1, mapTileNumLayer2); // Carrega a segunda camada
                break;
        }
    }

    public void draw(Graphics2D g2) {
        // Renderiza a camada 1
        drawLayer(g2, mapTileNumLayer1);

        // Renderiza a camada 2 por cima da camada 1
        drawLayer(g2, mapTileNumLayer2);
    }

    private void drawLayer(Graphics2D g2, int[][][] mapTileNumLayer) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNumLayer[gp.currentMap][worldCol][worldRow];

            // Desenha apenas se o tile for diferente de -1
            if (tileNum != -1) {
                int worldX = worldCol * gp.tileSize;
                int worldY = worldRow * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

                if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                        worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                        worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                        worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

                    g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                }
            }

            worldCol++;

            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}