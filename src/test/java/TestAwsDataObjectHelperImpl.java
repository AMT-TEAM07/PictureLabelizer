import io.github.cdimascio.dotenv.Dotenv;
import org.amt.team07.helpers.dataObjects.AwsDataObjectHelperImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;

import java.io.File;
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
        assertTrue(bucketManager.ExistsBucket(bucketName));
        assertFalse(bucketManager.ExistsObject(objectName));

        //when
        bucketManager.CreateObject(objectName, testImagePath);

        //then
        assertTrue(bucketManager.ExistsObject(objectName));
    }

    @Test
    void DownloadObject_NominalCase_Success() {
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
    }

//    @Test
//    void Exists_NominalCase_Success() {
//        //given
//        this.bucketManager.CreateObject(this.bucketUrl);
//        boolean actualResult;
//
//        //when
//        actualResult = this.bucketManager.Exists(bucketUrl);
//
//        //then
//        assertTrue(actualResult);
//    }
//
//    @Test
//    void Exists_ObjectNotExistBucket_Success() {
//        //given
//        String notExistingBucket = "notExistingBucket" + this.domain;
//        boolean actualResult;
//
//        //when
//        actualResult = this.bucketManager.Exists(notExistingBucket);
//
//        //then
//        assertFalse(actualResult);
//    }
//
//    @Test
//    void Exists_ObjectNotExistFile_Success() {
//        //given
//        this.bucketManager.Create(this.bucketUrl);
//        String notExistingFile = bucketUrl + "//" + "notExistingFile.jpg";
//        assertTrue(this.bucketManager.Exists(bucketUrl));
//        boolean actualResult;
//
//        //when
//        actualResult = this.bucketManager.Exists(notExistingFile);
//
//        //then
//        assertFalse(actualResult);
//    }
//
//    @Test
//    void RemoveObject_EmptyBucket_Success() {
//        //given
//        this.bucketManager.Create(this.bucketUrl);
//        assertTrue(this.bucketManager.Exists(bucketUrl));
//
//        //when
//        this.bucketManager.RemoveObject(this.bucketUrl);
//
//        //then
//        assertFalse(this.bucketManager.Exists(bucketUrl));
//    }
//
//    @Test
//    void RemoveObject_NotEmptyBucket_Success() {
//        //given
//        String fileName = this.imageName;
//        String objectUrl = this.bucketUrl + "/" + this.imageName;
//        this.bucketManager.Create(this.bucketUrl);
//        this.bucketManager.Create(objectUrl, this.pathToTestFolder + "//" + fileName);
//
//        assertTrue(this.bucketManager.Exists(bucketUrl));
//        assertTrue(this.bucketManager.Exists(objectUrl));
//
//        //when
//        this.bucketManager.RemoveObject(this.bucketUrl);
//
//        //then
//        assertFalse(this.bucketManager.Exists(bucketUrl));
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
        if (this.bucketManager.ExistsObject(this.objectName)) {
            this.bucketManager.RemoveObject(this.objectName);
        }
    }
}