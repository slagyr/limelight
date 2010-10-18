(ns limelight.prop)

(deftype Prop []
  limelight.model.api.PropProxy
  (applyOptions [this options] nil))