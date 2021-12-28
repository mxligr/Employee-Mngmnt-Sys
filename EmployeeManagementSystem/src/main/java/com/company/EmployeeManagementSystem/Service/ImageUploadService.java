package com.company.EmployeeManagementSystem.Service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Service
public class ImageUploadService {

    @Value("${image.folder}")
    private String imageFolder;

    private org.slf4j.Logger logger = LoggerFactory.getLogger(ImageUploadService.class);

    public File upload(MultipartFile imageFile){
        try{
            Path path = Paths.get(imageFolder, imageFile.getOriginalFilename());
            Files.write(path, imageFile.getBytes());
            return path.toFile();
        }catch(IOException e)
        {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
