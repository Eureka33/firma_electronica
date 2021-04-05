-- [NOTA] Este script esta hecho a la medida para una base de datos POSTGRESQL ver 11
--
--	Script de generacion de tablas ver. 1.3
--

CREATE TABLE eurk_wsss_configuracion (
    clave 	varchar(32) 	NOT NULL,
    valor 	varchar(512) 	NOT NULL,
    CONSTRAINT eurk_wsss_configuracion_pkey PRIMARY KEY (clave)
);


CREATE TABLE eurk_wsss_usuario (
    id           serial     	primary key,
    clave        varchar(30)  	NOT NULL,
    nombre       varchar(256) 	NOT NULL
);



CREATE TABLE eurk_wsss_documento_firmado (
    id 		serial 		primary key,
    id_usuario 	int4 		NOT NULL,
    fecha_hora 	timestamp 	NOT NULL,
    folio 	varchar(60) 	NOT NULL,
    nombre 	varchar(256) 	NOT NULL,
    CONSTRAINT fk_usuario_docfrmd	FOREIGN KEY (id_usuario) REFERENCES eurk_wsss_usuario( id)
);


CREATE TABLE eurk_wsss_registro_batch (
    id 		serial 		primary key,
    id_usuario 	int4 		NOT NULL,
    fecha_hora 	timestamp 	NOT NULL,
    folio 	varchar(60) 	NOT NULL,
    nombre 	varchar(256) 	NOT NULL,
    CONSTRAINT fk_usuario_regbatch 	FOREIGN KEY (id_usuario) REFERENCES eurk_wsss_usuario( id)
);



CREATE TABLE public.eurk_wsss_documento_batch (
    id_documento_firmado 	int4 primary key,
    id_registro_batch 		int4 NOT NULL,
    CONSTRAINT fk_documento_docbatch 	FOREIGN KEY (id_documento_firmado) REFERENCES eurk_wsss_documento_firmado(id),
    CONSTRAINT fk_registro_docbatch 	FOREIGN KEY (   id_registro_batch) REFERENCES eurk_wsss_registro_batch(   id)
);
