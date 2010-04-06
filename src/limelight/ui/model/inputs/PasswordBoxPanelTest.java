//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import junit.framework.TestCase;
import limelight.ui.api.MockProp;
import limelight.ui.model.PropPanel;

public class PasswordBoxPanelTest extends TestCase
{
  private PasswordBoxPanel panel;
  private PropPanel parent;

  public void setUp() throws Exception
  {
    panel = new PasswordBoxPanel();
    parent = new PropPanel(new MockProp());
    parent.add(panel);
  }

  public void testSettingParentSetsTextAccessor() throws Exception
  {
    parent.setText("blah");
    assertEquals("blah", panel.getPasswordBox().getText());
  }

  public void testSettingParentSteralizesParent() throws Exception
  {
    assertEquals(true, parent.isSterilized());
  }

  public void testHasJPasswordBox() throws Exception
  {
    assertEquals(panel.getPasswordBox().getClass(), PasswordBox.class);
  }

  public void testCanBeBuffered() throws Exception
  {
    assertEquals(false, panel.canBeBuffered());
  }

}
