(ns beam-diagnostic-system.ui.terminal)

(defn printFirstQuestion [i] (
	if (= i 14) (println "Ingrese las caracteristicas de la viga: \n") 
	))

(defn getTextParams [valor] (
	cond
	(= 14 valor) "Ingresa la calidad del acero: "
	(= 13 valor) "Ingresa la calidad del hormigón: "
	(= 12 valor) "Ingresa la longitud de la viga: "
	(= 11 valor) "Ingresa el área del acero: "
	(= 10 valor) "Ingresa el ancho de la viga: "
	(= 9 valor)  "Ingresa la altura de la viga: "
	(= 8 valor)  "Ingresa la cantidad de puntos de apoyo: "
	(= 7 valor)  "Ingresa el momento último: "
	(= 6 valor)  "Ingresa el momento nominal: "
	(= 5 valor)  "Ingresa el corte último: "
	(= 4 valor)  "Ingresa el corte nominal: "
	(= 3 valor)  "Ingresa el diametro de la Barra Longitudinal: "
	(= 2 valor)  "Ingresa el diametro de la Barras de Estribo: "
	(= 1 valor)  "Ingresa el recubrimiento del Hormigón: "
	))

(defn getKeyParams [valor] (
	cond
	(= 14 valor) "calAcero"
	(= 13 valor) "calHormigon"
	(= 12 valor) "longitud"
	(= 11 valor) "areaAcero"
	(= 10 valor) "ancho"
	(= 9 valor)  "altura"
	(= 8 valor)  "ptosApoyo"
	(= 7 valor)  "momentoUltimo"
	(= 6 valor)  "momentoNominal"
	(= 5 valor)  "corteUltimo"
	(= 4 valor)  "corteNominal"
	(= 3 valor)  "diamBarraLong"
	(= 2 valor)  "diamBarrasEstribo"
	(= 1 valor)  "recubrimHormigon"
	))