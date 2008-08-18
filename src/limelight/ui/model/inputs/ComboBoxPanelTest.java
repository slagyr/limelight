package limelight.ui.model.inputs;

import junit.framework.TestCase;
import limelight.ui.model.PropPanel;
import limelight.ui.api.MockProp;

public class ComboBoxPanelTest extends TestCase
{
  private ComboBoxPanel panel;
  private PropPanel parent;

  public void setUp() throws Exception
  {
    panel = new ComboBoxPanel();
    parent = new PropPanel(new MockProp());
    parent.add(panel);
  }

  public void testCanBeBuffered() throws Exception
  {
    assertEquals(false, panel.canBeBuffered());
  }
  
  public void testSettingParentSetsTextAccessor() throws Exception
  {
    panel.getComboBox().addItem("foo");
    panel.getComboBox().addItem("blah");
    parent.setText("blah");
    assertEquals("blah", panel.getComboBox().getSelectedItem().toString());
  }

  public void testSettingParentSteralizesParent() throws Exception
  {
    assertEquals(true, parent.isSterilized());
  }
}
