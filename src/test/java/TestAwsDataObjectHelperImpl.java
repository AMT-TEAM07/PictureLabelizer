import io.github.cdimascio.dotenv.Dotenv;
import org.amt.team07.helpers.dataObjects.AwsDataObjectHelperImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class TestAwsDataObjectHelperImpl {

    private AwsDataObjectHelperImpl bucketManager;
    private ProfileCredentialsProvider credentialsProvider;
    private String bucketName;
    private Path testImagePath;
    private String objectName;

    @BeforeEach
    public void Init() {
        Dotenv dotenv = Dotenv.load();

        bucketName = dotenv.get("AWS_BUCKET");
        objectName = "test-image.png";
        testImagePath = Paths.get("src", "test", "resources", objectName);

        credentialsProvider = ProfileCredentialsProvider.create(dotenv.get("AWS_PROFILE"));
        bucketManager = new AwsDataObjectHelperImpl(credentialsProvider, bucketName);
    }

    @Test
    void CreateObject_CreateObjectWithExistingBucket_Success() {
        //given
        assertTrue(bucketManager.existsBucket(bucketName));
        assertFalse(bucketManager.existsObject(objectName));

        //when
        bucketManager.createObject(objectName, testImagePath);

        //then
        assertTrue(bucketManager.existsObject(objectName));
    }

    @Test
    void ExistsBucket_NominalCase_Success() {
        //given
        String existingBucket = bucketName;
        boolean actualResult;

        //when
        actualResult = bucketManager.existsBucket(existingBucket);

        //then
        assertTrue(actualResult);
    }

    @Test
    void ExistsBucket_NotExistBucket_Success() {
        //given
        String notExistingBucket = "notExistingBucket-" + bucketName;
        boolean actualResult;

        //when
        actualResult = bucketManager.existsBucket(notExistingBucket);

        //then
        assertFalse(actualResult);
    }

    @Test
    void ExistsObject_NominalCase_Success() {
        //given
        bucketManager.createObject(objectName, testImagePath);
        boolean actualResult;

        //when
        actualResult = bucketManager.existsObject(objectName);

        //then
        assertTrue(actualResult);
    }

    @Test
    void ExistsObject_NotExistObject_Success() {
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
    void RemoveObject_EmptyBucket_Success() {
        //given
        assertTrue(bucketManager.existsBucket(bucketName));
        assertFalse(bucketManager.existsObject(objectName));

        //when
        bucketManager.removeObject(objectName);

        //then
        assertFalse(bucketManager.existsObject(objectName));
    }

    @Test
    void RemoveObject_NotEmptyBucket_Success() {
        //given
        assertTrue(bucketManager.existsBucket(bucketName));
        bucketManager.createObject(objectName, testImagePath);
        assertTrue(bucketManager.existsObject(objectName));

        //when
        bucketManager.removeObject(objectName);

        //then
        assertFalse(bucketManager.existsObject(objectName));
    }

//    @Test
//    void DownloadObject_NominalCase_Success() {
//        //given
//        bucketManager.CreateObject(objectName, testImagePath);
//
//        assertTrue(bucketManager.ExistsObject(objectName));
//
//        //when
//        bucketManager.DownloadObject(objectName, destinationFullPath);
//
//        //then
//        File file = new File(destinationFullPath);
//        assertTrue(file.exists());
//    }


    @AfterEach
    void Cleanup() {
//        String destinationFullPath = this.pathToTestFolder + "\\" + this.prefixObjectDownloaded + this.imageName;
//
//        File file = new File(destinationFullPath);
//        if (file.exists()) {
//            file.delete();
//        }
        this.bucketManager = new AwsDataObjectHelperImpl(this.credentialsProvider, this.bucketName);
        if (this.bucketManager.existsObject(this.objectName)) {
            this.bucketManager.removeObject(this.objectName);
        }
    }
}