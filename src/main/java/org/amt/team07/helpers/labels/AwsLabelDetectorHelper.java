package org.amt.team07.helpers.labels;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.List;

public class AwsLabelDetectorHelper implements LabelDetectorHelper {

    private final RekognitionClient rekClient;

    public AwsLabelDetectorHelper(AwsCredentialsProvider credentialsProvider, String region) {
        rekClient = RekognitionClient.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider)
                .build();
    }

    public List<LabelWrapper> execute(String imageUri, int nbLabels, double minConfidence) throws IOException {
        checkNbLabelsAndMinConfidence(nbLabels, minConfidence);

        var image = Image.builder()
                .bytes(SdkBytes.fromInputStream(new BufferedInputStream((new URL(imageUri)).openStream())))
                .build();

        List<Label> awsLabels = getLabelsfromImage(image, nbLabels, minConfidence);
        return LabelWrapper.from(awsLabels);
    }

    public List<LabelWrapper> executeB64(String imageB64, int nbLabels, double minConfidence) {
        checkNbLabelsAndMinConfidence(nbLabels, minConfidence);

        var image = Image.builder()
                .bytes(SdkBytes.fromByteBuffer(ByteBuffer.wrap(java.util.Base64.getDecoder().decode(imageB64))))
                .build();

        List<Label> awsLabels = getLabelsfromImage(image, nbLabels, minConfidence);
        return LabelWrapper.from(awsLabels);
    }

    private void checkNbLabelsAndMinConfidence(int nbLabels, double minConfidence) throws InvalidParameterException {
        if (nbLabels < 1) {
            throw new InvalidParameterException("nbLabels must be at least 1");
        }
        if (minConfidence < 0 || minConfidence > 100) {
            throw new InvalidParameterException("minConfidence must be between 0 and 100");
        }
    }

    private List<Label> getLabelsfromImage(Image myImage, int nbLabels, double minConfidence) throws RekognitionException {
        try {

            DetectLabelsRequest detectLabelsRequest = DetectLabelsRequest.builder()
                    .image(myImage)
                    .maxLabels(nbLabels)
                    .minConfidence((float) minConfidence)
                    .build();

            DetectLabelsResponse labelsResponse = rekClient.detectLabels(detectLabelsRequest);
            return labelsResponse.labels();
        } catch (RekognitionException e) {
            throw RekognitionException.builder().message("Error while detecting labels").build();
        }
    }
}
