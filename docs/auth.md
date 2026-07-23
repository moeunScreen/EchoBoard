## 인증(Auth)

EchoBoard는 페이지 렌더링과 비동기 데이터 요청을 함께 사용하는 구조로 구현되었습니다

---
### Page Request
| Method | URL          | Response        | 설명   |
| ------ | ------------ | --------------- | ---- |
| POST   | /auth/login  | redirect:/      | 로그인  |
| POST   | /auth/logout | redirect:/login | 로그아웃 |

### Data Request

| Method | URL          | Response     | 설명      |
| ------ | ------------ | ------------ | ------- |
| POST   | /auth/record | LoginHistory | 로그인기록저장 |

---

### 로그인

| 항목          |                                    |     |
| ----------- | ---------------------------------- | --- |
| Method      | POST                               |     |
| URL         | /auth/login                        |     |
| Request     | username(String), password(String) |     |
| 요청유형        | Page Request                       |     |
| 성공 Response | redirect:/                         |     |
| 실패 Response | redirect:/login                    |     |
| 설명          | 사용자 인증 후 HttpSession 생성            |     |

---

### 로그아웃

| 항목          |                                |
| ----------- | ------------------------------ |
| Method      | POST                           |
| URL         | /auth/logout                   |
| Request     | -                              |
| 요청유형        | Page Request                   |
| 성공 Response | redirect:/login                |
| 설명          | HttpSession 종료 및 DB 세션 상태 비활성화 |

---

### 로그인 기록 저장

| 항목       |                                      |
| -------- | ------------------------------------ |
| Method   | POST                                 |
| URL      | /auth/record                         |
| Request  | userId(Long), ip(String), ua(String) |
| 요청유형     | Data Request                         |
| Response | LoginHistory                         |
| 설명       | 사용자 로그인 성공 이력 저장                     |
|          |                                      |



#### Response LoginHistory Data

| 필드        | 타입            |
| --------- | ------------- |
| id        | Long          |
| user      | User          |
| ipAddress | String        |
| userAgent | String        |
| loginTime | LocalDateTime |
| success   | Boolean       |

---
