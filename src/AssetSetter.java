public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setMonster() {
        gp.monster[0] = new MON_Skeleton(gp);
        gp.monster[0].worldX = gp.tileSize*6;
        gp.monster[0].worldY = gp.tileSize*5;
        System.out.println("Monstro criado: " + gp.monster[0].name);
        System.out.println("Posição do monstro: (" + gp.monster[0].worldX + ", " + gp.monster[0].worldY + ")");

        gp.monster[1] = new MON_Skeleton(gp);
        gp.monster[1].worldX = gp.tileSize*7;
        gp.monster[1].worldY = gp.tileSize*4;
        System.out.println("Monstro criado: " + gp.monster[1].name);
        System.out.println("Posição do monstro: (" + gp.monster[1].worldX + ", " + gp.monster[1].worldY + ")");
    }
}
