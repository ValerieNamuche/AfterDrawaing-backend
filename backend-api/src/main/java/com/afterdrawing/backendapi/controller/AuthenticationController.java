package com.afterdrawing.backendapi.controller;

import com.afterdrawing.backendapi.core.entity.JwtRequest;
import com.afterdrawing.backendapi.core.entity.JwtResponse;
import com.afterdrawing.backendapi.resource.authentication.*;
import com.afterdrawing.backendapi.sercurity.JWTUtility;
import com.afterdrawing.backendapi.service.AuthenticationService;
import com.afterdrawing.backendapi.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("api/v1/authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    JWTUtility jwtUtility;

    @Autowired
    private UserDetailsService userDetailsService;
    /////////////

    private final AuthenticationService authenticationService;

    @Autowired
    private JwtService jwtService;
/////////////////////
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignUpResource registrationRequest){
        authenticationService.signUp(registrationRequest);
        return new ResponseEntity<>("User registration was successfull", HttpStatus.OK);
    }


    @PostMapping("/login")
    public JwtResponse login(@RequestBody JwtRequest jwtRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails
                = jwtService.loadUserByUsername(jwtRequest.getUsername());

        final String token =
                jwtUtility.generateToken(userDetails);

        return  new JwtResponse(userDetails, token);

    }


    /* @PostMapping("/sign-in")
    public JwtResponse signIn(@RequestBody JwtRequest loginRequest) throws Exception {
        return jwtService.createJwtToken(loginRequest);
    }*/

    /*
    @PostMapping("/refresh-token")
    public AuthenticationResource refreshToken(@Valid @RequestBody RefreshTokenResource refreshTokenRequest, Principal principal){
        String email = principal.getName();
        refreshTokenRequest.setEmail(email);
        return authenticationService.refreshToken(refreshTokenRequest);
    }
    */
    /*
    @PostMapping("/sign-out")
    public ResponseEntity<String> signOut(@Valid @RequestBody RefreshTokenResource refreshTokenRequest){
        authenticationService.signOut(refreshTokenRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Refresh token has been deleted");
    }

    @GetMapping("/verification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable(name = "token") String token){
        authenticationService.verifyAccount(token);
        return new ResponseEntity<>("User account has been activated", HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordResource forgotPasswordRequest){
        authenticationService.forgotPassword(forgotPasswordRequest);
        return new ResponseEntity<>("Petition sent", HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordResource resetPasswordRequest){
        authenticationService.resetPassword(resetPasswordRequest);
        return new ResponseEntity<>("Password changed", HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordResource changePasswordRequest, Principal principal){
        String username = principal.getName();
        authenticationService.changePassword(username, changePasswordRequest);
        return new ResponseEntity<>("Password changed", HttpStatus.OK);
    }
    */
}
