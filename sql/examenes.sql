CREATE TABLE IF NOT EXISTS examen (
	folioExamen int(5) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	nombre varchar(30) NOT NULL,
	calificacion float NOT NULL,
	inicio datetime NOT NULL,
	fin datetime NOT NULL
);

CREATE TABLE IF NOT EXISTS pregunta (
	id int(5) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	descripcion varchar(1000) NOT NULL,
	tipo varchar(100) NOT NULL,
    folioExamen int(5) NULL,
	FOREIGN KEY (folioExamen) REFERENCES examen (folioExamen)
);


CREATE TABLE IF NOT EXISTS respuesta (
	id int(5) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	idPregunta int(5) NULL,
	descripcion varchar(1000) NOT NULL,
    folioExamen int(5) NULL,
    correcto TINYINT(1) NULL,
	FOREIGN KEY (idPregunta) REFERENCES pregunta (id)
);