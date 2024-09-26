package com.example.state;

public class ExampleFunction implements Function {
    @Override
    public void apply(Object input) {
        System.out.println("Function executed with input: " + input);
    }
}