from __future__ import print_function
from gensim.models import KeyedVectors

# Creating the model
ko_model = KeyedVectors.load_word2vec_format(r'C:\Users\Admin\Desktop\ko\ko.vec')


#의뢰받은 명사들을 추출합니다.
request_nouns = ['안경', '바다']
#어플이 디비에서 가지고 있는 모든 명사를 추출합니다.
total_nouns = ['학교', '종이', '피자']



first_dict = {}
second_dict = {}
for request_noun in request_nouns:
    for total_noun in total_nouns:
        simil = ko_model.wv.similarity(request_noun,total_noun)
        second_dict[total_noun] = simil
    first_dict[request_noun] = second_dict

print(first_dict)