data segment  
    h_str	DB	13,10,'Hello',13,10,'$'        
data ends
 
code segment
    assume CS:code,DS:data
	
start:
	mov ax,Data
	mov ds,ax
	lea dx,h_str
	mov ah,09h
	int 21h
	
	mov ax,000h
	int 62h
	
	mov ax,4c00h
	int 21h
code ends

end start