# 老鼠的 「飛機盃」生存床戰 使用手冊

## 玩法簡介
玩家分為最多四隊，玩法大致上與床戰相同，與一般床戰不同的機制有以下幾點！
* 死亡噴裝<br>
```原則上不噴裝，只有礦物類資源會因死亡而損失一半```
* 核心保護時間<br>
```目前設定在30分鐘後可開始拆床```
* 生存世界 (重點)<br>
```每個隊伍可以擁有自己專屬的生存世界，可在內取得生存物資、礦物```
* 黑曜石剋星<br>
```可在一瞬間擊破黑曜石```
* 裝備耐久度<br>
```鑽石工具耐久度消耗速度四倍、其他工具三倍、裝備兩倍```

雖然不同的點只有以上五點，但因為開放生存世界，可取得很多在床戰中拿不到的物資。另外合成配方表除了一些較特殊的物品(如:床)，幾乎沒什麼限制，也可以利用紅石機關來蓋出屬於自己的堡壘！

## 指令簡介
原則上這個床戰還屬於半測試階段，能使用的指令還很少，不過如果有任何建議還是希望大家能夠提出來！
### 指令格式: /bw <子指令> [參數...]
關於子指令的類別由以下所示
* start (不需任何參數)<br>
```即遊戲開始的指令，開始後會自動破壞"沒有玩家的隊伍的床"，並開始運作遊戲。```
* tp + [世界名稱]<br>
```可任意傳送至其他世界，若要查看世界的列表，只要輸入"/bw tp"不加上任何參數即可。```
* team <類別參數> [子參數...]<br>
關於這個指令的子參數還蠻複雜的，有各項不同的功能。如以下所示
  * selector <true|false><br>
```是否開啟隊伍選擇器(即自選隊視窗)，設定為true即開啟隊伍選擇視窗，玩家可透過輸入指令"/team"來選擇自己的隊伍。```
  * random <每隊玩家數量><br>
```即"隨機選隊"。```
  * set [玩家名稱] [隊伍名稱]<br>
```將第一個參數的玩家設定為第二個參數的隊伍，若要查看隊伍的列表，只要輸入"/bw team set"不加上任何參數即可。```
  * reset (不須任何參數)<br>
```重置所有隊伍。```
  * remove <玩家名稱><br>
```將該玩家從現有隊伍中移除。```
* time <時間><br>
```(屬實驗功能，可能造成錯誤) 設定遊戲時間，格式為 分: 秒```<br>
```如 /bw time 10:00```
* speed <倍率><br>
```(屬實驗功能，可能造成錯誤) 設定遊戲速度```<br>
```會加速的項目包刮遊戲事件運行速度及資源生成器速度```<br>
```建議設定為20的因數(如:2,4,5...)```<br>
```若要設定速度兩倍，則須輸入 /bw speed 2```
## 若有建議/問題 歡迎提出 我會對插件內容進行更改! 謝謝