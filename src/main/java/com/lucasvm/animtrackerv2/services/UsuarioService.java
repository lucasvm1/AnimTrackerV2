package com.lucasvm.animtrackerv2.services;

import com.lucasvm.animtrackerv2.models.UsuarioModel;
import com.lucasvm.animtrackerv2.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UsuarioModel usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .roles("USER")
                .build();
    }

    public UsuarioModel salvarUsuario(UsuarioModel usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        return usuarioRepository.save(usuario);

    }

    public UsuarioModel getUsuarioAutenticado(Principal principal) {
        return usuarioRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    public UsuarioModel validateUsuarioAutenticado(Principal principal, UUID usuarioId) throws AccessDeniedException {
        UsuarioModel usuarioAutenticado = usuarioRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        if (!usuarioAutenticado.getId().equals(usuarioId)) {
            throw new AccessDeniedException("Você não tem permissão para realizar esta operação");
        }

        return usuarioAutenticado;
    }



}
