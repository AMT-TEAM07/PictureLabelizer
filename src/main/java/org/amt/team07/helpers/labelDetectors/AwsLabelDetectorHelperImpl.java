package org.amt.team07.helpers.labelDetectors;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
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

    public List<LabelWrapper> execute(String imageUri, int nbLabels, double minConfidence) throws RuntimeException {
        checkNbLabelsAndMinConfidence(nbLabels, minConfidence);

        Image myImage;
        try {
            myImage = Image.builder()
                    .bytes(SdkBytes.fromInputStream(new BufferedInputStream((new URL(imageUri)).openStream())))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Label> awsLabels = getLabelsfromImage(myImage, nbLabels, minConfidence);
        return LabelWrapper.from(awsLabels);
    }

    public List<LabelWrapper> executeB64(String imageB64, int nbLabels, double minConfidence) {
        checkNbLabelsAndMinConfidence(nbLabels, minConfidence);

        Image myImage = Image.builder()
                .bytes(SdkBytes.fromByteBuffer(ByteBuffer.wrap(java.util.Base64.getDecoder().decode(imageB64))))
                .build();

        List<Label> awsLabels = getLabelsfromImage(myImage, nbLabels, minConfidence);
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

    private List<Label> getLabelsfromImage(Image myImage, int nbLabels, double minConfidence) throws RekognitionException{
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
