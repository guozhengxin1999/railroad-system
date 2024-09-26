package com.example.state;

public class RaiseEventAction extends Action {
    private StateMachine stateMachine;
    private Event event;

    public RaiseEventAction(StateMachine stateMachine, Event event) {
        this.stateMachine = stateMachine;
        this.event = event;
    }

    @Override
    public void execute() {
        stateMachine.handleEvent(event);
    }
}