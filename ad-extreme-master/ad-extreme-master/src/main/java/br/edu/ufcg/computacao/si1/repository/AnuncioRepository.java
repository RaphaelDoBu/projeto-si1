package br.edu.ufcg.computacao.si1.repository;

import br.edu.ufcg.computacao.si1.model.Anuncio;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Marcus Oliveira on 28/12/16.
 */
public interface AnuncioRepository extends JpaRepository<Anuncio, Long> {

}
