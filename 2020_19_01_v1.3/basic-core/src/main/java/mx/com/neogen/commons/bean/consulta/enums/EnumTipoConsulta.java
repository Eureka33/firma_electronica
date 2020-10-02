package com.eurk.core.beans.consulta.enums;

public enum EnumTipoConsulta {
	AUTOCOMPLEATADA	,	// consulta filtrada por tokens (devuelve primer página y total de resultados)
	PAGINADA		,	// regresa la pagina solicitada (consulta por tokens / propiedades)
	NOPAGINADA		;	// regresa todos los resultados (consulta por tokens / propiedades)
}