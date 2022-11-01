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

    public AwsDataObjectHelperImpl(String bucketUrl) {
    }

    public AwsDataObjectHelperImpl() {
    }

    public void Create(String objectname) {
    }

    public void Create(String objectname, String path) {
    }

    public boolean Exists(String objectname) {
        return false;
    }

    public void DownloadObject(String objectUrl, String destinationFullPath) {
    }

    public void RemoveObject(String bucketUrl) {
    }
}
