//Define Clock source
#define MCU_CLOCK_SOURCE      
#define MCU_CLOCK_SOURCE_HXT   // HXT, LXT, HIRC, LIRC
#define MCU_CLOCK_FREQUENCY    50000000  //Hz

//Define MCU Interfaces
#define MCU_INTERFACE_TMR1
#define TMR1_CLOCK_SOURCE_HXT  // HXT, LXT, HCLK, EXT, LIRC, HIRC
#define TMR1_CLOCK_DIVIDER     1
#define TMR1_OPERATING_MODE    TIMER_PERIODIC_MODE // ONESHOT, PERIODIC, TOGGLE, CONTINUOUS
#define TMR1_OPERATING_FREQ    10 //Hz

#define MCU_INTERFACE_SPI3
#define SPI3_CLOCK_SOURCE_HCLK // HCLK, PLL
#define PIN_SPI3_SS0_PD8
#define PIN_SPI3_SCLK_PD9
#define PIN_SPI3_MISO0_PD10
#define PIN_SPI3_MOSI0_PD11

#define MCU_INTERFACE_ADC
#define ADC_CLOCK_SOURCE_HXT   // HXT, LXT, PLL, HIRC, HCLK
#define ADC_CLOCK_DIVIDER      1
#define PIN_ADC_7
#define ADC_CHANNEL_MASK       ADC_CH_7_MASK            
#define ADC_INPUT_MODE         ADC_INPUT_MODE_SINGLE_END // SINGLE_END, DIFFERENTIAL
#define ADC_OPERATION_MODE     ADC_OPERATION_MODE_SINGLE_CYCLE // SINGLE, SINGLE_CYCLE, CONTINUOUS 

#define MCU_INTERFACE_PWM0
#define PWM0_CH01_CLOCK_SOURCE_HCLK // HXT, LXT, HCLK, HIRC
#define PIN_PWM0_CH0_PA12
