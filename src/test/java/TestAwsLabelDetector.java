import io.github.cdimascio.dotenv.Dotenv;
import org.amt.team07.helpers.labels.AwsLabelDetectorHelper;
import org.amt.team07.helpers.labels.LabelWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.rekognition.model.RekognitionException;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestAwsLabelDetector {
    private AwsLabelDetectorHelper labelDetectorHelper;

    @BeforeEach
    public void setup() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .systemProperties()
                .load();

        AwsBasicCredentials credentials = AwsBasicCredentials
                .create(dotenv.get("AWS_ACCESS_KEY_ID"), dotenv.get("AWS_SECRET_ACCESS_KEY"));
        AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);

        labelDetectorHelper = new AwsLabelDetectorHelper(credentialsProvider, dotenv.get("AWS_DEFAULT_REGION"));
    }

    @Test
    void crashIfURLIsInvalid() {
        assertThrows(RekognitionException.class, () -> labelDetectorHelper.execute("https://www.google.com", 10, 0.5));
    }

    @Test
    void crashIfNbLabelsIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> labelDetectorHelper.execute("https://a.cdn-hotels.com/gdcs/production196/d1429/5c2581f0-c31d-11e8-87bb-0242ac11000d.jpg?impolicy=fcrop&w=800&h=533&q=medium", -1, 0.5));
    }

    @Test
    void crashIfMinConfidenceIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> labelDetectorHelper.execute("https://a.cdn-hotels.com/gdcs/production196/d1429/5c2581f0-c31d-11e8-87bb-0242ac11000d.jpg?impolicy=fcrop&w=800&h=533&q=medium", 10, -0.5));
    }

    @Test
    void crashIfMinConfidenceIsOver100() {
        assertThrows(IllegalArgumentException.class, () -> labelDetectorHelper.execute("https://a.cdn-hotels.com/gdcs/production196/d1429/5c2581f0-c31d-11e8-87bb-0242ac11000d.jpg?impolicy=fcrop&w=800&h=533&q=medium", 10, 100.5));
    }

    @Test
    void getCorrectAmountOfLabels() throws IOException {
        //given
        String image = "https://a.cdn-hotels.com/gdcs/production196/d1429/5c2581f0-c31d-11e8-87bb-0242ac11000d.jpg?impolicy=fcrop&w=800&h=533&q=medium";
        String b64 = "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";
        int nbLabels = 10;
        double minConfidence = 0.0;

        //when
        List<LabelWrapper> labels = labelDetectorHelper.execute(image, nbLabels, minConfidence);
        List<LabelWrapper> labelsB64 = labelDetectorHelper.executeB64(b64, nbLabels, minConfidence);

        //then
        assertTrue(nbLabels >= labels.size());
        assertTrue(nbLabels >= labelsB64.size());
    }

    @Test
    void labelsAreAtMinimumConfidence() throws IOException {
        //given
        String image = "https://a.cdn-hotels.com/gdcs/production196/d1429/5c2581f0-c31d-11e8-87bb-0242ac11000d.jpg?impolicy=fcrop&w=800&h=533&q=medium";
        String b64 = "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";
        int nbLabels = 10;
        double minConfidence = 99.99;

        //when
        List<LabelWrapper> labels = labelDetectorHelper.execute(image, nbLabels, minConfidence);
        List<LabelWrapper> labelsB64 = labelDetectorHelper.executeB64(b64, nbLabels, minConfidence);

        //then
        for (LabelWrapper label : labels) {
            assertTrue(label.getConfidence() >= minConfidence);
        }
        for (LabelWrapper label : labelsB64) {
            assertTrue(label.getConfidence() >= minConfidence);
        }
    }
}
