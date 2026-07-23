
### Page Request 

| Method | URL            | Response      | 설명   |
| ------ | -------------- | ------------- | ---- |
| GET    | /notifications | notifications | 알림목록 |

### Data Request

| Method | URL                      | Response | 데이터설명                 | 설명      |
| ------ | ------------------------ | -------- | --------------------- | ------- |
| POST   | /notifications/{id}/read | Map      | success, unreadCount  | 알림하나 읽기 |
| POST   | /notifications/read-all  | String   | notifications View 반환 | 알림모두 읽기 |

---
### 알림 목록 페이지

| 항목         | 내용                             |
| ---------- | ------------------------------ |
| Method     | GET                            |
| URL        | /notifications                 |
| 요청유형       | Page Request                   |
| Request    | HttpSession(USER_ID)           |
| Response   | notifications (Thymeleaf View) |
| View Model | `List<notifications>`          |
| 설명         | 사용자의 알림 목록을 조회하여 화면에 출력        |


---
### 알림 읽음 처리

| 항목       | 내용                             | 추가설명 |
| -------- | ------------------------------ | ---- |
| Method   | POST                           |      |
| URL      | /notifications/{id}/read       |      |
| 요청유형     | Data Request                   |      |
| Request  | id(Long), HttpSession(USER_ID) | 알림id |
| Response | Map(success, unreadCount)      |      |
| 설명       | 알림을 읽음 처리하고 남은 미읽음 개수 반환       |      |

---

### 전체 읽음 처리

| 항목       | 내용                             |
| -------- | ------------------------------ |
| Method   | POST                           |
| URL      | /notifications/read-all        |
| 요청유형     | Data Request                   |
| Request  | HttpSession(USER_ID)           |
| Response | notifications (Thymeleaf View) |
| 설명       | 사용자의 모든 알림을 읽음 처리후 페이지 반환      |
