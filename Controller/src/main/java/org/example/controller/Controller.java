package org.example.controller;

import com.example.state.CallFunctionAction;
import com.example.state.Event;
import com.example.state.ExampleFunction;
import com.example.state.StateMachine;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

@Service
public class Controller {
    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    private StateMachine stateMachine;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Controller() {
        stateMachine = new StateMachine("Controller");
        //State：Approaching, Crossing, Leaving
        //Event:seen ¬seen
        stateMachine.addState("Idle", Collections.emptyList());

        stateMachine.addState("Approaching", Arrays.asList(new CallFunctionAction(stateMachine, "raise_gate", null)));
        stateMachine.addState("Crossing", Arrays.asList(new CallFunctionAction(stateMachine, "turn_on_light", null)));
        stateMachine.addState("Leaving", Arrays.asList(new CallFunctionAction(stateMachine, "lower_gate", null)));

        stateMachine.addTransition("Idle", "seen", "Approaching");
        stateMachine.addTransition("Approaching", "seen", "Crossing");
        stateMachine.addTransition("Crossing", "¬seen", "Leaving");
        stateMachine.addTransition("Leaving", "¬seen", "Idle");

        //Function
        stateMachine.addFunction("raise_gate", new ExampleFunction());
        stateMachine.addFunction("lower_gate", new ExampleFunction());
        stateMachine.addFunction("turn_on_light", new ExampleFunction());
        stateMachine.addFunction("turn_off_light", new ExampleFunction());

        stateMachine.setInitialState("Idle");
    }

    public void handleEvent(Event event) throws JsonProcessingException {
        stateMachine.handleEvent(event);

        Event event1;
        if(event.getName().equals("seen")) {
            event1 = new Event("approaching",null);
            log.info("train is approaching.");
        }else{
            event1 = new Event("leaving",null);
            log.info("train left.");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(event1);
        rabbitTemplate.convertAndSend("gate-queue", json);
        rabbitTemplate.convertAndSend("light-queue", json);

    }
}
