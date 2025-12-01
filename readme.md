# 크레디탑 관리자 프로그램 (MARU_APP)
건흥페이먼츠의 결제 솔루션 **크레디탑**을 운영·모니터링하기 위한 통합 관리자 웹 애플리케이션입니다. 
가맹점과 거래, 정산, 고객지원, 시스템 관리를 하나의 백오피스에서 처리할 수 있도록 설계되었습니다.

## 요약
- **자바 8 + Apache Ant** 기반의 Spring MVC / Spring Security 웹앱입니다.
- 기본 진입점은 `/init`이며, 내장 Tomcat 런처(`com.pgmate.lib.tomcat.Tomcat8`)로 실행합니다.
- Ant 빌드 결과물(`lib/MARU_v1.jar`, `lib/MARU_lib-1.0.1.jar`)을 포함한 배포 패키지를 생성하거나, Git 클론 후 직접 빌드하여 사용할 수 있습니다.

## 기술 스택 (Tech Stack)
- Language : Java
- Core Framework : Spring Boot
- Web Framework : Spring Web MVC 4.2.9
- Application Sever : Embedded Tomcat 8.x
- DB : MariaDB 1.5.5
- Connection Pool : HikariCP 2.7.9
- Build Tool : Apache Ant
- IDE : IntelliJ, Eclipse
- Logging : Logback

## 다운로드 / 배포 패키지 받기
다운로드용 패키지를 직접 준비하려면 다음 절차를 따르세요.
1. **소스 받기**
   ```bash
   git clone <저장소-URL>
   cd MARU_APP
   ```
2. **빌드 및 패키징** (Ant 필요)
   ```bash
   ant -f bin/build.xml clean compile
   ```
   - 빌드 후 `lib/`에 실행 JAR이 생성되며, `web/`에 컴파일된 클래스가 배치됩니다.

## 주요 기능
- **가맹점/대행사 관리**: 가맹점·대행사·지사 등록, 터미널 관리, 수수료 템플릿, IP/SMS 인증 관리
- **거래 모니터링**: 승인·매입·취소 내역 조회, 오류 추적, 위험도(Risk) 변경 Hook, PG사별 영수증 연동(다날·올앳·웰컴 등)
- **정산/입금**: 정산 스케줄링, 미정산/보류 금액 처리, 대출 선지급 및 실시간 정산, 입금 내역 업로드, 차액 정산
- **결제수단 모듈**: 휴대폰 결제, 현금영수증 발행, 월세앱, 정기결제, 기타 트랜잭션 처리
- **통계/대시보드**: 일/월별 매출·수수료·손익 현황, 배분 리포트
- **시스템 관리**: 공지/FAQ, 작업 스케줄, TMS 연동, 사용자 TODO 로깅, 전자계약 API 연동

## 아키텍처 개요
```
┌────────────┐       ┌───────────────────────────────────────┐
│   Client   │◀────▶│  Spring MVC (DispatcherServlet)       │
│  (Browser) │       │  - Controller (`com.pgmate.app.ctl`)  │
└────────────┘       │  - DAO (`com.pgmate.app.dao`)         │
                     │  - Session / Security / Interceptor   │
                     └───────────────────────────────────────┘
                               │
                               ▼
                     ┌────────────────────┐
                     │  Service Layer     │
                     │  (DAO + Utility)   │
                     └────────────────────┘
                               │
                               ▼
                     ┌────────────────────┐
                     │  Database (MariaDB)│
                     │  + External APIs   │
                     └────────────────────┘
```
- **내장 Tomcat 8 런처**가 `conf/service.json`을 읽어 HTTP/AJP/SSL 커넥터를 구성합니다.
- DAO 계층은 `com.pgmate.lib.dao.DAO` 기반 커스텀 ORM 유틸과 HikariCP 커넥션 풀을 사용합니다.
- 공통 유틸(`com.pgmate.lib.util`, `com.pgmate.app.util`)과 캐시(`SharedCacheMap`)가 SMS 인증키 등 단기 데이터를 관리합니다.
- Spring Security(XML)로 URL 단위 접근 제어와 2차(SMS) 인증을 처리합니다.

## 디렉터리 구조
```
MARU_APP/
├── bin/                 # Ant 빌드 스크립트 및 기동 스크립트(start/stop)
├── conf/                # 서비스/DB/로그 설정 (service.json, logback.xml ...)
├── lib/                 # 외부 라이브러리 및 빌드 산출물(JAR)
├── src/                 # Java 소스
│   ├── com/pgmate/app   # Controller, DAO, Security, Utility 등 애플리케이션 코드
│   └── com/pgmate/lib   # 공용 라이브러리(톰캣 런처, DB, 큐, 암호화 등)
├── test/                # JUnit 테스트
├── web/                 # JSP, 정적 리소스, Spring XML 설정(web.xml, cp-*.xml)
└── readme.md
```

