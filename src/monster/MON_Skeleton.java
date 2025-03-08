package monster;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class MON_Skeleton extends Entity {

    GamePanel gp;

    public MON_Skeleton(GamePanel gp) {

        super(gp);
        this.gp = gp;

        type = 1;
        name = "Esqueleto";
        speed = 1;
        maxlife = 5;
        life = maxlife;

        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {

        up1 = setup("/monstros/skeleton_up1", gp.tileSize, gp.tileSize);
        up2 = setup("/monstros/skeleton_up2", gp.tileSize, gp.tileSize);
        down1 = setup("/monstros/skeleton_down1", gp.tileSize, gp.tileSize);
        down2 = setup("/monstros/skeleton_down2", gp.tileSize, gp.tileSize);
        right1 = setup("/monstros/skeleton_right1", gp.tileSize, gp.tileSize);
        right2 = setup("/monstros/skeleton_right2", gp.tileSize, gp.tileSize);
        left1 = setup("/monstros/skeleton_left1", gp.tileSize, gp.tileSize);
        left2 = setup("/monstros/skeleton_left2", gp.tileSize, gp.tileSize);

    }

    public void update() {

        super.update();

        int xDistance = Math.abs(worldX - gp.player.worldX);
        int yDistance = Math.abs(worldY - gp.player.worldY);
        int tileDistance = (xDistance + yDistance)/gp.tileSize;

        if (onPath == false && tileDistance < 5) {

            int i = new Random().nextInt(100)+1;
            if (i > 50) {
                onPath = true;
            }
        }
        if (onPath == true && tileDistance > 20) {
            onPath = false;
        }
    }

    public void setAction() {

        if (onPath == true) {

            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;

            searchPath(goalCol, goalRow);
        } else {

            actionLockCounter++;

            if (actionLockCounter == 120) {
                Random random = new Random();
                int i = random.nextInt(100) + 1;

                if (i <= 25) {
                    direction = "up";
                }
                if (i > 25 && i <= 50) {
                    direction = "down";
                }
                if (i > 50 && i <= 75) {
                    direction = "left";
                }
                if (i > 75 && i <= 100) {
                    direction = "right";
                }
                actionLockCounter = 0;
            }
        }
    }

    public void damageReaction() {

        actionLockCounter = 0;
        onPath = true;
    }
}