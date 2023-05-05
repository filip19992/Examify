package pl.examify.Examify.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {
    @GetMapping("/getCurrentUser")
    public ResponseEntity<String> getCurrentUser(HttpServletRequest request) {
        try {
            return new ResponseEntity<>( request.getUserPrincipal().getName(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>( "",HttpStatus.OK);
        }
    }
}
