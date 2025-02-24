
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
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
    public final int maxWorldCol = 24;
    public final int maxWorldRow = 14;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    //TELA CHEIA
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;

    //FPS
    int FPS = 60;

    //SISTEMA
    TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public ChecarColisao Colisao = new ChecarColisao(this);
    public UI ui = new UI(this);
    public AssetSetter aSetter = new AssetSetter(this);
    Thread gameThread;

    //ENTIDADES E OBJETOS
    public Player player = new Player(this, keyH);
    public Entity monster[] = new Entity[20];
    ArrayList<Entity> entityList = new ArrayList<>();

    //ESTADO DO JOGO
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int titleState = 0;


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
        aSetter.setMonster();
        gameState = titleState;

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();

        setFullScreen();
    }

    public void startGameThread() {
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
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    if(monster[i].alive == true && monster[i].dying == false) {
                        monster[i].update();
                    }
                    if(monster[i].alive == false) {
                        monster[i] = null;

                    }
                }
            }
        }
        if (gameState == pauseState) {

        }
    }

    public void drawToTempScreen(){
        g2.clearRect(0, 0, screenWidth2, screenHeight2);
        if (gameState == titleState) {
            ui.draw(g2);
        } else {
            tileM.draw(g2);
            entityList.add(player);
            //System.out.println("Jogador adicionado à entityList");
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    entityList.add(monster[i]);
                    //System.out.println("Monstro " + i + " adicionado à entityList: " + monster[i].name);
                }
            }
            //SORT
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    return Integer.compare(e1.worldY, e2.worldY);
                }
            });
            // DESENHAR ENTIDADES
            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }
            // LIMPAR A LISTA APÓS DESENHAR
            entityList.clear(); // Limpa a lista uma única vez

            //ESTADO DE PAUSA
            //if (gameState == pauseState || gameState == playState) {
            ui.draw(g2);
        }
    }
    public void drawToScreen(){
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }
    }

