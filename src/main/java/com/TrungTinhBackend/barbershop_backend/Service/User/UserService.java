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
    APIResponse register(RegisterDTO registerDTO, MultipartFile img) throws IOException;
    APIResponse login(LoginDTO loginDTO, HttpServletResponse response, HttpServletRequest request);
    APIResponse getAllUser();
    APIResponse getUserByPage(int page, int size);
    APIResponse getUserById(Long id);
    APIResponse updateUser(Long id, UserDTO userDTO, MultipartFile img) throws IOException;
    APIResponse deleteUser(Long id);
    APIResponse restoreUser(Long id);
    APIResponse searchUser(String keyword, int page, int size);
    APIResponse sendOtpToEmail(String email) throws Exception;
    APIResponse verifyOtpAndChangePassword(ResetPasswordDTO resetPasswordDTO) throws Exception;
    APIResponse logout(Long id, HttpServletResponse response);
}
