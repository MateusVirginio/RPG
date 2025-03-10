package main;

import monster.MON_Skeleton;
import entity.Npc;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {

        this.gp = gp;
    }

    public void setNpc() {

        int mapNum = 0;
        int i = 0;
        gp.npc[mapNum][i] = new Npc(gp);
        gp.npc[mapNum][i].worldX = gp.tileSize*30;
        gp.npc[mapNum][i].worldY = gp.tileSize*10;
        i++;
    }

    public void setMonster() {

        int mapNum = 1;
        int i = 0;
        gp.monster[mapNum][i] = new MON_Skeleton(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize*23;
        gp.monster[mapNum][i].worldY = gp.tileSize*11;
        i++;
        gp.monster[mapNum][i] = new MON_Skeleton(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize*24;
        gp.monster[mapNum][i].worldY = gp.tileSize*11;
        i++;
        gp.monster[mapNum][i] = new MON_Skeleton(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize*25;
        gp.monster[mapNum][i].worldY = gp.tileSize*11;
        i++;
        gp.monster[mapNum][i] = new MON_Skeleton(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize*26;
        gp.monster[mapNum][i].worldY = gp.tileSize*11;
        i++;
        gp.monster[mapNum][i] = new MON_Skeleton(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize*26;
        gp.monster[mapNum][i].worldY = gp.tileSize*12;
        i++;
    }
}