//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.commands;

import limelight.About;
import limelight.Context;
import limelight.io.FakeFileSystem;
import limelight.io.Templater;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class CreateCommandTest
{
  private CreateCommand command;
  private FakeFileSystem fs;
  private ByteArrayOutputStream output;

  private static class NullTemplaterLogger extends Templater.TemplaterLogger
  {
    @Override
    public void say(String message)
    {
    }
  }

  @Before
  public void setUp() throws Exception
  {
    System.setProperty("limelight.home", "home");
    Context.removeInstance();

    output = new ByteArrayOutputStream();
    Command.setOutput(new PrintStream(output));
    fs = new FakeFileSystem();

    command = new CreateCommand();
    command.setTemplaterLoger(new NullTemplaterLogger());
    command.setFileSystem(fs);
  }

  private void setupSceneTemplates()
  {
    fs.createTextFile("home/lib/limelight/templates/sources/scene/props.rb.template", "props content !-SCENE_TITLE-!");
    fs.createTextFile("home/lib/limelight/templates/sources/scene/styles.rb.template", "styles content !-SCENE_NAME-!");
    fs.createTextFile("home/lib/limelight/templates/sources/scene_spec/scene_spec.rb.template", "scene_spec content");
  }

  private void setupProductionTemplates()
  {
    fs.createTextFile("home/lib/limelight/templates/sources/production/production.rb.template", "production content !-PRODUCTION_NAME-! !-CURRENT_VERSION-!");
    fs.createTextFile("home/lib/limelight/templates/sources/production/stages.rb.template", "stages content !-DEFAULT_SCENE_NAME-!");
    fs.createTextFile("home/lib/limelight/templates/sources/production/styles.rb.template", "styles content");
    fs.createTextFile("home/lib/limelight/templates/sources/production/spec/spec_helper.rb.template", "spec_helper content");
  }

  private void setupProjectTemplates()
  {
    fs.createTextFile("home/lib/limelight/templates/sources/features/step_definitions/limelight_steps.rb.template", "limelight_steps content");
    fs.createTextFile("home/lib/limelight/templates/sources/features/support/env.rb.template", "env content");
    fs.createTextFile("home/lib/limelight/templates/sources/project/Rakefile.template", "Rakefile content !-LLP_NAME-!");
    fs.createTextFile("home/lib/limelight/templates/sources/project/spec_helper.template", "project spec_helper content");
  }

  @Test
  public void argumentsAreNotNull() throws Exception
  {
    assertNotNull(command.getArguments());
  }

  @Test
  public void badComponent() throws Exception
  {
    command.execute("blah", "blah");

    assertEquals("ERROR! I don't know how to create 'blah's", output.toString().trim());
  }

  @Test
  public void creatingScene() throws Exception
  {
    setupSceneTemplates();

    command.execute("scene", "howdy");

    assertEquals("props content Howdy", fs.readTextFile("howdy/props.rb"));
    assertEquals("styles content howdy", fs.readTextFile("howdy/styles.rb"));
    assertEquals(true, fs.exists("howdy/players"));
    assertEquals("scene_spec content", fs.readTextFile("spec/howdy/howdy_spec.rb"));
  }

  @Test
  public void creatingProduction() throws Exception
  {
    setupProductionTemplates();
    setupSceneTemplates();

    command.execute("production", "hello");

    assertEquals("production content Hello " + About.version.toString(), fs.readTextFile("hello/production.rb"));
    assertEquals("stages content default_scene", fs.readTextFile("hello/stages.rb"));
    assertEquals("styles content", fs.readTextFile("hello/styles.rb"));
    assertEquals("spec_helper content", fs.readTextFile("hello/spec/spec_helper.rb"));
    assertEquals("props content Default Scene", fs.readTextFile("hello/default_scene/props.rb"));
    assertEquals("styles content default_scene", fs.readTextFile("hello/default_scene/styles.rb"));
    assertEquals(true, fs.exists("hello/default_scene/players"));
    assertEquals("scene_spec content", fs.readTextFile("hello/spec/default_scene/default_scene_spec.rb"));
  }

  @Test
  public void creatingProductionUsingCustomSceneName() throws Exception
  {
    setupProductionTemplates();
    setupSceneTemplates();

    command.execute("production", "hello", "--scene-name=baby");

    assertEquals("props content Baby", fs.readTextFile("hello/baby/props.rb"));
    assertEquals("styles content baby", fs.readTextFile("hello/baby/styles.rb"));
    assertEquals(true, fs.exists("hello/baby/players"));
    assertEquals("scene_spec content", fs.readTextFile("hello/spec/baby/baby_spec.rb"));
  }

  @Test
  public void creatingSceneWithCustomProductionPath() throws Exception
  {
    fs.createDirectory("myProd");
    setupSceneTemplates();

    command.execute("scene", "howdy", "--production-path=myProd");

    assertEquals("props content Howdy", fs.readTextFile("myProd/howdy/props.rb"));
    assertEquals("styles content howdy", fs.readTextFile("myProd/howdy/styles.rb"));
    assertEquals(true, fs.exists("myProd/howdy/players"));
    assertEquals("scene_spec content", fs.readTextFile("myProd/spec/howdy/howdy_spec.rb"));
  }

  @Test
  public void creatingProductionUsingCustomTestPath() throws Exception
  {
    setupProductionTemplates();
    setupSceneTemplates();

    command.execute("production", "hello", "--test-path=tests");

    assertEquals("scene_spec content", fs.readTextFile("hello/tests/default_scene/default_scene_spec.rb"));
    assertEquals("spec_helper content", fs.readTextFile("hello/tests/spec_helper.rb"));
  }

  @Test
  public void creatingProject() throws Exception
  {
    setupProductionTemplates();
    setupSceneTemplates();
    setupProjectTemplates();

    command.execute("project", "hello");

    assertEquals("limelight_steps content", fs.readTextFile("hello/features/step_definitions/limelight_steps.rb"));
    assertEquals("env content", fs.readTextFile("hello/features/support/env.rb"));
    assertEquals("Rakefile content hello", fs.readTextFile("hello/Rakefile"));
    assertEquals("spec_helper content", fs.readTextFile("hello/spec/spec_helper.rb"));
    assertEquals("scene_spec content", fs.readTextFile("hello/spec/default_scene/default_scene_spec.rb"));
    assertEquals("production content Hello " + About.version.toString(), fs.readTextFile("hello/production/production.rb"));
    assertEquals("stages content default_scene", fs.readTextFile("hello/production/stages.rb"));
    assertEquals("styles content", fs.readTextFile("hello/production/styles.rb"));
    assertEquals("props content Default Scene", fs.readTextFile("hello/production/default_scene/props.rb"));
    assertEquals("styles content default_scene", fs.readTextFile("hello/production/default_scene/styles.rb"));
    assertEquals(true, fs.exists("hello/production/default_scene/players"));
  }
}
