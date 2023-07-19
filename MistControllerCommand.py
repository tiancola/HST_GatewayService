'''
coding:utf-8
@Software:PyCharm
@Time:2023/6/27 20:16
@Author:zhaoke
@Description:
'''

import hashlib
import json
import sys

import requests
from datetime import datetime, timezone, timedelta



def get_SHA256_str_java(string):
    message_digest = hashlib.sha256()
    message_digest.update(string.encode('utf-8'))
    return message_digest.hexdigest()


def get_current_timestamp():
    now = datetime.now(timezone(timedelta(hours=8)))
    epoch_milli = int(now.timestamp()) * 1000
    return str(epoch_milli)

# 解析Java传递的参数
java_args = json.loads(sys.argv[1])
data = java_args["data"]
# 获取要替换的Comm值
commond = data["mist"]

app_id = "hst_cadx"
app_secret = "6c03430ssd374809a8ff7b67cd0ec328"
ts = get_current_timestamp()
dev_id = "DEV0133060401035"
dev_type = "foglamp"
ctl_type = "get"
sid = "111"
data = ""
string = app_id + app_secret + ts + dev_id + dev_type + ctl_type + sid + data
sign = get_SHA256_str_java(string)

url = "http://10.168.160.22/dev-tool/ocp/api/gateway/screen/get"

meta_info = {
    "msg_type": 0,
    "sid": sid,
    "dev_id": dev_id,
    "dev_type": dev_type,
    "ctl_type": ctl_type,
    "app_id": app_id,
    "ts": ts,
    "sign": sign
}

# commond为传过来的控制命令
data = commond

json_payload = json.dumps({"_meta": meta_info, "data": data})

response = requests.post(url, data=json_payload)
print("Response:", response.text)

