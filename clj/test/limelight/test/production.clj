(ns limelight.test.production
  (:use
    [limelight.production]
    [lazytest.describe :only (describe testing it with)]
    [lazytest.context :only (fn-context)]))

(describe "Production lineage"
  (it (some #{limelight.model.api.ProductionProxy} (supers limelight.production.Production))))


