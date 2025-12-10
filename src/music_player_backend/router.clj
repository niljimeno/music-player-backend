(ns music-player-backend.router
  (:require [music-player-backend.routes.song :as route-song]
            [music-player-backend.routes.search :as route-search]
            [music-player-backend.routes.not-found :as route-not-found]
            [music-player-backend.server :as server]))

(defn handler
  "Handle http requests and send them to the correct route function"
  [req]
  (let [uri (:uri req)]
    (cond
     (= uri "/song") (route-song/route req)
     (= uri "/search") (route-search/route req)
     :else (route-not-found/route req))))
