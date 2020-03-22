package com.tutorial.kafka.producer;

import com.alibaba.fastjson.JSON;
import com.tutorial.kafka.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.multipart.MultipartFile;


@Component
@Slf4j
public class KafkaSender {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_TXT = "topic-txt";

    private static final String TOPIC_FILE = "topic-file";

    public void sendTxt(Object str) {
        String jsonObj = JSON.toJSONString(str);
        log.info("------------ message = {}", jsonObj);

        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(TOPIC_TXT, jsonObj);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.info("------------ Produce: The message failed to be sent:" + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                log.info("------------ Produce: The message was sent successfully:");
                log.info("------------ Produce: result: " + stringObjectSendResult.toString());
            }
        });
    }

    public void sendFile(MultipartFile file) {
        log.info("------------ FileName=" + file.getName()+file.getSize());
        String jsonObj = JSON.toJSONString(FileUtil.multipartFile2File(file));
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(TOPIC_FILE, jsonObj);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.info("------------ Produce: The file failed to be sent:" + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                log.info("------------ Produce: The file was sent successfully");
            }
        });

    }
}
