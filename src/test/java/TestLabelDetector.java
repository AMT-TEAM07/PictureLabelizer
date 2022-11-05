import io.github.cdimascio.dotenv.Dotenv;
import org.amt.team07.helpers.dataObjects.AwsDataObjectHelperImpl;
import org.amt.team07.helpers.labelDetectors.AwsLabelDetectorHelperImpl;
import org.amt.team07.helpers.labelDetectors.LabelWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.rekognition.model.Label;
import software.amazon.awssdk.services.rekognition.model.RekognitionException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestLabelDetector {
    private ProfileCredentialsProvider credentialsProvider;
    private AwsLabelDetectorHelperImpl labelDetectorHelper;

    @BeforeEach
    public void init()
    {
        Dotenv dotenv = Dotenv.load();
        credentialsProvider = ProfileCredentialsProvider.create(dotenv.get("AWS_PROFILE"));
        labelDetectorHelper = new AwsLabelDetectorHelperImpl(credentialsProvider);
    }

    @Test
    public void crashIfURLIsInvalid()
    {
        assertThrows(RekognitionException.class, () -> labelDetectorHelper.Execute("https://www.google.com", 10, 0.5));
    }

    @Test
    public void crashIfNbLabelsIsNegative()
    {
        assertThrows(IllegalArgumentException.class, () -> labelDetectorHelper.Execute("https://a.cdn-hotels.com/gdcs/production196/d1429/5c2581f0-c31d-11e8-87bb-0242ac11000d.jpg?impolicy=fcrop&w=800&h=533&q=medium", -1, 0.5));
    }

    @Test
    public void crashIfMinConfidenceIsNegative()
    {
        assertThrows(IllegalArgumentException.class, () -> labelDetectorHelper.Execute("https://a.cdn-hotels.com/gdcs/production196/d1429/5c2581f0-c31d-11e8-87bb-0242ac11000d.jpg?impolicy=fcrop&w=800&h=533&q=medium", 10, -0.5));
    }

    @Test
    public void crashIfMinConfidenceIsOver100()
    {
        assertThrows(IllegalArgumentException.class, () -> labelDetectorHelper.Execute("https://a.cdn-hotels.com/gdcs/production196/d1429/5c2581f0-c31d-11e8-87bb-0242ac11000d.jpg?impolicy=fcrop&w=800&h=533&q=medium", 10, 100.5));
    }

    @Test
    void getCorrectAmountOfLabels() {
        //given
        String image = "https://a.cdn-hotels.com/gdcs/production196/d1429/5c2581f0-c31d-11e8-87bb-0242ac11000d.jpg?impolicy=fcrop&w=800&h=533&q=medium";
        int nbLabels = 10;
        double minConfidence = 0.0;

        //when
        List<LabelWrapper> labels = labelDetectorHelper.Execute(image, nbLabels, minConfidence);

        //then
        assertTrue(nbLabels >= labels.size());
    }

    @Test
    void labelsAreAtMinimumConfidence() {
        //given
        String image = "https://a.cdn-hotels.com/gdcs/production196/d1429/5c2581f0-c31d-11e8-87bb-0242ac11000d.jpg?impolicy=fcrop&w=800&h=533&q=medium";
        int nbLabels = 10;
        double minConfidence = 99.99;

        //when
        List<LabelWrapper> labels = labelDetectorHelper.Execute(image, nbLabels, minConfidence);

        //then
        for (LabelWrapper label : labels) {
            assertTrue(label.getConfidence() >= minConfidence);
        }
    }
}
