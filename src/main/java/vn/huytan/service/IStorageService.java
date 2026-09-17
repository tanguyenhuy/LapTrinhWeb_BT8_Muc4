package vn.huytan.service;

import java.nio.file.Path;
import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {
    void init();
    void store(MultipartFile file, String storeFilename);
    String getStorageFilename(MultipartFile file, String id);
    void delete(String storeFilename);
    Path load(String filename);
}