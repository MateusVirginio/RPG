package main;

import entity.Entity;

public class CheckCollision {
    GamePanel gp;

    public CheckCollision(GamePanel gp) {

        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        if (entityLeftCol >= 0 && entityRightCol < gp.maxWorldCol &&
                entityTopRow >= 0 && entityBottomRow < gp.maxWorldRow) {

            int tileNum1, tileNum2;
            switch (entity.direction) {
                case "up":
                    entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                    if (entityTopRow >= 0) {
                        tileNum1 = gp.tileM.mapTileNumLayer2[gp.currentMap][entityLeftCol][entityTopRow];
                        tileNum2 = gp.tileM.mapTileNumLayer2[gp.currentMap][entityRightCol][entityTopRow];
                        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                            entity.collisionOn = true;
                        }
                    }
                    break;

                case "down":
                    entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                    if (entityBottomRow < gp.maxWorldRow) {
                        tileNum1 = gp.tileM.mapTileNumLayer2[gp.currentMap][entityLeftCol][entityBottomRow];
                        tileNum2 = gp.tileM.mapTileNumLayer2[gp.currentMap][entityRightCol][entityBottomRow];
                        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                            entity.collisionOn = true;
                        }
                    }
                    break;

                case "left":
                    entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                    if (entityLeftCol >= 0) {
                        tileNum1 = gp.tileM.mapTileNumLayer2[gp.currentMap][entityLeftCol][entityTopRow];
                        tileNum2 = gp.tileM.mapTileNumLayer2[gp.currentMap][entityLeftCol][entityBottomRow];
                        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                            entity.collisionOn = true;
                        }
                    }
                    break;

                case "right":
                    entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                    if (entityRightCol < gp.maxWorldCol) {
                        tileNum1 = gp.tileM.mapTileNumLayer2[gp.currentMap][entityRightCol][entityTopRow];
                        tileNum2 = gp.tileM.mapTileNumLayer2[gp.currentMap][entityRightCol][entityBottomRow];
                        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                            entity.collisionOn = true;
                        }
                    }
                    break;
            }
        }
    }

    public int checkEntity(Entity entity, Entity[][] target) {

        int index = 999;
        for (int i = 0; i < target[gp.currentMap].length; i++) {
            if (target[gp.currentMap][i] != null && target[gp.currentMap][i] != entity) {

                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].worldX + target[gp.currentMap][i].solidArea.x;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].worldY + target[gp.currentMap][i].solidArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed; break;
                    case "down":
                        entity.solidArea.y += entity.speed; break;
                    case "left":
                        entity.solidArea.x -= entity.speed; break;
                    case "right":
                        entity.solidArea.x += entity.speed; break;
                }
                if (entity.solidArea.intersects(target[gp.currentMap][i].solidArea)) {
                    entity.collisionOn = true;
                    index = i;
                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].solidAreaDefaultX;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].solidAreaDefaultY;
            }
        }
        return index;
    }

    public boolean checkPlayer(Entity entity) {

        boolean contactPlayer = false;

        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        switch (entity.direction) {
            case "up":
                entity.solidArea.y -= entity.speed; break;
            case "down":
                entity.solidArea.y += entity.speed; break;
            case "left":
                entity.solidArea.y -= entity.speed; break;
            case "right":
                entity.solidArea.y += entity.speed; break;
        }
        if (entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;

        return contactPlayer;
    }
}