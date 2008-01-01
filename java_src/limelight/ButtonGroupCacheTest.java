package limelight;

import junit.framework.TestCase;

import javax.swing.*;

public class ButtonGroupCacheTest extends TestCase
{
  private ButtonGroupCache cache;

  public void setUp() throws Exception
  {
    cache = new ButtonGroupCache();
  }

  public void testCreatingNewGroups() throws Exception
  {
    ButtonGroup group1 = cache.get("group1");
    assertNotNull(group1);

    ButtonGroup group2 = cache.get("group2");
    assertNotNull(group2);

    assertTrue(group1 != group2);
  }
  
  public void testGroupsAreUniqueByName() throws Exception
  {
    ButtonGroup group1 = cache.get("group1");
    assertSame(group1, cache.get("group1"));

    ButtonGroup group2 = cache.get("group2");
    assertSame(group2, cache.get("group2"));
  }
}
