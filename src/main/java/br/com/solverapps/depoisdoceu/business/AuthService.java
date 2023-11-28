package br.com.solverapps.depoisdoceu.business;

import br.com.solverapps.depoisdoceu.data.dto.AuthenticationDTO;
import br.com.solverapps.depoisdoceu.data.dto.LoginResponseDTO;
import br.com.solverapps.depoisdoceu.data.dto.RegisterDTO;
import br.com.solverapps.depoisdoceu.data.model.User;
import br.com.solverapps.depoisdoceu.data.repository.UserRepository;
import br.com.solverapps.depoisdoceu.security.JwtService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    Logger logger = LoggerFactory.getLogger(AuthService.class);

    public LoginResponseDTO login(AuthenticationDTO request){
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.login(), request.password());
        Authentication auth = authenticationManager.authenticate(usernamePassword);//Throws an exception in case of invalid credentials

        String token = jwtService.generateToken((User)auth.getPrincipal());

        logger.debug("The user "+request.login()+" is logged in now.");
        return new LoginResponseDTO(token);
    }

    public Boolean register(RegisterDTO request){
        if(StringUtils.isBlank(request.login())
                || StringUtils.isBlank(request.email())
                || StringUtils.isBlank(request.name()))
            return false;
        if(this.userRepository.findUserByLogin(request.login()).isPresent())
            return false;

        String encryptedPassword = new BCryptPasswordEncoder().encode(request.password());
        User newUser = new User(request.name(), request.login(), encryptedPassword, request.email(), request.role());

        this.userRepository.save(newUser);

        logger.debug("The user "+newUser+" was registered.");
        return true;
    }
}
