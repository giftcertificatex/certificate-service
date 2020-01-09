package com.giftok.certificate;

import com.giftok.certificate.topic.CertificateCreatedPublisher;
import com.giftok.certificate.topic.CertificateCreationSubscriber;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.protobuf.ByteString;
import com.giftok.certificate.message.CertificateMessageOuterClass.CertificateMessage;

public class Application {
    public static void main(String[] args) {
        CertificateCreatedPublisher publisher = new CertificateCreatedPublisher();

        MessageReceiver receiver = (message, consumer) -> {
            String messageId = message.getMessageId();
            ByteString messageData = message.getData();
            System.out.println("Message Id: " + messageId);
            try {
                ByteString data = CertificateMessage.newBuilder()
                        .mergeFrom(messageData)
                        .setId(messageId)
                        .build()
                        .toByteString();

                publisher.publishMessage(data);
                // Ack only after all work for the message is complete.
                consumer.ack();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        CertificateCreationSubscriber subscriber = new CertificateCreationSubscriber();
        subscriber.await(receiver);

    }
}
