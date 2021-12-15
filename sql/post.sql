-- Database: examenes

-- DROP DATABASE IF EXISTS examenes;

CREATE DATABASE examenes
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Spanish_Mexico.1252'
    LC_CTYPE = 'Spanish_Mexico.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
	
	CREATE TABLE IF NOT EXISTS alumno (
		matricula varchar(10) PRIMARY KEY NOT NULL,
		contrasena varchar(30) NOT NULL,
		nombre varchar(30) NOT NULL,
		apellidoPaterno varchar(25) NOT NULL,
		apellidoMaterno varchar(25) NOT NULL);
	
	INSERT INTO alumno (matricula, contrasena, nombre, apellidoPaterno, apellidoMaterno) VALUES ('zS19030170', '12345', 'Karla Fernanda', 'Guevara', 'Flores'), ('zS19030172', '1234', 'Nadia Itzel', 'Bravo', 'Guevara');
	
	CREATE TABLE IF NOT EXISTS profesor (
		usuario varchar(10) PRIMARY KEY NOT NULL,
		contrasena varchar(16) NOT NULL
	);

	INSERT INTO profesor (usuario, contrasena) VALUES ('Profesor', '12345');

	CREATE TABLE IF NOT EXISTS examen(
		folioExamen serial PRIMARY KEY,
		nombre varchar(30) NOT NULL,
		calificacion float NULL,
		inicio timestamp NOT NULL,
		fin timestamp NOT NULL,
		usuario varchar(10) NULL,
		FOREIGN KEY (usuario) REFERENCES profesor (usuario));

	CREATE TABLE IF NOT EXISTS pregunta (
		id serial PRIMARY KEY,
		descripcion varchar(1000) NOT NULL,
		tipo varchar(100) NOT NULL,
		folioExamen int NULL,
		FOREIGN KEY (folioExamen) REFERENCES examen (folioExamen));


	CREATE TABLE IF NOT EXISTS respuesta (
		id serial PRIMARY KEY,
		idPregunta int NULL,
		descripcion varchar(1000) NOT NULL,
		folioExamen int NULL,
		correcto bool NULL,
		FOREIGN KEY (idPregunta) REFERENCES pregunta (id));

