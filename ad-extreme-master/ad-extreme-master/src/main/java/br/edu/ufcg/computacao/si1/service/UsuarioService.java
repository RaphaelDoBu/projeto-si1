package br.edu.ufcg.computacao.si1.service;

import br.edu.ufcg.computacao.si1.model.Usuario;
import br.edu.ufcg.computacao.si1.model.form.UsuarioForm;

import java.util.Collection;
import java.util.Optional;

public interface UsuarioService {
    Usuario create(UsuarioForm usuarioForm);

    Optional<Usuario> getById(Long id);

    Optional<Usuario> getByEmail(String email);

    Collection<Usuario> getAll();

    boolean update(Usuario usuario);

    boolean delete(Long id);
}
