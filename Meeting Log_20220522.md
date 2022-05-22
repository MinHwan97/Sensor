# 3차 회의

> 회의일시 : 2022년 5월 22일 일요일 14:00~15:00 <br/>
> 회의방식 : 비대면 (웹엑스) <br/>
> 참석자명 : 김기홍, 송민환, 홍윤기, 황윤정<br/>

<br/><br/><br/><br/>

## 1. 진행상황 공유

### 1) 기홍

<br/>
<img src="https://user-images.githubusercontent.com/55615744/169700523-95a0e0fb-9834-4dea-86d9-fb6c2c42579d.jpeg"  width="400" height="700">
<br/>

- 2층 와이파이를 몇 개 따긴 했는 데 주변에 다른 조 이야기를 들어보니까 머신러닝을 쓰는 데가 있음. (주로 랜덤포레스트) 기본적으로 알고리즘이 들어가는 데 그거 말고 머신러닝에서 학습시켜서 정확도를 높이려고 사용함. 
- AP 까지는 알아냈는데 다음주에 가서 래퍼 포인트 보고 정확도가 얼마나 되는 지 파악해볼 예정
- 와이파이 뭐 알아왔는 지 캡쳐본 같은 거는 카톡에 공유하겠다.

### 2) 민환

- 5층 저저번주에 조사했었음. 근데 저번주에 데이터 공유하는 게 어렵다고 해서 더이상 안하고 있었고, 앱 UI 어떻게 할지 찾고 있었음.


### 3) 윤기

- 데이터 모은 걸 어떻게 공유할지? 캐시에 어떻게 접근하는지를 찾아봤는 데 아직 제대로 나온 게 없었음
- 4층 담당이었고, 조사라기보다는 와이파이는 대부분 추가해놨는 데 이걸 하더라도 합치는 방법이 나와야 하니까 아직 진행중. 합치는 방법이 없으면 한 폰으로 다 해야할 거 같다. 만약에 그러면 UI 먼저 만들고 해야함. 
- 안드로이드 스튜디오로 코딩할 때 아예 데이터를 접근할 수 있게끔 열어두게 코딩하는 법도 있다고 하는 데 그것도 더 조사해봐야할 거 같다. → 이렇게 하면 난이도가 올라갈 거 같다.

### 4) 윤정

- 엑세스 포인트를 수집하는 거 담당이 아니였어서 주로 회의록 쓰는거랑 래퍼런스 참고해서 봤음


<br/><br/><br/><br/>

## 2. 업무 분배

### 1) Requirements

- Target Service Area:
– Service area (target places): 2nd, 4th, 5th floor of AI bldg.
- Build Two Apps – for admin & users:
– Admin tool for the Wi‐Fi site survey
– User application for indoor localization
- You can choose one of two approaches
– **(i) Fingerprinting,**  or (ii) Geometrical approach
- You can use any attributes (features) as the key information for your system
– Typical features/attributes you may use  : RSSI, Wi‐Fi RTT, sensors, and/or etc.
– You may use a single attribute (e.g., RSSI) or multiple combined attributes (RSSI +
sound + light/time)
- **Your Admin tool SHOULD:**
– perform the site survey
– be easy to use (so that anyone can do the site survey)
– be work solely or in the way of crowdsourcing
– The location information should be stored in a database (local or remote)
• The location DB can be easily managed by the manager
- **Your user app SHOULD:**
– have a BUTTON
• If you click the button, your app will show the current position
(e.g,. #413, 2F lobby,  5F left corridor, … )
– You can add any enhanced functions to your user app.
- You must use a remote repository to manage your team source codes
• Manage your project on your team web site (e.g., GitHub)
– Make a wiki page and post your proposal(plan)/progress in the web page
– (One of your team members) post the URL of your page on the CyberCampus (Due: 6/3 Fri)
- Your final report (ppt format) must include
– Project title (although the goal is the same for all teams)
– Team name, Member information (+e‐mail address)
– Brief description
– System architecture  (describe how location data are gathered, stored, processed, and utilized …)
– Contents (in details)
– Key features (advantages)
– Required technology for implementation
– Roles of each member: which person in the team is going to assume primary responsibility for which parts of the project (including the proposal, final report, and presentation)
– Also, prepare a PPT slide for the final presentation ( 10 min presentation)

<br/>

### 2) 앱 개발

- 한 앱에 어드민 뷰랑 유저뷰를 만들자 하나당 버튼이랑 결과나오는 것만 하는거니까 따로 따로 만들 필요 없을 것 같다. 
    - 저도 비슷하다. 사이트 서베이 공유해서 유저뷰로 나타내니까 따로 하니까 앱끼리 송신하는 게 쉽지 않을 수 있을 거 같다.
- 일단 일주일 안에 앱을 만드는 게 최우선 
- 서버와 통신하는 것까지는 복잡도가 높아질 것 같아서 일단 정확도를 최대한 높일 수 있도록 해보자 
- 일단 현재까지 래퍼 포인트를 돌아다녀보면서 50~60개를 해봤는 데 정확도가 높진 않았음. 그래서 그게 조금 걱정되긴 한다. 그래서 다음주에 2층 한 번 더 돌아다녀보면서 해볼 예정 

**→ 결론: 다음주까지 유아이 변경을 끝내고, 그다다음주에 래퍼포인트를 계속 많이 쌓아서 정확도를 높이기 (14주차까지)**

 

<br/>
<img src="https://user-images.githubusercontent.com/55615744/169700485-202a9665-0c65-4605-9653-1fa5c25c70e1.png"  width="1000" height="600">
<br/>

- 알고리즘을 하나로 고정시켜 놓고 진행 →  알고리즘 하나 지워주면 좋을 거 같음. 코드 내 알고리즘 확인하고 지우기
- 첫 페이지에서 유저랑 어드민 뷰 나누기 (버튼)
- locate me는 유저 버튼 클릭시 텍스트버튼으로 결과 나오게


<br/><br/><br/><br/>
## 3. 마무리

### 1) 결론

- 다음 주까지 앱개발 후 이후에 래퍼런스 포인트 수집을 통해서 정확도 높이기
- 각자 파트별로 나눠서 앱 개발 진행하고 다음주 수요일 수업 이후에 간단하게 상황 공유 ( 윤정은 다리 수술 입원으로 인해 참석이 어려워 이후에 상황 공유받기로 함 ) 

### 2) To-Do

- [ ]  화면 1 : 기존에 있던 알고리즘 삭제 후 첫 화면에서 어드민, 유저 뷰 분리 → 윤기
- [ ]  화면 2: 어드민 화면 + 메인 엑티비티 → 기홍
- [ ]  화면 3: 유저 화면 버튼 클릭시 현재 위치 찾는 페이지 → 민환
- [ ]  회의록 정리 및 래퍼런스 포인트 수집 시 데이터 공유 방법 조사 → 윤정
