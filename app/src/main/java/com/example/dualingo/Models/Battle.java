package com.example.dualingo.Models;

import java.util.List;

public class Battle {
    private String idBattle;
    private List<String> opponentList;

    public Battle(String idBattle, List<String> opponentList) {
        this.idBattle = idBattle;
        this.opponentList = opponentList;
    }

    public Battle() {
    }

    public String getIdBattle() {
        return idBattle;
    }

    public void setIdBattle(String idBattle) {
        this.idBattle = idBattle;
    }

    public List<String> getOpponentList() {
        return opponentList;
    }

    public void setOpponentList(List<String> opponentList) {
        this.opponentList = opponentList;
    }
}
