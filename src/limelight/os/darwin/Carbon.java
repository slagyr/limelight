//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.os.darwin;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.NativeLibrary;
import com.sun.jna.ptr.IntByReference;

public interface Carbon extends Library
{
  Carbon INSTANCE = (Carbon) Native.loadLibrary("Carbon", Carbon.class);
  NativeLibrary library = NativeLibrary.getInstance("Carbon");

  //SystemUIMode
  int kUIModeNormal = 0;
  int kUIModeContentSuppressed = 1;
  int kUIModeContentHidden = 2;
  int kUIModeAllSuppressed = 4;
  int kUIModeAllHidden = 3;

  //SystemUIOptions
  int kUIOptionAutoShowMenuBar = 1 << 0;
  int kUIOptionDisableAppleMenu = 1 << 2;
  int kUIOptionDisableProcessSwitch = 1 << 3;
  int kUIOptionDisableForceQuit = 1 << 4;
  int kUIOptionDisableSessionTerminate = 1 << 5;
  int kUIOptionDisableHide = 1 << 6;

//  SystemUIMode originalMode;
//SystemUIOptions originalOptions;

//  public int GetSystemUIMode(Pointer mode, Pointer options);
  public int GetSystemUIMode(IntByReference mode, IntByReference options);

  public int SetSystemUIMode(int mode, int options);

}
