//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.MockGraphics;
import limelight.ui.api.MockProp;
import limelight.ui.model.PropPanel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TextBoxPanelTest extends Assert
{
  TextBoxPanel panel;
  PropPanel parent;
  MockGraphics graphics;
  TextModel model;

  @Before
  public void setUp()
  {
    panel = new TextBoxPanel();
    parent = new PropPanel(new MockProp());
    parent.add(panel);
    graphics = new MockGraphics();
    model = panel.getModel();
    model.setText("Some Text");
  }

  @Test
  public void shouldDefaultStyles() throws Exception
  {
    assertEquals("150", panel.getStyle().getWidth());
    assertEquals("28", panel.getStyle().getHeight());
    assertEquals("#ffffffff", panel.getStyle().getBackgroundColor());
  }

  @Test
  public void shouldHaveDefaultPadding()
  {
    assertEquals("2", panel.getStyle().getTopPadding());
    assertEquals("2", panel.getStyle().getRightPadding());
    assertEquals("2", panel.getStyle().getBottomPadding());
    assertEquals("2", panel.getStyle().getLeftPadding());
  }

  @Test
  public void shouldHaveDefaultBorderWidths()
  {
    assertEquals("4", panel.getStyle().getTopBorderWidth());
    assertEquals("4", panel.getStyle().getRightBorderWidth());
    assertEquals("4", panel.getStyle().getBottomBorderWidth());
    assertEquals("4", panel.getStyle().getLeftBorderWidth());
  }

  @Test
  public void shouldHaveDefaultBorderColors()
  {
    assertEquals("#00000000", panel.getStyle().getTopBorderColor());
    assertEquals("#00000000", panel.getStyle().getRightBorderColor());
    assertEquals("#00000000", panel.getStyle().getBottomBorderColor());
    assertEquals("#00000000", panel.getStyle().getLeftBorderColor());
  }
  
  @Test
  public void shouldHaveDefaultAlignment() throws Exception
  {
    assertEquals("center", panel.getStyle().getVerticalAlignment());
    assertEquals("left", panel.getStyle().getHorizontalAlignment());
  }
  
  @Test
  public void shouldHaveDefaultCursor() throws Exception
  {
    assertEquals("text", panel.getStyle().getCursor());
  }
          
  @Test
  public void shouldSetPainterOnParent() throws Exception
  {
    PropPanel newParent = new PropPanel(new MockProp());
    
    panel.setParent(newParent);

    assertEquals(TextInputPanel.TextInputPropPainter.instance, newParent.getPainter());
  }
}
