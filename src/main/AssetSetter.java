package main;

import entity.Npc;
import monster.MON_Skeleton;
import editor.MonsterData;
import editor.NpcData;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setNpc() {
        for (int i = 0; i < gp.npcData.getPlacedNpcs().size(); i++) {
            NpcData.NpcPlacement npcPlacement = gp.npcData.getPlacedNpcs().get(i);
            gp.npc[0][i] = new Npc(gp);
            gp.npc[0][i].worldX = gp.tileSize * npcPlacement.col;
            gp.npc[0][i].worldY = gp.tileSize * npcPlacement.row;
            gp.npc[0][i].setDialogues(npcPlacement.dialogues);
        }
    }

    public void setMonster() {
        for (int i = 0; i < gp.monsterData.getPlacedMonsters().size(); i++) {
            MonsterData.MonsterPlacement monsterPlacement = gp.monsterData.getPlacedMonsters().get(i);
            gp.monster[1][i] = new MON_Skeleton(gp);
            gp.monster[1][i].worldX = gp.tileSize * monsterPlacement.col;
            gp.monster[1][i].worldY = gp.tileSize * monsterPlacement.row;
            gp.monster[1][i].maxlife = monsterPlacement.maxLife;
            gp.monster[1][i].speed = monsterPlacement.speed;
        }
    }
}