-- [NOTA] Este script esta hecho a la medida para una base de datos MSSQL 
--
--	Script de generacion de tablas ver. 1.3 > Registro de solicitudes de firma
--

drop table if exists eurk_wsss_visitas_solicitud;
drop table if exists eurk_wsss_solicitud_atendida;
drop table if exists eurk_wsss_solicitud_firma;


CREATE TABLE eurk_wsss_solicitud_firma (
    id 					integer identity primary key,
    
    id_usuario 			integer			NOT NULL,
    fecha_hora 			datetime 		NOT NULL,
    folio 				varchar( 60) 	NOT NULL,
    nombre				varchar( 256) 	NOT NULL,
    email_destinatario  varchar( 128) 	not null,
    email_solicitante   varchar( 128)   ,
    
    estatus         	SMALLINT        NOT NULL DEFAULT 0,
    last_update			datetime		NOT NULL,
    
    CONSTRAINT fk_usuario_solfirm	FOREIGN KEY (id_usuario) REFERENCES eurk_wsss_usuario( id),
    constraint u_folio_solfirm		unique( folio)
);

CREATE TABLE eurk_wsss_solicitud_atendida (
    id_documento_firmado 	integer not null,
    id_solicitud_firma      integer NOT NULL,
    
    constraint pk_solicitud_atendida primary key( id_documento_firmado, id_solicitud_firma),
    
    CONSTRAINT fk_documento_solaten  FOREIGN KEY ( id_documento_firmado) REFERENCES eurk_wsss_documento_firmado(id),
    CONSTRAINT fk_solicitud_solaten  FOREIGN KEY (   id_solicitud_firma) REFERENCES eurk_wsss_solicitud_firma(  id),
    
    CONSTRAINT u_documento_firmado   UNIQUE( id_documento_firmado),
    CONSTRAINT u_solicitud_firma     UNIQUE(  id_solicitud_firma)
);

create table eurk_wsss_visitas_solicitud(
	id_solicitud_firma		integer not null primary key,
	
	fecha_hora 				datetime	not null,
	visitas					smallint    not null,
	
	CONSTRAINT fk_solicitud_vissol  FOREIGN KEY (   id_solicitud_firma) REFERENCES eurk_wsss_solicitud_firma(  id)
);


INSERT INTO eurk_wsss_configuracion (clave, valor) VALUES (         'link.maximo.visitas', '1');
