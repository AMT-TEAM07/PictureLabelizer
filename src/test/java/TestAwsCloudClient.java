import org.amt.team07.clients.AwsCloudClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestAwsCloudClient {
    @Test
    void CanGetAWSCloudClientInstance() {
        AwsCloudClient instance = AwsCloudClient.getInstance();
        assertNotNull(instance);
    }
}
