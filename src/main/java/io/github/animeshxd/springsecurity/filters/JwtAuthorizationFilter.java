package io.github.animeshxd.springsecurity.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.animeshxd.springsecurity.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    List<String> allowedUrls = new ArrayList<>();

    public JwtAuthorizationFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    public JwtAuthorizationFilter setAllowedUrls(String ...allowedUrls) {
        this.allowedUrls = List.of(allowedUrls);
        return this;
    }
    public final JwtUtils jwtUtils;
    public final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean canIgnore = allowedUrls.stream().anyMatch(s -> s.equals(request.getServletPath()));
        if (canIgnore){
            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader == null) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return;
        }
        if (!authHeader.startsWith("Bearer ")){
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return;
        }
        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() == null){
            String jws = authHeader.substring(7);
            try {
                UserDetails user = userDetailsService.loadUserByUsername(jwtUtils.getClaims(jws).getSubject());
                context.setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
                filterChain.doFilter(request, response);
                return;
            }catch (UsernameNotFoundException e){
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }catch (Exception e){
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                new ObjectMapper().writeValue(response.getOutputStream(), Map.of("error", e.getMessage()));
                return;
            }
        }
        filterChain.doFilter(request, response);

    }
}
