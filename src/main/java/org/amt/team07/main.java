package org.amt.team07;

import org.amt.team07.clients.AwsCloudClient;

public class main {
    public static void main(String[] args) {
        AwsCloudClient client = AwsCloudClient.getInstance();
        client.rekognitionFromURL("https://a.cdn-hotels.com/gdcs/production196/d1429/5c2581f0-c31d-11e8-87bb-0242ac11000d.jpg?impolicy=fcrop&w=800&h=533&q=medium",
                10, 99.);
    }
}
