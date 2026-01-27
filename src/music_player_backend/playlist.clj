(ns music-player-backend.playlist
  (:require [music-player-backend.server :as server]
            [music-player-backend.db :as db]
            [clojure.java.jdbc :as jdbc]))

(defn add-playlist [data]
  (try
    (let [result (db/crud :playlists
                          :insert data)]

      (println "Inserted playlist:" result)
      (server/respond result :status 201))

    (catch Exception e
      (println "An error ocurred:" (.getMessage e))
      (server/respond "Error inserting playlist" :status 400))))

(defn update-playlist [data]
  (try
    (let [playlist (jdbc/query db/db ["SELECT name FROM playlists
                                       WHERE userid = ? AND id = ?" (:userid data) (:id data)])]

      (if (empty? playlist)
        (server/respond "Playlist does not exist" :status 404)

        (let [result (db/crud :playlists
                              :update data)]
          (println "Updated playlist:" result)
          (server/respond result :status 200))))

    (catch Exception e
      (println "An error ocurred:" (.getMessage e))
      (server/respond "Error updating playlist" :status 400))))

(defn delete-playlist [data]
  (try
    (let [playlist-id (jdbc/query db/db ["SELECT name FROM playlists
                                          WHERE userid = ? AND id = ?" (:userid data) (:id data)])]
      (if (empty? playlist-id)
        (server/respond "Playlist does not exists" :status 404)

        (let [result (db/crud-delete :playlists (:id data))]
          (println "Removed playlist" playlist-id ": " result)
          (server/respond result :status 200))))

    (catch Exception e
      (println "An error ocurred:" (.getMessage e))
      (server/respond "Error deleting playlist" :status 400))))