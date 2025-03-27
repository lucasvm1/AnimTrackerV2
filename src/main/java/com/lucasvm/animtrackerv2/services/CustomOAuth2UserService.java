package com.lucasvm.animtrackerv2.services;

import com.lucasvm.animtrackerv2.models.UsuarioModel;
import com.lucasvm.animtrackerv2.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomOAuth2UserService extends org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");

        Optional<UsuarioModel> usuarioOptional = usuarioRepository.findByEmail(email);

        UsuarioModel usuario;
        if (usuarioOptional.isPresent()) {
            usuario = usuarioOptional.get();
        } else {
            usuario = new UsuarioModel();
            usuario.setEmail(email);
            String senhaAleatoria = passwordEncoder.encode("GoogleAuth-" + System.currentTimeMillis());
            usuario.setSenha(senhaAleatoria);
            usuario.setAuth_provider("Google");
            usuarioRepository.save(usuario);
        }

        return oAuth2User;
    }

}
