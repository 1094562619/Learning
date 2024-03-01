package com.learning.kafka;

/**
 * kafka配置类
 *
 * @description: TODO
 * @author: tf
 * @date: 2024年03月01日 14:35
 */

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * 通过重新注册KafkaStreamsConfiguration对象，设置自定配置参数
 * 可能你会有这样的疑问，前面介绍Kafka时候不是直接在yml文件里面设置参数就行了吗？为什么这里还要自己写配置类呢？
 * 是因为Spring对KafkaStream的集成并不是很好，所以我们才需要自己去写配置类信息。
 * 需要注意的一点是，配置类中必须添加@EnableKafkaStreams这一注解。
 * <p>
 * 原文链接：https://blog.csdn.net/weixin_45750572/article/details/126100115
 */
@Setter
@Getter
@Configuration
@EnableKafkaStreams
@ConfigurationProperties(prefix = "kafka")
public class KafkaStreamConfig {
    private static final int MAX_MESSAGE_SIZE = 16 * 1024 * 1024;
    private String hosts;
    private String group;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration defaultKafkaStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, hosts);
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, this.getGroup() + "_stream_aid");
        props.put(StreamsConfig.CLIENT_ID_CONFIG, this.getGroup() + "_stream_cid");
        props.put(StreamsConfig.RETRIES_CONFIG, 10);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        return new KafkaStreamsConfiguration(props);
    }
}