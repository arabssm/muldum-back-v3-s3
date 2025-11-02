# Muldum Backend - Hexagonal Architecture

## Tech Stack
- Java 21
- Spring Boot 3.x
- Gradle
- Spring Data JPA
- PostgreSQL
- AWS S3
- JWT Authentication
- Docker (개발용)
- JUnit5 / Mockito

---

## Architecture: Hexagonal (Ports and Adapters)

### Domain Layer
- 비즈니스 로직의 중심
- 외부 기술이나 프레임워크에 의존하지 않음
- `core.domain` 하위에 엔티티, 도메인 서비스 정의

### Application Layer
- 유스케이스(UseCase) 정의 및 실행
- 포트를 통해 외부 어댑터 호출
- 트랜잭션 관리 및 도메인 이벤트 처리
- 위치: `core.application`

### Adapter Layer
- 외부 시스템과의 연결부 (inbound/outbound)
- 예: REST 컨트롤러, JPA Repository, S3 Storage 등
- 위치:
    - `adapter.in.web` → REST API (Controller)
    - `adapter.out.persistence` → DB 접근 (Repository)
    - `adapter.out.s3` → AWS S3 연동

### Configuration Layer
- 스프링 설정 및 의존성 주입 관리
- 위치: `infrastructure.config`

---

## Directory Structure

```
muldum/
├── core/
│ ├── domain/
│ │ ├── file/
│ │ │ ├── File.java
│ │ │ ├── FileMetadata.java
│ │ │ └── FileStoragePort.java
│ │ └── user/
│ │ ├── User.java
│ │ └── UserRepositoryPort.java
│ └── application/
│ ├── file/
│ │ ├── UploadFileUseCase.java
│ │ ├── GetFileUrlUseCase.java
│ │ └── DeleteFileUseCase.java
│ └── user/
│ └── RegisterUserUseCase.java
│
├── adapter/
│ ├── in/
│ │ └── web/
│ │ ├── FileController.java
│ │ └── UserController.java
│ └── out/
│ ├── persistence/
│ │ ├── FileJpaEntity.java
│ │ ├── FileJpaRepository.java
│ │ └── FilePersistenceAdapter.java
│ └── s3/
│ ├── S3StorageAdapter.java
│ └── S3Config.java
│
├── infrastructure/
│ ├── config/
│ │ ├── SecurityConfig.java
│ │ ├── JwtConfig.java
│ │ └── S3Config.java
│ └── exception/
│ └── GlobalExceptionHandler.java
│
├── MuldumApplication.java
└── build.gradle
```

---

## 핵심 내용: S3 연동 (포트 + 어댑터)

### 1) Outbound Port (Domain / Application)
```java
// package muldum.core.domain.file;
public interface FileStoragePort {
    String uploadFile(String fileName, byte[] content, String contentType);
    byte[] downloadFile(String fileName);
    void deleteFile(String fileName);
}
```

## Database Schema

```sql
// 유저
Table User {
  id int [primary key, increment]
  bsm_id int [not null, unique]
  name varchar2(4) [not null]
  enrolled_at int [not null]
  email text [not null]
  class_no int [not null]
  grade int [not null]
  user_role enum("student", "teacher", "mento") [not null]
}

// 팀 리스트
Table Team {
  id int [primary key, increment]
  name varchar2(40) [not null]
  readme text
}

// 어느학생이 어느 팀에 속해있는지
Table Member {
  id int [primary key, increment]
  user_id int [not null]
  team_id int [not null]
}

// 갤러리
Table Gallery {
  id int [primary key, increment]
  type enum("design", "major", "network", "free")
  team_id int [not null]
  file_id int [not null]
  title varchar2(20)
  description text
}

// S3 파일 저장
Table S3 {
  id int [primary key, increment]
  file_url text [not null]
  file_name varchar2(100)
  file_type varchar2(30)  // 예: "image/png", "application/pdf"
  file_extension varchar2(10) // 예: "png", "pdf"
  user_id int
  updated_at datetime [not null]
}

// 공지사항
Table Notice {
  id int [primary key, increment]
  user_id int [not null] //공지를 등록한 사람
  team_id int // null이거나 해당팀이 없을때 전체 멘션
  title varchar2(20) [not null]
  content text
  file_id int
  is_alerted boolean [default: false]
}

//댓글(댓글의 종류는 타입으로 관리)
Table Comment {
  id int [primary key, increment]
  type enum("notice", "gallery") //ex) type이 notice면 공지에 대한 댓글
  commented_at int [not null]
  user_id int [not null]
  content text
  created_at datetime [not null]
  updated_at datetime [not null]
}

// 평가
Table Test {
  id int [primary key, increment]
  team_id int [not null]
  title varchar2(40) [not null]
  object1 varchar2(40) [not null]
  object2 varchar2(40) [not null]
  stacks text [not null]
  teacher_feedback text [not null]
  mento_feedback text [not null]
  created_at datetime [not null]
}

// 순위
Table Award {
  id int [primary key, increment]
  team_id int
  year int [not null]
  rank int [not null, unique]
}

// 물품 신청
Table ItemRequest {
  id int [primary key, increment]
  team_id int [not null]
  product_name varchar(100) [not null]
  quantity int [default: 1]
  price int
  product_link text
  item_source varchar2(20)
  status enum("PENDING", "APPROVED", "REJECTED")
  reason text
}
Table ItemSource {
  id varchar2(20) [primary key]
  label varchar2(20) [not null]
  description text
}

// 현 예산
Table ItemBudget {
  id int [primary key, increment]
  team_id int [not null, unique]
  total_budget int [not null]
  used_budget int [default: 0]
  updated_at datetime
}

Table Token {
  id int [primary key, increment]
  user_id int [not null]
  refresh_token text [not null]
  device_info varchar(255)
  ip_address varchar(50)
  is_revoked boolean [default: false]
  created_at datetime [default: "now"]
  expires_at datetime [not null]
}

// 관계
Ref: User.id < Member.user_id
Ref: Team.id < Member.team_id
Ref: Team.id < Gallery.team_id
Ref: S3.id < Gallery.file_id
Ref: Team.id < Notice.team_id
Ref: S3.id < Notice.file_id
Ref: User.id < Notice.user_id
Ref: Team.id < Test.team_id
Ref: User.id < S3.user_id
Ref: Team.id < Award.team_id
Ref: Team.id - ItemBudget.team_id
Ref: Team.id < ItemRequest.team_id
Ref: ItemRequest.item_source > ItemSource.id
Ref: User.id < Comment.user_id
Ref: Notice.id < Comment.commented_at
Ref: User.id - Token.user_id

```
