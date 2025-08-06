package data;

import main.GamePanel;
import java.io.*;

public class SaveLoad {

    GamePanel gp;

    public SaveLoad(GamePanel gp) {
        this.gp = gp;
    }

    public void save() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("save.dat")));
            DataStorage ds = new DataStorage();

            // Salvando dados do jogador
            ds.maxlife = gp.player.maxlife;
            ds.life = gp.player.life;
            ds.playerWorldX = gp.player.worldX;
            ds.playerWorldY = gp.player.worldY;
            ds.currentMap = gp.currentMap;

            // Salvando o estado dos monstros
            ds.monsterAlive = new boolean[gp.maxMap][20];
            for (int map = 0; map < gp.maxMap; map++) {
                for (int i = 0; i < gp.monster[map].length; i++) {
                    if (gp.monster[map][i] != null) {
                        ds.monsterAlive[map][i] = gp.monster[map][i].alive;
                    } else {
                        ds.monsterAlive[map][i] = false;
                    }
                }
            }

            oos.writeObject(ds);
            oos.close();
        } catch (Exception e) {
            System.out.println("Erro ao Salvar: " + e.getMessage());
        }
    }

    public void load() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("save.dat"));
            DataStorage ds = (DataStorage) ois.readObject();

            gp.currentMap = ds.currentMap;
            gp.player.maxlife = ds.maxlife;
            gp.player.life = ds.life;
            gp.player.worldX = ds.playerWorldX;
            gp.player.worldY = ds.playerWorldY;

            gp.tileM.loadMaps();

            for (int map = 0; map < gp.maxMap; map++) {
                for (int i = 0; i < gp.monster[map].length; i++) {
                    if (gp.monster[map][i] != null) {
                        gp.monster[map][i].alive = ds.monsterAlive[map][i];
                        if (!ds.monsterAlive[map][i]) {
                            gp.monster[map][i] = null;
                        }
                    }
                }
            }
            ois.close();

        } catch (Exception e) {
            System.out.println("Erro ao Carregar");
        }
    }
}