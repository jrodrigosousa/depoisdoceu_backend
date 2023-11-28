package br.com.solverapps.depoisdoceu.controller;

import br.com.solverapps.depoisdoceu.business.AuthService;
import br.com.solverapps.depoisdoceu.data.dto.AuthenticationDTO;
import br.com.solverapps.depoisdoceu.data.dto.LoginResponseDTO;
import br.com.solverapps.depoisdoceu.data.dto.RegisterDTO;
import br.com.solverapps.depoisdoceu.data.model.User;
import br.com.solverapps.depoisdoceu.data.repository.UserRepository;
import br.com.solverapps.depoisdoceu.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationDTO request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO request){
        if(authService.register(request))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }
}
