package com.example.pos.authentication.controllers;

import com.example.pos.authentication.dtos.LoginUserDto;
import com.example.pos.authentication.dtos.RegisterUserDto;
import com.example.pos.authentication.entity.User;
import com.example.pos.authentication.responses.LoginResponse;
import com.example.pos.authentication.services.AuthenticationService;
import com.example.pos.authentication.services.JwtService;
import com.example.pos.components.JavaResponse;
import com.example.pos.constant.JavaConstant;
import com.example.pos.constant.JavaValidation;
import com.example.pos.entity.OpenShift;
import com.example.pos.repository.shiftRepository.OpenShiftRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.*;

@RequestMapping("/api/auth")
@RestController
@Validated
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;
    @Autowired
    private OpenShiftRepository repoOpen;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        HashMap<String, Object> error = new HashMap<>();

        // String keyEmail ="email";
        // String email = JavaValidation.checkEmail(registerUserDto.getEmail());

        String keyFullName = "fullName";
        String fullName = JavaValidation.checkField(registerUserDto.getFullName(), keyFullName);

        String keyPassword = "password";
        String password = JavaValidation.checkPassword(registerUserDto.getPassword());

        // String keyRole ="role";
        // String role = JavaValidation.checkRole(""+registerUserDto.getRole() ,
        // keyRole);

        // String keyPhone ="phone";
        // String phone = JavaValidation.checkPhone(registerUserDto.getPhone());

        // if( !email.isEmpty() ) error.put(keyEmail, email);
        // if( !role.isEmpty() ) error.put(keyRole,role);
        if (!fullName.isEmpty())
            error.put(keyFullName, fullName);
        if (!password.isEmpty())
            error.put(keyPassword, password);
        // if( !phone.isEmpty() ) error.put(keyPhone,phone);
        if (!error.isEmpty())
            return ResponseEntity.status(500).body(error);

        User registeredUser = authenticationService.signup(registerUserDto);

        HashMap<String, Object> map = new HashMap<>();
        map.put("id", registeredUser.getId());
        map.put("name", registeredUser.getFullName());
        map.put("userCode", registeredUser.getUserCode());
        map.put("password", registeredUser.getPassword());
        // map.put("phone", registeredUser.getPhone());
        map.put("role", registeredUser.getRole());
        // return ResponseEntity.ok(registeredUser);
        return JavaResponse.success(map);
    }

    @Autowired
    private HttpSession httpSession;

    @PostMapping("/login")
    public ResponseEntity<User> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken)
                .setExpiresIn(jwtService.getExpirationTime());

        int countPosId = repoOpen.countPosId(JavaConstant.currentDate);
        countPosId++;
        String posId = "";
        if (countPosId < 10) {
            posId = "0" + countPosId;
        } else {
            posId = "" + countPosId;
        }

        httpSession.setAttribute(JavaConstant.userId, authenticatedUser.getId());
        httpSession.setAttribute(JavaConstant.userCode, authenticatedUser.getUserCode());

        authenticatedUser.setToken(jwtToken);
        authenticatedUser.setPosId(posId);
        authenticatedUser.setExpiredToken(String.valueOf(loginResponse.getExpiresIn()));
        return ResponseEntity.ok(authenticatedUser);
    }

}