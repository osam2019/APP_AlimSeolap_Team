import matplotlib.pyplot as plt
from wordcloud import WordCloud
import json

font_path = r'C:\Users\Admin\Desktop\ko\kofont.ttf'
wordcloud = WordCloud(
   font_path = font_path,
   width = 200,
   height = 200
   )

json_data = '{"안경": 5,"피자": 4,"학교": 3,"노트북": 2,"안녕": 1}'

nouns_dict = json.loads(json_data)

f = open("nouns.txt", 'w')
for noun in nouns_dict.keys():
   for i in range(1, nouns_dict[noun]):
      f.write(noun + "\n")
f.close()


text = open("nouns.txt").read()
wordcloud = wordcloud.generate_from_text(text)

fig = plt.figure(figsize=(12,12))
# plt.imshow(wordcloud)
# plt.axis("off")
# plt.show()
fig.savefig('nouns_cloud.png')
