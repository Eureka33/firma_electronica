<%@page import="mx.eureka.firma.digital.bean.BeanInfoDocumento"%>

<%@ page language="java" contentType="text/html"%>

<%
    String[] errorMessages = (String[]) session.getAttribute( "errorMessages");
    session.removeAttribute( "errorMessages");
%>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
	<script type="text/javascript">	
        jQuery.noConflict();
        
        jQuery( document).ready( function( $) {
            inicializa();
        });
        
		function inicializa() {
			document.getElementById( 'mensajeResultado').style.display= "<%= (errorMessages == null)? "none" : "" %>";
		}
        
        function validarForm( event) {
            event.stopPropagation;
            
            let errores = 0;
            
            errores += validarArchivo(    'documento', '.pdf',     'Documento', 20);
            errores += validarArchivo(  'certificado', '.cer',   'Certificado',  1);
            errores += validarArchivo( 'llavePrivada', '.key', 'Llave Privada',  1);
            
            errores += validarTexto( 'password',         'Contraseña',  20);
            errores += validarCorreo(  'correo', 'Correo Electrónico', 150);
            
            return errores === 0;
        }
                
        function validarArchivo( id, extension, nombre, maxSizeMB) {
            let errorId = 'error_' + id;
            
            hide( errorId);
            
            let files = jQuery("#" + id + ":eq(0)")[0].files;
            if ( files.length === 0) {
                update( errorId, "El archivo de " + nombre + " es requerido");
                return 1;
            }
            
            if( !files[0].name.toLowerCase().endsWith( extension)) {
                update( errorId, "La extensión del archivo deber ser (" + extension + ")");
                return 1;
            }
            
            if ( files[0].size > (maxSizeMB*1024*1024)) {
                update( errorId, "El tamaño del archivo no debe exceder " + maxSizeMB + " MB");
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
 				<img src="./images/logo_organizacion.jpg" style="height: 150px;" />
			</td>
		</tr>
        
        <tr><td>&nbsp;</td></tr>
	
		<tr>
			<td>
                <form action="firmaDocumento" method="POST" enctype="multipart/form-data">
                    <table style="width: 100%;">
                        <tr>
                            <td>
                                <span class="prompt">Documento (*):</span>
                            </td>
        					<td>
                                <input type="file" id="documento" name="documento" class="form-control"
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
                                    placeholder="Ingrese contraseña FIEL"
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
</body>
</html>