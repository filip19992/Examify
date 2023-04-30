package pl.examify.Examify.api;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TemplateController {
    private final ResourceLoader resourceLoader;

    public TemplateController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/")
    public ResponseEntity<String> main() {
        Resource resource = resourceLoader.getResource("classpath:/static/main.html");
        try {
            String content = new String(resource.getInputStream().readAllBytes());
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/exam")
    public ResponseEntity<String> exam() {
        Resource resource = resourceLoader.getResource("classpath:/static/exam.html");
        try {
            String content = new String(resource.getInputStream().readAllBytes());
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
