package com.neconico.neconico.file.process;

import com.neconico.neconico.file.policy.FilePolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileProcessFactory {

    private static String fileLocation;

    public static FileProcess getFileProcess(FilePolicy filePolicy) throws IllegalArgumentException {
        switch (fileLocation) {
            case "s3":
                return new S3FileProcess(filePolicy);
            case "local":
                return new LocalFileProcess(filePolicy);
            default:
                throw new IllegalArgumentException("filetype error");
        }
    }

    @Value("${file.location}")
    private void setFileType(String type) {
        fileLocation = type;
    }
}
