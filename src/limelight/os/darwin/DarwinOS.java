package limelight.os.darwin;

import limelight.os.OS;
import com.sun.jna.ptr.IntByReference;

public class DarwinOS extends OS
{
  public IntByReference originalMode = new IntByReference(-1);
  public IntByReference originalOptions = new IntByReference(-1);

  protected void turnOnKioskMode()
  {
    Carbon.INSTANCE.GetSystemUIMode(originalMode, originalOptions);
    Carbon.INSTANCE.SetSystemUIMode(Carbon.kUIModeContentHidden, Carbon.kUIOptionDisableAppleMenu | Carbon.kUIOptionDisableForceQuit);
  }

  protected void turnOffKioskMode()
  {
    Carbon.INSTANCE.SetSystemUIMode(originalMode.getValue(), originalOptions.getValue());
  }
}

