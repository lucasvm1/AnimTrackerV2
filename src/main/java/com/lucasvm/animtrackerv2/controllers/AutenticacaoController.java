package com.lucasvm.animtrackerv2.controllers;

import com.lucasvm.animtrackerv2.dtos.UsuarioDTO;
import com.lucasvm.animtrackerv2.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.UUID;

@Controller
public class AutenticacaoController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String home(Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        try {
            usuarioService.getUsuarioAutenticado(principal);
            return "redirect:/dashboard";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    @GetMapping("/login")
    public String paginaLogin(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("erro", "Email ou senha inválidos.");
        }
        return "views/autenticacao/login";
    }

    @GetMapping("/registro")
    public String paginaRegistro() {
        return "views/autenticacao/registro";
    }

    @PostMapping("/registro/processar")
    public String processarRegistro(
            @RequestParam String nome,
            @RequestParam String email,
            @RequestParam String senha,
            @RequestParam(required = false) String data_nascimento,
            RedirectAttributes redirectAttributes) {

        try {
            if (usuarioService.existsByEmail(email)) {
                redirectAttributes.addFlashAttribute("erro", "Este email já está em uso.");
                return "redirect:/registro";
            }

            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setNome(nome);
            usuarioDTO.setEmail(email);
            usuarioDTO.setSenha(senha);

            if (data_nascimento != null && !data_nascimento.isEmpty()) {
                usuarioDTO.setData_nascimento(LocalDate.parse(data_nascimento));
            }

            usuarioDTO.setStatus("ATIVO");
            usuarioDTO.setAuth_provider("Local");

            usuarioService.salvar(usuarioDTO);

            redirectAttributes.addFlashAttribute("sucesso", "Conta criada com sucesso! Faça login para continuar.");
            return "redirect:/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao criar conta: " + e.getMessage());
            return "redirect:/registro";
        }
    }

    @GetMapping("/termos")
    public String paginaTermos() {
        return "views/autenticacao/termos";
    }

    @GetMapping("/privacidade")
    public String paginaPrivacidade() {
        return "views/autenticacao/privacidade";
    }
}