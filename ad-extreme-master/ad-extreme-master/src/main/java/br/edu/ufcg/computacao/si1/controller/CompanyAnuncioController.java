package br.edu.ufcg.computacao.si1.controller;

import br.edu.ufcg.computacao.si1.model.Anuncio;
import br.edu.ufcg.computacao.si1.model.Usuario;
import br.edu.ufcg.computacao.si1.model.form.AnuncioForm;
import br.edu.ufcg.computacao.si1.repository.UsuarioRepository;
import br.edu.ufcg.computacao.si1.service.AnuncioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
public class CompanyAnuncioController {
	
    @Autowired
    private AnuncioServiceImpl anuncioService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @RequestMapping(value = "/company/cadastrar/anuncio", method = RequestMethod.GET)
    public ModelAndView getPageCadastarAnuncio(AnuncioForm anuncioForm){
        ModelAndView model = new ModelAndView();

        model.addObject("tipos", anuncioForm.getTipos());
        model.setViewName("company/cadastrar_anuncio");

        return model;
    }

    @RequestMapping(value = "/company/listar/anuncios", method = RequestMethod.GET)
    public ModelAndView getPageListarAnuncios(){
        ModelAndView model = new ModelAndView();

        model.addObject("anuncios", anuncioService.getAnuncioRepository().findAll());

        model.setViewName("company/listar_anuncios");

        return model;
    }

    @RequestMapping(value = "/company/cadastrar/anuncio", method = RequestMethod.POST)
    public ModelAndView cadastroAnuncio(@Valid AnuncioForm anuncioForm, BindingResult result, RedirectAttributes attributes){
    	
    	if(result.hasErrors()){
            return getPageCadastarAnuncio(anuncioForm);
        }

        Anuncio anuncio = new Anuncio();
        anuncio.setTitulo(anuncioForm.getTitulo());
        anuncio.setPreco(anuncioForm.getPreco());
        anuncio.setTipo(anuncioForm.getTipo());
        anuncio.setIdUsuario(getIdUsuario());

        anuncioService.create(anuncio);

        attributes.addFlashAttribute("mensagem", "Anúncio cadastrado com sucesso!");
        return new ModelAndView("redirect:/company/cadastrar/anuncio");
    }
    
    @RequestMapping(value = "/company/listar/anuncios/{tipo}", method = RequestMethod.GET)
    public ModelAndView buscarAnunciosPorTipo(@PathVariable String tipo){
        ModelAndView model = new ModelAndView();

        model.addObject("anuncios", anuncioService.getAnuncioRepository().getAnuncioByTipo(tipo));

        model.setViewName("company/listar_anuncios");

        return model;
    }
    
    @RequestMapping(value = "/company/listar/anuncios/data", method = RequestMethod.GET)
    public ModelAndView buscarAnunciosPorData(@RequestParam("data") String data) throws ParseException{
    	ModelAndView model = new ModelAndView();
    	SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
    	Date aux = formato.parse(data);
        model.addObject("anuncios", anuncioService.getAnuncioRepository().getAnuncioByDataDeCriacao(aux));

        model.setViewName("company/listar_anuncios");

        return model;
    }
    
    // Listar anuncio por usuario
    @RequestMapping(value = "/company/listar/anuncios/usuario", method = RequestMethod.GET)
    public ModelAndView buscarAnunciosPorUsuario(){
        ModelAndView model = new ModelAndView();

        model.addObject("anuncios", anuncioService.getAnuncioRepository().getAnuncioByIdUsuario(getIdUsuario()));

        model.setViewName("company/listar_anuncios");

        return model;
    }
    
    public Long getIdUsuario(){
    	UsuarioController uc = new UsuarioController();
        Usuario usuario = usuarioRepository.findByEmail(uc.getUsuario().getEmail());
        return usuario.getId();
    }


}
