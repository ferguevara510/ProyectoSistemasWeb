CREATE DATABASE evaluaciones;
USE evaluaciones;

CREATE TABLE IF NOT EXISTS alumno (
	matricula varchar(10) PRIMARY KEY NOT NULL,
	contrasena varchar(16) NOT NULL,
	nombre varchar(50) NOT NULL,
	apellidoPaterno varchar(25) NOT NULL,
	apellidoMaterno varchar(25) NOT NULL
);

INSERT INTO alumno (matricula, contrasena, nombre, apellidoPaterno, apellidoMaterno) VALUES ('zS19030170', '12345', 'Karla Fernanda', 'Guevara', 'Flores'), ('zS19030172', '1234', 'Nadia Itzel', 'Bravo', 'Guevara');

CREATE TABLE IF NOT EXISTS profesor (
	usuario varchar(10) PRIMARY KEY NOT NULL,
	contrasena varchar(16) NOT NULL
);

INSERT INTO profesor (usuario, contrasena) VALUES ('Profesor', '12345');

CREATE TABLE IF NOT EXISTS examen (
	folioExamen int(5) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	nombre varchar(30) NOT NULL,
	calificacion float NULL,
	inicio datetime NOT NULL,
	fin datetime NOT NULL,
	usuario varchar(10) NULL,
	FOREIGN KEY (usuario) REFERENCES profesor (usuario)
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