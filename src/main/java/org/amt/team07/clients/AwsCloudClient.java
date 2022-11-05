package org.amt.team07.clients;

import io.github.cdimascio.dotenv.Dotenv;
import org.amt.team07.helpers.dataObjects.AwsDataObjectHelperImpl;
import org.amt.team07.helpers.labelDetectors.AwsLabelDetectorHelperImpl;
import software.amazon.awssdk.auth.credentials.*;

public class AwsCloudClient {
    private static AwsCloudClient instance;

    private AwsCredentialsProvider credentialsProvider;

    private AwsDataObjectHelperImpl dataObjectHelper;

    private AwsLabelDetectorHelperImpl labelDetectorHelper;

    private AwsCloudClient() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .systemProperties()
                .load();
        AwsBasicCredentials credentials = AwsBasicCredentials.create(dotenv.get("AWS_ACCESS_KEY_ID"), dotenv.get("AWS_SECRET_ACCESS_KEY"));
        credentialsProvider = StaticCredentialsProvider.create(credentials);
        dataObjectHelper = new AwsDataObjectHelperImpl(credentialsProvider, dotenv.get("AWS_BUCKET"));
        labelDetectorHelper = new AwsLabelDetectorHelperImpl(credentialsProvider);
    }

    public static AwsCloudClient getInstance() {
        if (instance == null) {
            instance = new AwsCloudClient();
        }
        return instance;
    }
}
