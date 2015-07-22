(ns beam-diagnostic-system.model.core 
	(:use [beam-diagnostic-system.model.attributes :only [getAttributes]])
	(:use [beam-diagnostic-system.model.rules]))
	  

; mapa inicial con las caracteristicas de la viga que debe obtener el experto 
; despues de realizar las mediciones 
(def paramsViga
	{"calAcero" "", "calHormigon" "", "longitud" "", "areaAcero" "", 
      "ancho" "", "altura" "", "ptosApoyo" "", "momentoUltimo" "", "momentoNominal" "", 
      "corteUltimo" "", "corteNominal" "", "diamBarraLong" "", "diamBarrasEstribo" "",
      "recubrimHormigon" ""} 
      )

(defn convertParamsStringtoInt
	"Convierte los valores de las caracteristicas de la viga, ingresadas por el usuario,
	en valores numericos"
	[params] (
	let [mapa {}] (
		assoc (assoc (assoc (assoc (assoc (assoc (assoc (assoc (assoc (assoc (assoc (assoc (assoc 
			(assoc mapa 
			"calAcero" (read-string (params "calAcero"))) 
			"calHormigon" (read-string (params "calHormigon")))
			"longitud" (read-string (params "longitud")))
			"areaAcero" (read-string (params "areaAcero")))
			"ancho" (read-string (params "ancho")))
			"altura" (read-string (params "altura")))
			"ptosApoyo" (read-string (params "ptosApoyo")))
			"momentoUltimo" (read-string (params "momentoUltimo")))
			"momentoNominal" (read-string (params "momentoNominal")))
			"corteUltimo" (read-string (params "corteUltimo")))
			"corteNominal" (read-string (params "corteNominal")))
			"diamBarraLong" (read-string (params "diamBarraLong")))
			"diamBarrasEstribo" (read-string (params "diamBarrasEstribo")))
			"recubrimHormigon" (read-string (params "recubrimHormigon"))
		)
	))

(defn comprobarVigaBienDimensionada 
	"Utiliza las caracteristicas de la viga para obtener los atributos 
	que se usarán en las fórmulas"
	[params] (
		if (rVerifReqDis (getAttributes (convertParamsStringtoInt params)) (convertParamsStringtoInt params))
			true
			false

	))














