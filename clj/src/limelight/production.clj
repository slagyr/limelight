(ns limelight.production)

(deftype Production []
  limelight.model.api.ProductionProxy
  (callMethod [this name args] nil)
  (getCastingDirector [this] nil)
  (getTheater [this] nil))
