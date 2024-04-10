package com.twd.SpringSecurityJWT.service;

import com.twd.SpringSecurityJWT.dto.*;
import com.twd.SpringSecurityJWT.entity.OurUsers;
import com.twd.SpringSecurityJWT.repository.OurUserRepo;
import org.springframework.http.HttpStatus;
//import org.springframework.http.SignUpResponce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
public class AuthService {
    @Autowired
    private OurUserRepo ourUserRepo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    public ResponseEntity<?> signUp(SignUpRequest registrationRequest){
        SignUpResponce resp = new SignUpResponce();
        try{
            Optional<OurUsers> check=ourUserRepo.findByEmail(registrationRequest.getEmail());
            if (check.isPresent()) {
                resp.setStatusCode(400);
                return new ResponseEntity<> (resp, HttpStatus.BAD_REQUEST);
            }

           OurUsers ourUsers = new OurUsers();
           ourUsers.setEmail(registrationRequest.getEmail());
           ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()) );
           ourUsers.setRole(registrationRequest.getRole());
           OurUsers ourUserResult = ourUserRepo.save(ourUsers);
           if(ourUserResult.getId()>0){
                resp.setOurUsers(ourUserResult);
                resp.setMessage("User Saved successfully");
                resp.setStatusCode(200);
            }
            return ResponseEntity.ok(resp);

        }catch(Exception e){
           resp.setStatusCode(500);

            return new ResponseEntity<> ("{\"statusCode\": \"500\"}", INTERNAL_SERVER_ERROR);
        }
    }


    public SignInResponce signIn(SignInRequest signinRequest){
        SignInResponce resp = new SignInResponce();
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPathword()));
            var user =ourUserRepo.findByEmail(signinRequest.getEmail()).orElseThrow();
            System.out.println("USER IS: "+ user);
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
              resp.setStatusCode("200");
              resp.setToken(jwt);
              resp.setRefreshToken(refreshToken);
              resp.setExpirationTime("24Hr");
              resp.setMessage("Successfully Signed In");
        }
        catch (Exception e){
            resp.setStatusCode("500");
        }
        return resp;
    }
    public RefreshResponce refreshToken(RefreshResponce refreshTokenRegist){
        RefreshResponce response = new RefreshResponce();
        String ourEmail = jwtUtils.extractUsername(refreshTokenRegist.getToken());
        OurUsers users = ourUserRepo.findByEmail(ourEmail).orElseThrow();
        if(jwtUtils.isTokenValid(refreshTokenRegist.getToken(), users)){
            var jwt = jwtUtils.generateToken(users);
            response.setStatusCode("200");
            response.setToken(jwt);
            response.setRefreshToken(refreshTokenRegist.getToken());
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Refreshed Token");
        }
        response.setStatusCode("500");
        return response;
    }
}

