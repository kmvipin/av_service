package com.avvsion.service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.InputStream;

@Service
public interface FileService {
    String uploadImage(String path, MultipartFile file);
    InputStream requestImage(String path, String name) throws FileNotFoundException;
}