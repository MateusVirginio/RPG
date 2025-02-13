public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setMonster() {
        gp.monster[0] = new MON_Skeleton(gp);
        gp.monster[0].worldX = gp.tileSize*23;
        gp.monster[0].worldY = gp.tileSize*36;
    }
}
