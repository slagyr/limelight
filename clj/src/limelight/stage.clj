(ns limelight.stage)

(deftype Stage []
  limelight.model.api.StageProxy
  (applyOptions [this options] nil))

