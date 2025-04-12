package com.lucasvm.animtrackerv2.controllers;

import com.lucasvm.animtrackerv2.dtos.CenaDTO;
import com.lucasvm.animtrackerv2.models.CenaModel;
import com.lucasvm.animtrackerv2.repositories.CenaRepository;
import com.lucasvm.animtrackerv2.services.CenaService;
import com.lucasvm.animtrackerv2.services.ProjetoService;
import com.lucasvm.animtrackerv2.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class CenaController {

    @Autowired
    private CenaService cenaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProjetoService projetoService;
    @Autowired
    private CenaRepository cenaRepository;

    @GetMapping("/cenas")
    public String paginaCenas(Principal principal, Model model) throws AccessDeniedException {

        var usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);

        usuarioAutenticado = usuarioService.validateUsuarioAutenticado(principal, usuarioAutenticado.getId());

        if(usuarioAutenticado == null) {
            return "redirect:/login";
        }

        List<CenaModel.StatusCena> statusCenasOrdenados = List.of(
                CenaModel.StatusCena.EM_PRODUCAO,
                CenaModel.StatusCena.NAO_INICIADA,
                CenaModel.StatusCena.PAUSADA,
                CenaModel.StatusCena.APROVADA
        );

        model.addAttribute("statusCenasOrdenados", statusCenasOrdenados);

        List<CenaModel.EstagioCena> estagioCenas = List.of(CenaModel.EstagioCena.values());
        model.addAttribute("estagioCenas", estagioCenas);

        List<CenaDTO> cenas = cenaService.listarTodosPorUsuario(usuarioAutenticado.getId());

        Map<CenaModel.StatusCena, List<CenaDTO>> cenasPorStatus = statusCenasOrdenados.stream()
                .collect(Collectors.toMap(
                        status -> status,
                        status -> cenas.stream()
                                .filter(c -> c.getStatus().equals(status))
                                .collect(Collectors.toList()),
                        (a, b) -> b,
                        LinkedHashMap::new
                ));

        model.addAttribute("cenasPorStatus", cenasPorStatus);

        return "viewa/cenas/principal";

    }

    @GetMapping("/cenas/criar")
    public String paginaCriarCena(Principal principal, Model model) throws AccessDeniedException {

        var usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);

        usuarioAutenticado = usuarioService.validateUsuarioAutenticado(principal, usuarioAutenticado.getId());

        if(usuarioAutenticado == null) {
            return "redirect:/login";
        }

        List<CenaModel.EstagioCena> estagioCenas = List.of(CenaModel.EstagioCena.values());
        model.addAttribute("estagioCenas", estagioCenas);

        List<CenaModel.StatusCena> statusCenas = List.of(CenaModel.StatusCena.values());
        model.addAttribute("statusCenas", statusCenas);

        return "views/cenas/criar";
    }

    @PostMapping("/cenas/criar")
    public String criarCena(Principal principal, Model model, @ModelAttribute CenaDTO cenaDTO) throws AccessDeniedException {

        var usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);

        usuarioAutenticado = usuarioService.validateUsuarioAutenticado(principal, usuarioAutenticado.getId());

        if(usuarioAutenticado == null) {
            return "redirect:/login";
        }

        cenaService.salvar(cenaDTO, usuarioAutenticado.getId());

        return "redirect:/cenas";
    }

    @GetMapping("/cena/{id}")
    public String paginaCena(Principal principal, Model model, @PathVariable UUID id) throws AccessDeniedException {

        var usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);

        usuarioAutenticado = usuarioService.validateUsuarioAutenticado(principal, usuarioAutenticado.getId());

        if(usuarioAutenticado == null) {
            return "redirect:/login";
        }

        Optional<CenaModel> cenaOpt = cenaRepository.findByIdAndProjetoClienteUsuarioId(id, usuarioAutenticado.getId());

        if(cenaOpt.isPresent()) {
            model.addAttribute("cena", cenaOpt.get());
            return "views/cenas/detalhes";
        } else {
            return "views/cenas/nao_encontrada";
        }
    }

    @GetMapping("/cena/editar/{id}")
    public String paginaEditarCena(Principal principal, Model model, @PathVariable UUID id) throws AccessDeniedException {

        var usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);

        usuarioAutenticado = usuarioService.validateUsuarioAutenticado(principal, usuarioAutenticado.getId());

        if(usuarioAutenticado == null) {
            return "redirect:/login";
        }

        Optional<CenaModel> cenaOpt = cenaRepository.findByIdAndProjetoClienteUsuarioId(id, usuarioAutenticado.getId());

        if(cenaOpt.isPresent()) {
            CenaModel cena = cenaOpt.get();
            model.addAttribute("cena", cena);

            if(cena.getStatus() != null) {
                model.addAttribute("status", cena.getStatus().getDisplayName());
            }

            if(cena.getEstagio() != null) {
                model.addAttribute("estagio", cena.getEstagio().getDisplayName());
            }

            List<CenaModel.EstagioCena> estagioCenas = List.of(CenaModel.EstagioCena.values());
            model.addAttribute("estagioCenas", estagioCenas);

            List<CenaModel.StatusCena> statusCenas = List.of(CenaModel.StatusCena.values());
            model.addAttribute("statusCenas", statusCenas);

            return "views/cenas/editar";
        } else {
            return "views/cenas/nao_encontrada";
        }
    }

    @PostMapping("/cena/editar/{id}")
    public String editarCena(Principal principal, @PathVariable UUID id, @ModelAttribute CenaDTO cenaDTO) throws AccessDeniedException {

        var usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);

        usuarioAutenticado = usuarioService.validateUsuarioAutenticado(principal, usuarioAutenticado.getId());

        if(usuarioAutenticado == null) {
            return "redirect:/login";
        }

        Optional<CenaModel> cenaOpt = cenaRepository.findByIdAndProjetoClienteUsuarioId(id, usuarioAutenticado.getId());

        if(cenaOpt.isPresent()) {
            cenaService.atualizar(id, cenaDTO, usuarioAutenticado.getId());
            return "redirect:/cena/" + id;
        } else {
            return "views/cenas/nao_encontrada";
        }
    }

    @PostMapping("/cena/remover/{id}")
    public String removerCena(Principal principal, @PathVariable UUID id) throws AccessDeniedException {

        var usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);

        usuarioAutenticado = usuarioService.validateUsuarioAutenticado(principal, usuarioAutenticado.getId());

        if(usuarioAutenticado == null) {
            return "redirect:/login";
        }

        Optional<CenaModel> cenaOpt = cenaRepository.findByIdAndProjetoClienteUsuarioId(id, usuarioAutenticado.getId());

        if(cenaOpt.isPresent()) {
            cenaService.remover(id, usuarioAutenticado.getId());
            return "redirect:/cenas";
        } else {
            return "views/cenas/nao_encontrada";
        }
    }

}
