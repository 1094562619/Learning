package com.learning.kafka;

/**
 * @description: TODO
 * @author: tf
 * @date: 2024年03月01日 15:35
 */
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

import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
public class WordCount {

    public static void main(String[] args) throws Exception {
        /**
         *  1.创建一个java.util.Properties映射来指定在StreamsConfig配置值。
         *  （1）BOOTSTRAP_SERVERS_CONFIG，它指定了一个主机/端口对，表示Kafka地址；
         *  （2）APPLICATION_ID_CONFIG，它提供了Streams应用程序的唯一标识符；
         *  （3）其他配置，例如，记录键值对的默认序列化和反序列化库
         */
        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "42.42.192.111.87:9092");
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-wordcount");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        /**
         * 2.定义Streams应用程序的计算逻辑。
         *   (1)定义为连接处理器节点的拓扑；
         */
        final StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> source = builder.stream("streams-plaintext-input");
        source.flatMapValues(value -> Arrays.asList(value.toLowerCase(Locale.getDefault()).split("\\W+")))
                .groupBy((key, value) -> value)
                .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store"))
                .toStream()
                .to("streams-wordcount-output", Produced.with(Serdes.String(), Serdes.Long()));
        final Topology topology = builder.build();
        /**
         * （2）将拓扑图放入KafkaStreams流client中
         */
        final KafkaStreams streams = new KafkaStreams(topology, props);


        /**
         * 3.启动stream流，让其一直运行
         * 通过调用它的start()函数，我们可以触发这个客户机的执行。在此客户机上调用close()之前，执行不会停止。
         * 例如，我们可以添加一个带有倒计时锁闩的关机钩子来捕获用户中断，并在终止程序时关闭客户端:
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