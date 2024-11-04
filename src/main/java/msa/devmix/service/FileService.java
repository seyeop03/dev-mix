package msa.devmix.service;

import msa.devmix.dto.UploadFileNameDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String getFullPath(String filename);
    String uploadFile(MultipartFile multipartFile) throws IOException;
}
