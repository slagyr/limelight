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
    assertEquals("className", StringUtil.camalize("class_name"));
    assertEquals("onceUponATime", StringUtil.camalize("once_upon_a_time"));
    assertEquals("abcEfgHij", StringUtil.camalize("AbC_eFg_hiJ"));
    assertEquals("withSpaces", StringUtil.camalize("with spaces"));
    assertEquals("withDash", StringUtil.camalize("with-dash"));
  }

  @Test
  public void capitalCamalizeStrings() throws Exception
  {
    assertEquals("ClassName", StringUtil.capitalCamalize("class_name"));
    assertEquals("OnceUponATime", StringUtil.capitalCamalize("once_upon_a_time"));
    assertEquals("AbcEfgHij", StringUtil.capitalCamalize("AbC_eFg_hiJ"));
    assertEquals("WithSpaces", StringUtil.capitalCamalize("with spaces"));
    assertEquals("WithDash", StringUtil.capitalCamalize("with-dash"));
  }
  
//
//  it "should underscore a name" do
//    "ClassName".underscored.should == "class_name"
//    "OneTwoThree".underscored.should == "one_two_three"
//    "One".underscored.should == "one"
//  end

  
}
