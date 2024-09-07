package com.checkout.payment.gateway.controller;

import com.checkout.payment.gateway.model.AuthenticationRequest;
import com.checkout.payment.gateway.model.AuthenticationResponse;
import com.checkout.payment.gateway.model.ErrorResponse;
import com.checkout.payment.gateway.model.PaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

  private final AuthenticationManager authenticationManager;

  @Autowired
  public LoginController(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Operation(summary = "Login operation")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "202", description = "login request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class)) }),
      @ApiResponse(responseCode = "400", description = "bad request",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }),
      @ApiResponse(responseCode = "500", description = "internal server error",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) })
  })
  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authRequest, HttpSession session) {
    AuthenticationResponse statusResponse = new AuthenticationResponse();
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
      );
      SecurityContextHolder.getContext().setAuthentication(authentication);
      statusResponse.setMessage("Success login");
      return new ResponseEntity<>(statusResponse,  HttpStatus.OK);
    } catch (AuthenticationException e) {
      statusResponse.setMessage("login fails");
      if (session != null) {
        session.invalidate();
      }
      SecurityContextHolder.clearContext();
      return new ResponseEntity<>(statusResponse,  HttpStatus.UNAUTHORIZED);
    }
  }

}