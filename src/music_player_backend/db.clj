(ns music-player-backend.db
  (:require [clojure.java.jdbc :as jdbc])
  (:gen-class))

(def db
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "db/database.db"})

(defn user-table []
  (println "Start user-table")
  (map #(apply jdbc/create-table-ddl
               (conj % {:conditional? true}))
       [[:users
         [[:id :int :primary :key]
          [:username :text :unique]
          [:password :text]]]

        [:playlists
         [[:id :int :primary :key]
          [:name :text :unique]
          [:userId :int]]]

        [:songs
         [[:id :int :primary :key]
          [:name :text]
          [:url :text]]]

        [:connections
         [[:id :int :primary :key]
          [:songId :int]
          [:playlistId :int]
          [:trackNumber :int]]]]))

(defn crud
  ([table action data]
   (case action
     :insert (jdbc/insert! db table data)
     :update (jdbc/update! db table (:new data) ["id = ?" (:id data)])
     :delete (crud table (:id data))))
  ([table id] ;; deletion case
   (case table
     :songs (jdbc/delete! db :connections ["songId = ?" id])
     :playlists (jdbc/delete! db :connections ["playlistId = ?" id]))
   (jdbc/delete! db table ["id = ?" id])))

;; create playlist: needs user id

(defn create-db []
  (jdbc/execute! db ["PRAGMA foreign_keys = ON;"])
  (doall (map (partial jdbc/db-do-commands db)
              (user-table))))

(defn set-db []
  (println "Setting DB...")
  (create-db))
