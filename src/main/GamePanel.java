package main;

import ai.PathFinder;
import data.SaveLoad;
import editor.MonsterData;
import editor.MonsterDataSaver;
import editor.NpcData;
import editor.NpcDataSaver;
import editor.PlayerData;
import editor.PlayerDataSaver;
import entity.Entity;
import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable {

    //Configs da tela
    final int originalTileSize = 16; // 16x16 titulo
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48 titulo
    public final int maxScreenCol = 24;
    public final int maxScreenRow = 14;
    public final int screenWidth = tileSize * maxScreenCol; // 960 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    //Configs do mundo
    public final int maxWorldCol = 84;
    public final int maxWorldRow = 74;
    public final int maxMap = 10;
    public int currentMap = 0;

    //TELA CHEIA
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    public boolean fullScreenOn = false;
    BufferedImage tempScreen;
    Graphics2D g2;

    //FPS
    int FPS = 60;

    //SISTEMA
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public CheckCollision Colisao = new CheckCollision(this);
    public UI ui = new UI(this);
    public main.Event event = new Event(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public PathFinder pFinder = new PathFinder(this);
    public SaveLoad saveLoad = new SaveLoad(this);
    Config config = new Config(this);
    Thread gameThread;

    //ENTIDADES E OBJETOS
    public Player player;
    public Entity monster[][] = new Entity[maxMap][20];
    public Entity npc[][] = new Entity[maxMap][10];
    ArrayList<Entity> entityList = new ArrayList<>();

    // Dados carregados do editor
    public NpcData npcData;
    public MonsterData monsterData;
    public PlayerData playerData;

    //ESTADO DO JOGO
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int optionState = 4;
    public final int gameOverState = 5;
    public final int transitionState = 6;


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setFullScreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        Main.window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        screenWidth2 = (int) width;
        screenHeight2 = (int) height;
    }

    public void setupGame() {
        this.player = new Player(this, keyH);
        loadAllData();
        aSetter.setMonster();
        aSetter.setNpc();
        gameState = titleState;
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();

        if (fullScreenOn == true) {
            setFullScreen();
        }
    }

    public void loadAllData() {
        this.npcData = new NpcData();
        this.monsterData = new MonsterData();

        // Carregar dados de mapas, NPCs, monstros e jogador do editor
        // Supondo que os arquivos do editor são "mapa_camada1.csv" e "mapa_camada2.csv"
        tileM.loadMap("/res/maps/mapa01_Camada de Blocos 1.csv", 0, tileM.mapTileNumLayer1);
        tileM.loadMap("/res/maps/mapa01_Camada de Blocos 2.csv", 0, tileM.mapTileNumLayer2);

        NpcDataSaver.loadNpcsFromFile("npcs.npc", npcData);
        MonsterDataSaver.loadMonstersFromFile("monsters.monster", monsterData);
        this.playerData = PlayerDataSaver.loadPlayerData("player.player");

        if (playerData != null) {
            player.worldX = playerData.startX * tileSize;
            player.worldY = playerData.startY * tileSize;
            player.maxlife = playerData.maxLife;
            player.life = playerData.maxLife;
            player.speed = playerData.speed;
        }
    }

    public void retry() {
        currentMap = 0;
        player.setDefaultPositions();
        player.restoreLife();
        aSetter.setNpc();
        aSetter.setMonster();
    }

    public void restart() {
        currentMap = 0;
        player.setDefaultValues();
        player.setDefaultPositions();
        player.restoreLife();
        aSetter.setNpc();
        aSetter.setMonster();
    }

    public void startGameThread() {
        saveLoad.load();
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        double drawInterval = 1000000000 / FPS; // 0.01666 seconds
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {

            update();
            drawToTempScreen(); //DESENHA TUDO PARA UMA TELA TEMPORARIA
            drawToScreen(); //DESENHA PARA A TELA DO JOGO

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update() {
        if (gameState == playState) {
            player.update();
            for (int i = 0; i < monster[currentMap].length; i++) {
                if (monster[currentMap][i] != null) {
                    if (monster[currentMap][i].alive == true && monster[currentMap][i].dying == false) {
                        monster[currentMap][i].update();
                    }
                    if (monster[currentMap][i].alive == false) {
                        monster[currentMap][i] = null;
                    }
                }
            }
            for (int i = 0; i < npc[currentMap].length; i++) {
                if (npc[currentMap][i] != null) {
                    npc[currentMap][i].update();
                }
            }
            if (gameState == pauseState) {

            }
        }
    }

    public void drawToTempScreen() {
        g2.clearRect(0, 0, screenWidth2, screenHeight2);
        if (gameState == titleState) {
            ui.draw(g2);
        } else {
            tileM.draw(g2);
            entityList.add(player);
            for (int i = 0; i < monster[currentMap].length; i++) {
                if (monster[currentMap][i] != null) {
                    entityList.add(monster[currentMap][i]);
                }
            }
            for (int i = 0; i < npc[currentMap].length; i++) {
                if (npc[currentMap][i] != null) {
                    entityList.add(npc[currentMap][i]);
                }
            }
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    return Integer.compare(e1.worldY, e2.worldY);
                }
            });
            for (int j = 0; j < entityList.size(); j++) {
                entityList.get(j).draw(g2);
            }
            entityList.clear();
            ui.draw(g2);
        }
    }

    public void drawToScreen() {
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }
}