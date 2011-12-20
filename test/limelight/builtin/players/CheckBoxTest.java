package limelight.builtin.players;

import limelight.model.api.FakePropProxy;
import limelight.ui.events.panel.CastEvent;
import limelight.ui.model.PropPanel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CheckBoxTest
{
  public CheckBox checkBox;
  public PropPanel propPanel;

  @Before
  public void setUp() throws Exception
  {
    checkBox = new CheckBox();
    propPanel = new PropPanel(new FakePropProxy());
    checkBox.install(new CastEvent(propPanel));
  }

  @Test
  public void installation() throws Exception
  {
    assertEquals(propPanel, checkBox.getPropPanel());
    assertNotNull(checkBox.getCheckBoxPanel());
    assertEquals(checkBox.getCheckBoxPanel(), propPanel.getChildren().get(0));
    assertEquals(true, propPanel.isSterilized());
    assertNotNull(propPanel.getStagehands().get("check-box"));
    assertEquals(CheckBox.class, propPanel.getStagehands().get("check-box").getClass());
  }

  @Test
  public void isChecked() throws Exception
  {
    assertEquals(false, checkBox.isChecked());
    assertEquals(false, checkBox.isSelected());

    checkBox.getCheckBoxPanel().setSelected(true);

    assertEquals(true, checkBox.isChecked());
    assertEquals(true, checkBox.isSelected());
  }

  @Test
  public void checking() throws Exception
  {
    checkBox.setChecked(true);
    assertEquals(true, checkBox.getCheckBoxPanel().isSelected());

    checkBox.setSelected(false);
    assertEquals(false, checkBox.getCheckBoxPanel().isSelected());
  }
}
