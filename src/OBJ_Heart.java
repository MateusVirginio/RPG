public class OBJ_Heart extends Entity{

    public OBJ_Heart(GamePanel gp) {
        super(gp);

        name = "Heart";
        image = setup("/objetos/vida_cheia",gp.tileSize, gp.tileSize);
        image2 = setup("/objetos/vida_meia",gp.tileSize, gp.tileSize);
        image3 = setup("/objetos/sem_vida",gp.tileSize, gp.tileSize);
    }
}
