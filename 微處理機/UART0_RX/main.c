//
// UART0_RX : UART0 RX recieve and display reading
//
// Board       : Nu-LB-NUC140
// MCU         : NUC140VE3CN (LQFP100)
// UART0       : baudrate=115200, databit=8, stopbit=1, paritybit=0, flowcontrol=None

#include <stdio.h>
#include <math.h>
#include <string.h>
#include "NUC100Series.h"
#include "MCU_init.h"
#include "SYS_init.h"
#include "LCD.h"
#include "Scankey.h"
#include "Seven_Segment.h"

// Global Variables
char     reading[16];
uint8_t     writing[16]={0};
uint8_t  comRbuf[256];
volatile uint8_t comRbytes = 0;
volatile uint8_t RX_Flag =0;
int i,j,A,B,line=0,show7=0,len,tmp;
char 			ans[5] = "8654";

volatile uint8_t KEY_Flag, index_key_scan,key_scan;

volatile uint8_t int_cnt;

volatile uint32_t index_5ms, cnt_5ms, cnt_1s, cnt_100ms;
volatile uint32_t digit0, digit1, digit2, digit3;
void Init_Timer0(void)
{
  TIMER_Open(TIMER0, TIMER_PERIODIC_MODE, 200);
  TIMER_EnableInt(TIMER0);
  NVIC_EnableIRQ(TMR0_IRQn);
  TIMER_Start(TIMER0);
}

void Init_KEY(void)
{
    GPIO_SetMode(PA, (BIT0 | BIT1 | BIT2), GPIO_MODE_QUASI);
	  GPIO_SetMode(PA, (BIT3 | BIT4 | BIT5), GPIO_MODE_QUASI);
    GPIO_EnableInt(PA, 0, GPIO_INT_LOW);
    GPIO_EnableInt(PA, 1, GPIO_INT_LOW);
    GPIO_EnableInt(PA, 2, GPIO_INT_LOW);		
    NVIC_EnableIRQ(GPAB_IRQn);
    NVIC_SetPriority(GPAB_IRQn,3); 	
    GPIO_SET_DEBOUNCE_TIME(GPIO_DBCLKSRC_LIRC, GPIO_DBCLKSEL_128);
    GPIO_ENABLE_DEBOUNCE(PA, (BIT0 | BIT1 | BIT2));
}



void TMR0_IRQHandler(void)
{
	cnt_5ms++;
	index_5ms = cnt_5ms % 4;
	CloseSevenSegment();
	if (index_5ms == 0) {
		PA0=1; PA1=1; PA2=1; PA3=1; PA4=1; PA5=0;
		ShowSevenSegment(0,4);				
	}	
	if (index_5ms == 1)  {
		PA0=1; PA1=1; PA2=1; PA3=1; PA4=0; PA5=1;
		ShowSevenSegment(1,5);		
	}	
	if (index_5ms == 2)  {
		PA0=1; PA1=1; PA2=1; PA3=0; PA4=1; PA5=1;
		ShowSevenSegment(2,6);		
	}
	if (index_5ms == 3)  {
		PA0=1; PA1=1; PA2=1; PA3=1; PA4=1; PA5=1;
		ShowSevenSegment(3,8);
	}		
	
	
	if (cnt_5ms % 100 == 0) 
	{
		key_scan++;
		index_key_scan = key_scan % 3;
		if (index_key_scan == 0)
		{
			PA0=1; PA1=1; PA2=1; PA3=1; PA4=1; PA5=0;
		}
		if (index_key_scan == 1)
		{
			PA0=1; PA1=1; PA2=1; PA3=1; PA4=0; PA5=1;
		}
		if (index_key_scan == 2)
		{
			PA0=1; PA1=1; PA2=1; PA3=0; PA4=1; PA5=1;
		}
		NVIC_EnableIRQ(GPAB_IRQn);
		int_cnt ++;
	}
	
	if (cnt_5ms % 20 == 0) cnt_100ms++;
	if (cnt_5ms % 200 == 0) cnt_1s++;
	
  TIMER_ClearIntFlag(TIMER0);
}


