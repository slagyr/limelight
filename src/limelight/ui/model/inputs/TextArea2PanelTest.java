package limelight.ui.model.inputs;

import limelight.ui.MockGraphics;
import limelight.ui.api.MockProp;
import limelight.ui.model.PropPanel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextArea2PanelTest
{
  TextArea2Panel panel;
  PropPanel parent;
  MockGraphics graphics;
  TextModel boxInfo;

  @Before
  public void setUp()
  {
    panel = new TextArea2Panel();
    parent = new PropPanel(new MockProp());
    parent.add(panel);
    graphics = new MockGraphics();
    boxInfo = panel.getModelInfo();
    boxInfo.setText("Some Text");
  }

  @Test
  public void canResetPaintableRegion()
  {
    panel.resetPaintableRegion();

    assertEquals(TextModel.SIDE_TEXT_MARGIN, panel.paintableRegion.x);
    assertEquals(panel.getWidth() - TextModel.SIDE_TEXT_MARGIN * 2,panel.paintableRegion.width);
  }

}
