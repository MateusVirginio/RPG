package editor;

import java.util.ArrayList;
import java.util.List;

public class NpcData {

    private String npcToPlaceName;
    private String[] npcToPlaceDialogues;

    private final List<NpcPlacement> placedNpcs = new ArrayList<>();

    public void addNpcToPlace(String name, String[] dialogues) {
        this.npcToPlaceName = name;
        this.npcToPlaceDialogues = dialogues;
    }

    public boolean isNpcToPlace() {
        return npcToPlaceName != null;
    }

    public void placeNpc(int col, int row) {
        if (isNpcToPlace()) {
            placedNpcs.add(new NpcPlacement(npcToPlaceName, npcToPlaceDialogues, col, row));
            npcToPlaceName = null;
            npcToPlaceDialogues = null;
        }
    }

    public List<NpcPlacement> getPlacedNpcs() {
        return placedNpcs;
    }

    public void clearNpcs() {
        placedNpcs.clear();
    }

    public static class NpcPlacement {
        public String name;
        public String[] dialogues;
        public int col;
        public int row;

        public NpcPlacement(String name, String[] dialogues, int col, int row) {
            this.name = name;
            this.dialogues = dialogues;
            this.col = col;
            this.row = row;
        }
    }
}