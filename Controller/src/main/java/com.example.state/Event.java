package com.example.state;

public class Event {
    private String name;
    private Object data;

    public Event(String name, Object data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public Object getData() {
        return data;
    }
}