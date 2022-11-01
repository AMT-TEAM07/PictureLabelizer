package org.amt.team07.helpers.dataObjects;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.nio.file.Path;

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

    public void CreateObject(String objectName, Path filePath) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(this.bucketName)
                .key(objectName)
                .build();

        s3.putObject(objectRequest, RequestBody.fromFile(filePath));
    }

    public boolean ExistsBucket(String bucketName) {
        HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                .bucket(bucketName)
                .build();
        try {
            s3.headBucket(headBucketRequest);
            return true;
        } catch (NoSuchBucketException e) {
            return false;
        }
    }

    public boolean ExistsObject(String objectName) {
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .build();
        try {
            s3.headObject(headObjectRequest);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    public void DownloadObject(String objectUrl, String destinationFullPath) {
        // TODO
    }

    public void RemoveObject(String objectName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(this.bucketName)
                .key(objectName)
                .build();
        s3.deleteObject(deleteObjectRequest);
    }
}
