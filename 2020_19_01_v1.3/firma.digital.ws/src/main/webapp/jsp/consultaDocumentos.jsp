<%@page import="java.net.URLEncoder"%>
<%@page import="com.meve.ofspapel.firma.digital.core.entidades.Usuario"%>

<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>

<%
    final String[] errorMessages = (String[]) session.getAttribute( "errorMessages");
    session.removeAttribute( "errorMessages");
    
    final Usuario usuario = (Usuario) session.getAttribute( "usuario");
%>

<html lang="es">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
    
    <title>Consulta de Documentos Firmados</title>
    
    <link rel="stylesheet" href="./libs/fontawesome/css/all.min.css"    />
    <link rel="stylesheet" href="./libs/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="./libs/scripts/estilos.css"            />
       
    <script src="./libs/jquery/jquery.min.js"></script>
    <script src="./libs/bootstrap/js/bootstrap.min.js"></script>
    <script src="./libs/bootstrap/js/popper.min.js"></script>
    
    <script src="./libs//handlebars/handlebars.js"></script>
    <script src="./libs/scripts/eureka33/eurk-core.min.js"></script>
	   
    <script src="./libs/fontawesome/js/fontawesome.min.js"></script>
    <script src="./libs/jquery/is.min.js"></script>
    <script src="./libs/scripts/util_functions.js"></script>
    
	<script>	
        jQuery.noConflict();
        
        var env = { documento: {}};
        
        jQuery( document).ready( function( $) {
            if( is.ie()) {
                navigation.goto( 'pages/incompatible_browser.html');
                
            } else {
                server.resource.load( 'componente_autocompletion', './libs/components/autocompletion/component', function() {
                    inicializa();
                });
            } 
        });
        
		function inicializa() {
			document.getElementById( 'mensajeResultado').style.display= "<%= (errorMessages == null)? "none" : "" %>";
            <% if ( usuario != null) { %>
                 server.resource.load( 'componente_tabla', './libs/components/tabla/component', function() {
					console.log( "resource loaded: [tabla]");
                    env.tabla = new Tabla( 'documento', env.config, env.callbacks);
                    env.tabla.update();
                    
                    env.autoComplete = new AutoCompletion( 'autoComplete_documento', env.autocompletion, env.callbacks);
		
				});
            <% } %>
		}
        
        function validarForm( event) {
            event.stopPropagation;
                        
            let errores = 0;
            
            errores += validation.validarArchivos(  'certificado', '.cer',   'Certificado',   1, 1);
            errores += validation.validarArchivos( 'llavePrivada', '.key', 'Llave Privada',   1, 1);
            
            errores += validation.validarTexto( 'password',         'Contraseña',  20);
            
            let submit = (errores === 0);
            
            if( submit) {
                setTimeout( function() { jQuery( '#processing').modal( 'show');}, 0);
            }
            
            return submit;
        }
        
        env.config = {
            module    : "documento",
            id        : "id",
            pagination: true,
            isEditable: false,
			columns   : [
				{value: "fechaHora", label: "Fecha Hora"},
				{value:     "folio", label:      "Folio"},
				{value:    "nombre", label: "Documentos"}
			],
            actions   : [
                {value: 'view', icon: "download", title: "Descarga y validación del documento"}
            ],
            resource: "documento/firmado",
			search: {
                tipo: "PAGINADA", "propiedades": {}, "ordenacion": [{"campo": "fechaHora", "direccion": "desc"}]
            }
        };
        
        env.autocompletion = {
            resource: "documento/firmado",
            item: {
                value: "id",
                label: "(${fechaHora}) ${nombre} : ${folio}"
            }
		};
        
        env.callbacks = {
        
            show_action_view: function( item, items) {
                return true;
            },
    
            on_click_action: function( info) {
                env.callbacks.consulta_documento( info.id);
            },
            
            on_click_autocomplete : function() {
                env.callbacks.consulta_documento( env.idItem);
            },
            
            consulta_documento: function( id) {
                server.data.get( 'documento/firmado/' + id, function(resp) {
                    let item = resp.item;
                    let folio = item.folio;
                    let encodedName = encodeURIComponent( item.nombre);
                    
                    navigation.goto( 'validacionDocumento?folio='+ folio + '&nombre=' + encodedName);
                });
            }
        };
        
        var componente = {
            documento: {
                obtener_pagina: function ( pagina) {
                    env.tabla.mostrarPagina( pagina);
                }
            }
        };
        
	</script>
    
