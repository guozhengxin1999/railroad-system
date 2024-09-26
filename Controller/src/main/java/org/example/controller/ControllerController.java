package org.example.controller;

import com.example.state.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class ControllerController {
    @Autowired
    private Controller controller;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        String content = new String(file.getBytes(),  StandardCharsets.UTF_8);
        String[] lines = content.split("\n");
        for(String line : lines){
            Event event = new Event(line,null);
            controller.handleEvent(event);
        }

        return "Event processed";
    }
}