## 요구 사항
- Java 8 (JDK 1.8)
- Apache Ant 1.9 이상
- MariaDB 또는 호환 MySQL 서버 (기본 스키마: `CP_MARU`)
- UTF-8 로케일 및 권한이 부여된 `logs/`, `web/upload` 디렉터리 생성 가능 환경

## 개발 환경 준비
1. **필수 도구 설치**: JDK 1.8, Apache Ant
2. **환경 파일 설정**: `conf/service.json`에서 DB 및 포트를 환경에 맞게 수정, `conf/logback.xml` 로그 경로 확인
3. **IDE 임포트**: IntelliJ/Eclipse에서 Java 8 프로젝트로 열기, `web/WEB-INF/classes`를 컴파일 아웃풋으로 매핑

## 빌드 가이드
```bash
ant -f bin/build.xml clean compile
- `compile` 타겟은 `web/WEB-INF/classes`에 클래스 파일을 생성하고, `lib/MARU_v1.jar`, `lib/MARU_lib-1.0.1.jar`를 만듭니다.
- Linux에서 경로 이슈가 있으면 `-Dfile.separator=/` 옵션을 추가하세요.
- 추가 의존 라이브러리는 `lib/`, `lib/apache/`, `lib/tomcat/`, `lib/spring/` 하위에 배치합니다.

## 실행 방법
### Linux / macOS
```bash
cd bin
chmod +x start.sh stop.sh
./start.sh    # 기존 프로세스가 있으면 stop.sh 실행 후 기동
```
- JVM 주요 옵션: `-DCP_CONF=../conf`, `-Dlogback.configurationFile=../conf/logback.xml`, `-Djava.io.tmpdir=../web/upload`, `-Xms1024m -Xmx2048m`

### Windows
```cmd
cd bin
start.bat   # stop.bat은 제공되지 않으므로 수동 종료 필요
```

### 종료
```bash
cd bin
./stop.sh   # pwd.pid에 기록된 PID를 종료
```

## 환경 설정 요약
| 파일 | 목적 | 주요 키 |
| --- | --- | --- |
| `conf/service.json` | DB 및 Tomcat 포트/스레드 설정 | `db.jdbcUrl`, `db.userName`, `tomcat.port`, `tomcat.host`, `tomcat.ssl.*` |
| `conf/firm.json` | 펌뱅킹 서버 연동 | `firmServer`, `firmPort`, `firmTimeout` |
| `conf/logback.xml` | 로그 정책 | `../logs/root.txt`, `../logs/access.txt` |
| `web/WEB-INF/cp-security-config.xml` | Spring Security 접근 제어 | `intercept-url`, `form-login` |
| `web/WEB-INF/cp-web-config.xml` | MVC 설정 | `context:component-scan`, `SessionInterceptor` |

## 보안 및 접근 제어
- **인증/인가**: Spring Security 기반 폼 로그인, URL별 Role 매핑(`ROLE_COMP`, `ROLE_DIST`, `ROLE_AGENCY`, `ROLE_SALES`, `ROLE_MCHT`, `ROLE_TMN` 등)
- **세션 관리**: `SessionInterceptor`가 세션 만료/비정상 요청 차단 및 접속 로그 기록, `WebCache` + `SharedCacheMap`으로 SMS 인증번호 5분 보관
- **네트워크**: AJP 및 선택적 SSL 지원, 외부 연동 Hook(`RiskChangeHook`)은 HTTPS 사용
- **로그**: `access.txt`, `root.txt` 로테이션 로그를 통해 접근 및 시스템 이벤트 추적

## 테스트
- JUnit 4.x 기반 테스트가 `test/`에 위치합니다.

## 용어 및 권한 체계
| 용어 | 설명 |
| --- | --- |
| **본사 (COMP)** | 시스템 전체를 관리하는 최고 권한 |
| **대행사 (DIST)** | 하위 가맹점/지사 관리, 일부 정산 기능 |
| **에이전시 (AGENCY)** | 대행사 산하 영업조직 |
| **지사 (SALES)** | 지역 영업조직, 제한된 거래/정산 조회 |
| **가맹점 (MCHT)** | 본인 거래/정산 정보 접근 |
| **터미널 (TMN)** | POS 단말 계정, 거래 조회 중심 |
| **Aggregator** | 복수 가맹점을 묶는 특수 유형, 시리얼 기반 인증 정책 적용 |

## 운영 가이드
1. 기본 브랜치는 `dev`, 운영 배포용은 `was1`, `was2`입니다.