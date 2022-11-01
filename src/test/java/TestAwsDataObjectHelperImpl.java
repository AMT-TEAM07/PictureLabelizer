import io.github.cdimascio.dotenv.Dotenv;
import org.amt.team07.helpers.dataObjects.AwsDataObjectHelperImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class TestAwsDataObjectHelperImpl {

    private AwsDataObjectHelperImpl bucketManager;

    private ProfileCredentialsProvider credentialsProvider;
    private String domain;
    private String bucketName;
    private String bucketUrl;
    private String imageName;
    private String pathToTestFolder;
    private String fullPathToImage;
    private String prefixObjectDownloaded;

    @BeforeEach
    public void Init()
    {
        Dotenv dotenv = Dotenv.load();
        this.pathToTestFolder = System.getProperty("user.dir").replace("bin\\Debug\\netcoreapp3.1", "testData");
        this.bucketName = dotenv.get("AWS_BUCKET");
        this.domain = "aws.dev.actualit.info";
        this.bucketUrl = bucketName + "." + this.domain;
        this.imageName = "emiratesa380.jpg";
        this.fullPathToImage = pathToTestFolder + "\\" + imageName;
        this.prefixObjectDownloaded = "downloaded";
        this.credentialsProvider = ProfileCredentialsProvider.create(dotenv.get("AWS_PROFILE"));

        this.bucketManager = new AwsDataObjectHelperImpl(credentialsProvider, bucketUrl);
    }

    @Test
     void CreateObject_CreateNewBucket_Success() {
        assertFalse(this.bucketManager.Exists(bucketUrl));

        this.bucketManager.Create(bucketUrl);

        assertTrue(this.bucketManager.Exists(bucketUrl));
    }

    @Test
    void CreateObject_CreateObjectWithExistingBucket_Success()
    {
        //given
        String fileName = this.imageName;
        String objectUrl = this.bucketUrl + "/" + this.imageName;
        this.bucketManager.Create(this.bucketUrl);
        
        assertTrue(this.bucketManager.Exists(this.bucketUrl));
        assertFalse(this.bucketManager.Exists(objectUrl));

        //when
        this.bucketManager.Create(objectUrl, this.pathToTestFolder + "//" + fileName);

        //then
        assertTrue(this.bucketManager.Exists(objectUrl));
    }

    @Test
    void CreateObject_CreateObjectBucketNotExist_Success()
    {
        //given
        String fileName = this.imageName;
        String objectUrl = this.bucketUrl + "/" + this.imageName;
        assertFalse(this.bucketManager.Exists(this.bucketUrl));
        assertFalse(this.bucketManager.Exists(objectUrl));

        //when
        this.bucketManager.Create(objectUrl, this.pathToTestFolder + "//" + fileName);

        //then
        assertTrue(this.bucketManager.Exists(objectUrl));
    }

    @Test
    void DownloadObject_NominalCase_Success()
    {
        //given
        String objectUrl = bucketUrl + "//" + this.imageName;
        String destinationFullPath = this.pathToTestFolder + "//" + this.prefixObjectDownloaded + this.imageName;
        this.bucketManager.Create(objectUrl, this.pathToTestFolder + "//" + this.imageName);

        assertTrue(this.bucketManager.Exists(bucketUrl));

        //when
        this.bucketManager.DownloadObject(objectUrl, destinationFullPath);

        //then
        File file = new File(destinationFullPath);
        assertTrue(file.exists());
    }

    @Test
    void Exists_NominalCase_Success()
    {
        //given
        this.bucketManager.Create(this.bucketUrl);
        boolean actualResult;

        //when
        actualResult = this.bucketManager.Exists(bucketUrl);

        //then
        assertTrue(actualResult);
    }

    @Test
    void Exists_ObjectNotExistBucket_Success()
    {
        //given
        String notExistingBucket = "notExistingBucket" + this.domain;
        boolean actualResult;

        //when
        actualResult = this.bucketManager.Exists(notExistingBucket);

        //then
        assertFalse(actualResult);
    }

    @Test
    void Exists_ObjectNotExistFile_Success()
    {
        //given
        this.bucketManager.Create(this.bucketUrl);
        String notExistingFile = bucketUrl + "//" + "notExistingFile.jpg";
        assertTrue(this.bucketManager.Exists(bucketUrl));
        boolean actualResult;

        //when
        actualResult = this.bucketManager.Exists(notExistingFile);

        //then
        assertFalse(actualResult);
    }

    @Test
    void RemoveObject_EmptyBucket_Success()
    {
        //given
        this.bucketManager.Create(this.bucketUrl);
        assertTrue(this.bucketManager.Exists(bucketUrl));

        //when
        this.bucketManager.RemoveObject(this.bucketUrl);

        //then
        assertFalse(this.bucketManager.Exists(bucketUrl));
    }

    @Test
    void RemoveObject_NotEmptyBucket_Success()
    {
        //given
        String fileName = this.imageName;
        String objectUrl = this.bucketUrl + "/" + this.imageName;
        this.bucketManager.Create(this.bucketUrl);
        this.bucketManager.Create(objectUrl, this.pathToTestFolder + "//" + fileName);

        assertTrue(this.bucketManager.Exists(bucketUrl));
        assertTrue(this.bucketManager.Exists(objectUrl));

        //when
        this.bucketManager.RemoveObject(this.bucketUrl);

        //then
        assertFalse(this.bucketManager.Exists(bucketUrl));
    }

    @AfterEach
    void Cleanup(){
        String destinationFullPath = this.pathToTestFolder + "\\" + this.prefixObjectDownloaded + this.imageName;

        File file = new File(destinationFullPath);
        if (file.exists())
        {
            file.delete();
        }

        this.bucketManager = new AwsDataObjectHelperImpl(this.credentialsProvider, this.bucketUrl);
        if (this.bucketManager.Exists(bucketUrl))
        {
            this.bucketManager.RemoveObject(this.bucketUrl);
        }
    }
 }