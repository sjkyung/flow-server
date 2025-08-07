## Project Summary
실시간 대기열 시스템  
트래픽 집중 시 특정 유저만 입장 가능하도록 제어하는 시스템.
Redis Sorted Set(ZSET)을 기반으로 대기/진입 큐를 분리하여 효율적인 입장 관리와 유저 상태 추적을 구현.


## Requirements
- 트래픽 집중 시 사용자 진입을 제어할 수 있는 대기열 제공
- 사용자 요청 시 대기열 등록 및 대기 순위 조회 가능
- 정해진 기준에 따라 순차적으로 유저 입장 허용
- 부하 테스트를 통한 처리 성능 검증

## Application Architecture

```
📁 flow-server/
├── flow-core/            # Redis 기반 대기열 로직 및 진입 제어 기능
└── flow-website/         # 유저가 접근하는 실제 대상 페이지
```

## Tech Stack

| 분류         | 기술 스택                                                                    |
|------------|--------------------------------------------------------------------------|
| Language   | Kotlin, Java 21                                                          |
| Framework  | Spring Boot 3.x, Spring WebFlux, Spring Data Reactive Redis                   |
| Build Tool | Gradle (Kotlin DSL)                                                      |
| Database   | Redis                                                                    |
| Redis Usage | Redis Sorted Set(ZSET) 대기열                                               |
| Infra      | Docker, Locust    |    
| Test       | JUnit 5, AssertJ, Mockito, reactor-test (비동기 Flux/Mono 테스트) , Embedd Redis |


## Load Test
- 대기열 등록, 순위 조회, 입장 처리 등 주요 API에 대해 **동시 요청 시 응답 시간 및 처리율(RPS)** 을 측정했습니다.
- 테스트 시나리오에는 최대 동시 접속자 수, 요청 실패율, ZSET 처리 속도 등을 포함하여 성능 병목 구간을 분석했습니다. 
- 테스트 결과를 바탕으로 ZPOPMIN 처리 속도, 레디스 병렬 처리 한계, 락 경합 상황에서의 안정성을 검토했습니다.


## 기타 참고 자료
- [API 명세서(Swagger) (작성예정)](https://이미지)
- [기술 블로그 포스팅 (작성예정)](https://notion링크)