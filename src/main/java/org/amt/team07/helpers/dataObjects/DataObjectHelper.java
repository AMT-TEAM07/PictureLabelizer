package org.amt.team07.helpers.dataObjects;

import java.nio.file.Path;

public interface DataObjectHelper {
    void createObject(String objectName, Path filePath);
    boolean existsBucket(String bucketName);
    boolean existsObject(String objectName);
    void removeObject(String objectName);
    boolean downloadObject(String objectUrl, Path downloadedImagePath);
}
