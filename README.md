# Hello Kafka!


## Commands
```
brew install kafka
```

### Server
```
zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties

kafka-server-start /usr/local/etc/kafka/server.properties
```

### Topic
```
kafka-topics -version

kafka-topics --bootstrap-server localhost:9092 --list

kafka-topics --bootstrap-server localhost:9092 --topic first_topic --create

kafka-topics --bootstrap-server localhost:9092 --topic second_topic --create --partitions 5

kafka-topics --bootstrap-server localhost:9092 --topic java-program-sticky-one --create --partitions 5

kafka-topics --bootstrap-server localhost:9092 --topic java-program-round-robin-one --create --partitions 5

kafka-topics --bootstrap-server localhost:9092 --topic third_topic --create --partitions 3 --replication-factor 1

kafka-topics --bootstrap-server localhost:9092 --topic first_topic --describe

kafka-topics --bootstrap-server localhost:9092 --topic unknown_topic --delete
```

### Producer
```
kafka-console-producer --bootstrap-server localhost:9092  --topic first_topic

kafka-console-producer --bootstrap-server localhost:9092  --topic first_topic --producer-property acks=all

kafka-console-producer --bootstrap-server localhost:9092  --topic new_topic 

kafka-console-producer --bootstrap-server localhost:9092 --producer-property partitioner.class=org.apache.kafka.clients.producer.RoundRobinPa
rtitioner --topic second_topic

kafka-console-producer --bootstrap-server localhost:9092 \
--producer-property partitioner.class=org.apache.kafka.clients.producer.RoundRobinPartitioner \ 
--topic second_topic
```
### Consumer
``` 
kafka-console-consumer --bootstrap-server localhost:9092 --topic second_topic

kafka-console-consumer --bootstrap-server localhost:9092 --topic second_topic --from-beginning

kafka-console-consumer --bootstrap-server localhost:9092 --topic second_topic  \
--formatter kafka.tools.DefaultMessageFormatter \
--property print.timestamp=true --property print.key=true \ 
--property print.value=true --property print.partition=true --from-beginning 
```


### Consumer Group
``` 
kafka-console-consumer --bootstrap-server localhost:9092 --topic third_topic  --formatter kafka.tools.DefaultMessageFormatter \ 
--property print.timestamp=true  \
--property print.key=true --property print.value=true \ 
--property print.partition=true --from-beginning --group my-first-consumer-group

kafka-consumer-groups --bootstrap-server localhost:9092 --list

kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group my-first-consumer-group

kafka-consumer-groups --bootstrap-server localhost:9092 --group my-first-consumer-group --reset-offsets --to-earliest --topic third_topic --dry-run

kafka-consumer-groups --bootstrap-server localhost:9092 --group my-first-consumer-group --reset-offsets --to-earliest --topic third_topic --execute

```