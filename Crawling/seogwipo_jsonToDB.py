#!/usr/bin/env python
# coding: utf-8

# In[ ]:


import pymysql.cursors
import json
import datetime

with open('seogwipo.json', 'r') as f:
    file = json.load(f)

connection = pymysql.connect(host='##',
                             user='##',
                             password='##',
                             db='##',
                             charset='utf8')

curs = connection.cursor()

def convertTime(unixTime):
    unixTime = unixTime[:-3]
    unixTime = int(unixTime)
    realTime = datetime.datetime.fromtimestamp(unixTime).strftime('%Y-%m-%d')
    
    return realTime
    
for day in range(32):
    try:
        day = str(day)
        data = file[day]

        for i in range(len(data)):
            culture = data[i]['culture']
            cultureName = data[i]['cultureName']
            startDate1 = data[i]['startDate']
            startDate = str(startDate1)
            startDate = convertTime(startDate)
            endDate1 = data[i]['endDate']
            endDate = str(endDate1)
            endDate = convertTime(endDate)
            location = data[i]['location']
            explanation = data[i]['explanation']

            if culture == '002':
                sql = "INSERT INTO Model (culture, cultureName, startDate, endDate, location, explanation) VALUES (%s, %s, %s, %s, %s, %s)"
                values = ('행사', cultureName, startDate, endDate, location, explanation)
                curs.execute(sql, values)
            elif culture == '003':
                sql = "INSERT INTO Model (culture, cultureName, startDate, endDate, location, explanation) VALUES (%s, %s, %s, %s, %s, %s)"
                values = ('행사', cultureName, startDate, endDate, location, explanation)
                curs.execute(sql, values)
            elif culture == '004':
                sql = "INSERT INTO Model (culture, cultureName, startDate, endDate, location, explanation) VALUES (%s, %s, %s, %s, %s, %s)"
                values = ('전시', cultureName, startDate, endDate, location, explanation)
                curs.execute(sql, values)

        connection.commit()
        
    except IndexError:
        pass
    except KeyError:
        print('null')
        continue
    

print(done)
file.close()

