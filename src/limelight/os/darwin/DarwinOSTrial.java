//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.os.darwin;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

public class DarwinOSTrial
{
  public static void main(String[] args)
  {
    final DarwinOS os = new DarwinOS();
    os.enterKioskMode();

    JFrame frame = new JFrame();
    frame.setSize(400, 400);

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


    frame.setVisible(true);
    frame.setAlwaysOnTop(true);
    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
  }

}
