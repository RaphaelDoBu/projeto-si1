package br.edu.ufcg.computacao.si1.controller;

import br.edu.ufcg.computacao.si1.model.Usuario;
import br.edu.ufcg.computacao.si1.model.form.AnuncioForm;
import br.edu.ufcg.computacao.si1.repository.AnuncioRepository;
import br.edu.ufcg.computacao.si1.service.AnuncioServiceImpl;
import br.edu.ufcg.computacao.si1.service.UsuarioServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

@Controller
public class UsuarioAnuncioController {

	@Autowired
	private AnuncioServiceImpl anuncioService;
	@Autowired
	private AnuncioRepository anuncioRep;
	@Autowired
	private UsuarioServiceImpl usuarioServiceImpl;

	@RequestMapping(value = "/user/cadastrar/anuncio", method = RequestMethod.GET)
	public ModelAndView getPageCadastrarAnuncio(AnuncioForm anuncioForm) {
		Usuario usuarioLogado = usuarioServiceImpl.usuarioLogadoEmail();
		ModelAndView model = new ModelAndView();

		model.addObject("saldoCredor", usuarioLogado.getSaldoCredor());

		model.addObject("tipos", anuncioForm.getTipos());
		model.setViewName("user/cadastrar_anuncio");

		return model;
	}

	@RequestMapping(value = "/user/listar/anuncios", method = RequestMethod.GET)
	public ModelAndView getPageListarAnuncios(Model mod) {
		Usuario usuarioLogado = usuarioServiceImpl.usuarioLogadoEmail();

		mod.addAttribute("saldoCredor", usuarioLogado.getSaldoCredor());
		Long idUsuario = usuarioLogado.getId();
		ModelAndView model = new ModelAndView();

		model.addObject("anuncios", anuncioRep.findAll());
		model.addObject("idUsuario", idUsuario);
		model.setViewName("user/listar_anuncios");

		return model;
	}

	@RequestMapping(value = "/user/cadastrar/anuncio", method = RequestMethod.POST)
	public ModelAndView cadastroAnuncio(@Valid AnuncioForm anuncioForm, BindingResult result,
			RedirectAttributes attributes) {

		if (result.hasErrors()) {
			return getPageCadastrarAnuncio(anuncioForm);
		}

		anuncioService.cadastrarAnuncio(anuncioForm);

		attributes.addFlashAttribute("mensagem", "Anúncio cadastrado com sucesso!");
		return new ModelAndView("redirect:/user/cadastrar/anuncio");
	}

	@RequestMapping(value = "/user/listar/anuncios/usuario", method = RequestMethod.GET)
	public ModelAndView buscarAnunciosPorUsuario(Model mod) {

		Usuario usuarioLogado = usuarioServiceImpl.usuarioLogadoEmail();
		Long idUsuario = usuarioLogado.getId();
		ModelAndView model = new ModelAndView();

		mod.addAttribute("saldoCredor", usuarioLogado.getSaldoCredor());
		model.addObject("anuncios", anuncioRep.findAll());
		model.addObject("idUsuario", idUsuario);

		model.addObject("anuncios", anuncioService.getAnuncioRepository().getAnuncioByIdUsuario(getIdUsuario()));

		model.setViewName("user/listar_meus_anuncios");

		return model;
	}

	@RequestMapping(value = "/user/listar/anuncios/{tipo}", method = RequestMethod.GET)
	public ModelAndView buscarAnunciosPorTipo(@PathVariable String tipo, Model mod) {
		Usuario usuarioLogado = usuarioServiceImpl.usuarioLogadoEmail();
		Long idUsuario = usuarioLogado.getId();
		ModelAndView model = new ModelAndView();
		
		mod.addAttribute("saldoCredor", usuarioLogado.getSaldoCredor());
		model.addObject("anuncios", anuncioRep.findAll());
		model.addObject("idUsuario", idUsuario);
		mod.addAttribute("tipo", tipo);
		model.addObject("anuncios", anuncioService.getAnuncioRepository().getAnuncioByTipo(tipo));

		model.setViewName("user/listar_anuncios");

		return model;
	}

	@RequestMapping(value = "/user/listar/anuncios/data", method = RequestMethod.GET)
	public ModelAndView buscarAnunciosPorData(@RequestParam("data") String data, Model mod) throws ParseException {
		ModelAndView model = new ModelAndView();
		Usuario usuarioLogado = usuarioServiceImpl.usuarioLogadoEmail();
		Long idUsuario = usuarioLogado.getId();
		
		mod.addAttribute("saldoCredor", usuarioLogado.getSaldoCredor());
		model.addObject("anuncios", anuncioRep.findAll());
		model.addObject("idUsuario", idUsuario);

		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		Date aux = formato.parse(data);
		model.addObject("anuncios", anuncioService.getAnuncioRepository().getAnuncioByDataDeCriacao(aux));

		model.setViewName("user/listar_anuncios");

		return model;
	}

	public Long getIdUsuario() {
		Usuario usuarioLogado = usuarioServiceImpl.usuarioLogadoEmail();
		return usuarioLogado.getId();
	}

	@RequestMapping(value = "/user/anuncio/comprado/{id}", method = RequestMethod.GET)
	public ModelAndView comprarAnuncio(@PathVariable Long id) {
		anuncioService.compraAnuncio(id);

		return new ModelAndView("redirect:/user/listar/anuncios");
	}

}
