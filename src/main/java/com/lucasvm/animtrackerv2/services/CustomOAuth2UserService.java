package com.lucasvm.animtrackerv2.services;

import com.lucasvm.animtrackerv2.models.UsuarioModel;
import com.lucasvm.animtrackerv2.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            OAuth2User oAuth2User = super.loadUser(userRequest);
            Map<String, Object> attributes = oAuth2User.getAttributes();
            String nome = (String) attributes.get("name");
            String email = (String) attributes.get("email");

            Optional<UsuarioModel> usuarioOptional = usuarioRepository.findByEmail(email);

            UsuarioModel usuario;
            if (usuarioOptional.isPresent()) {
                usuario = usuarioOptional.get();
            } else {
                usuario = new UsuarioModel();
                usuario.setNome(nome);
                usuario.setEmail(email);
                String senhaAleatoria = passwordEncoder.encode("GoogleAuth-" + System.currentTimeMillis());
                usuario.setSenha(senhaAleatoria);
                usuario.setAuth_provider("Google");
                usuarioRepository.save(usuario);
            }

            // Criar um novo mapa de atributos para incluir o ID do usuário
            Map<String, Object> customAttributes = new HashMap<>(attributes);
            customAttributes.put("id", usuario.getId()); // Assumindo que há um getter getId()

            // Retornar um novo OAuth2User com as informações do usuário do banco de dados
            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                    customAttributes,
                    "email" // A chave que será usada como nameAttributeKey
            );

        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }
}