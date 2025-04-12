package com.lucasvm.animtrackerv2.controllers;

import com.lucasvm.animtrackerv2.dtos.ClienteDTO;
import com.lucasvm.animtrackerv2.models.ClienteModel;
import com.lucasvm.animtrackerv2.repositories.ClienteRepository;
import com.lucasvm.animtrackerv2.services.ClienteService;
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
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/clientes")
    public String paginaClientes(Principal principal, Model model) throws AccessDeniedException {

        var usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);

        usuarioAutenticado = usuarioService.validateUsuarioAutenticado(principal, usuarioAutenticado.getId());

        if (usuarioAutenticado == null) {
            return "redirect:/login";
        }

        List<ClienteDTO> clientes = clienteService.listarTodosPorUsuario(usuarioAutenticado.getId());

        List<ClienteDTO> clientesOrdenados = clientes.stream()
                .sorted(Comparator.comparing(c -> c.getNome().toLowerCase()))
                .toList();

        Map<String, List<ClienteDTO>> clientesPorLetra = clientesOrdenados.stream()
                .filter(c -> c.getNome() != null && !c.getNome().isEmpty())
                .collect(Collectors.groupingBy(c -> {
                    char firstChar = c.getNome().charAt(0);
                    return Character.isLetter(firstChar)
                            ? String.valueOf(Character.toUpperCase(firstChar))
                            : "#";
                }, LinkedHashMap::new, Collectors.toList()));

        clientesPorLetra.computeIfPresent("#", (k, v) -> v.isEmpty() ? null : v);

        List<String> letrasDisponiveis = clientesPorLetra.keySet().stream()
                .sorted((a, b) -> {
                    if (a.equals("#")) return 1;
                    if (b.equals("#")) return -1;
                    return a.compareTo(b);
                })
                .toList();

        model.addAttribute("clientes", clientesOrdenados);
        model.addAttribute("clientesPorLetra", clientesPorLetra);
        model.addAttribute("letrasDisponiveis", letrasDisponiveis);

        model.addAttribute("clientesTipos", List.of(ClienteModel.TipoCliente.values()));
        model.addAttribute("clientesCategorias", List.of(ClienteModel.CategoriaCliente.values()));

        return "views/clientes/principal";
    }

    @GetMapping("/cliente/criar")
    public String paginaCriarCliente(Model model, Principal principal) throws AccessDeniedException {

        var usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);

        usuarioAutenticado = usuarioService.validateUsuarioAutenticado(principal, usuarioAutenticado.getId());

        if (usuarioAutenticado == null) {
            return "redirect:/login";
        }

        List<ClienteModel.TipoCliente> tiposClientes = Arrays.asList(ClienteModel.TipoCliente.values());
        model.addAttribute("tiposClientes", tiposClientes);

        List<ClienteModel.CategoriaCliente> clientesCategorias = Arrays.asList(ClienteModel.CategoriaCliente.values());
        model.addAttribute("clientesCategorias", clientesCategorias);

        return "views/clientes/criar";
    }

    @PostMapping("/cliente/criar")
    public String criarCliente(@ModelAttribute ClienteDTO clienteDTO, Principal principal) throws AccessDeniedException {
        var usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);

        usuarioAutenticado = usuarioService.validateUsuarioAutenticado(principal, usuarioAutenticado.getId());

        if (usuarioAutenticado == null) {
            return "redirect:/login";
        }

        clienteService.salvar(clienteDTO, usuarioAutenticado.getId());

        return "redirect:/clientes";
    }

    @GetMapping("/cliente/{id}")
    public String paginaCliente(Model model, Principal principal, @PathVariable UUID id) throws AccessDeniedException {
        var usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);

        usuarioAutenticado = usuarioService.validateUsuarioAutenticado(principal, usuarioAutenticado.getId());

        if (usuarioAutenticado == null) {
            return "redirect:/login";
        }

        Optional<ClienteModel> clienteOpt = clienteRepository.findByIdAndUsuarioId(id, usuarioAutenticado.getId());

        if (clienteOpt.isPresent()) {
            model.addAttribute("cliente", clienteOpt.get());
            return "views/clientes/detalhes";
        } else {
            return "views/clientes/nao-encontrado";
        }
    }

    @GetMapping("/cliente/editar/{id}")
    public String paginaEditarCliente(Principal principal, @PathVariable UUID id, Model model) throws AccessDeniedException {
        var usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);

        usuarioAutenticado = usuarioService.validateUsuarioAutenticado(principal, usuarioAutenticado.getId());

        if (usuarioAutenticado == null) {
            return "redirect:/login";
        }

        Optional<ClienteModel> clienteOpt = clienteRepository.findByIdAndUsuarioId(id, usuarioAutenticado.getId());

        if (clienteOpt.isPresent()) {

            var cliente = clienteOpt.get();

            model.addAttribute("cliente", cliente);

            if (cliente.getTipo() != null) {
                model.addAttribute("selectedTipoCliente", cliente.getTipo().name());
            }

            if (cliente.getCategoria() != null) {
                model.addAttribute("selectedCategoria", cliente.getCategoria().name());
            }

            List<ClienteModel.TipoCliente> clientesTipos = Arrays.asList(ClienteModel.TipoCliente.values());
            model.addAttribute("clientesTipos", clientesTipos);

            List<ClienteModel.CategoriaCliente> clientesCategorias = Arrays.asList(ClienteModel.CategoriaCliente.values());
            model.addAttribute("clientesCategorias", clientesCategorias);

            return "views/clientes/editar";
        } else {
            return "views/clientes/nao-encontrado";
        }
    }

    @PostMapping("/cliente/editar/{id}")
    public String editarCliente(@ModelAttribute ClienteDTO clienteDTO, Principal principal, @PathVariable UUID id) throws AccessDeniedException {
        var usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);

        usuarioAutenticado = usuarioService.validateUsuarioAutenticado(principal, usuarioAutenticado.getId());

        if (usuarioAutenticado == null) {
            return "redirect:/login";
        }

        Optional<ClienteModel> clienteOpt = clienteRepository.findByIdAndUsuarioId(id, usuarioAutenticado.getId());

        if (clienteOpt.isPresent()) {
            clienteService.atualizar(id, clienteDTO, usuarioAutenticado.getId());
            return "redirect:/cliente/" + id;
        } else {
            return "views/clientes/nao-encontrado";
        }
    }

    @PostMapping("/cliente/remover/{id}")
    public String removerCliente(@PathVariable UUID id, Principal principal) throws AccessDeniedException {
        var usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);

        usuarioAutenticado = usuarioService.validateUsuarioAutenticado(principal, usuarioAutenticado.getId());

        if (usuarioAutenticado == null) {
            return "redirect:/login";
        }

        Optional<ClienteModel> clienteOpt = clienteRepository.findByIdAndUsuarioId(id, usuarioAutenticado.getId());

        if (clienteOpt.isPresent()) {
            clienteService.remover(id, usuarioAutenticado.getId());
            return "redirect:/clientes";
        } else {
            return "views/clientes/nao-encontrado";
        }
    }

}
