#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/commands/command'

module Limelight

  # Limelight::Main is used, when installed as a gem, to work with Limelight production.  It provides
  # a handful of utilities to create, bundle, and develop with Limelight.
  #
  # For example, running the following command will generate a new Limelight production for you.
  #
  #   jruby -S limelight create production <production name>
  #
  # Assuming you used "sandbox" as the name or your production, you'd end up with the following directory structure
  # generated for you.
  #
  #   - sandbox
  #   | - stages.rb
  #   | - styles.rb
  #   | - production.rb
  #   | - default_scene
  #     | - props.rb
  #     | - styles.rb
  #     | - players
  #       | - <player_name>.rb
  #   | - spec
  #     | - spec_helper.rb
  #     | - default_scene
  #       | - default_scene_spec.rb
  #
  # In this case, you've just created a production called "Sandbox".  By convention, the name of the production matches the name of the root directory.
  # Notice that there are 3 files and 2 directories.  Let's start by talking about the files.
  #
  # == stages.rb
  # This file uses a DSL to configure the stages that will be used in the production.
  # See Limelight::StagesBuilder
  #
  # == styles.rb
  # This file defines production level styles. Each scene may have their own styles but styles defined here will be avaiable to all scenes.
  # See Limelight::StylesBuilder
  #
  # == production.rb
  # This file defines a module names Production where you can defined hooks and behavior for your production.
  # See Limelight::Production
  #
  # For the most part, each directory inside the root directory is a scene.  This production has one scene named "default_scene" (this is the default name).
  # Each scene starts out containing 2 file and a directory.  Let's look at those..
  #
  # == props.rb
  # This file defines the structure of your scene.  Scenes are composed of Props.  In this file you use the Prop DSL to
  # specify all the components of your scene.
  # See Limelight::PropBuilder
  # See Limelight::Prop
  #
  # == styles.rb
  # Similar to the styles.rb file located in the root directory, this file contains definitions of styles.  However,
  # all styles defined here are only available to the containgin scene.  Styles define the look and feel of your scenes.
  # See Limelight::StylesBuilder
  # See Limelight::Style
  #
  # == players
  # A directory containing all the players used in the scene.  Players are modules that are included by Prop objects.
  # If you have a Prop named 'wall', then you may optionaly have a file named 'wall.rb' in the players directory.
  # Inside 'wall.rb' you would define a module named 'Wall'.  All behavior defined in the Wall modules will automatically be included
  # in every prop named 'wall'.
  #
  # So there's a brief overview for you.  Besure to check out the Limelight Docs production.  You can find it by installing Limelight and starting it up.
  #
  # For more info on available commands: 
  # See Limelight::Commands
  #
  class Main
    
    # Instantiates a new instance of Main to process the command
    #
    def self.run(args)
      new().run(args)
    end

    # Executes behavior of limelight command.
    #
    def run(args)
      command_name = args.shift
      command = Commands[command_name]
      if command
        command.new.run(args)
      else
        usage
      end
    end

    # Prints the usage of the limelight command.
    #
    def usage
      Commands["help"].new.run([])
      exit -1
    end

    private ###############################################

  end


end
