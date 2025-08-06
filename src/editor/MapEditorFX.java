package editor;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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

    final int originalTileSize = 16;
    final int scale = 3;
    final int tileSize = originalTileSize * scale;
    final int maxWorldCol = 84;
    final int maxWorldRow = 74;
    final int screenWidth = tileSize * 24;
    final int screenHeight = tileSize * 14;

    final int paletteCols = 5;
    final int paletteWidth = paletteCols * tileSize;

    private Map<Integer, Tile> tileMap = new HashMap<>();
    private ImageView selectedTileView;
    private int selectedTileIndex = -1;
    private int activeLayer = 1;

    private MapData mapData;
    private MapCanvas mapCanvas;
    private Stage primaryStage;

    private final NpcData npcData = new NpcData();
    private final MonsterData monsterData = new MonsterData();

    // Campos da aba de NPC
    private TextField npcNameField;
    private TextArea npcDialoguesArea;
    private Button addNpcButton;
    private BufferedImage npcImage;

    // Campos da aba de Monstros
    private TextField monsterNameField;
    private TextField monsterMaxLifeField;
    private TextField monsterSpeedField;
    private Button addMonsterButton;
    private BufferedImage monsterImage;

    // Campos da aba de Player
    private TextField playerMaxLifeField;
    private TextField playerSpeedField;
    private TextField playerWorldXField;
    private TextField playerWorldYField;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("RPG Map Editor - JavaFX");

        BorderPane root = new BorderPane();
        Pane mapPane = new Pane();
        mapPane.setPrefSize(screenWidth, screenHeight);
        mapPane.setStyle("-fx-background-color: #36454F;");

        mapData = new MapData(maxWorldCol, maxWorldRow);
        mapCanvas = new MapCanvas(screenWidth, screenHeight, mapData, tileSize, this);
        mapPane.getChildren().add(mapCanvas);

        TabPane tabPane = new TabPane();
        tabPane.setPrefSize(paletteWidth, screenHeight);
        tabPane.getTabs().addAll(createMapTab(), createNpcTab(), createMonsterTab(), createPlayerTab());

        loadNpcImage();
        loadMonsterImage();

        root.setCenter(mapPane);
        root.setRight(tabPane);

        Scene scene = new Scene(root, screenWidth + paletteWidth, screenHeight);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Tab createMapTab() {
        Tab mapTab = new Tab("Mapas");
        mapTab.setClosable(false);

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        loadTiles();

        VBox paletteBox = new VBox(10);
        displayTilePalette(paletteBox);

        HBox buttonsBox = new HBox(10);
        Button saveButton = new Button("Salvar Mapa");
        saveButton.setOnAction(e -> saveMap());

        Button loadButton = new Button("Carregar Mapa");
        loadButton.setOnAction(e -> loadMap());

        Button clearButton = new Button("Limpar Mapa");
        clearButton.setOnAction(e -> {
            mapData.clearMap(maxWorldCol, maxWorldRow);
            mapCanvas.draw();
        });

        buttonsBox.getChildren().addAll(saveButton, loadButton, clearButton);

        ComboBox<String> layerSelector = new ComboBox<>(FXCollections.observableArrayList("Camada 1", "Camada 2"));
        layerSelector.setValue("Camada 1");
        layerSelector.setOnAction(e -> {
            activeLayer = layerSelector.getValue().equals("Camada 1") ? 1 : 2;
        });

        content.getChildren().addAll(new Label("Paleta de Tiles"), paletteBox, layerSelector, buttonsBox);
        mapTab.setContent(content);
        return mapTab;
    }

    private Tab createNpcTab() {
        Tab npcTab = new Tab("NPCs");
        npcTab.setClosable(false);

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        Label nameLabel = new Label("Nome do NPC:");
        npcNameField = new TextField();

        Label dialoguesLabel = new Label("Diálogos (separe por vírgulas):");
        npcDialoguesArea = new TextArea();
        npcDialoguesArea.setWrapText(true);

        addNpcButton = new Button("Adicionar NPC ao Mapa");
        addNpcButton.setOnAction(e -> {
            String name = npcNameField.getText();
            String dialogues = npcDialoguesArea.getText();
            if (!name.isEmpty() && !dialogues.isEmpty()) {
                npcData.addNpcToPlace(name, dialogues.split(","));
                mapCanvas.draw();
                System.out.println("NPC '" + name + "' pronto para ser posicionado. Clique no mapa.");
            }
        });

        HBox npcButtonsBox = new HBox(10);
        Button saveNpcButton = new Button("Salvar NPCs");
        saveNpcButton.setOnAction(e -> saveNpcs());

        Button loadNpcButton = new Button("Carregar NPCs");
        loadNpcButton.setOnAction(e -> loadNpcs());

        npcButtonsBox.getChildren().addAll(saveNpcButton, loadNpcButton);

        content.getChildren().addAll(new Label("Configuração de NPC"), nameLabel, npcNameField, dialoguesLabel, npcDialoguesArea, addNpcButton, npcButtonsBox);
        npcTab.setContent(content);
        return npcTab;
    }

    private Tab createMonsterTab() {
        Tab monsterTab = new Tab("Monstros");
        monsterTab.setClosable(false);

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        Label nameLabel = new Label("Nome do Monstro:");
        monsterNameField = new TextField();

        Label maxLifeLabel = new Label("Vida Máxima:");
        monsterMaxLifeField = new TextField();

        Label speedLabel = new Label("Velocidade:");
        monsterSpeedField = new TextField();

        addMonsterButton = new Button("Adicionar Monstro ao Mapa");
        addMonsterButton.setOnAction(e -> {
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
        });

        HBox monsterButtonsBox = new HBox(10);
        Button saveMonsterButton = new Button("Salvar Monstros");
        saveMonsterButton.setOnAction(e -> saveMonsters());

        Button loadMonsterButton = new Button("Carregar Monstros");
        loadMonsterButton.setOnAction(e -> loadMonsters());

        monsterButtonsBox.getChildren().addAll(saveMonsterButton, loadMonsterButton);

        content.getChildren().addAll(new Label("Configuração de Monstro"), nameLabel, monsterNameField, maxLifeLabel, monsterMaxLifeField, speedLabel, monsterSpeedField, addMonsterButton, monsterButtonsBox);
        monsterTab.setContent(content);
        return monsterTab;
    }

    private Tab createPlayerTab() {
        Tab playerTab = new Tab("Jogador");
        playerTab.setClosable(false);

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        Label maxLifeLabel = new Label("Vida Máxima:");
        playerMaxLifeField = new TextField("6");

        Label speedLabel = new Label("Velocidade:");
        playerSpeedField = new TextField("4");

        Label posLabel = new Label("Posição Inicial (x, y):");
        HBox posBox = new HBox(10);
        playerWorldXField = new TextField("40");
        playerWorldYField = new TextField("37");
        posBox.getChildren().addAll(playerWorldXField, playerWorldYField);

        Button setPlayerButton = new Button("Atualizar Posição do Jogador");
        setPlayerButton.setOnAction(e -> {
            try {
                int x = Integer.parseInt(playerWorldXField.getText());
                int y = Integer.parseInt(playerWorldYField.getText());
                mapCanvas.updatePlayerPosition(x, y);
            } catch (NumberFormatException ex) {
                System.err.println("Por favor, insira valores numéricos válidos para a posição do jogador.");
            }
        });

        HBox playerButtonsBox = new HBox(10);
        Button savePlayerButton = new Button("Salvar Atributos");
        savePlayerButton.setOnAction(e -> savePlayerData());

        Button loadPlayerButton = new Button("Carregar Atributos");
        loadPlayerButton.setOnAction(e -> loadPlayerData());

        playerButtonsBox.getChildren().addAll(savePlayerButton, loadPlayerButton);

        content.getChildren().addAll(new Label("Atributos do Jogador"), maxLifeLabel, playerMaxLifeField, speedLabel, playerSpeedField, posLabel, posBox, setPlayerButton, playerButtonsBox);
        playerTab.setContent(content);
        return playerTab;
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

    private void loadTiles() {
        setup(0, "grama_2", false);
        setup(25, "grama_1", false);
        setup(99, "arbusto", true);
        setup(65, "parede_frente_esquerda_1-4", true);
        setup(66, "parede_frente_meio_1-5", true);
        setup(67, "parede_frente_direita_1-4", true);
        setup(169, "bloco_1-2", true);
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

    private void displayTilePalette(VBox paletteBox) {
        GridPane tileGrid = new GridPane();
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
                imageView.setStyle("-fx-effect: innershadow(gaussian, green, 10, 1.0, 0, 0);");
            });

            tileGrid.add(imageView, col, row);
            col++;
            if (col >= paletteCols) {
                col = 0;
                row++;
            }
        }
        paletteBox.getChildren().add(tileGrid);
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

    public static void main(String[] args) {
        launch(args);
    }
}