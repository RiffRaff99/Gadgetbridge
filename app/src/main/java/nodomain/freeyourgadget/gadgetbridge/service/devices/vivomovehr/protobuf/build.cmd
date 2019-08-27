@echo off
"c:\Program Files\protoc\bin\protoc.exe" -I=. --java_out=lite:..\..\..\..\..\..\..\ gdi_smart_proto.proto gdi_find_my_watch.proto gdi_device_status.proto gdi_sms_notification.proto
rem "c:\Program Files\protoc\bin\protoc.exe" -I=. --java_out=..\..\..\..\..\..\..\ gdi_smart_proto.proto gdi_find_my_watch.proto gdi_device_status.proto gdi_sms_notification.proto