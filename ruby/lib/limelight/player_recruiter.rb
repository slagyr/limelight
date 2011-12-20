#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

require 'limelight/player'
require 'limelight/builtin/players'

module Limelight

  # The PlayerRecruiter is responsible for finding Players for Props within a Production.
  # Each scene has a unique instance of a PlayerRecruiter
  #
  # Users of Limelight need not be concerned with this class.
  #
  class PlayerRecruiter

    attr_reader :cast

    def initialize()
      @cast = Module.new
      @fs = Java::limelight.Context.fs
    end

    def can_recruit?(player_name, players_path)
      @cast.const_defined?(player_name.camelized) || @fs.exists(player_filename(player_name, players_path))
    end

    alias :canRecruit :can_recruit?

    def recruit_player(player_name, players_path)
      module_name = player_name.camelized
      if @cast.const_defined?(module_name)
        @cast.const_get(module_name)
      else
        player_filename = player_filename(player_name, players_path)
        src = @fs.read_text_file(player_filename)

        player = Player.new(player_name, player_filename)
        player.module_eval(src, player_filename)
        @cast.const_set(module_name, player)
        player
      end
    end

    alias :recruitPlayer :recruit_player

    private ###############################################


    def player_filename(player_name, players_path)
      "#{@fs.join(players_path, player_name)}.rb"
    end

  end

end