package uz.hojiakbar.child_tracking.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
 import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
 import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.hojiakbar.child_tracking.service.impl.UserDetailsServiceImpl;
import uz.hojiakbar.child_tracking.util.JwtUtils;

import java.io.IOException;



@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");



    /// -------------------------------------------
        System.out.println("=== JWT FILTER ===");
        System.out.println("URI: " + request.getRequestURI());
        System.out.println("Auth header: " + authHeader);

        /// --------------------------------

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("Token: " + token);

            try {
                String username = jwtUtils.getEmailFromToken(token);
                System.out.println("Username: " + username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    System.out.println("User details: " + userDetails);

                    if (jwtUtils.validateToken(token)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        System.out.println("Auth SET: " + SecurityContextHolder.getContext().getAuthentication());
                    }
                }
            } catch (Exception e) {
                System.out.println("=== EXCEPTION ===");
                System.out.println("Type: " + e.getClass().getName());
                System.out.println("Message: " + e.getMessage());
                e.printStackTrace();
            }
        }

        filterChain.doFilter(request, response);
        System.out.println("Final auth: " + SecurityContextHolder.getContext().getAuthentication());

    }
}