//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.model.api.FakePropProxy;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropPanelLayoutTest
{
  private PropPanel parent;
  private PropPanelLayout layout;
  private FakeScene root;

  @Before
  public void setUp() throws Exception
  {
    root = new FakeScene();
    MockStage frame = new MockStage();
    root.setStage(frame);
    parent = new PropPanel(new FakePropProxy());
    root.add(parent);

    layout = PropPanelLayout.instance;
  }

  @Test
  public void sizeUsingAutoWidthAndHeight() throws Exception
  {
    root.setSize(100, 100);
    parent.getStyle().setWidth("auto");
    parent.getStyle().setHeight("auto");
    layout.snapToSize(parent);
    assertEquals(100, parent.getWidth());
    assertEquals(100, parent.getHeight());

    parent.getStyle().setWidth("auto");
    parent.getStyle().setHeight("50");
    layout.snapToSize(parent);
    assertEquals(100, parent.getWidth());
    assertEquals(50, parent.getHeight());

    parent.getStyle().setWidth("42%");
    parent.getStyle().setHeight("auto");
    layout.snapToSize(parent);
    assertEquals(42, parent.getWidth());
    assertEquals(100, parent.getHeight());
  }

}
