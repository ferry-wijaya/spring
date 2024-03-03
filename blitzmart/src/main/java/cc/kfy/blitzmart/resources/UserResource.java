package cc.kfy.blitzmart.resources;

import cc.kfy.blitzmart.domain.HttpResponse;
import cc.kfy.blitzmart.domain.User;
import cc.kfy.blitzmart.domain.UserPrincipal;
import cc.kfy.blitzmart.dto.UserDTO;
import cc.kfy.blitzmart.form.LoginForm;
import cc.kfy.blitzmart.provider.TokenProvider;
import cc.kfy.blitzmart.service.RoleService;
import cc.kfy.blitzmart.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static cc.kfy.blitzmart.dtomapper.UserDTOMapper.toUser;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
@Slf4j
public class UserResource {
	private final UserService userService;
	private final RoleService roleService;
	private final AuthenticationManager authenticationManager;
	private final TokenProvider tokenProvider;

	@PostMapping("/login")
	public ResponseEntity<HttpResponse> login(@RequestBody @Valid LoginForm loginForm) {
		//authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword()));
		authenticationManager.authenticate(unauthenticated(loginForm.getEmail(), loginForm.getPassword()));
		UserDTO userDTO = userService.getUserByEmail(loginForm.getEmail());
		//return sendResponse(userDTO);
		return userDTO.isUsingMfa() ? sendVerificationCode(userDTO) : sendResponse(userDTO);
	}

	@PostMapping("/register")
	public ResponseEntity<HttpResponse> saveUser(@RequestBody @Valid User user) {
		UserDTO userDTO = userService.createUser(user);
		return ResponseEntity.created(getUri()).body(
						HttpResponse.builder()
										.timeStamp(now().toString())
										.data(of("user", userDTO))
										.message("User created")
										.status(CREATED)
										.statusCode(CREATED.value())
										.build());
	}

	@GetMapping("/profile")
	public ResponseEntity<HttpResponse> profile(Authentication authentication) {
		UserDTO userDTO = userService.getUserByEmail(authentication.getName());
		//System.out.println(authentication);
		return ResponseEntity.ok().body(
						HttpResponse.builder()
										.timeStamp(now().toString())
										.data(of("user", userDTO))
										.message("Profile Verifier")
										.status(OK)
										.statusCode(OK.value())
										.build());
	}

	@GetMapping("/verify/code/{email}/{code}")
	public ResponseEntity<HttpResponse> verifyCode(@PathVariable("email") String email, @PathVariable("code") String code) {
		UserDTO userDTO = userService.verifyCode(email, code);
		return ResponseEntity.ok().body(
						HttpResponse.builder()
										.timeStamp(now().toString())
										.data(of(
														"user", userDTO,
														"access_token", tokenProvider.createAccessToken(getUserPrincipal(userDTO)),
														"refresh_token", tokenProvider.createRefreshToken(getUserPrincipal(userDTO))))
										.message("Login Success")
										.status(OK)
										.statusCode(OK.value())
										.build());
	}

	@RequestMapping("/error")
	public ResponseEntity<HttpResponse> handleError(HttpServletRequest request) {
		return ResponseEntity.badRequest().body(
						HttpResponse.builder()
										.timeStamp(now().toString())
										.reason("There is no mapping for a " + request.getMethod() + " request for this path on the server")
										.status(BAD_REQUEST)
										.statusCode(BAD_REQUEST.value())
										.build());
	}
	private ResponseEntity<HttpResponse> sendResponse(UserDTO userDTO) {
		return ResponseEntity.ok().body(
						HttpResponse.builder()
										.timeStamp(now().toString())
										.data(of(
														"user", userDTO,
														"access_token", tokenProvider.createAccessToken(getUserPrincipal(userDTO)),
														"refresh_token", tokenProvider.createRefreshToken(getUserPrincipal(userDTO))))
										.message("Login Success")
										.status(OK)
										.statusCode(OK.value())
										.build());
	}

	private URI getUri() {
		return URI.create(fromCurrentContextPath().path("/user/get/<userId>").toUriString());
	}

	private UserPrincipal getUserPrincipal(UserDTO userDTO) {
		return new UserPrincipal(
						toUser(userService.getUserByEmail(userDTO.getEmail())),
						roleService.getRoleByUserId(userDTO.getId()).getPermission());
	}

	private ResponseEntity<HttpResponse> sendVerificationCode(UserDTO userDTO) {
		userService.sendVerificationCode(userDTO);
		return ResponseEntity.ok().body(
						HttpResponse.builder()
										.timeStamp(now().toString())
										.data(of("user", userDTO))
										.message("Verification Code Sent")
										.status(OK)
										.statusCode(OK.value())
										.build());
	}
}
