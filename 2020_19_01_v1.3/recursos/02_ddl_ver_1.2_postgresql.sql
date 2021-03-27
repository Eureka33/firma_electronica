-- [NOTA] Este script esta hecho a la medida para una base de datos POSTGRESQL ver 11
--
--	Script de generación de tablas ver. 1.4 > Registro de solicitudes de firma
--

drop table if exists eurk_wsss_solicitud_atendida;
drop table if exists eurk_wsss_solicitud_firma;


CREATE TABLE eurk_wsss_solicitud_firma (
    id 					serial 			primary key,
    
    id_usuario 			int4 			NOT NULL,
    fecha_hora 			timestamp 		NOT NULL,
    folio 				varchar(60) 	NOT NULL,
    path_upload			varchar(256) 	NOT NULL,
    email_destinatario  varchar(128) 	not null,
    
    estatus         	SMALLINT        NOT NULL DEFAULT 0,
    last_update			timestamp		NOT NULL,
    
    CONSTRAINT fk_usuario_solfirm	FOREIGN KEY (id_usuario) REFERENCES eurk_wsss_usuario( id),
    constraint u_folio_solfirm		unique( folio)
);

CREATE TABLE eurk_wsss_solicitud_atendida (
    id_documento_firmado 	int4 not null,
    id_solicitud_firma      int4 NOT NULL,
    
    constraint pk_solicitud_atendida primary key( id_documento_firmado, id_solicitud_firma),
    
    CONSTRAINT fk_documento_solaten  FOREIGN KEY ( id_documento_firmado) REFERENCES eurk_wsss_documento_firmado(id),
    CONSTRAINT fk_solicitud_solaten  FOREIGN KEY (   id_solicitud_firma) REFERENCES eurk_wsss_solicitud_firma(  id),
    
    CONSTRAINT u_documento_firmado   UNIQUE( id_documento_firmado),
    CONSTRAINT u_solicitud_firma     UNIQUE(  id_solicitud_firma)
);