(ns music-player-backend.routes.track
  (:require [music-player-backend.track :as track]
            [music-player-backend.server :as server]
            [music-player-backend.json :as json]
            [music-player-backend.db-mg :as db]))

(defn route
  "Route to handle track requests"
  [req]
  (let [user (db/get-user (:username (:user-data req)))
        data (assoc (json/json-from-request req) :user_id (:_id (:user-data req)))]

    (println data)

    (if user
      (case (:request-method req)
        :post (track/add data)
        :put (track/edit data)
        :delete (track/delete data)
        (server/respond "Unknown request" :status 405))

      (server/respond "User not found" :status 404))))
