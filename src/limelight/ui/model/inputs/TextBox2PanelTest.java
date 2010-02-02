package limelight.ui.model.inputs;

import limelight.ui.MockGraphics;
import limelight.ui.api.MockProp;
import limelight.ui.model.PropPanel;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.FocusEvent;

import static org.junit.Assert.assertEquals;

public class TextBox2PanelTest
{
  TextBox2Panel panel;
  PropPanel parent;
  MockGraphics graphics;
  TextModel boxInfo;

  @Before
  public void setUp()
  {
    panel = new TextBox2Panel();
    parent = new PropPanel(new MockProp());
    parent.add(panel);
    graphics = new MockGraphics();
    boxInfo = panel.getBoxInfo();
    boxInfo.setText("Some Text");
  }

  @Test
  public void canResetPaintableRegion()
  {
    assertEquals(TextModel.LEFT_TEXT_MARGIN, panel.paintableRegion.x);
    assertEquals(panel.getWidth(),panel.paintableRegion.width);

    panel.resetPaintableRegion();

    assertEquals(0, panel.paintableRegion.x);
    assertEquals(0, panel.paintableRegion.width);
  }
  
  @Test
  public void canCalculatePaintableRegionFromIndex()
  {
    panel.resetPaintableRegion();
    panel.setPaintableRegion(0);
    panel.setPaintableRegion(4);

    assertEquals(TextModel.LEFT_TEXT_MARGIN,panel.paintableRegion.x );
    assertEquals(boxInfo.getXPosFromIndex(4) - TextModel.LEFT_TEXT_MARGIN, panel.paintableRegion.width);
  }
}
