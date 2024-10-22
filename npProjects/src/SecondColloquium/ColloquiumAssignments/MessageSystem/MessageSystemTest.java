package SecondColloquium.ColloquiumAssignments.MessageSystem;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

class UnsupportedOperationException extends Exception {

    public UnsupportedOperationException() {

    }
}

class PartitionDoesNotExistException extends Exception {

    public PartitionDoesNotExistException(String topicName, int partitionNumber) {
        // The topic1 does not have a partition with number 7
        super(String.format("The %s does not have a partition with number %d", topicName, partitionNumber));
    }
}

class PartitionAssigner {

    public static Integer assignPartition(Message message, int partitionsCount) {
        return (Math.abs(message.getKey().hashCode()) % partitionsCount) + 1;
    }
}

class Message implements Comparable<Message> {

    private LocalDateTime timestamp;
    private String message;
    private Integer partition;
    private String key;

    public Message(LocalDateTime timestamp, String message, String key) {
        this.timestamp = timestamp;
        this.message = message;
        this.key = key;
    }

    public Message(LocalDateTime timestamp, String message, Integer partition, String key) {
        this.timestamp = timestamp;
        this.message = message;
        this.partition = partition;
        this.key = key;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public Integer getPartition() {
        return partition;
    }

    public String getKey() {
        return key;
    }

    @Override
    public int compareTo(Message other) {
        return this.timestamp.compareTo(other.timestamp);
    }

    @Override
    public String toString() {
        return "Message{" +
                "timestamp=" + timestamp +
                ", message='" + message + '\'' +
                '}';
    }
}

class Partition {

    private int number;
    private TreeSet<Message> messages;

    public Partition(int number) {
        this.number = number;
        this.messages = new TreeSet<>();
    }

    public Partition(int number, TreeSet<Message> messages) {
        this.number = number;
        this.messages = messages;
    }

    public void addMessage(Message message) {
        if (message.getTimestamp().isBefore(MessageBroker.getMinimumDate())) {
            return;
        }
        if (messages.size() == MessageBroker.getCapacityPerTopic()) {
            messages.remove(messages.first());
        }

        messages.add(message);
    }

    public int getCountOfMessages() {
        return messages.size();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("%2d : Count of messages:%6d", number, getCountOfMessages()))
                .append("\n");
        builder.append("Messages:")
                .append("\n");
        String messagesStr = messages.stream()
                .map(m -> m.toString())
                .collect(Collectors.joining("\n"));
        builder.append(messagesStr);

        return builder.toString();
    }
}

class Topic {

    private String name;
    private int partitionsCount;
    private TreeMap<Integer, Partition> partitions;

    public Topic(String name, int partitionsCount) {
        this.name = name;
        this.partitionsCount = partitionsCount;
        this.partitions = new TreeMap<>();

        for (int i = 1; i <= partitionsCount; i++) {
            partitions.put(i, new Partition(i));
        }
    }

    public String getName() {
        return name;
    }

    public int getPartitionsCount() {
        return partitionsCount;
    }

    public TreeMap<Integer, Partition> getPartitions() {
        return partitions;
    }

    public void addMessage(Message message) throws PartitionDoesNotExistException {
        Integer partition = message.getPartition();
        if (partition == null) {
            partition = PartitionAssigner.assignPartition(message, partitionsCount);
        }

        if (!partitions.containsKey(partition)) {
            throw new PartitionDoesNotExistException(name, partition);
        }

        partitions.get(partition).addMessage(message);
    }

    public void changeNumberOfPartitions(int newPartitionsNumber) throws UnsupportedOperationException {
        if (newPartitionsNumber < partitionsCount) {
            throw new UnsupportedOperationException();
        } else if (newPartitionsNumber > partitionsCount) {
            for (int i = partitionsCount + 1; i <= newPartitionsNumber; i++) {
                partitions.put(i, new Partition(i));
            }

            partitionsCount = newPartitionsNumber;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("Topic: %10s Partitions: %5d", name, partitionsCount))
                .append("\n");
        String partitionsStr = partitions.values().stream()
                .map(p -> p.toString())
                .collect(Collectors.joining("\n"));
        builder.append(partitionsStr);

        return builder.toString();
    }
}

class MessageBroker {

