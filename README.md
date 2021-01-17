# Field_test_Backend
## Test Objectives 1
* 使用sysbench壓測mysql，執行結果紀錄於StressTestReport.PDF
  * 測試時間為60秒
  * 測試筆數為1000000
  * 測試腳本oltp.lua

## Test Objectives 2
* 建立database
  * name:testDB
  * user:root
* 建立table
  * uuid INT unsigned auto_increment PRIMARY KEY
  * Date char(10)
  * Time char(10)
  * usec char(10)
  * SourceIP char(20)
  * SourcePort char(10)
  * DestinationIP char(20)
  * DestinationPort char(10)
  * FQDN char(100)
* 使用java_io.pkts解包pacp，發現解析資料不如預期，後續查找方式無果，原打算使用其他解包方式，因時間不足，故此題無法於時程內完成。
* io.pkts解包方式於附件內

## Test Objectives 3
* 於centos內架設nginx伺服器
* 已打包jar檔，可使用指令 [ java -jar /home/connect.jar ] 直接執行
* healthy.php
  > 該php用於判斷是否與mysql連線
  
  > 如連線成功呈顯json格式，內容為{"mysql_status":"OK"}
  
  > 如連線失敗呈顯json格式，內容為{"mysql_status":"ERROR"}
  
  > 後續用於回傳java判斷log寫入
  
* config.ini
  > 設定連線資訊，判斷使用指定http GET PHP的頁面回傳

* connectMySQL.java
  > 解析config.ini取得html網址
  
  > 依照取得的html網址( Http://127.0.0.1/healthy.php )發送request，取得頁面回傳資訊
  
  > 依照頁面響應資訊分三種：
  
  > * 成功取得頁面資訊，且mysql連線成功
  
  >  > log寫入格式：[2019-07-08 09:32:09.284321] OK:PID:32471, response: {"mysql_status":"OK"}
  
  > * 成功取得頁面資訊，但mysql連線失敗
  
  >  > log寫入格式：[2019-07-08 09:31:09.284321] ERROR:PID:32472 ,response: {"mysql_status":"ERROR"}
  
  > * 頁面資訊取得失敗
  
  >  > log寫入格式：[2019-07-08 09:31:09.284321] ERROR:PID:32473 ,ERROR-Can’t connect to Http://127.0.0.1/healthy.php
  
* CornTab設定為每五分鐘執行一次連線測試

  > */5 * * * * java -jar /home/connect.jar
  
