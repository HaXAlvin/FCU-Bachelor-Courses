/******************************************************************************
 * @file     hid_kb.h
 * @brief    USBD HID keyboard header file
 *
 * @note
 * Copyright (C) 2013 Nuvoton Technology Corp. All rights reserved.
 ******************************************************************************/
#ifndef __USBD_HID_H__
#define __USBD_HID_H__

/* Define the vendor id and product id */
#define USBD_VID        0x0416
#define USBD_PID        0xB000

/*!<Define HID Class Specific Request */
#define GET_REPORT          0x01
#define GET_IDLE            0x02
#define GET_PROTOCOL        0x03
#define SET_REPORT          0x09
#define SET_IDLE            0x0A
#define SET_PROTOCOL        0x0B

/*!<USB HID Interface Class protocol */
#define HID_NONE            0x00
#define HID_KEYBOARD        0x01
#define HID_MOUSE           0x02
#define HID_DIGITIZER       0x03
#define HID_GAMEPAD         0x04

#define HID_FUNCTION    HID_KEYBOARD

#if(HID_FUNCTION == HID_GAMEPAD)
# define HID_REPORT_BUFSIZE		    4  // bytes
#elif(HID_FUNCTION == HID_MOUSE)
# define HID_REPORT_BUFSIZE		    3  // bytes
#else
# define HID_REPORT_BUFSIZE		    8  // bytes
#endif
/*!<USB HID Class Report Type */
#define HID_RPT_TYPE_INPUT      0x01
#define HID_RPT_TYPE_OUTPUT     0x02
#define HID_RPT_TYPE_FEATURE    0x03

/*-------------------------------------------------------------*/
/* Define EP maximum packet size */
#define EP0_MAX_PKT_SIZE    8
#define EP1_MAX_PKT_SIZE    EP0_MAX_PKT_SIZE
#define EP2_MAX_PKT_SIZE    8

#define SETUP_BUF_BASE  0
#define SETUP_BUF_LEN   8
#define EP0_BUF_BASE    (SETUP_BUF_BASE + SETUP_BUF_LEN)
#define EP0_BUF_LEN     EP0_MAX_PKT_SIZE
#define EP1_BUF_BASE    (SETUP_BUF_BASE + SETUP_BUF_LEN)
#define EP1_BUF_LEN     EP1_MAX_PKT_SIZE
#define EP2_BUF_BASE    (EP1_BUF_BASE + EP1_BUF_LEN)
#define EP2_BUF_LEN     EP2_MAX_PKT_SIZE

/* Define the interrupt In EP number */
#define INT_IN_EP_NUM   0x01

/* Define Descriptor information */
#define HID_DEFAULT_INT_IN_INTERVAL     20
#define USBD_SELF_POWERED               0
#define USBD_REMOTE_WAKEUP              0
#define USBD_MAX_POWER                  50  /* The unit is in 2mA. ex: 50 * 2mA = 100mA */

#define LEN_CONFIG_AND_SUBORDINATE      (LEN_CONFIG+LEN_INTERFACE+LEN_HID+LEN_ENDPOINT)


void HID_Init(void);
void HID_ClassRequest(void);
void EP2_Handler(void);
extern void HID_UpdateKbData(void);
#endif  /* __USBD_HID_H_ */

/*** (C) COPYRIGHT 2013 Nuvoton Technology Corp. ***/
