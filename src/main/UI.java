package main;

import entity.Entity;
import object.OBJ_Heart;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.Color.*;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B;
    BufferedImage vida_cheia, vida_meia, sem_vida;
    public int commandNum = 0;
    int subState = 0;
    public int counter = 0;
    public String currentDialogue = "";

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
        //JANELA DE INICIO
        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
        //DIALOGOS
        if (gp.gameState == gp.dialogueState) {
            drawDialogueScreen();
        }
        //JANELA DE OPÇÕES
        if (gp.gameState == gp.optionState) {
            drawOptionsScreen();
        }
        //FINAL DE JOGO
        if (gp.gameState == gp.gameOverState) {
            drawGameOverScreen();
        }
        //TRANSIÇÃO DE MAPA
        if (gp.gameState == gp.transitionState) {
            drawTransition();
        }
    }

    public void drawPlayerLife() {

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

    public void drawDialogueScreen() {

        //JANELA
        int x = gp.tileSize*2;
        int y = gp.tileSize/2;
        int width = gp.screenWidth - (gp.tileSize*4);
        int height = gp.tileSize*4;
        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));
        x += gp.tileSize;
        y += gp.tileSize;

        for(String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }

    public void drawTitleScreen() {

        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        //TITULO DA JANELA
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 60F));
        String text = "ShadowBlade";

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

    public void drawGameOverScreen() {

        g2.setColor(new Color(0,0,0,150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));

        text = "VOCÊ FOI MORTO";
        //Sombra
        g2.setColor(black);
        x = getXforCenteredText(text);
        y = gp.tileSize*4;
        g2.drawString(text, x, y);
        //Principal
        g2.setColor(white);
        g2.drawString(text, x-4, y-4);
        //Tentar Novamente
        g2.setFont(g2.getFont().deriveFont(50f));
        text = "Recomeçar";
        x = getXforCenteredText(text);
        y += gp.tileSize*4;
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x-40, y);
        }

        //Voltar pra Tela Inicial
        text = "Sair";
        x = getXforCenteredText(text);
        y += 55;
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawString(">", x-40, y);
        }
    }

    public void drawOptionsScreen() {

        g2.setColor(white);
        g2.setFont(g2.getFont().deriveFont(32F));

        //SUB JANELA
        int frameX = gp.tileSize * 8;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 8;
        int frameHeight = gp.tileSize * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch (subState) {
            case 0: optionsTop(frameX, frameY); break;
            case 1: optionsFullScreenNotification(frameX, frameY); break;
            case 2: optionsControl(frameX, frameY); break;
            case 3: optionsEndGame(frameX, frameY); break;
        }
        gp.keyH.enterPressed = false;
    }

    public void optionsTop(int frameX, int frameY){

        int textX;
        int textY;

        //TITULO
        String text = "Opções";
        textX = getXforCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        //TELA CHEIA ON/OFF
        textX = frameX + gp.tileSize;
        textY += gp.tileSize*3;
        g2.drawString("Tela Cheia", textX, textY);
        if (commandNum == 0){
            g2.drawString(">", textX-25, textY);
            if (gp.keyH.enterPressed == true) {
                if (gp.fullScreenOn == false) {
                    gp.fullScreenOn = true;
                }
                else if (gp.fullScreenOn == true) {
                    gp.fullScreenOn = false;
                }
                subState = 1;
            }
        }

        //CONTROLES
        textY += gp.tileSize;
        g2.drawString("Controles", textX, textY);
        if (commandNum == 1){
            g2.drawString(">", textX-25, textY);
            if (gp.keyH.enterPressed == true) {
                subState = 2;
                commandNum = 0;
            }
        }
        //FIM DE JOGO
        textY += gp.tileSize;
        g2.drawString("Fim de Jogo", textX, textY);
        if (commandNum == 2){
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 3;
                commandNum = 0;
            }
        }
        //VOLTAR
        textY += gp.tileSize*2;
        g2.drawString("Voltar", textX, textY);
        if (commandNum == 3){
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed == true) {
                gp.gameState = gp.playState;
                commandNum = 0;
            }
        }
        //TELA CHEIA CHECK BOX
        textX = frameX + (int)(gp.tileSize*5.5);
        textY = frameY + gp.tileSize*3 + 24;
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(textX, textY, 24, 24);
        if (gp.fullScreenOn == true) {
            g2.fillRect(textX, textY, 24, 24);
        }

        gp.config.saveConfig();
    }

    public void optionsControl(int frameX, int frameY) {

        int textX;
        int textY;

        String text = "Controles";
        textX = getXforCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        textX = frameX + gp.tileSize/2;
        textY += gp.tileSize;
        g2.drawString("Mover", textX, textY); textY += gp.tileSize;
        g2.drawString("Ataque", textX, textY); textY += gp.tileSize;
        g2.drawString("Confirmar", textX, textY); textY += gp.tileSize;
        g2.drawString("Pause", textX, textY); textY += gp.tileSize;
        g2.drawString("Opções", textX, textY); textY += gp.tileSize;

        textX = frameX + gp.tileSize*5;
        textY = frameY + gp.tileSize/2;
        g2.drawString("WASD", textX, textY*2); textY += gp.tileSize;
        g2.drawString("ENTER", textX, textY*2); textY += gp.tileSize;
        g2.drawString("ESC", textX, textY*2); textY += gp.tileSize;

        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize*9;
        g2.drawString("Voltar", textX, textY);
        if (commandNum == 0){
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 0;
                commandNum = 1;
            }
        }
    }

    public void optionsEndGame(int frameX, int frameY) {

        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize*3;

        currentDialogue = "Fechar o jogo e \nretornar para a tela de \nínicio?";

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }
        //SIM
        String text = "Sim";
        textX = getXforCenteredText(text);
        textY += gp.tileSize * 2;
        g2.drawString(text, textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed == true) {
                subState = 0;
                gp.gameState = gp.titleState;
                gp.saveLoad.save();
            }
        }
        //Não
        text = "Não";
        textX = getXforCenteredText(text);
        textY += gp.tileSize;
        g2.drawString(text, textX, textY);
        if (commandNum == 1) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed == true) {
                subState = 0;
                commandNum = 2;
            }
        }
    }

    public void optionsFullScreenNotification ( int frameX, int frameY){

        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        currentDialogue = "A mudançã ocorrerá \nquando o jogo for \nreiniciado";

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }
        //Voltar
        textY = frameY + gp.tileSize * 9;
        g2.drawString("Voltar", textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed == true) {
                subState = 0;
            }
        }
    }

    public void drawTransition() {

        counter++;
        g2.setColor(new Color(0, 0, 0, counter*5));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        if (counter == 50) {
            counter = 0;
            gp.gameState = gp.playState;
            gp.currentMap = gp.event.tempMap;
            gp.player.worldX = gp.tileSize * gp.event.tempCol;
            gp.player.worldY = gp.tileSize * gp.event.tempRow;
            gp.event.previousEventX = gp.player.worldX;
            gp.event.previousEventY = gp.player.worldY;

        }
    }

    public void drawSubWindow(int x, int y, int width, int height) {

        Color c = new Color(0,0,0,210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25); // Desenha a borda da subjanela

    }

    public int getXforCenteredText(String text) {

        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }
}
