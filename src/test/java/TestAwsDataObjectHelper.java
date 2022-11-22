import io.github.cdimascio.dotenv.Dotenv;
import org.amt.team07.Main;
import org.amt.team07.helpers.objects.AwsDataObjectHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//TODO REVIEW Remove all aws dependencies in test class
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class TestAwsDataObjectHelper {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());
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

        //TODO REVIEW Improvement, would be appreciate to have a config file for testing and another one for prod
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
    void canCreateObjectInExistingBucket() {
        //given
        assertTrue(bucketManager.existsRootObject(bucketName));
        assertFalse(bucketManager.existsObject(objectName));

        //when
        bucketManager.createObject(objectName, testImagePath);

        //then
        assertTrue(bucketManager.existsObject(objectName));
    }

    @Test
    void canConfirmBucketExists() {
        //given
        String existingBucket = bucketName;
        boolean actualResult;

        //when
        actualResult = bucketManager.existsRootObject(existingBucket);

        //then
        assertTrue(actualResult);
    }

    @Test
    void canConfirmBucketDoesNotExist() {
        //given
        String notExistingBucket = "notExistingBucket-" + bucketName;
        boolean actualResult;

        //when
        actualResult = bucketManager.existsRootObject(notExistingBucket);

        //then
        assertFalse(actualResult);
    }

    @Test
    void canConfirmObjectExists() {
        //given
        bucketManager.createObject(objectName, testImagePath);
        boolean actualResult;

        //when
        actualResult = bucketManager.existsObject(objectName);

        //then
        assertTrue(actualResult);
    }

    @Test
    void canConfirmObjectDoesNotExist() {
        //given
        String notExistingFileName = "notExistingFile.jpg";
        assertTrue(bucketManager.existsRootObject(bucketName));
        boolean actualResult;

        //when
        actualResult = bucketManager.existsObject(notExistingFileName);

        //then
        assertFalse(actualResult);
    }

    @Test
    void canRemoveObjectFromNotEmptyBucket() {
        //given
        assertTrue(bucketManager.existsRootObject(bucketName));
        bucketManager.createObject(objectName, testImagePath);
        assertTrue(bucketManager.existsObject(objectName));

        //when
        bucketManager.removeObject(objectName);

        //then
        assertFalse(bucketManager.existsObject(objectName));
    }

    @Test
    void canDownloadExistingObject() {
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
    void returnFalseWhenDownloadingNotExistingObject() {
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
    void canGeneratePresignedPublicUrlForGivenObjectName() {
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
            LOG.log(Level.INFO, "{0}", "Deleting file => " + file.delete());
        }
        if (bucketManager.existsObject(objectName)) {
            bucketManager.removeObject(objectName);
        }
    }
}