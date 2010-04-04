package limelight.ui.model.inputs;

import junit.framework.TestCase;
import limelight.ui.api.MockProp;
import limelight.ui.model.*;

public class ScrollLayoutTest extends TestCase
{
  private RootPanel root;
  private PropPanel parent;

  public void setUp() throws Exception
  {
    root = new RootPanel(new MockProp());
    root.setFrame(new MockPropFrame());
    parent = new PropPanel(new MockProp());
    root.add(parent);
    parent.getStyle().setWidth("100");
    parent.getStyle().setHeight("100");
  }

  public void testScrollContentThatIsntAlignedTopLeft() throws Exception
  {
    parent.getStyle().setScrollbars("on");
    parent.getStyle().setAlignment("center");
    PropPanel panel = addChildWithSize(parent, "200", "200");
    PropPanelLayout.instance.doLayout(parent);

    parent.getVerticalScrollbar().setValue(1);
    ScrollLayout verticalLayout = new ScrollLayout(ScrollBarPanel.VERTICAL, parent.getVerticalScrollbar().getScrollBar());
    verticalLayout.doLayout(parent);
    assertEquals(-1, panel.getY());
                                                                                            
    parent.getHorizontalScrollbar().setValue(2);
    ScrollLayout horizontalLayout = new ScrollLayout(ScrollBarPanel.HORIZONTAL, parent.getHorizontalScrollbar().getScrollBar());
    horizontalLayout.doLayout(parent);
    assertEquals(-2, panel.getX());
  }

  private PropPanel addChildWithSize(BasePanel parent, String width, String height)
  {
    PropPanel panel = new PropPanel(new MockProp());
    panel.getStyle().setWidth(width);
    panel.getStyle().setHeight(height);
    parent.add(panel);
    return panel;
  }
}
