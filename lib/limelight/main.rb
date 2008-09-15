#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/commands'

module Limelight

  # Used to open and manage Limelight Productions.
  #
  # Productions may have a single scene or multiple scenes.
  #
  # Single-Scene Production Directory Structure:
  #
  #   - calculator
  #   | - stages.rb
  #   | - props.rb
  #   | - styles.rb
  #   | - players
  #     | - <player_name>.rb
  #     | - *
  #
  # In a Single-Scene production, the scene name and production name are the same.  As seen above, both names are 'calculator'.
  # Inside the scene there are three files and one directory, all of which are options.
  #
  # == stages.rb
  # This file uses a DSL to configure the stages that will be used in the production.
  # See Limelight::StagesBuilder
  #
  # == props.rb
  # This file defines the props contained in the scene
  # See Limelight::PropBuilder
  #
  # == styles.rb
  # This file defines the styles used in the scene
  # See Limelight::StylesBuilder
  #
  # == players
  # A directory containing all the players used in the scene.  Players are modules that are included by Prop objects.
  # If you have a Prop named 'wall', then you may optionaly have a file named 'wall.rb' in the players directory.
  # Inside 'wall.rb' you would define a module named 'Wall'.  All behavior defined in the Wall modules will automatically be included
  # in every prop named 'wall'.
  #
  # Multiple-Scene Production Directory Structure:
  #
  #   - sandbox
  #   | - stages.rb
  #   | - styles.rb
  #   | - fader
  #     | - props.rb
  #     | - styles.rb
  #     | - players
  #       | - <player_name>.rb
  #   | - floater
  #     | - props.rb
  #     | - styles.rb
  #     | - players
  #       | - <player_name>.rb
  #
  # In a Multiple-Scene production, the production acquires that name of the root directory.  In this case the production is named 'sandbox'.
  # Each directory inside the root directory is a scene.  This production has two scenes named 'fader' and 'floater'.  Each scene is structured
  # the same as in a Single-Scene production with the exception of the 'stages.rb' file.  This file is specific to the production.  The production
  # may contain a 'styles.rb' which contains styles used by multiple scenes.  Other files may exist in the directory structure and they will not
  # conflict with Limelight.
  #
  # See Limelight::Commands
  #
  class Main

    class << self

      # Executes behavior of limelight command.
      #
      def run(args)
        command_name = args.shift
        command = Commands::COMMANDS[command_name]
        if command
          command.new.run(args)
        else
          usage
        end
      end

      # Prints the usage of the limelight command.
      #
      def usage
        puts "Usage: limelight <command> [options] [params]"
        puts "commands:"
        Commands::COMMANDS.keys.sort.each do |key|
          command = Commands::COMMANDS[key]
          puts "\t#{key}\t\t#{command.description}"
        end
      end
    end


  end


end
