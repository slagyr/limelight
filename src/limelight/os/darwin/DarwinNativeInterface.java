package limelight.os.darwin;

import limelight.os.NativeInterface;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

public class DarwinNativeInterface implements NativeInterface
{
  static
  {
    System.loadLibrary("LLNativeDarwin");
  }

  private boolean inKioskMode;

  private native void turnOnKioskMode();
  private native void turnOffKioskMode();

  public void enterKioskMode()
  {
    turnOnKioskMode();
    inKioskMode = true;
  }

  public void exitKioskMode()
  {
    inKioskMode = false;
    turnOffKioskMode();
  }

  public boolean isInKioskMode()
  {
    return inKioskMode;
  }

//  public static void main(String[] args)
//  {
//    final DarwinNativeInterface os = new DarwinNativeInterface();
//    os.enterKioskMode();
//
//    JFrame frame = new JFrame();
//    frame.setSize(400, 400);
//
//    final JButton button = new JButton("Exit Kiosk Mode");
//    button.addActionListener(new ActionListener()
//    {
//      public void actionPerformed(ActionEvent e)
//      {
//        if(os.isInKioskMode())
//        {
//          os.exitKioskMode();
//          button.setText("Enter Kiosk Mode");
//        }
//        else
//        {
//          os.enterKioskMode();
//          button.setText("Exit Kiosk Mode");
//        }
//      }
//    });
//    frame.add(button);
//
//    frame.setVisible(true);
//    frame.setAlwaysOnTop(true);
//    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
//  }
}
