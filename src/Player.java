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
    }
    public void getPlayerImage() {

        try {

            up1 = ImageIO.read(getClass().getResourceAsStream("/player/up1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/up2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/down1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/down2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/left2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/right2.png"));

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
                else if (spriteNum == 2) {
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
                    break;
                case "down":
                    if(spriteNum == 1) {
                        image = down1; }
                    if(spriteNum == 2) {
                        image = down2; }
                    break;
                case "left":
                    if(spriteNum == 1) {
                        image = left1; }
                    if(spriteNum == 2) {
                        image = left2; }
                    break;
                case "right":
                    if(spriteNum == 1) {
                        image = right1; }
                    if(spriteNum == 2) {
                        image = right2; }
                    break;
        }

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}
