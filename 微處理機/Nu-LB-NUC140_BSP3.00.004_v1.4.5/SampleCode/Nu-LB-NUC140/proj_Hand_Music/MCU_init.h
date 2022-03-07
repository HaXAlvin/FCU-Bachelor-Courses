//Define Clock source
#define MCU_CLOCK_SOURCE      
#define MCU_CLOCK_SOURCE_HXT // HXT, LXT, HIRC, LIRC 
#define MCU_CLOCK_FREQUENCY   50000000  //Hz

//Define MCU Interfaces
#define MCU_INTERFACE_TMR2
#define TMR2_CLOCK_SOURCE_HXT // HXT, LXT, HCLK, EXT, LIRC, HIRC
#define TMR2_CLOCK_DIVIDER    12
#define TMR2_OPERATING_MODE   TIMER_CONTINUOUS_MODE // ONESHOT, PERIODIC, TOGGLE, CONTINUOUS
#define TMR2_OPERATING_FREQ   1000000 //Hz
#define TMR2_EVENT_DETECTION  TIMER_COUNTER_FALLING_EDGE // FALLING, RISING
#define TMR2_CAPTURE_MODE TIMER_CAPTURE_FREE_COUNTING_MODE // FREE_COUNTING, COUNTER_RESET
#define TMR2_CAPTURE_EDGE TIMER_CAPTURE_FALLING_AND_RISING_EDGE // FALLING, RISING, FALLING_AND_RISING
#define PIN_TC2_PB2  // TC0_PB15, TC1_PE5, TC2_PB2, TC3_PB3

#define MCU_INTERFACE_PWM0
#define PWM0_CH01_CLOCK_SOURCE_HXT // HXT, LXT, HCLK, HIRC
#define PIN_PWM0_CH0_PA12  

#define MCU_INTERFACE_SPI3
#define SPI3_CLOCK_SOURCE_HCLK // HCLK, PLL
#define PIN_SPI3_SS0_PD8
#define PIN_SPI3_SCLK_PD9
#define PIN_SPI3_MISO0_PD10
#define PIN_SPI3_MOSI0_PD11

