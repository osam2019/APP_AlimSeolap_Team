from __future__ import print_function
from flask import Flask, request, session, redirect, url_for, abort, render_template, flash,send_file
from flask_restful import reqparse, abort, Api, Resource
from konlpy.tag import Kkma
from konlpy.utils import pprint
from gensim.models import KeyedVectors
import matplotlib.pyplot as plt
from wordcloud import WordCloud
import json

app = Flask(__name__)

@app.route('/')
def hello_world():
    return '{"result": "null"}'

#이쪽 url로 post를 보내면 이 함수가 반응합니다
@app.route('/api/analyze-sentence', methods =['POST'])
def post_action():
    if request.method == 'POST':
        #json 데이터를 받습니다.
        json_data = request.get_json()
        print('json_data : {json_data}')

        #꼬꼬마 객체를 생성
        kkma = Kkma()
        #api 함수를 사용해 명사를 추출합니다
        nouns_list = kkma.nouns(json_data['sentence'])
        print(nouns_list)
        #list 자료구조를 json 형식으로 변환합니다.
        nouns_json = json.dumps(nouns_list)
        
        nouns_json = '{"result":' + nouns_json + "}"
        
        print(nouns_json)
        
        #명사들을 return 해줍니다
        return nouns_json
    return 'none POST!'

#유사도 분석을 수행합니다. 
@app.route('/api/similarity-analysis', methods =['POST'])
def similarity_analysis():
    if request.method == 'POST':
        #워드 임베딩 모델 파일을 가져옵니다. 절대경로로 지정되어 있으니 적절히 수정하세요.
        ko_model = KeyedVectors.load_word2vec_format('./ko.vec')
        dict_data = request.get_json()
        
        #의뢰받은 명사들을 추출합니다.
        request_nouns = dict_data['request_noun']
        #어플이 디비에서 가지고 있는 모든 명사를 추출합니다.
        total_nouns = dict_data['total_nouns']

        #return할 json 형식 데이터를 만듭니다.
        first_dict = {}
        for request_noun in request_nouns: 
            second_dict = {}
            for total_noun in total_nouns:
                simil = ko_model.wv.similarity(request_noun,total_noun)
                second_dict[total_noun] = str(simil)
            first_dict[request_noun] = second_dict
        return json.dumps(first_dict)

@app.route('/api/get-wordcloud', methods =['POST'])
def get_wordcloud():
    if request.method == 'POST':
        font_path = './kohana.ttf'
        wordcloud = WordCloud(
        font_path = font_path,
        width = 500,
        height = 500,
        background_color='white'
        )

        nouns_dict = request.get_json()
        # f = open("nouns.txt", 'w')
        # for noun in nouns_dict.keys():
        #     for i in range(1, abs(nouns_dict[noun])):
        #         f.write(noun + "\n")
        # f.close()


        # text = open("nouns.txt").read()
        # wordcloud = wordcloud.generate_from_text(text)
        for noun in nouns_dict:
            nouns_dict[noun] = abs(int(nouns_dict[noun]*100))
        wordcloud = wordcloud.generate_from_frequencies(nouns_dict)

        fig = plt.figure(figsize=(12,12))
        plt.imshow(wordcloud)
        plt.axis("off")
        fig.savefig('nouns_cloud.png')

        filename = 'nouns_cloud.png'
        
        return send_file(filename, mimetype='image/png')



if __name__ == '__main__':
    app.debug = True

    #배포용
    app.run(host='0.0.0.0', port='80') 

    #로컬호스트 테스트용
    # app.run()
