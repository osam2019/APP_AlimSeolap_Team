# Back-End
자연어처리 기능을 수행하는 Flask 기반 백엔드 API 서버입니다.

## API

 - `POST` **/api/analyze-sentence**
   - **object** *(body)*: 
   ```javascript
   {
       "sentence": "입력할 문장"
   }
   ```
   - **response**
   ```javascript
   {
       "result": ["결과값 리스트(배열)"]
   }
   ```
   
 - `POST` **/api/similarity-analysis**
   - **object** *(body)*: 
   ```javascript
   {
    "request_noun": ["오늘", "치킨"],
    "total_nouns" : ["신발", "피자", "행사"]
   }
   ```
   request_noun : 의뢰하고자 하는 명사를 리스트로 넣으세요.  
   total_nouns : 당신이 value를 가지고 있는 명사를 리스트로 넣으세요.
   
   - **response**
   ```javascript
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
   ```javascript
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
     
<img src="./nouns_cloud.png" width="90%"></img>

## Dependancy
 - Python `(>=3)`
 - JDK `(>=8)`
 - pip install
   - flask
   - flask_restful
   - konlpy
   - gensim
   - matplotlib
   - wordcloud
  
## 서버 구동

```bash
$ python -m pip install {의존성 관련 라이브러리 설치}
$ python server.py
```
