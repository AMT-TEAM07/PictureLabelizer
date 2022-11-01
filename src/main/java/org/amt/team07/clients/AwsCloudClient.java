package org.amt.team07.clients;

import org.amt.team07.helpers.dataObjects.AwsDataObjectHelperImpl;
import org.amt.team07.helpers.labelDetectors.AwsLabelDetectorHelperImpl;

public class AwsCloudClient {
    private static AwsCloudClient instance;

    private AwsDataObjectHelperImpl dataObjectHelper;

    private AwsLabelDetectorHelperImpl labelDetectorHelper;

    private AwsCloudClient() {
        dataObjectHelper = new AwsDataObjectHelperImpl();
        labelDetectorHelper = new AwsLabelDetectorHelperImpl();
    }

    public static AwsCloudClient getInstance() {
        if (instance == null) {
            instance = new AwsCloudClient();
        }
        return instance;
    }
}