</head>
<body>
	
    <table class="container" <%= (usuario != null)? "hidden" : ""%>>
		<tr>
            <td style="text-align: center;">
 				<img src="./images/logo_organizacion.png" style="height: 150px;" alt="organization logo"/>
			</td>
		</tr>
        
        <tr><td>&nbsp;</td></tr>
        
        <tr>
            <td style="padding:10px 50px;">
                <span style="font-family: Verdadana, sans-serif; font-size: 0.9em; text-align: justify;">
                    <b>¡Bienvenido!</b>. Para consultar los documentos que usted ha firmado, por favor, ingrese 
                    la siguiente información.
                </span>
            </td>
        </tr>
        
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td>
                <form action="consultaDocumentos" method="POST" enctype="multipart/form-data" accept-charset="UTF-8">
                    <table style="width: 100%;">
                        <tr>
                            <td>
                                <span class="prompt">Certificado (*):</span>
                            </td>
        					<td>
                                <input type="file" id="certificado" name="certificado" class="form-control"
                                    accept=".cer, application/pkix-cert" title="Certificado FIEL"
                                />
                            </td>
                    	</tr>
                    
                        <tr>
                            <td>&nbsp;</td>
                            <td><span id="error_certificado" class="error" hidden></span> 
                        </tr>

                        <tr>
    						<td>
                                <span class="prompt">Llave Privada (*):</span>
                            </td>
                			<td>
                                <input type="file" id="llavePrivada" name="llavePrivada" class="form-control"
                                    accept=".key, application/pkcs8" title="Llave privada del certificado"
                                />
                            </td>
                        </tr>
                        
                        <tr>
                            <td>&nbsp;</td>
                            <td><span id="error_llavePrivada" class="error" hidden></span> 
                        </tr>
        				
                        <tr>
    						<td>
                                <span class="prompt">Contraseña (*):</span>
                            </td>
                    		<td>
                                <input type="password" id="password" name="password" class="form-control col-sm-4"
                                    placeholder="contraseña FIEL" autocomplete="off"
                                    title="Contraseña FIEL" maxlength="20"
                                />
                            </td>
    					</tr>
                        
                        <tr>
                            <td colspan="2">
                				<table style="width: 100%">
                                	<tr>
                						<td width="60%">&nbsp;</td>
                						<td width="40%" style="text-align: right;">
                                            <button type="submit" class="btn btn-primary" onclick="javascript: return validarForm( event);">
                                                <i class="fas fa-pencil-square-o"></i>Aceptar
                                            </button>
                                            &nbsp;
                                            <button type="button" class="btn btn-info" onclick="javascript: navigation.goto( 'firmaDocumento');" title="Firmar Documento(s)">
                                                Cancelar
                                            </button>
                						</td>
                                        
                					</tr>
                				</table>
                            </td>
                        </tr>
        			</table>
                </form>
			</td>
		</tr>
		<tr><td>&nbsp;</td></tr>
	
		<tr id="mensajeResultado">
			<td>
				<table width="100%">
					<tr>
						<td style="text-align: center; width: 100%;" >
                            <% if (errorMessages != null) { %>
                                <% for (String message : errorMessages) { %>
                                    <span style="font-size: 1.2em; font-weight: bold; color: red;"><%= message %></span><br/>
                                <% } %>
                            <% } %>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
                        
    <table class="container" <%= (usuario == null)? "hidden" : ""%>>
		<tr>
            <td style="text-align: center;">
 				<img src="./images/logo_organizacion.png" style="height: 150px;" alt="organization logo"/>
			</td>
		</tr>
        
        <tr><td>&nbsp;</td></tr>
        
        <tr>
			<td>
				<navbar class="navbar navbar-light bg-light">
                    <a class="navbar-brand" href="#">&nbsp;</a>
                    
					<button type="button" class="btn btn-sm btn-link" onclick="javascript: navigation.goto( 'firmaDocumento');" title="Firmar Documento(s)">
                        <i class="fas fa-file-signature"></i>&nbsp;Firmar Documento(s)
                    </button>
                            
                    <button type="button" class="btn btn-sm btn-link" onclick="javascript: navigation.logout();" title="Salir (logout)">
                        <i class="fas fa-sign-out-alt"></i>&nbsp;Salir
                    </button>
                </navbar>
			</td>
		</tr>
        
        <tr><td>&nbsp;</td></tr>
        
        <tr>
            <td style="padding:10px 50px;">
                <span style="font-family: Verdadana, sans-serif; font-size: 0.9em; text-align: justify;">
                    <b>Usuario: </b><%= (usuario != null)? usuario.getNombre() : "" %>
                    (<i><%= (usuario != null)? usuario.getClave() : "" %></i>)
                </span>
            </td>
        </tr>
        
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td>
                <div id="list_documento" class="card">
                    <div class="card-header" style="color: #0056b3;">
                        Documentos Firmados
                        <div class="float-right col-sm-6" id="container_autoComplete_documento"></div>
                    </div>
                    <div class="card-body" style="padding: 12px;">
                        <table class="table table-striped table-hover">
                            <thead id="header_documento" class="thead-dark"></thead>
                            <tbody id="page_documento"></tbody>
                            <tfoot id="footer_documento"></tfoot>
                        </table>
                    </div>
                </div>
            </td>
        </tr>
    </table>
    
    <div class="modal fade" id="processing" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Autenticación de Usuario</h5>
                </div>
                <div class="modal-body">
                    Su petición esta siendo procesada. Por favor espere.
                </div>
            </div>
        </div>
    </div>
    
    <a id="download" href=""></a>
    
    <div id="componente_autocompletion"></div>
    <div id="componente_tabla"></div>
</body>
</html>