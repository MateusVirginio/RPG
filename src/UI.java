import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.Color.*;
import static java.awt.SystemColor.text;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B;
    BufferedImage vida_cheia, vida_meia, sem_vida;
    public int commandNum = 0;

    public UI(GamePanel gp) {
        this.gp = gp;

        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);

        //CRIANDO HUD OBJECT
        Entity heart = new OBJ_Heart(gp);
        vida_cheia = heart.image;
        vida_meia = heart.image2;
        sem_vida = heart.image3;

    }

    public void draw(Graphics2D g2) {

        this.g2 = g2;

        g2.setFont(arial_40);
        g2.setColor(white);

        if (gp.gameState == gp.playState) {
            drawPlayerLife();
        }
        if (gp.gameState == gp.pauseState) {
            drawPlayerLife();
            drawPauseScreen();
        }
        //JANELA DE INICIO
        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
    }

    public void drawPauseScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSADO";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2;

        g2.drawString(text, x, y);
    }

    public int getXforCenteredText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }
        public void drawPlayerLife () {


            int x = gp.tileSize / 2;
            int y = gp.tileSize / 2;
            int i = 0;

            //VIDA CHEIA
            while (i < gp.player.maxlife / 2) {
                if (i < gp.player.life / 2) {
                    g2.drawImage(vida_cheia, x, y, null); // Coração cheio
                } else if (i == gp.player.life / 2 && gp.player.life % 2 == 1) {
                    g2.drawImage(vida_meia, x, y, null); // Meio coração
                } else {
                    g2.drawImage(sem_vida, x, y, null); // Coração vazio
                }
                i++;
                x += gp.tileSize; // Move para a próxima posição
            }

        }
        public void drawTitleScreen () {

            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

            //TITULO DA JANELA
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 60F));
            String text = "CLEITON RASTA: A PEDRA FILOSOFAL";

            int x = getXforCenteredText(text);
            int y = gp.tileSize * 3;

            //SOMBRA
            g2.setColor(gray);
            g2.drawString(text, x + 5, y + 5);
            //COR PRINCIPAL
            g2.setColor(white);
            g2.drawString(text, x, y);
            //MENU
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));

            text = "NOVO JOGO";
            x = getXforCenteredText(text);
            y += gp.tileSize * 3.5;
            g2.drawString(text, x, y);
            if (commandNum == 0) {
                g2.drawString(">", x - gp.tileSize, y);
            }

            text = "CARREGAR JOGO";
            x = getXforCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 1) {
                g2.drawString(">", x - gp.tileSize, y);
            }

            text = "SAIR";
            x = getXforCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 2) {
                g2.drawString(">", x - gp.tileSize, y);

            }
        }
    }
