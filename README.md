# Project name : FocusPom (Focus🧠 + Pomodoro🍅)

## 프로젝트를 진행 하게된 계기
저는 평소 **포모도로(Pomodoro) 기법**을 활용해 공부하거나 프로젝트를 진행하는 편입니다.
포모도로 기법은 **일정한 시간 동안 집중해서 작업한 뒤 짧은 휴식을 반복하는 방식**으로, 일반적으로 **25분 집중 + 5분 휴식**을 기본 단위로 합니다.

그동안 저는 유튜브에서 포모도로 타이머 영상을 보며 공부했지만, 매번 컨디션에 따라 공부 시간과 휴식 시간을 조절하려 할 때마다 원하는 영상을 찾기가 번거로웠습니다.
또한, 공부가 끝난 후 정확한 공부 시간을 확인하기 어려운 점도 불편했습니다.

이런 불편함을 해결하기 위해, 다양한 공부 모드를 제공하고 사용자가 자유롭게 시간을 설정할 수 있는 **포모도로 앱**을 직접 개발하게 되었습니다.
또한, 공부 시간을 효과적으로 관리할 수 있도록 시각적인 통계를 추가해, 보다 체계적으로 학습할 수 있도록 했습니다.

## 설명
**포모도로 기법**을 활용하여 공부 시간을 효율적으로 관리할 수 있는 애플리케이션입니다.<br>
사용자는 **여러 가지 공부 모드** 중 선택하거나, **커스텀 타이머** 기능을 이용해 자신만의 공부 및 휴식 시간을 설정할 수 있습니다.<br>
또한, **공부 통계 기능**을 통해 하루 동안 공부한 시간과 모드별 누적 공부 시간을 쉽게 확인할 수 있습니다.<br>
<br> <br>

## 기능

- **포모도로 기법**을 활용하여 집중력과 생산성을 극대화할 수 있는 타이머 제공.
- **공부 통계**을 제공하여 오늘 공부한 시간 및 모드별 누적 공부 시간 확인.
- **다양한 공부 모드**:
  - **짧은 집중 모드**: 15분 공부 + 3분 휴식
  - **기본 모드**: 25분 공부 + 5분 휴식
  - **롱 포커스 모드**: 50분 공부 + 10분 휴식
  - **울트라 포커스 모드**: 90분 공부 + 20분 휴식
  - **커스텀 모드**: 원하는 공부 및 휴식 시간 직접 설정 가능.


<br> <br>
## 기술 스택

- **언어 (Languages)**: Kotlin, XML
- **도구 (Tools)**: Android Studio
- **라이브러리 (Libraries)**: Android SDK, Glide, Retrofit, Room
- **기술 적용**:
  - **Retrofit**: **넥슨 FCONLINE4 오픈 API**와의 통신을 통해 유저의 경기 전적 및 상세 기록 조회.
  - **Glide**: 이미지 로딩 및 표시 최적화, 플레이어가 경기에서 실제 사용한 선수 사진 조회
  - **Room**: 로컬 데이터베이스 사용, 유저의 즐겨찾기 선수 및 경기 기록 저장.
  - **RecyclerView**: 유연하고 효율적인 리스트 구현, 명언 및 경기 기록 표시.

<br> <br>

## 개발 중 겪은 문제와 그 해결 과정
앱을 개발하던 중, **Room 데이터베이스**에서 데이터를 메인 스레드에서 직접 불러오려고 시도했을 때 앱이 강제 종료되는 현상이 발생했습니다. 이는 안드로이드에서 **메인 스레드에서 데이터베이스 작업을 처리할 수 없다는 제약** 때문에 발생한 문제였습니다.

### 문제 원인
메인 스레드에서 Room DB에 접근하려고 했기 때문에 **UI 스레드가 블로킹**되어 앱이 크래시가 발생한 것입니다. 안드로이드에서는 UI 스레드에서 시간이 오래 걸리는 작업을 하면 앱이 응답하지 않게 되므로, 데이터베이스 작업은 항상 **백그라운드 스레드**에서 처리해야 한다는 점을 간과한 것이었습니다.

## 해결과정
이 문제를 해결하기 위해, 데이터베이스 작업을 **별도의 스레드**에서 처리하도록 변경했습니다. 이를 위해 **Executor**를 사용해 별도의 스레드를 만들고, Room DB에 데이터를 요청했습니다. 이후 **UI 업데이트는 메인 스레드에서** 처리하도록 했습니다. 이를 위해 runOnUiThread() 메서드를 사용해 UI 업데이트를 메인 스레드에서 안전하게 처리할 수 있도록 했습니다.

또한, 체크박스 클릭 시 **즐겨찾기 데이터**를 **추가하거나 삭제하는 로직**에서도, 백그라운드 스레드를 사용하여 데이터베이스 작업을 처리했습니다. 이로써 **UI 스레드의 블로킹을 방지**하고 앱이 정상적으로 동작하게 되었습니다.

## 결과
이 방식으로 백그라운드 스레드를 활용해 데이터베이스 작업을 안전하게 처리하고, UI 업데이트는 메인 스레드에서 처리함으로써 앱이 정상적으로 작동하게 되었습니다. 이 과정에서 **스레드 처리**와 **Room 데이터베이스 연동 방식**에 대한 깊은 이해를 얻을 수 있었습니다.

<br> <br>
