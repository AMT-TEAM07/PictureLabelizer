package org.amt.team07.clients;

import io.github.cdimascio.dotenv.Dotenv;
import org.amt.team07.helpers.dataObjects.AwsDataObjectHelperImpl;
import org.amt.team07.helpers.labelDetectors.AwsLabelDetectorHelperImpl;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;

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
     * @param nbLabels the maximum number of labels to return
     * @param minConfidence the minimum confidence for a label to be returned
     */
    public void rekognitionFromURL(String image, int nbLabels, double minConfidence) {
        System.out.println(labelDetectorHelper.execute(image, nbLabels, minConfidence));
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
