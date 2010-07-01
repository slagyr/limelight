package limelight.ui.model.inputs.painting;

import limelight.ui.MockGraphics;
import limelight.ui.MockPanel;
import limelight.ui.model.inputs.MockTextContainer;
import limelight.ui.model.inputs.MockTextModel;
import limelight.util.Box;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

public class TextPanelCaretPainterTest
{
  private MockTextContainer container;
  private MockTextModel model;
  private MockGraphics graphics;
  private TextPanelPainter painter;

  @Before
  public void setUp()
  {
    container = new MockTextContainer();
    container.bounds = new Box(0, 0, 150, 20);

    model = new MockTextModel(container);
    model.addLayout("Some Text");
    model.setCaretIndex(4);

    graphics = new MockGraphics();
    container.cursorOn = true;
    painter = TextPanelCaretPainter.instance;
  }

  @Test
  public void willNotPaintIfTheCursorIsOff()
  {
    container.cursorOn = false;

    painter.paint(graphics, model);

    assertEquals(true, graphics.drawnLines.isEmpty());
  }

  @Test
  public void willPaintIfTheCursorIsOn()
  {
    painter.paint(graphics, model);

    assertEquals(false, graphics.filledShapes.isEmpty());
  }

  @Test
  public void willPaintTheCursorAtTheCursorX()
  {
    painter.paint(graphics, model);

    assertEquals(new Box(0, 0, 1, 10), graphics.filledShapes.get(0).shape);
  }

  @Test
  public void willPaintTheCursorBlack()
  {
    painter.paint(graphics, model);

    assertEquals(Color.black, graphics.getColor());
  }
}
