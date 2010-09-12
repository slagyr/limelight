//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import junit.framework.TestCase;
import limelight.ui.model.MockParentPanel;
import limelight.ui.model.ScenePanel;
import limelight.ui.model.MockPropFrame;
import limelight.ui.model.PropPanel;
import limelight.ui.api.MockProp;
import limelight.Context;

public class InputPanelUtilTest extends TestCase
{
  private TestableInputPanel input;
  private PropPanel parent;
  private MockParentPanel rootPanel;
  private TestableInputPanel input2;
  private TestableInputPanel input3;

  public void setUp() throws Exception
  {
    input = new TestableInputPanel();
    parent = new PropPanel(new MockProp());
    parent.add(input);
    Context.instance().keyboardFocusManager = new limelight.KeyboardFocusManager().installed();
  }

  private void attatchRoot()
  {
    ScenePanel root = new ScenePanel(new MockProp());
    root.setFrame(new MockPropFrame());
    rootPanel = new MockParentPanel();
    root.add(rootPanel);
    rootPanel.add(parent);
  }

  private void buildInputTree()
  {
    attatchRoot();
    input2 = new TestableInputPanel();
    input3 = new TestableInputPanel();
    rootPanel.add(input2);
    rootPanel.add(input3);
  }

  public void testFindNextInput() throws Exception
  {
    buildInputTree();
    assertSame(input2, InputPanelUtil.nextInputPanel(input));
    assertSame(input3, InputPanelUtil.nextInputPanel(input2));
    assertSame(input, InputPanelUtil.nextInputPanel(input3));
  }

  public void testFindPreviousInput() throws Exception
  {
    buildInputTree();
    assertSame(input, InputPanelUtil.previousInputPanel(input2));
    assertSame(input2, InputPanelUtil.previousInputPanel(input3));
    assertSame(input3, InputPanelUtil.previousInputPanel(input));
  }
}
