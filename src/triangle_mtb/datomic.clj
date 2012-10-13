(ns triangle-mtb.datomic
  (:use [datomic.api :only [q db] :as d])
  (:use [clj-time.format :as f])
  (:require [clj-time.core]))

(def uri "datomic:mem://triangle-mtb")

(d/create-database uri)

(def conn (d/connect uri))

(def trails-schema-tx [
 ;; Trail Name
 {:db/id #db/id [:db.part/db]
  :db/ident :trail/name
  :db/unique :db.unique/identity
  :db/valueType :db.type/string
  :db/cardinality :db.cardinality/one
  :db/index true
  :db.install/_attribute :db.part/db},
 ;; Trail "Open"ness
 {:db/id #db/id [:db.part/db]
  :db/ident :trail/open?
  :db/valueType :db.type/boolean
  :db/cardinality :db.cardinality/one
  :db/index true
  :db.install/_attribute :db.part/db},
 ;; Last Updated
 {:db/id #db/id [:db.part/db]
  :db/ident :trail/last-updated
  :db/valueType :db.type/instant
  :db/cardinality :db.cardinality/one
  :db.install/_attribute :db.part/db}
 ])

@(d/transact conn schema-tx)

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
