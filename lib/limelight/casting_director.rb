#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/players'

module Limelight
  
  class CastingDirector
    
    def initialize(loader)
      @loader = loader
      @known_players = {}
    end
    
    def fill_cast(prop)
      cast_default_player(prop)
      cast_additional_players(prop)
    end
    
    private ###############################################
    
    def cast_default_player(prop)
      return if prop.name.nil? || prop.name.empty?
      prop.include_player(@known_players[prop.name]) if player_exists?(prop, prop.name)
    end
    
    def cast_additional_players(prop)
      return if prop.players.nil? || prop.players.empty?
      player_names = prop.players.split(/[ ,]/)
      player_names.each do |player_name|
        apply_player(prop, player_name)
      end
    end
    
    def apply_player(prop, player_name)
      prop.include_player(@known_players[player_name]) if player_exists?(prop, player_name)
    end
    
    def player_exists?(prop, player_name)
      load_player_by_name(prop, player_name) if !@known_players.has_key?(player_name)
      return @known_players[player_name] != :does_not_exist
    end
    
    def load_player_by_name(prop, player_name)
      load_custom_player(prop, player_name)
      load_builtin_player(player_name) if !@known_players.has_key?(player_name)
      @known_players[player_name] = :does_not_exist if !@known_players.has_key?(player_name)
    end
    
    def load_custom_player(prop, player_name)
      player_filename = File.join([prop.scene.path.to_s, "players", "#{player_name}.rb"])
      return if !@loader.exists?(player_filename)
      
      Kernel.load @loader.path_to(player_filename)
      
      module_name = player_name.camalized
      return if !Object.const_defined?(module_name)
      
      mod = Object.const_get(module_name)
      @known_players[player_name] = mod
    end
    
    def load_builtin_player(player_name)
      begin
        module_name = player_name.camalized
        return if !Limelight::Players.const_defined?(module_name)
        mod = Limelight::Players.const_get(module_name)
        @known_players[player_name] = mod
      rescue NameError
      end
    end
    
  end
  
end