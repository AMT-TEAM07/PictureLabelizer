package org.amt.team07.clients;

import io.github.cdimascio.dotenv.Dotenv;
import org.amt.team07.helpers.objects.AwsDataObjectHelper;
import org.amt.team07.helpers.labels.AwsLabelDetectorHelper;
import software.amazon.awssdk.auth.credentials.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AwsCloudClient {
    private static AwsCloudClient instance;

    private AwsCredentialsProvider credentialsProvider;

    private AwsDataObjectHelper dataObjectHelper;

    private AwsLabelDetectorHelper labelDetectorHelper;

    private AwsCloudClient() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .systemProperties()
                .load();
        AwsBasicCredentials credentials = AwsBasicCredentials.create(dotenv.get("AWS_ACCESS_KEY_ID"),
                dotenv.get("AWS_SECRET_ACCESS_KEY"));
        credentialsProvider = StaticCredentialsProvider.create(credentials);
        dataObjectHelper = new AwsDataObjectHelper(credentialsProvider, dotenv.get("AWS_DEFAULT_REGION"),
                dotenv.get("AWS_BUCKET"));
        labelDetectorHelper = new AwsLabelDetectorHelper(credentialsProvider, dotenv.get("AWS_DEFAULT_REGION"));
    }

    public void analyzeFromPath(String objectName, int nbLabels, double minConfidence, Path path) throws IOException {
        dataObjectHelper.createObject(objectName, path);
        String url = dataObjectHelper.getPresignedUrl(objectName);
        analyzeFromURL(url, objectName, nbLabels, minConfidence);
    }

    /**
     * Get a list of labels from a URL of a picture
     * @param image the image to analyze
     * @param myName is the name of the file
     * @param nbLabels the maximum number of labels to return
     * @param minConfidence the minimum confidence for a label to be returned
     */
    public void analyzeFromURL(String image, String myName, int nbLabels, double minConfidence) throws IOException {
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

        Path path = Paths.get(name);
        Files.writeString(path, json.toString(), StandardCharsets.UTF_8);
        dataObjectHelper.createObject(name, path);
        Files.delete(path);
    }

    /**
     * Get a list of labels from an image in base 64
     * @param image the image to analyze
     * @param nbLabels the maximum number of labels to return
     * @param minConfidence the minimum confidence for a label to be returned
     */
    public void analyzeFromBase64(String image, int nbLabels, double minConfidence) {
        System.out.println(labelDetectorHelper.executeB64(image, nbLabels, minConfidence));
    }

    public static AwsCloudClient getInstance() {
        if (instance == null) {
            instance = new AwsCloudClient();
        }
        return instance;
    }
}
