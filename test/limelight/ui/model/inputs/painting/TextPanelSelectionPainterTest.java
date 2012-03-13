//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.inputs.painting;

import limelight.ui.MockGraphics;
import limelight.ui.MockTypedLayoutFactory;
import limelight.ui.text.TextLocation;
import limelight.util.Box;
import limelight.util.Colors;
import limelight.util.TestUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

public class TextPanelSelectionPainterTest
{
  public TextPanelPainter painter;
  public limelight.ui.model.text.TextModel model;
  public limelight.ui.model.text.MockTextContainer container;
  public MockGraphics graphics;

  @Before
  public void setUp()
  {
    assumeTrue(TestUtil.notHeadless());
    container = new limelight.ui.model.text.MockTextContainer();
    container.bounds = new Box(0, 0, 150, 28);

    model = new limelight.ui.model.text.MultiLineTextModel(container);
    model.setTypedLayoutFactory(MockTypedLayoutFactory.instance);
    model.setText("Some Text");
    model.setCaretLocation(TextLocation.at(0, 4));

    graphics = new MockGraphics();
    container.cursorOn = true;
    painter = TextPanelSelectionPainter.instance;
    model.startSelection(TextLocation.at(0, 6));
  }

  @Test
  public void willNotPaintIfSelectionIsOff()
  {
    model.deactivateSelection();

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
    container = new limelight.ui.model.text.MockTextContainer();
    container.bounds = new Box(0, 0, 150, 30);
    model = new limelight.ui.model.text.MultiLineTextModel(container);
    model.setTypedLayoutFactory(MockTypedLayoutFactory.instance);
    model.setText("This is some\nMulti lined text");
    model.startSelection(TextLocation.at(0, 2));
    model.setCaretLocation(TextLocation.at(1, 5));

    painter.paint(graphics, model);

    assertEquals(new Box(20, 0, 130, 10), graphics.filledShapes.get(0).shape);
    assertEquals(new Box(0, 11, 50, 10), graphics.filledShapes.get(1).shape);
  }
}
