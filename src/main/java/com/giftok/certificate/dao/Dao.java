package com.giftok.certificate.dao;

import com.giftok.certificate.PropertiesHolder;
import com.giftok.certificate.annotation.Collection;
import com.giftok.certificate.dto.Dto;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.concurrent.ExecutionException;

public abstract class Dao<E extends Dto> {
    private String collectionName;

    public Dao() throws IOException {
        initCollectionName();
        String projectId = PropertiesHolder.getProperty("google.app.engine.project-id");
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build();
        FirebaseApp.initializeApp(options);
    }

    public void insert(E element) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(collectionName).document(element.getId());
        docRef.set(element).get();
    }

    public void delete(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(collectionName).document(id);
        docRef.delete().get();
    }

    private void initCollectionName() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Class<?> parameter = (Class<?>)type.getActualTypeArguments()[0];
        if (parameter.isAnnotationPresent(Collection.class)) {
            Collection collection = parameter.getAnnotation(Collection.class);
            this.collectionName = collection.value();
        }
    }

}
