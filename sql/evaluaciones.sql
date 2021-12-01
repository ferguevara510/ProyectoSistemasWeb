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