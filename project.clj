(defproject triangle-mtb "0.1.0"
  :description "Triangle MTB trail status API"
  :url "http://triangle-mtb.herokuapp.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [enlive "1.0.0"]
                 [com.datomic/datomic-free "0.8.3551"]
                 [compojure "1.1.3"]
                 [ring/ring-jetty-adapter "1.1.6"]
                 [cheshire "4.0.3"]]
  :main triangle-mtb.command-line
  :plugins [[lein-ring "0.7.3"]]
  :ring {:handler triangle-mtb.api}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}})
