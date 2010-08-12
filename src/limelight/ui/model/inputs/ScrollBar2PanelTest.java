//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.model.ScenePanel;
import limelight.ui.model.PropPanel;
import limelight.ui.model.MockPropFrame;
import limelight.ui.api.MockProp;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ScrollBar2PanelTest
{
  private ScrollBar2Panel verticalScrollBar;
  private ScrollBar2Panel horizontalScrollBar;

  @Before
  public void setUp() throws Exception
  {
    verticalScrollBar = new ScrollBar2Panel(ScrollBar2Panel.VERTICAL);
    verticalScrollBar.setSize(15, 100);
    verticalScrollBar.configure(10, 100);
    horizontalScrollBar = new ScrollBar2Panel(ScrollBar2Panel.HORIZONTAL);
    horizontalScrollBar.setSize(100, 15);
    horizontalScrollBar.configure(10, 100);
  }

  @Test
  public void shouldHasScrollBarWithOrientation() throws Exception
  {
    assertEquals(ScrollBar2Panel.VERTICAL, verticalScrollBar.getOrientation());

    assertEquals(ScrollBar2Panel.HORIZONTAL, horizontalScrollBar.getOrientation());
  }

  @Test
  public void shouldDefaultSizes() throws Exception
  {
    assertEquals(15, verticalScrollBar.getWidth());
    assertEquals(15, horizontalScrollBar.getHeight());
  }

  @Test
  public void shouldSize() throws Exception
  {
    verticalScrollBar.setSize(100, 200);
    assertEquals(200, verticalScrollBar.getHeight());
    assertEquals(15, verticalScrollBar.getWidth());

    horizontalScrollBar.setSize(100, 200);
    assertEquals(100, horizontalScrollBar.getWidth());
    assertEquals(15, horizontalScrollBar.getHeight());
  }

  @Test
  public void changesCausesLayout() throws Exception
  {
    ScenePanel root = new ScenePanel(new MockProp());
    root.setFrame(new MockPropFrame());
    PropPanel parent = new PropPanel(new MockProp());
    root.add(parent);
    parent.add(verticalScrollBar);
    parent.doLayout();
    verticalScrollBar.setValue(50);

    assertEquals(true, parent.needsLayout());
  }

  @Test
  public void shouldParentIsMarkedAsChanged() throws Exception
  {
    ScenePanel root = new ScenePanel(new MockProp());
    root.setFrame(new MockPropFrame());
    PropPanel parent = new PropPanel(new MockProp());
    root.add(parent);
    parent.add(verticalScrollBar);
    parent.doLayout();

    verticalScrollBar.setValue(50);

    assertEquals(true, parent.needsLayout());
  }

  @Test
  public void shouldConfigure() throws Exception
  {
    verticalScrollBar.configure(100, 500);
    assertEquals(100, verticalScrollBar.getVisibleAmount());
    assertEquals(500, verticalScrollBar.getAvailableAmount());
    assertEquals(5, verticalScrollBar.getUnitIncrement());
    assertEquals(90, verticalScrollBar.getBlockIncrement());

    verticalScrollBar.configure(500, 1000);
    assertEquals(500, verticalScrollBar.getVisibleAmount());
    assertEquals(1000, verticalScrollBar.getAvailableAmount());
    assertEquals(5, verticalScrollBar.getUnitIncrement());
    assertEquals(450, verticalScrollBar.getBlockIncrement());
  }

  @Test
  public void shouldCannotBeBuffered() throws Exception
  {
    assertEquals(false, verticalScrollBar.canBeBuffered());
  }

  @Test
  public void shouldCalculateGemSize() throws Exception
  {
    horizontalScrollBar.setSize(100, 15);

    horizontalScrollBar.configure(50, 100);
    assertEquals(31, horizontalScrollBar.getGemSize());

    horizontalScrollBar.configure(25, 100);
    assertEquals(16, horizontalScrollBar.getGemSize());

    horizontalScrollBar.configure(33, 100);
    assertEquals(20, horizontalScrollBar.getGemSize());
  }

  @Test
  public void shouldHaveMinimunGemSize() throws Exception
  {
    horizontalScrollBar.setSize(100, 15);
    horizontalScrollBar.configure(1, 100);
    assertEquals(16, horizontalScrollBar.getGemSize());
  }

  @Test
  public void shouldCalculateGemLocation() throws Exception
  {
    horizontalScrollBar.setSize(100, 15);
    horizontalScrollBar.configure(50, 100);
    
    horizontalScrollBar.setValue(0);
    assertEquals(5, horizontalScrollBar.getGemLocation());

    horizontalScrollBar.setValue(25);
    assertEquals(21, horizontalScrollBar.getGemLocation());

    horizontalScrollBar.setValue(50);
    assertEquals(36, horizontalScrollBar.getGemLocation());
  }
  
  @Test
  public void CannotSetValueLessThanMin() throws Exception
  {
    verticalScrollBar.setValue(-1);

    assertEquals(0, verticalScrollBar.getValue());
  }
  
  @Test
  public void CannotSetValueHigherThanMax() throws Exception
  {
    verticalScrollBar.configure(10, 100);
    verticalScrollBar.setValue(1000);

    assertEquals(verticalScrollBar.getMaxValue(), verticalScrollBar.getValue());
  }
}