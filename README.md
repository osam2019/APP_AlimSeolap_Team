# Overview

알림서랍의 탄생은 아주 일상적인 순간에서 시작됩니다. 스마트폰을 켰는데 많은 알림들이 와있어 상태바가 지저분해져 있는 그 순간이요. 알림들 중에는 유용한 알림들도 몇 있지만 대다수는 불필요한 정보의 알림입니다. 그래서 생각했습니다. 

> ### 이 많은 알림들 중에서 내게 딱 필요한 알림들만 확인할 순 없을까?

이 질문으로 알림서랍은 세상으로 나왔습니다. 알림서랍은 스마트폰에 무작위로 밀려드는 푸시 알림들을 당신의 취향에 맞춰 필터링 해주는 앱입니다. 사용하면 사용할수록 정교해지는 알림서랍 서비스를 이용해보세요!



# 본 Repository는 크게 3 부분으로 나눠집니다.





- ## Python_Server

  자연어처리 기능을 수행하는 Flask 기반 API 서버입니다.

  

  ### API

  - `POST` **/api/analyze-sentence**

    - **object** *(body)*: 

    ```
    {
        "sentence": "입력할 문장"
    }
    ```

    명사만 추출하고 싶은 문장을 넣으세요

    - **response**

    ```
    {
        "result": ["결과값 리스트(배열)"]
    }
    ```

    명사들을 리스트로 반환해줍니다.

    

  - `POST` **/api/similarity-analysis**

    - **object** *(body)*:

    ```
    {
     "request_noun": ["오늘", "치킨"],
     "total_nouns" : ["신발", "피자", "행사"]
    }
    ```

    request_noun : 의뢰하고자 하는 명사를 리스트로 넣으세요.
    total_nouns : 당신이 value를 가지고 있는 명사를 리스트로 넣으세요.

    - **response**

    ```
    {
        {
        '오늘': {'신발': 0.08428423, '피자': 0.105281755, '행사': 0.2277687}, 
        '치킨': {'신발': 0.06422423, '피자': 0.6277687, '행사': 0.105281755}
        }
    }
    ```

    명사 간의 유사도를 json 형식으로 반환합니다.

    

  - `POST` **/api/get-wordcloud**

    - **object** *(body)*:

    ```
    {
        "치킨": 7.56,
        "피자": 6.01,
        "목걸이": -0.0056,
        "일본": -2.6486
    }
    ```

    total 명사와 그 명사의 value 값을 json 형식으로 보내주시면 됩니다.   

    value 절대값이 클수록 큰 글자로 표현하고자합니다.

    - **response**

  [![img](https://github.com/noti-dropper/backend/raw/master/nouns_cloud.png)](https://github.com/noti-dropper/backend/blob/master/nouns_cloud.png)

  ### Dependancy

  - Python `(>=3.5)`
  - JDK `(>=8)`
  - pip install
    - flask
    - flask_restful
    - konlpy
    - gensim
    - matplotlib
    - wordcloud

  ### 서버 구동

  ```
  $ python -m pip install {의존성 관련 라이브러리 설치}
  $ python server.py
  ```



***

- ## Notification_Provider

  가상의 알림을 만들고 발송하는 앱입니다.

  

  ### Dependancy

  - JDK `(>=8)`
  - Android Studio `(>=3)`
  - Android API Level 2

## 

- ## Notification_Drawer

  알림서랍 메인 앱입니다. 



### 	 Dependancy

- - JDK `(>=8)`
  - Android Studio `(>=3)`
  - Android API Level 29

## 

***



# Developer Contact

버그 및 오류 발생시 Contact 

###### 우리 같이 토의해봐요!

### API Server Developer

![image-20191024140541092](C:\Users\Admin\AppData\Roaming\Typora\typora-user-images\image-20191024140541092.png)



### Android Backend Developer

![image-20191024140916724](C:\Users\Admin\AppData\Roaming\Typora\typora-user-images\image-20191024140916724.png)

![image-20191024141255834](C:\Users\Admin\AppData\Roaming\Typora\typora-user-images\image-20191024141255834.png)



### Android Frontend Developer

![image-20191024144201521](C:\Users\Admin\AppData\Roaming\Typora\typora-user-images\image-20191024144201521.png)







# 곧이어 업데이트 사항

1. 문장을 파악하기 위해서 문장 내부에 있는 명사를 모두 살필 필요는 없을겁니다. 더 중요한 유효 의미 명사를 가려내는 알고리즘을 구상하고 있습니다.
2. 현재는 유저의 취향 모델을 명사들에 대한 선호도의 조합으로 설계하였습니다. 더 정교한 취향 모델링을 위해 딥러닝 구조의 모델을 구상하고 있습니다.
3. 조금 더 예쁜 디자인! 디자이너와 프론트 개발자가 집을 못 갈 예정입니다.









# License

MIT License

Copyright (c) 2019 Gonglee

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

