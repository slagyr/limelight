package limelight.ui.model.inputs;

import junit.framework.TestCase;
import limelight.ui.model.PropPanel;
import limelight.ui.api.MockProp;

public class ButtonPanelTest extends TestCase
{
  private ButtonPanel panel;
  private PropPanel parent;

  public void setUp() throws Exception
  {
    panel = new ButtonPanel();
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
    assertEquals("blah", panel.getButton().getText());
  }
  
  public void testSettingParentSteralizesParent() throws Exception
  {
    assertEquals(true, parent.isSterilized());
  }
  
}
