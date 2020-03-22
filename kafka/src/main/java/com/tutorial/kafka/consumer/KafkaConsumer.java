package com.tutorial.kafka.consumer;

import com.alibaba.fastjson.JSON;
import com.tutorial.kafka.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;

@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(id = "tt", topics = "topic-txt")
    public void listenTxt(ConsumerRecord<?, ?> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            log.info("+++++++++++++++ Receive:Topic:" + topic);
            log.info("+++++++++++++++ Receive:Record:" + record);
            log.info("+++++++++++++++ Receive:Message:" + message);
        }
    }

    @KafkaListener(id = "tf", topics = "topic-file")
    public void listenFile(ConsumerRecord<?, ?> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {
            String msg = (String) kafkaMessage.get();
            File file = JSON.parseObject(msg, File.class);
            log.info("+++++++++++++++ Receive:Topic:" + topic);
            try {
                saveFile("QA.pdf", file);
            } catch (Exception e) {
                log.error("保存文件失败");
            }
        }
    }

    private void saveFile(String filename ,File saveFile) throws Exception {
        byte[] data = FileUtil.file2byte(saveFile);
        if(data != null){
            String filepath ="/Users/guangzheng.li/IdeaProjects/tutorial/download/" + filename;
            File file  = new File(filepath);
            if(file.exists()){
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data,0,data.length);
            fos.flush();
            fos.close();
        }
    }

}
