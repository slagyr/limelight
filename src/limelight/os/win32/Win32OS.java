//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.os.win32;
import limelight.os.OS;

public class Win32OS extends OS
{
  private int hookThreadId;
  // Create this object here and keep it around so it doesn't get garbage collected.
  private final KeyboardHandler keyboardHandler = new KeyboardHandler();

  public String dataRoot()
  {
    return System.getProperty("user.home") + "/Application Data/Limelight";
  }

  protected void turnOnKioskMode()
  {
    new Thread()
    {
      public void run()
      {
        W32API.HINSTANCE appInstance = Kernel32.INSTANCE.GetModuleHandle(null);
        final User32.HHOOK keystrokeHook = User32.INSTANCE.SetWindowsHookEx(User32.WH_KEYBOARD_LL, keyboardHandler, appInstance, 0);

        hookThreadId = Kernel32.INSTANCE.GetCurrentThreadId();
        MsgLoop();
        User32.INSTANCE.UnhookWindowsHookEx(keystrokeHook);
      }
    }.start();
  }

  protected void turnOffKioskMode()
  {
    if(hookThreadId == 0)
      return;

    User32.INSTANCE.PostThreadMessage(new W32API.DWORD(hookThreadId), User32.WM_QUIT, new W32API.WPARAM(0), new W32API.LPARAM(0));
    hookThreadId = 0;
  }

  protected void launch(String URL) throws java.io.IOException
  {
    runtime.exec("cmd.exe", "/C", "start", URL);
  }

  private void MsgLoop()
  {
    User32.MSG message = new User32.MSG();

    while(User32.INSTANCE.GetMessage(message, null, 0, 0) != 0)
    {
      User32.INSTANCE.TranslateMessage(message);
      User32.INSTANCE.DispatchMessage(message);
    }
  }

  private static class KeyboardHandler implements User32.LowLevelKeyboardProc
  {
    public W32API.LRESULT callback(int nCode, W32API.WPARAM wParam, User32.KBDLLHOOKSTRUCT lParam)
    {
      boolean shouldEatKeys = false;

      if(nCode == User32.HC_ACTION)
      {
        switch(wParam.intValue())
        {
          case User32.WM_KEYDOWN:
          case User32.WM_SYSKEYDOWN:
          case User32.WM_KEYUP:
          case User32.WM_SYSKEYUP:

            boolean alt = (lParam.flags & User32.LLKHF_ALTDOWN) != 0;
            boolean ctrl = (User32.INSTANCE.GetKeyState(User32.VK_CONTROL) & 0x8000) != 0;
            boolean tab = lParam.vkCode == User32.VK_TAB;
            boolean esc = lParam.vkCode == User32.VK_ESCAPE;
            boolean del = lParam.vkCode == User32.VK_DELETE;
            boolean space = lParam.vkCode == User32.VK_SPACE;
            boolean windowsKey = (lParam.vkCode == 91) || (lParam.vkCode == 92) || (lParam.vkCode == 93);

            shouldEatKeys = (alt && tab) || (alt && esc) || (alt && space) || (ctrl && esc) || windowsKey || (ctrl && alt && del);

//System.err.println("alt:" + alt + " ctrl:" + ctrl + " space:" + space + " tab:" + tab + " del:" + del + " windowsKey:" + windowsKey);

            break;
        }
      }
      int value = shouldEatKeys ? 1 : User32.INSTANCE.CallNextHookEx(new User32.HHOOK(), nCode, wParam, lParam.getPointer()).intValue();
      return new W32API.LRESULT(value);
    }
  }

  public void configureSystemProperties()
  {
    System.setProperty("jruby.shell", "cmd.exe");
    System.setProperty("jruby.script", "jruby.bat org.jruby.Main");
  }
}
