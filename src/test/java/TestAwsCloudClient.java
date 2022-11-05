import org.amt.team07.clients.AwsCloudClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestAwsCloudClient {
    @Test
    void getInstance_NominalCase_Success() {
        AwsCloudClient instance = AwsCloudClient.getInstance();
        assertNotNull(instance);
    }
}
