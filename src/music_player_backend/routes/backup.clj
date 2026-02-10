(ns music-player-backend.routes.backup
  (:require [music-player-backend.server :as server]))

(defn route
  "Route to get all data related to a user"
  [req]
  (println (:user-data req)))
