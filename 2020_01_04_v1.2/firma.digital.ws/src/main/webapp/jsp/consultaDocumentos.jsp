<%@page import="java.net.URLEncoder"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.List"%>
<%@page import="com.meve.ofspapel.firma.digital.core.entidades.ArchivoDepositado"%>
<%@page import="com.meve.ofspapel.firma.digital.core.entidades.Usuario"%>

<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>

<%
    final String[] errorMessages = (String[]) session.getAttribute( "errorMessages");
    session.removeAttribute( "errorMessages");
    
    final Usuario usuario = (Usuario) session.getAttribute( "usuario");
    final List<ArchivoDepositado> archivos = (List) request.getAttribute( "archivos");
%>

<html lang="es">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
    
    <title>Consulta de Documentos Firmados</title>
    
    <link rel="stylesheet" href="./libs/fontawesome/css/all.min.css"     />
    <link rel="stylesheet" href="./libs/bootstrap/css/bootstrap.min.css" />
    
    <style type="text/css">
        .container {
            border: none; 
            width: 700px;
            margin-left: auto; 
            margin-right:auto;
        }
        
        .prompt {
            font-size: 1.2em;
            font-weight: bold;
        }
        
        .error {
            font-size: 1.0em;
            color: red;
        }
    </style>
    
    <script type="text/javascript" src="./libs/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="./libs/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="./libs/fontawesome/js/fontawesome.min.js"></script>
    <script type="text/javascript" src="./libs/jquery/is.min.js"></script>
    
	<script type="text/javascript">	
        jQuery.noConflict();
        
        jQuery( document).ready( function( $) {
            if( is.ie()) {
                window.location= './pages/incompatible_browser.html';
            } else {
                inicializa();
            } 
        });
        
		function inicializa() {
			document.getElementById( 'mensajeResultado').style.display= "<%= (errorMessages == null)? "none" : "" %>";
		}
        
        function validarForm( event) {
            event.stopPropagation;
                        
            let errores = 0;
            
            errores += validarArchivos(  'certificado', '.cer',   'Certificado',   1, 1);
            errores += validarArchivos( 'llavePrivada', '.key', 'Llave Privada',   1, 1);
            
            errores += validarTexto( 'password',         'Contraseña',  20);
            
            let submit = (errores === 0);
            
            if( submit) {
                setTimeout( function() { jQuery( '#processing').modal( 'show');}, 0);
            }
            
            return submit;
        }
        
        function validarArchivos( id, extension, nombre, maxItems, maxSizeMB) {
            let errorId = 'error_' + id;
            
            hide( errorId);
            
            let files = jQuery("#" + id + ":eq(0)")[0].files;
            
            if ( files.length === 0) {
                update( errorId, "El archivo de " + nombre + " es requerido");
                return 1;
            }
            
            if( noCumplenConExtension( files, extension) ) {
                update( errorId, "La extensión del archivo(s) deber ser (" + extension + ")");
                return 1;
            }
            
            if ( excedenMaxItems( files, maxItems)) {
                update( errorId, "El número del archivos no debe ser mayor a " + maxItems);
                return 1;
            }   
            
            if ( excedenMaxSizeMB( files, maxSizeMB)) {
                update( errorId, "El tamaño total del archivo(s) no debe exceder " + maxSizeMB + " MB");
                return 1;
            }            
            
            return 0;
        }
        
        function validarTexto( id, nombre, maxLength) {
            let errorId = 'error_' + id;
            
            hide( errorId);
            
            let value = jQuery("#" + id).val().trim();
            if ( value.length === 0) {
                update( errorId, "El campo " + nombre + " es requerido");
                return 1;
            }
            
            if ( value.length > maxLength) {
                update( errorId, "El campo " + nombre + " no debe exceder de " + maxLength + " caracteres");
                return 1;
            }
            
            return 0;
        }
        
        function validarCorreo( id, nombre, maxLength) {
            let errorId = 'error_' + id;
            
            hide( errorId);
            
            let value = jQuery("#" + id).val().trim();
            
            if( value.length === 0) {
                return 0;
            }
            
            if( !isValidEmail( value)) {
                update( errorId, "El " + nombre + " no tiene un formato valido valido");
                return 1;
            }
            
            if ( value.length > maxLength) {
                update( errorId, "El " + nombre + " no debe exceder de " + maxLength + " caracteres");
                return 1;
            }
            
            return 0;
        }
        
        function isValidEmail( value) {
            return /^\w+([\.\+\-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/.test( value);
        }
        
        function noCumplenConExtension( files, extension) {
            let error = false;
            let file;
            
            for( let i = 0; i < files.length; ++i) {
                file = files[i];
                if ( !file.name.toLowerCase().endsWith( extension)) {
                    error = true;
                }
            }
            
            return error;
        }
        
        function excedenMaxItems( files, maxItems) {
            return files.length > maxItems;
        }
        
        function excedenMaxSizeMB( files, maxSizeMB) {
            let total = 0;
            let file;
            
            for( let i = 0; i < files.length; ++i) {
                file = files[i];
                total = total + file.size;
            }

            return total > (maxSizeMB*1024*1024);
        }
        
        function hide( id) {
            jQuery( '#' + id).attr( 'hidden', true);
        }
        
        function update( id, text) {
            jQuery( '#' + id).text( text).attr( 'hidden', false);
        }
        
        function regresar() {
			var link = document.getElementById( 'download');
			link.href= './firmaDocumento';
			link.click();
		}
        
        function logout() {
			var link = document.getElementById( 'download');
			link.href= './logout';
			link.click();
		}
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
                                            <button type="button" class="btn btn-info" onclick="javascript: regresar();" title="Firmar Documento(s)">
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
                    
					<button type="button" class="btn btn-sm btn-link" onclick="javascript: regresar();" title="Firmar Documento(s)">
                        <i class="fas fa-file-signature"></i>&nbsp;Firmar Documento(s)
                    </button>
                            
                    <button type="button" class="btn btn-sm btn-link" onclick="javascript: logout();" title="Salir (logout)">
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
                <table class="table">
                    <thead class="header-dark">
                        <tr>
                            <th>Fecha Hora</th>
                            <th>Folio</th>
                            <th>Documento</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <%  if ( archivos == null) { %>
                            <tr>
                                <td colspan="4">No hay resultados que mostrar</td>
                            </tr>
                        <% } else { %>                          
                            <%
                                DateFormat formatter = new SimpleDateFormat ("dd/MM/yyyy HH:mm:ss");
                                for ( ArchivoDepositado archivo : archivos) {
                            %>
                                <tr>
                                    <td><%= formatter.format( archivo.getFechaHora())%></td>
                                    <td><%= archivo.getFolio()  %></td>
                                    <td><%= archivo.getNombre() %></td>
                                    <td>
                                        <a href="./validacionDocumento?folio=<%= archivo.getFolio()%>&nombre=<%= URLEncoder.encode( archivo.getNombre(), "UTF-8")%>"
                                           title="Descarga y validación del documento"
                                        >
                                            <i class="fas fa-download"></i>
                                        </a>
                                    </td>
                                </tr>
                            <% } %>
                        <% } %>
                    </tbody>
                </table>
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
</body>
</html>