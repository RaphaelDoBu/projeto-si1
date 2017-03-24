package br.edu.ufcg.computacao.si1.controller;

import br.edu.ufcg.computacao.si1.model.Anuncio;
import br.edu.ufcg.computacao.si1.model.Usuario;
import br.edu.ufcg.computacao.si1.model.form.AnuncioForm;
import br.edu.ufcg.computacao.si1.repository.AnuncioRepository;
import br.edu.ufcg.computacao.si1.repository.UsuarioRepository;
import br.edu.ufcg.computacao.si1.service.AnuncioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

@Controller
public class An {

    @Autowired
    private AnuncioServiceImpl anuncioService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AnuncioRepository anuncioRep;

    @RequestMapping(value = "/user/cadastrar/anuncio", method = RequestMethod.GET)
    public ModelAndView getPageCadastrarAnuncio(AnuncioForm anuncioForm){
    	UsuarioController uc = new UsuarioController();
		ModelAndView model = new ModelAndView();
		Usuario usuarioLogado = usuarioRepository.findByEmail(uc.getUsuario().getEmail());
		model.addObject("saldoCredor", usuarioLogado.getSaldoCredor());
    	model.addObject("saldoDevedor", usuarioLogado.getSaldoDevedor());
		
		model.addObject("tipos", anuncioForm.getTipos());
		model.setViewName("user/cadastrar_anuncio");

        return model;
    }

    @RequestMapping(value = "/user/listar/anuncios", method = RequestMethod.GET)
    public ModelAndView getPageListarAnuncios(Model mod){
    	UsuarioController uc = new UsuarioController();

		Usuario usuarioLogado = usuarioRepository.findByEmail(uc.getUsuario().getEmail());
		mod.addAttribute("saldoCredor", usuarioLogado.getSaldoCredor());
    	mod.addAttribute("saldoDevedor", usuarioLogado.getSaldoDevedor());
    	Long idUsuario = usuarioLogado.getId();
		ModelAndView model = new ModelAndView();

		model.addObject("anuncios", anuncioRep.findAll());
		model.addObject("idUsuario", idUsuario);
		model.setViewName("user/listar_anuncios");

        return model;
    }

    @RequestMapping(value = "/user/cadastrar/anuncio", method = RequestMethod.POST)
    public ModelAndView cadastroAnuncio(@Valid AnuncioForm anuncioForm, BindingResult result, RedirectAttributes attributes){
    	
    	UsuarioController uc = new UsuarioController();

		if (result.hasErrors()) {
			return getPageCadastrarAnuncio(anuncioForm);
		}

		Anuncio anuncio = new Anuncio();
		anuncio.setTitulo(anuncioForm.getTitulo());
		anuncio.setPreco(anuncioForm.getPreco());
		anuncio.setTipo(anuncioForm.getTipo());

		Usuario usuario = usuarioRepository.findByEmail(uc.getUsuario().getEmail());
		Long idUsuario = usuario.getId();

		anuncio.setIdUsuario(idUsuario);

		anuncioService.create(anuncio);
		
        attributes.addFlashAttribute("mensagem", "Anúncio cadastrado com sucesso!");
        return new ModelAndView("redirect:/user/cadastrar/anuncio");
    }
    
    @RequestMapping(value = "/user/listar/anuncios/usuario", method = RequestMethod.GET)
    public ModelAndView buscarAnunciosPorUsuario(){
        ModelAndView model = new ModelAndView();

        model.addObject("anuncios", anuncioService.getAnuncioRepository().getAnuncioByIdUsuario(getIdUsuario()));

        model.setViewName("user/listar_anuncios");

        return model;
    }
    
    @RequestMapping(value = "/user/listar/anuncios/{tipo}", method = RequestMethod.GET)
    public ModelAndView buscarAnunciosPorTipo(@PathVariable String tipo){
        ModelAndView model = new ModelAndView();

        model.addObject("anuncios", anuncioService.getAnuncioRepository().getAnuncioByTipo(tipo));

        model.setViewName("user/listar_anuncios");

        return model;
    }
    
    @RequestMapping(value = "/user/listar/anuncios/data", method = RequestMethod.GET)
    public ModelAndView buscarAnunciosPorData(@RequestParam("data") String data) throws ParseException{
    	ModelAndView model = new ModelAndView();
    	SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
    	Date aux = formato.parse(data);
        model.addObject("anuncios", anuncioService.getAnuncioRepository().getAnuncioByDataDeCriacao(aux));

        model.setViewName("user/listar_anuncios");

        return model;
    }
    
    public Long getIdUsuario(){
    	UsuarioController uc = new UsuarioController();
        Usuario usuario = usuarioRepository.findByEmail(uc.getUsuario().getEmail());
        return usuario.getId();
    }
    

	@RequestMapping(value = "/user/anuncio/comprado/{id}", method = RequestMethod.GET)
	public ModelAndView comprarAnuncio(@PathVariable Long id, Model model) {
		UsuarioController uc = new UsuarioController();

		Usuario usuarioLogado = usuarioRepository.findByEmail(uc.getUsuario().getEmail());
		Long idUsuario = usuarioLogado.getId();

		Anuncio recuperaAnuncio = anuncioRep.findOne(id);
		// usuario logado compra o anuncio
		usuarioLogado.compraAnuncio(recuperaAnuncio.getPreco());

		// usuario que tem o anuncio vai vender
		Usuario usuarioDonoAnuncio = usuarioRepository.findOne(recuperaAnuncio.getIdUsuario());
		usuarioDonoAnuncio.vendeAnuncio(recuperaAnuncio.getPreco());

		// excluir anuncio
		anuncioRep.delete(id);

		return new ModelAndView("redirect:/user/listar/anuncios");
	}


}
