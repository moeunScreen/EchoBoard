
EchoBoard는 페이지 렌더링과 비동기 데이터 요청을 함께 사용하는 구조로 구현되었습니다

---
### Page Request

| Method | URL           | Response     | 설명            |
| ------ | ------------- | ------------ | ------------- |
| GET    | /posts        | post         | 게시판 목록 페이지    |
| GET    | /posts/new    | post/new     | 게시글 작성 페이지    |
| GET    | /posts/{id}   | post/detail  | 게시글 상세 정보 페이지 |
| GET    | /posts/search | post         | 검색게시글 화면에 출력  |
| GET    | /posts/top    | `List<Post>` | 인기게시글 top5    |


### Data Request

| Method | URL                 | Response | 데이터설명      | 설명  |
| ------ | ------------------- | -------- | ---------- | --- |
| POST   | /api/posts/new      | Long     | 생성된 게시글 ID | 글작성 |
| DELETE | /api/posts/{postId} | HTTP 204 | No Content | 글삭제 |


---
### 게시판목록 페이지
| 항목         | 내용                     |
| ---------- | ---------------------- |
| Method     | GET                    |
| URL        | /posts                 |
| 요청유형       | Page Request           |
| Request    | Pageable               |
| Response   | posts (Thymeleaf View) |
| View Model | posts, page, topPosts  |
| 설명         | 전체 게시글을 조회하여 목록 화면에 출력 |
#### View Model 
| 필드       | 타입                 | 설명            |
| -------- | ------------------ | ------------- |
| posts    | List<PostResponse> | 화면 출력용 게시글 목록 |
| page     | Page<PostResponse> | 게시글 페이징 정보    |
| topPosts | List<PostResponse> | 인기 게시글 TOP5   |

---
### 글 작성

| 항목       |                                         |
| -------- | --------------------------------------- |
| Method   | POST                                    |
| URL      | /api/posts/new                          |
| 요청유형     | Data Request                            |
| Request  | PostCreateRequest, HttpSession(USER_ID) |
| Response | Long (생성된 게시글 ID)                       |


#### Request PostCreateRequest

| 필드      | 타입     | 설명     |
| ------- | ------ | ------ |
| title   | String | 게시글 제목 |
| content | String | 게시글 내용 |

---
### 글 삭제

| 항목       | 내용                              |
| -------- | ------------------------------- |
| Method   | DELETE                          |
| URL      | /api/posts/{postId}             |
| 요청유형     | Data Request                    |
| Request  | postId(Long)                    |
| Response | 204 No Content                  |
| 설명       | 게시글 삭제 시 관련 댓글과 Redis 캐시를 함께 삭제 |


---

### 인기게시글 top5

| 항목         | 내용                       |
| ---------- | ------------------------ |
| Method     | GET                      |
| URL        | /posts/top               |
| 요청유형       | Page Request             |
| Request    | -                        |
| Response   | posts (Thymeleaf View)   |
| View Model | `List<PostResponse>`     |
| 설명         | 조회수를 기준으로 인기 게시글 TOP5 조회 |


---
### 게시글 검색

| 항목         | 내용                       |
| ---------- | ------------------------ |
| Method     | GET                      |
| URL        | /posts/search            |
| 요청유형       | Page Request             |
| Request    | query(String), Pageable  |
| Response   | post.html                |
| View Model | `Page<PostResponse>`     |
| 설명         | 검색 결과를 조회하여 posts 화면에 출력 |

### PostResponse

| 필드        | 타입            |
| --------- | ------------- |
| id        | Long          |
| title     | String        |
| content   | String        |
| viewCount | long          |
| author    | String        |
| createdAt | LocalDateTime |
