package org.amt.team07.clients;

import io.github.cdimascio.dotenv.Dotenv;
import org.amt.team07.helpers.dataObjects.AwsDataObjectHelper;
import org.amt.team07.helpers.labelDetectors.AwsLabelDetectorHelper;
import software.amazon.awssdk.auth.credentials.*;

public class AwsCloudClient {
    private static AwsCloudClient instance;

    private AwsCredentialsProvider credentialsProvider;

    private AwsDataObjectHelper dataObjectHelper;

    private AwsLabelDetectorHelper labelDetectorHelper;

    private AwsCloudClient() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .systemProperties()
                .load();
        AwsBasicCredentials credentials = AwsBasicCredentials.create(dotenv.get("AWS_ACCESS_KEY_ID"),
                dotenv.get("AWS_SECRET_ACCESS_KEY"));
        credentialsProvider = StaticCredentialsProvider.create(credentials);
        dataObjectHelper = new AwsDataObjectHelper(credentialsProvider, dotenv.get("AWS_DEFAULT_REGION"),
                dotenv.get("AWS_BUCKET"));
        labelDetectorHelper = new AwsLabelDetectorHelper(credentialsProvider);
    }

    public static AwsCloudClient getInstance() {
        if (instance == null) {
            instance = new AwsCloudClient();
        }
        return instance;
    }
}
