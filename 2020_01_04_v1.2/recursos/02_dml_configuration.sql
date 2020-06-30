--
--	Propieadades de configuración
--	
--	TODO: Revisar el uso de cada propiedad en el codigo fuente
--

insert into eurk_wsss_configuracion( clave, valor) values (     'algoritmo.digest.cipher', 'SHA1withRSA');
insert into eurk_wsss_configuracion( clave, valor) values (      'url.servicio.ocsp.fiel', 'http://www.sat.gob.mx/ocsp');
insert into eurk_wsss_configuracion( clave, valor) values (        'url.servicio.ocsp.fp', 'http://200.77.236.81:8083/OCSP');
insert into eurk_wsss_configuracion( clave, valor) values (           'url.servicio.ocsp', 'https://cfdi.sat.gob.mx/edofiel');
insert into eurk_wsss_configuracion( clave, valor) values (    'path.directorio.descarga', 'C:\sigmadoc\FIEL\descargas');
insert into eurk_wsss_configuracion( clave, valor) values ( 'path.certificados.raiz.fiel', 'C:\sigmadoc\FIEL\certificados\fiel');
insert into eurk_wsss_configuracion( clave, valor) values (   'path.certificados.raiz.fp', 'C:\sigmadoc\FIEL\certificados\funcion_publica');
insert into eurk_wsss_configuracion( clave, valor) values (      'path.certificados.raiz', 'C:\sigmadoc\FIEL\certificados_raiz');
insert into eurk_wsss_configuracion( clave, valor) values (         'path.repositorio.fs', 'C:\sigmadoc\FIEL\repositorio');
insert into eurk_wsss_configuracion( clave, valor) values (             'url.server.name', 'http://localhost:8080');
insert into eurk_wsss_configuracion( clave, valor) values (            'path.app.context', '/digitalSignWSSS');
insert into eurk_wsss_configuracion( clave, valor) values (  'string.organizacion.nombre', 'Aeropuertos y Servicios Auxiliares');

