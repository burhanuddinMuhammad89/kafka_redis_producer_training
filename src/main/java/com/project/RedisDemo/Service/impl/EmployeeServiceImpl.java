package com.project.RedisDemo.Service.impl;

import com.google.gson.Gson;
import com.project.RedisDemo.Service.EmployeeService;
import com.project.RedisDemo.model.Employee;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Value("${my.property}")
    private String myProperty;

    @Value("${my.topic}")
    private String topicName;

    Jedis jedis = new Jedis("127.0.0.1",6379);

    @Override
    public void redisSend(String employeName) {

        if(jedis.isConnected()){
            System.out.println("Connection to server sucessfully");
        }

        //store data in redis list
        jedis.sadd("employee-name", employeName);
    }

    @Override
    public Set<String> getRedisData() {
        return jedis.smembers("employee-name");
    }

    @Override
    public void sendToKafka(Employee employee) {

        String json = new Gson().toJson(employee);

        // create instance for properties to access producer configs
        Properties props = new Properties();

        //Assign localhost id
        props.put("bootstrap.servers", myProperty);

        //Set acknowledgements for producer requests.
        props.put("acks", "all");

                //If the request fails, the producer can automatically retry,
                props.put("retries", 0);

        //Specify buffer size in config
        props.put("batch.size", 16384);

        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);

        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer
                <String, String>(props);

            producer.send(new ProducerRecord<String, String>(topicName,
                    json));
        System.out.println("Message sent successfully");
        producer.close();


    }

    @Override
    public void getEmployeeKafka() {
        final Consumer<Long, String> consumer = null;
        final int giveUp = 100;   int noRecordsCount = 0;
            final ConsumerRecords<Long, String> consumerRecords =
                    consumer.poll(1000);
//            if (consumerRecords.count()==0) {
//                noRecordsCount++;
//                if (noRecordsCount > giveUp) break;
//                else continue;
//            }
            consumerRecords.forEach(record -> {
                System.out.printf("Consumer Record:(%d, %s, %d, %d)\n",
                        record.key(), record.value(),
                        record.partition(), record.offset());
            });
            consumer.commitAsync();
        consumer.close();
        System.out.println("DONE");
    }

}
