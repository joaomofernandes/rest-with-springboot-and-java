package com.example.demo.Controllers.v1;

import com.example.demo.Service.AuthService;
import com.example.demo.data.vo.Security.AccountCredentialsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Authentication Endpoint")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @SuppressWarnings("rawtypes")
    @Operation(summary = "Authenticates a user and returns a token.")
    @PostMapping(value = "/signin")
    public ResponseEntity signin(@RequestBody AccountCredentialsVO credentials){
        if(credentials == null || credentials.getUsername() == null || credentials.getUsername().isBlank()
            || credentials.getPassword() == null || credentials.getPassword().isBlank()){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        }else{
            var token = authService.signin(credentials);
            if(token == null){
                return  ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
            }
            return token;
        }
    }

    @SuppressWarnings("rawtypes")
    @Operation(summary = "Refresh token for authenticated user and returns a token.")
    @PutMapping(value = "/refresh/{username}")
    public ResponseEntity refreshToken(@PathVariable("username") String username,
                                       @RequestHeader("Authorization") String refreshToken){
        if(refreshToken == null || refreshToken.isBlank()
                || username == null || username.isBlank()){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        }else{
            var token = authService.refreshToken(username, refreshToken);
            if(token == null){
                return  ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
            }
            return token;
        }
    }
}
