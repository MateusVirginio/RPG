package entity;

import main.GamePanel;

import java.util.Random;

public class Npc extends Entity {

    public Npc(GamePanel gp) {
        super(gp);

        speed = 1;

        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
        setDialogue();
    }
    public void getImage() {

        up1 = setup("/npc/npc_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/npc/npc_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/npc/npc_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/npc_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/npc/npc_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/npc/npc_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/npc/npc_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/npc_right_2", gp.tileSize, gp.tileSize);
    }
    public void setDialogue() {

        dialogues[0] = "Viajante, você se atreve a entrar nas Catacumbas da Perdição?";
        dialogues[1] = "Saiba que muitos aventureiros corajosos como você entraram por \nessas portas... e nunca mais foram vistos.";
        dialogues[2] = "Os monstros lá dentro são implacáveis, e as sombras escondem \nsegredos que podem consumir até a alma mais forte.";
        dialogues[3] = "Se você insiste em prosseguir, esteja preparado para \nenfrentar o pior.";

    }
    public void speak() {
        super.speak();
    }
    public void setAction() {

        actionLockCounter++;

        if(actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100)+1;

            if(i <= 25) {
                direction = "up";
            }
            if(i > 25 && i <= 50) {
                direction = "down";
            }
            if(i > 50 && i <= 75) {
                direction = "left";
            }
            if(i > 75 && i <= 100) {
                direction = "right";
            }
            actionLockCounter = 0;
        }
    }
}