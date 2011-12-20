//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringUtilTest
{
  @Test
  public void join() throws Exception
  {
    String result = StringUtil.join(",", "one", "two", "three");
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
    assertEquals("Class Name", StringUtil.titleCase("Class Name"));
    assertEquals("Class Name", StringUtil.titleCase("class_name"));
    assertEquals("Once Upon A Time", StringUtil.titleCase("once_upon_a_time"));
    assertEquals("Once Upon A Time", StringUtil.titleCase("once-upon-a-time"));
    assertEquals("Ab C E Fg Hi J", StringUtil.titleCase("AbC-eFg-hiJ"));
    assertEquals("Ab C E Fg Hi J", StringUtil.titleCase("AbC_eFg_hiJ"));
    assertEquals("With Spaces", StringUtil.titleCase("with spaces"));
    assertEquals("Some Title", StringUtil.titleCase("Some Title"));
    assertEquals("Some Title", StringUtil.titleCase("SomeTitle"));
  }

  @Test
  public void camalizeStrings() throws Exception
  {
    assertEquals("defaultSceneName", StringUtil.camelCase("defaultSceneName"));
    assertEquals("setDefaultSceneName", StringUtil.camelCase("set defaultSceneName"));
    assertEquals("className", StringUtil.camelCase("class_name"));
    assertEquals("onceUponATime", StringUtil.camelCase("once_upon_a_time"));
    assertEquals("withSpaces", StringUtil.camelCase("with spaces"));
    assertEquals("withDash", StringUtil.camelCase("with-dash"));
    assertEquals("startingCapital", StringUtil.camelCase("starting Capital"));
  }

  @Test
  public void capitalCamalizeStrings() throws Exception
  {
    assertEquals("DefaultSceneName", StringUtil.capitalCamelCase("DefaultSceneName"));
    assertEquals("DefaultSceneName", StringUtil.capitalCamelCase("defaultSceneName"));
    assertEquals("ClassName", StringUtil.capitalCamelCase("class_name"));
    assertEquals("OnceUponATime", StringUtil.capitalCamelCase("once_upon_a_time"));
    assertEquals("WithSpaces", StringUtil.capitalCamelCase("with spaces"));
    assertEquals("WithDash", StringUtil.capitalCamelCase("with-dash"));
  }

  @Test
  public void underscore() throws Exception
  {
    assertEquals("class_name", StringUtil.snakeCase("class_name"));
    assertEquals("class_name", StringUtil.snakeCase("ClassName"));
    assertEquals("one_two_three", StringUtil.snakeCase("OneTwoThree"));
    assertEquals("one", StringUtil.snakeCase("One"));
    assertEquals("one_two_three", StringUtil.snakeCase("one-two-three"));
    assertEquals("one_two_three", StringUtil.snakeCase("one two three"));
  }

  @Test
  public void spearcase() throws Exception
  {
    assertEquals("class-name", StringUtil.spearCase("class_name"));
    assertEquals("class-name", StringUtil.spearCase("ClassName"));
    assertEquals("one-two-three", StringUtil.spearCase("OneTwoThree"));
    assertEquals("one", StringUtil.spearCase("One"));
    assertEquals("one-two-three", StringUtil.spearCase("one_two-three"));
    assertEquals("one-two-three", StringUtil.spearCase("one two three"));
  }
}
