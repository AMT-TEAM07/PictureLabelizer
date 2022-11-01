package org.amt.team07.helpers.dataObjects;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class AwsDataObjectHelperImpl {

    private final S3Client s3;
    private final String bucketName;

    public AwsDataObjectHelperImpl(ProfileCredentialsProvider credentialsProvider, String bucketName) {
        this.bucketName = bucketName;
        this.s3 = S3Client.builder()
                .region(Region.EU_WEST_2)
                .credentialsProvider(credentialsProvider)
                .build();
    }

    public void Create(String objectName) {
    }
}
