<%@page import="com.meve.ofspapel.firma.digital.core.entidades.Usuario"%>
<%@page import="mx.eureka.firma.digital.bean.BeanInfoDocumento"%>
<%@page import="java.net.URLEncoder"%>

<%@page language="java" contentType="text/html; charset=UTF-8" %>

<%
	BeanInfoDocumento info = (BeanInfoDocumento) request.getAttribute( "info");
    String resultado = (String) request.getAttribute( "resultado");
    String error     = (String) request.getAttribute(     "error");
    
    final Usuario usuario = (Usuario) session.getAttribute( "usuario");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <title>Descarga y Firma de Documentos</title>
    
    <%@include file="meta_no_cache.jspf"%>
    
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
			document.getElementById( 'mensajeResultado').style.display="<%= (resultado == null && error == null)? "none": ""%>"; 
            
            jQuery( ".fiel").hide();
            jQuery( "#btnDownload").hide();
            jQuery( "#btnSign").hide();
            
            <% if( error == null) { %>
                jQuery( ".fiel").show();
                jQuery( "#btnDownload").show();
                jQuery( "#btnSign").show();
            <% } %>
        }
		
		function descargar() {
			var link = document.getElementById( 'download');
			link.href= './descargaDocumento?isUpload=true&folio=<%= info.getFolio() %>&nombre=<%= URLEncoder.encode( info.getNombre(), "UTF-8") %>';
			link.click();
		}
        
        function validarForm( event) {
            event.stopPropagation;
                        
            let errores = 0;
            
            errores += validation.validarArchivos(  'certificado', '.cer',   'Certificado',   1, 1);
            errores += validation.validarArchivos( 'llavePrivada', '.key', 'Llave Privada',   1, 1);
            errores += validation.validarTexto( 'password', 'Contraseña',  20);
            
            errores += validation.validarCorreo(  'correo', 'Correo Electrónico', 150);
            
            let submit = (errores === 0);
            
            if( submit) {
                setTimeout( function() { jQuery( '#processing').modal( 'show');}, 0);
            }
            
            return submit;
        }
	</script>
</head>
<body>
    <table class="container">
		<tr>
            <td>
 				<img src="./images/logo_organizacion.png" style="height: 150px; width: 100%;" alt="organization logo"/>
			</td>
		</tr>
        
        <tr>
			<td>
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
			</td>
		</tr>
		
		<tr>
			<td>
                <form action="solicitudDocumento?&folio=<%= info.getFolio() %>&nombre=<%= URLEncoder.encode( info.getNombre(), "UTF-8") %>"
                    method="POST" enctype="multipart/form-data" accept-charset="UTF-8"
                >
                    <div class="card">
                        <div class="card-header">
                            <span>Solicitud de Firma de Documento</span>
                        </div>
                        <div class="card-body" id="paso1">
                            <table style="width: 100%;">
                                <tr>
                                    <td style="width: 150px;">
                                        <span style="font-size: 1.2em; font-weight: bold;">Folio:</span>
                                    </td>
                                    <td>
                                        <input type="text" value="<%= info.getFolio() %>" style="font-size: 1.2em; width: 100%" disabled/>
                                    </td>
                                </tr>
					
                                <tr>
                                    <td style="width: 150px;">
                                        <span style="font-size: 1.2em; font-weight: bold;">Nombre:</span>
                                    </td>
                                    <td>
                                        <div class="row">
                                            <div class="col-sm-8">
                                                <input type="text" value="<%= info.getNombre() %>" style="font-size: 1.2em; width: 100%" disabled/>
                                            </div>
                                            <div class="col-sm-4" style="text-align: right;">
                                                <button type="button" id="btnDownload" class="btn btn-info" 
                                                    title="Descargar documento" onclick="javascript: descargar();"
                                                >
                                                    <i class="fas fa-download"></i>
                                                </button>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
		
                                <%@include file="../WEB-INF/pages/fragments/campos_fiel.jspf" %>
                            </table>
                    
                            <br/>
                    
                            <div class="row">
                               <div class="col-sm-12" style="text-align: right;">
                                    <button type="submit" id="btnSign" class="btn btn-primary" onclick="javascript: validarForm( event);">
                                        <i class="fas fa-file-signature"></i>&nbsp;Firmar Documento
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
			</td>
		</tr>
    
        <tr><td>&nbsp;</td></tr>
	
		<tr id="mensajeResultado" style="display: none;">
			<td>
				<table width="100%">
					<tr>
						<td style="text-align: center;" width="100%">
							<span style="font-size: 1.4em; font-weight: bold; color: red;">
                                <% if (resultado != null) { %><%= resultado %><% } %>
                                <% if (error != null) { %><%= error %><% } %>
                            </span>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		
	</table>
                        
    <%@include file="../WEB-INF/pages/fragments/processing.jspf" %>
	<a id="download" href=""></a>
</body>
</html>