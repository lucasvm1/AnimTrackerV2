package com.lucasvm.animtrackerv2.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucasvm.animtrackerv2.models.UsuarioModel;
import com.lucasvm.animtrackerv2.repositories.UsuarioRepository;
import com.lucasvm.animtrackerv2.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private void adicionarAtributosNoModelo(Model model, UsuarioModel usuario) {
        model.addAttribute("usuario", usuario);
        model.addAllAttributes(getUsuarioComoMapa(usuario));
    }

    private Map<String, Object> getUsuarioComoMapa(UsuarioModel usuario) {
        return objectMapper.convertValue(usuario, Map.class);
    }

    @GetMapping("/dashboard")
    public String paginaDashboard(Principal principal, Model model) {
        try {
            UsuarioModel usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);
            adicionarAtributosNoModelo(model, usuarioAutenticado);
            return "views/dashboard/principal";
        } catch (UsernameNotFoundException e) {
            return "redirect:/login";
        }
    }


}
