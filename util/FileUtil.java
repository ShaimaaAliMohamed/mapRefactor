package com.sadeem.smap.util;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUtil {

    public void writeToFile(MultipartFile file, String filePath) throws IOException {
        File targetFile = new File(filePath);
        try (FileOutputStream fos = new FileOutputStream(targetFile)) {
            fos.write(file.getBytes());
        }
    }
}