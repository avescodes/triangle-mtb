(ns triangle-mtb.datomic
  (:use [datomic.api :only [q db] :as d])
  (:require [clojure.java.io :as io]))

(def uri "datomic:mem://triangle-mtb")

(d/create-database uri)

(def conn (d/connect uri))

(defn loadResource [filename]
  (datomic.Util/readAll (io/reader (io/resource filename))))
(def trails-schema-tx (loadResource "trails.dtm"))
@(d/transact conn trails-schema-tx)

(def triangle-mtb-time-format (java.text.SimpleDateFormat. "yy/MM/dd hh:mm a"))
(defn current-year [] (.. java.util.Calendar (getInstance) (get java.util.Calendar/YEAR)))
(defn time-string->inst [time-string]
  (let [string-with-year (str (current-year) "/" time-string)]
    (.parse triangle-mtb-time-format string-with-year)))

(defn trail-status->entity-tx [[name open last-updated]]
  {
   :db/id (d/tempid :db.part/user)
   :trail/name name
   :trail/open? (= :open open)
   :trail/last-updated (time-string->inst last-updated)
   })

(defn update-trail-status [trail-statuses]
  @(d/transact conn (map trail-status->entity-tx trail-statuses)))
