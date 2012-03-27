//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.inputs;

import limelight.ui.model.Layouts;
import limelight.ui.model.MockParentPanel;
import limelight.util.TestUtil;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assume.assumeTrue;

public class TextInputPanelLayoutTest extends Assert
{
  @Test
  public void instance() throws Exception
  {
    assertEquals(TextInputPanelLayout.class, TextInputPanelLayout.instance.getClass());
  }

  @Test
  public void clearsTextLayout() throws Exception
  {
    assumeTrue(TestUtil.notHeadless());
    MockTextInputPanel panel = new MockTextInputPanel();
    MockParentPanel parent = new MockParentPanel();
    parent.add(panel);

    Layouts.on(panel, TextInputPanelLayout.instance);

    assertEquals(true, panel.mockModel.clearLayoutsCalled);
  }
}
