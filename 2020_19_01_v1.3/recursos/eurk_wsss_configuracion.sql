--
-- Contenido de la tabla de configuracion
--
--	Las rutas del sistema de archivos deben ser actualizadas de acuerdo al ambiente de ejecución
--

INSERT INTO eurk_wsss_configuracion (clave, valor) VALUES (     'algoritmo.digest.cipher', 'SHA1withRSA');
INSERT INTO eurk_wsss_configuracion (clave, valor) VALUES (      'url.servicio.ocsp.fiel', 'http://www.sat.gob.mx/ocsp');
INSERT INTO eurk_wsss_configuracion (clave, valor) VALUES (        'url.servicio.ocsp.fp', 'http://200.77.236.81:8083/OCSP');
INSERT INTO eurk_wsss_configuracion (clave, valor) VALUES (           'url.servicio.ocsp', 'https://cfdi.sat.gob.mx/edofiel');
INSERT INTO eurk_wsss_configuracion (clave, valor) VALUES (             'url.server.name', 'http://localhost:8080');
INSERT INTO eurk_wsss_configuracion (clave, valor) VALUES (            'path.app.context', '/digitalSignWSSS');
INSERT INTO eurk_wsss_configuracion (clave, valor) VALUES (  'string.organizacion.nombre', 'Eureka Consulting México');
INSERT INTO eurk_wsss_configuracion (clave, valor) VALUES (    'path.directorio.descarga', 'C:\SIGMADOC\firmaDigitalv3\descargas');
INSERT INTO eurk_wsss_configuracion (clave, valor) VALUES ( 'path.certificados.raiz.fiel', 'C:\SIGMADOC\firmaDigitalv3\certificados\fiel');
INSERT INTO eurk_wsss_configuracion (clave, valor) VALUES (   'path.certificados.raiz.fp', 'C:\SIGMADOC\firmaDigitalv3\certificados\funcion_publica');

INSERT INTO eurk_wsss_configuracion (clave, valor) VALUES (      'path.certificados.raiz','C:\SIGMADOC\firmaDigitalv3\certificados_raiz');
INSERT INTO eurk_wsss_configuracion (clave, valor) VALUES (         'path.repositorio.fs','C:\SIGMADOC\firmaDigitalv3\repositorio');

INSERT INTO eurk_wsss_configuracion (clave, valor) VALUES (         'link.maximo.visitas', '1');