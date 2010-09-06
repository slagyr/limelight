#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/player'
require 'limelight/builtin/players'

module Limelight

  # The CastingDirector is responsible for finding Players for Props within a Production.
  # Each Producer has an instance of a CastingDirector
  #
  # Users of Limelight need not be concerned with this class.
  #
  class CastingDirector

    def initialize(loader)
      @loader = loader
      @known_players = {}
    end

    def fill_cast(prop)
      raise LimelightException.new("Cannot cast a Prop without a Scene.") if prop.scene.nil?
      cast_default_player(prop)
      cast_additional_players(prop)
    end

    private ###############################################

    def cast_default_player(prop)
      return if prop.name.nil? || prop.name.empty?
      cast_player(prop, prop.name)
    end

    def cast_additional_players(prop)
      return if prop.players.nil? || prop.players.empty?
      player_names = prop.players.split(/[ ,]/)
      player_names.each do |player_name|
        cast_player(prop, player_name)
      end
    end

    def cast_player(prop, player_name)
      recruiter = Recruiter.new(prop, player_name, @loader)
      Limelight::Player.cast(recruiter.player, prop) if recruiter.player_exists?
    end
  end

  class Recruiter

    def initialize(prop, player_name, loader)
      @prop = prop
      @cast = prop.scene.cast
      @player_name = player_name
      @module_name = player_name.camalized
      @loader = loader
    end

    def player
      return @cast.const_get(@module_name)
    end

    def player_already_exists?
      return @cast.const_defined?(@module_name)
    end

    def player_exists?
      recruit_player if !player_already_exists?
      return @cast.const_get(@module_name) != :does_not_exist
    end

    def recruit_player
      recruit_custom_player
      recruit_builtin_player if !player_already_exists?
      @cast.const_set(@module_name, :does_not_exist) if !player_already_exists?
    end

    def recruit_custom_player
      player_filename = locate_player
      load_player(player_filename) if player_filename
    end

    def recruit_builtin_player
      begin
        return if !Limelight::Builtin::Players.const_defined?(@module_name)
        @cast.const_set(@module_name, Limelight::Builtin::Players.const_get(@module_name))
      rescue NameError
      end
    end

    private ###############################################

    def locate_player
      player_filename = File.join(@prop.scene.path.to_s, "players", "#{@player_name}.rb")
      if !@loader.exists?(player_filename)
        if @prop.scene.production && @prop.scene.path != @prop.scene.production.path
          player_filename = File.join(@prop.scene.production.path.to_s, "players", "#{@player_name}.rb")
          return nil if !@loader.exists?(player_filename)
        else
          return nil
        end
      end

      return player_filename
    end

    def load_player(player_filename)
      src = IO.read(@loader.path_to(player_filename))

      player = Player.new
      player.module_eval(src, player_filename)
      @cast.const_set(@module_name, player)
    end

  end


end