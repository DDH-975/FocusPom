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
## 🛠️ 기술 스택

- **언어 (Languages)**: Kotlin, XML
- **도구 (Tools)**: Android Studio
- **라이브러리 (Libraries)**: Android SDK, Room, ✨[MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)✨

- **기술 적용**:
  - **DrawerLayout** : 사이드 메뉴를 통해 통계 페이지 및 다른 화면으로 쉽게 이동할 수 있도록 네비게이션 구성.
  - **Room** : 로컬 데이터베이스를 활용하여 사용자의 공부 기록 및 통계 저장.
  - **[MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)** : 공부 시간을 효과적으로 분석할 수 있도록 **파이 차트(Pie Chart), 바 차트(Bar Chart), 라인 차트(Line Chart)** 를 활용해 시각화
  - **코루틴 (Coroutines)**: 비동기 작업 최적화
    - `suspend fun`, `withContext`, `launch`를 활용하여 데이터베이스 작업 처리.
<br> <br>


## 실행 화면 (Screenshots & GIFs)

<h3>📌 다양한 공부 모드</h3>

<table>
  <tr>
    <th>짧은 집중 모드</th>
    <th>기본 모드</th>
    <th>롱 포커스 모드</th>
    <th>울트라 포커스 모드</th>
    <th>커스텀 모드</th>
  </tr>
  <tr>
    <td><img src="screenshot/short.png" width="220"/></td>
    <td><img src="screenshot/basic.png" width="220"/></td>
    <td><img src="screenshot/long.png" width="220"/></td>
    <td><img src="screenshot/ultra.png" width="220"/></td>
    <td><img src="screenshot/custom.png" width="220"/></td>
  </tr>
</table>

<br>

### ⏳ 타이머 실행 예시 기본 모드(25분 공부 + 5분 휴식), 커스텀 모드
| **기본 모드** | **커스텀 모드** |
|:-:|:-:|
| ![기본](기본모드.gif) | ![커스텀](커스텀.gif) |
<br>

### 📊 공부 통계 화면
<img src="screenshot/statistics.gif" alt="App Demo" width="250">
  




