package com.tutorial.kafka.controller;

import com.tutorial.kafka.log.annotation.LogRuntimeLogger;
import com.tutorial.kafka.log.annotation.LogTag;
import com.tutorial.kafka.producer.KafkaSender;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@AllArgsConstructor
@RestController
@RequestMapping("/kafka")
@LogRuntimeLogger(topic = "topic-log")
public class KafkaController {

    private final KafkaSender kafkaSender;

    @PostMapping("/txt")
    @LogTag("SEND_TXT")
    public void send(String txt) {
        kafkaSender.sendTxt(txt);
    }

    @PostMapping("/file")
    @LogTag("SEND_FILE")
    public void uploadHandler(@RequestParam("file") MultipartFile file) {
        kafkaSender.sendFile(file);
    }
}
