<%@page import="com.meve.ofspapel.firma.digital.core.entidades.Usuario"%>

<%@page language="java" contentType="text/html; charset=UTF-8" %>

<%
    String[] errorMessages = (String[]) session.getAttribute( "errorMessages");
    session.removeAttribute( "errorMessages");
    
    final Usuario usuario = (Usuario) session.getAttribute( "usuario");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
    
    <title>Firma y Resguardo de Documentos Digitales</title>
    
    <link rel="stylesheet" href="./libs/fontawesome/css/all.min.css"    />
    <link rel="stylesheet" href="./libs/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="./libs/scripts/estilos.css"            />
    
    <script src="./libs/jquery/jquery.min.js"             ></script>
    <script src="./libs/bootstrap/js/bootstrap.min.js"    ></script>
    <script src="./libs/fontawesome/js/fontawesome.min.js"></script>
    <script src="./libs/jquery/is.min.js"                 ></script>
    <script src="./libs/scripts/util_functions.js?rnd=<%= Math.random()%>"></script>
 
    <script>
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
            jQuery( "#idOperacion1, #idOperacion2").change( activar_continuar);
            jQuery( "#btnPaso1").click( mostrar_paso2);
		}
        
        function validarForm( event) {
            event.stopPropagation;
                        
            let errores = 0;
            
            errores += validation.validarArchivos(    'documento', '.pdf',     'Documento', 100, 25);
            errores += validation.validarArchivos(  'certificado', '.cer',   'Certificado',   1, 1);
            errores += validation.validarArchivos( 'llavePrivada', '.key', 'Llave Privada',   1, 1);
            
            errores += validation.validarTexto( 'password',         'Contraseña',  20);
            
            let idOperacion = parseInt( jQuery( "input[name='idOperacion']:checked").val(), 10);
            
            if( idOperacion === 1) {
                let error = validation.validarTexto( 'correoDestinatario', 'Correo del Destinatario', 150);
                error = (error === 0)? validation.validarCorreo( 'correoDestinatario', 'Correo del Destinatario', 150) : error;
                error = (error === 0)? validation.validarDistinto( 'correoDestinatario', 'correo', 'Mi Correo Electrónico') : error;
                errores += error;
            }
            
            errores += validation.validarCorreo(  'correo', 'Correo Electrónico', 150);
            
            let submit = (errores === 0);
            
            if( submit) {
                setTimeout( function() { jQuery( '#processing').modal( 'show');}, 0);
            }
            
            return submit;
        }
        
        function activar_continuar( event) {
            jQuery( "#btnPaso1").attr( "disabled", false);
        }
        
        function mostrar_paso2( event) {
            jQuery( "#paso1").hide();
            
            let idOperacion = parseInt( jQuery( "input[name='idOperacion']:checked").val(), 10);
            
            if( idOperacion === 0) {
                jQuery( "#titulo").html( "Firma de Documento");
                hide( "rowDestinatario");
                jQuery( "#btnSubmit").html( '<i class="fas fa-pencil-alt"></i> FirmarDocumento');
                show( "footer");
            } else {
                jQuery( "#titulo").html( "Solicitud de Firma de Documento");
                show( "rowDestinatario");
                jQuery( "#btnSubmit").html( '<i class="fas fa-envelope"></i> Enviar Solicitud');
                hide( "footer");
            }
            
            jQuery( "#paso2").attr( "hidden", false);
        }
        
        function hide( id) {
            let dom = jQuery( "#" + id);
            dom.hide();
            dom.attr( 'hidden', true);
        }
        
        function show( id) {
            let dom = jQuery( "#" + id);
            dom.show();
            dom.attr( 'hidden', false);
        }
	</script>    
