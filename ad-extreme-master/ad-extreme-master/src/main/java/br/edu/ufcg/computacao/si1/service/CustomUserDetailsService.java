package br.edu.ufcg.computacao.si1.service;

import br.edu.ufcg.computacao.si1.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Serializable.class);
    private final UsuarioService userService;

    @Autowired
    public CustomUserDetailsService(UsuarioService userService) {
        this.userService = userService;
    }

    @Override
    public Usuario loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.debug("Authenticating user with email={}", email.replaceFirst("@.*", "@***"));

        Optional<Usuario> usuario = userService.getByEmail(email);

        if (usuario.isPresent()) {
            return usuario.get();
        } else {
            throw new UsernameNotFoundException(String.format("User with email=%s was not found", email));
        }

    }

}