package org.example.echoBoard.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.echoBoard.model.SessionEntity;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.echoBoard.dto.request.SignUpRequest;
import org.example.echoBoard.model.User;
import org.example.echoBoard.repository.LoginHistoryRepository;
import org.example.echoBoard.repository.SessionRepository;
import org.example.echoBoard.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService{


 /*
 역할

유저 조회
로그인 성공/실패 판단

  */

    private final UserRepository userRepository;
    private final LoginHistoryRepository loginHistoryRepository;
    private final SessionRepository sessionRepository;
    private final LoginHistoryService loginHistoryService;
    private final UserLockService userLockService;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void login(String username, String rawPassword,
                      HttpServletRequest request) {


        User user = userRepository.findByUsername(username);

        if (user == null) {
            // 가짜 지연 or 공통 메시지
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        if (user.isLocked()) {
            // 아직 잠금 시간 남아있으면 예외
            if (user.getLockedUntil() != null &&
                    user.getLockedUntil().isAfter(LocalDateTime.now())) {
                throw new IllegalStateException("계정이 잠겨 있습니다. 잠시 후 다시 시도하세요.");
            }

            // 잠금 시간이 지났으면 풀기
            user.setLocked(false);
            user.setLockedUntil(null);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
            }

        boolean success = passwordEncoder.matches(rawPassword, user.getPassword());

        // 🔹 로그인 이력 기록 (성공/실패 모두)
        // Lock상태에서도 시도는 기록
        loginHistoryService.recordLogin(user, request.getRemoteAddr(),
                request.getHeader("User-Agent"), success);

        if (!success) {

            // 1. 먼저 최근 실패 횟수 체크
            int failCount = loginHistoryRepository
                    .countFailAfterLastSuccess(
                            user.getId(),
                            LocalDateTime.now().minusMinutes(10)
                    );

            System.out.println("failCount : "+failCount);

            if (failCount >= 10) {
                userLockService.lockUser(user.getId());

                throw new IllegalStateException("10분 내 로그인 실패 10회로 계정이 잠겼습니다.");
            }

            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        // ===========================
        // 세션 처리 및 DB 동기화
        // ===========================



        String dbSession = request.getRequestedSessionId();
        // 기존 세션 무효화 (DB)
        sessionRepository.updateIsActiveBySessionId(dbSession);
        // 기존 HttpSession도 무효화
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        //새 HttpSession 생성
        HttpSession newSession = request.getSession(true);
        // DB 세션 생성
        SessionEntity sessionEntity = SessionEntity.builder()
                .user(user)
                .sessionId(newSession.getId())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();

        sessionRepository.save(sessionEntity);

        // 서버 세션에 최소 정보만 저장
        newSession.setAttribute("USER_ID", user.getId());


    }

    public User register(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void signUp(SignUpRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }

    public User findById(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }
}