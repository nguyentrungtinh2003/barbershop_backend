package com.TrungTinhBackend.barbershop_backend.Service.Google;

import com.TrungTinhBackend.barbershop_backend.DTO.UserDTO;
import com.TrungTinhBackend.barbershop_backend.Entity.Users;
import com.TrungTinhBackend.barbershop_backend.Repository.UsersRepository;
import com.TrungTinhBackend.barbershop_backend.Service.Jwt.JwtUtils;
import com.TrungTinhBackend.barbershop_backend.Service.Jwt.UserDetailsService;
import com.TrungTinhBackend.barbershop_backend.Service.User.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(oAuth2User.getName());
        userDTO.setEmail(oAuth2User.getEmail());
        userDTO.setImg(oAuth2User.getPicture());

        userService.processOAuthPostLogin(userDTO);

        UserDetails userDetails = userDetailsService.loadUserByUsername(userDTO.getUsername());

        Authentication authentication1 = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication1);

        String jwtToken = jwtUtils.generateToken(userDetails);

        response.sendRedirect("http://localhost:5173/oauth2/redirect?token=" + jwtToken);
    }
}
