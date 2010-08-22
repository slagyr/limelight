//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.model.MockRootPanel;
import limelight.ui.model.ScenePanel;
import limelight.ui.model.PropPanel;
import limelight.ui.model.MockPropFrame;
import limelight.ui.api.MockProp;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ScrollBarPanelTest
{
  private ScrollBarPanel verticalScrollBar;
  private ScrollBarPanel horizontalScrollBar;

  @Before
  public void setUp() throws Exception
  {
    verticalScrollBar = new ScrollBarPanel(ScrollBarPanel.VERTICAL);
    verticalScrollBar.setSize(15, 100);
    verticalScrollBar.configure(10, 100);
    horizontalScrollBar = new ScrollBarPanel(ScrollBarPanel.HORIZONTAL);
    horizontalScrollBar.setSize(100, 15);
    horizontalScrollBar.configure(10, 100);
  }

  @Test
  public void shouldHasScrollBarWithOrientation() throws Exception
  {
    assertEquals(ScrollBarPanel.VERTICAL, verticalScrollBar.getOrientation());

    assertEquals(ScrollBarPanel.HORIZONTAL, horizontalScrollBar.getOrientation());
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
  public void shouldCalculateSliderSize() throws Exception
  {
    horizontalScrollBar.setSize(100, 15);

    horizontalScrollBar.configure(50, 100);
    assertEquals(31, horizontalScrollBar.getSliderSize());

    horizontalScrollBar.configure(25, 100);
    assertEquals(16, horizontalScrollBar.getSliderSize());

    horizontalScrollBar.configure(33, 100);
    assertEquals(20, horizontalScrollBar.getSliderSize());
  }

  @Test
  public void shouldHaveMinimumSliderSize() throws Exception
  {
    horizontalScrollBar.setSize(100, 15);
    horizontalScrollBar.configure(1, 100);
    assertEquals(16, horizontalScrollBar.getSliderSize());
  }

  @Test
  public void shouldCalculateSliderLocation() throws Exception
  {
    horizontalScrollBar.setSize(100, 15);
    horizontalScrollBar.configure(50, 100);
    
    horizontalScrollBar.setValue(0);
    assertEquals(5, horizontalScrollBar.getSliderPosition());

    horizontalScrollBar.setValue(25);
    assertEquals(21, horizontalScrollBar.getSliderPosition());

    horizontalScrollBar.setValue(50);
    assertEquals(36, horizontalScrollBar.getSliderPosition());
  }

  @Test
  public void settingSliderLocation() throws Exception
  {
    horizontalScrollBar.setSize(100, 15);
    horizontalScrollBar.configure(50, 100);
    
    horizontalScrollBar.setSliderPosition(5);
    assertEquals(0, horizontalScrollBar.getValue());

    horizontalScrollBar.setSliderPosition(21);
    assertEquals(26, horizontalScrollBar.getValue());

    horizontalScrollBar.setSliderPosition(36);
    assertEquals(50, horizontalScrollBar.getValue());
  }
  
  @Test
  public void slidePositionStaysWithinBounds() throws Exception
  {
    horizontalScrollBar.setSize(100, 15);
    horizontalScrollBar.configure(50, 100);

    horizontalScrollBar.setSliderPosition(-1);
    assertEquals(horizontalScrollBar.getMinSliderPosition(), horizontalScrollBar.getSliderPosition());

    horizontalScrollBar.setSliderPosition(99999);
    assertEquals(horizontalScrollBar.getMaxSliderPosition(), horizontalScrollBar.getSliderPosition());
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

  @Test
  public void isDirtyAfterSettingValue() throws Exception
  {
    MockRootPanel root = new MockRootPanel();
    root.add(verticalScrollBar);
    verticalScrollBar.configure(10, 100);

    verticalScrollBar.setValue(10);

    assertEquals(verticalScrollBar.getAbsoluteBounds(), root.dirtyRegions.get(0));
  }

  @Test
  public void pressingButtonsWillMakeDirty() throws Exception
  {
    MockRootPanel root = new MockRootPanel();
    root.add(verticalScrollBar);

    verticalScrollBar.setIncreasingButtonActive(true);
    assertEquals(verticalScrollBar.getAbsoluteBounds(), root.dirtyRegions.get(0));
    
    root.dirtyRegions.clear();
    verticalScrollBar.setDecreasingButtonActive(true);
    assertEquals(verticalScrollBar.getAbsoluteBounds(), root.dirtyRegions.get(0));
  }
}
