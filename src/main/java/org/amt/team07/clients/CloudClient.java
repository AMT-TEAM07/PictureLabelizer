package org.amt.team07.clients;

import java.io.IOException;
import java.nio.file.Path;

public interface CloudClient {
    void analyzeFromPath(Path path, String objectName, int nbLabels, double minConfidence) throws IOException;

    void analyzeFromURL(String imageURL, String objectName, int nbLabels, double minConfidence) throws IOException;

    void analyzeFromBase64(String imageB64, int nbLabels, double minConfidence);
}
