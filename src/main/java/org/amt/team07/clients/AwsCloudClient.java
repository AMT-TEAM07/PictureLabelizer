package org.amt.team07.clients;

import io.github.cdimascio.dotenv.Dotenv;
import org.amt.team07.helpers.labels.AwsLabelDetectorHelper;
import org.amt.team07.helpers.objects.AwsDataObjectHelper;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AwsCloudClient implements CloudClient {

    private static final Logger LOG = Logger.getLogger(AwsCloudClient.class.getName());

    private static AwsCloudClient instance;

    private final AwsDataObjectHelper dataObjectHelper;

    private final AwsLabelDetectorHelper labelDetectorHelper;

    private AwsCloudClient() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .systemProperties()
                .load();
        AwsBasicCredentials credentials = AwsBasicCredentials.create(dotenv.get("AWS_ACCESS_KEY_ID"),
                dotenv.get("AWS_SECRET_ACCESS_KEY"));
        AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);
        dataObjectHelper = new AwsDataObjectHelper(credentialsProvider, dotenv.get("AWS_DEFAULT_REGION"),
                dotenv.get("AWS_BUCKET"));
        labelDetectorHelper = new AwsLabelDetectorHelper(credentialsProvider, dotenv.get("AWS_DEFAULT_REGION"));
    }

    public void analyzeFromPath(Path path, String objectName, int nbLabels, double minConfidence) throws IOException {
        dataObjectHelper.createObject(objectName, path);
        String url = dataObjectHelper.getPresignedUrl(objectName);
        analyzeFromURL(url, objectName, nbLabels, minConfidence);
    }

    /**
     * Get a list of labels from a URL of a picture
     *
     * @param imageURL      the image to analyze
     * @param objectName    is the name of the file
     * @param nbLabels      the maximum number of labels to return
     * @param minConfidence the minimum confidence for a label to be returned
     */
    public void analyzeFromURL(String imageURL, String objectName, int nbLabels, double minConfidence) throws IOException {
        //Création de la string json
        StringBuilder json = new StringBuilder("{labels:[");
        String suffix = "";
        for (var label : labelDetectorHelper.execute(imageURL, nbLabels, minConfidence)) {
            json.append(suffix);
            json.append(label);
            suffix = ",";
        }
        json.append("]}");

        //Ecriture du json dans un fichier
        String name = objectName + ".json";

        Path path = Paths.get(name);
        Files.writeString(path, json.toString(), StandardCharsets.UTF_8);
        dataObjectHelper.createObject(name, path);
        Files.delete(path);
    }

    /**
     * Get a list of labels from an image in base 64
     *
     * @param imageB64      the image to analyze
     * @param nbLabels      the maximum number of labels to return
     * @param minConfidence the minimum confidence for a label to be returned
     */
    public void analyzeFromBase64(String imageB64, int nbLabels, double minConfidence) {
        LOG.log(Level.INFO, "{0}", "Analyse en mode base 64 terminée. Voici les résultats : " + labelDetectorHelper.executeB64(imageB64, nbLabels, minConfidence));
    }

    public static AwsCloudClient getInstance() {
        if (instance == null) {
            instance = new AwsCloudClient();
        }
        return instance;
    }
}
