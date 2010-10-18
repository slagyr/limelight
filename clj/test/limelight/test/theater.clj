(ns limelight.test.theater
  (:use
    [limelight.theater]
    [lazytest.describe :only (describe testing it with)]
    [lazytest.context :only (fn-context)]))

(describe "Theater lineage"
  (it (some #{limelight.model.api.TheaterProxy} (supers limelight.theater.Theater))))


