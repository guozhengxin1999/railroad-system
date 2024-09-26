package org.example.light;

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
public class Light {
    private StateMachine stateMachine;

    public Light() {
        stateMachine = new StateMachine("Light");
        // State Off On
        // Event approaching leaving
        stateMachine.addState("Off", Collections.emptyList());
        stateMachine.addState("On", Arrays.asList(new CallFunctionAction(stateMachine, "turn_on_light", null)));

        stateMachine.addTransition("Off", "approaching", "On");
        stateMachine.addTransition("On", "leaving", "Off");

        //function
        stateMachine.addFunction("turn_on_light", new ExampleFunction());
        stateMachine.addFunction("turn_off_light", new ExampleFunction());

        stateMachine.setInitialState("Off");
    }

    @RabbitListener(queues = "light-queue")
    public void receiveMessage(String json) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        Event event = new Event(jsonNode.get("name").asText(),jsonNode.get("data"));
        log.info("Train: {}", event.getName());
        stateMachine.handleEvent(event);
    }
}
