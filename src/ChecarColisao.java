public class ChecarColisao {
    GamePanel gp;

    public ChecarColisao(GamePanel gp) {
        this.gp = gp;
    }

    public void ChecarTile(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        if (entityLeftCol >= 0 && entityLeftCol < gp.tileM.mapTileNum.length &&
                entityRightCol >= 0 && entityRightCol < gp.tileM.mapTileNum.length &&
                entityTopRow >= 0 && entityTopRow < gp.tileM.mapTileNum[0].length &&
                entityBottomRow >= 0 && entityBottomRow < gp.tileM.mapTileNum[0].length) {
            int tileNum1, tileNum2;
            switch (entity.direction) {
                case "up":
                    entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                    if (entityTopRow >= 0) {
                        tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                        tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                            entity.collisionOn = true;
                        }
                    }
                    break;

                case "down":
                    entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                    if (entityBottomRow < gp.tileM.mapTileNum[0].length) {
                        tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                        tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                            entity.collisionOn = true;
                        }
                    }
                    break;

                case "left":
                    entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                    if (entityLeftCol >= 0) {
                        tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                        tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                            entity.collisionOn = true;
                        }
                    }
                    break;

                case "right":
                    entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                    if (entityRightCol < gp.tileM.mapTileNum.length) {
                        tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                        tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                            entity.collisionOn = true;
                        }
                    }
                    break;
            }
        }
    }

    public int checkEntity(Entity entity, Entity[] target) {
        int index = 999;
        for (int i = 0; i < target.length; i++) {
            if (target[i] != null) {

                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
                target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;

                switch (entity.direction) {
                    case "up": entity.solidArea.y -= entity.speed; break;
                    case "down": entity.solidArea.y += entity.speed; break;
                    case "left": entity.solidArea.x -= entity.speed; break;
                    case "right": entity.solidArea.x += entity.speed; break;
                }
                if (entity.solidArea.intersects(target[i].solidArea)) {
                    if(target[i] != entity){
                    entity.collisionOn = true;
                    index = i;}
                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }
        }
        return index;
    }
    public void checkPlayer(Entity entity) {

        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        switch (entity.direction) {
            case "up":
                entity.solidArea.y -= entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entity.solidArea.y += entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entity.solidArea.y -= entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entity.solidArea.y += entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.collisionOn = true;
                    break;
                }
        }
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
    }
}
