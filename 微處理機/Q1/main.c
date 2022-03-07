//
// ADC_VR1 : using ADC7 to read Variable Resistor 
//
// EVB : Nu-LB-NUC140
// MCU : NUC140VE3CN
// VR1 : Variable Resistor on learning board
//
// ADC7/PA7 : NUC140VE3CN LQFP100 pin78

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "NUC100Series.h"
#include "MCU_init.h"
#include "SYS_init.h"
#include "LCD.h"
#include "Seven_Segment.h"

#define  C6   2000
#define  C5   523

int8_t x=0,y=0,r=0;
volatile uint8_t u8ADF;

char     reading[16];
uint8_t  writing[16]={0};
uint8_t  comRbuf[256];
volatile uint8_t comRbytes = 0;
volatile uint8_t RX_Flag =0;

unsigned char ball[8] = {
	0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF
};

unsigned char racket[32] = {
	0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,
	0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF
};
void UART02_IRQHandler(void)
{
	uint8_t c,i=0;
	uint32_t u32IntSts = UART0->ISR;
	if(u32IntSts & UART_IS_RX_READY(UART0)) // check ISR on & RX is ready
  {
		while (!(UART0->FSR & UART_FSR_RX_EMPTY_Msk)){ // check RX is not empty
			c = UART_READ(UART0); // read UART RX data
			if (c!='\n') {        // check line-end 
				comRbuf[comRbytes] = c;
				comRbytes++;
			} else {
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
void ADC_IRQHandler(void)
{
    uint32_t u32Flag;

    // Get ADC conversion finish interrupt flag
    u32Flag = ADC_GET_INT_FLAG(ADC, ADC_ADF_INT);

    if(u32Flag & ADC_ADF_INT)
        u8ADF = 1;

    ADC_CLR_INT_FLAG(ADC, u32Flag);
}

void Init_ADC(void)
{
    ADC_Open(ADC, ADC_INPUT_MODE, ADC_OPERATION_MODE, ADC_CHANNEL_MASK);
    ADC_POWER_ON(ADC);
    ADC_EnableInt(ADC, ADC_ADF_INT);
    NVIC_EnableIRQ(ADC_IRQn);
}
int32_t main (void)
{
		
		
    uint32_t u32ADCvalue = 0;
		int ball_x = 0;
		int ball_y = 0;
		int first_way;
		srand(5378);
		first_way = rand()%4;
		if(first_way==0){
			ball_x=-1;
			ball_y=-1;
		}else if(first_way==1){
			ball_x=1;
			ball_y=-1;
		}else if(first_way==2){
			ball_x=-1;
			ball_y=1;
		}else if(first_way==3){
			ball_x=1;
			ball_y=1;
		}
    SYS_Init();
		
		init_LCD();
		clear_LCD();
		Init_ADC(); 
		PWM_EnableOutput(PWM1, PWM_CH_0_MASK);
    PWM_Start(PWM1, PWM_CH_0_MASK);	
		Init_UART0(); // initialize UART1 for Bluetooth
		RX_Flag = 0;// 0 = write 1=read
		
		x=56,y=24;
		u8ADF = 0;
    while(1) {
			
			ADC_START_CONV(ADC);
      while (u8ADF == 0);
      u32ADCvalue = ADC_GET_CONVERSION_DATA(ADC, 7);
      //sprintf(writing, "ADC0 = %4d",u32ADCvalue);
			
			if(ball_x>0){
				x+=3;
				if(x>117){
					PWM_ConfigOutputChannel(PWM1, PWM_CH0, C5, 90); // 0=Buzzer ON
					PWM_EnableOutput(PWM1, PWM_CH_0_MASK);
					x-=3;
					ball_x=-1;
				}
			}else{
				x-=3;
				if(x<3){
					PWM_ConfigOutputChannel(PWM1, PWM_CH0, C5, 90); // 0=Buzzer ON
					PWM_EnableOutput(PWM1, PWM_CH_0_MASK);
					x+=3;
					ball_x=1;
				}
			}
			if(ball_y>0){
				y+=3;
				if(y>56){
					PWM_ConfigOutputChannel(PWM1, PWM_CH0, C5, 90); // 0=Buzzer ON
					PWM_EnableOutput(PWM1, PWM_CH_0_MASK);
					y--;
					ball_y=-1;
				}
			}else{
				y-=3;
				if(y<3){
					PWM_ConfigOutputChannel(PWM1, PWM_CH0, C5, 90); // 0=Buzzer ON
					PWM_EnableOutput(PWM1, PWM_CH_0_MASK);
					y++;
					ball_y=1;
				}
			}
			
			
			             
			
			if(ball_y==-1 && x>r && x<r+32 && y<16){
				ball_y = 1;
				PWM_ConfigOutputChannel(PWM1, PWM_CH0, C6, 90); // 0=Buzzer ON
					PWM_EnableOutput(PWM1, PWM_CH_0_MASK);
			}
			if(ball_y==1 && x>(u32ADCvalue/43) && x<(u32ADCvalue/43)+32 && y>40){
				PWM_ConfigOutputChannel(PWM1, PWM_CH0, C6, 90); // 0=Buzzer ON
					PWM_EnableOutput(PWM1, PWM_CH_0_MASK);
				ball_y = -1;
			}
			
			sprintf(writing, "%03d%03d%03d\n",x,y,(u32ADCvalue/43));
			
			UART_Write(UART0, writing, 10);
			
			draw_Bmp32x8((u32ADCvalue/43),50,FG_COLOR,BG_COLOR,racket); // opp
			if(RX_Flag==1){
				r = (reading[0]-48)*100+(reading[1]-48)*10+(reading[2]-48)*1;
				RX_Flag=0;
			}
			draw_Bmp32x8(r,6,FG_COLOR,BG_COLOR,racket);	// my
			draw_Bmp8x8(x,y,FG_COLOR,BG_COLOR,ball);
			
			CLK_SysTickDelay(100000);
			PWM_DisableOutput(PWM1, PWM_CH_0_MASK);
			clear_LCD();
			
			
    }
}

