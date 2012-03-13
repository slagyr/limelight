//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.text.offsetting;

import limelight.ui.MockTypedLayoutFactory;
import limelight.ui.text.TextLocation;
import limelight.util.Box;
import limelight.util.TestUtil;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

public class FittingYOffsetStrategyTest
{
  private limelight.ui.model.text.MultiLineTextModel model;
  private limelight.ui.model.text.MockTextContainer textContainer;

  @Before
  public void setUp() throws Exception
  {
    assumeTrue(TestUtil.notHeadless());
    textContainer = new limelight.ui.model.text.MockTextContainer(new Box(0, 0, 100, 15));
    model = new limelight.ui.model.text.MultiLineTextModel(textContainer);
    model.setTypedLayoutFactory(MockTypedLayoutFactory.instance);
  }

  @Test
  public void shouldOffsetToAllowRoomForCursor() throws Exception
  {
    model.setText("line1\nline2");
    model.setCaretLocation(TextLocation.at(1, 2));
    model.setOffset(0, 0);

    int offset = YOffsetStrategy.FITTING.calculateYOffset(model);
    assertEquals(-6, offset);
  }

  @Test
  public void shouldNotGoNutsWhenTextIsBiggetThanHeight() throws Exception
  {
    textContainer.bounds = new Box(0, 0, 100, 6);
    model.setText("blah");
    model.setCaretLocation(TextLocation.at(0, 2));
    model.setOffset(0, 0);

    assertEquals(-2, YOffsetStrategy.FITTING.calculateYOffset(model));
  }

  @Test
  public void shouldNotHaveOffsetGreaterThen0() throws Exception
  {
    model.setText("line1\nline2");
    model.setCaretLocation(TextLocation.at(0, 2));
    model.setOffset(0, -10);

    assertEquals(0, YOffsetStrategy.FITTING.calculateYOffset(model));
  }
}
