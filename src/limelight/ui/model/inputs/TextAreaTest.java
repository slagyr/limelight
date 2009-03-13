//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import junit.framework.TestCase;

public class TextAreaTest extends TestCase
{
  private TextArea textArea;

  public void setUp() throws Exception
  {
    TextAreaPanel panel = new TextAreaPanel();
    textArea = new TextArea(panel);
  }

  public void testLineWrapping() throws Exception
  {
    assertEquals(true, textArea.getLineWrap());
  }
}
