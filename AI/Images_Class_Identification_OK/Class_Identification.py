#!/usr/bin/env python
# coding: utf-8

# import the necessary packages
from mrcnn.config import Config
from mrcnn import model as modellib

import imutils
import cv2
import os
import argparse
import colorsys
import random
import numpy as np

# construct the argument parse and parse the arguments
ap = argparse.ArgumentParser()

ap.add_argument("-w", "--whichone", type=str, default=None, help="path to input video file")
ap.add_argument("-i", "--input", type=str, default=None, help="path to input video file")
ap.add_argument('-f', "--folder", type=str, default=None, help="path to input video folder")
ap.add_argument("-o", "--output", type=str, default="./output", help="path to output images folder")
ap.add_argument("-s", "--source", type=str, default="./source", help="path to save sorce video to image folder")
ap.add_argument("-c", "--csvfolder", type=str, default="./csvfiles", help="path to output csv files folder")
ap.add_argument("-e", "--extendsion", type=str, default="mp4", help="video extendsion file")
ap.add_argument("--fps", type=int, default=30, help="count video to image per frame")
ap.add_argument("-r", "--result", type=int, default=1, help='whether storage source and result image')
args = vars(ap.parse_args())

# ===== 改這裡就好 ====
which_one = "sheep" # 固定寫死。人 : "person"； 車 : "car" ； 巴士 : "bus" ；羊 : "sheep"
which_one = args["whichone"] # python Class_Identification.py -w person
# 可以變成參數
# python Class_Identification.py -w person
# python Class_Identification.py -w car
# python Class_Identification.py -w bus
# python Class_Identification.py -w sheep
# ===== 改這裡就好 ====

#Project: current directory
pathProject = os.getcwd()
pathLib     = os.path.sep.join([pathProject,"lib"])
#testing dataset
videoPath = args["input"]
videoFolder = args["folder"]
inputPath   = args["input"]
csvfolder = args["csvfolder"]
fps = args['fps']
outputPath  = args["output"]
if args['result'] == 1:
    save = True
else:
    save = False
sorucePath = args['source']
labelsPath  = os.path.sep.join([pathLib, "coco_labels.txt"])
weightsPath = os.path.sep.join([pathLib, "mask_rcnn_coco.h5"])

color={"red":(0,0,255),"green":(0,255,0),"blue":(255,0,0),
       "yellow":(0,255,255),"cyan":(255,255,0)} #BGR

# load the class label names from disk, one label per line
CLASS_NAMES = open(labelsPath).read().strip().split("\n")

# generate random (but visually distinct) colors for each class label
# (thanks to Matterport Mask R-CNN for the method!)
hsv = [(i / len(CLASS_NAMES), 1, 1.0) for i in range(len(CLASS_NAMES))]
COLORS = list(map(lambda c: colorsys.hsv_to_rgb(*c), hsv))
random.seed(42)
random.shuffle(COLORS)

class SimpleConfig(Config):
    # give the configuration a recognizable name
    NAME = "coco_inference"
    # set the number of GPUs to use along with the number of images per GPU
    GPU_COUNT = 1
    IMAGES_PER_GPU = 1
    # number of classes (we would normally add +1 for the background
    # but the background class is *already* included in the class names)
    NUM_CLASSES = len(CLASS_NAMES)

# initialize the inference configuration
config = SimpleConfig()

# initialize the Mask R-CNN model for inference and then load the weights
print("[INFO] loading Mask R-CNN model...")
model = modellib.MaskRCNN(mode="inference", config=config,	model_dir=os.getcwd())
model.load_weights(weightsPath, by_name=True)

dataSource = "./my_images/" + which_one + ".jpg"

which_one_result = which_one+"_result.jpg"
img = cv2.imread(dataSource)
img = cv2.resize(img, (1800, 1000), interpolation=cv2.INTER_AREA)
overlay = img.copy()

def mask_rcnn(image, convertSize, showName=False, showNumberOfPerson=False):
    img = image.copy()
    # perform a forward pass of the network to obtain the results
    print("[INFO] making predictions with Mask R-CNN...")
    color = (0,255,0) #BGR
    width_img   = img.shape[1] #x
    ratio = convertSize/width_img
    img = imutils.resize(img, width=convertSize)
    
    #detect results   
    r = model.detect([img], verbose=1)[0]
    
    #convert the image back to BGR so we can use OpenCV's drawing functions
    img = cv2.cvtColor(img, cv2.COLOR_RGB2BGR)
    count  = 0 #count people
    bboxes = [] #boxes in image
    #loop over the predicted scores and class labels
    for i in range(0, len(r["scores"])):
    #extract the bounding box information, class ID, label, predicted probability, and visualization color
        classID = r["class_ids"][i]
        label   = CLASS_NAMES[classID]
        if label != which_one: #check person class car
            continue
        
        count  = count + 1
        (startY, startX, endY, endX) = r["rois"][i] #get bbox
        #covert to original codinate
        startY = int(startY/ratio)
        startX = int(startX/ratio)
        endY   = int(endY/ratio)
        endX   = int(endX/ratio)
        bbox = [startY, startX, endY, endX]
        bboxes.append(bbox)
        img = imutils.resize(img, width=width_img) #covert image to orginal size
        
        #draw the bounding box
        cv2.rectangle(img, (startX, startY), (endX, endY), color, 2)
        #show class label, and score of the object
        if showName:
            #score = r["scores"][i]
            #text = "{}: {:.3f}".format(label, score)
            text ="#"+str(count)
            y    = startY - 10 if startY - 10 > 10 else startY + 10
            cv2.putText(img, text, (startX, y), cv2.FONT_HERSHEY_SIMPLEX, 0.6, color, 2)
      #Total of boxs
    if showNumberOfPerson:
        cv2.putText(img, str(len(bboxes)),(10, 50), cv2.FONT_HERSHEY_SIMPLEX, 1.5, color, 2)
            
    return img, bboxes


# In[8]:


bb=[]
widthMask= 512 #image size for Mask RCNN

crop_img,bb = mask_rcnn(img, widthMask)

#cv2.imwrite('crop_img.jpg', crop_img)
print(bb)


# In[9]:


num = 0
for i in range(len(bb)):
    num += 1
print(num)


# In[10]:


text = "which_one Counting : " + which_one + " " + str(num)
cv2.putText(crop_img, text, (10, 40), cv2.FONT_HERSHEY_TRIPLEX, 1, (255, 0, 255), 1, cv2.LINE_AA)
cv2.imwrite(which_one_result, crop_img)
