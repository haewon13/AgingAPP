# AgingApp
<h3> '아이 사진관' Android Application  / 실종아동의 현재 나이 이미지 생성 및 찾기 프로젝트 (딥러닝 GAN 기반) </h3>
<BR>
  <h3>폴더에 관한 설명입니다.</h3>

  
### ● crawling
크롤링에 관한 코드와 데이터입니다.
Google 이미지에서 특정 키워드로 검색해서 나온 결과를 각 url 수집 후 이미지를 다운로드 받았습니다. 

### ● data
지금까지 확보한 데이터 셋입니다.  
(전체 데이터셋을 용량 및 저작권 문제로 업로드가 되지 않아 데이터셋별로 예시 이미지를 첨부하였습니다.)

### ● family classifier
이 폴더에 있는 코드는 IPCGAN에 통합할 Family Classifier의 모델, Family Classifier 데이터를 만들기 위한 딥러닝 모듈인 Face Point Detection 모듈에 대한 코드입니다.

### ● test case
IPCGAN의 test 결과입니다. 이에따라 데이터셋의 변화를 주기로 했습니다.
