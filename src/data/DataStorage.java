package data;

import java.io.Serializable;

public class DataStorage implements Serializable {

    //Dados do Jogador
    public int maxlife;
    public int life;
    public int playerWorldX;
    public int playerWorldY;
    public int currentMap;

    //Dados dos Monstros
    public boolean[][] monsterAlive; // Armazena se cada monstro está vivo ou não

}