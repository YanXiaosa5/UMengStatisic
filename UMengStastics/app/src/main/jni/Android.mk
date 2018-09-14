LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := uninstall
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	C:\fanhua-project-android\UMengStastics\app\src\main\jni\uninstall.c \

LOCAL_C_INCLUDES += C:\fanhua-project-android\UMengStastics\app\src\debug\jni
LOCAL_C_INCLUDES += C:\fanhua-project-android\UMengStastics\app\src\main\jni

include $(BUILD_SHARED_LIBRARY)
