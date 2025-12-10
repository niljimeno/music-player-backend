(ns example)

;; Ejecuta "clojure example.clj"!

(println "Hello!") ;; Hello!

(println (+ 2 3)) ;; 5


;;;;;;;;;;;;;;;
;; Funciones ;;
;;;;;;;;;;;;;;;

(defn hello-world []
  (println "Hello!"))

(println (hello-world)) ;; Hello!

;; Sin parentesis, la funcion no se ejecuta
(println hello-world) ;; #object


(defn constante []
  5)
(println (constante)) ;; 5

(defn suma [a b]
  (+ a b))

(println (suma 2 3)) ;; 5


(defn suma-con-descripcion [a b]
  "Mamaguevo" ;; no afecta a su funcionamiento
  [a b]
  (+ a b))

(println (suma-con-descripcion 2 3)) ;; 5


;; las funciones devuelven solo el ultimo resultado
(defn muchas-cosas []
  (println "hola")
  (+ 3 3)
  (println "buenas")
  (+ 2 2) ;; <- este
)

(println (muchas-cosas)) ;; 4



;;;;;;;;;;;;;;;
;; Variables ;;
;;;;;;;;;;;;;;;

(def x 10)
(println x) ;; 10

;; let: variables usadas dentro de un rango
(let [x 25]
  (println x)) ;; 25

;; fuera del rango de let:
(println x) ;; 10

;; funcion local (fn), sin definir (defn)
;; ahora mismo no tiene ningun uso
(println ((fn suma [a b] (+ a b)) 2 3)) ;; 5



;;;;;;;;;;;;
;; Tablas ;;
;;;;;;;;;;;;

(def tabla {:x 10 :y 20})

(println tabla) ;; {:x 10 :y 20}
(println (:x tabla)) ;; 10

;; :atom ---> los dos puntos marcan un atomo
;; valor immutable identificado por su nombre (como una llave (key))
;; ejemplos: :atom :x :keys :out


;; argumento opcional
(defn hola
  "No hace falta memorizar esto pero bueno."
  [& {:keys [nombre] :or {nombre "Sin Nombre"}}]
  (println (str "Hola " nombre "!")))

(hola) ;; Hola Sin Nombre!
(hola :nombre "Juan") ;; Hola Juan!


(defn mas-argumentos
  "Lo mismo pero mas dificil"
  [aprovado & {:keys [estudiante profe] :or {estudiante "Nobita"
                                             profe "Juanjo"}}]
  (println (str estudiante " ha sido "
                (if aprovado "aprovado" "suspendido")
                " por " profe "!")))

(mas-argumentos true) ;; Nobita ha sido aprovado por Juanjo!
(mas-argumentos false :estudiante "Alvaro" :profe "Carlos") ;; Alvaro ha sido suspendido por Carlos!
