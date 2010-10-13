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


//    it "should convert into camel case" do
//    "class_name".camalized.should == "ClassName"
//    "once_upon_a_time".camalized.should == "OnceUponATime"
//    "AbC_eFg_hiJ".camalized.should == "AbcEfgHij"
//    "with spaces".camalized.should == "WithSpaces"
//  end
//
//  it "should convert into camel case" do
//    "class_name".camalized(:lower).should == "className"
//    "once_upon_a_time".camalized(:lower).should == "onceUponATime"
//    "AbC_eFg_hiJ".camalized(:lower).should == "abcEfgHij"
//    "with spaces".camalized(:lower).should == "withSpaces"
//  end
//
//  it "should underscore a name" do
//    "ClassName".underscored.should == "class_name"
//    "OneTwoThree".underscored.should == "one_two_three"
//    "One".underscored.should == "one"
//  end
  
}
