//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringUtilTest
{
  @Test
  public void join() throws Exception
  {
    String result = StringUtil.join(",", new String[]{"one", "two", "three"});
    assertEquals("one,two,three", result);
    result = StringUtil.join(",", "one", "two", "three");
    assertEquals("one,two,three", result);

    result = StringUtil.join(",");
    assertEquals("", result);

    result = StringUtil.join(",", "one");
    assertEquals("one", result);
  }

  @Test
  public void convertingStringsIntoTitles() throws Exception
  {
    assertEquals("Class Name", StringUtil.titleize("Class Name"));
    assertEquals("Class Name", StringUtil.titleize("class_name"));
    assertEquals("Once Upon A Time", StringUtil.titleize("once_upon_a_time"));
    assertEquals("Ab C E Fg Hi J", StringUtil.titleize("AbC_eFg_hiJ"));
    assertEquals("With Spaces", StringUtil.titleize("with spaces"));
    assertEquals("Some Title", StringUtil.titleize("Some Title"));
    assertEquals("Some Title", StringUtil.titleize("SomeTitle"));
  }

  @Test
  public void camalizeStrings() throws Exception
  {
    assertEquals("defaultSceneName", StringUtil.camalize("defaultSceneName"));
    assertEquals("setDefaultSceneName", StringUtil.camalize("set defaultSceneName"));
    assertEquals("className", StringUtil.camalize("class_name"));
    assertEquals("onceUponATime", StringUtil.camalize("once_upon_a_time"));
    assertEquals("withSpaces", StringUtil.camalize("with spaces"));
    assertEquals("withDash", StringUtil.camalize("with-dash"));
  }

  @Test
  public void capitalCamalizeStrings() throws Exception
  {
    assertEquals("DefaultSceneName", StringUtil.capitalCamalize("DefaultSceneName"));
    assertEquals("DefaultSceneName", StringUtil.capitalCamalize("defaultSceneName"));
    assertEquals("ClassName", StringUtil.capitalCamalize("class_name"));
    assertEquals("OnceUponATime", StringUtil.capitalCamalize("once_upon_a_time"));
    assertEquals("WithSpaces", StringUtil.capitalCamalize("with spaces"));
    assertEquals("WithDash", StringUtil.capitalCamalize("with-dash"));
  }

  @Test
  public void underscore() throws Exception
  {
    assertEquals("class_name", StringUtil.underscore("class_name"));
    assertEquals("class_name", StringUtil.underscore("ClassName"));
    assertEquals("one_two_three", StringUtil.underscore("OneTwoThree"));
    assertEquals("one", StringUtil.underscore("One"));
    assertEquals("one_two_three", StringUtil.underscore("one-two-three"));
    assertEquals("one_two_three", StringUtil.underscore("one two three"));
  }
}
