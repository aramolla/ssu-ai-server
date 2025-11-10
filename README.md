# 숭실대 AI대학 홈페이지 서버

### 주요 특징

- 동적 게시판 시스템 메타데이터 기반으로 다양한 타입(LIST, THUMBNAIL, FAQ, COMPANY)의 게시판 동적 생성
- 파일 관리 첨부파일 및 썸네일 이미지 업로드/다운로드
- 인증/인가 JWT 기반 Spring Security 인증
- 권한별 API 분리 (Public / Admin)
- 동적 필드 게시판별 최대 5개의 커스텀 필드(sub1~sub5) 지원

## 🛠 기술 스택

- Java 21
- Spring Boot 3.5.7
- Spring Data JPA, MySQL 8.x
- Spring Security + JWT
- Gradle

## 📁 프로젝트 구조

```
src/main/java/com/ai/
├── api/
│   ├── auth/                    # 인증/인가
│   │   ├── controller/
│   │   ├── domain/
│   │   ├── dto/
│   │   ├── repository/
│   │   └── service/
│   │
│   ├── board/                   # 게시판 메타데이터
│   │   ├── controller/
│   │   │   ├── admin/
│   │   │   └── public/
│   │   ├── domain/
│   │   │   ├── Board.java
│   │   │   ├── BoardCategory.java   # 게시판 카테고리 Enum
│   │   │   └── BoardType.java       # LIST, THUMBNAIL, FAQ, CALENDER
│   │   ├── dto/
│   │   ├── repository/
│   │   └── service/
│   │
│   ├── post/                    # 게시글
│   │   ├── controller/
│   │   │   ├── admin/           # CUD 전용 (관리자)
│   │   │   │   ├── BasePostAdminController.java
│   │   │   │   ├── NoticeAdminController.java
│   │   │   │   ├── BudgetAdminController.java
│   │   │   │   └── ...
│   │   │   └── public/          # Read 전용 (공개)
│   │   │       ├── BasePostPublicController.java
│   │   │       ├── NoticePublicController.java
│   │   │       ├── BudgetPublicController.java
│   │   │       └── ...
│   │   ├── domain/
│   │   ├── dto/
│   │   ├── repository/
│   │   └── service/
│   │
│   └── resource/                # 첨부파일
│       ├── controller/
│       │   ├── admin/
│       │   └── public/
│       ├── domain/
│       │   ├── Attachment.java
│       │   └── PostAttachment.java
│       ├── dto/
│       ├── repository/
│       └── service/
│
└── common/                      # 공통 모듈
    ├── config/
    │   └── SecurityConfig.java
    ├── exception/               # 예외 처리
    │   ├── GlobalExceptionHandler.java
    │   ├── EntityNotFoundException.java
    │   ├── DuplicateEntityException.java
    │   ├── UnauthorizedException.java
    │   ├── InvalidTokenException.java
    │   ├── InvalidFileException.java
    │   ├── FileStorageException.java
    │   └── ErrorResponse.java
    ├── security/
    │   └── JwtAuthenticationFilter.java
    └── util/
        └── JwtUtil.java
```

## 설치 및 실행

### 파일 업로드 디렉토리 생성

```bash
mkdir -p /path/to/upload
```

### 5. 실행

```bash
# 빌드
./gradlew clean build

# 실행
./gradlew bootRun
```

서버가 `http://localhost:4000`에서 실행

### 6. 초기 테스트 데이터 생성

