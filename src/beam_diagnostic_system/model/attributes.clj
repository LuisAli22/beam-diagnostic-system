(ns beam-diagnostic-system.model.attributes
	(:use [beam-diagnostic-system.util.miMath :only [sqrt]] ))



(defn getAlturaMinima
	"Calcula la altura minima y retorna ese valor dentro de un nuevo mapa
	él cual tambien tendra los valores del mapa que recibio como parametro"
	[mapa ptosApoyo altura] (
	let [key_ "alturaMinima"](
	cond
		(= ptosApoyo 1) (assoc mapa key_ (/ altura 8.0))
		(= ptosApoyo 2) (assoc mapa key_ (/ altura 16.0))
		(= ptosApoyo 3) (assoc mapa key_ (/ altura 18.5))

		; si es mayor que 3
		:else (assoc mapa key_ (/ altura 21.0))
	)))

(defn getAlturaUtil [mapa altura diamBarraLong diamBarrasEstribo recubrimHormigon] (
	let [key_ "alturaUtil"] (
	assoc mapa key_ (- altura (/ diamBarraLong 2.0) diamBarrasEstribo recubrimHormigon)
	)))

(defn getSeccionMinima [mapa calAcero calHormigon ancho] (
	let [key_ "seccionMinima"] (
	assoc mapa key_ (* (/ (sqrt calHormigon) (* 4.0 calAcero)) ancho (mapa "alturaUtil"))
	)))

(defn getLimiteSeccionMinima [mapa calAcero ancho] (
	let [key_ "limiteSeccionMinima"] (
	assoc mapa key_ (* (/ 1.4 calAcero) ancho (mapa "alturaUtil"))
	)))

(defn getCapacidadCorte [mapa corteNominal] (
	let [key_ "capacidadCorte"] (
	assoc mapa key_ (* 0.75 corteNominal)
	)))

(defn getCapacidadFlexion [mapa momentoNominal] (
	let [key_ "capacidadFlexion"] (
	assoc mapa key_ (* 0.9 momentoNominal)
	)))

; Importante no cambiar el orden en que invocan los metodos, la altura minima se debe obtener antes 
; que la seccion minima y limiteseccion minima
(defn getAttributes 
	"params: contiene las caracteristicas numericas de la viga 
	Se retorna un mapa con los atributos que se usaran para aplicar las reglas"
	[params] (
	let [map {}] (
		getCapacidadFlexion (getCapacidadCorte (getLimiteSeccionMinima (getSeccionMinima (getAlturaUtil 
			(getAlturaMinima map (params "ptosApoyo") (params "altura"))
			(params "altura") (params "diamBarraLong") (params "diamBarrasEstribo") (params "recubrimHormigon"))
				(params "calAcero") (params "calHormigon") (params "ancho"))
					(params "calAcero") (params "ancho"))
						(params "corteNominal"))
							(params "momentoNominal")
		)
	))
