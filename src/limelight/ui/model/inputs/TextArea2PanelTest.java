//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.MockGraphics;
import limelight.ui.api.MockProp;
import limelight.ui.model.PropPanel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TextArea2PanelTest extends Assert
{
  TextArea2Panel panel;
  PropPanel parent;
  MockGraphics graphics;
  TextModel boxInfo;

  @Before
  public void setUp()
  {
    panel = new TextArea2Panel();
    parent = new PropPanel(new MockProp());
    parent.add(panel);
    graphics = new MockGraphics();
    boxInfo = panel.getModelInfo();
    boxInfo.setText("Some Text");
  }
  
  @Test
  public void shouldDefaultStyles() throws Exception
  {
    assertEquals("150", panel.getStyle().getWidth());
    assertEquals("75", panel.getStyle().getHeight());
  }

}
