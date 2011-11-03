//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.commands;

import limelight.Context;
import limelight.model.FakeProduction;
import limelight.model.api.MockStudio;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class OpenCommandTest
{
  private OpenCommand command;
  private MockStudio studio;

  @Before
  public void setUp() throws Exception
  {
    command = new OpenCommand();
    command.setShouldBoot(false);
    studio = MockStudio.installed();
    Context.instance().studio = studio;
  }

  @Test
  public void defaultProduction() throws Exception
  {
    command.execute();

    assertEquals(command.defaultProduction(), studio.openedProduction);
  }

  @Test
  public void openSpecifiedProduction() throws Exception
  {
    command.execute("/path/to/blah");

    assertEquals("/path/to/blah", studio.openedProduction);
  }

  @Test
  public void hasRiggerSwitch() throws Exception
  {
    Map<String,String> args = command.getArguments().parse("/path");
    assertEquals(null, args.get("rigger"));

    args = command.getArguments().parse("-r", "/path");
    assertEquals("on", args.get("rigger"));

    args = command.getArguments().parse("--rigger", "/path");
    assertEquals("on", args.get("rigger"));
  }

  @Test
  public void riggerGetOpened() throws Exception
  {
    final FakeProduction production = new FakeProduction();
    studio.stubUtilitiesProduction(production);

    command.execute("-r", "/path");

    assertEquals("/path", studio.openedProduction);
    assertEquals("openRigger", production.lastMethodCalled);
  }
}
