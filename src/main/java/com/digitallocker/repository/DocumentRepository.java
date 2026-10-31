package com.digitallocker.repository;

import com.digitallocker.entity.Document;
import com.digitallocker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByUserOrderByUploadDateDesc(User user);
    Optional<Document> findByIdAndUser(Long id, User user);
    long countByUser(User user);
}
