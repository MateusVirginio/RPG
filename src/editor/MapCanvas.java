package editor;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.awt.image.BufferedImage;
import editor.NpcData.NpcPlacement;
import editor.MonsterData.MonsterPlacement;
import javafx.scene.image.Image;

public class MapCanvas extends Canvas {
    private final MapData mapData;
    private final int tileSize;
    private final MapEditorFX editor;
    private int playerWorldX, playerWorldY;

    public MapCanvas(double width, double height, MapData mapData, int tileSize, MapEditorFX editor) {
        super(width, height);
        this.mapData = mapData;
        this.tileSize = tileSize;
        this.editor = editor;

        this.playerWorldX = editor.maxWorldCol / 2;
        this.playerWorldY = editor.maxWorldRow / 2;

        draw();

        this.setOnMouseClicked(event -> {
            int col = (int) (event.getX() / tileSize);
            int row = (int) (event.getY() / tileSize);

            if (editor.isDeleteMode()) {
                int activeLayer = this.editor.getActiveLayer();
                if (activeLayer == 1) {
                    mapData.setMapLayer1Tile(col, row, -1);
                } else {
                    mapData.setMapLayer2Tile(col, row, -1);
                }

                editor.getNpcData().getPlacedNpcs().removeIf(npc -> npc.col == col && npc.row == row);
                editor.getMonsterData().getPlacedMonsters().removeIf(monster -> monster.col == col && monster.row == row);

                draw();
            } else if (editor.isFillMode()) {
                int selectedTileIndex = this.editor.getSelectedTileIndex();
                if (selectedTileIndex != -1) {
                    mapData.fill(col, row, selectedTileIndex, editor.getActiveLayer());
                    draw();
                }
            } else {
                int selectedTileIndex = this.editor.getSelectedTileIndex();
                int activeLayer = this.editor.getActiveLayer();
                int brushSize = editor.getBrushSize();

                if (selectedTileIndex != -1) {
                    mapData.drawBrush(col, row, selectedTileIndex, activeLayer, brushSize);
                    draw();
                } else if (editor.getNpcData().isNpcToPlace()) {
                    editor.getNpcData().placeNpc(col, row);
                    draw();
                } else if (editor.getMonsterData().isMonsterToPlace()) {
                    editor.getMonsterData().placeMonster(col, row);
                    draw();
                }
            }
        });
    }

    public void updatePlayerPosition(int x, int y) {
        this.playerWorldX = x;
        this.playerWorldY = y;
        draw();
    }

    public void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        drawLayers(gc);
        drawGrid(gc);
    }

    private void drawLayers(GraphicsContext gc) {
        drawLayer(gc, mapData.getMapLayer1());
        drawLayer(gc, mapData.getMapLayer2());
        drawNpcs(gc);
        drawMonsters(gc);
        drawPlayer(gc, playerWorldX, playerWorldY);
        if (editor.isCollisionOverlayMode()) {
            drawCollisionOverlay(gc);
        }
    }

    private void drawLayer(GraphicsContext gc, int[][] layerData) {
        for (int col = 0; col < editor.maxWorldCol; col++) {
            for (int row = 0; row < editor.maxWorldRow; row++) {
                int tileIndex = layerData[col][row];
                if (tileIndex != -1 && editor.getTile(tileIndex) != null) {
                    BufferedImage bufferedImage = editor.getTile(tileIndex).image;
                    javafx.scene.image.Image fxImage = editor.convertToFxImage(bufferedImage);
                    if (fxImage != null) {
                        gc.drawImage(fxImage, col * tileSize, row * tileSize, tileSize, tileSize);
                    }
                }
            }
        }
    }

    private void drawNpcs(GraphicsContext gc) {
        for (NpcPlacement npc : editor.getNpcData().getPlacedNpcs()) {
            BufferedImage npcImage = editor.getNpcImage();
            if (npcImage != null) {
                Image fxImage = editor.convertToFxImage(npcImage);
                if (fxImage != null) {
                    gc.drawImage(fxImage, npc.col * tileSize, npc.row * tileSize, tileSize, tileSize);
                }
            }
        }
    }

    private void drawMonsters(GraphicsContext gc) {
        for (MonsterPlacement monster : editor.getMonsterData().getPlacedMonsters()) {
            BufferedImage monsterImage = editor.getMonsterImage();
            if (monsterImage != null) {
                Image fxImage = editor.convertToFxImage(monsterImage);
                if (fxImage != null) {
                    gc.drawImage(fxImage, monster.col * tileSize, monster.row * tileSize, tileSize, tileSize);
                }
            }
        }
    }

    private void drawPlayer(GraphicsContext gc, int x, int y) {
        gc.setFill(Color.BLUE);
        gc.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
    }

    private void drawCollisionOverlay(GraphicsContext gc) {
        gc.setFill(Color.rgb(255, 0, 0, 0.4));
        for (int col = 0; col < editor.maxWorldCol; col++) {
            for (int row = 0; row < editor.maxWorldRow; row++) {
                int tileIndex = mapData.getMapLayer1()[col][row];
                if (tileIndex != -1 && editor.getTile(tileIndex).collision) {
                    gc.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
                }
                tileIndex = mapData.getMapLayer2()[col][row];
                if (tileIndex != -1 && editor.getTile(tileIndex).collision) {
                    gc.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
                }
            }
        }
    }

    private void drawGrid(GraphicsContext gc) {
        gc.setStroke(Color.GRAY);
        for (int x = 0; x < getWidth(); x += tileSize) {
            gc.strokeLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += tileSize) {
            gc.strokeLine(0, y, getWidth(), y);
        }
    }
}