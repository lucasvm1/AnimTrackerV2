package com.lucasvm.animtrackerv2.controllers;

import com.lucasvm.animtrackerv2.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AutenticacaoController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String paginaLogin() {
        return "views/autenticacao/login";
    }




}
