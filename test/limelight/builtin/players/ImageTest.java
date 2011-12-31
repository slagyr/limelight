package limelight.builtin.players;

import limelight.model.api.FakePropProxy;
import limelight.ui.events.panel.CastEvent;
import limelight.ui.model.PropPanel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ImageTest
{
  public Image image;
  public PropPanel propPanel;

  @Before
  public void setUp() throws Exception
  {
    image = new Image();
    propPanel = new PropPanel(new FakePropProxy());
    image.install(new CastEvent(propPanel));
  }

  @Test
  public void installation() throws Exception
  {
    assertEquals(propPanel, image.getPropPanel());
    assertNotNull(image.getImagePanel());
    assertEquals(image.getImagePanel(), propPanel.getChildren().get(0));
    assertEquals(true, propPanel.isSterilized());
    assertNotNull(propPanel.getStagehands().get("image"));
    assertEquals(Image.class, propPanel.getStagehands().get("image").getClass());
  }

  @Test
  public void settingTheImagePath() throws Exception
  {
    image.setFilename("foo.jpg");
    assertEquals("foo.jpg", image.getFilename());
    assertEquals("foo.jpg", image.getImagePanel().getFilename());
  }

  @Test
  public void rotation() throws Exception
  {
    image.setRotation(123.45);
    assertEquals(123.45, image.getRotation(), 0.01);
    assertEquals(123.45, image.getImagePanel().getRotation(), 0.01);
  }

  @Test
  public void scaled() throws Exception
  {
    assertEquals(true, image.isScaled());
    image.setScaled(false);
    assertEquals(false, image.isScaled());
    assertEquals(false, image.getImagePanel().isScaled());
  }
}

