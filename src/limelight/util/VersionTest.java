//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.util;

import junit.framework.TestCase;

public class VersionTest extends TestCase
{
  private Version version1_2_3;
  private Version copy1_2_3;
  private Version version3_2_1;
  private Version version1_2_4;
  private Version version1_3_3;
  private Version vertion2_2_3;

  public void setUp() throws Exception
  {
    version1_2_3 = new Version("1.2.3");
    copy1_2_3 = new Version("1.2.3");
    version3_2_1 = new Version("3.2.1");
    version1_2_4 = new Version("1.2.4");
    version1_3_3 = new Version("1.3.3");
    vertion2_2_3 = new Version("2.2.3");
  }

  public void testValidParsing() throws Exception
  {
    checkParsedVersion("1.2.3", 1, 2, 3);
    checkParsedVersion("0.0.1", 0, 0, 1);
    checkParsedVersion("9.9.9", 9, 9, 9);
    checkParsedVersion("10.20.30", 10, 20, 30);
  }

  private void checkParsedVersion(String versionString, int major, int minor, int patch)
  {
    Version version = new Version(versionString);
    assertEquals(major, version.getMajor());
    assertEquals(minor, version.getMinor());
    assertEquals(patch, version.getPatch());
  }

  public void testInvlidVersionFormats() throws Exception
  {
    checkInvalidVersion(null, "Could not parse version number: null");
    checkInvalidVersion("", "Could not parse version number: ");
    checkInvalidVersion("...", "Could not parse version number: ...");
    checkInvalidVersion("1", "Could not parse version number: 1");
    checkInvalidVersion("1.2", "Could not parse version number: 1.2");
    checkInvalidVersion("1.2.3.4", "Could not parse version number: 1.2.3.4");
    checkInvalidVersion("a.b.c", "Could not parse version number: a.b.c");
  }

  private void checkInvalidVersion(String value, String errorMessage)
  {
    try
    {
      new Version(value);
      fail("should raise exception: " + value);
    }
    catch(Exception e)
    {
      assertEquals(errorMessage, e.getMessage());
    }
  }

  public void testEquals() throws Exception
  {
    assertEquals(version1_2_3, copy1_2_3);
    assertEquals(copy1_2_3, version1_2_3);
    assertEquals(false, version1_2_3.equals(version3_2_1));
    assertEquals(false, version3_2_1.equals(version1_2_3));
  }

  public void testLessThan() throws Exception
  {
    assertEquals(false, version1_2_3.isLessThan(copy1_2_3));
    assertEquals(true, version1_2_3.isLessThan(version1_2_4));
    assertEquals(true, version1_2_4.isLessThan(version1_3_3));
    assertEquals(true, version1_3_3.isLessThan(vertion2_2_3));
    assertEquals(false, vertion2_2_3.isLessThan(version1_3_3));
    assertEquals(false, version1_3_3.isLessThan(version1_2_4));
    assertEquals(false, version1_2_4.isLessThan(copy1_2_3));
  }
  
  public void testGreaterThan() throws Exception
  {
    assertEquals(false, version1_2_3.isGreaterThan(copy1_2_3));
    assertEquals(false, version1_2_3.isGreaterThan(version1_2_4));
    assertEquals(false, version1_2_4.isGreaterThan(version1_3_3));
    assertEquals(false, version1_3_3.isGreaterThan(vertion2_2_3));
    assertEquals(true, vertion2_2_3.isGreaterThan(version1_3_3));
    assertEquals(true, version1_3_3.isGreaterThan(version1_2_4));
    assertEquals(true, version1_2_4.isGreaterThan(copy1_2_3));
  }
  
  public void testGreaterThanOrEqual() throws Exception
  {
    assertEquals(true, version1_2_3.isGreaterThanOrEqual(copy1_2_3));
    assertEquals(false, version1_2_3.isGreaterThanOrEqual(version1_2_4));
    assertEquals(true, version1_2_4.isGreaterThanOrEqual(copy1_2_3));
  }

  public void testLessThanOrEquals() throws Exception
  {
    assertEquals(true, version1_2_3.isLessThanOrEqual(copy1_2_3));
    assertEquals(true, version1_2_3.isLessThanOrEqual(version1_2_4));
    assertEquals(false, version1_2_4.isLessThanOrEqual(copy1_2_3));
  }
}
