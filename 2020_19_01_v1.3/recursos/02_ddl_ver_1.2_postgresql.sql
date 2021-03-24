-- [NOTA] Este script esta hecho a la medida para una base de datos POSTGRESQL ver 11
--
--	Script de generación de tablas ver. 1.4 > Registro de solicitudes de firma
--

CREATE TABLE eurk_wsss_solicitud_firma (
    id 				serial 			primary key,
    
    id_usuario 		int4 			NOT NULL,
    fecha_hora 		timestamp 		NOT NULL,
    folio 			varchar(60) 	NOT NULL,
    path_upload		varchar(256) 	NOT NULL,
    
    estatus         SMALLINT        NOT NULL DEFAULT 1,
    last_update		timestamp		NOT NULL,
    
    CONSTRAINT fk_usuario_solfirm	FOREIGN KEY (id_usuario) REFERENCES eurk_wsss_usuario( id)
);

CREATE TABLE eurk_wsss_solicitud_atendida (
    id_documento_firmado 	int4 primary key,
    id_solicitud_firma      int4 NOT NULL,
    CONSTRAINT fk_documento_solaten 	FOREIGN KEY ( id_documento_firmado) REFERENCES eurk_wsss_documento_firmado(id),
    CONSTRAINT fk_solicitud_solaten 	FOREIGN KEY (   id_solicitud_firma) REFERENCES eurk_wsss_solicitud_firma(  id)
);
