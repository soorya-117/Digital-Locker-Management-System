package com.digitallocker.service;

import com.digitallocker.entity.Document;
import com.digitallocker.entity.User;
import com.digitallocker.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final Path uploadFolder = Paths.get("uploads").toAbsolutePath().normalize();

    public DocumentService(DocumentRepository documentRepository) throws IOException {
        this.documentRepository = documentRepository;
        Files.createDirectories(uploadFolder);
    }

    public void saveDocument(MultipartFile file, String category, User user) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please choose a file.");
        }

        // Keep the original extension, but use a random name on the server.
        String originalName = Paths.get(file.getOriginalFilename()).getFileName().toString();
        String extension = "";
        int dot = originalName.lastIndexOf('.');
        if (dot >= 0) {
            extension = originalName.substring(dot);
        }

        String storedName = UUID.randomUUID() + extension;
        Path target = uploadFolder.resolve(storedName).normalize();

        if (!target.startsWith(uploadFolder)) {
            throw new IOException("Invalid file path.");
        }

        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        Document document = new Document();
        document.setDocumentName(originalName);
        document.setCategory(category);
        document.setStoredFileName(storedName);
        document.setUploadDate(LocalDateTime.now());
        document.setUser(user);

        documentRepository.save(document);
    }

    public List<Document> getUserDocuments(User user) {
        return documentRepository.findByUserOrderByUploadDateDesc(user);
    }

    public Optional<Document> getDocument(Long id, User user) {
        return documentRepository.findByIdAndUser(id, user);
    }

    public long countDocuments(User user) {
        return documentRepository.countByUser(user);
    }

    public Path getFilePath(String storedName) {
        return uploadFolder.resolve(storedName).normalize();
    }

    public void deleteDocument(Document document) throws IOException {
        Path file = getFilePath(document.getStoredFileName());
        Files.deleteIfExists(file);
        documentRepository.delete(document);
    }
}
