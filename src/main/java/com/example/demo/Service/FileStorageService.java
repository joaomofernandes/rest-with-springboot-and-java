package com.example.demo.Service;

import com.example.demo.Exceptions.FileStorageException;
import com.example.demo.Exceptions.MyFileNotFoundException;
import com.example.demo.config.FileStorageConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig) {
        Path path = Paths.get(fileStorageConfig.getUpload_dir())
                .toAbsolutePath().normalize(); // colocar o diretorio com as barras ao contrario e colocar o c: no diretório.

        this.fileStorageLocation = path;

        try{
            Files.createDirectories(this.fileStorageLocation);
        }catch(Exception e){
            throw new FileStorageException("Cannot create directory where the uploaded files will be stored.", e);
        }
    }

    public String storeFile(MultipartFile file){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try{
            if(fileName.contains("..") || fileName.contains(" ") ){
                throw new FileStorageException("Sorry, filename contains invalid path sequence: " + fileName);
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        }catch (FileStorageException e){
            throw new FileStorageException("Cannot store " + fileName + ". Please try again.", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Resource loadFileAsResource(String fileName){
        try{
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()){
                return resource;
            }else throw new MyFileNotFoundException("File not found: " + fileName);
        }catch(Exception e){
            throw new MyFileNotFoundException("File not found: " + fileName, e);
        }
    }
}
