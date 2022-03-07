DATAS SEGMENT    
    Color   DB  1FH        ;定義的背景顏色表  
    Count   DW  1          ;Count計數1秒是變換背景   
	Sizes 	DW	1804H      ; int size = 1800
DATAS ENDS  
   
STACKS SEGMENT STACK 'S'  
    ;堆棧段代碼  
    DW 80 DUP(0)  
STACKS ENDS  
  
CODES SEGMENT  
    ASSUME CS:CODES,DS:DATAS,SS:STACKS  
MAIN    PROC    FAR  
    MOV AX,DATAS  
    MOV DS,AX            ;將數據段DATAS存入DS中  
;------------------------------------------------------------------    
    MOV DX,SEG INT_1CH   ;SEG標號段地址  
    MOV DS,DX  
    LEA DX,INT_1CH       ;調用子函數INT_1CH 取偏移地址      
      
    ;AH=25H功能:置中斷向量AL=中斷號 DS:DX=入口  
    MOV AH,25H                  
    MOV AL,1CH           ;設置新的1CH中斷向量  
    INT 21H  
      
    ;退出程序並返回操作系統  
    MOV AH,4CH  
    INT 21H  
MAIN    ENDP      
;------------------------------------------------------------------  
;子程序:顯示背景 FAR(主程序和子程序不在同一代碼段)  
INT_1CH     PROC    FAR  
    PUSH AX      ;保存寄存器  
    PUSH BX  
    PUSH CX  
    PUSH DX  
    PUSH DS  
      
    STI                  ;開中斷  
    MOV AX,DATAS  
    MOV DS,AX            ;將數據段DATAS存入DS中  
      
    ;------------------------------------------  
    ;- INT 1CH系統中斷每秒發生18.2次          -  
    ;- Count計數至18為1秒變換背景顏色         -  
    ;- Count初值為1,先減1執行一次顯示藍色背景 -  
    ;- 執行時賦值為18,每次減1,減至0更換背景色 -  
    ;------------------------------------------  
      
    DEC Count            ;Count初值為1,先減1  
    JNZ Exit             ;JNZ(結果不為0跳轉) 否則Count=0執行背景色輸出           
;------------------------------------------------------------------   
    ;調用BIOS10H的06號中斷設置屏幕初始化或上卷  
      
    ;--------------------------------  
    ;- AL=上卷行數 AL=0全屏幕為空白 -  
    ;- BH=卷入行屬性                -  
    ;- CH=左上角行號 CL=左上角列號  -  
    ;- DH=右下角行號 DL=右下角列號  -  
    ;--------------------------------     
  
    ;----------------------------------  
    ;- BL的顏色屬性為IRGB|IRGB        -  
    ;- 高4位是背景色 低4位是前景色    -  
    ;- I=高亮 R=紅 G=綠 B=藍 共8色    -  
    ;----------------------------------  
      
    MOV AH,6         ;清全屏  
    MOV AL,0  
    MOV BH,Color         ;起始設置為藍底白字 1FH=0001(藍色)|1111B 詳解見上表  
    MOV CX,1804H  
    MOV DX,Sizes         ;(全屏)表示18行4F列  
    INT 10H
	
	mov dl,'>'
	;mov ah,2
	int 21H
    
	ADD Sizes,1			;size +=1
    ADD Color,8          ;0001|1111+8=27H=0010(綠色)|0111 同理加8      
    MOV Count,9             ;計數至18(1秒)重新開始,賦值為18減至0執行變色  
;------------------------------------------------------------------     
Exit:   
    CLI                    ;關中斷  
    POP DS  
    POP DX  
    POP CX  
    POP BX  
    POP AX            ;恢復寄存器    
    IRET                  ;中斷返回  
INT_1CH  ENDP  
;------------------------------------------------------------------           
CODES ENDS  
END MAIN  