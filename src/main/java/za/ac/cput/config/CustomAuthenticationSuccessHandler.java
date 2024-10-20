package za.ac.cput.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
            // Get the roles of the authenticated user
            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

            // Redirect based on role
            if (roles.contains("ROLE_ADMIN")) {
                response.sendRedirect("/admin-layout"); // Redirect admin users to admin layout
            } else if (roles.contains("ROLE_LANDLORD")) {
                response.sendRedirect("/landlord-layout"); // Redirect landlord users to landlord layout
            } else if (roles.contains("ROLE_STUDENT")) {
                //response.sendRedirect("/student-inbox"); // Redirect students to student inbox
            } else {
                response.sendRedirect("/home"); // Fallback or unauthorized users
            }
        }

}


