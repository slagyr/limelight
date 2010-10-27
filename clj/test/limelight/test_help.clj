(ns limelight.test-help
   (:use [lazytest.context :only (fn-context)]))

(def *fs* (atom nil))

(def fake-fs
  (fn-context (fn [] (swap! *fs* (fn [_] (limelight.io.FakeFileSystem/installed))))
              (fn [] )))