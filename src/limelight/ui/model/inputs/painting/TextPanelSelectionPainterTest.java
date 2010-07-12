package limelight.ui.model.inputs.painting;

import limelight.ui.MockTypedLayoutFactory;
import limelight.ui.model.inputs.MockTextContainer;
import limelight.ui.model.inputs.MultiLineTextModel;
import limelight.ui.model.inputs.TextModel;
import limelight.util.Box;
import limelight.util.Colors;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextPanelSelectionPainterTest extends AbstractTextPanelPainterTest
{
  @Before
  public void setUp()
  {
    testClassInit();
    painter = TextPanelSelectionPainter.instance;
    model.setSelectionOn(true);
    model.setSelectionIndex(6);
  }

  @Test
  public void willNotPaintIfSelectionIsOff()
  {
    model.setSelectionOn(false);

    painter.paint(graphics, model);

    assertEquals(true, graphics.filledShapes.isEmpty());
  }

  @Test
  public void willNotPaintIfThereIsNoText()
  {
    model.setText(null);

    painter.paint(graphics, model);

    assertEquals(true, graphics.filledShapes.isEmpty());
  }

  @Test
  public void willFillABoxAroundSelectedText()
  {
    painter.paint(graphics, model);

    assertEquals(new Box(40, 0, 20, 10), graphics.filledShapes.get(0).shape);
  }

  @Test
  public void willFillTheBoxCyan()
  {
    painter.paint(graphics, model);

    assertEquals(Colors.resolve("#BBD453AA"), graphics.filledShapes.getLast().color);
  }

  @Test
  public void willFillMultipleBoxesForMultiLinedSelectedText()
  {
    container = new MockTextContainer();
    container.bounds = new Box(0, 0, 150, 30);
    model = new MultiLineTextModel(container);
    model.setTypedLayoutFactory(MockTypedLayoutFactory.instance);
    model.setText("This is some\nMulti lined text");
    model.setSelectionOn(true);
    model.setSelectionIndex(2);
    model.setCaretIndex(18);

    painter.paint(graphics, model);

    assertEquals(new Box(20, 0, 130, 10), graphics.filledShapes.get(0).shape);
    assertEquals(new Box(0, 11, 50, 10), graphics.filledShapes.get(1).shape);
  }
}
