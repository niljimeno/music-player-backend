(ns music-player-backend.router
  (:require [music-player-backend.routes.song :as route-song]
            [music-player-backend.routes.login :as route-login]
            [music-player-backend.routes.search :as route-search]
            [music-player-backend.routes.register :as route-register]
            [music-player-backend.routes.not-found :as route-not-found]))

(defn handler
  "Handle http requests and send them to the correct route function"
  [req]
  (let [uri (:uri req)]
    ((case uri
       "/song" route-song/route
       "/login" route-login/route
       "/search" route-search/route
       "/register" route-register/route
       route-not-found/route)
     req)))
