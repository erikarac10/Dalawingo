package com.example.dalawingo.data;

import java.util.List;

public class Unit {
    private String title;
    private String instructions;
    private List<Phrase> phrases;
    private List<UnitQuestion> questions;

    public Unit(String title, String instructions,
                List<Phrase> phrases, List<UnitQuestion> questions) {
        this.title = title;
        this.instructions = instructions;
        this.phrases = phrases;
        this.questions = questions;
    }

    public String getTitle() { return title; }
    public String getInstructions() { return instructions; }
    public List<Phrase> getPhrases() { return phrases; }
    public List<UnitQuestion> getQuestions() { return questions; }
}

