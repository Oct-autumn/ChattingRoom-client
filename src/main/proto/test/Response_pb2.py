# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: Response.proto
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x0eResponse.proto\x12\x17\x63n.csuosa.pojo.response\"\xae\x03\n\x0cResponsePOJO\x12\x38\n\x04type\x18\x01 \x01(\x0e\x32*.cn.csuosa.pojo.response.ResponsePOJO.Type\x12\x34\n\x06result\x18\x02 \x01(\x0b\x32\x1f.cn.csuosa.pojo.response.ResultH\x00\x88\x01\x01\x12\x31\n\x07message\x18\x03 \x03(\x0b\x32 .cn.csuosa.pojo.response.Message\x12\x39\n\x0b\x63hannelInfo\x18\x04 \x03(\x0b\x32$.cn.csuosa.pojo.response.ChannelInfo\x12<\n\nmemberInfo\x18\x05 \x01(\x0b\x32#.cn.csuosa.pojo.response.MemberInfoH\x01\x88\x01\x01\"h\n\x04Type\x12\n\n\x06RETAIN\x10\x00\x12\n\n\x06RESULT\x10\x01\x12\x0c\n\x08PUSH_MSG\x10\x02\x12\x11\n\rPUSH_CHA_LIST\x10\x03\x12\x14\n\x10PUSH_MEMBER_LIST\x10\x04\x12\x11\n\rPUSH_SYS_INFO\x10\x05\x42\t\n\x07_resultB\r\n\x0b_memberInfo\"2\n\x06Result\x12\x0e\n\x06StCode\x18\x01 \x01(\x05\x12\x10\n\x03msg\x18\x02 \x01(\tH\x00\x88\x01\x01\x42\x06\n\x04_msg\"^\n\x07Message\x12\x0f\n\x07\x63hannel\x18\x01 \x01(\t\x12\x10\n\x08\x66romNick\x18\x02 \x01(\t\x12\x0c\n\x04type\x18\x03 \x01(\x05\x12\x11\n\ttimestamp\x18\x04 \x01(\x03\x12\x0f\n\x07\x63ontent\x18\x05 \x01(\x0c\"N\n\x0b\x43hannelInfo\x12\x0c\n\x04name\x18\x01 \x01(\t\x12\x0c\n\x04isIN\x18\x02 \x01(\x08\x12\x10\n\x08isPublic\x18\x03 \x01(\x08\x12\x11\n\tmemberNum\x18\x04 \x01(\x05\"5\n\nMemberInfo\x12\x13\n\x0b\x63hannelName\x18\x01 \x01(\t\x12\x12\n\nmemberNick\x18\x02 \x03(\tB)\n\x1b\x63n.csuosa.chatroomcli.protoB\x08ResponseH\x01\x62\x06proto3')



_RESPONSEPOJO = DESCRIPTOR.message_types_by_name['ResponsePOJO']
_RESULT = DESCRIPTOR.message_types_by_name['Result']
_MESSAGE = DESCRIPTOR.message_types_by_name['Message']
_CHANNELINFO = DESCRIPTOR.message_types_by_name['ChannelInfo']
_MEMBERINFO = DESCRIPTOR.message_types_by_name['MemberInfo']
_RESPONSEPOJO_TYPE = _RESPONSEPOJO.enum_types_by_name['Type']
ResponsePOJO = _reflection.GeneratedProtocolMessageType('ResponsePOJO', (_message.Message,), {
  'DESCRIPTOR' : _RESPONSEPOJO,
  '__module__' : 'Response_pb2'
  # @@protoc_insertion_point(class_scope:cn.csuosa.pojo.response.ResponsePOJO)
  })
_sym_db.RegisterMessage(ResponsePOJO)

Result = _reflection.GeneratedProtocolMessageType('Result', (_message.Message,), {
  'DESCRIPTOR' : _RESULT,
  '__module__' : 'Response_pb2'
  # @@protoc_insertion_point(class_scope:cn.csuosa.pojo.response.Result)
  })
_sym_db.RegisterMessage(Result)

Message = _reflection.GeneratedProtocolMessageType('Message', (_message.Message,), {
  'DESCRIPTOR' : _MESSAGE,
  '__module__' : 'Response_pb2'
  # @@protoc_insertion_point(class_scope:cn.csuosa.pojo.response.Message)
  })
_sym_db.RegisterMessage(Message)

ChannelInfo = _reflection.GeneratedProtocolMessageType('ChannelInfo', (_message.Message,), {
  'DESCRIPTOR' : _CHANNELINFO,
  '__module__' : 'Response_pb2'
  # @@protoc_insertion_point(class_scope:cn.csuosa.pojo.response.ChannelInfo)
  })
_sym_db.RegisterMessage(ChannelInfo)

MemberInfo = _reflection.GeneratedProtocolMessageType('MemberInfo', (_message.Message,), {
  'DESCRIPTOR' : _MEMBERINFO,
  '__module__' : 'Response_pb2'
  # @@protoc_insertion_point(class_scope:cn.csuosa.pojo.response.MemberInfo)
  })
_sym_db.RegisterMessage(MemberInfo)

if _descriptor._USE_C_DESCRIPTORS == False:

  DESCRIPTOR._options = None
  DESCRIPTOR._serialized_options = b'\n\033cn.csuosa.chatroomcli.protoB\010ResponseH\001'
  _RESPONSEPOJO._serialized_start=44
  _RESPONSEPOJO._serialized_end=474
  _RESPONSEPOJO_TYPE._serialized_start=344
  _RESPONSEPOJO_TYPE._serialized_end=448
  _RESULT._serialized_start=476
  _RESULT._serialized_end=526
  _MESSAGE._serialized_start=528
  _MESSAGE._serialized_end=622
  _CHANNELINFO._serialized_start=624
  _CHANNELINFO._serialized_end=702
  _MEMBERINFO._serialized_start=704
  _MEMBERINFO._serialized_end=757
# @@protoc_insertion_point(module_scope)