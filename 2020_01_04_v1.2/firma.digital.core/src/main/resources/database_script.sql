--
-- En esta tabla se almacena la información sobre vigencias de certificados
--
create table certificado ( 
    no_serie			varchar2( 20)	primary key,
    fec_inicial_cert	timestamp 		not null,
    fec_final_cert		timestamp 		not null,
    rfc					varchar2( 18) 	not null,
    edo_certificado		char(	   1)	not null
);

create index idx_rfc on certificado(rfc);


create table configuracion(
	propiedad		varchar2(  32)	primary key,
	valor			varchar2( 512)	not null
);

insert into configuracion ( propiedad, valor) values (
	'algoritmo.digest.cipher', 'SHA1withRSA'
);

insert into configuracion ( propiedad, valor) values (
	'path.directorio.descarga', 'C:\firma_electronica\descargas'
);

insert into configuracion ( propiedad, valor) values (
	'url.servicio.ocsp.fiel', 'http://www.sat.gob.mx/ocsp'
);

insert into configuracion ( propiedad, valor) values (
	'path.certificados.raiz.fiel', 'C:\firma_electronica\certificados\fiel'
);


insert into configuracion ( propiedad, valor) values (
	'path.certificados.raiz.fp', 'C:\firma_electronica\certificados\funcion_publica'
);

insert into configuracion ( propiedad, valor) values (
	'url.servicio.ocsp.fp', 'http://200.77.236.81:8083/OCSP'
);

