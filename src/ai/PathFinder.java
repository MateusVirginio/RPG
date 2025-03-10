package ai;

import main.GamePanel;

import java.util.ArrayList;

public class PathFinder {

    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    public PathFinder(GamePanel gp) {
        this.gp = gp;
        instantiateNodes();
    }

    public void instantiateNodes() {

        node = new Node[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {

            node[col][row] = new Node(col, row);

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    public void resetNodes() {

        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {

            //Reseta o open, checked e o solid
            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
        //Reseta outras configs
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {

        resetNodes();

        //Define o nodo de ínicio e o de destino
        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {

            //Define o nodo solido
            //Checar os tiles
            int tileNum = gp.tileM.mapTileNumLayer2[gp.currentMap][col][row];
            if (gp.tileM.tile[tileNum].collision == true) {
                node[col][row].solid = true;
            }
            getCost(node[col][row]);
            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    public void getCost(Node node) {

        //G custo
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;
        //H custo
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;
        // F custo
        node.fCost = node.gCost + node.hCost;
    }

    public boolean search() {

        while (goalReached == false && step < 500) {

            int col = currentNode.col;
            int row = currentNode.row;

            //Verifica o atual nodo
            currentNode.checked = true;
            openList.remove(currentNode);

            //Abre o nodo de cima
            if (row - 1 >= 0) {
                openNode(node[col][row-1]);
            }
            //Abre o nodo da esquerda
            if (col - 1 >= 0) {
                openNode(node[col-1][row]);
            }
            //Abre o nodo de baixo
            if (row + 1 < gp.maxWorldRow) {
                openNode(node[col][row+1]);
            }
            //Abre o nodo da direita
            if (col + 1 < gp.maxWorldCol) {
                openNode(node[col+1][row]);
            }

            //Encontrar o melhor nodo
            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for (int i = 0; i < openList.size(); i++) {
                //Verifica se o custo dos nodos de F é melhor
                if (openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }
                //Se o custo de F for igual, verifica o custo de G
                else if (openList.get(i).fCost == bestNodefCost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }
            //Se não tiver nenhum nodo na OpenList, encerra o loop
            if (openList.size() == 0) {
                break;
            }
            //
            currentNode = openList.get(bestNodeIndex);

            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step++;
        }
        return goalReached;
    }

    public void openNode(Node node) {

        if (node.open == false && node.checked == false && node.solid == false) {

            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }

    public void trackThePath() {

        Node current = goalNode;

        while (current != startNode) {

            pathList.add(0, current);
            current = current.parent;
        }
    }
}