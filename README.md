# 📝 Blog Project (Backend)

**Spring Boot**와 **Java** 기반의 개인 블로그 서비스의 백엔드 프로젝트입니다.  

**Docker** 기반으로 실행 환경과, 인프라 상황에 맞춰 **AWS(CodeDeploy) 기반 배포** 또는 **SSH를 통한 직접 배포** 방식을 선택해 배포할 수 있도록 구성하였습니다.  
안전한 사용자 인증과 유연한 운영 환경을 위해 **JWT** 기반 인증, **Flyway**를 활용한 스키마 관리, **GitHub Actions**를 통한 배포 자동화를 중심으로 구성되어 있습니다.  
<br/>

> **Frontend Repository**: [ttony-kim/blog-front](https://github.com/ttony-kim/blog-front)

<br/>

## 🛠 사용 기술

* **Language:** Java 17  
* **Framework:** Spring Boot 3.1.3  
* **Database:** MySQL (Database: blog)  
* **Migration:** Flyway  
* **Security:** JWT (인증/인가), Jasypt (설정 정보 암호화)  
* **DevOps:** GitHub Actions, Docker Compose  
* **Deploy:** AWS (S3, CodeDeploy, EC2, Docker), SSH / SCP 배포 지원

<br/>

## 🏗 CD/CI 파이프라인
이 프로젝트는 여러 개의 `deploy 파일`을 통해 다양한 배포 환경을 제공합니다. 모든 배포는 **GitHub Actions**의 `workflow_dispatch`를 통해 수동으로 제어됩니다.

### 1. AWS Cloud Deployment
- Flow: GitHub Actions → AWS S3 → AWS CodeDeploy → EC2 (Docker Compose)
- AWS의 관리형 서비스를 활용하여 안정적인 배포 파이프라인을 구성했습니다.

### 2. Direct SSH Deployment
- Flow: GitHub Actions → SSH/SCP → 온프레미스 Server
- 중간 배포 서비스 없이 서버에 직접 빌드 파일을 전송하고 스크립트를 실행하여 배포합니다.

<br/>

## 🔐 보안 및 데이터베이스 

### 인증 및 암호화 (JWT & Jasypt)
- **JWT (JSON Web Token)**: `Stateless` 인증 구조를 적용하여 사용자 인증 및 권한을 처리합니다.
- **Jasypt**: `yml` 파일 내의 DB 정보 등 민감한 데이터를 암호화하여 관리합니다.
- **Secret Management**: JWT 서명과 Jasypt 복호화에 필요한 `secretKey`는 환경 변수를 통해 주입받도록 구성했습니다.

### Flyway 스키마 관리
- `Flyway`를 통해 DB 스키마 버전을 관리합니다. 
- 애플리케이션 실행 시 `db/migration` 스크립트에 따라 `blog` 데이터베이스의 테이블이 자동으로 생성됩니다.

<br/>

## ⚙️ 실행 환경 설정 

로컬 환경에서 프로젝트를 실행 시 아래의 설정이 필요합니다.

### Spring Profile
``` 
 -Dspring.profiles.active=local
```
### Environment Variables
* `secretKey`: Jasypt 복호화 및 JWT 서명에 사용되는 비밀키입니다.

<br/>

## ✨ 주요 기능 
- **인증 및 인가**: `JWT`를 활용한 로그인 및 권한별 접근 제어
- **블로그 서비스**: 게시글 CRUD 및 카테고리 관리 기능
- **로깅 시스템**: `LogFilter` 및 `ReadableRequestWrapper`를 활용한 HTTP 요청/응답 상세 로깅
- **환경 분리**: `local`, `dev` 등 프로파일별 설정 분리

<br/>

## 📂 주요 파일 구조
- `.github/workflows/`: 배포 방식별 YAML 워크플로우 파일 (AWS, SSH 등)
- `src/main/resources/db/migration`: Flyway 스키마 마이그레이션 스크립트
- `appspec.yml` & `scripts/`: AWS CodeDeploy 배포 정의 및 자동화 스크립트
- `docker-compose.yml`: 인프라 구성을 위한 컨테이너 설정 파일
