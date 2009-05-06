/*
 *  LLNativeDarwinjnilib.c
 *  LLNativeDarwin
 *
 *  Created by Micah Martin on 5/6/09.
 *  Copyright (c) 2009, 8th Light, Inc.. All rights reserved.
 *
 */

#include "limelight_os_OS.h"
#import <Carbon/Carbon.h>	// Needed for kiosk api functions

SystemUIMode originalMode;
SystemUIOptions originalOptions;

JNIEXPORT void JNICALL Java_limelight_os_OS_turnOnKioskMode(JNIEnv *env, jobject obj)
{
    GetSystemUIMode(&originalMode, &originalOptions);
	SetSystemUIMode(kUIModeContentHidden, kUIOptionDisableForceQuit | kUIOptionDisableAppleMenu);
}

JNIEXPORT void JNICALL Java_limelight_os_OS_turnOffKioskMode(JNIEnv *env, jobject obj)
{
    SetSystemUIMode(originalMode, originalOptions);
}