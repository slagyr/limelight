//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.api;

import limelight.ResourceLoader;
import limelight.styles.ScreenableStyle;
import limelight.styles.Style;
import limelight.ui.Panel;

public class MockProp implements Prop
{
  public Style hoverStyle;
  public String text;
  public String name;
  public boolean hooverOn;
  public Scene scene;
  public ResourceLoader loader;

  public MockProp()
  {
  }

  public MockProp(String name)
  {
    this();
    this.name = name;
  }

  public Panel getPanel()
  {
    return null;
  }

  public ScreenableStyle getStyle()
  {
    throw new RuntimeException("getStyle called on MockProp");
  }

  public Style getHoverStyle()
  {
    return hoverStyle;
  }

  public String getName()
  {
    return name;
  }

  public String getText()
  {
    return text;
  }

  public Scene getScene()
  {
    return scene;
  }

  public ResourceLoader getLoader()
  {
    return loader;
  }

  public void setText(String value)
  {
    text = value;
  }
}
