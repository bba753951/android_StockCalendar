import pandas as pd
import requests
from urllib.request import urlopen
from bs4 import BeautifulSoup
from sqlalchemy import create_engine
import MySQLdb
import sys,traceback

db = MySQLdb.connect(
        host='localhost',
        port = 3306,
        user='******',
        passwd='******',
        db ='*******',
        charset='utf8',
        )
cursor= db.cursor()
cursor.execute("DROP TABLE IF EXISTS `calendar_a`")
cursor.execute("DROP TABLE IF EXISTS `calendar_b`")

sql="CREATE TABLE `calendar_a`(公司資訊 VARCHAR(15),股利所屬年度 VARCHAR(5),權利分派基準日 DATE,盈餘轉增資配股 text,法定盈餘公積、資本公積轉增資配股 text,除權交易日 text,盈餘分配之股東現金股利 text,法定盈餘公積、資本公積發放之現金 text,除息交易日 text,現金股利發放日 text,現金增資總股數 text,現金增資認股比率 text,現金增資認購價 text,公告日期 text,公告時間 text,普通股每股面額 text,eventcolor VARCHAR(10),typek VARCHAR(5))DEFAULT CHARSET=utf8;"
sql1="CREATE TABLE `calendar_b`(公司資訊 VARCHAR(25),公司地址 text,會別 VARCHAR(10),股東常臨時會日期 text,開會地點 text,是否改選董監 VARCHAR(5),聯絡電話 text,股務單位 text,股務單位電話 text,行使期間 text,電子投票平台 text,投票網址 text,eventcolor VARCHAR(10),typek VARCHAR(5))DEFAULT CHARSET=utf8;"
cursor.execute(sql)
cursor.execute(sql1)
cursor.close()
db.commit()
db.close()

def year(year):
    if year=="NaN":
        return "NaN"
    else:
        year=year.split('/')
        year[0]=str(int(year[0])+1911)
        year='-'.join(year)
        return year


engine = create_engine('mysql+pymysql://bba753951:624001479@localhost:3306/bba753951_database?charset=utf8',echo=False)
def fun(kind):
    try:
        color={'sii':'#FF7D33','otc':'#1DCBBE','rotc':'#4A6812','pub':'#BB4CA8'}
        typek={'sii':'上市','otc':'上櫃','rotc':'興櫃','pub':'公開發行'}
        column=['公司代號','公司名稱','股利所屬年度','權利分派基準日','盈餘轉增資配股','法定盈餘公積、資本公積轉增資配股','除權交易日','盈餘分配之股東現金股利','法定盈餘公積、資本公積發放之現金','除息交易日','現金股利發放日','現金增資總股數','現金增資認股比率','現金增資認購價','公告日期','公告時間','普通股每股面額']
        r='http://mops.twse.com.tw/mops/web/ajax_t108sb27?encodeURIComponent=1&step=1&firstin=1&off=1&keyword4=&code1=&TYPEK2=&checkbtn=&queryName=&TYPEK={}&co_id_1=&co_id_2=&year=108&month=&b_date=&e_date=&type='.format(kind)
        data=pd.read_html(r,encoding='utf-8')[0]
        data=data.drop([0,1],axis=0)
        data.columns = column
        data['公司資訊'] =data.apply(lambda x: str(x['公司代號'])+str(x['公司名稱']+"(除)"),axis=1)
        data['eventcolor']=color[kind]
        data['typek']=typek[kind]
        data=data.fillna("NaN")
        data=data[data['公司代號'].str.isdigit()]
        data['除權交易日'] =data.apply(lambda x: year(x['除權交易日']),axis=1)
        data=data.drop(['公司代號','公司名稱'],axis=1)
        pd.io.sql.to_sql(data,'calendar_a', engine,  if_exists='append',index=False)
        print(data.shape)
        print('--------------------------success_'+kind+'_a')


        
    except:
        print('--------------------------no_'+kind+'_a')
        traceback.print_exc()

def fun_b(kind):
    try:
        color={'sii':'#FF7D33','otc':'#1DCBBE','rotc':'#4A6812','pub':'#BB4CA8'}
        typek={'sii':'上市','otc':'上櫃','rotc':'興櫃','pub':'公開發行'}
        column1=['公司代號','公司名稱','公司地址','會別','股東常臨時會日期','開會地點','是否改選董監','聯絡電話','股務單位','股務單位電話','行使期間','電子投票平台','投票網址']

        r1='http://mops.twse.com.tw/mops/web/ajax_t108sb31?encodeURIComponent=1&run=Y&step=1&TYPEK={}&year=108&firstin=true'.format(kind)
        data1=pd.read_html(r1,encoding='utf-8')
        data1=data1[0].drop([0],axis=0)
        data1.columns = column1
        data1['公司資訊'] =data1.apply(lambda x: str(x['公司代號'])+str(x['公司名稱']+"(股)"),axis=1)
        data1['eventcolor']=color[kind]
        data1['typek']=typek[kind]
        data1=data1[data1['公司代號'].str.isdigit()]
        data1['股東常臨時會日期'] =data1.apply(lambda x: year(x['股東常臨時會日期']),axis=1)
        data1=data1.drop(['公司代號','公司名稱'],axis=1)

        print(data1.shape)
        pd.io.sql.to_sql(data1,'calendar_b', engine,  if_exists='append',index=False)
        print('--------------------------success_'+kind+'_b')
    except:
        print('--------------------------no_'+kind+'_b')
        traceback.print_exc()
a=['sii','otc','rotc','pub']
for i in a:
    fun(i)
    fun_b(i)
engine.dispose()

