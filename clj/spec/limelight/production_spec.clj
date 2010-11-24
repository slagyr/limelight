(ns limelight.production-spec
  (:use
    [speclj.core]
    [limelight.production])
  (:import
    [limelight.production Production]
    [limelight.casting-director CastingDirector]))

(describe "Production"

  (it "has limelight lineage"
    (should (isa? Production limelight.model.api.ProductionProxy)))

  (it "can be contructed"
    (let [production (Production. :peer :theater)]
      (should= :peer (.peer production))
      (should= :theater (.theater production))))
  )

(describe "Fully constructed production"
  (with loader (limelight.util.MockResourceLoader.))
  (with peer-production (limelight.model.FakeProduction. "Mock" @loader))
  (with production (new-production @peer-production))

  (it "connects with the peer production"
    (should= @peer-production (.peer @production))
    (should= @production (.getProxy (.peer @production))))


  (it "creates a theater"
    (should= limelight.theater.Theater (type @(.theater @production)))
    (should= (.getTheater @peer-production) (.peer @(.theater @production)))
    (should= @production (.production @(.theater @production))))

  (it "can load stages"
    (set! (.readTextResult @loader) "(stage \"One\" {})")
    (.loadStages @production)
    (should= 1 (count (.getStages (.getTheater @peer-production))))
    (should= "stages.clj" (.pathToReadText @loader)))
  )