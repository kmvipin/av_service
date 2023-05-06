package com.avvsion.service.service.fileserviceimpl;

import com.avvsion.service.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{
    @Override
    public String uploadImage(String path, MultipartFile file) {
        //File name
        String name = file.getOriginalFilename();
        String randomId = UUID.randomUUID().toString();
        name = randomId.concat(name.substring(name.lastIndexOf(".")));
        //Full Path
        String filePath = path+File.separator+name;
        //create folder if not created
        File f = new File(path);
        if(!f.exists()){
            f.mkdir();
        }
        //file copy
        try {
            Files.copy(file.getInputStream(), Paths.get(filePath));
        }
        catch(IOException e){
            System.out.println("heloo dostoo");
            e.printStackTrace();
            return null;
        }
        return name;
    }
    @Override
    public InputStream requestImage(String path, String name) throws FileNotFoundException {
        String fullPath = path+File.separator+name;
        InputStream is = new FileInputStream(fullPath);
        return is;
    }
    @Override
    public void deleteImage(String path, String name){
        File file = new File(path,name);
        file.delete();
    }
}
