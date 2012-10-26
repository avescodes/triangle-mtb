(ns triangle-mtb.datomic
  (:use [datomic.api :only [q db] :as d])
  (:require [tools.schema]
            [tools.time]
            [tools.connection-logging]))

(def uri (or (System/getenv "DATOMIC_URL") "datomic:mem://triangle-mtb"))

(defn conn []
  (tools.connection-logging/log-ip-addresses)
  (d/create-database uri)
  (d/connect uri))

(tools.schema/load-schema (conn))

(defn trail-status->entity-tx [[name open last-updated]]
  {
   :db/id (d/tempid :db.part/user)
   :trail/name name
   :trail/open? (= :open open)
   :trail/last-updated (tools.time/time-string->inst last-updated)
   })

(defn update-trail-status [trail-statuses]
  @(d/transact (conn) (map trail-status->entity-tx trail-statuses)))

(defn- qes-and-touch
  "Returns the entities returned by a query, assuming that
   all :find results are entity ids."
  [query db & args]
  (->> (apply q query db args)
       (mapv (fn [items] 
               (mapv (partial d/entity db) items)))
       flatten
       (mapv d/touch)))

(defn all [] (qes-and-touch '[:find ?e :where [?e :trail/name _]] (db (conn))))
(defn open [] (qes-and-touch '[:find ?e :where [?e :trail/open? true]] (db (conn))))
(defn closed [] (qes-and-touch '[:find ?e :where [?e :trail/open? false]] (db (conn))))
