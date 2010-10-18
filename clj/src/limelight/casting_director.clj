(ns limelight.casting-director)

(deftype CastingDiretor []
  limelight.model.api.CastingDirector
  (castPlayer [this prop player-name] nil))