    private static LocalDateTime MINIMUM_DATE;
    private static Integer CAPACITY_PER_TOPIC;
    private Map<String, Topic> topics;

    public MessageBroker(LocalDateTime minimumDate, Integer capacityPerTopic) {
        MINIMUM_DATE = minimumDate;
        CAPACITY_PER_TOPIC = capacityPerTopic;
        this.topics = new TreeMap<>();
    }

    public static LocalDateTime getMinimumDate() {
        return MINIMUM_DATE;
    }

    public static Integer getCapacityPerTopic() {
        return CAPACITY_PER_TOPIC;
    }

    public Map<String, Topic> getTopics() {
        return topics;
    }

    public void addTopic(String topic, int partitionsCount) {
        if (!topics.containsKey(topic)) {
            topics.put(topic, new Topic(topic, partitionsCount));
        }
    }

    public void addMessage(String topic, Message message) throws PartitionDoesNotExistException {
        if (message.getTimestamp().isBefore(MINIMUM_DATE)) {
            return;
        }

        topics.get(topic).addMessage(message);
    }

    public void changeTopicSettings(String topic, int partitionsCount) throws UnsupportedOperationException {
        topics.get(topic).changeNumberOfPartitions(partitionsCount);
    }

    private int getCountOfTopics() {
        return topics.size();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("Broker with %2d topics:", getCountOfTopics()))
                .append("\n");
        String topicsStr = topics.values().stream()
                .map(t -> t.toString())
                .collect(Collectors.joining("\n"));
        builder.append(topicsStr);

        return builder.toString();
    }
}

public class MessageSystemTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String date = sc.nextLine();
        LocalDateTime localDateTime = LocalDateTime.parse(date);
        Integer partitionsLimit = Integer.parseInt(sc.nextLine());
        MessageBroker broker = new MessageBroker(localDateTime, partitionsLimit);
        int topicsCount = Integer.parseInt(sc.nextLine());

        //Adding topics
        for (int i = 0; i < topicsCount; i++) {
            String line = sc.nextLine();
            String[] parts = line.split(";");
            String topicName = parts[0];
            int partitionsCount = Integer.parseInt(parts[1]);
            broker.addTopic(topicName, partitionsCount);
        }

        //Reading messages
        int messagesCount = Integer.parseInt(sc.nextLine());

        System.out.println("===ADDING MESSAGES TO TOPICS===");
        for (int i = 0; i < messagesCount; i++) {
            String line = sc.nextLine();
            String[] parts = line.split(";");
            String topic = parts[0];
            LocalDateTime timestamp = LocalDateTime.parse(parts[1]);
            String message = parts[2];
            if (parts.length == 4) {
                String key = parts[3];
                try {
                    broker.addMessage(topic, new Message(timestamp, message, key));
                } catch (PartitionDoesNotExistException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                Integer partition = Integer.parseInt(parts[3]);
                String key = parts[4];
                try {
                    broker.addMessage(topic, new Message(timestamp, message, partition, key));
                } catch (PartitionDoesNotExistException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        System.out.println("===BROKER STATE AFTER ADDITION OF MESSAGES===");
        System.out.println(broker);

        System.out.println("===CHANGE OF TOPICS CONFIGURATION===");
        //topics changes
        int changesCount = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < changesCount; i++) {
            String line = sc.nextLine();
            String[] parts = line.split(";");
            String topicName = parts[0];
            Integer partitions = Integer.parseInt(parts[1]);
            try {
                broker.changeTopicSettings(topicName, partitions);
            } catch (UnsupportedOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("===ADDING NEW MESSAGES TO TOPICS===");
        messagesCount = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < messagesCount; i++) {
            String line = sc.nextLine();
            String[] parts = line.split(";");
            String topic = parts[0];
            LocalDateTime timestamp = LocalDateTime.parse(parts[1]);
            String message = parts[2];
            if (parts.length == 4) {
                String key = parts[3];
                try {
                    broker.addMessage(topic, new Message(timestamp, message, key));
                } catch (PartitionDoesNotExistException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                Integer partition = Integer.parseInt(parts[3]);
                String key = parts[4];
                try {
                    broker.addMessage(topic, new Message(timestamp, message, partition, key));
                } catch (PartitionDoesNotExistException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        System.out.println("===BROKER STATE AFTER CONFIGURATION CHANGE===");
        System.out.println(broker);


    }
}