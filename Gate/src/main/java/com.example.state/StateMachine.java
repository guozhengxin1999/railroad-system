package com.example.state;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class StateMachine {
    private String name;
    private State currentState;
    private Map<String, State> states;
    private Map<String, Map<String, State>> transitions;
    private Map<String, Function> functions;

    public StateMachine(String name) {
        this.name = name;
        this.states = new HashMap<>();
        this.transitions = new HashMap<>();
        this.functions = new HashMap<>();
    }

    public void addState(String name, List<Action> actions) {
        State state = new State(name, actions);
        states.put(name, state);
        transitions.put(name, new HashMap<>());
    }

    public void addTransition(String fromState, String event, String toState) {
        transitions.get(fromState).put(event, states.get(toState));
    }

    public void addFunction(String name, Function function) {
        functions.put(name, function);
    }

    public void setInitialState(String name) {
        currentState = states.get(name);
    }

    public void handleEvent(Event event) {
        if (currentState == null) {
            throw new IllegalStateException("Initial state not set");
        }

        State nextState = transitions.get(currentState.getName()).get(event.getName());
        if (nextState != null) {
            currentState = nextState;
            for (Action action : currentState.getActions()) {
                action.execute();
            }
        }
        log.info("Gate : {}", currentState.getName());
    }

    public void invokeFunction(String name, Object input) {
        Function function = functions.get(name);
        if (function != null) {
            function.apply(input);
        }
    }
}