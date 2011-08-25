//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.util;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class OptionsTest
{
  public static class OptionsSubject
  {
    private boolean primitiveBoolean;
    private Boolean boxedBoolean;
    private byte primitiveByte;
    private Byte boxedByte;
    private short primitiveShort;
    private Short boxedShort;
    private int primitiveInt;
    private Integer boxedInt;
    private long primitiveLong;
    private Long boxedLong;
    private float primitiveFloat;
    private Float boxedFloat;
    private double primitiveDouble;
    private Double boxedDouble;
    private String string;
    private Collection<Object> collection;

    public void setPrimitiveBoolean(boolean primitiveBoolean)
    {
      this.primitiveBoolean = primitiveBoolean;
    }

    public void setBoxedBoolean(Boolean boxedBoolean)
    {
      this.boxedBoolean = boxedBoolean;
    }

    public void setPrimitiveByte(byte primitiveByte)
    {
      this.primitiveByte = primitiveByte;
    }

    public void setBoxedByte(Byte boxedByte)
    {
      this.boxedByte = boxedByte;
    }

    public void setPrimitiveShort(short primitiveShort)
    {
      this.primitiveShort = primitiveShort;
    }

    public void setBoxedShort(Short boxedShort)
    {
      this.boxedShort = boxedShort;
    }

    public void setPrimitiveInt(int primitiveInt)
    {
      this.primitiveInt = primitiveInt;
    }

    public void setBoxedInt(Integer boxedInt)
    {
      this.boxedInt = boxedInt;
    }

    public void setPrimitiveLong(long primitiveLong)
    {
      this.primitiveLong = primitiveLong;
    }

    public void setBoxedLong(Long boxedLong)
    {
      this.boxedLong = boxedLong;
    }

    public void setPrimitiveFloat(float primitiveFloat)
    {
      this.primitiveFloat = primitiveFloat;
    }

    public void setBoxedFloat(Float boxedFloat)
    {
      this.boxedFloat = boxedFloat;
    }

    public void setPrimitiveDouble(double primitiveDouble)
    {
      this.primitiveDouble = primitiveDouble;
    }

    public void setBoxedDouble(Double boxedDouble)
    {
      this.boxedDouble = boxedDouble;
    }

    public void setString(String string)
    {
      this.string = string;
    }

    public void setCollection(Collection<Object> collection)
    {
      this.collection = collection;
    }
  }

  private OptionsSubject subject;

  @Before
  public void setUp() throws Exception
  {
    subject = new OptionsSubject();
  }

  @Test
  public void canUseSetters() throws Exception
  {
    Options.apply(subject, Util.toMap("primitiveBoolean", true, "primitiveInt", 42, "primitiveDouble", 3.14, "string", "blah"));

    assertEquals(true, subject.primitiveBoolean);
    assertEquals(42, subject.primitiveInt);
    assertEquals(3.14, subject.primitiveDouble, 0.01);
    assertEquals("blah", subject.string);
  }

  @Test
  public void doesntCrashWhenOptionsCantBeMapped() throws Exception
  {
    final Map<String, Object> options = Util.toMap("foo", "bar");
    try
    {
      Options.apply(subject, options);
      assertEquals("bar", options.get("foo"));
    }
    catch(Exception e)
    {
      fail("Should not throw exception");
    }
  }

  @Test
  public void usedValuesAreRemovedFromTheMap() throws Exception
  {
    final Map<String, Object> options = Util.toMap("primitiveBoolean", true, "primitiveInt", 42, "primitiveDouble", 3.14, "string", "blah");
    Options.apply(subject, options);

    assertEquals(0, options.size());
  }

  @Test
  public void incompatibleValuesDoesntCauseCrash() throws Exception
  {
    final Map<String, Object> options = Util.toMap("primitiveBoolean", 1);

    Options.apply(subject, options);

    assertEquals(1, options.size());
  }

  @Test
  public void canCoerceStringToBooleanTrue() throws Exception
  {
    Options.apply(subject, Util.toMap("primitiveBoolean", "true"));
    assertEquals(true, subject.primitiveBoolean);

    subject.primitiveBoolean = false;
    Options.apply(subject, Util.toMap("boxedBoolean", "TRUE"));
    assertEquals(true, subject.boxedBoolean);

    subject.primitiveBoolean = false;
    Options.apply(subject, Util.toMap("boxedBoolean", "on"));
    assertEquals(true, subject.boxedBoolean);

    subject.primitiveBoolean = false;
    Options.apply(subject, Util.toMap("primitiveBoolean", "ON"));
    assertEquals(true, subject.primitiveBoolean);
  }
  
  @Test
  public void canCoerceStringToBooleanFalse() throws Exception
  {
    subject.primitiveBoolean = true;
    Options.apply(subject, Util.toMap("primitiveBoolean", "false"));
    assertEquals(false, subject.primitiveBoolean);

    subject.primitiveBoolean = true;
    Options.apply(subject, Util.toMap("boxedBoolean", "FALSE"));
    assertEquals(false, subject.boxedBoolean);

    subject.primitiveBoolean = true;
    Options.apply(subject, Util.toMap("boxedBoolean", "off"));
    assertEquals(false, subject.boxedBoolean);

    subject.primitiveBoolean = true;
    Options.apply(subject, Util.toMap("primitiveBoolean", "OFF"));
    assertEquals(false, subject.primitiveBoolean);
  }

  @Test
  public void canCoerceStringToBytes() throws Exception
  {
    Options.apply(subject, Util.toMap("primitiveByte", "42"));
    assertEquals(42, subject.primitiveByte);

    Options.apply(subject, Util.toMap("boxedByte", "42"));
    assertEquals(new Byte((byte)42), subject.boxedByte);
  }

  @Test
  public void canCoerceStringToShorts() throws Exception
  {
    Options.apply(subject, Util.toMap("primitiveShort", "42"));
    assertEquals(42, subject.primitiveShort);

    Options.apply(subject, Util.toMap("boxedShort", "42"));
    assertEquals(new Short((short)42), subject.boxedShort);
  }

  @Test
  public void canCoerceStringToInts() throws Exception
  { 
    Options.apply(subject, Util.toMap("primitiveInt", "42"));
    assertEquals(42, subject.primitiveInt);

    Options.apply(subject, Util.toMap("boxedInt", "42"));
    assertEquals(new Integer(42), subject.boxedInt);
  }

  @Test
  public void canCoerceStringsToLongs() throws Exception
  {
    Options.apply(subject, Util.toMap("primitiveLong", "42"));
    assertEquals(42, subject.primitiveLong);

    Options.apply(subject, Util.toMap("boxedLong", "42"));
    assertEquals(new Long(42), subject.boxedLong);
  }

  @Test
  public void canCoerceStringToFloats() throws Exception
  {
    Options.apply(subject, Util.toMap("primitiveFloat", "3.14"));
    assertEquals(3.14, subject.primitiveFloat, 0.01);

    Options.apply(subject, Util.toMap("boxedFloat", "3.14"));
    assertEquals(new Float(3.14), subject.boxedFloat, 0.01);
  }
  
  @Test
  public void canCoerceStringToDouble() throws Exception
  {
    Options.apply(subject, Util.toMap("primitiveDouble", "3.14"));
    assertEquals(3.14, subject.primitiveDouble, 0.01);

    Options.apply(subject, Util.toMap("boxedDouble", "3.14"));
    assertEquals(3.14, subject.boxedDouble, 0.01);
  }

  @Test
  public void canCoerceStringToCollection() throws Exception
  {
    Options.apply(subject, Util.toMap("collection", "one, two, three"));
    List<Object> collection = new ArrayList<Object>(subject.collection);
    assertEquals("one", collection.get(0));
    assertEquals("two", collection.get(1));
    assertEquals("three", collection.get(2));
  }
}
