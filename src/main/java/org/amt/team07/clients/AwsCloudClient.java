package org.amt.team07.clients;

import io.github.cdimascio.dotenv.Dotenv;
import org.amt.team07.helpers.dataObjects.AwsDataObjectHelperImpl;
import org.amt.team07.helpers.labelDetectors.AwsLabelDetectorHelperImpl;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AwsCloudClient {
    private static AwsCloudClient instance;

    private ProfileCredentialsProvider credentialsProvider;

    private AwsDataObjectHelperImpl dataObjectHelper;

    private AwsLabelDetectorHelperImpl labelDetectorHelper;

    private AwsCloudClient() {
        Dotenv dotenv = Dotenv.load();
        credentialsProvider = ProfileCredentialsProvider.create(dotenv.get("AWS_PROFILE"));
        dataObjectHelper = new AwsDataObjectHelperImpl(credentialsProvider, dotenv.get("AWS_BUCKET"));
        labelDetectorHelper = new AwsLabelDetectorHelperImpl(credentialsProvider);
    }

    /**
     * Get a list of labels from a URL of a picture
     * @param image the image to analyze
     * @param myName is the name of the file
     * @param nbLabels the maximum number of labels to return
     * @param minConfidence the minimum confidence for a label to be returned
     */
    public void rekognitionFromURL(String image, String myName, int nbLabels, double minConfidence) {
        //Création de la string json
        StringBuilder json = new StringBuilder("{labels:[");
        String suffix = "";
        for (var label : labelDetectorHelper.execute(image, nbLabels, minConfidence)) {
            json.append(suffix);
            json.append(label);
            suffix = ",";
        }
        json.append("]}");

        //Ecriture du json dans un fichier
        String name = myName + ".json";
        try {
            Path file = Paths.get(name);
            Files.writeString(file, json.toString(), StandardCharsets.UTF_8);
            dataObjectHelper.createObject(name, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a list of labels from an image in base 64
     * @param image the image to analyze
     * @param nbLabels the maximum number of labels to return
     * @param minConfidence the minimum confidence for a label to be returned
     */
    public void rekognitionFromBase64(String image, int nbLabels, double minConfidence) {
        System.out.println(labelDetectorHelper.executeB64(image, nbLabels, minConfidence));
    }

    public static AwsCloudClient getInstance() {
        if (instance == null) {
            instance = new AwsCloudClient();
        }
        return instance;
    }
}
