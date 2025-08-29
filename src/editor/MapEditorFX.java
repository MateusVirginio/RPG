package editor;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tile.Tile;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapEditorFX extends Application {

    @FXML private BorderPane root;
    @FXML private Pane mapPane;
    @FXML private TabPane tabPane;
    @FXML private GridPane tileGrid;
    @FXML private ComboBox<String> layerSelector;
    @FXML private Button deleteButton;
    @FXML private Button fillButton;
    @FXML private Button collisionButton;
    @FXML private Slider brushSizeSlider;
    @FXML private TextField npcNameField;
    @FXML private TextArea npcDialoguesArea;
    @FXML private TextField monsterNameField;
    @FXML private TextField monsterMaxLifeField;
    @FXML private TextField monsterSpeedField;
    @FXML private TextField playerMaxLifeField;
    @FXML private TextField playerSpeedField;
    @FXML private TextField playerWorldXField;
    @FXML private TextField playerWorldYField;
    @FXML private ImageView previewImageView;
    @FXML private Label previewLabel;

    final int originalTileSize = 16;
    final int scale = 3;
    final int tileSize = originalTileSize * scale;
    final int maxWorldCol = 84;
    final int maxWorldRow = 74;
    final int screenWidth = tileSize * 24;
    final int screenHeight = tileSize * 14;
    final int paletteCols = 5;

    private Map<Integer, Tile> tileMap = new HashMap<>();
    private ImageView selectedTileView;
    private int selectedTileIndex = -1;
    private int activeLayer = 1;
    private boolean deleteMode = false;
    private boolean fillMode = false;
    private boolean collisionOverlayMode = false;
    private int brushSize = 1;

    private MapData mapData;
    private MapCanvas mapCanvas;
    private Stage primaryStage;

    private final NpcData npcData = new NpcData();
    private final MonsterData monsterData = new MonsterData();
    private BufferedImage npcImage;
    private BufferedImage monsterImage;

    private BufferedImage grassSpritesheet;
    private BufferedImage dungeonSpritesheet;
    private BufferedImage propsSpritesheet;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/editor/MapEditor.fxml"));
        loader.setController(this);
        Parent root = loader.load();

        mapData = new MapData(maxWorldCol, maxWorldRow);
        mapCanvas = new MapCanvas(screenWidth, screenHeight, mapData, tileSize, this);

        Pane centerPane = (Pane) root.lookup("#mapPane");
        if (centerPane != null) {
            centerPane.getChildren().add(mapCanvas);
        } else {
            System.err.println("MapPane não encontrado no FXML. Por favor, verifique o fx:id.");
        }

        loadNpcImage();
        loadMonsterImage();
        loadSpritesheets();
        displayTilePalette(tileGrid);

        layerSelector.setItems(FXCollections.observableArrayList("Camada 1", "Camada 2"));
        layerSelector.setValue("Camada 1");
        layerSelector.setOnAction(e -> {
            activeLayer = layerSelector.getValue().equals("Camada 1") ? 1 : 2;
        });

        brushSizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            brushSize = newVal.intValue();
        });

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/editor/css/styles.css").toExternalForm());

        primaryStage.setTitle("RPG Map Editor - JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void saveMap() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Mapa");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file1 = fileChooser.showSaveDialog(primaryStage);
        if (file1 != null) {
            File file2 = new File(file1.getAbsolutePath().replace(".csv", "_camada2.csv"));
            MapSaver.saveMapToCsv(file1.getAbsolutePath(), mapData.getMapLayer1(), maxWorldCol, maxWorldRow);
            MapSaver.saveMapToCsv(file2.getAbsolutePath(), mapData.getMapLayer2(), maxWorldCol, maxWorldRow);
            System.out.println("Mapas salvos em: " + file1.getAbsolutePath() + " e " + file2.getAbsolutePath());
        }
    }

    @FXML
    private void loadMap() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Carregar Mapa");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file1 = fileChooser.showOpenDialog(primaryStage);
        if (file1 != null) {
            File file2 = new File(file1.getAbsolutePath().replace("_camada1.csv", "_camada2.csv"));
            mapData.loadMapFromCsv(file1.getAbsolutePath(), 1);
            if (file2.exists()) {
                mapData.loadMapFromCsv(file2.getAbsolutePath(), 2);
            }
            mapCanvas.draw();
            System.out.println("Mapa carregado de: " + file1.getAbsolutePath());
        }
    }

    @FXML
    private void clearMap() {
        mapData.clearMap();
        mapCanvas.draw();
    }

    @FXML
    private void toggleDeleteMode() {
        deleteMode = !deleteMode;
        fillMode = false;
        if (deleteMode) {
            deleteButton.setStyle("-fx-background-color: #ff3333;");
            fillButton.setStyle(null);
            System.out.println("Modo de deleção ativado.");
        } else {
            deleteButton.setStyle(null);
            System.out.println("Modo de deleção desativado.");
        }
    }

    @FXML
    private void toggleFillMode() {
        fillMode = !fillMode;
        deleteMode = false;
        if (fillMode) {
            fillButton.setStyle("-fx-background-color: #33ccff;");
            deleteButton.setStyle(null);
            System.out.println("Modo de preenchimento ativado.");
        } else {
            fillButton.setStyle(null);
            System.out.println("Modo de preenchimento desativado.");
        }
    }

    @FXML
    private void toggleCollisionOverlay() {
        collisionOverlayMode = !collisionOverlayMode;
        mapCanvas.draw();
    }

    @FXML
    private void addNpcToMap() {
        String name = npcNameField.getText();
        String dialogues = npcDialoguesArea.getText();
        if (!name.isEmpty() && !dialogues.isEmpty()) {
            npcData.addNpcToPlace(name, dialogues.split(","));
            mapCanvas.draw();
            System.out.println("NPC '" + name + "' pronto para ser posicionado. Clique no mapa.");
        }
    }

    @FXML
    private void saveNpcs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar NPCs");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("NPC Files", "*.npc"));

        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            NpcDataSaver.saveNpcsToFile(file.getAbsolutePath(), npcData.getPlacedNpcs());
            System.out.println("NPCs salvos em: " + file.getAbsolutePath());
        }
    }

    @FXML
    private void loadNpcs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Carregar NPCs");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("NPC Files", "*.npc"));

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            npcData.clearNpcs();
            NpcDataSaver.loadNpcsFromFile(file.getAbsolutePath(), npcData);
            mapCanvas.draw();
            System.out.println("NPCs carregados de: " + file.getAbsolutePath());
        }
    }

    @FXML
    private void addMonsterToMap() {
        try {
            String name = monsterNameField.getText();
            int maxLife = Integer.parseInt(monsterMaxLifeField.getText());
            int speed = Integer.parseInt(monsterSpeedField.getText());
            if (!name.isEmpty()) {
                monsterData.addMonsterToPlace(name, maxLife, speed);
                mapCanvas.draw();
                System.out.println("Monstro '" + name + "' pronto para ser posicionado. Clique no mapa.");
            }
        } catch (NumberFormatException ex) {
            System.err.println("Por favor, insira valores numéricos válidos para Vida e Velocidade.");
        }
    }

    @FXML
    private void saveMonsters() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Monstros");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Monster Files", "*.monster"));

        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            MonsterDataSaver.saveMonstersToFile(file.getAbsolutePath(), monsterData.getPlacedMonsters());
            System.out.println("Monstros salvos em: " + file.getAbsolutePath());
        }
    }

    @FXML
    private void loadMonsters() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Carregar Monstros");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Monster Files", "*.monster"));

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            monsterData.clearMonsters();
            MonsterDataSaver.loadMonstersFromFile(file.getAbsolutePath(), monsterData);
            mapCanvas.draw();
            System.out.println("Monstros carregados de: " + file.getAbsolutePath());
        }
    }

    @FXML
    private void updatePlayerPosition() {
        try {
            int x = Integer.parseInt(playerWorldXField.getText());
            int y = Integer.parseInt(playerWorldYField.getText());
            mapCanvas.updatePlayerPosition(x, y);
        } catch (NumberFormatException ex) {
            System.err.println("Por favor, insira valores numéricos válidos para a posição do jogador.");
        }
    }

    @FXML
    private void savePlayerData() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Dados do Jogador");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Player Data Files", "*.player"));

        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            try {
                int maxLife = Integer.parseInt(playerMaxLifeField.getText());
                int speed = Integer.parseInt(playerSpeedField.getText());
                int startX = Integer.parseInt(playerWorldXField.getText());
                int startY = Integer.parseInt(playerWorldYField.getText());

                PlayerData playerData = new PlayerData(maxLife, speed, startX, startY);
                PlayerDataSaver.savePlayerData(file.getAbsolutePath(), playerData);
                System.out.println("Dados do jogador salvos em: " + file.getAbsolutePath());
            } catch (NumberFormatException e) {
                System.err.println("Erro: dados do jogador inválidos. Por favor, insira valores numéricos.");
            }
        }
    }

    @FXML
    private void loadPlayerData() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Carregar Dados do Jogador");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Player Data Files", "*.player"));

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            PlayerData playerData = PlayerDataSaver.loadPlayerData(file.getAbsolutePath());
            if (playerData != null) {
                playerMaxLifeField.setText(String.valueOf(playerData.maxLife));
                playerSpeedField.setText(String.valueOf(playerData.speed));
                playerWorldXField.setText(String.valueOf(playerData.startX));
                playerWorldYField.setText(String.valueOf(playerData.startY));
                mapCanvas.updatePlayerPosition(playerData.startX, playerData.startY);
                System.out.println("Dados do jogador carregados de: " + file.getAbsolutePath());
            }
        }
    }

    private void loadNpcImage() {
        try {
            this.npcImage = ImageIO.read(getClass().getResourceAsStream("/res/npc/npc_down_1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMonsterImage() {
        try {
            this.monsterImage = ImageIO.read(getClass().getResourceAsStream("/res/monstros/skeleton_down1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSpritesheets() {
        try {
            this.grassSpritesheet = ImageIO.read(getClass().getResourceAsStream("/res/tiles/Grass.png"));
            this.dungeonSpritesheet = ImageIO.read(getClass().getResourceAsStream("/res/tiles/Dungeon.png"));
            this.propsSpritesheet = ImageIO.read(getClass().getResourceAsStream("/res/tiles/Props.png"));

            splitAndSetup(grassSpritesheet, 8, 8, 0, 0, 0, false);
            splitAndSetup(grassSpritesheet, 8, 8, 4, 0, 25, false);

            splitAndSetup(dungeonSpritesheet, 18, 18, 0, 0, 64, true);
            splitAndSetup(dungeonSpritesheet, 18, 18, 0, 10, 248, false);
            splitAndSetup(dungeonSpritesheet, 18, 18, 0, 12, 288, true);
            splitAndSetup(dungeonSpritesheet, 18, 18, 0, 14, 324, false);

            splitAndSetup(propsSpritesheet, 10, 10, 0, 0, 400, true);
            splitAndSetup(propsSpritesheet, 10, 10, 0, 1, 410, false);
            splitAndSetup(propsSpritesheet, 10, 10, 0, 2, 420, false);

            setup(0, "grama_2", false);
            setup(25, "grama_1", false);
            setup(99, "arbusto", true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void splitAndSetup(BufferedImage sheet, int sheetCols, int sheetRows, int startCol, int startRow, int startIndex, boolean collision) {
        int tileWidth = sheet.getWidth() / sheetCols;
        int tileHeight = sheet.getHeight() / sheetRows;

        int index = startIndex;
        for (int row = startRow; row < sheetRows; row++) {
            for (int col = startCol; col < sheetCols; col++) {
                if (index < tileMap.size() + startIndex) {
                    continue;
                }
                BufferedImage subImage = sheet.getSubimage(col * tileWidth, row * tileHeight, tileWidth, tileHeight);
                BufferedImage scaledImage = scaleImage(subImage, tileSize, tileSize);

                Tile newTile = new Tile();
                newTile.image = scaledImage;
                newTile.collision = collision;
                tileMap.put(index, newTile);
                index++;
            }
        }
    }

    private void setup(int index, String imagePath, boolean collision) {
        try {
            BufferedImage originalImage = ImageIO.read(getClass().getResourceAsStream("/res/tiles/" + imagePath + ".png"));
            if (originalImage != null) {
                BufferedImage scaledImage = scaleImage(originalImage, tileSize, tileSize);
                Tile newTile = new Tile();
                newTile.image = scaledImage;
                newTile.collision = collision;
                tileMap.put(index, newTile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();
        return scaledImage;
    }

    private void displayTilePalette(GridPane tileGrid) {
        int col = 0;
        int row = 0;

        for (Map.Entry<Integer, Tile> entry : tileMap.entrySet()) {
            int tileIndex = entry.getKey();
            BufferedImage bufferedImage = entry.getValue().image;
            javafx.scene.image.Image fxImage = convertToFxImage(bufferedImage);
            ImageView imageView = new ImageView(fxImage);
            imageView.setFitWidth(tileSize);
            imageView.setFitHeight(tileSize);

            imageView.setOnMouseClicked(e -> {
                if (selectedTileView != null) {
                    selectedTileView.setStyle(null);
                }
                selectedTileView = imageView;
                selectedTileIndex = tileIndex;
                imageView.setStyle("-fx-effect: innershadow(gaussian, #00FF00, 10, 1.0, 0, 0);");
                updateSelectedTilePreview(entry.getValue());
            });

            tileGrid.add(imageView, col, row);
            col++;
            if (col >= paletteCols) {
                col = 0;
                row++;
            }
        }
    }

    private void updateSelectedTilePreview(Tile tile) {
        javafx.scene.image.Image fxImage = convertToFxImage(tile.image);
        previewImageView.setImage(fxImage);
        previewLabel.setText(String.format("Índice: %d\nColisão: %b", selectedTileIndex, tile.collision));
    }

    public javafx.scene.image.Image convertToFxImage(BufferedImage image) {
        if (image == null) {
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", out);
            byte[] bytes = out.toByteArray();
            return new javafx.scene.image.Image(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getSelectedTileIndex() {
        return selectedTileIndex;
    }

    public Tile getTile(int index) {
        return tileMap.get(index);
    }

    public int getActiveLayer() {
        return activeLayer;
    }

    public NpcData getNpcData() {
        return npcData;
    }

    public MonsterData getMonsterData() {
        return monsterData;
    }

    public BufferedImage getNpcImage() {
        return npcImage;
    }

    public BufferedImage getMonsterImage() {
        return monsterImage;
    }

    public boolean isDeleteMode() {
        return deleteMode;
    }

    public boolean isFillMode() {
        return fillMode;
    }

    public int getBrushSize() {
        return brushSize;
    }

    public boolean isCollisionOverlayMode() {
        return collisionOverlayMode;
    }

    public static void main(String[] args) {
        launch(args);
    }
}