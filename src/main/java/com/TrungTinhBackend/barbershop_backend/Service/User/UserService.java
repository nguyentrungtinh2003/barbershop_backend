package com.TrungTinhBackend.barbershop_backend.Service.User;

import com.TrungTinhBackend.barbershop_backend.DTO.LoginDTO;
import com.TrungTinhBackend.barbershop_backend.DTO.RegisterDTO;
import com.TrungTinhBackend.barbershop_backend.DTO.ResetPasswordDTO;
import com.TrungTinhBackend.barbershop_backend.DTO.UserDTO;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    APIResponse register(RegisterDTO registerDTO);
    APIResponse login(LoginDTO loginDTO, HttpServletResponse response, HttpServletRequest request);
    APIResponse getUserByPage(int page, int size);
    APIResponse getUserById(Long id);
    APIResponse updateUser(Long id, UserDTO userDTO, MultipartFile img) throws IOException;
    APIResponse deleteUser(Long id);
    APIResponse restoreUser(Long id);
    APIResponse sendOtpToEmail(String email) throws Exception;
    APIResponse verifyOtpAndChangePassword(ResetPasswordDTO resetPasswordDTO) throws Exception;
}
