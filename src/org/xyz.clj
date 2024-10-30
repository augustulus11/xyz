(ns org.xyz
  (:require [com.biffweb :as biff]
            [org.xyz.email :as email]
            [org.xyz.home :as home]
            [org.xyz.blog :as blog]
            [org.xyz.blerbs :as blerbs]
            [org.xyz.webrings-and-buttons :as webrings-and-buttons]
            [org.xyz.credits :as credits]
            [org.xyz.middleware :as mid]
            [org.xyz.ui :as ui]
            [org.xyz.worker :as worker]
            [org.xyz.schema :as schema]
            [clojure.test :as test]
            [clojure.tools.logging :as log]
            [clojure.tools.namespace.repl :as tn-repl]
            [malli.core :as malc]
            [malli.registry :as malr]
            [nrepl.cmdline :as nrepl-cmd]
            [next.jdbc :as jdbc]
            [honey.sql :as honey]
            [clojure.edn :as edn]
            [clojure.java.io :as io])
  (:gen-class))

(def modules
  [home/module
   blog/module
   blerbs/module
   webrings-and-buttons/module
   credits/module
   schema/module
   worker/module])

(def routes [["" {:middleware [mid/wrap-site-defaults]}
              (keep :routes modules)]
             ["" {:middleware [mid/wrap-api-defaults]}
              (keep :api-routes modules)]])

(def handler (-> (biff/reitit-handler {:routes routes})
                 mid/wrap-base-defaults))

(def static-pages (apply biff/safe-merge (map :static modules)))

(defn generate-assets! [ctx]
  (biff/export-rum static-pages "target/resources/public")
  (biff/delete-old-files {:dir "target/resources/public"
                          :exts [".html"]}))

(defn on-save [ctx]
  (biff/add-libs)
  (biff/eval-files! ctx)
  (generate-assets! ctx)
  (test/run-all-tests #"org.xyz.*-test"))

(def initial-system
  {:biff/modules #'modules
   :biff/merge-context-fn identity
   :biff/handler #'handler
   :biff.beholder/on-save #'on-save
   :biff.middleware/on-error #'ui/on-error})

(comment (defn use-postgres
           "Create a database reference."
           [{:keys [org.xyz/db-config] :as ctx}]
           (assoc ctx :org.xyz/db (jdbc/get-datasource db-config))))

(defn use-postgres [{:keys [biff/secret] :as ctx}]
  (log/info "It's ============== " (secret :example/postgres-url))
  (let [ds (jdbc/get-datasource (secret :example/postgres-url))]
    (comment (jdbc/execute! ds [(slurp (io/resource "migrations.sql"))]))
    (assoc ctx :example/ds ds)))

(defn use-thing
  "Test."
  [ctx]
  (assoc ctx :myval 69))

(defonce system (atom {}))

(def components
  [biff/use-aero-config
   biff/use-queues
   biff/use-jetty
   biff/use-chime
   biff/use-beholder
   use-thing
   use-postgres])

(defn start []
  (let [new-system (reduce (fn [system component]
                             (log/info "Starting: " component)
                             (component system))
                           initial-system
                           components)]
    (reset! system new-system)
    (generate-assets! new-system)
    (log/info "System started.")
    (log/info "Go to" (:biff/base-url new-system))
    new-system))

(defn -main []
  (let [{:keys [biff.nrepl/args]} (start)]
    (apply nrepl-cmd/-main args)))

(defn refresh []
  (doseq [f (:biff/stop @system)]
    (log/info "stopping:" (str f))
    (f))
  (tn-repl/refresh :after `start)
  :done)
