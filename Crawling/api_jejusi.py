#!/usr/bin/env python
# coding: utf-8

# In[ ]:


from urllib.request import urlopen
from urllib.parse import urlencode, unquote, quote_plus
import requests
import json
import xmltodict

fileName = 'jejusi.json'
file = open(fileName, 'w', encoding='utf-8')

(/, https://www.data.go.kr/data/15002244/openapi.do)
apiUrl = '### api url ###'

names_key = { 'edate': 'endDate',
             'sdate': 'startDate',
             'info': 'explanation',
             'title': 'cultureName' }

def getJson(url):
    get_data = requests.get(url)
    xpars = xmltodict.parse(get_data.text)
    jsonDump = json.dumps(xpars)
    jsonBody = json.loads(jsonDump)
    return jsonBody

def rename(Body):
    final = Body['rfcOpenApi']['body']['data']['list']    
    for row in final:
        for k, v in names_key.items():
            for old_name in row:
                if k == old_name:
                    row[v] = row.pop(old_name)
    return final
    
def save(data):
    json.dump(data, file, indent=4, ensure_ascii=False)
    

for i in range(0, 61):
    queryString = "?" + urlencode({
    "ServiceKey" : unquote("### your key ###"),
    "pageNo" : i,
    "numOfRows" : 10
    })
    
    body = getJson(apiUrl+queryString)
    result = rename(body)
    save(result)

file.close()



