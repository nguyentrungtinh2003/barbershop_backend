package com.TrungTinhBackend.barbershop_backend.Controller;

import com.TrungTinhBackend.barbershop_backend.DTO.LoginDTO;
import com.TrungTinhBackend.barbershop_backend.DTO.RegisterDTO;
import com.TrungTinhBackend.barbershop_backend.DTO.ResetPasswordDTO;
import com.TrungTinhBackend.barbershop_backend.DTO.UserDTO;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import com.TrungTinhBackend.barbershop_backend.Service.User.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class UsersController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<APIResponse> register(@RequestPart(name = "user") RegisterDTO registerDTO,
                                                @RequestPart(name = "img", required = false) MultipartFile img) throws IOException {
        return ResponseEntity.ok(userService.register(registerDTO, img));
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response, HttpServletRequest request) {
        return ResponseEntity.ok(userService.login(loginDTO, response, request));
    }

    @PostMapping("customer/page")
    public ResponseEntity<APIResponse> getUsersByPage(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "6") int size) {
        return ResponseEntity.ok(userService.getUserByPage(page,size));
    }

    @PostMapping("customer/{id}")
    public ResponseEntity<APIResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("customer/update/{id}")
    public ResponseEntity<APIResponse> updateUser(@PathVariable Long id, @RequestPart(name = "user") UserDTO userDTO, @RequestPart(name = "img",required = false) MultipartFile img) throws IOException {
        return ResponseEntity.ok(userService.updateUser(id, userDTO, img));
    }

    @DeleteMapping("customer/delete/{id}")
    public ResponseEntity<APIResponse> deleteUser(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @PutMapping("customer/restore/{id}")
    public ResponseEntity<APIResponse> restoreUser(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(userService.restoreUser(id));
    }

    @GetMapping("customer/search")
    public ResponseEntity<APIResponse> searchUser(@RequestParam(name = "keyword") String keyword,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "6") int size) throws IOException {
        return ResponseEntity.ok(userService.searchUser(keyword, page, size));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<APIResponse> forgotPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) throws Exception {
        return ResponseEntity.ok(userService.sendOtpToEmail(resetPasswordDTO.getEmail()));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<APIResponse> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) throws Exception {
        return ResponseEntity.ok(userService.verifyOtpAndChangePassword(resetPasswordDTO));
    }

    @GetMapping("/logout/{id}")
    public ResponseEntity<APIResponse> logout(@PathVariable Long id, HttpServletResponse response) throws Exception {
        return ResponseEntity.ok(userService.logout(id,response));
    }
}
