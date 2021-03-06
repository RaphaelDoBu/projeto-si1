package br.edu.ufcg.computacao.si1.service;

import br.edu.ufcg.computacao.si1.model.Anuncio;
import br.edu.ufcg.computacao.si1.model.form.AnuncioForm;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

/**
 * Created by Marcus Oliveira on 28/12/16.
 */
public interface AnuncioService {

    Anuncio create(Anuncio anuncio);
    Optional<Anuncio> getById(Long id);   
    Collection<Anuncio> getAnuncioByDateFormat(Date data);
    Collection<Anuncio> get(String tipo);
    Collection<Anuncio> getAll();
    boolean update(Anuncio anuncio);
    boolean delete(Long id);
    Anuncio cadastrarAnuncio(AnuncioForm anuncioForm);
    void compraAnuncio(Long id);
    

}
