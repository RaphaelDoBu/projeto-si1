package br.edu.ufcg.computacao.si1.service;

import br.edu.ufcg.computacao.si1.model.Usuario;
import br.edu.ufcg.computacao.si1.model.form.UsuarioForm;
import br.edu.ufcg.computacao.si1.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    private UsuarioRepository usuarioRepository;

    @Autowired
    public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario create(UsuarioForm usuarioForm) {

        Usuario usuario=null;

        switch (usuarioForm.getRole()){
            case 1:
                usuario = new Usuario(usuarioForm.getNome(), usuarioForm.getEmail(),
                        usuarioForm.getSenha(), "USER");
                break;
            case 2:
                usuario = new Usuario(usuarioForm.getNome(), usuarioForm.getEmail(),
                        usuarioForm.getSenha(), "COMPANY");

                //new BCryptPasswordEncoder().encode(usuarioForm.getSenha()), "COMPANY");
                usuario.setR("COMPANY");
                break;
        }

        System.out.println(usuario + "estah sendo criado");
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> getById(Long id) {
        return Optional.ofNullable(usuarioRepository.findOne(id));
    }

    @Override
    public Optional<Usuario> getByEmail(String email) {
        System.out.println(email + "estah sendo retornado");
        return Optional.ofNullable(usuarioRepository.findByEmail(email));
    }

    @Override
    public Collection<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public boolean update(Usuario usuario) {
        System.out.println(usuario + "estah sendo atualizado");

        if (usuarioRepository.exists(usuario.getId())) {
            usuarioRepository.save(usuario);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        if (usuarioRepository.exists(id)) {
            usuarioRepository.delete(id);
            return true;
        }
        return false;
    }
}
