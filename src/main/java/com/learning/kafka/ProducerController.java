package com.learning.kafka;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * 生产者
 *
 * @description: TODO
 * @author: tf
 * @date: 2024年03月01日 16:40
 */
@Api(tags = "kafka生产者")
@RestController
public class ProducerController {
    @Autowired
    private AdminClient adminClient;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @ApiOperation(value = "发送消息")
    @PostMapping("/addMessage")
    public String addMessage(@Param("message") String message, @Param("topic") String topic, @Param("key") String key) {
        kafkaTemplate.send(topic, key, message);
        return "ok";
    }

    //创建主题
    @ApiOperation(value = "创建主题")
    @PostMapping("/createTopic")
    public String createTopic(@ApiParam(value = "主题名字", required = true) String topicName, @ApiParam(value = "分区数量",
            required = true) int partitions, @ApiParam(value = "备份数量", required = true) short replicationFactor) {
        NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);
        try {
            adminClient.createTopics(Collections.singleton(newTopic)).all().get();
            return "ok";
        } catch (Exception e) {
            return "failed";
        }
    }

    //删除主题
    @ApiOperation(value = "删除主题")
    @PostMapping("/deleteTopic")
    public String deleteTopic(@ApiParam(value = "主题名字", required = true) String topicName) {
        try {
            adminClient.deleteTopics(Collections.singleton(topicName)).all().get();
            return "ok";
        } catch (Exception e) {
            return "failed";
        }
    }

}
