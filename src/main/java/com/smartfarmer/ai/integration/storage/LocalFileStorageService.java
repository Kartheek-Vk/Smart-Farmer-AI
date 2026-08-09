package com.smartfarmer.ai.integration.storage;

import com.smartfarmer.ai.exception.BusinessException;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalFileStorageService implements FileStorageService {

    private final StorageProperties storageProperties;
    private Path rootPath;

    public LocalFileStorageService(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    @PostConstruct
    void init() {
        try {
            rootPath = Path.of(storageProperties.localDirectory()).toAbsolutePath().normalize();
            Files.createDirectories(rootPath);
        } catch (IOException ex) {
            throw new BusinessException("Unable to initialize file storage");
        }
    }

    @Override
    public StoredFile store(MultipartFile file) {
        validate(file);
        String sanitizedExtension = extensionOf(file.getOriginalFilename());
        String storageKey = UUID.randomUUID() + sanitizedExtension;
        Path target = rootPath.resolve(storageKey).normalize();
        try {
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new BusinessException("Unable to store file");
        }
        return new StoredFile(storageKey, file.getOriginalFilename(), file.getContentType(), file.getSize(), target.toUri().toString());
    }

    @Override
    public void delete(String storageKey) {
        try {
            Files.deleteIfExists(rootPath.resolve(storageKey).normalize());
        } catch (IOException ex) {
            throw new BusinessException("Unable to delete stored file");
        }
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("File is required");
        }
        if (file.getSize() > storageProperties.maxFileSizeBytes()) {
            throw new BusinessException("File size exceeds the allowed limit");
        }
        Set<String> allowed = Set.copyOf(storageProperties.allowedContentTypes());
        if (!allowed.contains(file.getContentType())) {
            throw new BusinessException("Unsupported file type");
        }
        String original = StringUtils.cleanPath(file.getOriginalFilename() == null ? "" : file.getOriginalFilename());
        if (original.contains("..") || original.isBlank()) {
            throw new BusinessException("Invalid filename");
        }
    }

    private String extensionOf(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return "";
        }
        return originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
    }
}