</head>
<body>
	
    <table class="container">
		<tr>
            <td style="text-align: center;">
 				<img src="./images/logo_organizacion.png" style="height: 150px; width: 100%;" alt="organization logo"/>
			</td>
		</tr>
        
        <tr>
			<td>
				<navbar class="navbar navbar-light bg-light">
                    <a class="navbar-brand" href="#">&nbsp;</a>
                    
                    <button type="button" class="btn btn-sm btn-link" onclick="javascript: navigation.goto( 'consultaDocumentos');" title="Mis documentos firmados">
                        <i class="fas fa-list"></i>&nbsp;Mis Documentos
                    </button>
                            
                    <% if ( usuario != null) { %>
                        <button type="button" class="btn btn-sm btn-link" onclick="javascript: navigation.logout();" title="Salir (logout)">
                            <i class="fas fa-sign-out-alt"></i>&nbsp:Salir
                        </button>
                    <% } %>
                </navbar>
			</td>
		</tr>
		
		<tr>
			<td>
                <form action="firmaDocumento" method="POST" enctype="multipart/form-data" accept-charset="UTF-8">
                    
                    <div class="card">
                        <div class="card-header">
                            <span id="titulo">Selecci&oacute;n de Operaci&oacute;n</span>
                        </div>
                        <div class="card-body" id="paso1">
                            <h5 class="card-title">¿Como desea firmar el documento?</h5>
                            
                            <div class="row">
                                <div class="col-sm-2" style="text-align: right;">
                                    <input type="radio" id="idOperacion1" name="idOperacion" value="0"/>
                                </div>
                                <div class="col-sm-10">
                                    <p class="card-text">
                                        <label for="idOperacion1">Firmaré el documento con mi propia FIEL</label>
                                    </p>
                                </div>
                                <div class="col-sm-2" style="text-align: right;">
                                    <input type="radio" id="idOperacion2" name="idOperacion" value="1"/>
                                </div>
                                <div class="col-sm-10">
                                    <p class="card-text">
                                        <label for="idOperacion2">Solicitaré a alguien más que firme el documento</label>
                                    </p>
                                </div>
                                <div class="col-sm-12" style="text-align: right;">
                                    <button type="button" id="btnPaso1" class="btn btn-primary" disabled>Continuar</button>
                                </div>
                            </div>
                        </div>
                    
                        <div class="card-body" id="paso2" hidden>
                            <table style="width: 100%;">
                                <tr>
                                    <td>
                                        <span class="prompt">Documento (*):</span>
                                    </td>
                            		<td>
                                        <input type="file" id="documento" name="documento" class="form-control" multiple
                                            accept="application/pdf" title="Documento que desea firmar"
                                        />
                                        <span id="error_documento" class="error" hidden></span>
                                    </td>
                            	</tr>
                      
                                <tr>
                                    <td>
                                        <span class="prompt">Certificado (*):</span>
                                    </td>
                                    <td>
                                        <input type="file" id="certificado" name="certificado" class="form-control"
                                            accept=".cer, application/pkix-cert" title="Archivo de su certificado FIEL"
                                        />
                                        <span id="error_certificado" class="error" hidden></span>
                                    </td>
                            	</tr>
                                
                                <tr>
                					<td>
                                        <span class="prompt">Llave Privada (*):</span>
                                    </td>
                        			<td>
                                        <input type="file" id="llavePrivada" name="llavePrivada" class="form-control"
                                            accept=".key, application/pkcs8" title="Archivo de llave privada de su certificado FIEL"
                                        />
                                        <span id="error_llavePrivada" class="error" hidden></span>
                                    </td>
                                </tr>
                            
                                <tr>
                					<td>
                                        <span class="prompt">Contraseña (*):</span>
                                    </td>
                            		<td>
                                        <input type="password" id="password" name="password" class="form-control col-sm-4"
                                            placeholder="contraseña FIEL" autocomplete="off"
                                            title="Contraseña de su certificado FIEL" maxlength="20"
                                        />
                                        <span id="error_password" class="error" hidden></span>
                                    </td>
                        		</tr>
                                
                                <tr id="rowDestinatario" hidden>
            						<td>
                                        <span class="prompt">Correo Destinatario:</span>
                                    </td>
                                	<td>
                                        <input type="text" id="correoDestinatario" name="correoDestinatario" class="form-control col-sm-8"
                                            title="Correo electrónico del destinatario de la solicitud"
                                        />
                                        <span id="error_correoDestinatario" class="error" hidden></span>
                                    </td>
                                </tr>
                                
                                <tr>
            						<td>
                                        <span class="prompt">Mi Correo Electr&oacute;nico:</span>
                                    </td>
                                	<td>
                                        <input type="text" id="correo" name="correo" class="form-control col-sm-8"
                                            title="Correo electrónico a donde le enviaremos la información para consultar el archivo firmado"
                                        />
                                        <span id="error_correo" class="error" hidden></span>
                                    </td>
                                </tr>
                            </table>
                            
                            <div class="row">
                                <div class="col-sm-12" style="text-align: right;">
                                    <button type="submit" id="btnSubmit" class="btn btn-primary firma" onclick="javascript: return validarForm( event);"></button>
                                    &nbsp;
                                    <button type="button" class="btn btn-info" onclick="javascript: navigation.goto( 'firmaDocumento');" title="Cancelar Operación">
                                        Cancelar
                                    </button>
                                </div>
                            </div>
                            
                            <div id="footer" style="padding: 25px 50px;" hidden>
                                <span style="font-family: Verdadana, sans-serif; font-size: 0.9em; text-align: justify;">
                                    <b>Importante</b>. El documento firmado puede ser validado haciendo uso del c&oacute;digo QR o 
                                    por medio de la liga que se encuentra dentro del documento firmado en la secci&oacute;n 
                                    &quot;Trazabilidad&quot;.
                                </span>
                            </div>
                            
                        </div>
                    </div>
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
                        
    <div class="modal fade" id="processing" tabindex="-1" role="dialog" aria-hidden="true">
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
    
    <a id="download" href=""></a>
</body>
</html>