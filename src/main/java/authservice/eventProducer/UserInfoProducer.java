package authservice.eventProducer;

import authservice.model.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class UserInfoProducer {
    public final KafkaTemplate<String, UserInfoDto> kafkaTemplate;

    @Autowired
    public UserInfoProducer(KafkaTemplate<String, UserInfoDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    @Value("${spring.kafka.topic-json.name}")
    private String topicJsonName;


    public void sendEventToKafka(UserInfoEvent eventData) {
        Message<UserInfoEvent> message = MessageBuilder.withPayload(eventData)
                .setHeader(KafkaHeaders.TOPIC, topicJsonName).build();
        System.out.println("📤 Sending to Kafka:");
        System.out.println("👤 UserID: " + eventData.getUserId());
        System.out.println("👨‍🦰 FirstName: " + eventData.getFirstName());
        System.out.println("🧑‍🦰 LastName: " + eventData.getLastName());
        System.out.println("📞 Phone: " + eventData.getPhoneNumber());
        System.out.println("📧 Email: " + eventData.getEmail());

        kafkaTemplate.send(message);
    }
}
