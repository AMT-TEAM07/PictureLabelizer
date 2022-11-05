package org.amt.team07;

import org.amt.team07.clients.AwsCloudClient;

public class main {
    public static void main(String[] args) {
        AwsCloudClient client = AwsCloudClient.getInstance();
        client.executeRekog("", new int[]{5, 25});
    }
}
