package com.giftok.certificate.topic;

import com.giftok.certificate.PropertiesHolder;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;

import java.util.concurrent.TimeUnit;

public class CertificateCreatedPublisher {
    private static final String PROJECT = PropertiesHolder.getProperty("google.app.engine.project-id");
    private static final String TOPIC = PropertiesHolder.getProperty("google.app.engine.topic.certificate-created.id");

    public void publishMessage(ByteString data) throws Exception {
        ProjectTopicName topicName = ProjectTopicName.of(PROJECT, TOPIC);
        Publisher publisher = null;
        ApiFuture<String> future = null;
        try {
            // Create a publisher instance with default settings bound to the topic
            publisher = Publisher.newBuilder(topicName).build();

            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

            // Once published, returns a server-assigned message id (unique within the topic)
            future = publisher.publish(pubsubMessage);

            // Add an asynchronous callback to handle success / failure
            ApiFutures.addCallback(
                    future,
                    new ApiFutureCallback<>() {

                        @Override
                        public void onFailure(Throwable throwable) {
                            if (throwable instanceof ApiException) {
                                ApiException apiException = ((ApiException) throwable);
                                // details on the API exception
                                System.out.println(apiException.getStatusCode().getCode());
                                System.out.println(apiException.isRetryable());
                            }
                            System.out.println("Error publishing message : " + data);
                        }

                        @Override
                        public void onSuccess(String messageId) {
                            // Once published, returns server-assigned message ids (unique within the topic)
                            System.out.println(messageId);
                        }
                    },
                    MoreExecutors.directExecutor());

        } finally {
            future.get();
            if (publisher != null) {
                // When finished with the publisher, shutdown to free up resources.
                publisher.shutdown();
                publisher.awaitTermination(1, TimeUnit.MINUTES);
            }
        }
    }

}
