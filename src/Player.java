import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{

    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public Player(GamePanel gp, KeyHandler keyH) {

        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        setDefaultValues();
        getPlayerImage();

        solidArea = new Rectangle(8,16,32,32);
    }
    public void setDefaultValues() {

        worldX = gp.tileSize * 12;
        worldY = gp.tileSize * 11;
        speed = 4;
        direction = "down";

        //STATUS DO PERSONAGEM
        maxlife = 6;
        life = maxlife;
    }
    public void getPlayerImage() {

        try {

            up1 = ImageIO.read(getClass().getResourceAsStream("/player2/up1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player2/up2.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/player2/up3.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player2/down1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player2/down2.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/player2/down3.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player2/left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player2/left2.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/player2/left3.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player2/right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player2/right2.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/player2/right3.png"));

        }catch (IOException e) {
           e.printStackTrace();
        }
    }

    public void update() {

        if(keyH.upPressed == true || keyH.downPressed == true ||
                keyH.leftPressed == true || keyH.rightPressed == true) {

            if(keyH.upPressed == true) {
                direction = "up";
            }
            else if(keyH.downPressed == true) {
                direction = "down";
            }
            else if(keyH.leftPressed == true) {
                direction = "left";
            }
            else if(keyH.rightPressed == true) {
                direction = "right";
            }
            // Checar Colisao Do Tile
            colisionOn = false;
            gp.Colisao.ChecarTile(this);

            // Se a colisao for falsa, o jogador pode se mover
            if (colisionOn == false) {

                switch (direction) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }

            spriteCounter++;
            if(spriteCounter > 12) {
                if(spriteNum == 1) {
                    spriteNum = 2;
                }
                if(spriteNum == 2) {
                    spriteNum = 3;
                }
                else if (spriteNum == 3) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
        }
        }
    }
    public void draw(Graphics2D g2) {

        BufferedImage image = null;

            switch(direction) {
                case "up":
                    if(spriteNum == 1) {
                        image = up1; }
                    if(spriteNum == 2) {
                        image = up2; }
                    if(spriteNum == 3) {
                        image = up3; }
                    break;
                case "down":
                    if(spriteNum == 1) {
                        image = down1; }
                    if(spriteNum == 2) {
                        image = down2; }
                    if(spriteNum == 3) {
                        image = down3; }
                    break;
                case "left":
                    if(spriteNum == 1) {
                        image = left1; }
                    if(spriteNum == 2) {
                        image = left2; }
                    if(spriteNum == 3) {
                        image = left3; }
                    break;
                case "right":
                    if(spriteNum == 1) {
                        image = right1; }
                    if(spriteNum == 2) {
                        image = right2; }
                    if(spriteNum == 3) {
                        image = right3; }
                    break;
        }

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}
