public class OBJ_Heart extends Entity{

    public OBJ_Heart(GamePanel gp) {
        super(gp);

        name = "Heart";
        image = setup("/objetos/vida_cheia");
        image2 = setup("/objetos/vida_meia");
        image3 = setup("/objetos/sem_vida");
    }
}
