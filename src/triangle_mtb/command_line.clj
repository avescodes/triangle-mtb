(ns triangle-mtb.command-line
  (:require [triangle-mtb.trail-status :as mtb]
            [triangle-mtb.datomic :as trails])
  (:gen-class :main true))

(defn -main
  "Triangle MTB Command Line Tool"
  [& args]
  (println "Updating trail statuses...")
  (trails/update-trail-status (mtb/current-status))
  (println "Updated trail statuses.")
  (System/exit 0))
