package br.edu.ufcg.computacao.si1.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ufcg.computacao.si1.model.Usuario;

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
        
        model.setViewName("user/index");

        return model;
    }

    @RequestMapping(value = "/company", method = RequestMethod.GET)
    public ModelAndView getPageIndexCompany(){
        ModelAndView model = new ModelAndView();
        
        UsuarioController uc = new UsuarioController();
        String usuario = uc.getUsuario().getN();
        System.out.println(usuario);
        
        model.setViewName("company/index");

        return model;
    }
}
