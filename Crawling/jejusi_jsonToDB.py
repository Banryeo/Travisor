#!/usr/bin/env python
# coding: utf-8

# In[ ]:


import pymysql.cursors
import json


with open('jejusi.json', 'r') as f:
    file = json.load(f)

connection = pymysql.connect(host='##',
                             user='##',
                             password='##',
                             db='##',
                             charset='utf8')

curs = connection.cursor()

for i in file:
    cultureName = i['cultureName']
    startDate = i['startDate']
    endDate = i['endDate']
    location = i['location']
    explanation = i['explanation']
    
    sql = "INSERT INTO Model (cultureName, startDate, endDate, location, explanation) VALUES (%s, %s, %s, %s, %s)"
    values = (cultureName, startDate, endDate, location, explanation)
    curs.execute(sql, values)
    
    connection.commit()
    
print(done)
file.close()

