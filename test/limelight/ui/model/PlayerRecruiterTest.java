//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.builtin.BuiltinBeacon;
import limelight.io.FakeFileSystem;
import limelight.java.JavaCastingDirector;
import limelight.model.FakeProduction;
import limelight.model.PlayerRecruiter;
import limelight.model.api.CastingDirector;
import limelight.model.api.FakeCastingDirector;
import limelight.model.api.FakePropProxy;
import limelight.util.Util;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.*;

public class PlayerRecruiterTest
{
  private PropPanel panel;
  private PlayerRecruiter recruiter;
  private FakeCastingDirector castingDirector;
  private FakeCastingDirector buildingCastingDirector;

  @Before
  public void setUp() throws Exception
  {
    FakeProduction production = new FakeProduction("/path/to/testProduction");
    ScenePanel scene = new ScenePanel(new FakePropProxy(), Util.toMap("name", "theScene", "path", "theScene"));
    scene.setProduction(production);
    panel = new PropPanel(new FakePropProxy());
    scene.add(panel);
    castingDirector = new FakeCastingDirector();
    recruiter = new PlayerRecruiter();
    buildingCastingDirector = new FakeCastingDirector();
    recruiter.setBuiltinCastingDirector(buildingCastingDirector);
    FakeFileSystem.installed();
  }

  @Test
  public void recruitingFromTheScene() throws Exception
  {
    castingDirector.recruitablePath = "/path/to/testProduction/theScene/players";

    String player = recruiter.recruit(panel, "blah", castingDirector);

    assertEquals("theScene/blah", player);
    assertEquals(1, castingDirector.castings.size());
    final List<String> casts = castingDirector.castings.get(panel.getProxy());
    assertEquals(1, casts.size());
    assertEquals("/path/to/testProduction/theScene/players/blah", casts.get(0));
  }

  @Test
  public void recruitingFromTheProduction() throws Exception
  {
    castingDirector.recruitablePath = "/path/to/testProduction/players";

    String player = recruiter.recruit(panel, "blah", castingDirector);

    assertEquals("blah", player);
    assertEquals(1, castingDirector.castings.size());
    final List<String> casts = castingDirector.castings.get(panel.getProxy());
    assertEquals(1, casts.size());
    assertEquals("/path/to/testProduction/players/blah", casts.get(0));
  }

  @Test
  public void builtinCastingDirector() throws Exception
  {
    recruiter = new PlayerRecruiter();
    final CastingDirector director = recruiter.getBuiltinCastingDirector();
    assertNotNull(director);
    assertEquals(JavaCastingDirector.class, director.getClass());
    assertEquals(ClassLoader.getSystemClassLoader(), ((JavaCastingDirector)director).getClassLoader());
  }

  @Test
  public void recruitBuiltinPlayers() throws Exception
  {
    castingDirector.recruitablePath = "none";
    buildingCastingDirector.recruitablePath = BuiltinBeacon.getBuiltinPlayersPath();

    String player = recruiter.recruit(panel, "blah", castingDirector);

    assertEquals("limelight:blah", player);
    assertEquals(1, buildingCastingDirector.castings.size());
    final List<String> casts = buildingCastingDirector.castings.get(panel.getProxy());
    assertEquals(1, casts.size());
    assertEquals(BuiltinBeacon.getBuiltinPlayersPath() + "/blah", casts.get(0));
  }
}
