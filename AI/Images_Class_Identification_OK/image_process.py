# -*- coding: utf-8 -*-

import numpy as np
import argparse

ap = argparse.ArgumentParser()

ap.add_argument("-m", "--image", type=str, default=None, help="path to input image file")
ap.add_argument("-f", "--filter", type=str, default=None, help="choose image filter func")
args = vars(ap.parse_args())

filter = args["filter"].lower()
img_path = args["image"]


from skimage.exposure import rescale_intensity

def convolve(image, kernel):
    # 取得圖像與濾波器的寬高
    (iH, iW) = image.shape[:2]
    (kH, kW) = kernel.shape[:2]

    # 計算 padding='same' 單邊所需的補零行數
    pad = int((kW - 1) / 2)
    image = cv2.copyMakeBorder(image, pad, pad, pad, pad, cv2.BORDER_REPLICATE)
    output = np.zeros((iH, iW), dtype="float32")

    # 卷積
    for y in np.arange(pad, iH + pad):
        for x in np.arange(pad, iW + pad):            
            roi = image[y - pad:y + pad + 1, x - pad:x + pad + 1]  # 裁切圖像            
            k = (roi * kernel).sum()                               # 卷積計算
            output[y - pad, x - pad] = k                           # 更新計算結果的矩陣 

    # 調整影像色彩深淺範圍至 (0, 255)
    output = rescale_intensity(output, in_range=(0, 255))
    output = (output * 255).astype("uint8")
   
    return output     # 回傳結果影像

"""## 灰階化(Gray)"""

import skimage
import cv2


image = cv2.imread(img_path)

# 灰階化
gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
if filter == "gray":
    convoleOutput = gray
    cv2.imshow("gray", gray)
elif filter == "blur":
    # 大模糊
    largeBlur = np.ones((21, 21), dtype="float") * (1.0 / (21 * 21))
    convoleOutput = convolve(gray, largeBlur)
    opencvOutput = cv2.filter2D(gray, -1, largeBlur)
    cv2.imshow("large Blur", convoleOutput)
elif filter == "sharpen":
    # sharpening filter
    sharpen = np.array((
        [0, -1, 0],
        [-1, 5, -1],
        [0, -1, 0]), dtype="int")
    convoleOutput = convolve(gray, sharpen)
    opencvOutput = cv2.filter2D(gray, -1, sharpen)
    cv2.imshow("sharpen", convoleOutput)
elif filter == "laplacian":
    # Laplacian filter
    laplacian = np.array((
        [0, 1, 0],
        [1, -4, 1],
        [0, 1, 0]), dtype="int")
    convoleOutput = convolve(gray, laplacian)
    opencvOutput = cv2.filter2D(gray, -1, laplacian)
    cv2.imshow("laplacian edge detection", convoleOutput)
elif filter == "sobel_x":
    # Sobel x-axis filter
    sobelX = np.array((
        [-1, 0, 1],
        [-2, 0, 2],
        [-1, 0, 1]), dtype="int")
    convoleOutput = convolve(gray, sobelX)
    opencvOutput = cv2.filter2D(gray, -1, sobelX)
    cv2.imshow("x-axis edge detection", convoleOutput)
elif filter == "sobel_y":
    # Sobel y-axis filter
    sobelY = np.array((
        [-1, -2, -1],
        [0, 0, 0],
        [1, 2, 1]), dtype="int")
    convoleOutput = convolve(gray, sobelY)
    opencvOutput = cv2.filter2D(gray, -1, sobelY)
    cv2.imshow("y-axis edge detection", convoleOutput)
img = convoleOutput
cv2.imwrite(f"{''.join(img_path.split('.')[0:-1])}_{filter.title()}_Result.{img_path.split('.')[-1]}", img)
# 按 Enter 關閉視窗
cv2.waitKey(0)
cv2.destroyAllWindows()