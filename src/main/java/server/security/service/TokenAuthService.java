package server.security.service;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import server.security.authentication.UserAuthentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class TokenAuthService {
    @Value("${jwt.token.header}")
    private String headerName;

    @Autowired
    private TokenHandler tokenHandler;

    @Autowired
    private SecurityUserDetailsService securityUserDetailsService;



    public Optional<Authentication> getAuthentication(@NonNull HttpServletRequest request) {
        String token = request.getHeader(headerName);
        return Optional
                .ofNullable(token)
                .flatMap(tokenHandler::extractUsername)
                .flatMap(securityUserDetailsService::findByUsername)
                .map(UserAuthentication::new);

    }
}
