//package com.learning.iotdb.config;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * IOTDB配置类
// *
// * @author: tf
// * @date: 2024年02月01日 14:10
// */
//@Slf4j
//@Configuration
//public class IotDbConfig {
//    private static SessionPool sessionPool;
//    @Value("${spring.iotdb.username}")
//    private String username;
//
//    @Value("${spring.iotdb.password}")
//    private String password;
//
//    @Value("${spring.iotdb.ip}")
//    private String ip;
//
//    @Value("${spring.iotdb.port:6667}")
//    private int port;
//
//    @Value("${spring.iotdb.maxSize:10}")
//    private int maxSize;
//
//    @Bean
//    public SessionPool sessionPool() {
//        return new SessionPool.Builder().host(ip).port(port).user(username).password(password).maxSize(maxSize).build();
//    }
//
//}
