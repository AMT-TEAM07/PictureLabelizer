package org.amt.team07.helpers.labels;

import java.io.IOException;
import java.util.List;

public interface LabelDetectorHelper {
    List<LabelWrapper> execute(String imageUri, int nbLabels, double minConfidence) throws IOException;

    List<LabelWrapper> executeB64(String imageB64, int nbLabels, double minConfidence);
}
