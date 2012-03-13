package limelight.builtin.players;

import limelight.model.api.FakePropProxy;
import limelight.model.api.FakeSceneProxy;
import limelight.ui.events.panel.CastEvent;
import limelight.ui.model.PropPanel;
import limelight.ui.model.ScenePanel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TextBoxTest
{
  public TextBox textBox;
  public PropPanel propPanel;

  @Before
  public void setUp() throws Exception
  {
    textBox = new TextBox();
    propPanel = new PropPanel(new FakePropProxy());
    new ScenePanel(new FakeSceneProxy()).add(propPanel);
    textBox.install(new CastEvent(propPanel));
  }

  @Test
  public void installation() throws Exception
  {
    assertEquals(propPanel, textBox.getPropPanel());
    assertNotNull(textBox.getTextBoxPanel());
    assertEquals(textBox.getTextBoxPanel(), propPanel.getChildren().get(0));
    assertEquals(true, propPanel.isSterilized());
    assertNotNull(propPanel.getBackstage().get("text-box"));
    assertEquals(TextBox.class, propPanel.getBackstage().get("text-box").getClass());
  }

  @Test
  public void passwordMode() throws Exception
  {
    assertEquals(false, textBox.isPassword());

    textBox.setPassword(true);

    assertEquals(true, textBox.isPassword());
  }
}
