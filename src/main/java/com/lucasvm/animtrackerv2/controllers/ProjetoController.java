package com.lucasvm.animtrackerv2.controllers;

import com.lucasvm.animtrackerv2.dtos.ProjetoDTO;
import com.lucasvm.animtrackerv2.models.ProjetoModel;
import com.lucasvm.animtrackerv2.repositories.ProjetoRepository;
import com.lucasvm.animtrackerv2.services.ProjetoService;
import com.lucasvm.animtrackerv2.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProjetoRepository projetoRepository;

    @GetMapping("/projetos")
    public String paginaProjetos(Principal principal, Model model) throws AccessDeniedException {

        var usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);

        usuarioAutenticado = usuarioService.validateUsuarioAutenticado(principal, usuarioAutenticado.getId());

        if (usuarioAutenticado == null) {
            return "redirect:/login";
        }

        List<ProjetoModel.statusProjeto> statusProjetosOrdenados = List.of(
                ProjetoModel.statusProjeto.EM_ANDAMENTO,
                ProjetoModel.statusProjeto.NAO_INICIADO,
                ProjetoModel.statusProjeto.CONCLUIDO,
                ProjetoModel.statusProjeto.CANCELADO
        );
        model.addAttribute("statusProjetosOrdenados", statusProjetosOrdenados);

        List<ProjetoModel.tipoAnimacao> tipoAnimacao = List.of(ProjetoModel.tipoAnimacao.values());
        model.addAttribute("tipoAnimacao", tipoAnimacao);

        List<ProjetoDTO> projetos = projetoService.listarTodosPorUsuario(usuarioAutenticado.getId());
        model.addAttribute("projetos", projetos);

        Map<ProjetoModel.statusProjeto, List<ProjetoDTO>> projetosPorStatus = statusProjetosOrdenados.stream()
                .collect(Collectors.toMap(
                        status -> status,
                        status -> projetos.stream()
                                .filter(p -> p.getStatus().equals(status))
                                .collect(Collectors.toList()),
                        (a, b) -> b,
                        LinkedHashMap::new
                ));

        model.addAttribute("projetosPorStatus", projetosPorStatus);
        model.addAttribute("usuario", usuarioAutenticado);

        return "views/projetos/principal";
    }

    @GetMapping("/projetos/criar")
    public String paginaCriarProjeto(Principal principal, Model model) throws AccessDeniedException {

        var usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);

        usuarioAutenticado = usuarioService.validateUsuarioAutenticado(principal, usuarioAutenticado.getId());

        if (usuarioAutenticado == null) {
            return "redirect:/login";
        }

        List<ProjetoModel.tipoAnimacao> tipoAnimacao = List.of(ProjetoModel.tipoAnimacao.values());
        model.addAttribute("tipoAnimacao", tipoAnimacao);

        List<ProjetoModel.statusProjeto> statusProjeto = List.of(ProjetoModel.statusProjeto.values());
        model.addAttribute("statusProjeto", statusProjeto);

        model.addAttribute("usuario", usuarioAutenticado);

        return "views/projetos/criar";
    }

    @PostMapping("/projetos/criar")
    public String criarProjeto(@ModelAttribute ProjetoDTO projetoDTO, Principal principal) throws AccessDeniedException {

        var usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);

        usuarioAutenticado = usuarioService.validateUsuarioAutenticado(principal, usuarioAutenticado.getId());

        if (usuarioAutenticado == null) {
            return "redirect:/login";
        }

        projetoService.salvar(projetoDTO, usuarioAutenticado.getId());


        return "redirect:/projetos";
    }

    @GetMapping("/projeto/{id}")
    public String paginaProjeto(Model model, Principal principal, @PathVariable UUID id) throws AccessDeniedException {

        var usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);

        usuarioAutenticado = usuarioService.validateUsuarioAutenticado(principal, usuarioAutenticado.getId());

        if (usuarioAutenticado == null) {
            return "redirect:/login";
        }

        Optional<ProjetoModel> projetoOpt = projetoRepository.findByIdAndClienteUsuarioId(id, usuarioAutenticado.getId());

        if (projetoOpt.isPresent()) {
            model.addAttribute("projeto", projetoOpt.get());
            model.addAttribute("usuario", usuarioAutenticado);
            return "views/projetos/detalhes";
        } else {
            return "views/projetos/nao-encontrado";
        }
    }

    @GetMapping("/projeto/editar/{id}")
    public String paginaEditarProjeto(Model model, Principal principal, @PathVariable UUID id) throws AccessDeniedException {

        var usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);

        usuarioAutenticado = usuarioService.validateUsuarioAutenticado(principal, usuarioAutenticado.getId());

        if (usuarioAutenticado == null) {
            return "redirect:/login";
        }

        Optional<ProjetoModel> projetoOpt = projetoRepository.findByIdAndClienteUsuarioId(id, usuarioAutenticado.getId());

        if (projetoOpt.isPresent()) {
            ProjetoModel projeto = projetoOpt.get();
            model.addAttribute("projeto", projeto);

            if (projeto.getStatus() != null) {
                model.addAttribute("selectedStatusProjeto", projeto.getStatus().getDisplayName());
            }

            if (projeto.getTipo_animacao() != null) {
                model.addAttribute("selectedTipoAnimacao", projeto.getTipo_animacao().getDisplayName());
            }

            List<ProjetoModel.tipoAnimacao> tipoAnimacao = List.of(ProjetoModel.tipoAnimacao.values());
            model.addAttribute("tipoAnimacao", tipoAnimacao);

            List<ProjetoModel.statusProjeto> statusProjeto = List.of(ProjetoModel.statusProjeto.values());
            model.addAttribute("statusProjeto", statusProjeto);

            model.addAttribute("usuario", usuarioAutenticado);

            return "views/projetos/editar";
        } else {
            return "views/projetos/nao-encontrado";
        }
    }

    @PostMapping("/projeto/editar/{id}")
    public String editarProjeto(@PathVariable UUID id, @ModelAttribute ProjetoDTO projetoDTO, Principal principal) throws AccessDeniedException {

        var usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);

        usuarioAutenticado = usuarioService.validateUsuarioAutenticado(principal, usuarioAutenticado.getId());

        if (usuarioAutenticado == null) {
            return "redirect:/login";
        }

        Optional<ProjetoModel> projetoOpt = projetoRepository.findByIdAndClienteUsuarioId(id, usuarioAutenticado.getId());

        if (projetoOpt.isPresent()) {
            projetoService.atualizar(id, projetoDTO, usuarioAutenticado.getId());
            return "redirect:/projeto/" + id;
        } else {
            return "/views/projetos/nao-encontrado";
        }

    }

    @PostMapping("/projeto/remover/{id}")
    public String removerProjeto(@PathVariable UUID id, Principal principal) throws AccessDeniedException {

        var usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);

        usuarioAutenticado = usuarioService.validateUsuarioAutenticado(principal, usuarioAutenticado.getId());

        if (usuarioAutenticado == null) {
            return "redirect:/login";
        }

        Optional<ProjetoModel> projetoOpt = projetoRepository.findByIdAndClienteUsuarioId(id, usuarioAutenticado.getId());

        if (projetoOpt.isPresent()) {
            projetoService.remover(id, usuarioAutenticado.getId());
            return "redirect:/projetos";
        } else {
            return "/views/projetos/nao-encontrado";
        }

    }

}
