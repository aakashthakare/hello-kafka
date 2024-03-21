package kafka.consumer;

import java.util.Properties;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerUtils {
    public static final Logger log = LoggerFactory.getLogger(ConsumerUtils.class.getSimpleName());

    public static Properties defaultConsumerProperties() {
        Properties producerProps = new Properties();
        producerProps.setProperty("bootstrap.servers", "localhost:9092");
        producerProps.setProperty("key.deserializer", StringDeserializer.class.getName());
        producerProps.setProperty("value.deserializer", StringDeserializer.class.getName());
        return producerProps;
    }
}
