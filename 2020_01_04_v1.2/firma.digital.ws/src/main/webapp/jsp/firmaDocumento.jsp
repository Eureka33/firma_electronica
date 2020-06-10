<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>

<%@page import="mx.eureka.firma.digital.bean.BeanInfoDocumento"%>

<%
    String[] errorMessages = (String[]) session.getAttribute( "errorMessages");
    session.removeAttribute( "errorMessages");
%>

<html lang="es">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
    
    <title>Firma y Resguardo de Documentos Digitales</title>
    
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
            
            errores += validarArchivos(    'documento', '.pdf',     'Documento', 100, 25);
            errores += validarArchivos(  'certificado', '.cer',   'Certificado',   1, 1);
            errores += validarArchivos( 'llavePrivada', '.key', 'Llave Privada',   1, 1);
            
            errores += validarTexto( 'password',         'Contraseña',  20);
            errores += validarCorreo(  'correo', 'Correo Electrónico', 150);
            
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
            for ( file of files) {
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
            for( file of files) {
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
        
	</script>
    
</head>
<body>
	
    <table class="container">
		<tr>
            <td style="text-align: center;">
 				<img src="./images/logo_organizacion.png" style="height: 150px;" alt="organization logo"/>
			</td>
		</tr>
        
        <tr><td>&nbsp;</td></tr>
	
		<tr>
			<td>
                <form action="firmaDocumento" method="POST" enctype="multipart/form-data" accept-charset="UTF-8">
                    <table style="width: 100%;">
                        <tr>
                            <td>
                                <span class="prompt">Documento (*):</span>
                            </td>
        					<td>
                                <input type="file" id="documento" name="documento" class="form-control" multiple
                                    accept="application/pdf" title="Documento que desea firmar"
                                />
                            </td>
                    	</tr>
                        
    					<tr>
                            <td>&nbsp;</td>
                            <td><span id="error_documento" class="error" hidden></span> 
                        </tr>

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
                                    placeholder="contraseña FIEL"
                                    title="Contraseña FIEL" maxlength="20"
                                />
                            </td>
    					</tr>
                        
                        <tr>
                            <td>&nbsp;</td>
                            <td><span id="error_password" class="error" hidden></span> 
                        </tr>
                        
                        <tr>
    						<td>
                                <span class="prompt">Correo Electr&oacute;nico:</span>
                            </td>
                    		<td>
                                <input type="text" id="correo" name="correo" class="form-control col-sm-8">
                            </td>
    					</tr>
                        
                        <tr>
                            <td>&nbsp;</td>
                            <td><span id="error_correo" class="error" hidden></span> 
                        </tr>
                        
                        <tr>
                            <td colspan="2">
                				<table style="width: 100%">
                                	<tr>
                						<td width="60%">&nbsp;</td>
                						<td width="40%" style="text-align: right;">
                                            <button type="submit" class="btn btn-primary" onclick="javascript: return validarForm(event);">
                                                <i class="fas fa-pencil-square-o"></i>Firmar Documento
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
	
		<tr>
            <td style="padding:10px 50px;">
                <span style="font-family: Verdadana, sans-serif; font-size: 0.9em; text-align: justify;">
                    <b>Importante</b>. El documento firmado puede ser validado haciendo uso del c&oacute;digo QR o 
                    por medio de la liga que se encuentra dentro del documento firmado en la secci&oacute;n 
                    &quot;Trazabilidad&quot;.
                </span>
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
                        
    <div class="modal fade" id="processing" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Firma Digital</h5>
                </div>
                <div class="modal-body">
                    Su documento esta siendo procesado. Por favor espere.
                </div>
            </div>
        </div>
    </div>
</body>
</html>