package io.github.animeshxd.springsecurity.services;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOuth2UserService extends DefaultOAuth2UserService{

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User loadUser = super.loadUser(userRequest);
        String name = loadUser.getAttribute("name");
        System.out.println(name);
        return loadUser;
    }

}