package org.amt.team07.helpers.labelDetectors;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import java.util.List;

public class AwsLabelDetectorHelperImpl {

    private final RekognitionClient rekClient;

    public AwsLabelDetectorHelperImpl(ProfileCredentialsProvider credentialsProvider) {
        rekClient = RekognitionClient.builder()
                .region(Region.EU_WEST_2)
                .credentialsProvider(credentialsProvider)
                .build();
    }

    public String Execute(String imageUri, int[] params) {
        List<Label> list = getLabelsfromImage("f", imageUri);
    }

    private List<Label> getLabelsfromImage(String bucket, String image) {
        try {
            S3Object s3Object = S3Object.builder()
                    .bucket(bucket)
                    .name(image)
                    .build() ;

            Image myImage = Image.builder()
                    .s3Object(s3Object)
                    .build();

            DetectLabelsRequest detectLabelsRequest = DetectLabelsRequest.builder()
                    .image(myImage)
                    .maxLabels(10)
                    .build();

            DetectLabelsResponse labelsResponse = rekClient.detectLabels(detectLabelsRequest);
            return labelsResponse.labels();
            /*System.out.println("Detected labels for the given photo");
            for (Label label: labels) {
                System.out.println(label.name() + ": " + label.confidence().toString());
            }*/

        } catch (RekognitionException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }
}
