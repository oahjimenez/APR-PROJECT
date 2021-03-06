CREATE TABLE USUARIO (
   ID    		INTEGER PRIMARY KEY,
   RUT          CHAR(10)  NOT NULL,
   NOMBRES      CHAR(80)  NOT NULL,
   APELLIDOS	CHAR(80)  NOT NULL,
   DIRECCION    CHAR(140) NOT NULL,
   TELEFONO     CHAR(20)  NOT NULL,
   FECHA_REGISTRO  DATE NOT NULL,
   FECHA_RETIRO DATE NULL
);

CREATE UNIQUE INDEX RUT_USUARIO_UNQ ON USUARIO(RUT) WHERE FECHA_RETIRO IS NULL;

CREATE TABLE ADMINISTRADOR (
    ID 			 INTEGER PRIMARY KEY,
    PASSWORD     TEXT NOT NULL,

    FOREIGN KEY(ID) REFERENCES USUARIO(ID)
);

CREATE TABLE MEDIDOR (
   ID           TEXT PRIMARY KEY,
   DESCRIPCION    CHAR(80) NULL,
   FECHA_REGISTRO DATE NOT NULL,
   FECHA_RETIRO DATE NULL
);

-- REGISTRO DE ADMINISTRADORES QUE CREAN USUARIOS
CREATE TABLE REGISTRA_USUARIO (
	USUARIO_ID CHAR(10) NOT NULL,
	ADMINISTRADOR_ID CHAR(10) NOT NULL,
	FECHA_REGISTRO DATE NOT NULL,

	FOREIGN KEY(USUARIO_ID) REFERENCES USUARIO(ID)
	FOREIGN KEY(ADMINISTRADOR_ID) REFERENCES ADMINISTRADOR(ID)
	PRIMARY KEY (USUARIO_ID,ADMINISTRADOR_ID,FECHA_REGISTRO)
);

-- REGISTRO DE ADMINISTRADORES QUE RETIRA USUARIOS
CREATE TABLE RETIRA_USUARIO(
	USUARIO_ID CHAR(10) NOT NULL,
	ADMINISTRADOR_ID CHAR(10) NOT NULL,
	FECHA_REGISTRO DATE NOT NULL,

	FOREIGN KEY(USUARIO_ID) REFERENCES USUARIO(ID)
	FOREIGN KEY(ADMINISTRADOR_ID) REFERENCES ADMINISTRADOR(ID)
	PRIMARY KEY (USUARIO_ID,ADMINISTRADOR_ID,FECHA_REGISTRO)
);


CREATE TABLE REGISTRA_MEDIDOR (
	ADMINISTRADOR_ID  CHAR(10) NOT NULL,
	MEDIDOR_ID TEXT NOT NULL ,
	FECHA_REGISTRO DATE NOT NULL,

	FOREIGN KEY(ADMINISTRADOR_ID) REFERENCES ADMINISTRADOR(ID)
	FOREIGN KEY(MEDIDOR_ID) REFERENCES MEDIDOR(ID)
	PRIMARY KEY (ADMINISTRADOR_ID,MEDIDOR_ID,FECHA_REGISTRO)
);

CREATE TABLE TIENE_MEDIDOR (
	USUARIO_ID CHAR(10) NOT NULL,
	MEDIDOR_ID TEXT NOT NULL,
	FECHA_ADQUISICION DATE NOT NULL,
	FECHA_FINIQUITO DATE,

	FOREIGN KEY(USUARIO_ID) REFERENCES USUARIO(ID)
	FOREIGN KEY(MEDIDOR_ID) REFERENCES MEDIDOR(ID)
	PRIMARY KEY (USUARIO_ID,MEDIDOR_ID,FECHA_ADQUISICION)
);


CREATE TABLE LECTURA_MENSUAL (
	USUARIO_ID CHAR(10) NOT NULL,
	MEDIDOR_ID TEXT NOT NULL,
	FECHA DATE NOT NULL,
	VALOR REAL NOT NULL,

	FOREIGN KEY(USUARIO_ID) REFERENCES USUARIO(ID)
	FOREIGN KEY(MEDIDOR_ID) REFERENCES MEDIDOR(ID)
	PRIMARY KEY(USUARIO_ID,MEDIDOR_ID,FECHA)
);

CREATE TABLE SUBSIDIO (
	ID INTEGER PRIMARY KEY AUTOINCREMENT, 
	USUARIO_ID CHAR(10) NOT NULL,
	PORCENTAJE_SUBSIDIO REAL NOT NULL,
	TOPE REAL NOT NULL,
	FECHA DATE NOT NULL,

	FOREIGN KEY(USUARIO_ID) REFERENCES USUARIO(ID)
	UNIQUE(USUARIO_ID,FECHA) ON CONFLICT REPLACE
);

