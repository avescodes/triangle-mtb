(ns triangle-mtb.datomic
  (:use [datomic.api :only [q db] :as d])
  (:use [clj-time.format :as f]))

(def uri "datomic:mem://triangle-mtb")

(d/create-database uri)

(def conn (d/connect uri))

;; Trails
(def schema-tx [
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

(def ^:private mtb-time (f/formatters "MM/dd h:m a"))

(defn- trail-status->entity-tx [name open last-updated]
  {
   :db/id (d/tempid :db.part/user)
   :trail/name name
   :trail/open? (= :open open)
   :trail/last-updated (... last-updated)
   }
)

(defn update-trail-status [trail-statuses]
  @(d/transact conn (map trail-status->entity-tx trail-statuses)))


