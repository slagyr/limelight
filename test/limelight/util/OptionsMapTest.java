//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.util;

import org.junit.Test;

import java.util.HashMap;
import static org.junit.Assert.*;

public class OptionsMapTest
{
  private OptionsMap loadStarterMap()
  {
    HashMap<Object, Object> starter = new HashMap<Object, Object>();
    starter.put("string", "string");
    starter.put(42, "integer");
    starter.put(true, "boolean");
    starter.put(3.14, "double");

    OptionsMap map = new OptionsMap(starter);
    return map;
  }

  @Test
  public void keysAreConvertedToStrings() throws Exception
  {
    OptionsMap map = loadStarterMap();

    assertEquals("string", map.get("string"));
    assertEquals("integer", map.get("42"));
    assertEquals("boolean", map.get("true"));
    assertEquals("double", map.get("3.14"));
  }
  
  @Test
  public void canAccessValuesUsingNonStrings() throws Exception
  {
    OptionsMap map = loadStarterMap();

    assertEquals("string", map.get("string"));
    assertEquals("integer", map.get(42));
    assertEquals("boolean", map.get(true));
    assertEquals("double", map.get(3.14));
  }
  
  @Test
  public void noCrashingWhenGettingNull() throws Exception
  {
    OptionsMap map = loadStarterMap();

    assertEquals(null, map.get(null));
  }
  
  @Test
  public void hasKeysAsStrings() throws Exception
  {
    OptionsMap map = loadStarterMap();

    assertEquals(true, map.containsKey("string"));
    assertEquals(true, map.containsKey("42"));
    assertEquals(true, map.containsKey("true"));
    assertEquals(true, map.containsKey("3.14"));
  }

  @Test
  public void hasKeysAsNonStrings() throws Exception
  {
    OptionsMap map = loadStarterMap();

    assertEquals(true, map.containsKey("string"));
    assertEquals(true, map.containsKey(42));
    assertEquals(true, map.containsKey(true));
    assertEquals(true, map.containsKey(3.14));
    assertEquals(false, map.containsKey(null));
  }
  
  @Test
  public void removingAllKeysAsStrings() throws Exception
  {
    OptionsMap map = loadStarterMap();

    assertEquals("string", map.remove("string"));
    assertEquals("integer", map.remove("42"));
    assertEquals("boolean", map.remove("true"));
    assertEquals("double", map.remove("3.14"));
    assertEquals(null, map.remove(null));

    assertEquals(0, map.size());
  }
    
  @Test
  public void removingAllKeysAsNonStrings() throws Exception
  {
    OptionsMap map = loadStarterMap();

    assertEquals("string", map.remove("string"));
    assertEquals("integer", map.remove(42));
    assertEquals("boolean", map.remove(true));
    assertEquals("double", map.remove(3.14));
    assertEquals(null, map.remove(null));

    assertEquals(0, map.size());
  }

  @Test
  public void handledClojureKeywords() throws Exception
  {
    HashMap<Object, Object> starter = new HashMap<Object, Object>();
    starter.put(new FakeKeyword("foo"), "foo value");

    OptionsMap map = new OptionsMap(starter);

    assertEquals("foo value", map.get("foo"));
    assertEquals("foo value", map.get(new FakeKeyword("foo")));
    assertEquals(true, map.containsKey(new FakeKeyword("foo")));
    assertEquals("foo value", map.remove(new FakeKeyword("foo")));
    assertEquals(0, map.size());
  }
}
