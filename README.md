# AgingApp
<h2> '아이 사진관' Android Application </h2>
<h3>/ 딥러닝 GAN 기반 실종아동의 현재 나이 이미지 생성 및 찾기 프로젝트 
  <BR>-> Face Aging Gan기반의 아동의 나이 든 모습 생성 모바일 어플리케이션 </h3>
<BR><BR>

**Table of Contents**

[TOCM]

[TOC]


### ● Purpose
현재까지 실종 아동을 찾을 수 있는 대부분의 방법은 실종 당시의 사진을 전단지나 TV매체를 통해 알리는 것이 대부분이었다. <BR>
그러나 장기 실종자의 경우 실종 당시의 사진만으로 찾기에는 많은 어려움이 있다. 실종아동의 성장 후 모습을 예측하여 찾는 시스템이 필요하지만 
현재 노화 기술의 정확도로는 한계가 있다. <BR>
노화기술의 정확도 향상을 위한 데이터를 수집할 수 있는 어플리케이션을 개발하였고 이 기능을 구현하기 위해 Face-Aging GAN기반의 IPCGAN을 이용하였다. <BR><BR>

### ● ※ 
사용자가 5,6,7세 아동의 모습을 촬영하거나 사진을 첨부 하면 해당 상세정보는 메인 서버로 전송되어 16~19세의 나이대로 노화된 모습의 결과물을 생성한다.<BR> 
해당 생성된 사진을 다시 앱으로 전송하여 사용자가 현재의 모습과 비교할 수 있고 화면 하단의 공유 버튼을 누르면 원하는 공유 수단(ex. 카카오톡)을 이용하여 사진을 전송할 수 있다.<BR><BR> 

### ● Demo Videos
`<link>`
1. 최종 시연영상 -> https://www.youtube.com/watch?v=VGtunDVEt3g
2. 튜토리얼 시연영상 -> https://www.youtube.com/watch?v=Mn18600nDRM
<BR><BR>
  
### ● UI images
본 어플리케이션에는 크게는 2가지 기능이 있다. <BR>
- 5,6,7세의 사용자가 정면의 모습을 촬영하면 메인서버에서 IPCGAN을 거쳐 15~19세의 노화된 모습의 결과물을 받는 노화 카메라 기능<BR>
- 5,6,7세의 모습과 15~19세의 모습, 총 2장의 사진을 첨부하여 변환된 모습과 실제 모습을 비교해볼 수 있는 기능<BR>
각 기능에 대한 예시 UI를 첨부하였다.<BR><BR>
  <BR> <h3> 1- Intro, 튜토리얼 </h3><BR><BR>
    <img src="https://github.com/haewon13/AgingAPP/blob/master/UI/1.JPG" width=100% ><BR>   
    <BR> <h3> 2- 첫 번째 기능 </h3><BR> 
<img src="https://github.com/haewon13/AgingAPP/blob/master/UI/2.JPG" width=100% ><BR>
      <img src="https://github.com/haewon13/AgingAPP/blob/master/UI/3.JPG" width=100% ><BR>  
      
<BR> <h3> 2- 두 번째 기능 </h3><BR> 
<img src="https://github.com/haewon13/AgingAPP/blob/master/UI/4.JPG" width=100%> <img src= "https://github.com/haewon13/AgingAPP/blob/master/UI/5.JPG" width=300 > <BR>
<BR><BR>
  
### ● Web Server PHP code
Linux 서버에 APM 구축 뒤 APP<->서버 HTTP 프로토콜로 해당 php code를 불러서 이미지를 저장 후 변환시켰다.<BR>
name: updown.php 코드
  
    
  <BR><BR>
