DATAS SEGMENT    
    Color   DB  1FH        ;�w�q���I���C���  
    Count   DW  1          ;Count�p��1��O�ܴ��I��   
	Sizes 	DW	1804H      ; int size = 1800
DATAS ENDS  
   
STACKS SEGMENT STACK 'S'  
    ;��̬q�N�X  
    DW 80 DUP(0)  
STACKS ENDS  
  
CODES SEGMENT  
    ASSUME CS:CODES,DS:DATAS,SS:STACKS  
MAIN    PROC    FAR  
    MOV AX,DATAS  
    MOV DS,AX            ;�N�ƾڬqDATAS�s�JDS��  
;------------------------------------------------------------------    
    MOV DX,SEG INT_1CH   ;SEG�и��q�a�}  
    MOV DS,DX  
    LEA DX,INT_1CH       ;�եΤl���INT_1CH �������a�}      
      
    ;AH=25H�\��:�m���_�V�qAL=���_�� DS:DX=�J�f  
    MOV AH,25H                  
    MOV AL,1CH           ;�]�m�s��1CH���_�V�q  
    INT 21H  
      
    ;�h�X�{�Ǩê�^�ާ@�t��  
    MOV AH,4CH  
    INT 21H  
MAIN    ENDP      
;------------------------------------------------------------------  
;�l�{��:��ܭI�� FAR(�D�{�ǩM�l�{�Ǥ��b�P�@�N�X�q)  
INT_1CH     PROC    FAR  
    PUSH AX      ;�O�s�H�s��  
    PUSH BX  
    PUSH CX  
    PUSH DX  
    PUSH DS  
      
    STI                  ;�}���_  
    MOV AX,DATAS  
    MOV DS,AX            ;�N�ƾڬqDATAS�s�JDS��  
      
    ;------------------------------------------  
    ;- INT 1CH�t�Τ��_�C��o��18.2��          -  
    ;- Count�p�Ʀ�18��1���ܴ��I���C��         -  
    ;- Count��Ȭ�1,����1����@������Ŧ�I�� -  
    ;- ����ɽ�Ȭ�18,�C����1,���0�󴫭I���� -  
    ;------------------------------------------  
      
    DEC Count            ;Count��Ȭ�1,����1  
    JNZ Exit             ;JNZ(���G����0����) �_�hCount=0����I�����X           
;------------------------------------------------------------------   
    ;�ե�BIOS10H��06�����_�]�m�̹���l�ƩΤW��  
      
    ;--------------------------------  
    ;- AL=�W����� AL=0���̹����ť� -  
    ;- BH=���J���ݩ�                -  
    ;- CH=���W���渹 CL=���W���C��  -  
    ;- DH=�k�U���渹 DL=�k�U���C��  -  
    ;--------------------------------     
  
    ;----------------------------------  
    ;- BL���C���ݩʬ�IRGB|IRGB        -  
    ;- ��4��O�I���� �C4��O�e����    -  
    ;- I=���G R=�� G=�� B=�� �@8��    -  
    ;----------------------------------  
      
    MOV AH,6         ;�M����  
    MOV AL,0  
    MOV BH,Color         ;�_�l�]�m���ũ��զr 1FH=0001(�Ŧ�)|1111B �ԸѨ��W��  
    MOV CX,1804H  
    MOV DX,Sizes         ;(����)���18��4F�C  
    INT 10H
	
	mov dl,'>'
	;mov ah,2
	int 21H
    
	ADD Sizes,1			;size +=1
    ADD Color,8          ;0001|1111+8=27H=0010(���)|0111 �P�z�[8      
    MOV Count,9             ;�p�Ʀ�18(1��)���s�}�l,��Ȭ�18���0�����ܦ�  
;------------------------------------------------------------------     
Exit:   
    CLI                    ;�����_  
    POP DS  
    POP DX  
    POP CX  
    POP BX  
    POP AX            ;��_�H�s��    
    IRET                  ;���_��^  
INT_1CH  ENDP  
;------------------------------------------------------------------           
CODES ENDS  
END MAIN  