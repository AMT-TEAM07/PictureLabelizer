package org.amt.team07.clients;

import io.github.cdimascio.dotenv.Dotenv;
import org.amt.team07.helpers.dataObjects.AwsDataObjectHelperImpl;
import org.amt.team07.helpers.labelDetectors.AwsLabelDetectorHelperImpl;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;

public class AwsCloudClient {
    private static AwsCloudClient instance;

    private ProfileCredentialsProvider credentialsProvider;

    private AwsDataObjectHelperImpl dataObjectHelper;

    private AwsLabelDetectorHelperImpl labelDetectorHelper;

    private AwsCloudClient() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();
        credentialsProvider = ProfileCredentialsProvider.create(dotenv.get("AWS_PROFILE"));
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
