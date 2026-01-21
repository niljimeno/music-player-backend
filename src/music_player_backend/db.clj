(ns music-player-backend.db
  (:require [clojure.java.jdbc :as jbdc])
  (:gen-class))

(def db
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "db/database.db"})

(defn user-table []
  (println "Start user-table")
  (map #(apply jbdc/create-table-ddl
               (conj % {:conditional? true}))
       [[:users
         [[:id :int :primary :key]
          [:username :text :unique]
          [:password :text]]]

        [:playlists
         [[:id :int :primary :key]
          [:userId :int]]]

        [:songs
         [[:id :int :primary :key]
          [:url :text]]]

        [:connections
         [[:id :int :primary :key]
          [:songId :int]
          [:playlistId :int]
          [:trackNumber :int]]]]))

(defn create-db []
  (jbdc/execute! db ["PRAGMA foreign_keys = ON;"])
  (doall (map (partial jbdc/db-do-commands db)
              (user-table))))

(defn set-db []
  (println "Setting DB...")
  (create-db))
