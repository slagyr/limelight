package limelight.builtin.players;

import limelight.model.api.FakePropProxy;
import limelight.ui.events.panel.CastEvent;
import limelight.ui.model.FakeScene;
import limelight.ui.model.PropPanel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RadioButtonTest
{
  public RadioButton button;
  public PropPanel propPanel;

  @Before
  public void setUp() throws Exception
  {
    button = new RadioButton();
    propPanel = new PropPanel(new FakePropProxy());
    new FakeScene().add(propPanel);
    button.install(new CastEvent(propPanel));
  }

  @Test
  public void installation() throws Exception
  {
    assertEquals(propPanel, button.getPropPanel());
    assertNotNull(button.getButtonPanel());
    assertEquals(button.getButtonPanel(), propPanel.getChildren().get(0));
    assertEquals(true, propPanel.isSterilized());
    assertNotNull(propPanel.getBackstage().get("radio-button"));
    assertEquals(RadioButton.class, propPanel.getBackstage().get("radio-button").getClass());
  }
  
  @Test
  public void selected() throws Exception
  {
    assertEquals(false, button.isSelected());
    button.setSelected(true);
    assertEquals(true, button.isSelected());
    button.setSelected(false);
    assertEquals(false, button.isSelected());
    button.select();
    assertEquals(true, button.isSelected());
  }

  @Test
  public void group() throws Exception
  {
    button.setGroup("turtles");
    assertEquals("turtles", button.getGroup());
    assertEquals("turtles", button.getButtonPanel().getGroup());
  }
}
