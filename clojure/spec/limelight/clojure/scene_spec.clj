;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.scene-spec
  (:use
    [speclj.core]
    [limelight.clojure.core :exclude (scene production)]
    [limelight.clojure.spec-helper]
    [limelight.clojure.scene]
    [limelight.clojure.production :only (new-production)])
  (:import
    [limelight.clojure.scene Scene]
    [limelight.clojure.casting PlayerRecruiter]))

(describe "Scene"

  (with scene (new-scene {:name "Bill"}))

  (it "has limelight lineage"
    (should (isa? Scene limelight.model.api.SceneProxy)))

  (it "can be created"
    (should-not= nil @(._peer @scene))
    (should= @(._peer @scene) (.getPeer @scene))
    (should= limelight.ui.model.ScenePanel (type @(._peer @scene)))
    (should= PlayerRecruiter (type @(._player-recruiter @scene)))
    (should= @(._player-recruiter @scene) (.getPlayerRecruiter @(._peer @scene))))

  (it "has a helper namespace"
    (should-not= nil (._helper-ns @scene))
    (should= true (.startsWith (.getName (.getName (._helper-ns @scene))) "limelight.dynamic.scene.helper-")))

  (it "has a props namespace"
    (should-not= nil (._props-ns @scene))
    (should= true (.startsWith (.getName (.getName (._props-ns @scene))) "limelight.dynamic.scene.props-")))

  (context "with production"
    (with peer-production (limelight.model.FakeProduction. "Mock"))
    (with production (new-production @peer-production))
    (with fs (limelight.io.FakeFileSystem/installed))
    (with scene (new-scene {:name "bill" :path "bill" :production @production}))

    (it "loads the helper on creation"
      (.createTextFile @fs "/Mock/bill/helper.clj" "(defn foo [] :foo)")
      (let [foo (ns-resolve (._helper-ns @scene) 'foo)]
        (should-not= nil foo)
        (should= :foo (foo))))

    (it "the helper uses production helper"
      (.createTextFile @fs "/Mock/helper.clj" "(defn foo [] :foo)")
      (.createTextFile @fs "/Mock/bill/helper.clj" "(defn bar [] [(foo) :bar])")
      (let [bar (ns-resolve (._helper-ns @scene) 'bar)]
        (should= [:foo :bar] (bar))))

    (it "helper is usable by scene player"
      (.createTextFile @fs "/Mock/bill/helper.clj" "(defn foo [] :foo)")
      (.createTextFile @fs "/Mock/bill/players/bill.clj" "(on-scene-opened [e] (def *message* (foo)))")
      (.cast (.recruitPlayer @(._player-recruiter @scene) "bill" "/Mock/bill/players") (peer @scene))
      (.dispatch (limelight.ui.events.panel.SceneOpenedEvent.) (peer @scene))
      (should= :foo @(ns-resolve (get @(._cast @(._player-recruiter @scene)) "bill") '*message*)))

    (it "prop ns is used for prop building"
      (.createTextFile @fs "/Mock/bill/helper.clj" "(defn foo [] :foo)")
      (build @scene "[(foo) {:text (str *ns*)}]")
      (.illuminate (peer @scene))
      (should= 1 (count (children @scene)))
      (should= "foo" (name (first (children @scene))))
      (should= (str (._props-ns @scene)) (text (first (children @scene)))))

    )
  )

(run-specs)