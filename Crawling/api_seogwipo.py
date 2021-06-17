#!/usr/bin/env python
# coding: utf-8

# In[ ]:


from urllib.request import urlopen
from urllib.parse import urlencode, unquote, quote_plus
import requests
import json

fileName = 'seogwipo.json'
file = open(fileName, 'w', encoding='utf-8')

apiUrl = '### api url ###'

names_key = { 'end': 'endDate',
             'start': 'startDate',
             'pay': 'explanation',
             'title': 'cultureName',
             'cname': 'location',
             'category': 'culture' }


def getJson(url):
    get_data = requests.get(url)
    json = get_data.json()
    return json

def update(jsonBody):
    for i in range(1, 31):
            i = str(i)
            try:
                final = jsonBody['exMap'][i]
            except IndexError:
                pass
            except KeyError:
                print('null')
                continue
            for row in final:
                for k, v in names_key.items():
                    for old_name in row:
                        if k == old_name:
                            row[v] = row.pop(old_name)

    return final

def save(data):
    data = body['exMap']
    json.dump(data, file, indent=4, ensure_ascii=False)

body = getJson(apiUrl)
final = rename(body)
save(final)

print(final)

file.close()



