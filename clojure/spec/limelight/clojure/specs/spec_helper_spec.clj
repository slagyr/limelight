(ns limelight.clojure.specs.spec-helper-spec
  (:use
    [speclj.core]
    [limelight.clojure.util :only (->options)]
    [limelight.clojure.specs.spec-helper]
    [limelight.clojure.core :as ll :exclude (production stage scene)]))

(describe "spec-helper"

  (with-all fs (limelight.io.FakeFileSystem.))
  (with-all test-opts {:file-system @fs})
  (before-all
    (.createTextFile @fs "calc/stages.clj" "(stage \"default\" :default-scene-name \"default-scene\")")
    (.createTextFile @fs "calc/face/props.clj"
      "[:lcd {:text \"()\" :id \"lcd\"}]
      (for [label \"123+456-789*c0 /\"]
        [:calc-button {:text (str label)}])
      [:calc-button {:text \"enter\" :width \"100%\"}]")
    (.createTextFile @fs "calc/face/players/calc_button.clj" "(on-mouse-clicked [e] (let [p (prop e)] (text= (find-by-id prop \"lcd\") (text prop))))"))

  (context "with clojure calc production"

    (with-limelight @test-opts :production "calc" :scene "face" :stage "default")

    (it "loads the production"
      (should= "calc" (name @production)))

    (it "loads the stage"
      (should= "default" (name @stage)))

    (it "loads the scene"
      (should= "face" (name @scene)))

    (it "the scene is on the stage"
      (should= @scene (ll/scene @stage))
      (should= @stage (ll/stage @scene)))

    (it "it all belongs to the production"
      (should= @production (ll/production @stage))
      (should= @production (ll/production @scene)))
    )

  (context "with calc on unspecified stage"
    (with-limelight @test-opts :production "calc" :scene "face")

    (it "loads the scene on the default stage"
      (should= (default-stage (theater @production)) @stage)
      (should= @scene (ll/scene @stage))
      (should= @stage (ll/stage @scene)))
    )

  (context "unspecified production"
    (with-limelight @test-opts :scene "blah")

    (it "loads the production from the current directory"
      (should= "." (path @production))
      (should= "" (name @production))
      (should= "blah" (name @scene)))
    )

  (context "with extra props as string"
    (with-limelight @test-opts :production "calc" :scene "face" :props "[:one {:id \"one\"} [:two]]")

    (it "the scene has the regular props"
      (should-not= nil (find-by-id @scene "lcd"))
      (should= "()" (text (find-by-id @scene "lcd")))
      (should= 17 (count (find-by-name @scene "calc-button"))))

    (it "and the scene has the extra props"
      (let [one (find-by-id @scene "one")]
        (should-not= nil one)
        (should= "one" (name one))
        (should= 1 (count (children one)))
        (should= "two" (name (first (children one))))))
    )

  (context "with extra props as data"
    (with-limelight @test-opts :production "calc" :scene "default_scene" :props '[:one {:id "one"} [:two]])

    (it "the scene has the extra props"
      (let [one (find-by-id @scene "one")]
        (should-not= nil one)
        (should= "one" (name one))
        (should= 1 (count (children one)))
        (should= "two" (name (first (children one))))))
    )

  (context "with ghost scene"
    (with-limelight @test-opts :production "calc" :scene-path "ghost" :props [:one {:id "one"} [:two]])

    (it "has the specified path"
      (should= "calc/ghost" (path @scene)))

    (it "has a default name"
      (should= "ghost" (name @scene)))

    (it "has the specified props"
      (should= 1 (count (children @scene)))
      (let [one (find-by-id @scene "one")]
        (should-not= nil one)
        (should= "one" (name one))
        (should= 1 (count (children one)))
        (should= "two" (name (first (children one))))))
    )

  (context "with named ghost scene"
    (with-limelight @test-opts :production "calc" :scene-path "ghost" :scene-name "casper")

    (it "has the specified name"
      (should= "casper" (name @scene)))
    )

  (context "extra props installing props"
    (with-limelight @test-opts :production "calc" :scene-path "ghost" :props '(install "face/props.clj"))

    (it "the scene has the installed props"
      (should-not= nil (find-by-id @scene "lcd"))
      (should= "()" (text (find-by-id @scene "lcd")))
      (should= 17 (count (find-by-name @scene "calc-button"))))
    )

  (context "extra props using prop-params"
    (with-limelight @test-opts :production "calc" :scene-path "ghost" :props '[(:foo *context*)] :prop-params {:foo "FOO"})

    (it "uses the params"
      (should= "FOO" (name (first (children @scene)))))
    )

    (context "prop-params are used by full scene"
      (before-all (.createTextFile @fs "calc/alternate/props.clj" "[:message {:text (:message *context*)}]"))

      (with-limelight @test-opts :production "calc" :scene "alternate" :prop-params {:message "HELLO"})

      (it "prop has parameterized message"
        (should= 1 (count (children @scene)))
        (should= "message" (name (first (children @scene))))
        (should= "HELLO" (text (first (children @scene)))))
      )

    (context "ghost scenes use players"
      (with-limelight @test-opts :production "calc" :scene-path "face" :props '[:calc-button])

      (it "extra prop has player"
        (let [prop (first (children @scene))]
          (should= "calc-button" (name prop))
          (should= ["calc-button"] (map name (players prop)))))
      )
  )

(run-specs)
