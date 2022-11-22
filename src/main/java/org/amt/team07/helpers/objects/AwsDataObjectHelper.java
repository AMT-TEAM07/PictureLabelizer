package org.amt.team07.helpers.objects;

import org.amt.team07.Main;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AwsDataObjectHelper implements DataObjectHelper {

    private final AwsCredentialsProvider credentialsProvider;
    private static final Logger LOG = Logger.getLogger(Main.class.getName());
    private final S3Client s3;
    private final String region;
    private final String bucketName;

    public AwsDataObjectHelper(AwsCredentialsProvider credentialsProvider, String region, String bucketName) {
        this.credentialsProvider = credentialsProvider;
        this.bucketName = bucketName;
        this.region = region;
        s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider)
                .build();
    }

    public boolean existsRootObject(String rootObjectName) {
        HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                .bucket(rootObjectName)
                .build();
        try {
            s3.headBucket(headBucketRequest);
            return true;
        } catch (NoSuchBucketException e) {
            //TODO REVIEW Either catching, or returning, but not this way
            LOG.log(Level.INFO, "{0}", e.getMessage());
            return false;
        }
    }

    //TODO REVIEW Remove all bucket mention from you public method. Everything is an object.
    public void createRootObject(String rootObjectName) {
        if (!existsRootObject(rootObjectName)) {
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(rootObjectName)
                    .build();
            s3.createBucket(createBucketRequest);
        }
    }

    public void removeRootObject(String rootObjectName) {
        if (existsRootObject(rootObjectName)) {
            DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder()
                    .bucket(rootObjectName)
                    .build();
            s3.deleteBucket(deleteBucketRequest);
        }
    }

    public void createObject(String objectName, Path filePath) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .build();
        s3.putObject(objectRequest, RequestBody.fromFile(filePath));
    }

    public boolean existsObject(String objectName) {
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .build();
        try {
            s3.headObject(headObjectRequest);
            return true;
        } catch (NoSuchKeyException e) {
            LOG.log(Level.INFO, "{0}", e.getMessage());
            return false;
        }
    }

    public void removeObject(String objectName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .build();
        s3.deleteObject(deleteObjectRequest);
    }

    public boolean downloadObject(String objectUrl, Path downloadedImagePath) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectUrl)
                .build();
        try {
            s3.getObject(getObjectRequest, downloadedImagePath);
            return true;
        } catch (NoSuchKeyException e) {
            LOG.log(Level.INFO, "{0}", e.getMessage());
            return false;
        }
    }

    public String getPresignedUrl(String objectName) {
        try (S3Presigner presigner = S3Presigner.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.of(region))
                .build()) {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectName)
                    .build();
            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(java.time.Duration.ofMinutes(2))
                    .getObjectRequest(getObjectRequest)
                    .build();
            return presigner.presignGetObject(getObjectPresignRequest).url().toString();
        }
    }
}
