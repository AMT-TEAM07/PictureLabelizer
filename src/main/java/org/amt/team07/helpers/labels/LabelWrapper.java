package org.amt.team07.helpers.labels;

import software.amazon.awssdk.services.rekognition.model.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper for the label to make it easier to change the cloud providing the rekognition service
 */
public class LabelWrapper {
    private String name;
    private double confidence;

    /**
     * Constructor for the label wrapper
     * @param name the name of the label
     * @param confidence the confidence of the label
     */
    public LabelWrapper(String name, double confidence) {
        this.name = name;
        this.confidence = confidence;
    }

    /**
     * Getter for the name
     * @return the name of the label
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the confidence
     * @return the confidence of the label
     */
    public double getConfidence() {
        return confidence;
    }

    /**
     * Show the content of the label
     * @return the content of the label as a string
     */
    public String toString() {
        return "{Label: " + name + ", Confidence: " + confidence +"}";
    }

    /**
     * Converts a list of AWS labels to a list of label wrappers
     * @param awsLabels the list of AWS labels
     * @return a list of label wrappers corresponding to the AWS labels
     */
    public static ArrayList<LabelWrapper> from(List<Label> awsLabels){
        ArrayList<LabelWrapper> labels = new ArrayList<>();
        for (Label label : awsLabels) {
            labels.add(new LabelWrapper(label.name(), label.confidence()));
        }
        return labels;
    }
}
