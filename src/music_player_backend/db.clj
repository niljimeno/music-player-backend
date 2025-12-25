(ns music-player-backend.db
  (:require [clojure.java.jdbc :as jbdc])
  (:gen-class))

(def db
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "db/database.db"})

(defn user-table []
  (jbdc/create-table-ddl :users
                         [[:id :int :primary :key]
                          [:username :text]
                          [:password :text]]
                         {:conditional? true}))

(defn create-db []
  (jbdc/execute! db ["PRAGMA foreign_keys = ON;"])
  (try (jbdc/db-do-commands db
                            (user-table))
       (catch Exception e
         (println (.getMessage e)))))

(defn set-db []
  (println "Setting DB...")
  (create-db))