```sql
-- 게시판 메타데이터 생성
INSERT INTO board (title, content, board_type, categories, paging_num, sub1_label, sub2_label, sub3_label, sub4_label, sub5_label) 
VALUES 
('공지사항', '공지사항', 'LIST', '["일반"]', 10, NULL, NULL, NULL, NULL, NULL),
('예결산 공고', NULL, 'LIST', NULL, 10, NULL, NULL, NULL, NULL, NULL),
('기부금', NULL, 'LIST', NULL, 10, NULL, NULL, NULL, NULL, NULL),
('안전·보건', NULL, 'LIST', NULL, 10, NULL, NULL, NULL, NULL, NULL),
('취업정보', NULL, 'LIST', NULL, 10, NULL, NULL, NULL, NULL, NULL),
('기타', NULL, 'LIST', NULL, 10, NULL, NULL, NULL, NULL, NULL),
('자료실', NULL, 'LIST', NULL, 10, NULL, NULL, NULL, NULL, NULL),
('규정 및 메뉴얼', NULL, 'LIST', '["산학협력단", "지원기관", "기타"]', 10, NULL, NULL, NULL, NULL, NULL),
('FAQ', NULL, 'FAQ', '["교내연구지원", "지식재산권·기술이전", "창업지원"]', 10, NULL, NULL, NULL, NULL, NULL),
('연구성과', '연구성과', 'LIST', NULL, 10, NULL, NULL, NULL, NULL, NULL),
('교수소개', '교수소개', 'THUMBNAIL', '["전임교수", "교육·산학협력중점교수", "퇴임·명예교수"]', 10, '이메일', '연락처', '학력 및 경력', '담당과목', '위치'),
('학사일정', '학사일정', 'CALENDER', NULL, 10, NULL, NULL, NULL, NULL, NULL),
('소모임', '소모임', 'THUMBNAIL', NULL, 10, NULL, NULL, NULL, NULL, NULL),
('학부소식', '학부소식', 'LIST', NULL, 10, NULL, NULL, NULL, NULL, NULL),
('연구활동 소개', '연구활동 소개', 'THUMBNAIL', NULL, 9, 'link', NULL, NULL, NULL, NULL);
```

## 📡 API 엔드포인트

### 인증 API

```
POST   /api/auth/signup          # 회원가입
POST   /api/auth/login           # 로그인
POST   /api/auth/refresh         # 토큰 갱신
GET    /api/admin/test           # 관리자 테스트 (ROLE_ADMIN)
```

### 게시판 API

```
GET    /api/boards               # 전체 게시판 목록 조회
GET    /api/boards/{id}          # 특정 게시판 조회
POST   /api/boards               # 게시판 생성 (관리자)
PUT    /api/boards/{id}          # 게시판 수정 (관리자)
DELETE /api/boards/{id}          # 게시판 삭제 (관리자)
```

### 게시글 API

각 게시판별 엔드포인트:

```
# 공지사항
GET    /api/notice               # 목록 조회 (?category=&keyword=&page=&size=)
GET    /api/notice/{id}          # 상세 조회
POST   /api/notice               # 생성
PUT    /api/notice/{id}          # 수정
DELETE /api/notice/{id}          # 삭제

# 예결산 공고
GET    /api/budget
GET    /api/budget/{id}
POST   /api/budget
PUT    /api/budget/{id}
DELETE /api/budget/{id}

# 기부금
GET    /api/donation
...

# 안전·보건
GET    /api/safety
...

# 취업정보
GET    /api/recruitment
...

# 기타
GET    /api/etc
...

# 자료실
GET    /api/dataroom
...

# 규정 및 메뉴얼
GET    /api/manual
...

# FAQ
GET    /api/faq
...

# 연구성과
GET    /api/research
...

# 교수소개
GET    /api/professors
...

# 소모임
GET    /api/gathering
...

# 학부소식
GET    /api/precautions
...

# 연구활동 소개
GET    /api/lab-research
...
```

### 첨부파일 API

```
GET    /api/attachments/{id}         # 파일 다운로드
DELETE /api/attachments/{id}         # 파일 삭제 (관리자)
```

```

## 📝 주요 기능

### 1. 동적 게시판 시스템

- BoardType: LIST, THUMBNAIL, FAQ, COMPANY
- 동적 필드: sub1~sub5 (최대 5개의 커스텀 필드)
- 카테고리: JSON 배열로 저장

### 2. 게시글 관리

- CRUD 기능
- 카테고리별 필터링
- 키워드 검색
- 조회수 추적
- 공지사항 고정 (isNotice)

### 3. 파일 관리

- 썸네일: Post와 1:1 관계
- 첨부파일: PostAttachment 중간 테이블로 N:M 관계
- 파일 크기 제한: 300KB

### 4. 인증/인가

- JWT 기반 인증
- Access Token: 24시간
- Refresh Token: 7일
- ROLE_USER, ROLE_ADMIN으로 구분


### 수정 필요 사항
- 예외 처리 표준화 필요
- N+1 문제 잠재적 발생 (FetchType.LAZY 사용하지만 Fetch Join 미사용)
-  파일 업로드 보안 검증 부족 (확장자, 크기만 제한)
- DTO 검증 부족 (@Valid 미사용)
- 조회수 증가 로직이 동기 처리
- 인증요청 경로
```bash
curl -X GET http://localhost:4000/api/admin/test \
  -H "Authorization: Bearer <your_access_token>"
```
