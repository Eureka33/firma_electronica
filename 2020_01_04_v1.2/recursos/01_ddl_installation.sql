--
--	Servicio Web de Firma Electronica. Firma y Almacenamiento de documentos digitales
--	
--		digitalSignWSSS  (digital sign web service, sign and storage) 
--

--drop table eurk_wsss_documento_batch;
--drop table eurk_wsss_registro_batch;
--drop table eurk_wsss_documento_firmado;
--drop  table eurk_wsss_usuario;
--drop table eurk_wsss_configuracion;

create table eurk_wsss_configuracion (
	clave       varchar(  32) primary key,
	valor       varchar( 512) not null
);

create table eurk_wsss_usuario (
	id 			integer identity primary key,
	clave 		varchar(  30) not null,
	nombre 		varchar( 256) not null
);

create table eurk_wsss_documento_firmado (
	id			integer identity primary key,
	
	id_usuario  integer not null,
	fecha_hora  datetime not null,
	folio       varchar(  60) not null,
	nombre      varchar( 256) not null,
	
	constraint fk_usuario_docfrmd foreign key ( id_usuario) references eurk_wsss_usuario( id)
);

create table eurk_wsss_registro_batch(
	id          integer identity primary key,
	
	id_usuario  integer not null,
	fecha_hora  datetime not null,
	folio       varchar(  60) not null,
	nombre      varchar( 256) not null,
	
	constraint fk_usuario_regbatch foreign key ( id_usuario) references eurk_wsss_usuario( id)
);

create table eurk_wsss_documento_batch (
	id_documento_firmado	integer primary key,
	id_registro_batch		integer not null,
	
	constraint fk_documento_docbatch foreign key ( id_documento_firmado) references eurk_wsss_documento_firmado ( id),
	constraint fk_registro_docbatch foreign key ( id_registro_batch) references eurk_wsss_registro_batch ( id)
	
);