package com.example.state;

public class CallFunctionAction extends Action {
    private StateMachine stateMachine;
    private String functionName;
    private Object input;

    public CallFunctionAction(StateMachine stateMachine, String functionName, Object input) {
        this.stateMachine = stateMachine;
        this.functionName = functionName;
        this.input = input;
    }

    @Override
    public void execute() {
        stateMachine.invokeFunction(functionName, input);
    }
}