package org.example.gate;

import com.example.state.CallFunctionAction;
import com.example.state.Event;
import com.example.state.ExampleFunction;
import com.example.state.StateMachine;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

@Service
@Slf4j
public class Gate {
    private StateMachine stateMachine;

    public Gate() {
        stateMachine = new StateMachine("Gate");
        // State Open Closed
        // event approaching leaving
        stateMachine.addState("Open", Collections.emptyList());
        stateMachine.addState("Closed", Arrays.asList(new CallFunctionAction(stateMachine, "lower_gate", null)));

        stateMachine.addTransition("Open", "approaching", "Closed");
        stateMachine.addTransition("Closed", "leaving", "Open");

        //function
        stateMachine.addFunction("lower_gate", new ExampleFunction());
        stateMachine.addFunction("raise_gate", new ExampleFunction());

        stateMachine.setInitialState("Open"); //currentstate
    }

    @RabbitListener(queues = "gate-queue")
    public void receiveMessage(String json) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        // seen,¬seen
        Event event = new Event(jsonNode.get("name").asText(),jsonNode.get("data"));
        log.info("Train: {}", event.getName());
        stateMachine.handleEvent(event);

    }
}