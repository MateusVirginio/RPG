import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Random;

public class MON_Skeleton extends Entity {

    public MON_Skeleton(GamePanel gp){
        super(gp);

        String name = "Esqueleto";
        speed = 2;
        maxlife = 5;
        life = maxlife;
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        int solidAreaDefaultX = solidArea.x;
        int solidAreaDefaultY = solidArea.y;

        getImage();
    }
    public void getImage() {

        try {

            up1 = ImageIO.read(getClass().getResourceAsStream("/monstros/skeleton_right1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/monstros/skeleton_right2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/monstros/skeleton_down1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/monstros/skeleton_down2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/monstros/skeleton_right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/monstros/skeleton_right2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/monstros/skeleton_left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/monstros/skeleton_left2.png"));


        }catch (IOException e) {
            e.printStackTrace();
        }
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
