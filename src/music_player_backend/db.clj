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
         [[:id :integer :primary :key :autoincrement]
          [:username :text :unique]
          [:password :text]]]

        [:playlists
         [[:id :integer :primary :key :autoincrement]
          [:name :text]
          [:userid :int]]]

        [:songs
         [[:id :integer :primary :key :autoincrement]
          [:name :text]
          [:url :text]]]

        [:connections
         [[:id :integer :primary :key :autoincrement]
          [:songid :integer]
          [:playlistid :integer]
          [:tracknumber :integer]]]]))

(defn crud-delete
  [table data]
  (case table
    :songs (jdbc/delete! db :connections ["songid = ?" data])
    :playlists (jdbc/delete! db :connections ["playlistid = ?" data]))
  (jdbc/delete! db table ["id = ?" data]))

(defn crud
  ([table action data]
   (case action
     ;; :read [:id id]
     :read (jdbc/query db (format "SELECT * FROM %s WHERE %s = %d"
                                  (name table)
                                  (name (first (:match data)))
                                  (second (:match data))))

     ;; :insert data
     :insert (jdbc/insert! db table data)

     ;; :delete {:new data :id id}
     :update (jdbc/update! db table (:new data)
                           ["id = ?" (:id data)])

     ;; :delete id
     :delete (crud-delete table data)))

  ([table action]
   (case action
     ;; (crud table :read)
     :read (jdbc/query db (str "SELECT * FROM " (name table))))))

(defn id-less
  [x]
  (dissoc x :id))

(defn in?
  [x l]
  (some (partial = x) l))

(defn update-playlist
  [data]
  (let [playlist-id (:id data)
        previous-songs (->> (crud :connections :read)
                            (map id-less))
        new-songs (->> (:new data)
                       (map id-less)
                       (map #(assoc % :playlistid playlist-id)))]

    (println "Previous:" previous-songs)
    (println "New:" new-songs "yay")
    (println "Change:" (filter #(not (in? % previous-songs))
                               new-songs))
    (println "NChange:" (filter #(not (in? % new-songs))
                                previous-songs))

    (->> (filter #(not (in? % previous-songs))
                 new-songs)
         (map #(assoc % :playlistid playlist-id))
         (map (partial crud :connections :insert))
         doall)

    (->> (filter #(not (in? % new-songs))
                 previous-songs)
         (map #(jdbc/delete! db :connections
                             ["songid = ? AND playlistid = ? AND tracknumber = ?"
                              (:songid %) playlist-id (:tracknumber %)]))
         doall)))

;; create playlist: needs user id

(defn get-userid [username]
  (jdbc/query db ["SELECT id FROM users
                   WHERE username = ?" username]))

(defn create-db []
  (jdbc/execute! db ["PRAGMA foreign_keys = ON;"])
  (doall (map (partial jdbc/db-do-commands db)
              (user-table))))

(defn set-db []
  (println "Setting DB...")
  (create-db))
