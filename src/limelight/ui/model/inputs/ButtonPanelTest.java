//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import junit.framework.TestCase;
import limelight.ui.model.PropPanel;
import limelight.ui.api.MockProp;

public class ButtonPanelTest extends TestCase
{
  private Button2Panel panel;
  private PropPanel parent;

  public void setUp() throws Exception
  {
    panel = new Button2Panel();
    parent = new PropPanel(new MockProp());
    parent.add(panel);
  }

  public void testCanBeBuffered() throws Exception
  {
    assertEquals(false, panel.canBeBuffered());
  }
  
  public void testSettingParentSetsTextAccessor() throws Exception
  {
    parent.setText("blah");
    assertEquals("blah", panel.getText());
  }
  
  public void testSettingParentSteralizesParent() throws Exception
  {
    assertEquals(true, parent.isSterilized());
  }
  
}
