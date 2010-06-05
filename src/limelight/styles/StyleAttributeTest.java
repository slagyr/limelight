//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import limelight.Context;
import limelight.caching.SimpleCache;
import limelight.styles.compiling.IntegerAttributeCompiler;
import limelight.styles.values.SimpleIntegerValue;
import limelight.ui.Panel;
import limelight.ui.model.MockChangeablePanel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;

public class StyleAttributeTest extends Assert
{
  private StyleAttribute attribute;
  private MockChangeablePanel panel;

  @Before
  public void setUp() throws Exception
  {
    panel = new MockChangeablePanel();
    attribute = new StyleAttribute("NAME", new IntegerAttributeCompiler(), new SimpleIntegerValue(50));
    Context.instance().bufferedImageCache = new SimpleCache<Panel, BufferedImage>();
  }

  @Test
  public void testConstruction() throws Exception
  {
    assertEquals("NAME", attribute.name);
    assertEquals(IntegerAttributeCompiler.class, attribute.compiler.getClass());
    assertEquals(50, ((SimpleIntegerValue) attribute.defaultValue).getValue());
  }

  @Test
  public void shouldExpireBufferedImageCacheByDefaultWhenStyleChange() throws Exception
  {
    Context.instance().bufferedImageCache.cache(panel, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));

    attribute.applyChange(panel, new SimpleIntegerValue(25));

    assertEquals(null, Context.instance().bufferedImageCache.retrieve(panel));
  }

  @Test
  public void shouldMarkAsDirtyByDefaultWhenStyleChanged() throws Exception
  {
    assertEquals(false, panel.markedAsDirty);

    attribute.applyChange(panel, new SimpleIntegerValue(25));

    assertEquals(true, panel.markedAsDirty);
  }
}
