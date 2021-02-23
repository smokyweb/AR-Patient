/* This file was generated by upbc (the upb compiler) from the input
 * file:
 *
 *     envoy/config/core/v3/grpc_service.proto
 *
 * Do not edit -- your changes will be discarded when the file is
 * regenerated. */

#ifndef ENVOY_CONFIG_CORE_V3_GRPC_SERVICE_PROTO_UPBDEFS_H_
#define ENVOY_CONFIG_CORE_V3_GRPC_SERVICE_PROTO_UPBDEFS_H_

#if COCOAPODS==1
  #include  "third_party/upb/upb/def.h"
#else
  #include  "upb/def.h"
#endif
#if COCOAPODS==1
  #include  "third_party/upb/upb/port_def.inc"
#else
  #include  "upb/port_def.inc"
#endif
#ifdef __cplusplus
extern "C" {
#endif

#if COCOAPODS==1
  #include  "third_party/upb/upb/def.h"
#else
  #include  "upb/def.h"
#endif

#if COCOAPODS==1
  #include  "third_party/upb/upb/port_def.inc"
#else
  #include  "upb/port_def.inc"
#endif

extern upb_def_init envoy_config_core_v3_grpc_service_proto_upbdefinit;

UPB_INLINE const upb_msgdef *envoy_config_core_v3_GrpcService_getmsgdef(upb_symtab *s) {
  _upb_symtab_loaddefinit(s, &envoy_config_core_v3_grpc_service_proto_upbdefinit);
  return upb_symtab_lookupmsg(s, "envoy.config.core.v3.GrpcService");
}

UPB_INLINE const upb_msgdef *envoy_config_core_v3_GrpcService_EnvoyGrpc_getmsgdef(upb_symtab *s) {
  _upb_symtab_loaddefinit(s, &envoy_config_core_v3_grpc_service_proto_upbdefinit);
  return upb_symtab_lookupmsg(s, "envoy.config.core.v3.GrpcService.EnvoyGrpc");
}

UPB_INLINE const upb_msgdef *envoy_config_core_v3_GrpcService_GoogleGrpc_getmsgdef(upb_symtab *s) {
  _upb_symtab_loaddefinit(s, &envoy_config_core_v3_grpc_service_proto_upbdefinit);
  return upb_symtab_lookupmsg(s, "envoy.config.core.v3.GrpcService.GoogleGrpc");
}

UPB_INLINE const upb_msgdef *envoy_config_core_v3_GrpcService_GoogleGrpc_SslCredentials_getmsgdef(upb_symtab *s) {
  _upb_symtab_loaddefinit(s, &envoy_config_core_v3_grpc_service_proto_upbdefinit);
  return upb_symtab_lookupmsg(s, "envoy.config.core.v3.GrpcService.GoogleGrpc.SslCredentials");
}

UPB_INLINE const upb_msgdef *envoy_config_core_v3_GrpcService_GoogleGrpc_GoogleLocalCredentials_getmsgdef(upb_symtab *s) {
  _upb_symtab_loaddefinit(s, &envoy_config_core_v3_grpc_service_proto_upbdefinit);
  return upb_symtab_lookupmsg(s, "envoy.config.core.v3.GrpcService.GoogleGrpc.GoogleLocalCredentials");
}

UPB_INLINE const upb_msgdef *envoy_config_core_v3_GrpcService_GoogleGrpc_ChannelCredentials_getmsgdef(upb_symtab *s) {
  _upb_symtab_loaddefinit(s, &envoy_config_core_v3_grpc_service_proto_upbdefinit);
  return upb_symtab_lookupmsg(s, "envoy.config.core.v3.GrpcService.GoogleGrpc.ChannelCredentials");
}

UPB_INLINE const upb_msgdef *envoy_config_core_v3_GrpcService_GoogleGrpc_CallCredentials_getmsgdef(upb_symtab *s) {
  _upb_symtab_loaddefinit(s, &envoy_config_core_v3_grpc_service_proto_upbdefinit);
  return upb_symtab_lookupmsg(s, "envoy.config.core.v3.GrpcService.GoogleGrpc.CallCredentials");
}

UPB_INLINE const upb_msgdef *envoy_config_core_v3_GrpcService_GoogleGrpc_CallCredentials_ServiceAccountJWTAccessCredentials_getmsgdef(upb_symtab *s) {
  _upb_symtab_loaddefinit(s, &envoy_config_core_v3_grpc_service_proto_upbdefinit);
  return upb_symtab_lookupmsg(s, "envoy.config.core.v3.GrpcService.GoogleGrpc.CallCredentials.ServiceAccountJWTAccessCredentials");
}

UPB_INLINE const upb_msgdef *envoy_config_core_v3_GrpcService_GoogleGrpc_CallCredentials_GoogleIAMCredentials_getmsgdef(upb_symtab *s) {
  _upb_symtab_loaddefinit(s, &envoy_config_core_v3_grpc_service_proto_upbdefinit);
  return upb_symtab_lookupmsg(s, "envoy.config.core.v3.GrpcService.GoogleGrpc.CallCredentials.GoogleIAMCredentials");
}

UPB_INLINE const upb_msgdef *envoy_config_core_v3_GrpcService_GoogleGrpc_CallCredentials_MetadataCredentialsFromPlugin_getmsgdef(upb_symtab *s) {
  _upb_symtab_loaddefinit(s, &envoy_config_core_v3_grpc_service_proto_upbdefinit);
  return upb_symtab_lookupmsg(s, "envoy.config.core.v3.GrpcService.GoogleGrpc.CallCredentials.MetadataCredentialsFromPlugin");
}

UPB_INLINE const upb_msgdef *envoy_config_core_v3_GrpcService_GoogleGrpc_CallCredentials_StsService_getmsgdef(upb_symtab *s) {
  _upb_symtab_loaddefinit(s, &envoy_config_core_v3_grpc_service_proto_upbdefinit);
  return upb_symtab_lookupmsg(s, "envoy.config.core.v3.GrpcService.GoogleGrpc.CallCredentials.StsService");
}

UPB_INLINE const upb_msgdef *envoy_config_core_v3_GrpcService_GoogleGrpc_ChannelArgs_getmsgdef(upb_symtab *s) {
  _upb_symtab_loaddefinit(s, &envoy_config_core_v3_grpc_service_proto_upbdefinit);
  return upb_symtab_lookupmsg(s, "envoy.config.core.v3.GrpcService.GoogleGrpc.ChannelArgs");
}

UPB_INLINE const upb_msgdef *envoy_config_core_v3_GrpcService_GoogleGrpc_ChannelArgs_Value_getmsgdef(upb_symtab *s) {
  _upb_symtab_loaddefinit(s, &envoy_config_core_v3_grpc_service_proto_upbdefinit);
  return upb_symtab_lookupmsg(s, "envoy.config.core.v3.GrpcService.GoogleGrpc.ChannelArgs.Value");
}

UPB_INLINE const upb_msgdef *envoy_config_core_v3_GrpcService_GoogleGrpc_ChannelArgs_ArgsEntry_getmsgdef(upb_symtab *s) {
  _upb_symtab_loaddefinit(s, &envoy_config_core_v3_grpc_service_proto_upbdefinit);
  return upb_symtab_lookupmsg(s, "envoy.config.core.v3.GrpcService.GoogleGrpc.ChannelArgs.ArgsEntry");
}

#ifdef __cplusplus
}  /* extern "C" */
#endif

#if COCOAPODS==1
  #include  "third_party/upb/upb/port_undef.inc"
#else
  #include  "upb/port_undef.inc"
#endif

#endif  /* ENVOY_CONFIG_CORE_V3_GRPC_SERVICE_PROTO_UPBDEFS_H_ */
