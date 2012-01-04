(ns limelight.clojure.specs.spec-helper-spec
  (:use
    [speclj.core]
    [limelight.clojure.specs.spec-helper]
    [limelight.clojure.core :as ll :exclude (production stage scene)]))

(describe "spec-helper"

  (context "with clojure calc production"

    (with-limelight :production "productions/calc" :scene "default_scene" :stage "default")

    (it "loads the production"
      (should= "calc" (name @production)))

    (it "loads the stage"
      (should= "default" (name @stage)))

    (it "loads the scene"
      (should= "default_scene" (name @scene)))

    (it "the scene is on the stage"
      (should= @scene (ll/scene @stage))
      (should= @stage (ll/stage @scene)))

    (it "it all belongs to the production"
      (should= @production (ll/production @stage))
      (should= @production (ll/production @scene)))
    )

  (context "with calc on unspecified stage"
    (with-limelight :production "productions/calc" :scene "default_scene")

    (it "loads the scene on the default stage"
      (should= (default-stage (theater @production)) @stage)
      (should= @scene (ll/scene @stage))
      (should= @stage (ll/stage @scene)))
    )

  (context "unspecified production"
    (with-limelight :scene "blah")

    (it "loads the production from the current directory"
      (should= "." (path @production))
      (should= "" (name @production))
      (should= "blah" (name @scene)))
    )

  (context "with extra props as string"
    (with-limelight :production "productions/calc" :scene "default_scene" :props "[:one {:id \"one\"} [:two]]")

    (it "the scene has the regular props"
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
    (with-limelight :production "productions/calc" :scene "default_scene" :props '[:one {:id "one"} [:two]])

    (it "the scene has the extra props"
      (let [one (find-by-id @scene "one")]
        (should-not= nil one)
        (should= "one" (name one))
        (should= 1 (count (children one)))
        (should= "two" (name (first (children one))))))
    )

  (context "with ghost scene"
    (with-limelight :production "productions/calc" :scene-path "ghost" :props [:one {:id "one"} [:two]])

    (it "has the specified path"
      (should= "productions/calc/ghost" (path @scene)))

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
    (with-limelight :production "productions/calc" :scene-path "ghost" :scene-name "casper")

    (it "has the specified name"
      (should= "casper" (name @scene)))
    )

  (context "extra props installing props"
    (with-limelight :production "productions/calc" :scene-path "ghost" :props '(install "default_scene/props.clj"))

    (it "the scene has the installed props"
      (should-not= nil (find-by-id @scene "lcd"))
      (should= "()" (text (find-by-id @scene "lcd")))
      (should= 17 (count (find-by-name @scene "calc-button"))))
    )

  (context "extra props using prop-params"
    (with-limelight :production "productions/calc" :scene-path "ghost" :props '[(:foo *context*)] :prop-params {:foo "FOO"})

    (it "uses the params"
      (should= "FOO" (name (first (children @scene))))))

  (context "prop-params are used by full scene too"
    ;TODO
    )

  (context "ghost scenes use players"
    (with-limelight :production "productions/calc" :scene-path "default_scene" :props '[:calc-button])

    (it "extra prop has player"
      (let [prop (first (children @scene))]
        (should= "calc-button" (name prop))
        (should= ["calc-button"] (map name (players prop)))))
    )
  )
