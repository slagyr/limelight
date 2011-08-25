//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.os.win32;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

public class Win32OSTrial
{
  public static void main(String[] args) throws InterruptedException
  {
    final Win32OS os = new Win32OS();
    os.enterKioskMode();
    Thread.sleep(500);

    JFrame frame = new JFrame();
    frame.setSize(400, 400);
    frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

    frame.add(new JLabel("Type ALT-F4 to exit"));
    final JButton button = new JButton("Exit Kiosk Mode");
    button.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        if(os.isInKioskMode())
        {
          os.exitKioskMode();
          button.setText("Enter Kiosk Mode");
        }
        else
        {
          os.enterKioskMode();
          button.setText("Exit Kiosk Mode");
        }
      }
    });
    frame.add(button);


    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setUndecorated(true);
    frame.setVisible(true);
    frame.setAlwaysOnTop(true);
    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
  }

}
