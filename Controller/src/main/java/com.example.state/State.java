package com.example.state;

import java.util.List;

public class State {
    private String name;
    private List<Action> actions;

    public State(String name, List<Action> actions) {
        this.name = name;
        this.actions = actions;
    }

    public String getName() {
        return name;
    }

    public List<Action> getActions() {
        return actions;
    }
}