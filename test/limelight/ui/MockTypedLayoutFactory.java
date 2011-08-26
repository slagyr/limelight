//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui;

import limelight.ui.text.TypedLayoutFactory;
import limelight.ui.text.TypedLayout;

import java.awt.*;
import java.awt.font.FontRenderContext;

public class MockTypedLayoutFactory implements TypedLayoutFactory
{
  public static MockTypedLayoutFactory instance = new MockTypedLayoutFactory();

  private MockTypedLayoutFactory()
  {
  }

  public TypedLayout createLayout(String text, Font font, FontRenderContext renderContext)
  {
    return new MockTypedLayout(text);
  }
}
