package limelight.ui.model.inputs.painting;

import limelight.ui.MockGraphics;
import limelight.ui.model.inputs.MockTextContainer;
import limelight.ui.model.inputs.MockTextModel;
import limelight.ui.model.inputs.TextModel;
import limelight.ui.text.TextLocation;
import limelight.util.Box;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

public class TextPanelCaretPainterTest
{
  private MockTextContainer container;
  private TextModel model;
  private MockGraphics graphics;
  private TextPanelPainter painter;

  @Before
  public void setUp()
  {
    container = new MockTextContainer();
    container.bounds = new Box(0, 0, 150, 20);

    model = new MockTextModel(container);
    model.setText("Some Text");
    model.setCaretLocation(TextLocation.at(0, 4));

    graphics = new MockGraphics();
    container.cursorOn = true;
    painter = TextPanelCaretPainter.instance;
  }

  @Test
  public void willNotPaintIfTheCaretIsOff()
  {
    container.cursorOn = false;

    painter.paint(graphics, model);

    assertEquals(true, graphics.drawnLines.isEmpty());
  }

  @Test
  public void willPaintIfTheCaretIsOn()
  {
    model.setCaretOn(true);
    painter.paint(graphics, model);

    assertEquals(false, graphics.filledShapes.isEmpty());
  }
  
  @Test
  public void willNotPaintIfTheModelHasASelection()
  {
    model.setCaretOn(true);
    model.selectAll();
    painter.paint(graphics, model);

    assertEquals(true, graphics.filledShapes.isEmpty());
  }

  @Test
  public void willPaintTheCaretAtTheCaretX()
  {
    model.setCaretOn(true);
    painter.paint(graphics, model);

    assertEquals(new Box(40, 0, 1, 10), graphics.filledShapes.get(0).shape);
  }

  @Test
  public void willPaintTheCaretBlack()
  {
    model.setCaretOn(true);
    painter.paint(graphics, model);

    assertEquals(Color.black, graphics.getColor());
  }
}
