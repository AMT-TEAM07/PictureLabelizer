package org.amt.team07.helpers.labelDetectors;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;

public class AwsLabelDetectorHelper {

    private final RekognitionClient rekClient;

    public AwsLabelDetectorHelper(AwsCredentialsProvider credentialsProvider) {
        rekClient = RekognitionClient.builder()
                .region(Region.EU_WEST_2)
                .credentialsProvider(credentialsProvider)
                .build();
    }

    public String Execute(String imageUri, int[] params) {
        return null;
    }
}
