CREATE TABLE USUARIO (
   RUT          CHAR(10) PRIMARY KEY,
   NOMBRE       CHAR(80) NOT NULL,
   DIRECCION    CHAR(140) NOT NULL,
   TELEFONO     CHAR(20)  NOT NULL,
   FECHA_REGISTRO  DATE NOT NULL,
   FECHA_RETIRO DATE NULL
);

CREATE TABLE ADMINISTRADOR (
    RUT  CHAR(10) PRIMARY KEY,
    PASSWORD     TEXT NOT NULL,

    FOREIGN KEY(RUT) REFERENCES USUARIO(RUT)
);

CREATE TABLE MEDIDOR (
   ID           TEXT PRIMARY KEY,
   NOMBRE       CHAR(80) NOT NULL,
   FECHA_REGISTRO DATE NOT NULL,
   FECHA_RETIRO DATE NULL
);

-- REGISTRO DE ADMINISTRADORES QUE CREAN USUARIOS
CREATE TABLE REGISTRA_USUARIO (
	USUARIO_RUT CHAR(10) NOT NULL,
	ADMINISTRADOR_RUT CHAR(10) NOT NULL,
	FECHA_REGISTRO DATE NOT NULL,

	FOREIGN KEY(USUARIO_RUT) REFERENCES USUARIO(RUT)
	FOREIGN KEY(ADMINISTRADOR_RUT) REFERENCES ADMINISTRADOR(RUT)
	PRIMARY KEY (USUARIO_RUT,ADMINISTRADOR_RUT,FECHA_REGISTRO)
);

-- REGISTRO DE ADMINISTRADORES QUE RETIRA USUARIOS
CREATE TABLE RETIRA_USUARIO(
	USUARIO_RUT CHAR(10) NOT NULL,
	ADMINISTRADOR_RUT CHAR(10) NOT NULL,
	FECHA_REGISTRO DATE NOT NULL,

	FOREIGN KEY(USUARIO_RUT) REFERENCES USUARIO(RUT)
	FOREIGN KEY(ADMINISTRADOR_RUT) REFERENCES ADMINISTRADOR(RUT)
	PRIMARY KEY (USUARIO_RUT,ADMINISTRADOR_RUT,FECHA_REGISTRO)
);


CREATE TABLE REGISTRA_MEDIDOR (
	ADMINISTRADOR_RUT  CHAR(10) NOT NULL,
	MEDIDOR_ID TEXT NOT NULL ,
	FECHA_REGISTRO DATE NOT NULL,

	FOREIGN KEY(ADMINISTRADOR_RUT) REFERENCES ADMINISTRADOR(RUT)
	FOREIGN KEY(MEDIDOR_ID) REFERENCES MEDIDOR(ID)
	PRIMARY KEY (ADMINISTRADOR_RUT,MEDIDOR_ID,FECHA_REGISTRO)
);

CREATE TABLE TIENE_MEDIDOR (
	USUARIO_RUT CHAR(10) NOT NULL,
	MEDIDOR_ID TEXT NOT NULL,
	FECHA_ADQUISICION DATE NOT NULL,
	FECHA_FINIQUITO DATE,

	FOREIGN KEY(USUARIO_RUT) REFERENCES USUARIO(RUT)
	FOREIGN KEY(MEDIDOR_ID) REFERENCES MEDIDOR(ID)
	PRIMARY KEY (USUARIO_RUT,MEDIDOR_ID,FECHA_ADQUISICION)
);

CREATE TABLE LECTURA_MENSUAL (
	USUARIO_RUT CHAR(10) NOT NULL,
	MEDIDOR_ID TEXT NOT NULL,
	FECHA DATE NOT NULL,
	VALOR REAL NOT NULL,

	FOREIGN KEY(USUARIO_RUT) REFERENCES USUARIO(RUT)
	FOREIGN KEY(MEDIDOR_ID) REFERENCES MEDIDOR(ID)
);

CREATE TABLE SUBSIDIOS (
	USUARIO_RUT CHAR(10) NOT NULL,
	PORCENTAJE_SUBSIDIO REAL NOT NULL,
	TOPE REAL NOT NULL,
	FECHA DATE NOT NULL,

	FOREIGN KEY(USUARIO_RUT) REFERENCES USUARIO(RUT)
);

CREATE TABLE COSTO_METRO_CUBIDO (
   ID           INTEGER PRIMARY KEY,
   VALOR        REAL NOT NULL,
   FECHA_REGISTRO DATE NOT NULL,
   FECHA_VENCIMIENTO DATE
);

CREATE TABLE CARGO_FIJO_MENSUAL (
   ID           INTEGER PRIMARY KEY,
   VALOR        REAL NOT NULL,
   FECHA_REGISTRO DATE NOT NULL,
   FECHA_VENCIMIENTO DATE
);
