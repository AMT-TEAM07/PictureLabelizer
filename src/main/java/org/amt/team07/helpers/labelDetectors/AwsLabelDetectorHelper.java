package org.amt.team07.helpers.labelDetectors;

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

public class AwsLabelDetectorHelper {

    private final RekognitionClient rekClient;

    /**
     * Constructor for the AWS label detector helper
     * @param credentialsProvider the credentials provider to use
     */
    public AwsLabelDetectorHelper(ProfileCredentialsProvider credentialsProvider) {
        rekClient = RekognitionClient.builder()
                .region(Region.EU_WEST_2)
                .credentialsProvider(credentialsProvider)
                .build();
    }

    /**
     * Detects labels in an image stored in an S3 bucket.
     * @param imageUri url on the image
     * @param nbLabels maximum number of labels to return
     * @param minConfidence minimum confidence for a label to be returned
     * @return a list of labelWrapper
     * @throws RuntimeException if the image cannot be read
     */
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

    /**
     * Detects labels in an image in base 64.
     * @param imageB64 base 64 encoded image
     * @param nbLabels maximum number of labels to return
     * @param minConfidence minimum confidence for a label to be returned
     * @return a list of LabelWrapper
     */
    public List<LabelWrapper> executeB64(String imageB64, int nbLabels, double minConfidence) {
        checkNbLabelsAndMinConfidence(nbLabels, minConfidence);

        Image myImage = Image.builder()
                .bytes(SdkBytes.fromByteBuffer(ByteBuffer.wrap(java.util.Base64.getDecoder().decode(imageB64))))
                .build();

        List<Label> awsLabels = getLabelsfromImage(myImage, nbLabels, minConfidence);
        return LabelWrapper.from(awsLabels);
    }

    /**
     * Checks that the number of labels and the minimum confidence are valid.
     * @param nbLabels maximum number of labels
     * @param minConfidence minimum confidence for a label
     * @throws InvalidParameterException if the number of labels is negative or the minimum confidence is not between 0 and 100
     */
    private void checkNbLabelsAndMinConfidence(int nbLabels, double minConfidence) throws InvalidParameterException {
        if (nbLabels < 1) {
            throw new InvalidParameterException("nbLabels must be at least 1");
        }
        if (minConfidence < 0 || minConfidence > 100) {
            throw new InvalidParameterException("minConfidence must be between 0 and 100");
        }
    }


    /**
     * Detects labels in an image.
     * @param myImage the image
     * @param nbLabels maximum number of labels to return
     * @param minConfidence minimum confidence for a label to be returned
     * @return a list of AWS labels
     * @throws RekognitionException if the image cannot be read
     */
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
