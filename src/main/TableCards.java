package main;

import java.util.ArrayList;

public class TableCards {
    private ArrayList<Minion> front1 = new ArrayList<>();
    private ArrayList<Minion> back1 = new ArrayList<>();
    private ArrayList<Minion> front2 = new ArrayList<>();
    private ArrayList<Minion> back2 = new ArrayList<>();
    TableCards() {}
    ArrayList<Minion> getFront1() {return front1;}
    ArrayList<Minion> getBack1() {return back1;}
    ArrayList<Minion> getFront2() {return front2;}
    ArrayList<Minion> getBack2() {return back2;}
    void setFront1(ArrayList<Minion> front1) {this.front1 = front1;}
    void setBack1(ArrayList<Minion> back1) {this.back1 = back1;}
    void setFront2(ArrayList<Minion> front2) {this.front2 = front2;}
    void setBack2(ArrayList<Minion> back2) {this.back2 = back2;}
}
