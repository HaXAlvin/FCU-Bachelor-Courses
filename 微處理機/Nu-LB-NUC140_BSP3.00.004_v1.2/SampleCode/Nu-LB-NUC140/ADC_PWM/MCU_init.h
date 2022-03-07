
//Define Clock source
#define MCU_CLOCK_SOURCE      
#define MCU_CLOCK_SOURCE_HXT  // HXT, LXT, HIRC, LIRC
#define MCU_CLOCK_FREQUENCY   50000000  //Hz

//Define MCU Interfaces
#define MCU_INTERFACE_ADC
#define ADC_CLOCK_SOURCE_HXT  // HXT, LXT, PLL, HIRC, HCLK
#define ADC_CLOCK_DIVIDER     1
#define PIN_ADC7_PA7
#define ADC_CHANNEL_MASK      ADC_CH_7_MASK
#define ADC_INPUT_MODE        ADC_INPUT_MODE_SINGLE_END // SINGLE_END, DIFFERENTIAL
#define ADC_OPERATION_MODE    ADC_OPERATION_MODE_CONTINUOUS // SINGLE, SINGLE_CYCLE, CONTINUOUS

#define MCU_INTERFACE_PWM0
#define PWM0_CH01_CLOCK_SOURCE_HXT // HXT, LXT, HCLK, HIRC
#define PIN_PWM0_CH0_PA12
