package br.edu.ufcg.computacao.si1.service;

import br.edu.ufcg.computacao.si1.model.Anuncio;
import br.edu.ufcg.computacao.si1.model.Usuario;
import br.edu.ufcg.computacao.si1.model.form.AnuncioForm;
import br.edu.ufcg.computacao.si1.repository.AnuncioRepository;
import br.edu.ufcg.computacao.si1.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Marcus Oliveira on 28/12/16.
 */
@Service
public class AnuncioServiceImpl implements AnuncioService {
	// TODO add validity checks

	@Autowired
	private AnuncioRepository anuncioRepository;
	@Autowired
	private AnuncioServiceImpl anuncioService;
	@Autowired
	private UsuarioServiceImpl usuarioServiceImpl;
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	public AnuncioServiceImpl(AnuncioRepository anuncioRepository) {
		/* neste codigo apenas atribuimos o repositorio jpa ao atributo */
		this.anuncioRepository = anuncioRepository;
	}

	public AnuncioRepository getAnuncioRepository() {
		return this.anuncioRepository;
	}

	@Override
	public Anuncio create(Anuncio anuncio) {
		/* aqui salvamos o anuncio recem criado no repositorio jpa */
		return anuncioRepository.save(anuncio);
	}

	@Override
	public Optional<Anuncio> getById(Long id) {
		/* aqui recuperamos o anuncio pelo seu id */
		return Optional.ofNullable(anuncioRepository.findOne(id));
	}

	@Override
	public Collection<Anuncio> get(String tipo) {

		/*
		 * pegamos aqui todos os anuncios, mas retornamos os anuncios por tipo
		 * filtrando o tipo, pelo equals, retornando um arrayLista
		 */
		return anuncioRepository.findAll().stream().filter(anuncio -> anuncio.getTipo().equals(tipo))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public Collection<Anuncio> getAll() {
		/* aqui retornamos todos os anuncios, sem distincao */

		return anuncioRepository.findAll();
	}

	@Override
	public boolean update(Anuncio anuncio) {
		/* a atualizacao do anuncio eh feita apenas se o anuncio ja existir */
		if (anuncioRepository.exists(anuncio.get_id())) {
			anuncioRepository.save(anuncio);
			return true;
		}
		return false;
	}

	@Override
	public boolean delete(Long id) {
		/* aqui se apaga o anuncio se ele existir */

		if (anuncioRepository.exists(id)) {
			anuncioRepository.delete(id);
			return true;
		}
		return false;
	}

	@Override
	public Collection<Anuncio> getAnuncioByDateFormat(Date data) {
		return anuncioRepository.getAnuncioByDataDeCriacao(data);
	}

	@Override
	public Anuncio cadastrarAnuncio(AnuncioForm anuncioForm) {
		Anuncio anuncio = new Anuncio();
		anuncio.setTitulo(anuncioForm.getTitulo());
		anuncio.setPreco(anuncioForm.getPreco());
		anuncio.setTipo(anuncioForm.getTipo());

		Usuario usuarioLogado = usuarioServiceImpl.usuarioLogadoEmail();
		Long idUsuario = usuarioLogado.getId();
		anuncio.setIdUsuario(idUsuario);

		return anuncioService.create(anuncio);
	}

	@Override
	public void compraAnuncio(Long id) {
		Usuario usuarioLogado = usuarioServiceImpl.usuarioLogadoEmail();

		Anuncio recuperaAnuncio = anuncioRepository.findOne(id);
		// usuario logado compra o anuncio
		usuarioLogado.compraAnuncio(recuperaAnuncio.getPreco());

		// usuario que tem o anuncio vai vender
		Usuario usuarioDonoAnuncio = usuarioRepository.findOne(recuperaAnuncio.getIdUsuario());
		usuarioDonoAnuncio.vendeAnuncio(recuperaAnuncio.getPreco());

		// excluir anuncio
		anuncioRepository.delete(id);

	}

}
