//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.api;

import java.awt.event.WindowEvent;

public interface Stage
{
  Theater theater();
  
  boolean should_allow_close();

  void closing(WindowEvent e);

  void closed(WindowEvent e);

  void iconified(WindowEvent e);

  void deiconified(WindowEvent e);

  void activated(WindowEvent e);

  void deactivated(WindowEvent e);
}
