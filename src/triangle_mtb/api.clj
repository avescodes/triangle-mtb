(ns triangle-mtb.api
  (:use [compojure.core :only [defroutes GET]])
  (:require [ring.adapter.jetty :as ring]
            [cheshire.core :as json]
            [triangle-mtb.datomic :as trails]))

(defn json-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json"}
   :body (json/generate-string data)})

(defroutes application
  (GET "/all.json" [] (json-response (trails/all))))

(defn start [port]
  (ring/run-jetty #'application {:port (or port 8080) :join? false}))

(defn -main []
  (let [port (Integer. (System/getenv "PORT"))]
        (start port)))