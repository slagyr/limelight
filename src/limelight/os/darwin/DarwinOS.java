package limelight.os.darwin;

import limelight.os.OS;

//import javax.swing.*;
//import java.awt.event.ActionListener;
//import java.awt.event.ActionEvent;
//import java.awt.event.WindowListener;
//import java.awt.event.WindowEvent;
//import java.awt.*;

public class DarwinOS extends OS
{
  static
  {
    System.loadLibrary("LLNativeDarwin");
  }

//  public static void main(String[] args)
//  {
//    final DarwinOS os = new DarwinOS();
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
//
//    frame.setVisible(true);
//    frame.setAlwaysOnTop(true);
//    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
//  }
}
