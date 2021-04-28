package com.neconico.neconico.service.file;

import com.neconico.neconico.file.process.FileProcess;
import com.neconico.neconico.immutable.FileResultInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    void setFileProcess(FileProcess fileProcess);

    FileResultInfo uploadFiles(MultipartFile... files) throws IOException, IllegalStateException, IllegalArgumentException;

    boolean deleteFiles(String fileNames) throws IllegalArgumentException;

}
