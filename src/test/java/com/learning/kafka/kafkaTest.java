package com.learning.kafka;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * ������
 *
 * @description: TODO
 * @author: tf
 * @date: 2024��03��01�� 16:07
 */
public class kafkaTest {

    private String stateDir = "E:\\develop\\projects\\kafka";

    @Test
    public void wordCount() {
        /**
         *  1.����һ��java.util.Propertiesӳ����ָ����StreamsConfig����ֵ��
         *  ��1��BOOTSTRAP_SERVERS_CONFIG����ָ����һ������/�˿ڶԣ���ʾKafka��ַ��
         *  ��2��APPLICATION_ID_CONFIG�����ṩ��StreamsӦ�ó����Ψһ��ʶ����
         *  ��3���������ã����磬��¼��ֵ�Ե�Ĭ�����л��ͷ����л���
         */
        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "42.192.111.87:9092");
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-wordcount");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.STATE_DIR_CONFIG, stateDir);
        /**
         * 2.����StreamsӦ�ó���ļ����߼���
         *   (1)����Ϊ���Ӵ������ڵ�����ˣ�
         */
        final StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> source = builder.stream("streams-plaintext-input");
        source.flatMapValues(value -> Arrays.asList(value.toLowerCase(Locale.getDefault()).split("\\W+"))).groupBy((key, value) -> value).count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store")).toStream().to("streams-wordcount-output", Produced.with(Serdes.String(), Serdes.Long()));
        final Topology topology = builder.build();
        /**
         * ��2��������ͼ����KafkaStreams��client��
         */
        final KafkaStreams streams = new KafkaStreams(topology, props);


        /**
         * 3.����stream��������һֱ����
         * ͨ����������start()���������ǿ��Դ�������ͻ�����ִ�С��ڴ˿ͻ����ϵ���close()֮ǰ��ִ�в���ֹͣ��
         * ���磬���ǿ������һ�����е���ʱ���ŵĹػ������������û��жϣ�������ֹ����ʱ�رտͻ���:
         */
        final CountDownLatch latch = new CountDownLatch(1);
        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }
}
