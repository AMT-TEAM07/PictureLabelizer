package org.amt.team07.helpers.labelDetectors;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.List;

public class AwsLabelDetectorHelperImpl {

    private final RekognitionClient rekClient;

    public AwsLabelDetectorHelperImpl(ProfileCredentialsProvider credentialsProvider) {
        rekClient = RekognitionClient.builder()
                .region(Region.EU_WEST_2)
                .credentialsProvider(credentialsProvider)
                .build();
    }

    public List<LabelWrapper> Execute(String imageUri, int nbLabels, double minConfidence) {
        List<Label> awsLabels = getLabelsfromImage(imageUri, nbLabels, minConfidence);
        return LabelWrapper.from(awsLabels);
    }

    private List<Label> getLabelsfromImage(String image, int nbLabels, double minConfidence) {
        try {
            Image myImage = Image.builder()
                    .bytes(SdkBytes.fromInputStream(new BufferedInputStream((new URL(image)).openStream())))
                    .build();

            DetectLabelsRequest detectLabelsRequest = DetectLabelsRequest.builder()
                    .image(myImage)
                    .maxLabels(nbLabels)
                    .minConfidence((float) minConfidence)
                    .build();

            DetectLabelsResponse labelsResponse = rekClient.detectLabels(detectLabelsRequest);
            return labelsResponse.labels();
        } catch (RekognitionException e) {
            throw RekognitionException.builder().message("Error while detecting labels").build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
