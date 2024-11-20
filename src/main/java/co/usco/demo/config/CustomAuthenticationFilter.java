package co.usco.demo.config;

import java.io.IOException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/auth/login".equals(path)) {
            String documentType = request.getParameter("documentType");
            if (documentType != null && !documentType.isEmpty()) {
                SecurityContextHolder.getContext().setAuthentication(
                    new PreAuthenticatedAuthenticationToken(documentType, null)
                );
            }
        }
        filterChain.doFilter(request, response);
    }
}
