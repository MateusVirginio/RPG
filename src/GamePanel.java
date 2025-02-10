
import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    //Configs da tela
    final int originalTileSize = 16; // 16x16 titulo
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48 titulo
    public final int maxScreenCol = 24;
    public final int maxScreenRow = 14;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    //Configs do mundo
    public final int maxWorldCol = 24;
    public final int maxWorldRow = 14;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    //FPS
    int FPS = 60;

    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;
    public ChecarColisao Colisao = new ChecarColisao(this);
    public UI ui = new UI(this);
    Player player = new Player(this,keyH);

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

    public void setupGame() {
        gameState = playState;
        gameState = titleState;

    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    public void run() {

        double drawInterval = 1000000000/FPS; // 0.01666 seconds
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(gameThread != null) {

        update();

        repaint();


            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000;

                if(remainingTime < 0) {
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

        if(gameState == playState){
            player.update();
        }
        if(gameState == pauseState) {

        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        //TELA DE INICIO
        if(gameState == titleState) {
            ui.draw(g2);
        }
        //ESTADO DE PAUSA
        else if (gameState == pauseState) {
            tileM.draw(g2);
            player.draw(g2);
            ui.draw(g2);
        }
        else if (gameState == playState) {
            tileM.draw(g2);
            player.draw(g2);
            ui.draw(g2);
        }

        g2.dispose();
    }
}
