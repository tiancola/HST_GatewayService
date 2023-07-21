'''
coding:utf-8
@Software:PyCharm
@Time:2023/6/25 16:46
@Author:zhaoke
@Description:
'''

import requests
import json
from datetime import datetime

def get_current_time_string():
    now = datetime.now()
    formatted_time = now.strftime("%Y%m%d%H%M%S")
    return formatted_time

MTimestamp = get_current_time_string()

url = "http://8.142.73.253:6000/MessageServer"

def mid():
    # 构建JSON报文
    json_data = {
        "data":{
        "DataAttributes":[{'DName':'','DType':'','DValue':''}]
        },
        "body" :{
          "DeviceNo": "",
          "DeviceName": "",
          "Category": "感知设备",
          "SubCategory": "摄像机",
          "Location": "140+410",
           "ETime": ""
         },
          "header" :{
           "Address":"http://308c95db.r3.vip.cpolar.cn", "LisenerPort":"80",
           "ServerName":"test"},
         "message" :{
          "MId":'',
          "MType":"MsgRequest",
          "MTimestamp" :MTimestamp
        }
    }
    # 设置请求头
    headers = {
        "Content-Type": "application/octet-stream"
    }
    # 发送POST请求
    response = requests.post(url, json=json_data,headers=headers)

    if response.status_code == 200:
        print("MID请求成功")
    else:
        print("MID请求失败")

    # 处理响应结果
    # response_data = response.json()
    # print(response.text)
    data = json.loads(response.text)
    data_attributes = data["data"]["dataAttributes"]
    for attribute in data_attributes:
        if "DValue" in attribute:
            dvalue = attribute["DValue"]
            print("MId为", dvalue)
            break

    return dvalue

def process_initiation(MType,DeviceNo,Location,Data):
    # 构建JSON报文
    MID = mid()
    json_data = {
        "data": Data,
        "body": {
            "DeviceNo": DeviceNo,
            "DeviceName": "",
            "Category": "感知设备",
            "SubCategory": "摄像机",
            "Location": Location,
            "ETime": ""
        },
        "header": {
            "Address": "http://308c95db.r3.vip.cpolar.cn", "LisenerPort": "80",
            "ServerName": "test"},
        "message": {
            "MId": MID,
            "MType": MType,
            "MTimestamp": MTimestamp
        }
    }
    # 设置请求头
    headers = {
        "Content-Type": "application/octet-stream"
    }
    # 发送POST请求
    response = requests.post(url, json=json_data,headers=headers)

    if response.status_code == 200:
        print("报文请求成功")
        print('----------------------json data 信息----------------')
        print(json_data)
    else:
        print("报文请求失败")

    # 处理响应结果
    response_data = response.json()
    # print(response.text)
    pIns_values = response_data.get("data", {}).get("pIns", "")
    pIns_list = pIns_values.split(",") if pIns_values else []
    # 返回响应结果
    return pIns_list

# 启动流程
# pIns_list = process_initiation()