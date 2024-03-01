//package com.learning.iotdb;
//
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * @description: TODO
// * @author: tf
// * @date: 2024年02月01日 15:47
// */
//public class SessionPoolExample {
//    private static final Logger LOGGER = LoggerFactory.getLogger(SessionPoolExample.class);
//
//    private static SessionPool sessionPool;
//    private static ExecutorService service;
//
//    /**
//     * Build a custom SessionPool for this example
//     */
//    private static void constructCustomSessionPool() {
//        sessionPool = new SessionPool.Builder().host("42.192.111.87").port(6667).user("root").password("root").maxSize(3).build();
//    }
//
//    /**
//     * Build a redirect-able SessionPool for this example
//     */
//    private static void constructRedirectSessionPool() {
//        List<String> nodeUrls = new ArrayList<>();
//        nodeUrls.add("127.0.0.1:6667");
//        nodeUrls.add("127.0.0.1:6668");
//        sessionPool = new SessionPool.Builder().nodeUrls(nodeUrls).user("root").password("root").maxSize(3).build();
//    }
//
//    public static void main(String[] args) throws StatementExecutionException, IoTDBConnectionException, InterruptedException {
//        // Choose the SessionPool you going to use
////        constructRedirectSessionPool();
//        constructCustomSessionPool();
//        service = Executors.newFixedThreadPool(10);
//        //记录执行时间
//        long start = System.currentTimeMillis();
//        insertRecord();
//        long end = System.currentTimeMillis();
//        System.out.println("插入数据耗时：" + (end - start) + "ms");
//        queryByRowRecord();
//        Thread.sleep(1000);
//        queryByIterator();
//        sessionPool.close();
//        service.shutdown();
//    }
//
//    // more insert example, see SessionExample.java
//    private static void insertRecord() throws StatementExecutionException, IoTDBConnectionException {
//        String deviceId = "root.14.car.group1.device1.speed";
//        List<String> measurements = new ArrayList<>();
//        List<TSDataType> types = new ArrayList<>();
//        measurements.add("device_no");
//        measurements.add("device_name");
//        measurements.add("device_category_no");
//        measurements.add("device_type");
//        measurements.add("device_software_version");
//        measurements.add("ip_addr");
//        measurements.add("enabled_state");
//        measurements.add("memo");
//        measurements.add("gps_altitude");
//        measurements.add("gps_latitude");
//        measurements.add("gps_longitude");
//        measurements.add("kwh");
//        measurements.add("temperature");
//        measurements.add("car_type");
//        measurements.add("car_number");
//        measurements.add("offline_time");
//        measurements.add("car_id");
//        types.add(TSDataType.INT64);
//        types.add(TSDataType.TEXT);
//        types.add(TSDataType.INT64);
//        types.add(TSDataType.TEXT);
//        types.add(TSDataType.TEXT);
//        types.add(TSDataType.TEXT);
//        types.add(TSDataType.BOOLEAN);
//        types.add(TSDataType.TEXT);
//        types.add(TSDataType.DOUBLE);
//        types.add(TSDataType.DOUBLE);
//        types.add(TSDataType.DOUBLE);
//        types.add(TSDataType.DOUBLE);
//        types.add(TSDataType.DOUBLE);
//        types.add(TSDataType.TEXT);
//        types.add(TSDataType.TEXT);
//        types.add(TSDataType.INT64);
//        types.add(TSDataType.INT64);
//
//        for (long time = 0; time < 100000; time++) {
//            List<Object> values = new ArrayList<>();
//            values.add(1L);
//            values.add("device_name");
//            values.add(1L);
//            values.add("device_type");
//            values.add("device_software_version");
//            values.add("ip_addr");
//            values.add(true);
//            values.add("memo");
//            values.add(1.0);
//            values.add(1.0);
//            values.add(1.0);
//            values.add(1.0);
//            values.add(1.0);
//            values.add("car_type");
//            values.add("car_number");
//            values.add(1L);
//            values.add(1L);
//            sessionPool.insertRecord(deviceId, time, measurements, types, values);
//        }
//    }
//
//    private static void queryByRowRecord() {
//        for (int i = 0; i < 1; i++) {
//            service.submit(() -> {
//                SessionDataSetWrapper wrapper = null;
//                try {
//                    wrapper = sessionPool.executeQueryStatement("select * from root.sg1.d1");
//                    System.out.println(wrapper.getColumnNames());
//                    System.out.println(wrapper.getColumnTypes());
//                    while (wrapper.hasNext()) {
//                        System.out.println(wrapper.next());
//                    }
//                } catch (IoTDBConnectionException | StatementExecutionException e) {
//                    LOGGER.error("Query by row record error", e);
//                } finally {
//                    // remember to close data set finally!
//                    sessionPool.closeResultSet(wrapper);
//                }
//            });
//        }
//    }
//
//    private static void queryByIterator() {
//        for (int i = 0; i < 1; i++) {
//            service.submit(() -> {
//                SessionDataSetWrapper wrapper = null;
//                try {
//                    wrapper = sessionPool.executeQueryStatement("select * from root.sg1.d1");
//                    // get DataIterator like JDBC
//                    SessionDataSet.DataIterator dataIterator = (SessionDataSet.DataIterator) wrapper.iterator();
//                    System.out.println(wrapper.getColumnNames());
//                    System.out.println(wrapper.getColumnTypes());
//                    while (dataIterator.next()) {
//                        StringBuilder builder = new StringBuilder();
//                        for (String columnName : wrapper.getColumnNames()) {
//                            builder.append(dataIterator.getString(columnName) + " ");
//                        }
//                        System.out.println(builder);
//                    }
//                } catch (IoTDBConnectionException | StatementExecutionException e) {
//                    LOGGER.error("Query by Iterator error", e);
//                } finally {
//                    // remember to close data set finally!
//                    sessionPool.closeResultSet(wrapper);
//                }
//            });
//        }
//    }
//}
