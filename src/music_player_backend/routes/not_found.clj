(ns music-player-backend.routes.not-found
  (:require [music-player-backend.server :as server]))

(defn route [req]
  (server/respond "Route not found!" :status 404))
