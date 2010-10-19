(ns limelight.casting-director)

(deftype CastingDirector [loader]
  limelight.model.api.CastingDirector
  (castPlayer [this prop player-name] nil))

