package com.tutorial.kafka.controller;

import com.tutorial.kafka.producer.KafkaSender;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@AllArgsConstructor
@RestController
@RequestMapping("/kafka")
public class KafkaController {

    private final KafkaSender kafkaSender;

    @PostMapping("/txt")
    public void send(String txt) {
        kafkaSender.sendTxt(txt);
    }

    @PostMapping("/file")
    public void uploadHandler(@RequestParam("file") MultipartFile file) throws IOException {
        kafkaSender.sendFile(file);
    }
}
