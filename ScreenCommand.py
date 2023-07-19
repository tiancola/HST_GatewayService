# from Wplan import *
import requests
import sys
from sys import argv
import numpy as np
import json

class sendTest:
    def __init__(self, devc_id, vms_speed_limit):
        self.devc_id = devc_id
        self.vms_speed_limit = vms_speed_limit

    def send(self):
        url = "http://10.168.160.22/dev-tool/ocp/api/gateway/screen/get"

        payload = {
            "_meta": {
                "msg_type": 0,
                "sid": "111",
                "dev_id": self.devc_id,
                "dev_type": "screen",
                "ctl_type": "get",
                "app_id": "hst_cadx",
                "ts": "1685930602000",
                "sign": "18239c21d79ac99e10bf8e719fce4b3256ab31e4e6a777edb37c4bbcd434898c"
            },
            "data": {
                "list": {
                    "image": {
                        "base64": "data:image/jpg;base64,/9j/4AAQSkZA"
                    },
                    "text": {
                        "content": self.vms_speed_limit,
                        "color": "green",
                        "fonttype": "s",
                        "fontsize": "24"
                    },
                    "stayTime": 3
                }
            }
        }

        # 转换成json格式
        json_payload = json.dumps(payload)
        print(json_payload)

        # 发送请求
        # response = requests.post(url, data=json_payload)
        # print("Response: " + response.text)



if __name__ == '__main__':

    # 解析Java传递的参数
    json_str = json.dumps(sys.argv[1])
    java_args = json.loads(json_str)
    data = java_args["data"]["DataAttributes"][0]["DValue"]
    # data = "de1|sp1;de2|sp2"

    # 获取要替换的值
    data = data.split(';')
    deviceno_list = []
    vms_speed_limit_list = []
    for item in data:
        deviceno_list.append(item.split('|')[0])
        vms_speed_limit_list.append(item.split('|')[1])
    rows = np.vstack((deviceno_list, vms_speed_limit_list)).T

    for item in rows:
        sendtest = sendTest(item[0], item[1])
        sendtest.send()
