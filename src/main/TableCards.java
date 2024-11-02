package main;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;

public class TableCards {
    private ArrayList<Minion>[] cardRows;
    //private ArrayList<Minion> front1 = new ArrayList<>();
    //private ArrayList<Minion> back1 = new ArrayList<>();
    //private ArrayList<Minion> front2 = new ArrayList<>();
    //private ArrayList<Minion> back2 = new ArrayList<>();
    public TableCards() {
        cardRows = new ArrayList[4];
        for (int i = 0; i < cardRows.length; i++) {
            cardRows[i] = new ArrayList<>();
        }
    }

    public ArrayList<Minion> getRow(int IDX){
            return cardRows[IDX];
    }
    public void setRow(int IDX, ArrayList<Minion> row){cardRows[IDX] = row;}

    public void printTable(ObjectMapper objectMapper, ArrayNode outputCorrespondent) {

        for(int i = 0; i < cardRows.length; i++){
            ArrayNode separateRow = objectMapper.createArrayNode();
            ArrayList<Minion> row = cardRows[i];
            for (Minion minion: row) {
                minion.printMinion(separateRow, objectMapper);
            }
            outputCorrespondent.add(separateRow);
        }
    }
}
