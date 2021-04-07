<%@page import="com.meve.ofspapel.firma.digital.beans.DocumentoSolicitado"%>
<%@page import="com.meve.ofspapel.firma.digital.core.entidades.Usuario"%>
<%@page import="mx.eureka.firma.digital.bean.BeanInfoDocumento"%>
<%@page import="java.net.URLEncoder"%>

<%@page language="java" contentType="text/html; charset=UTF-8" %>

<%
	DocumentoSolicitado info = (DocumentoSolicitado) request.getAttribute( "info");
    String resultado = (String) request.getAttribute( "resultado");
    
    final Usuario usuario = (Usuario) session.getAttribute( "usuario");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
    
    <title>Descarga y Validación de Documentos Firmados</title>
    
    <link rel="stylesheet" href="./libs/fontawesome/css/all.min.css"    />
    <link rel="stylesheet" href="./libs/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="./libs/scripts/estilos.css?rnd=<%= Math.random()%>"/>
    
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
			document.getElementById( 'formValidacion').style.display='none';
			document.getElementById( 'mensajeResultado').style.display="<%= (resultado == null)? "none": ""%>"; 
		}
		
		function descargar() {
			var link = document.getElementById( 'download');
            
            <% if ( info != null && info.getDocumentoFirmado() != null) { %>
            link.href= './descargaDocumento?folio=<%= info.getDocumentoFirmado().getFolio() %>&nombre=<%= URLEncoder.encode( info.getDocumentoFirmado().getNombre(), "UTF-8") %>';
            link.click();
            <% } %>
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
            
            errores += validation.validarArchivos( 'archivo', '.pdf', 'Documento', 100, 25);
            
            let submit = (errores === 0);
            
            if( submit) {
                setTimeout( function() { jQuery( '#processing').modal( 'show');}, 0);
            }
            
            return submit;
        } 
        
	</script>
    
</head>
<body>
	
    <div class="container" style="text-align: center;">
		<img src="./images/logo_organizacion.png" style="height: 150px;" alt="organization logo"/>
    </div>
    
    <div class="container" style="margin-top: 5px;">
		<navbar class="navbar navbar-light bg-light">
            <a class="navbar-brand" href="#">&nbsp;</a>
                    
            <button type="button" class="btn btn-sm btn-link" onclick="javascript: navigation.goto( 'firmaDocumento');" title="Firmar Documento(s)">
                <i class="fas fa-file-signature"></i>&nbsp;Firmar Documento(s)
            </button>
             
            <% if ( usuario != null) { %>
                <button type="button" class="btn btn-sm btn-link" onclick="javascript: navigation.goto( 'consultaDocumentos');" title="Mis documentos firmados">
                    <i class="fas fa-list"></i>&nbsp;Mis Documentos
                </button>
            
                <button type="button" class="btn btn-sm btn-link" onclick="javascript: navigation.logout();" title="Salir (logout)">
                    <i class="fas fa-sign-out-alt"></i>&nbsp;Salir
                </button>
            <% } %>
        </navbar>
    </div>

    <div class="container" style="margin-top: 5px;">
        <form>
            <table style="width: 100%">
                <tr>
    				<td style="width: 250px;">
                        <span style="font-size: 1.2em; font-weight: bold;">Fecha Hora de Solicitud:</span>
                    </td>
                	<td>
                        <input type="text" value="<%= info.getFechaHora() %>" style="font-size: 1.2em; width: 100%" disabled/>
                    </td>
                </tr>
					
				<tr>
					<td>
                        <span style="font-size: 1.2em; font-weight: bold;">Folio Solicitud:</span>
                    </td>
                    <td>
                        <input type="text" value="<%= info.getFolio() %>" style="font-size: 1.2em; width: 100%" disabled/>
                    </td>
				</tr>
                
                <tr>
                    <td>
                        <span style="font-size: 1.2em; font-weight: bold;">Nombre Documento:</span>
                    </td>
                    <td>
                        <input type="text" value="<%= info.getNombre() %>" style="font-size: 1.2em; width: 100%" disabled/>
                    </td>
				</tr>
                    
                <tr>
                    <td>
                        <span style="font-size: 1.2em; font-weight: bold;">Estatus:</span>
                    </td>
                    <td>
                        <input type="text" value="<%= info.getEstatus() %>" style="font-size: 1.2em; width: 100%" disabled/>
                    </td>
                </tr>
				
                <tr>
					<td>
                        <span style="font-size: 1.2em; font-weight: bold;">Destinatario:</span>
                    </td>
					<td>
                        <input type="text" value="<%= info.getDestinatario()%>" style="font-size: 1.2em; width: 100%" disabled/>
                    </td>
                </tr>
                   
                <% if ( info.getDocumentoFirmado() != null) { %>
                    
                    <tr>
						<td>
                            <span style="font-size: 1.2em; font-weight: bold;">Fecha de Firma:</span>
                        </td>
						<td>
                            <input type="text" value="<%= info.getDocumentoFirmado().getFechaHora() %>" style="font-size: 1.2em; width: 100%" disabled/>
                        </td>
                    </tr>                    
					
					<tr>
						<td>
                            <span style="font-size: 1.2em; font-weight: bold;">Folio de Firma:</span>
                        </td>
						<td>
                            <input type="text" value="<%= info.getDocumentoFirmado().getFolio() %>" style="font-size: 1.2em; width: 100%" disabled/>
                        </td>
					</tr>
                    
                <% } %>
				
			</table>
        </form>
        
        <div class="row text-right">
            <div class="col-sm-12 text-right">
                <% if ( info.getDocumentoFirmado() != null) { %>        
	                <button type="button" class="btn btn-primary" onclick="javascript: descargar();"><i class="fas fa-download"></i>&nbsp;Descargar</button>
                    &nbsp;
                    <button type="button" class="btn btn-success" onclick="javascript: mostrarFormulario();"><i class="fas fa-clipboard-check"></i>&nbsp;Validar</button>
                <% } %>
                &nbsp;
                <button type="button" class="btn btn-secondary" onclick="javascript: navigation.goto( 'consultaDocumentos');" title="Regresar">
                    <i class="fas fa-step-backward"></i>&nbsp;Regresar
                </button>
            </div>
        </div>
	</div>
    
	<div class="container" id="mensajeResultado" style="margin-top: 10px; display: none;">
        <div class="alert alert-danger text-center" role="alert">
            <span style="font-size: 1.4em; font-weight: bold;" ><%= resultado %></span>
        </div>
	</div>
    
    <div class="container" style="margin-top: 5px;" id="formValidacion" style="display: none;">
        <div class="card">
            <form action="validacionSolicitud?rand=<%= info.getId()%>-<%= Math.random() %>"
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
	</div>
                        
    <%@include file="../WEB-INF/pages/fragments/processing.jspf" %>
	<a id="download" href=""></a>
    
</body>
</html>