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

  (it "has a namespace"
    (should-not= nil (._ns @scene))
    (should= true (.startsWith (.getName (.getName (._ns @scene))) "limelight.dynamic.scene-")))

  (context "with production"
    (with peer-production (limelight.model.FakeProduction. "Mock"))
    (with production (new-production @peer-production))
    (with fs (limelight.io.FakeFileSystem/installed))
    (with scene (new-scene {:name "bill" :path "bill" :production @production}))

    (it "loads the scene ns with players on creation"
      (.createTextFile @fs "/Mock/bill/players/bill.clj" "(defn foo [] :foo)")
      (let [foo (ns-resolve (._ns @scene) 'bill/foo)]
        (should-not= nil foo)
        (should= :foo (foo))))

    (it "the scene ns uses production player"
      (.createTextFile @fs "/Mock/production.clj" "(defn foo [] :foo)")
      (.createTextFile @fs "/Mock/bill/players/bill.clj" "(defn bar [] [(production/foo) :bar])")
      (.illuminate @production)
      (let [bar (ns-resolve (._ns @scene) 'bill/bar)]
        (should= [:foo :bar] (bar))))

    (it "player nses are usable by other players"
      (.createTextFile @fs "/Mock/bill/players/bill.clj" "(defn foo [] :foo)")
      (.createTextFile @fs "/Mock/bill/players/tom.clj" "(on-scene-opened [e] (def *message* (bill/foo)))")
      (.cast (.recruitPlayer @(._player-recruiter @scene) "tom" "/Mock/bill/players") (peer @scene))
      (.dispatch (limelight.ui.events.panel.SceneOpenedEvent.) (peer @scene))
      (should= :foo @(ns-resolve (._ns (get @(._cast @(._player-recruiter @scene)) "tom")) '*message*)))

    (it "scene ns is used for prop building"
      (.createTextFile @fs "/Mock/bill/players/bill.clj" "(defn foo [] :foo)")
      (build @scene "[(bill/foo) {:text (str *ns*)}]")
      (.illuminate (peer @scene))
      (should= 1 (count (children @scene)))
      (should= "foo" (name (first (children @scene))))
      (should= (str (._ns @scene)) (text (first (children @scene)))))

    (it "scene ns is used for style building"
      (.createTextFile @fs "/Mock/bill/players/bill.clj" "(defn foo [] :blue)")
      (.createTextFile @fs "/Mock/bill/styles.clj" "[:one :background-color (bill/foo)]")
      (let [styles (.loadStyles @production (peer @scene) {})
            style (.get styles "one")]
        (should= "#0000ffff" (.getBackgroundColor style))))
    )
  )

(run-specs)