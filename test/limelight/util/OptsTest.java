//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.util;

import org.junit.Test;

import java.util.HashMap;
import static org.junit.Assert.*;

public class OptsTest
{
  private Opts loadStarterMap()
  {
    HashMap<Object, Object> starter = new HashMap<Object, Object>();
    starter.put("string", "string");
    starter.put(42, "integer");
    starter.put(true, "boolean");
    starter.put(3.14, "double");

    return new Opts(starter);
  }

  @Test
  public void keysAreConvertedToStrings() throws Exception
  {
    Opts map = loadStarterMap();

    assertEquals("string", map.get("string"));
    assertEquals("integer", map.get("42"));
    assertEquals("boolean", map.get("true"));
    assertEquals("double", map.get("3.14"));
  }
  
  @Test
  public void canAccessValuesUsingNonStrings() throws Exception
  {
    Opts map = loadStarterMap();

    assertEquals("string", map.get("string"));
    assertEquals("integer", map.get(42));
    assertEquals("boolean", map.get(true));
    assertEquals("double", map.get(3.14));
  }
  
  @Test
  public void noCrashingWhenGettingNull() throws Exception
  {
    Opts map = loadStarterMap();

    assertEquals(null, map.get(null));
  }
  
  @Test
  public void hasKeysAsStrings() throws Exception
  {
    Opts map = loadStarterMap();

    assertEquals(true, map.containsKey("string"));
    assertEquals(true, map.containsKey("42"));
    assertEquals(true, map.containsKey("true"));
    assertEquals(true, map.containsKey("3.14"));
  }

  @Test
  public void hasKeysAsNonStrings() throws Exception
  {
    Opts map = loadStarterMap();

    assertEquals(true, map.containsKey("string"));
    assertEquals(true, map.containsKey(42));
    assertEquals(true, map.containsKey(true));
    assertEquals(true, map.containsKey(3.14));
    assertEquals(false, map.containsKey(null));
  }
  
  @Test
  public void removingAllKeysAsStrings() throws Exception
  {
    Opts map = loadStarterMap();

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
    Opts map = loadStarterMap();

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

    Opts map = new Opts(starter);

    assertEquals("foo value", map.get("foo"));
    assertEquals("foo value", map.get(new FakeKeyword("foo")));
    assertEquals(true, map.containsKey(new FakeKeyword("foo")));
    assertEquals("foo value", map.remove(new FakeKeyword("foo")));
    assertEquals(0, map.size());
  }

  @Test
  public void creatingNewOpts() throws Exception
  {
    Opts map = Opts.with();
    assertEquals(0, map.size());

    map = Opts.with("one", 1);
    assertEquals(1, map.size());
    assertEquals(1, map.get("one"));

    map = Opts.with("one", 1, "two", 2);
    assertEquals(2, map.size());
    assertEquals(1, map.get("one"));
    assertEquals(2, map.get("two"));
  }

  @Test
  public void wrongNumberOfParamsForCreation() throws Exception
  {
    try
    {
      Opts.with("one");
      fail("Should throw exception");
    }
    catch(RuntimeException e)
    {
      assertEquals("Opts.with must be called with an even number of parameters", e.getMessage());
    }
  }

  @Test
  public void mergingOptions() throws Exception
  {
    Opts left = Opts.with("one", 1, "two", 2);
    Opts right = Opts.with("two", "2", "three", 3);

    Opts merged = left.merge(right);
    assertEquals(1, merged.get("one"));
    assertEquals("2", merged.get("two"));
    assertEquals(3, merged.get("three"));

    merged = right.merge(left);
    assertEquals(1, merged.get("one"));
    assertEquals(2, merged.get("two"));
    assertEquals(3, merged.get("three"));
  }

  @Test
  public void mergingNewValues() throws Exception
  {
    Opts left = Opts.with("one", 1, "two", 2);

    Opts merged = left.merge("two", "2", "three", 3);
    assertEquals(1, merged.get("one"));
    assertEquals("2", merged.get("two"));
    assertEquals(3, merged.get("three"));
  }
}
