(ns triangle-mtb.command-line
  (:require [triangle-mtb.trail-status :as mtb])
  (:require [triangle-mtb.datomic :as trails])
  (:gen-class :main true))

(defn -main
  "Triangle MTB Command Line Tool"
  [& args]
  (trails/update-trail-status (mtb/current-status)))