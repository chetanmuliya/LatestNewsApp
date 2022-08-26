//
// Created by chettas on 8/25/2022.
//

#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_learn_cm_latestnewsapp_util_DRK_00024Companion_getRapidApiKey(JNIEnv *env, jobject object) {
    return env->NewStringUTF("283edac266mshbb7e646fd9567d7p163594jsnf436129fd1df");
}
extern "C"
JNIEXPORT jstring JNICALL
Java_learn_cm_latestnewsapp_util_GenericProvider_getRapidApiKey(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("283edac266mshbb7e646fd9567d7p163594jsnf436129fd1df");
}