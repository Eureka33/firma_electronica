<%@page import="mx.eureka.firma.digital.bean.AppContext"%>
<%@page import="com.meve.ofspapel.firma.digital.core.service.IConfiguracionService"%>
<%@page import="com.meve.ofspapel.firma.digital.beans.DocumentoFirmado"%>
<%@page import="com.meve.ofspapel.firma.digital.core.entidades.Usuario"%>
<%@page import="java.net.URLEncoder"%>

<%@page language="java" contentType="text/html; charset=UTF-8" %>

<%
	DocumentoFirmado info = (DocumentoFirmado) request.getAttribute( "info");
    String resultado = (String) request.getAttribute( "resultado");
    
    final IConfiguracionService configService = AppContext.getBean( IConfiguracionService.class);
        
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
            jQuery( "#divPreview").hide();
		}
		
		function descargar( isSolicitud) {
			var link = document.getElementById( 'download');
            
            if ( isSolicitud) {
                <% if ( info.getSolicitud() != null) { %>
                    link.href= './descargaDocumento?isUpload=true&folio=<%= info.getSolicitud().getFolio() %>&nombre=<%= URLEncoder.encode( info.getNombre(), "UTF-8") %>';
                <% } %>
            } else {
                link.href= './descargaDocumento?folio=<%= info.getFolio() %>&nombre=<%= URLEncoder.encode( info.getNombre(), "UTF-8") %>';
            }
            
            link.click();
		}
        
        function preview_documento( isSolicitud) {
            let div = jQuery( '#divPreview');
            let loaded = parseInt( div.data( 'loaded'), 10);
            
            if( loaded === 0) {
                let item = document.getElementById( 'preview');
                
                if ( isSolicitud) {
                    <% if ( info.getSolicitud() != null) { %>
                        item.src= './descargaDocumento?isUpload=true&folio=<%= info.getSolicitud().getFolio() %>&nombre=<%= URLEncoder.encode( info.getNombre(), "UTF-8") %>&inline=true';
                    <% } %>        
                } else { 
                    item.src= './descargaDocumento?folio=<%= info.getFolio() %>&nombre=<%= URLEncoder.encode( info.getNombre(), "UTF-8") %>&inline=true';
                }
                
                div.data( 'loaded', 1);
            }
            
            div.show();
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

    <div class="container" style="margin-top: 10px; margin-bottom: 15px;">
        <form>
            
            <div class="card">
                <div class="card-header" style="color: #0056b3;">
                    Documento Firmado
                </div>
                
                <div class="card-body" style="padding: 12px;">
                
                    <table style="width: 100%">
                        <tr>
                            <td style="width: 250px;">
                                <span style="font-size: 1.2em; font-weight: bold;">Nombre Documento:</span>
                            </td>
                            <td>
                                <div class="row">
                                        <div class="col-sm-8">
                                        <input type="text" value="<%= info.getNombre() %>" style="font-size: 1.2em; width: 100%" disabled/>
                                    </div>
                                    <div class="col-sm-4" style="text-align: right;">
                                        <%  boolean isSolicitud = info.getSolicitud() != null && !info.getSolicitud().getEstatus().equals( "FIRMADA"); %>
                                        <% if ( info.getSolicitud() == null || !info.getSolicitud().getEstatus().equals( "CANCELADA")) { %>
                                   
                                            <button type="button" class="btn btn-info" 
                                                title="Descargar documento" onclick="javascript: descargar( <%= isSolicitud %>);"
                                            >
                                                <i class="fas fa-download"></i>
                                            </button>
                                        
                                            <button type="button" class="btn btn-info" 
                                                title="Previsualizar documento" onclick="javascript: preview_documento( <%= isSolicitud %>);"
                                            >
                                                <i class="fas fa-eye"></i>
                                            </button>
                                        <% } %>
                                    </div>
                                </div>
                            </td>
                		</tr>
                
                        <tr id="divPreview" data-loaded="0">
                            <td></td>
                            <td>
                                <div style="text-align: center;">
                                    <embed id="preview" style="width: 100%; height: 450px;"/>
                                    <!--        
                                    <iframe style="width: 100%; height: 450px; border: none;"></iframe>
                                    -->
                                    <button type="button" class="btn btn-info" style="width: 80%;"  onclick="javascript: jQuery( '#divPreview').hide();">
                                        Ocultar
                                    </button>
                                </div>
                            </td>
                        </tr>
                        
                        <% if( info.getSolicitud() == null || info.getSolicitud().getEstatus().equals( "FIRMADA")) { %>
                            <tr>
                                <td style="width: 250px;">
                                    <span style="font-size: 1.2em; font-weight: bold;">
                                        Firmante:
                                    </span>
                                </td>
                    			<td>
                                    <input type="text" value="<%= info.getFirmante() != null? info.getFirmante() : "" %>" style="font-size: 1.2em; width: 100%" disabled/>
                                </td>
                            </tr>
                        <% } %>
                   
                        <% if ( info.getId() != null) { %>
                            <tr>
                                <td style="width: 250px;">
                                    <span style="font-size: 1.2em; font-weight: bold;">Fecha de Firma:</span>
                                </td>
                				<td>
                                    <input type="text" value="<%= info.getFechaHora() %>" style="font-size: 1.2em; width: 100%" disabled/>
                                </td>
                            </tr>                    
					
                			<tr>
                        		<td style="width: 250px;">
                                    <span style="font-size: 1.2em; font-weight: bold;">Folio de Firma:</span>
                                </td>
                				<td>
                                    <input type="text" value="<%= info.getFolio() %>" style="font-size: 1.2em; width: 100%" disabled/>
                                </td>
                            </tr>
                        <% } %>
				
        			</table>
                </div>
            </div>
            
            <% if( info.getSolicitud() != null) { %>
                <div class="card">
                    <div class="card-header" style="color: #0056b3;">
                        Solicitud
                    </div>
                    <div class="card-body" style="padding: 12px;">
                        <table style="width: 100%">
                            <tr>
                                <td style="width: 250px;">
                                    <span style="font-size: 1.2em; font-weight: bold;">Folio Solicitud:</span>
                                </td>
                                <td>
                                    <%  String formato = configService.getPropiedad( "string.formato.solicitud");   %>
                                    <input type="text" value="<%= String.format( formato, info.getSolicitud().getFechaHora().substring(6, 10), info.getSolicitud().getId())%>" style="font-size: 1.2em; width: 100%" disabled/>
                                </td>
                            </tr>
                                
                            <tr>
                                <td style="width: 250px;">
                                    <span style="font-size: 1.2em; font-weight: bold;">Fecha Hora de Solicitud:</span>
                                </td>
                            	<td>
                                    <input type="text" value="<%= info.getSolicitud().getFechaHora()%>" style="font-size: 1.2em; width: 100%" disabled/>
                                </td>
                            </tr>
                            
                            <tr>
                                <td style="width: 250px;">
                                    <span style="font-size: 1.2em; font-weight: bold;">Destinatario:</span>
                                </td>
                            	<td>
                                    <input type="text" value="<%= info.getSolicitud() == null? "" : info.getSolicitud().getSolicitante()%>" style="font-size: 1.2em; width: 100%" disabled/>
                                </td>
                            </tr>
                            
                            <tr>
                                <td style="width: 250px;">
                                    <span style="font-size: 1.2em; font-weight: bold;">Estatus:</span>
                                </td>
                                <td>
                                    <input type="text" value="<%= info.getSolicitud().getEstatus() %>" style="font-size: 1.2em; width: 100%" disabled/>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
            <% } %>
            
        </form>
        
        <div class="row text-right">
            <div class="col-sm-12 text-right">
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

    <% if ( info.getId() != null) { %>
        <div class="container" style="margin-top: 15px;">
            <div class="alert alert-info" role="alert">
                <span style="font-size: 1.2em;">
                    Si desea validar la integridad del documento pulse 
                    <a onclick="javascript: mostrarFormulario();" style="text-decoration: underline;">
                        <b>aqu&iacute;</b>
                    </a>
                </span>
            </div>
        </div>
    <% } %>
    
    <div class="container" style="margin-top: 15px;" id="formValidacion" style="display: none;">
        <div class="card">
            <%
                String query = "folio=" + info.getFolio() + "&nombre=" + URLEncoder.encode( info.getNombre(), "UTF-8");
            %>
            
            <form action="validacionDocumento?<%= query %>" method="post" enctype="multipart/form-data">
                <table style="margin: 20px; width: 95%;">
                    <tr>
                        <td>
                            <div class="alert alert-success" role="alert">
                                <span style="font-size: 1.2em;">1. Seleccione el documento para validar</span>
                            </div>
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
                        <td>
                            <div class="alert alert-success" role="alert">
                                <span style="font-size: 1.2em;">2. Pulse el botón para iniciar la validación</span>
                            </div>
                            <div style="text-align: right;">
                                <button type="submit" class="btn btn-info" onclick="javascript: return validar(event);">
                                    <i class="fas fa-spell-check"></i>&nbsp;Validar
                                </button>
                            </div>
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