Stock Caelndar
===
## 概念：
對於投資股票的人來說，股東會與除權息是個很重要的日期
因為通常股票都會在這個日期前後有很大的變動
因此決定設計一個app方便使用者存取資訊到手機上的日曆，不僅可以隨時觀看，也能設置提醒

## 資料處理
1. 使用python 製作爬蟲程式到“公開資訊觀測站”爬取需要的資訊

    - 公開資訊觀測站
    http://mops.twse.com.tw/mops/web/index
    - 除權息資訊
    http://mops.twse.com.tw/mops/web/t108sb27
    - 股東會資訊
    http://mops.twse.com.tw/mops/web/t108sb31_q1

2. 並使用linux排程設定每天早中晚各抓一次資料保持更新
3. 將資料處理後存到mysql database
4. 利用django製造一個可以回傳json資料的網址


## Android 程式概念
使用AsyncTask抓取網路json資料
呼叫第二個Acitvity並使用RecyclerView呈現
然後將使用者勾選的項目新增到日曆
## app使用前準備
需要先打開calendar的權限
手機setting ->App&notifications -> App permissions -> Calendar 
![](https://i.imgur.com/s0grXOj.png)


## 成果
![](https://i.imgur.com/EMalbd5.gif)


## 錯誤處理
### 使用者輸入不完全時
![](https://i.imgur.com/bEmiZIH.gif)

### 使用者沒有網路時
![](https://i.imgur.com/lGXAXe0.gif)


### 當使用者沒有勾選項目時會提醒
![](https://i.imgur.com/uflDBbU.gif)


