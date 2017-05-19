# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: network/network.proto

import sys
_b=sys.version_info[0]<3 and (lambda x:x) or (lambda x:x.encode('latin1'))
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
from google.protobuf import descriptor_pb2
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor.FileDescriptor(
  name='network/network.proto',
  package='network',
  syntax='proto2',
  serialized_pb=_b('\n\x15network/network.proto\x12\x07network\"R\n\x0eServerIdentity\x12\x0e\n\x06public\x18\x01 \x02(\x0c\x12\n\n\x02id\x18\x02 \x01(\x0c\x12\x0f\n\x07\x61\x64\x64ress\x18\x03 \x02(\t\x12\x13\n\x0b\x64\x65scription\x18\x04 \x01(\t')
)




_SERVERIDENTITY = _descriptor.Descriptor(
  name='ServerIdentity',
  full_name='network.ServerIdentity',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='public', full_name='network.ServerIdentity.public', index=0,
      number=1, type=12, cpp_type=9, label=2,
      has_default_value=False, default_value=_b(""),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='id', full_name='network.ServerIdentity.id', index=1,
      number=2, type=12, cpp_type=9, label=1,
      has_default_value=False, default_value=_b(""),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='address', full_name='network.ServerIdentity.address', index=2,
      number=3, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='description', full_name='network.ServerIdentity.description', index=3,
      number=4, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto2',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=34,
  serialized_end=116,
)

DESCRIPTOR.message_types_by_name['ServerIdentity'] = _SERVERIDENTITY
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

ServerIdentity = _reflection.GeneratedProtocolMessageType('ServerIdentity', (_message.Message,), dict(
  DESCRIPTOR = _SERVERIDENTITY,
  __module__ = 'network.network_pb2'
  # @@protoc_insertion_point(class_scope:network.ServerIdentity)
  ))
_sym_db.RegisterMessage(ServerIdentity)


# @@protoc_insertion_point(module_scope)
