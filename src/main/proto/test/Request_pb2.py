# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: Request.proto
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\rRequest.proto\x12\x16\x63n.csuosa.pojo.request\"\xa8\x03\n\x0bRequestPOJO\x12@\n\toperation\x18\x01 \x01(\x0e\x32-.cn.csuosa.pojo.request.RequestPOJO.Operation\x12/\n\x04user\x18\x02 \x01(\x0b\x32\x1c.cn.csuosa.pojo.request.UserH\x00\x88\x01\x01\x12\x35\n\x07\x63hannel\x18\x03 \x01(\x0b\x32\x1f.cn.csuosa.pojo.request.ChannelH\x01\x88\x01\x01\x12\x35\n\x07message\x18\x04 \x01(\x0b\x32\x1f.cn.csuosa.pojo.request.MessageH\x02\x88\x01\x01\"\x96\x01\n\tOperation\x12\n\n\x06RETAIN\x10\x00\x12\x0c\n\x08REGISTER\x10\x01\x12\t\n\x05LOGIN\x10\x02\x12\n\n\x06LOGOUT\x10\x03\x12\x0f\n\x0bUPDATE_INFO\x10\x04\x12\x0c\n\x08JOIN_CHA\x10\x05\x12\x0c\n\x08QUIT_CHA\x10\x06\x12\x0e\n\nCREATE_CHA\x10\x07\x12\x0c\n\x08SEND_MSG\x10\x08\x12\r\n\tHEARTBEAT\x10\tB\x07\n\x05_userB\n\n\x08_channelB\n\n\x08_message\"\x97\x01\n\x04User\x12\x10\n\x03uid\x18\x01 \x01(\x03H\x00\x88\x01\x01\x12\x12\n\x05\x65mail\x18\x02 \x01(\tH\x01\x88\x01\x01\x12\x18\n\x0b\x64\x65\x66\x61ultNick\x18\x03 \x01(\tH\x02\x88\x01\x01\x12\x0b\n\x03pwd\x18\x04 \x01(\x0c\x12\x14\n\x07pwd_old\x18\x05 \x01(\x0cH\x03\x88\x01\x01\x42\x06\n\x04_uidB\x08\n\x06_emailB\x0e\n\x0c_defaultNickB\n\n\x08_pwd_old\"S\n\x07\x43hannel\x12\x0c\n\x04name\x18\x01 \x01(\t\x12\x11\n\x04nick\x18\x02 \x01(\tH\x00\x88\x01\x01\x12\x13\n\x06ticket\x18\x03 \x01(\tH\x01\x88\x01\x01\x42\x07\n\x05_nickB\t\n\x07_ticket\";\n\x07Message\x12\x0c\n\x04type\x18\x01 \x01(\x05\x12\x11\n\ttimestamp\x18\x02 \x01(\x03\x12\x0f\n\x07\x63ontent\x18\x03 \x01(\x0c\x42(\n\x1b\x63n.csuosa.chatroomcli.protoB\x07RequestH\x01\x62\x06proto3')



_REQUESTPOJO = DESCRIPTOR.message_types_by_name['RequestPOJO']
_USER = DESCRIPTOR.message_types_by_name['User']
_CHANNEL = DESCRIPTOR.message_types_by_name['Channel']
_MESSAGE = DESCRIPTOR.message_types_by_name['Message']
_REQUESTPOJO_OPERATION = _REQUESTPOJO.enum_types_by_name['Operation']
RequestPOJO = _reflection.GeneratedProtocolMessageType('RequestPOJO', (_message.Message,), {
  'DESCRIPTOR' : _REQUESTPOJO,
  '__module__' : 'Request_pb2'
  # @@protoc_insertion_point(class_scope:cn.csuosa.pojo.request.RequestPOJO)
  })
_sym_db.RegisterMessage(RequestPOJO)

User = _reflection.GeneratedProtocolMessageType('User', (_message.Message,), {
  'DESCRIPTOR' : _USER,
  '__module__' : 'Request_pb2'
  # @@protoc_insertion_point(class_scope:cn.csuosa.pojo.request.User)
  })
_sym_db.RegisterMessage(User)

Channel = _reflection.GeneratedProtocolMessageType('Channel', (_message.Message,), {
  'DESCRIPTOR' : _CHANNEL,
  '__module__' : 'Request_pb2'
  # @@protoc_insertion_point(class_scope:cn.csuosa.pojo.request.Channel)
  })
_sym_db.RegisterMessage(Channel)

Message = _reflection.GeneratedProtocolMessageType('Message', (_message.Message,), {
  'DESCRIPTOR' : _MESSAGE,
  '__module__' : 'Request_pb2'
  # @@protoc_insertion_point(class_scope:cn.csuosa.pojo.request.Message)
  })
_sym_db.RegisterMessage(Message)

if _descriptor._USE_C_DESCRIPTORS == False:

  DESCRIPTOR._options = None
  DESCRIPTOR._serialized_options = b'\n\033cn.csuosa.chatroomcli.protoB\007RequestH\001'
  _REQUESTPOJO._serialized_start=42
  _REQUESTPOJO._serialized_end=466
  _REQUESTPOJO_OPERATION._serialized_start=283
  _REQUESTPOJO_OPERATION._serialized_end=433
  _USER._serialized_start=469
  _USER._serialized_end=620
  _CHANNEL._serialized_start=622
  _CHANNEL._serialized_end=705
  _MESSAGE._serialized_start=707
  _MESSAGE._serialized_end=766
# @@protoc_insertion_point(module_scope)
