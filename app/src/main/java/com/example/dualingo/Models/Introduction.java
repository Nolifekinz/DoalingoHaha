package com.example.dualingo.Models;

import java.util.List;

public class Introduction {
    private String idIntroduction;
    private List<Vocabulary> vocabularys;
    private List<Grammar> grammars;

    public String getIdIntroduction() {
        return idIntroduction;
    }

    public void setIdIntroduction(String idIntroduction) {
        this.idIntroduction = idIntroduction;
    }

    public List<Vocabulary> getVocabularys() {
        return vocabularys;
    }

    public void setVocabularys(List<Vocabulary> vocabularys) {
        this.vocabularys = vocabularys;
    }

    public List<Grammar> getGrammars() {
        return grammars;
    }

    public void setGrammars(List<Grammar> grammars) {
        this.grammars = grammars;
    }

    public Introduction(String idIntroduction, List<Vocabulary> vocabularys, List<Grammar> grammars) {
        this.idIntroduction = idIntroduction;
        this.vocabularys = vocabularys;
        this.grammars = grammars;
    }
}
