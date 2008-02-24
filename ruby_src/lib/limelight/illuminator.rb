require 'limelight/players'

module Limelight
  
  class Illuminator
    
    def initialize(loader)
      @loader = loader
      @known_players = {}
    end
    
    def illuminate(block)
      apply_default_player(block)
      apply_additional_players(block)
      
      block.children.each do |child|
        illuminate(child)
      end
    end
    
    def apply_default_player(block)
      return if block.class_name.nil? || block.class_name.empty?
      block.include_player(@known_players[block.class_name]) if player_exists?(block.class_name)
    end
    
    def apply_additional_players(block)
      return if block.players.nil? || block.players.empty?
      player_names = block.players.split(/[ ,]/)
      player_names.each do |player_name|
        apply_player(block, player_name)
      end
    end
    
    def apply_player(block, player_name)
      block.include_player(@known_players[player_name]) if player_exists?(player_name)
    end
    
    private ###############################################
    
    def player_exists?(player_name)
      load_player_by_name(player_name) if !@known_players.has_key?(player_name)
      return @known_players[player_name] != :does_not_exist
    end
    
    def load_player_by_name(player_name)
      load_custom_player(player_name)
      load_builtin_player(player_name) if !@known_players.has_key?(player_name)
      @known_players[player_name] = :does_not_exist if !@known_players.has_key?(player_name)
    end
    
    def load_custom_player(player_name)
      player_filename = "players/#{player_name}.rb"
      return if !@loader.exists?(player_filename)
      
      Kernel.load @loader.path_to(player_filename)
      
      module_name = player_name.camalized
      return if !Object.const_defined?(module_name)
      
      mod = Object.const_get(module_name)
      @known_players[player_name] = mod
    end
    
    def load_builtin_player(player_name)
      module_name = player_name.camalized
      return if !Limelight::Players.const_defined?(module_name)
      mod = Limelight::Players.const_get(module_name)
      @known_players[player_name] = mod
    end
    
  end
  
end