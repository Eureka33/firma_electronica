<%@page import="com.meve.ofspapel.firma.digital.core.entidades.Usuario"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>

<%@page import="mx.eureka.firma.digital.bean.BeanInfoDocumento"%>
<%@page import="java.net.URLEncoder"%>

<%
	BeanInfoDocumento info = (BeanInfoDocumento) request.getAttribute( "info");
    String resultado = (String) request.getAttribute( "resultado");
    
    final Usuario usuario = (Usuario) session.getAttribute( "usuario");
%>

<html lang="es">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
    
    <title>Descarga y Validación de Documentos Firmados</title>
    
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
			document.getElementById( 'formValidacion').style.display='none';
			document.getElementById( 'mensajeResultado').style.display="<%= (resultado == null)? "none": ""%>"; 
		}
		
		function descargar() {
			var link = document.getElementById( 'download');
			link.href= './descargaDocumento?folio=<%= info.getFolio() %>&nombre=<%= URLEncoder.encode( info.getNombre(), "UTF-8") %>';
			link.click();
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
        
        function consultas() {
			var link = document.getElementById( 'download');
			link.href= './consultaDocumentos';
			link.click();
		}
	
		function mostrarFormulario() {
			var formulario = document.getElementById( 'formValidacion');
			formulario.style.display = '';
			
			var mensaje = document.getElementById( 'mensajeResultado');
			if ( mensaje) {	mensaje.style.display = 'none'; }
			
		} 
        
        function validar(event) {
             event.stopPropagation;
                        
            let errores = 0;
            
            errores += validarArchivos( 'archivo', '.pdf', 'Documento', 100, 25);
            
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
        
	</script>
    
</head>
<body>
	
    <table style="border: none; margin-left: auto; margin-right:auto;">
		<tr>
            <td>
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
                    
                    <% if ( usuario != null) { %>
                        <button type="button" class="btn btn-sm btn-link" onclick="javascript: consultas();" title="Mis documentos firmados">
                            <i class="fas fa-list"></i>&nbsp;Mis Documentos
                        </button>
                    
                        <button type="button" class="btn btn-sm btn-link" onclick="javascript: logout();" title="Salir (logout)">
                            <i class="fas fa-sign-out-alt"></i>&nbsp;Salir
                        </button>
                    <% } %>
                </navbar>
			</td>
		</tr>
		
		<tr><td>&nbsp;</td></tr>
        
		<tr>
			<td>
				<table style="width: 100%;">
					<tr>
						<td style="width: 150px;">
                            <span style="font-size: 1.2em; font-weight: bold;">Folio:</span>
                        </td>
						<td>
                            <input type="text" value="<%= info.getFolio() %>" style="font-size: 1.2em; width: 100%" disabled/>
                        </td>
					</tr>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr>
						<td style="width: 150px;">
                            <span style="font-size: 1.2em; font-weight: bold;">Nombre:</span>
                        </td>
						<td>
                            <input type="text" value="<%= info.getNombre() %>" style="font-size: 1.2em; width: 100%" disabled/>
                        </td>
					</tr>
				</table>
			</td>
		</tr>
		
		<tr><td>&nbsp;</td></tr>
		
		<tr>
			<td>
				<table style="width: 100%">
					<tr>
						<td width="60%">&nbsp;</td>
						<td width="20%" style="text-align: right;">
                            <button type="button" class="btn btn-primary" onclick="javascript: descargar();"><i class="fas fa-download"></i>&nbsp;Descargar</button>
							<a id="download" href=""></a>
						</td>
                        <td width="20%" style="text-align: right;" <%= info.getNombre().endsWith( ".zip")? "hidden": ""%>>
							<button type="button" class="btn btn-success" onclick="javascript: mostrarFormulario();"><i class="fas fa-clipboard-check"></i>&nbsp;Validar</button>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		
		<tr><td>&nbsp;</td></tr>
	
		<tr id="mensajeResultado">
			<td>
				<table width="100%">
					<tr>
						<td style="text-align: center;" width="100%">
							<span style="font-size: 1.4em; font-weight: bold; color: red;" ><%= resultado %></span>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		
		<tr id="formValidacion">
			<td>
                <div class="card">
                    <form action="validacionDocumento?&folio=<%= info.getFolio() %>&nombre=<%= URLEncoder.encode( info.getNombre(), "UTF-8") %>"
                        method="post" enctype="multipart/form-data"
                    >
                        <table style="margin: 20px; width: 95%;">
                            <tr>
                                <td>
                                    <span style="font-size: 1.2em;">Seleccione el documento para validar</span>
                                </td>
    						</tr>
                           <tr>
                                <td>
                                    <input type="file" id="archivo" name="archivo"/>
    							</td>
        					</tr>
            				
                            <tr>
                                <td><span id="error_archivo" class="error" hidden></span> 
                            </tr>
                            
                            <tr>
                                <td style="text-align: right;">
                                    <button type="submit" class="btn btn-info" onclick="javascript: return validar(event);">
                                        <i class="fas fa-spell-check"></i>&nbsp;Comparar
                                    </button>
        						</td>
            				</tr>
                		</table>
                    </form>
                </div>
           	</td>
		</tr>
	</table>
                        
    <div class="modal fade" id="processing" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Validación de Documento</h5>
                </div>
                <div class="modal-body">
                    Su documento esta siendo procesado. Por favor espere.
                </div>
            </div>
        </div>
    </div>
	
</body>
</html>