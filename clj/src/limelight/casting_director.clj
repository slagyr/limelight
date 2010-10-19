(ns limelight.casting-director)

(deftype CastingDirector []
  limelight.model.api.CastingDirector
  (castPlayer [this prop player-name] nil))

