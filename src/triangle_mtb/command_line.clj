(ns triangle-mtb.command-line
  (:require [clojure.java.io :as io])
  (:gen-class :main true))

(defn -main
  "Triangle MTB Command Line Tool"
  [& args]
  (println args))