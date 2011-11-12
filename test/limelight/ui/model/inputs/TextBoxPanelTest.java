//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.inputs;

import limelight.model.api.FakePropProxy;
import limelight.ui.MockGraphics;
import limelight.ui.model.PropPanel;
import limelight.util.TestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assume.assumeTrue;

public class TextBoxPanelTest extends Assert
{
  TextBoxPanel panel;
  PropPanel parent;
  MockGraphics graphics;
  TextModel model;

  @Before
  public void setUp()
  {
    assumeTrue(TestUtil.notHeadless());
    panel = new TextBoxPanel();
    parent = new PropPanel(new FakePropProxy());
    parent.add(panel);
    graphics = new MockGraphics();
    model = panel.getModel();
    model.setText("Some Text");
  }

  @Test
  public void defaultStyles() throws Exception
  {
    assertEquals("150", panel.getStyle().getWidth());
    assertEquals("28", panel.getStyle().getHeight());
    assertEquals("#ffffffff", panel.getStyle().getBackgroundColor());
  }

  @Test
  public void hasDefaultPadding()
  {
    assertEquals("2", panel.getStyle().getTopPadding());
    assertEquals("2", panel.getStyle().getRightPadding());
    assertEquals("2", panel.getStyle().getBottomPadding());
    assertEquals("2", panel.getStyle().getLeftPadding());
  }

  @Test
  public void hasDefaultBorderWidths()
  {
    assertEquals("4", panel.getStyle().getTopBorderWidth());
    assertEquals("4", panel.getStyle().getRightBorderWidth());
    assertEquals("4", panel.getStyle().getBottomBorderWidth());
    assertEquals("4", panel.getStyle().getLeftBorderWidth());
  }

  @Test
  public void hasDefaultBorderColors()
  {
    assertEquals("#00000000", panel.getStyle().getTopBorderColor());
    assertEquals("#00000000", panel.getStyle().getRightBorderColor());
    assertEquals("#00000000", panel.getStyle().getBottomBorderColor());
    assertEquals("#00000000", panel.getStyle().getLeftBorderColor());
  }

  @Test
  public void hasDefaultAlignment() throws Exception
  {
    assertEquals("center", panel.getStyle().getVerticalAlignment());
    assertEquals("left", panel.getStyle().getHorizontalAlignment());
  }

  @Test
  public void hasDefaultCursor() throws Exception
  {
    assertEquals("text", panel.getStyle().getCursor());
  }

  @Test
  public void setsPainterOnParent() throws Exception
  {
    PropPanel newParent = new PropPanel(new FakePropProxy());

    panel.setParent(newParent);

    assertEquals(TextInputPanel.TextInputPropPainter.instance, newParent.getPainter());
  }
}
