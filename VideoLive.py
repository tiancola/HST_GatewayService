'''
coding:utf-8
@Software:PyCharm
@Time:2023/7/7 10:38
@Author:zhaoke
@Description:
调用videolive(resid)函数获取实时视频码流，传入resid参数
'''
import json
import requests
import time

video_info={
    'ipPort':'http://33.65.192.158:5000/ydjk',
    'username':'wtoe',
    'userPsw':'123456',
    'userMac':'',

}

def login(video_info):
    login_url = video_info['ipPort'] + "/video/login"
    param = "username=" + video_info['userName'] + "&password=" + video_info['userPsw'] + "&mac=" + video_info['userMac']
    response = requests.post(login_url, data=param.encode('utf-8'))
    json_data = json.loads(response.text)
    code = json_data['code']
    access_token = ""
    if code == "0":
        rows = json_data['rows']
        rows_data = json.loads(rows)
        token = rows_data['token']
        json_token = json.loads(token)
        access_token = json_token['accessToken']
    return access_token

def video_start(res_id, access_token, video_info):
    start_url = video_info['ipPort'] + "/video/start"
    param = "rateType=0&resId=" + res_id
    response = requests.post(start_url, data=param.encode('utf-8'), headers={'Authorization': access_token})
    json_data = json.loads(response.text)
    code = json_data['code']
    session_id = ""
    if code == "0":
        rows = json_data['rows']
        rows_data = json.loads(rows)
        session_id = rows_data['sessionId']
    return session_id

def video_status(session_id, access_token, video_info):
    status_url = video_info['ipPort'] + "/video/status"
    param = "sessionId=" + session_id
    result_dict = {}
    for _ in range(30):
        time.sleep(0.01)
        response = requests.post(status_url, data=param.encode('utf-8'), headers={'Authorization': access_token})
        json_data = json.loads(response.text)
        code = json_data['code']
        if code == "0":
            rows = json_data['rows']
            rows_data = json.loads(rows)
            flv = rows_data['flv']
            rtmp = rows_data['rtmp']
            if flv is not None and flv != "" and rtmp is not None and rtmp != "":
                result_dict['flv'] = flv
                result_dict['rtmp'] = rtmp
                return result_dict
    return result_dict

def video_stop(session_id, access_token, video_info):
    stop_url = video_info['ipPort'] + "/video/stop"
    param = "sessionId=" + session_id
    response = requests.post(stop_url, data=param.encode('utf-8'), headers={'Authorization': access_token})
    json_data = json.loads(response.text)
    code = json_data['code']
    result = {}
    if code == "0":
        print("停止视频播放成功。")
        result['Flag'] = True
    else:
        print("停止视频播放失败。")
        result['Flag'] = False
    return result

def videolive(resid):
    # 登录获取token
    access_token=login(video_info)
    # 获取指定摄像头session_id
    session_id = video_start(resid,access_token,video_info)
    # 获取码流字典，包括flv地址以及rtmp地址
    video_url=video_status(session_id,access_token,video_info)

    return video_url

