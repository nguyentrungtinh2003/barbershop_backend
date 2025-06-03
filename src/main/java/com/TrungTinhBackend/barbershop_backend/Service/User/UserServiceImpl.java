package com.TrungTinhBackend.barbershop_backend.Service.User;

import com.TrungTinhBackend.barbershop_backend.DTO.LoginDTO;
import com.TrungTinhBackend.barbershop_backend.DTO.RegisterDTO;
import com.TrungTinhBackend.barbershop_backend.DTO.ResetPasswordDTO;
import com.TrungTinhBackend.barbershop_backend.DTO.UserDTO;
import com.TrungTinhBackend.barbershop_backend.Entity.Users;
import com.TrungTinhBackend.barbershop_backend.Enum.RoleEnum;
import com.TrungTinhBackend.barbershop_backend.Exception.NotFoundException;
import com.TrungTinhBackend.barbershop_backend.Repository.UsersRepository;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import com.TrungTinhBackend.barbershop_backend.Service.Email.EmailService;
import com.TrungTinhBackend.barbershop_backend.Service.Img.ImgService;
import com.TrungTinhBackend.barbershop_backend.Service.Jwt.JwtUtils;
import com.TrungTinhBackend.barbershop_backend.Service.RefreshTokenService.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private ImgService imgService;

    @Override
    public APIResponse register(RegisterDTO registerDTO) {
        APIResponse apiResponse = new APIResponse();

        Users user = usersRepository.findByEmail(registerDTO.getEmail());
        if(user != null) {
            throw new RuntimeException("Email already exists !");
        }
        Users user1 = new Users();
        user1.setUsername(registerDTO.getUsername());
        user1.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user1.setEmail(registerDTO.getEmail());
        user1.setRoleEnum(RoleEnum.CUSTOMER);
        user1.setDeleted(false);

        usersRepository.save(user1);


        // Gửi email sau khi đăng ký thành công
        String to = user1.getEmail();
        String subject = "Đăng ký thành công";
        String body = "Chào " + user1.getUsername() + ",\n\n"
                + "Cảm ơn bạn đã đăng ký tài khoản. Chúc bạn trải nghiệm vui vẻ!\n\n"
                + "Trân trọng,\n"
                + "Đội ngũ phát triển";

        emailService.sendEmail(to, subject, body);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("User register success");
        apiResponse.setData(user1);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse login(LoginDTO loginDTO, HttpServletResponse response, HttpServletRequest request) {
        APIResponse apiResponse = new APIResponse();

        Users user = usersRepository.findByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new NotFoundException("User not found !");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        String jwt = jwtUtils.generateToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
        refreshTokenService.createRefreshToken(refreshToken, user);

        ResponseCookie jwtCookie = ResponseCookie.from("authToken", jwt)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")         // PHẢI giống với logout
                .maxAge(7 * 24 * 60 * 60) // 7 ngày
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Login success");
        apiResponse.setData(user);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getUserByPage(int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Page<Users> users = usersRepository.findAll(pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get users by page = "+page+" size = "+size+" success");
        apiResponse.setData(users);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getUserById(Long id) {
        APIResponse apiResponse = new APIResponse();

        Users user = usersRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found !")
        );

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get users by id = "+id+" success");
        apiResponse.setData(user);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse updateUser(Long id, UserDTO userDTO, MultipartFile img) throws IOException {
        APIResponse apiResponse = new APIResponse();

        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found!"));

        if (img != null && !img.isEmpty()) {
            user.setImg(imgService.updateImg(user.getImg(), img));
        }

        if (userDTO.getUsername() != null && !userDTO.getUsername().isEmpty()) {
            user.setUsername(userDTO.getUsername());
        }

        if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
            user.setEmail(userDTO.getEmail());
        }

        if (userDTO.getPhoneNumber() != null && !userDTO.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }

        if (userDTO.getAddress() != null && !userDTO.getAddress().isEmpty()) {
            user.setAddress(userDTO.getAddress());
        }

        if (userDTO.getBirthDay() != null) {
            user.setBirthDay(userDTO.getBirthDay());
        }

        if (userDTO.getRoleEnum() != null) {
            user.setRoleEnum(userDTO.getRoleEnum());
        }

        if (userDTO.getDescription() != null && !userDTO.getDescription().isEmpty()) {
            user.setDescription(userDTO.getDescription());
        }
        user.setUpdatedAt(LocalDateTime.now());

        // Save user back to DB
        usersRepository.save(user);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Update user with id = " + id + " success");
        apiResponse.setData(user);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse deleteUser(Long id) {
        APIResponse apiResponse = new APIResponse();

        Users user = usersRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found !")
        );

        user.setDeleted(true);
        usersRepository.save(user);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Delete user with id = "+id+" success");
        apiResponse.setData(user);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse restoreUser(Long id) {
        APIResponse apiResponse = new APIResponse();

        Users user = usersRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found !")
        );

        user.setDeleted(false);
        usersRepository.save(user);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Restore user with id = "+id+" success");
        apiResponse.setData(user);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse sendOtpToEmail(String email) throws Exception {
        APIResponse apiResponse = new APIResponse();

        Users user = usersRepository.findByEmail(email);
        if(user == null) {
            throw new NotFoundException("User not found !");
        }

        String otp = String.format("%06d",new Random().nextInt(999999));
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(3));
        usersRepository.save(user);

        emailService.sendEmail(email,"Mã OTP của bạn", "OTP : "+otp);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Send OTP success !");
        apiResponse.setData(otp);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse verifyOtpAndChangePassword(ResetPasswordDTO resetPasswordDTO) throws Exception {
        APIResponse apiResponse = new APIResponse();

        Users user = usersRepository.findByEmail(resetPasswordDTO.getEmail());
        if(user == null) {
            throw new NotFoundException("User not found by email " + resetPasswordDTO.getEmail());
        }

        if(!user.getOtp().equals(resetPasswordDTO.getOtp())) {
            throw new RuntimeException("Invalid OTP !");
        }

        if(user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            user.setOtp(null);
            user.setOtpExpiry(null);
            usersRepository.save(user);
        }

        user.setOtp(null);
        user.setOtpExpiry(null);
        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
        usersRepository.save(user);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Reset password success !");
        apiResponse.setData(null);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }
}
