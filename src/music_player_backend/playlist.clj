(ns music-player-backend.playlist
  (:require [music-player-backend.server :as server]
            [music-player-backend.db-mg :as db]))

(defn add [data]
  (try
    (let [playlist-data (db/insert-data "playlists" data)
          id (str (:_id playlist-data))]

      (println "Playlist added - _id:" id)
      (server/respond id :status 201))

    (catch Exception e
      (println "Error adding playlist:" (.getMessage e))
      (server/respond "Error adding playlist" :status 400))))

(defn edit [data]
  (try
    (let [playlist (db/get-playlist (:user_id data)
                                    (:id data))]

      (if (nil? playlist)
        (server/respond "Playlist does not exist" :status 404)

        (let [result (db/update-data "playlists" (:id data) {:name (:name data)})]
          (println "Updated playlist" playlist "->" (:name data) "-" result)
          (server/respond "Playlist updated" :status 200))))

    (catch Exception e
      (println "Error updating playlist:" (.getMessage e))
      (server/respond "Error updating playlist" :status 400))))

(defn delete [data]
  (try
    (let [playlist (db/get-playlist (:user_id data)
                                    (:id data))]
      (if (nil? playlist)
        (server/respond "Playlist does not exists" :status 404)

        (let [result (db/remove-data "playlists" (:id data))]
          (println "Removed playlist" playlist "-" result)
          (server/respond "Playlist removed" :status 200))))

    (catch Exception e
      (println "Error removing playlist:" (.getMessage e))
      (server/respond "Error removing playlist" :status 400))))

(defn update-list [data]
  (try
    (let [playlist (db/get-playlist (:user_id data)
                                    (:id data))]

      (if (nil? playlist)
        (server/respond "Playlist does not exist" :status 404)

        (let [result (db/update-data "playlists" (:id data) {:tracks (:track-ids data)})]

          (println "Updated playlist" playlist "-" result)
          (server/respond "Updated playlist tracks" :status 200))))

    (catch Exception e
      (println "Error updating playlist tracks:" (.getMessage e))
      (server/respond "Error updating playlist tracks" :status 400))))
