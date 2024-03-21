package kafka.producer;

import static kafka.producer.ProducerUtils.defaultProducerProperties;

import java.util.Objects;
import java.util.Properties;
import java.util.UUID;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RoundRobinPartitioner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerMain {

    public static final Logger log = LoggerFactory.getLogger(ProducerMain.class.getSimpleName());

    public static void simpleProducer() {
        try(KafkaProducer<String, String> producer = new KafkaProducer<>(defaultProducerProperties())) {
            ProducerRecord<String, String> record =
                new ProducerRecord<>("java-program", "Hello World!");
            producer.send(record);
        }
    }

    public static void producerCallback() {
        try(KafkaProducer<String, String> producer = new KafkaProducer<>(defaultProducerProperties())) {
            ProducerRecord<String, String> record = new ProducerRecord<>("java-program", "Hello World!" +
                UUID.randomUUID());
            producer.send(record, ProducerUtils::logSendCallback);
        }
    }

    public static void producerStickyPartitioner() throws InterruptedException {
        Properties properties = defaultProducerProperties();
        properties.setProperty("batch.size", "100");

        try(KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
            for (int i = 0; i < 30; i++) {
                for (int j = 0; j < 30; j++) {
                    ProducerRecord<String, String> record =
                        new ProducerRecord<>("java-program-sticky-one", "Hello World!" + UUID.randomUUID());
                    producer.send(record, ProducerUtils::logSendCallback);
                }
                Thread.sleep(500);
            }
        }
    }

    public static void producerRoundRobin() throws InterruptedException {
        Properties properties = defaultProducerProperties();
        properties.setProperty("partitioner.class", RoundRobinPartitioner.class.getName());

        try(KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
            for (int i = 0; i < 30; i++) {
                for (int j = 0; j < 30; j++) {
                    ProducerRecord<String, String> record =
                        new ProducerRecord<>("java-program-round-robin-one", "Hello World!");
                    producer.send(record, ProducerUtils::logSendCallback);
                }
                Thread.sleep(500);
            }
        }
    }


    public static void producerWithKeys() throws InterruptedException {
        Properties properties = defaultProducerProperties();

        try(KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
            for (int j = 0; j < 2; j++) {
                for (int i = 0; i < 10; i++) {
                    String key = "Key" + i;
                    ProducerRecord<String, String> record =
                        new ProducerRecord<>("java-program-key-in-partition", key, "Hello World!");
                    producer.send(record, (metadata, exception) -> {
                        if (Objects.isNull(exception)) {
                            log.info("Partition : {} | Key : {}", metadata.partition(), key);
                        } else {
                            log.error("Exception in producer with keys callback.", exception);
                        }
                    });
                    Thread.sleep(500);
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        simpleProducer();
//        producerCallback();
//        producerStickyPartitioner();
//        producerRoundRobin();
        producerWithKeys();
    }
}
