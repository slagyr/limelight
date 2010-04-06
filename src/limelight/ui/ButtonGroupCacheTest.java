//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;

import junit.framework.TestCase;

public class ButtonGroupCacheTest extends TestCase
{
  private ButtonGroupCache cache;

  public void setUp() throws Exception
  {
    cache = new ButtonGroupCache();
  }

  public void testCreatingNewGroups() throws Exception
  {
    RadioButtonGroup group1 = cache.get("group1");
    assertNotNull(group1);

    RadioButtonGroup group2 = cache.get("group2");
    assertNotNull(group2);

    assertEquals(true, group1 != group2);
  }
  
  public void testGroupsAreUniqueByName() throws Exception
  {
    RadioButtonGroup group1 = cache.get("group1");
    assertSame(group1, cache.get("group1"));

    RadioButtonGroup group2 = cache.get("group2");
    assertSame(group2, cache.get("group2"));
  }
}
