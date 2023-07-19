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
MId=''
url = "http://8.142.73.253:6000/MessageServer"
def process_initiation():
    # 构建JSON报文
    json_data = {
"data":{'DataAttributes': [{'DName': 'Stubs', 'DType': 'String', 'DValue': '137+180|40*46;138+160|100*45;138+510|100*42;139+020|100*60;139+310|100*53;139+660|100*58;140+410|40*29'}]},
    "body" :{
      "DeviceNo": "",
      "DeviceName": "",
      "Category": "感知设备",
      "SubCategory": "摄像机",
      "Location": "140+410",
       "ETime": ""
     },
      "header" :{
       "Address":"http://1f879549.r17.cpolar.top", "LisenerPort":"80",
       "ServerName":"test"},
     "message" :{
      "MId":MId,
      "MType":"Weather",
      "MTimestamp" :""
    }
}
    # 设置请求头
    headers = {
        "Content-Type": "application/octet-stream"
    }
    # 发送POST请求
    response = requests.post(url, json=json_data,headers=headers)

    if response.status_code == 200:
        print("请求成功")
    else:
        print("请求失败")

     # 处理响应结果
    response_data = response.json()
    # print(response.text)
    pIns_values = response_data.get("data", {}).get("pIns", "")
    pIns_list = pIns_values.split(",") if pIns_values else []
    # 返回响应结果
    return pIns_list

def mid():
    # 构建JSON报文
    json_data = {
"data":{
    'DataAttributes': [{'DName': '', 'DType': '', 'DValue': ''}]
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
       "Address":"8.142.73.253", "LisenerPort":"80",
       "ServerName":"test"},
     "message" :{
      "MId":"",
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
        print("请求成功")
    else:
        print("请求失败")

     # 处理响应结果
    # response_data = response.json()
    print(response.text)
    data = json.loads(response.text)
    data_attributes = data["data"]["DataAttributes"]
    for attribute in data_attributes:
        if "DValue" in attribute:
            dvalue = attribute["DValue"]
            print("MId为", dvalue)
            break

    return dvalue
# 获取mid
MId = mid()
# 启动流程
# pIns_list = process_initiation()
