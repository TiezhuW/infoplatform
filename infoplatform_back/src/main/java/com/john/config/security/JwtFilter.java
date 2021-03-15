package com.john.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.john.entity.Result;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public class JwtFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String jwtToken = req.getHeader("Authorization");

        if (jwtToken == null || !jwtToken.startsWith("Bearer ")) {
            SecurityContextHolder.clearContext();
            chain.doFilter(request, response);
            return;
        }

        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey("john")
                    .parseClaimsJws(jwtToken.replace("Bearer ", ""))
                    .getBody();
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            claims = null;
        }

        if (claims != null) {
            String username = claims.getSubject();
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        chain.doFilter(request, response);
    }
}
