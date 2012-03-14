package limelight.builtin.players;

import limelight.model.api.FakePropProxy;
import limelight.model.api.FakeSceneProxy;
import limelight.ui.events.panel.CastEvent;
import limelight.ui.model.FakeScene;
import limelight.ui.model.PropPanel;
import limelight.ui.model.ScenePanel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ButtonTest
{
  public Button button;
  public PropPanel propPanel;

  @Before
  public void setUp() throws Exception
  {
    button = new Button();
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
    assertNotNull(propPanel.getBackstage().get("button"));
    assertEquals(Button.class, propPanel.getBackstage().get("button").getClass());
  }
}
