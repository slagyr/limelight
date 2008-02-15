package limelight.ui;

import junit.framework.TestCase;

public class BlockPanelLayoutTest extends TestCase
{
  private MockBlockPanel panel;
  private MockBlock block;
  private BlockPanelLayout layout;
  private FlatStyle style;
  private MockPanel child;

  public void setUp() throws Exception
  {
    MockRootBlockPanel root = new MockRootBlockPanel();
    panel = new MockBlockPanel();
    root.add(panel);
    block = (MockBlock)panel.getBlock();
    style = block.getStyle();
    layout = panel.getLayout();

    style.setHeight("100");
    style.setWidth("100");

    child = new MockPanel();
    panel.add(child);
  }

  public void testSwitchingToScrollModeWhenChildrenAreTooBig() throws Exception
  {
    child.prepForSnap(500, 500);

    layout.doLayout();

    assertFalse(panel.isChild(child));
    assertEquals(1, panel.getChildren().size());
    ScrollPanel scrollPanel = (ScrollPanel)panel.getChildren().get(0);
    assertSame(child, scrollPanel.getView().getChildren().get(0));
  }

  public void testSwitchingToScrollModeWhenChildrenAreTooWide() throws Exception
  {
    child.prepForSnap(500, 10);

    layout.doLayout();

    assertFalse(panel.isChild(child));
    assertEquals(1, panel.getChildren().size());
    ScrollPanel scrollPanel = (ScrollPanel)panel.getChildren().get(0);
    assertSame(child, scrollPanel.getView().getChildren().get(0));
  }
  
  public void testSwitchingToScrollModeWhenChildrenAreTooTall() throws Exception
  {
    child.prepForSnap(10, 500);

    layout.doLayout();

    assertFalse(panel.isChild(child));
    assertEquals(1, panel.getChildren().size());
    ScrollPanel scrollPanel = (ScrollPanel)panel.getChildren().get(0);
    assertSame(child, scrollPanel.getView().getChildren().get(0));
  }
}
