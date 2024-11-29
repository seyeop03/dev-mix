package msa.devmix.service.implement;


import lombok.extern.slf4j.Slf4j;
import msa.devmix.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${file.dir}")
    private String fileDir; //실제 서버 저장 경로

    @Value("${file.url}")
    private String fileUrl; //파일 가져올 수 있는 URL


    @Override
    public String getFullPath(String filename) {
        log.info("filename: {}", fileDir + filename);
        return fileDir + filename;
    }

    /**
     * 서버에 파일 저장 후 업로드한 파일명 반환
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {

        if (multipartFile == null || multipartFile.isEmpty()) return null;

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);

        multipartFile.transferTo(new File(getFullPath(storeFilename)));

        return fileUrl + storeFilename;
    }

    @Override
    public void deleteFile(String fileName) {

        // C:/~~/uuid.png
        File file = new File(getFullPath(fileName));
        if (file.exists()) file.delete();
    }

    private String createStoreFilename(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." +ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
