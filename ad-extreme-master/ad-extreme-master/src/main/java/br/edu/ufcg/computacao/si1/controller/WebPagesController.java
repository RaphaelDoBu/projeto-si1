package br.edu.ufcg.computacao.si1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ufcg.computacao.si1.model.Usuario;
import br.edu.ufcg.computacao.si1.model.form.UsuarioForm;
import br.edu.ufcg.computacao.si1.repository.UsuarioRepository;
import br.edu.ufcg.computacao.si1.service.UsuarioServiceImpl;

@Controller
public class WebPagesController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getPageIndex(){
        ModelAndView model = new ModelAndView();
        model.setViewName("index");

        return model;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView getPageLogin(){
        ModelAndView model = new ModelAndView();
        model.setViewName("login");

        return model;
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ModelAndView getPageIndexUser(){
        ModelAndView model = new ModelAndView();
        
        UsuarioController uc = new UsuarioController();
        String usuario = uc.getUsuario().getN();
        
        System.out.println(usuario);
        System.out.println(rep.findByEmail(usuario));
        
        model.setViewName("user/index");

        return model;
    }
    
    @Autowired
    private UsuarioRepository rep;

    @RequestMapping(value = "/company", method = RequestMethod.GET)
    public ModelAndView getPageIndexCompany(){
        ModelAndView model = new ModelAndView();
        
        UsuarioController uc = new UsuarioController();
        String usuario = uc.getUsuario().getN();
        
        System.out.println(usuario);
        System.out.println((Usuario)rep.findByEmail(usuario));
        
        model.setViewName("company/index");

        return model;
    }
}
