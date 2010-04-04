package limelight.ui.model.inputs;

import junit.framework.TestCase;
import limelight.ui.model.ScenePanel;
import limelight.ui.model.MockPropFrame;
import limelight.ui.model.PropPanel;
import limelight.ui.model.TextAccessor;
import limelight.ui.MockPanel;
import limelight.ui.api.MockProp;
import limelight.Context;
import limelight.styles.Style;

import javax.swing.*;
import java.awt.*;

public class InputPanelUtilTest extends TestCase
{
  private TestableInputPanel input;
  private PropPanel parent;
  private MockPanel rootPanel;
  private TestableInputPanel input2;
  private TestableInputPanel input3;

  private static class TestableInputPanel extends AwtInputPanel
  {
    public Component input;

    protected Component createComponent()
    {
      return input = new JPanel();
    }

    protected TextAccessor createTextAccessor()
    {
      return null;
    }

    protected void setDefaultStyles(Style style)
    {
      style.setWidth("123");
      style.setHeight("456");
    }
  }


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
    rootPanel = new MockPanel();
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
