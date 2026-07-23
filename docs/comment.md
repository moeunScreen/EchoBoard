

EchoBoard는 페이지 렌더링과 비동기 데이터 요청을 함께 사용하는 구조로 구현되었습니다

#### Page Request
 X
### Data Request

| Method | URL                             | Response                | 데이터설명      | 설명       |
| ------ | ------------------------------- | ----------------------- | ---------- | -------- |
| GET    | /posts/{id}/comments            | `List<CommentResponse>` | -          | 댓글들 불러오기 |
| POST   | /posts/{id}/comments            | CommentResponse         | -          | 댓글작성     |
| DELETE | /api/posts/comments/{commentId} | HTTP 204                | No Content | 댓글삭제     |

---
### 댓글들 불러오기

| 항목       | 내용                      | 추가설명  |
| -------- | ----------------------- | ----- |
| Method   | GET                     |       |
| URL      | /posts/{id}/comments    |       |
| 요청유형     | Data Request            |       |
| Request  | id(Long)                | 포스트id |
| Response | `List<CommentResponse>` |       |


---
### 댓글 작성하기

| 항목       | 내용                                      | 추가설명                            |
| -------- | --------------------------------------- | ------------------------------- |
| Method   | POST                                    |                                 |
| URL      | /posts/{id}/comments                    |                                 |
| 요청유형     | Data Request                            |                                 |
| Request  | id(Long),content(String),parentId(Long) | 포스트id, parentId는 required=false |
| Response | CommentResponse                         |                                 |

parentId는 선택값이며 있을 시 대댓글로 생성됩니다

---
### 댓글 삭제

| 항목       | 내용                              |
| -------- | ------------------------------- |
| Method   | DELETE                          |
| URL      | /api/posts/comments/{commentId} |
| 요청유형     | Data Request                    |
| Request  | commentId(Long)                 |
| Response | 204 No Content                  |
| 설명       | 댓글삭제                            |

---

### CommentResponse

| 필드        | 타입            |
| --------- | ------------- |
| id        | Long          |
| content   | String        |
| username  | String        |
| userId    | Long          |
| createdAt | LocalDateTime |
| parentId  | Long          |
