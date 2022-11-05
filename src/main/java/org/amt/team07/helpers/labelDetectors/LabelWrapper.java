package org.amt.team07.helpers.labelDetectors;

import software.amazon.awssdk.services.rekognition.model.Label;

import java.util.ArrayList;
import java.util.List;

public class LabelWrapper {
    private String name;
    private double confidence;

    public LabelWrapper(String name, double confidence) {
        this.name = name;
        this.confidence = confidence;
    }

    public String getName() {
        return name;
    }

    public double getConfidence() {
        return confidence;
    }

    public String toString() {
        return "\"" + name + "\" is detected at " + confidence + "%";
    }

    public static ArrayList<LabelWrapper> from(List<Label> awsLabels){
        ArrayList<LabelWrapper> labels = new ArrayList<>();
        for (Label label : awsLabels) {
            labels.add(new LabelWrapper(label.name(), label.confidence()));
        }
        return labels;
    }
}
