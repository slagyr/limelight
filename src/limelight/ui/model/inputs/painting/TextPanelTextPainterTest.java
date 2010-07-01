package limelight.ui.model.inputs.painting;

import limelight.ui.MockTypedLayout;
import limelight.util.Colors;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

public class TextPanelTextPainterTest extends AbstractTextPanelPainterTest
{
  private MockTypedLayout layout;

  @Before
  public void setUp()
  {
    testClassInit();
    painter = TextPanelTextPainter.instance;
    layout = new MockTypedLayout(model.getText());
    model.getLines().add(layout);
  }

  @Test
  public void willNotPaintTextIfTextIsNull()
  {
    model.setText(null);

    painter.paint(graphics, model);

    assertEquals(false, layout.hasDrawn());
  }

  @Test
  public void willDrawTextToLayout()
  {
    painter.paint(graphics, model);

    assertEquals(true, layout.hasDrawn());
    assertEquals(model.getText(), layout.text);
  }

  @Test
  public void willDrawTextToCorrectLocation()
  {
    painter.paint(graphics, model);

    assertEquals(0, layout.drawnX);
    assertEquals(8, layout.drawnY);
  }

  @Test
  public void willDrawTextWithTheRightColor()
  {
    painter.paint(graphics, model);

    assertEquals(Color.black, graphics.color);
  }

  @Test
  public void shouldColorOfText() throws Exception
  {
    container.style.setTextColor("red");

    painter.paint(graphics, model);

    assertEquals(Colors.resolve("red"), graphics.color);
  }
}
