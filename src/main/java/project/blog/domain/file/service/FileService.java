package project.blog.domain.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import project.blog.domain.file.dto.FileDto;
import project.blog.global.config.common.ErrorCode;
import project.blog.global.config.properties.FileStorageProperties;
import project.blog.global.exception.custom.FileNotFoundException;
import project.blog.global.exception.custom.FileNotProvidedException;
import project.blog.global.exception.custom.FileUploadException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "pdf");

    private final FileStorageProperties fileStorage;

    public FileDto saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileNotProvidedException(ErrorCode.FILE_NOT_PROVIDED);
        }

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = getFileExtension(originalFileName);
        String savedFileName = UUID.randomUUID() + "." + extension;
        Path uploadPath = getUploadPath();

        try {
            // 파일 저장
            Path targetPath = uploadPath.resolve(savedFileName);
            file.transferTo(targetPath.toFile());

            return FileDto.of(originalFileName, savedFileName, file.getContentType(), extension, file.getSize(), uploadPath.toString());
        } catch(IOException e) {
            throw new FileUploadException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    public Resource getFileResource(String filePath) {
        try {
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new FileNotFoundException(ErrorCode.FILE_NOT_FOUND_OR_NOT_READABLE);
            }

            return resource;
        } catch (MalformedURLException e) {
            throw new FileNotFoundException(ErrorCode.INVALID_FILE_PATH);
        }
    }

    public void deleteFile(String savedFileName) {
        Path path = Paths.get(fileStorage.getPath(), savedFileName);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.error("File delete failed: " + savedFileName, e);
        }
    }

    private String getFileExtension(String fileName) {
        String extension = StringUtils.getFilenameExtension(fileName);
        if (!StringUtils.hasText(extension)) {
            throw new FileUploadException(ErrorCode.INVALID_FILE_EXTENSION);
        }

        String lowerCaseExtension = extension.toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(lowerCaseExtension)) {
            throw new FileUploadException(ErrorCode.NOT_ALLOWED_FILE_EXTENSION);
        }

        return lowerCaseExtension;
    }

    private Path getUploadPath() {
        Path path = Paths.get(fileStorage.getPath());
        try {
            if (Files.notExists(path)) {
                Files.createDirectories(path); // 디렉토리 생성
            }
        } catch(IOException e) {
            throw new FileUploadException(ErrorCode.FILE_DIRECTORY_CREATION_FAILED);
        }

        return path;
    }

}
