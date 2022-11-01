package org.amt.team07.helpers.labelDetectors;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;

public class AwsLabelDetectorHelperImpl {

    private final RekognitionClient rekClient;

    public AwsLabelDetectorHelperImpl(ProfileCredentialsProvider credentialsProvider) {
        rekClient = RekognitionClient.builder()
                .region(Region.EU_WEST_2)
                .credentialsProvider(credentialsProvider)
                .build();
    }

    public String Execute(String imageUri, int[] params) {
        return null;
    }
}