void GPAB_IRQHandler(void)
{
		NVIC_DisableIRQ(GPAB_IRQn);
	
		int_cnt =0;
	
    if (PA->ISRC & BIT0) {        // check if PA0 interrupt occurred
        PA->ISRC |= BIT0;			// clear PA0 interrupt status
				PA0=1;
			if (PA3==0) { PA3=1; KEY_Flag =3;}
				if (PA4==0) { PA4=1; KEY_Flag =6;}
				if (PA5==0) { PA5=1;KEY_Flag =9;}
				return;			
    } 
		if (PA->ISRC & BIT1) { 				// check if PA1 interrupt occurred
        PA->ISRC |= BIT1;         // clear PA1 interrupt status  
				PA1=1;
				if (PA3==0) { KEY_Flag =2; PA3=1;}
				if (PA4==0) { KEY_Flag =5; PA4=1;}
				if (PA5==0) { KEY_Flag =8; PA5=1;}
				return;				
    } 
		if (PA->ISRC & BIT2) { 				// check if PA2 interrupt occurred
        PA->ISRC |= BIT2;         // clear PA2 interrupt status  
				PA2=1;
				if (PA3==0) { KEY_Flag =1; PA3=1;}
				if (PA4==0) { KEY_Flag =4; PA4=1;}
				if (PA5==0) { KEY_Flag =7; PA5=1;}
				return;				
    }                     				// else it is unexpected interrupts
    PA->ISRC = PA->ISRC;	      	// clear all GPB pins
}

void UART02_IRQHandler(void)
{
	uint8_t c,i=0;
	uint32_t u32IntSts = UART0->ISR;
	if(u32IntSts & UART_IS_RX_READY(UART0)) // check ISR on & RX is ready
  {
		memset(reading,0,sizeof reading);
		while (!(UART0->FSR & UART_FSR_RX_EMPTY_Msk)){ // check RX is not empty
			c = UART_READ(UART0); // read UART RX data
			if (c!='\n') {        // check line-end 
				comRbuf[comRbytes] = c;
				comRbytes++;
				//print_Line(1,"1234");
			} else {
				//print_Line(2,"1234");
				for (i=0; i<comRbytes; i++)	 reading[i]=comRbuf[i]; // store received data to Message
				for (i=comRbytes; i<14; i++) reading[i]=' ';
				reading[i] = '\0';
				comRbytes=0;
				RX_Flag=1;	                 // set flag when BT command input
				break;
			}
		}		
	}
}

void Init_UART0(void)
{ 
  UART_Open(UART0, 115200);                     // set UART0 baud rate
  UART_ENABLE_INT(UART0, UART_IER_RDA_IEN_Msk); // enable UART0 interrupt (triggerred by Read-Data-Available)
  NVIC_EnableIRQ(UART02_IRQn);		              // enable Cortex-M0 NVIC interrupt for UART02
}

int32_t main()
{
	SYS_Init();   // initialize MCU
  init_LCD();   // initialize LCD
  clear_LCD();  // clear LCD screen	
	Init_KEY();
	Init_Timer0();
	OpenSevenSegment();
	Init_UART0(); // initialize UART1 for Bluetooth
	RX_Flag = 0;// 0 = write 1=read
	len=0;
	KEY_Flag=0;
  while(1){
		
    if (RX_Flag==1) {
			A=0;B=0;
			for(i=0;i<4;i++){
				for(j=0;j<4;j++){
					if(ans[i] == reading[j]){
						if(i==j){
							A++;
							break;
						}else{
							B++;
							break;
						}
					}
				}
			}
			reading[10] = A+48;
			reading[11] = 'A';
			reading[12] = B+48;
			reading[13] = 'B';
			//clear_LCD();
			print_Line(line++, reading);
			
			RX_Flag=0;
		}else if (RX_Flag == 0){
			
			
			if((KEY_Flag !=0) && (int_cnt != 0)){
				
				writing[len++] = KEY_Flag+48;
				
				KEY_Flag = 0;
			}
			if(len==4){
				writing[len] = '\n';
				//print_Line(0, writing);
				UART_Write(UART0, writing, 5);
				memset(writing,0,sizeof writing);
				len=0;
			}
		}
	}
}


