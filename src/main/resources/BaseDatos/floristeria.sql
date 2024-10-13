DROP DATABASE IF EXISTS Floristeria;
CREATE DATABASE Floristeria;
Use Floristeria;


CREATE TABLE Empleados(
Usuario varchar(20) primary key,
Contrasena varchar(32)
);

CREATE TABLE Flores(
Id int unsigned auto_increment primary key,
Nombre varchar(20),
Precio double(10, 2),
Color enum('Blanco','Amarillo','Rojo','Azul')
)