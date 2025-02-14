import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Entity {

    GamePanel gp;
    public int worldX, worldY;
    public int speed;

    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
    public String direction = "down";

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea;
    public boolean collisionOn = false;
    public int solidAreaDefaultX, solidAreaDefaultY;

    public int actionLockCounter = 0;
    public boolean invincible = false;
    public int invincibleCounter = 0;

    public BufferedImage image, image2, image3;
    public String name;
    public boolean collision = false;
    UtilityTool uTool = new UtilityTool();

    //STATUS DO PERSONAGEM
    public int maxlife;
    public int life;

    public Entity(GamePanel gp) {
        this.gp = gp;
        // Inicializa a área sólida (solidArea)
        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize); // Define o tamanho da área sólida
        solidAreaDefaultX = solidArea.x; // Salva a posição padrão X
        solidAreaDefaultY = solidArea.y; // Salva a posição padrão Y
    }
    public BufferedImage setup(String imagePath) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            // Verifica se imagePath é válido
            if (imagePath == null) {
                throw new IOException("Caminho da imagem não pode ser null.");
            }

            // Carrega a imagem
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            if (image == null) {
                throw new IOException("Imagem não encontrada: " + imagePath);
            }

            // Verifica se gp foi inicializado
            if (gp == null) {
                throw new NullPointerException("GamePanel (gp) não foi inicializado.");
            }

            // Redimensiona a imagem
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY - gp.player.screenY) {

            switch (direction) {
                case "up":
                    if (spriteNum == 1) image = up1;
                    if (spriteNum == 2) image = up2;
                    break;
                case "down":
                    if (spriteNum == 1) image = down1;
                    if (spriteNum == 2) image = down2;
                    break;
                case "left":
                    if (spriteNum == 1) image = left1;
                    if (spriteNum == 2) image = left2;
                    break;
                case "right":
                    if (spriteNum == 1) image = right1;
                    if (spriteNum == 2) image = right2;
                    break;
            }
            if (image != null) {
                g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            } else {
                System.out.println("Erro: Imagem do monstro é null");
            }
        }
    }
    public void setAction(){
    }
    public void update(){

        setAction();

        collisionOn = false;
        gp.Colisao.ChecarTile(this);
        gp.Colisao.checkEntity(this, gp.monster);
        gp.Colisao.checkPlayer(this);

        if (collisionOn == false) {

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
