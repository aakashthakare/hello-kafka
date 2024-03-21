package kafka.consumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerMain {

    public static final Logger log = LoggerFactory.getLogger(ConsumerMain.class.getSimpleName());

    private static void simpleConsumer() {
        String groupId = "my-first-consumer-group";

        Properties properties = ConsumerUtils.defaultConsumerProperties();
        properties.setProperty("group.id", groupId);
        properties.setProperty("auto.offset.reset", "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        String topic = "java-program";
        consumer.subscribe(Arrays.asList(topic));
        while(true) {
            log.info("Polling the messages.");
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

            for (ConsumerRecord<String, String> record: records) {
                log.info("Key {} | Value {} ", record.key(), record.value());
            }
        }
    }

    private static void shutdownConsumer() {
        String topic = "java-program";
        String groupId = "my-first-consumer-group";

        Properties properties = ConsumerUtils.defaultConsumerProperties();
        properties.setProperty("group.id", groupId);
        properties.setProperty("auto.offset.reset", "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        final Thread currentThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(() ->{
            log.info("Shutdown is detected.");
            consumer.wakeup();
            try {
                currentThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));

        try {
            consumer.subscribe(Arrays.asList(topic));
            while(true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

                for (ConsumerRecord<String, String> record: records) {
                    log.info("Key {} | Value {} ", record.key(), record.value());
                }
            }
        } catch (WakeupException e) {
            log.error("Wake Up", e);
        } catch (Exception e) {
            log.error("Error", e);
        } finally {
            consumer.close();
            log.info("The consumer is gracefully closed.");
        }
    }

    private static void coOpAssignorConsumer() {
        String groupId = "my-first-consumer-group";
        Properties properties = ConsumerUtils.defaultConsumerProperties();
        properties.setProperty("group.id", groupId);
        properties.setProperty("auto.offset.reset", "earliest");
        properties.setProperty("partition.assignment.strategy", CooperativeStickyAssignor.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        String topic = "java-program-with-group";
        consumer.subscribe(Arrays.asList(topic));
        while(true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

            for (ConsumerRecord<String, String> record: records) {
                log.info("Key {} | Value {} ", record.key(), record.value());
            }
        }
    }

    public static void main(String[] args) {
        simpleConsumer();
        shutdownConsumer();
        coOpAssignorConsumer();
    }
}
