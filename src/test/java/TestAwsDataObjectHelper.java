import io.github.cdimascio.dotenv.Dotenv;
import org.amt.team07.helpers.objects.AwsDataObjectHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class TestAwsDataObjectHelper {

    private AwsDataObjectHelper bucketManager;
    private String bucketName;
    private Path testImagePath;
    private Path downloadedImagePath;
    private String objectName;

    @BeforeEach
    public void setup() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .systemProperties()
                .load();

        AwsBasicCredentials credentials = AwsBasicCredentials
                .create(dotenv.get("AWS_ACCESS_KEY_ID"), dotenv.get("AWS_SECRET_ACCESS_KEY"));
        AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);

        String region = dotenv.get("AWS_DEFAULT_REGION");
        bucketName = dotenv.get("AWS_BUCKET");
        objectName = "test-image.png";
        testImagePath = Paths.get("src", "test", "resources", objectName);
        downloadedImagePath = Paths.get("src", "test", "resources", "downloaded-" + objectName);

        bucketManager = new AwsDataObjectHelper(credentialsProvider, region, bucketName);
    }

    @Test
    void createObject_CreateObjectWithExistingBucket_Success() {
        //given
        assertTrue(bucketManager.existsBucket(bucketName));
        assertFalse(bucketManager.existsObject(objectName));

        //when
        bucketManager.createObject(objectName, testImagePath);

        //then
        assertTrue(bucketManager.existsObject(objectName));
    }

    @Test
    void existsBucket_NominalCase_Success() {
        //given
        String existingBucket = bucketName;
        boolean actualResult;

        //when
        actualResult = bucketManager.existsBucket(existingBucket);

        //then
        assertTrue(actualResult);
    }

    @Test
    void existsBucket_NotExistBucket_Success() {
        //given
        String notExistingBucket = "notExistingBucket-" + bucketName;
        boolean actualResult;

        //when
        actualResult = bucketManager.existsBucket(notExistingBucket);

        //then
        assertFalse(actualResult);
    }

    @Test
    void existsObject_NominalCase_Success() {
        //given
        bucketManager.createObject(objectName, testImagePath);
        boolean actualResult;

        //when
        actualResult = bucketManager.existsObject(objectName);

        //then
        assertTrue(actualResult);
    }

    @Test
    void existsObject_NotExistObject_Success() {
        //given
        String notExistingFileName = "notExistingFile.jpg";
        assertTrue(bucketManager.existsBucket(bucketName));
        boolean actualResult;

        //when
        actualResult = bucketManager.existsObject(notExistingFileName);

        //then
        assertFalse(actualResult);
    }

    @Test
    void removeObject_EmptyBucket_Success() {
        //given
        assertTrue(bucketManager.existsBucket(bucketName));
        assertFalse(bucketManager.existsObject(objectName));

        //when
        bucketManager.removeObject(objectName);

        //then
        assertFalse(bucketManager.existsObject(objectName));
    }

    @Test
    void removeObject_NotEmptyBucket_Success() {
        //given
        assertTrue(bucketManager.existsBucket(bucketName));
        bucketManager.createObject(objectName, testImagePath);
        assertTrue(bucketManager.existsObject(objectName));

        //when
        bucketManager.removeObject(objectName);

        //then
        assertFalse(bucketManager.existsObject(objectName));
    }

    @Test
    void downloadObject_NominalCase_Success() {
        //given
        bucketManager.createObject(objectName, testImagePath);
        assertTrue(bucketManager.existsObject(objectName));
        boolean actualResult;

        //when
        actualResult = bucketManager.downloadObject(objectName, downloadedImagePath);

        //then
        assertTrue(actualResult);
        File file = new File(downloadedImagePath.toUri());
        assertTrue(file.exists());
        assertEquals(file.length(), testImagePath.toFile().length());
    }

    @Test
    void downloadObject_NotExistObject_Success() {
        //given
        assertFalse(bucketManager.existsObject(objectName));
        boolean actualResult;

        //when
        actualResult = bucketManager.downloadObject(objectName, downloadedImagePath);

        //then
        assertFalse(actualResult);
        File file = new File(downloadedImagePath.toUri());
        assertFalse(file.exists());
    }

    @Test
    void getPresignedUrl_NominalCase_Success() {
        //given
        bucketManager.createObject(objectName, testImagePath);
        assertTrue(bucketManager.existsObject(objectName));
        String actualResult;

        //when
        actualResult = bucketManager.getPresignedUrl(objectName);

        //then
        assertNotNull(actualResult);
    }

    @AfterEach
    void tearDown() {
        File file = new File(downloadedImagePath.toUri());
        if (file.exists()) {
            System.out.println("Deleting file => " + file.delete());
        }
        if (bucketManager.existsObject(objectName)) {
            bucketManager.removeObject(objectName);
        }
    }
}