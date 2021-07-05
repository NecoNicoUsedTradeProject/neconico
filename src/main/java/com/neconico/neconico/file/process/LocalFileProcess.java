package com.neconico.neconico.file.process;

import com.neconico.neconico.dto.file.FileResultInfoDto;
import com.neconico.neconico.file.policy.FilePolicy;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class LocalFileProcess implements FileProcess {

    private static final String LOCAL_DIR = "/localdir/";

    private String dirName;

    private int fileCount;

    private StringBuffer fileUrls = new StringBuffer();

    private StringBuffer fileNames = new StringBuffer();

    public LocalFileProcess(FilePolicy filePolicy) {
        dirName = filePolicy.getDirName();
        fileCount = filePolicy.getFileCount();
    }

    @Override
    public FileResultInfoDto uploadFile(MultipartFile... files) throws IOException, IllegalStateException, IllegalArgumentException {
        if(files.length > fileCount) {
            throw new IllegalArgumentException("There are more files than should be received");
        }

        for (MultipartFile file : files) {
            createDir();

            File dest = createDest(file);

            file.transferTo(dest);

            insertFileNamesAndURLs(dest);

        }

        return createFileResultInfo();
    }

    @Override
    public void deleteFiles(String fileNames) throws IllegalArgumentException {
        if(fileNames == null || fileNames.length() == 0) {
            throw new IllegalArgumentException("Entered the wrong path");
        }

        String[] originalFileNames = fileNames.split(":");

        deleteOriginFiles(originalFileNames);

    }

    private void createDir() {
        File file = new File(LOCAL_DIR + dirName + "/");
        if (!file.exists()) {
            file.mkdir();
        }
    }

    private File createDest(MultipartFile file) {
        return new File(LOCAL_DIR + dirName + "/" + convertUniqueName(file.getOriginalFilename()));
    }

    private String convertUniqueName(String originalFileName) {
        return UUID.randomUUID() + originalFileName;
    }

    private void insertFileNamesAndURLs(File dest) throws IOException {
        fileNames.append(dest + ":");
        fileUrls.append(dest.toURI().toURL() + ":");
    }

    private FileResultInfoDto createFileResultInfo() {
        String fileUrls = this.fileUrls.deleteCharAt(this.fileUrls.length() - 1).toString();

        String fileNames = this.fileNames.deleteCharAt(this.fileNames.length() - 1).toString();

        return new FileResultInfoDto(fileUrls, fileNames);
    }

    private void deleteOriginFiles(String[] originalFileNames) {
        for(String originalFileName : originalFileNames) {
            File originalFile = new File(originalFileName);

            if(originalFile.exists()) {
                originalFile.delete();
            }
        }
    }
}
