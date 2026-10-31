package com.digitallocker.controller;

import com.digitallocker.entity.Document;
import com.digitallocker.entity.User;
import com.digitallocker.repository.UserRepository;
import com.digitallocker.service.DocumentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Path;

@Controller
public class LockerController {

    private final UserRepository userRepository;
    private final DocumentService documentService;

    public LockerController(UserRepository userRepository, DocumentService documentService) {
        this.userRepository = userRepository;
        this.documentService = documentService;
    }

    private User getLoggedInUser(HttpSession session) {
        Object userId = session.getAttribute("loggedInUser");
        if (userId == null) {
            return null;
        }
        return userRepository.findById((Long) userId).orElse(null);
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = getLoggedInUser(session);
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);
        model.addAttribute("documents", documentService.getUserDocuments(user));
        model.addAttribute("documentCount", documentService.countDocuments(user));
        return "dashboard";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,
                         @RequestParam String category,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        User user = getLoggedInUser(session);
        if (user == null) return "redirect:/login";

        try {
            documentService.saveDocument(file, category, user);
            redirectAttributes.addFlashAttribute("message", "Document uploaded successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Upload failed: " + e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id, HttpSession session) throws IOException {
        User user = getLoggedInUser(session);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Document document = documentService.getDocument(id, user).orElse(null);
        if (document == null) return ResponseEntity.notFound().build();

        Path path = documentService.getFilePath(document.getStoredFileName());
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(document.getDocumentName()).build().toString())
                .body(resource);
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = getLoggedInUser(session);
        if (user == null) return "redirect:/login";

        Document document = documentService.getDocument(id, user).orElse(null);
        if (document == null) {
            redirectAttributes.addFlashAttribute("error", "Document not found.");
            return "redirect:/dashboard";
        }

        try {
            documentService.deleteDocument(document);
            redirectAttributes.addFlashAttribute("message", "Document deleted.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Could not delete the stored file.");
        }
        return "redirect:/dashboard";
    }
}
