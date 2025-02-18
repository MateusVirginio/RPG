import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Entity {

    GamePanel gp;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2,
            attackLeft1, attackLeft2, attackRight1, attackRight2;
    public boolean collision = false;
    public Rectangle solidArea;
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public BufferedImage image, image2, image3;
    UtilityTool uTool = new UtilityTool();
    public int type;// 0 - Player. 1 - Monster

    //ESTADO
    public int worldX, worldY;
    public String direction = "down";
    public int spriteNum = 1;
    public boolean collisionOn = false;
    public boolean attacking = false;
    public boolean invincible = false;
    public boolean alive = true;
    public boolean dying = false;
    public boolean hpBarOn = false;
    public int chaseSpeed = 2;
    public int attackRange = gp.tileSize;
    public int visible_range = 200;
    //CONTADOR
    public int spriteCounter = 0;
    public int actionLockCounter = 0;
    public int invincibleCounter = 0;
    public int dyingCounter = 0;
    public int hpBarCounter = 0;


    //STATUS DO PERSONAGEM
    public int maxlife;
    public int life;
    public String name;
    public int speed;


    public Entity(GamePanel gp) {
        this.gp = gp;
        // Inicializa a área sólida (solidArea)
        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize); // Define o tamanho da área sólida
        solidAreaDefaultX = solidArea.x; // Salva a posição padrão X
        solidAreaDefaultY = solidArea.y; // Salva a posição padrão Y

        chaseSpeed = speed * 2;
        attackRange = gp.tileSize;
    }

    public BufferedImage setup(String imagePath, int width, int height) {

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
            image = uTool.scaleImage(image, width, height);
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
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

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

            //BARRA DE VIDA MONSTROS
            if (type == 1 && hpBarOn == true) {
                double oneScale = (double) gp.tileSize / maxlife;
                double hpBarValue = oneScale * life;

                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX - 1, screenY - 16, gp.tileSize + 2, 12);

                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);

                hpBarCounter++;

                if (hpBarCounter > 600) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            if (invincible == true) {
                hpBarOn = true;
                hpBarCounter = 0;
                changeAlpha(g2, 0.4f);
            }
            if (dying == true) {
                dyingAnimation(g2);
            }
            changeAlpha(g2, 1f);

            if (image != null) {
                g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            } else {
                System.out.println("Erro: Imagem do monstro é null");
            }
        }
    }

    public void dyingAnimation(Graphics2D g2) {
        dyingCounter++;

        int i = 5;
        if (dyingCounter <= 5) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > i && dyingCounter <= i * 2) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > i * 2 && dyingCounter <= i * 3) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > i * 3 && dyingCounter <= i * 4) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > i * 4 && dyingCounter <= i * 5) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > i * 5 && dyingCounter <= i * 6) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > i * 6 && dyingCounter <= i * 7) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > i * 7 && dyingCounter <= i * 8) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > i * 8) {
            dying = false;
            alive = false;
        }
    }

    public void changeAlpha(Graphics2D g2, float alphaValue) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }


    public void setAction() {
        if (this.type == 1) { // Verifica se é um monstro
            if (canSeePlayer()) {
                moveTowardsPlayer(); // Persegue o jogador
            } else {
                // Comportamento padrão (opcional)
                direction = "down"; // Ou qualquer outra direção padrão
            }
        }
    }
    public boolean canSeePlayer() {
        double distanceToPlayer = calculateDistancePlayer();
        return distanceToPlayer < visible_range; // Verifica se o jogador está dentro do raio de visão
    }

    public void damageReaction() {
        actionLockCounter = 0;
        direction = gp.player.direction;
        if (canSeePlayer()) {
            moveTowardsPlayer();
        }
    }

    public double calculateDistancePlayer() {
        int deltaX = gp.player.worldX - this.worldX;
        int deltaY = gp.player.worldY - this.worldY;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public void moveTowardsPlayer() {
        // Verifica a distância até o jogador
        double distanceToPlayer = calculateDistancePlayer();

        // Se o monstro estiver dentro do alcance de ataque, ataca o jogador
        if (distanceToPlayer <= attackRange) {
            attackPlayer();
        } else {
            // Caso contrário, move-se em direção ao jogador com velocidade aumentada
            if (gp.player.worldX > this.worldX) {
                this.worldX += chaseSpeed;
                direction = "right";// Move para a direita
            } else if (gp.player.worldX < this.worldX) {
                this.worldX -= chaseSpeed;
                direction = "left";// Move para a esquerda
            }

            if (gp.player.worldY > this.worldY) {
                this.worldY += chaseSpeed;// Move para baixo
                direction = "down";
            } else if (gp.player.worldY < this.worldY) {
                this.worldY -= chaseSpeed; // Move para cima
                direction = "up";
            }
        }
    }

    public void attackPlayer() {
        // Lógica para atacar o jogador
        if (gp.player.invincible == false) {
            gp.player.life -= 1; // Reduz a vida do jogador
            gp.player.invincible = true; // Torna o jogador invencível temporariamente
            System.out.println(name + " atacou o jogador!");
        }
    }
    public void update() {

        if (this.type == 1) {
            setAction();
        }

        collisionOn = false;
        gp.Colisao.ChecarTile(this);
        gp.Colisao.checkEntity(this, gp.monster);
        boolean contactPlayer = gp.Colisao.checkPlayer(this);

        if (this.type == 1 && contactPlayer == true) {
            if (gp.player.invincible == false) {
                gp.player.life -= 1;
                gp.player.invincible = true;
            }
        }

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
        if (spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
        if (invincible == true) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }
}
