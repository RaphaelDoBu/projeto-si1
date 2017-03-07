package br.edu.ufcg.computacao.si1.config;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by marcus on 06/02/17.
 */
public class BasicEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return (String) charSequence;
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return ((String) charSequence).equals(s);
    }
}
