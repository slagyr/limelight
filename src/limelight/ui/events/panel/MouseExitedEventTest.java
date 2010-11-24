//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.events.panel;

import limelight.ui.MockPanel;
import limelight.ui.Panel;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static junit.framework.Assert.assertEquals;

public class MouseExitedEventTest
{
  private Panel panel;
  private MouseExitedEvent event;

  @Before
  public void setUp() throws Exception
  {
    panel = new MockPanel();
    event = new MouseExitedEvent(0, new Point(0, 0), 0);
  }
  
  @Test
  public void isNotInheritable() throws Exception
  {
    assertEquals(false, event.isInheritable());
  }
}
