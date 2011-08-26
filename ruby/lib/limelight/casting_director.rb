#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

require 'limelight/player'
require 'limelight/builtin/players'

module Limelight

  # The CastingDirector is responsible for finding Players for Props within a Production.
  # Each Producer has an instance of a CastingDirector
  #
  # Users of Limelight need not be concerned with this class.
  #
  class CastingDirector

    attr_reader :cast

    def initialize()
      @cast = Module.new
      @fs = Java::limelight.Context.fs
    end

    def has_player(player_name, players_path)
      return @cast.const_defined?(player_name.camalized) || @fs.exists("#{@fs.join(players_path, player_name)}.rb")
    end

    alias :hasPlayer :has_player

    def cast_player(prop, player_name, players_path)
      player = load_player(player_name, players_path)
      Limelight::Player.cast(player, prop)
    end

    alias :castPlayer :cast_player

    private ###############################################

    def load_player(player_name, players_path)
      module_name = player_name.camalized
      if @cast.const_defined?(module_name)
        return @cast.const_get(module_name)
      else
        player_filename = "#{@fs.join(players_path, player_name)}.rb"
        src = @fs.read_text_file(player_filename)

        player = Player.new
        player.module_eval(src, player_filename)
        @cast.const_set(module_name, player)
        return player
      end
    end

  end

end