CREATE TABLE SUBSIDIO_AUDIT (
   SUBSIDIO INTEGER PRIMARY KEY,
   SUBSIDIO_ID  INTEGER, 
   USUARIO_ID CHAR(10) NOT NULL,
   PORCENTAJE_SUBSIDIO REAL NOT NULL,
   TOPE REAL NOT NULL,
   FECHA DATE NOT NULL,
   ACCION      TEXT NOT NULL,
   FECHA_ULTIMA_MODIFICACION DATE NOT NULL,
   ADMINISTRADOR_ID INTEGER NOT NULL,
   FOREIGN KEY(ADMINISTRADOR_ID) REFERENCES ADMINISTRADOR(ID)
   CHECK (ACCION="INSERT" or ACCION="UPDATE" or ACCION="DELETE")

);

CREATE TABLE COSTO_METRO_CUBICO (
   ID           INTEGER PRIMARY KEY,
   COSTO        REAL NOT NULL,
   FECHA        DATE NOT NULL
);

CREATE TABLE COSTO_METRO_CUBICO_AUDIT (
   ID           INTEGER PRIMARY KEY,
   COSTO_METRO_CUBICO_ID INTEGER,
   COSTO        REAL NOT NULL,
   FECHA        DATE NOT NULL,
   ACCION		TEXT NOT NULL,
   FECHA_ULTIMA_MODIFICACION DATE NOT NULL,
   ADMINISTRADOR_ID INTEGER NOT NULL,
   FOREIGN KEY(ADMINISTRADOR_ID) REFERENCES ADMINISTRADOR(ID)
   CHECK (ACCION="INSERT" or ACCION="UPDATE" or ACCION="DELETE")
);

CREATE TABLE CARGO_FIJO_MENSUAL (
   ID           INTEGER PRIMARY KEY,
   CARGO        REAL NOT NULL,
   FECHA        DATE NOT NULL
);

CREATE TABLE CARGO_FIJO_MENSUAL_AUDIT (
   ID           INTEGER PRIMARY KEY,
   CARGO_FIJO_MENSUAL_ID INTEGER,
   CARGO        REAL NOT NULL,
   FECHA        DATE NOT NULL,
   ACCION		TEXT NOT NULL,
   FECHA_ULTIMA_MODIFICACION DATE NOT NULL,
   ADMINISTRADOR_ID INTEGER NOT NULL,
   FOREIGN KEY(ADMINISTRADOR_ID) REFERENCES ADMINISTRADOR(ID)
   CHECK (ACCION="INSERT" or ACCION="UPDATE" or ACCION="DELETE")
);


CREATE TABLE REGISTRA_MEDIDOR_USURIO (
   ADMINISTRADOR_ID         CHAR(10) NOT NULL,
   MEDIDOR_ID        TEXT NOT NULL ,
   FECHA_REGISTRO    DATE NOT NULL,
   FOREIGN KEY(ADMINISTRADOR_ID) REFERENCES ADMINISTRADOR(ID)
   FOREIGN KEY(MEDIDOR_ID) REFERENCES MEDIDOR (ID)
   PRIMARY KEY(ADMINISTRADOR_ID,MEDIDOR_ID,FECHA_REGISTRO)
);

CREATE TABLE ELIMINA_MEDIDOR  (
   ADMINISTRADOR_ID          CHAR(10) NOT NULL,
   MEDIDOR_ID                 TEXT NOT NULL ,
   FECHA_RETIRO               DATE NOT NULL,
   FOREIGN KEY(ADMINISTRADOR_ID)      REFERENCES ADMINISTRADOR(ID)
   FOREIGN KEY(MEDIDOR_ID)             REFERENCES MEDIDOR(ID)
);

CREATE TABLE REGISTRO_LECTURA  (
   ADMINISTRADOR_ID         CHAR(10) NOT NULL,
   MEDIDOR_ID         TEXT NOT NULL ,
   FECHAR_LECTURA     DATE 
   FECHA_INGRESO      DATE NOT NULL,

   FOREIGN KEY(ADMINISTRADOR_ID) REFERENCES ADMINISTRADOR(ID)
   FOREIGN KEY(MEDIDOR_ID) REFERENCES MEDIDOR(ID)

);

CREATE TABLE REGISTRA_SUBSIDIO (
	SUBSIDIO_ID  INTEGER NOT NULL,
	ADMINISTRADOR_ID CHAR(10) NOT NULL,
	FECHA_REGISTRO DATE NOT NULL,

	FOREIGN KEY(SUBSIDIO_ID) REFERENCES SUBSIDIO(ID)
	FOREIGN KEY(ADMINISTRADOR_ID) REFERENCES ADMINISTRADOR(ID)
	PRIMARY KEY (SUBSIDIO_ID,ADMINISTRADOR_ID,FECHA_REGISTRO)
);

