package com.giftok.certificate.topic;

import com.giftok.certificate.PropertiesHolder;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;

public class CertificateCreationSubscriber {
    private static final String PROJECT = PropertiesHolder.getProperty("google.app.engine.project-id");
    private static final String SUBSCRIPTION = PropertiesHolder.getProperty("google.app.engine.subscription.certificate-creation.id");

    public void await(MessageReceiver receiver) {
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(PROJECT, SUBSCRIPTION);
        try {
            // create a subscriber bound to the asynchronous message receiver
            Subscriber subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();
            subscriber.startAsync().awaitRunning();
            // Allow the subscriber to run indefinitely unless an unrecoverable error occurs.
            subscriber.awaitTerminated();
        } catch (IllegalStateException e) {
            System.out.println("Subscriber unexpectedly stopped: " + e);
        }
    }
}