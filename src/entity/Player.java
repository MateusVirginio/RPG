package entity;

import main.GamePanel;
import main.KeyHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public Player(GamePanel gp, KeyHandler keyH) {

        super(gp);

        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        attackArea.width = 46;
        attackArea.height = 46;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();

        solidArea = new Rectangle(8, 16, 32, 32);
    }

    public void setDefaultPositions() {

        worldX = gp.tileSize * 40;
        worldY = gp.tileSize * 37;
        direction = "up";
    }

    public void restoreLife() {

        life = maxlife;
        invincible = false;
    }
    public void setDefaultValues() {

        worldX = gp.tileSize * 12;
        worldY = gp.tileSize * 11;
        speed = 4;
        direction = "up";

        //STATUS DO PERSONAGEM
        maxlife = 6;
        life = maxlife;
    }

    public void getPlayerImage() {

        up1 = setup("/player/boy_up_1",gp.tileSize, gp.tileSize);
        up2 = setup("/player/boy_up_2",gp.tileSize, gp.tileSize);
        down1 = setup("/player/boy_down_1",gp.tileSize, gp.tileSize);
        down2 = setup("/player/boy_down_2",gp.tileSize, gp.tileSize);
        left1 = setup("/player/boy_left_1",gp.tileSize, gp.tileSize);
        left2 = setup("/player/boy_left_2",gp.tileSize, gp.tileSize);
        right1 = setup("/player/boy_right_1",gp.tileSize, gp.tileSize);
        right2 = setup("/player/boy_right_2",gp.tileSize, gp.tileSize);

    }

    public void getPlayerAttackImage() {

        attackUp1 = setup("/player/boy_attack_up_1", gp.tileSize, gp.tileSize);
        attackUp2 = setup("/player/boy_attack_up_2", gp.tileSize, gp.tileSize);
        attackDown1 = setup("/player/boy_attack_down_1", gp.tileSize, gp.tileSize );
        attackDown2 = setup("/player/boy_attack_down_2", gp.tileSize, gp.tileSize );
        attackLeft1 = setup("/player/boy_attack_left_1", gp.tileSize , gp.tileSize);
        attackLeft2 = setup("/player/boy_attack_left_2", gp.tileSize , gp.tileSize);
        attackRight1 = setup("/player/boy_attack_right_1", gp.tileSize , gp.tileSize);
        attackRight2 = setup("/player/boy_attack_right_2", gp.tileSize , gp.tileSize);
    }

    public void update() {

        if (gp.keyH.enterPressed == true && attacking == false) {
            if (!keyH.upPressed && !keyH.downPressed && !keyH.leftPressed && !keyH.rightPressed) {
                attacking = true; // Ativa o estado de ataque
                gp.keyH.enterPressed = false; // Reseta o estado da tecla Enter
            }
        }
        if (!attacking) {

            if (keyH.upPressed == true || keyH.downPressed == true ||
                    keyH.leftPressed == true || keyH.rightPressed == true) {

                if (keyH.upPressed == true) {
                    direction = "up";
                } else if (keyH.downPressed == true) {
                    direction = "down";
                } else if (keyH.leftPressed == true) {
                    direction = "left";
                } else if (keyH.rightPressed == true) {
                    direction = "right";
                }
                // Checar Colisao Do tile.Tile
                collisionOn = false;
                gp.Colisao.checkTile(this);

                // Checar Colisao Do Monstro
                int monsterIndex = gp.Colisao.checkEntity(this, gp.monster);
                contactMonster(monsterIndex);

                //Checar Evento
                gp.event.checkEvent();

                //Checar Colisao Do entity.Npc
                int npcIndex = gp.Colisao.checkEntity(this, gp.npc);
                interactNpc(npcIndex);

                // Se a colisao for falsa, o jogador pode se mover
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
            }
        }
        if (attacking) {
            attacking();
        }
        if (invincible == true){
            invincibleCounter++;
            if(invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
       if (life <= 0) {
           gp.gameState = gp.gameOverState;
       }
    }

    public void interactNpc(int i) {

        if (i != 999) {
            if (gp.keyH.enterPressed) {
                gp.gameState = gp.dialogueState;
                gp.npc[gp.currentMap][i].speak();
            }
        }
        gp.keyH.enterPressed = false;
    }

    public void attacking() {

        spriteCounter++;
        if (spriteCounter <= 5) {
            spriteNum = 1;
        }
        else if (spriteCounter > 5 && spriteCounter <= 25) {
            spriteNum = 2;
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            switch (direction) {
                case "up": worldY -= attackArea.height; break;
                case "down": worldY += attackArea.height; break;
                case "left": worldX -= attackArea.width; break;
                case "right": worldX += attackArea.width; break;
            }

            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            int monsterIndex = gp.Colisao.checkEntity(this, gp.monster);
            damageMonster(monsterIndex);

            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }
        else if (spriteCounter > 25) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public void contactMonster(int i){

        if (i != 999 && gp.monster[gp.currentMap][i] != null) {
            if (invincible == false && gp.monster[gp.currentMap][i].invincible == false && !gp.monster[gp.currentMap][i].dying) {
                life -= 1;
                invincible = true;
            }
        }
    }

    public void damageMonster(int i){

        if(i != 999) {
            if (gp.monster[gp.currentMap][i].invincible == false) {
                gp.monster[gp.currentMap][i].life -= 1;
                gp.monster[gp.currentMap][i].invincible = true;
                gp.monster[gp.currentMap][i].invincibleCounter = 0;
                gp.monster[gp.currentMap][i].damageReaction();

                if (gp.monster[gp.currentMap][i].life <= 0) {
                    gp.monster[gp.currentMap][i].dying = true;}
            }
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        int width = gp.tileSize;
        int height = gp.tileSize;
        int drawX = screenX;
        int drawY = screenY;

        switch (direction) {
            case "up":
                if (attacking == false) {
                    if (spriteNum == 1) {
                        image = up1;
                    }
                    if (spriteNum == 2) {
                        image = up2;
                    }
                }
                if (attacking == true) {
                    if (spriteNum == 1) {
                        image = attackUp1;
                    }
                    if (spriteNum == 2) {
                        image = attackUp2;
                    }
                    height = gp.tileSize * 2;
                    drawY = screenY - gp.tileSize;
                }
                break;
            case "down":
                if (attacking == false) {
                    if (spriteNum == 1) {
                        image = down1;
                    }
                    if (spriteNum == 2) {
                        image = down2;
                    }
                }
                if (attacking == true) {
                    if (spriteNum == 1) {
                        image = attackDown1;
                    }
                    if (spriteNum == 2) {
                        image = attackDown2;
                    }
                    height = gp.tileSize * 2;
                }
                break;
            case "left":
                if (attacking == false) {
                    if (spriteNum == 1) {
                        image = left1;
                    }
                    if (spriteNum == 2) {
                        image = left2;
                    }
                }
                if (attacking == true) {
                    if (spriteNum == 1) {
                        image = attackLeft1;
                    }
                    if (spriteNum == 2) {
                        image = attackLeft2;
                    }
                    width = gp.tileSize * 2;
                    drawX = screenX - gp.tileSize;
                }
                break;
            case "right":
                if (attacking == false) {
                    if (spriteNum == 1) {
                        image = right1;
                    }
                    if (spriteNum == 2) {
                        image = right2;
                    }
                }
                if (attacking == true) {
                    if (spriteNum == 1) {
                        image = attackRight1;
                    }
                    if (spriteNum == 2) {
                        image = attackRight2;
                    }
                    width = gp.tileSize * 2;
                }
                break;
        }
        g2.drawImage(image, drawX, drawY, width, height, null);
    }
}