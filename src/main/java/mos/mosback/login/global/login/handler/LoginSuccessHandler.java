package mos.mosback.login.global.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mos.mosback.login.domain.user.dto.UserInfo;
import mos.mosback.login.domain.user.repository.UserRepository;
import mos.mosback.login.global.jwt.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;


    @Value("${jwt.access.expiration}")
    private String accessTokenExpiration;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                         Authentication authentication) throws IOException {
        String email = extractUsername(authentication); // 인증 정보에서 Username(email) 추출
        String jwt = jwtService.createJwt(email); // JwtService의 createJwt를 사용하여 Jwt 토큰 발급
        String refreshToken = jwtService.createRefreshToken(); // JwtService의 createRefreshToken을 사용하여 RefreshToken 발급
        jwtService.sendAccessAndRefreshToken(response, jwt, refreshToken); // 응답 헤더에 Jwt, RefreshToken 실어서 응답

        String currentEmail = email;
        userRepository.findByEmail(currentEmail)
                .ifPresent(user -> {
                    user.updateRefreshToken(refreshToken);
                    userRepository.saveAndFlush(user);
                });
        log.info("로그인에 성공하였습니다. 이메일 : {}", currentEmail);
        log.info("로그인에 성공하였습니다. Jwt : {}", jwt);
        log.info("발급된 Jwt 만료 기간 : {}", accessTokenExpiration);

        Map<String, Object> result = new HashMap<>();
        result.put("jwt", jwt);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("status", 200);
        responseData.put("success", true);
        responseData.put("result", result);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseData));
    }



    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
