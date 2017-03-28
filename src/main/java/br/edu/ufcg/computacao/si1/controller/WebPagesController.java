package br.edu.ufcg.computacao.si1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ufcg.computacao.si1.model.Usuario;
import br.edu.ufcg.computacao.si1.repository.UsuarioRepository;

@Controller
public class WebPagesController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView getPageIndex() {
		ModelAndView model = new ModelAndView();
		model.setViewName("index");

		return model;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView getPageLogin() {
		ModelAndView model = new ModelAndView();
		model.setViewName("login");

		return model;
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ModelAndView getPageIndexUser(Model mod) {
		UsuarioController uc = new UsuarioController();

		Usuario usuarioLogado = usuarioRepository.findByEmail(uc.getUsuario().getEmail());
		mod.addAttribute("saldoCredor", usuarioLogado.getSaldoCredor());

		ModelAndView model = new ModelAndView();

		mod.addAttribute("user", usuarioLogado.getNome());
		model.setViewName("user/index");

		return model;
	}

	@RequestMapping(value = "/company", method = RequestMethod.GET)
	public ModelAndView getPageIndexCompany(Model mod) {
		UsuarioController uc = new UsuarioController();

		Usuario usuarioLogado = usuarioRepository.findByEmail(uc.getUsuario().getEmail());
		mod.addAttribute("saldoCredor", usuarioLogado.getSaldoCredor());
		
		ModelAndView model = new ModelAndView();
		mod.addAttribute("user", usuarioLogado.getNome());
		model.setViewName("company/index");

		return model;
	}

}
