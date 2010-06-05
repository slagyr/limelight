//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import limelight.Context;
import limelight.styles.abstrstyling.StyleCompiler;
import limelight.styles.abstrstyling.StyleValue;
import limelight.ui.model.ChangeablePanel;

public class StyleAttribute
{
  public final int index;
  public final String name;
  public final StyleCompiler compiler;
  public final StyleValue defaultValue;

  private static int nextIndex = 0;

  public static int nextIndex()
  {
    return nextIndex++;
  }

  public StyleAttribute(String name, StyleCompiler compiler, StyleValue defaultValue)
  {
    index = nextIndex();
    this.name = name;
    this.compiler = compiler;
    this.defaultValue = defaultValue;
  }

  public StyleAttribute(String name, String compilerType, String defaultValue)
  {
    index = nextIndex();
    this.name = name;
    compiler = Context.instance().styleAttributeCompilerFactory.compiler(compilerType, name);
    this.defaultValue = compiler.compile(defaultValue);
  }

  public int getIndex()
  {
    return index;
  }

  public String getName()
  {
    return name;
  }

  public StyleCompiler getCompiler()
  {
    return compiler;
  }

  public StyleValue getDefaultValue()
  {
    return defaultValue;
  }

  public String toString()
  {
    return index + ": " + name;
  }

  public StyleValue compile(Object value)
  {
    return compiler.compile(value);
  }

  public boolean nameMatches(String value)
  {
    return value.toLowerCase().replaceAll("_|\\-", " ").equals(name.toLowerCase());
  }

  public void applyChange(ChangeablePanel panel, StyleValue value)
  {
    expireCache(panel);
    panel.markAsDirty();
  }

  protected void expireCache(ChangeablePanel panel)
  {
    if(Context.instance().bufferedImageCache != null)
      Context.instance().bufferedImageCache.expire(panel);
  }

  protected void handleDimensionChange(ChangeablePanel panel)
  {
    expireCache(panel);
    panel.setSizeChangePending(true);
    panel.markAsNeedingLayout();
    panel.propagateSizeChangeUp();
    panel.propagateSizeChangeDown();
  }

  protected void handleBorderChange(ChangeablePanel panel)
  {
    expireCache(panel);
    panel.setBorderChanged(true);
    handleInsetChange(panel);
  }

  protected void handleInsetChange(ChangeablePanel panel)
  {
    expireCache(panel);
    panel.propagateSizeChangeDown();
    panel.markAsNeedingLayout();
    panel.clearCache();
  }

  protected void handleFontChange(ChangeablePanel panel)
  {
    expireCache(panel);
    panel.setSizeChangePending(true);
    panel.getTextAccessor().markAsNeedingLayout();
    panel.markAsNeedingLayout();
    panel.propagateSizeChangeUp();
  }
}
