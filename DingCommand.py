'''
coding:utf-8
@Software:PyCharm
@Time:2023/6/27 21:50
@Author:zhaoke
@Description:
'''
# -*- coding: utf-8 -*-
import dingtalk.api
access_token:""

req=dingtalk.api.OapiMessageCorpconversationAsyncsendV2Request("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2")

req.agent_id=32
req.to_all_user="true"
req.msg= {"msgtype":"text",
          "text":{"content":"111"}
}

try:
	resp= req.getResponse(access_token)
	print(resp)
except Exception as e:
	print(e)
