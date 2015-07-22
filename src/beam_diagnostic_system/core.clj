(ns beam-diagnostic-system.core
  (:use [beam-diagnostic-system.ui.terminal]
  	[beam-diagnostic-system.model.core]))

; https://clojuredocs.org/clojure.core/try


(defn ingresarParams 
	"Se obtienen las caracteristicas de la viga"
	[mapa i](	
	do (printFirstQuestion i) (print (getTextParams i)) (flush) 
	(if (> i 0) 
		(recur (assoc mapa (getKeyParams i) (read-line)) (dec i) ) 		
		(if (comprobarVigaBienDimensionada mapa)
			(println "\nLa viga no necesita refuezo")
			(println "\nLa viga necesita refuerzo.... esta inconclusoo aqui")
			)
		)
	))

; 14 son las caracteristicas de la viga
(defn -main
	"Punto de entrada"
  [& args]
  (ingresarParams paramsViga 14))
