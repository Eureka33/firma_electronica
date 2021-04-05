<%@page import="java.net.URLEncoder"%>
<%@page import="com.meve.ofspapel.firma.digital.core.entidades.Usuario"%>

<%@page language="java" contentType="text/html; charset=UTF-8" %>

<%
    final String[] errorMessages = (String[]) session.getAttribute( "errorMessages");
    session.removeAttribute( "errorMessages");
    
    final Usuario usuario = (Usuario) session.getAttribute( "usuario");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <title>Consulta de Documentos Firmados</title>
    
    <%@include file="meta_no_cache.jspf"%>  
    
    <link rel="stylesheet" href="./libs/fontawesome/css/all.min.css"    />
    <link rel="stylesheet" href="./libs/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="./libs/scripts/estilos.css?rnd=<%= Math.random()%>"/>
   
    <script src="./libs/jquery/jquery.min.js"             ></script>
    <script src="./libs/bootstrap/js/bootstrap.min.js"    ></script>
    <script src="./libs/bootstrap/js/popper.min.js"       ></script>
    <script src="./libs//handlebars/handlebars.js"        ></script>
    <script src="./libs/fontawesome/js/fontawesome.min.js"></script>
    <script src="./libs/jquery/is.min.js"></script>
    
    <script src="./libs/scripts/eureka33/eurk-core.min.js?rnd=<%= Math.random()%>"></script>
    <script src="./libs/scripts/util_functions.js?rnd=<%= Math.random()%>"        ></script>
    <script src="./libs/scripts/consulta_documentos.js?rnd=<%= Math.random()%>"   ></script>
    
	<script>	
        jQuery.noConflict();
        
        jQuery( document).ready( function( $) {
            if( is.ie()) {
                navigation.goto( 'pages/incompatible_browser.html');
                
            } else {
               componente.init( {usuarioAutenticado: <%= usuario != null %>, showErrors: <%= errorMessages != null%>});
            }
        });
		
	</script>
    
</head>
<body>
    <div class="container" style="text-align: center;">
		<img src="./images/logo_organizacion.png" style="height: 150px;" alt="organization logo"/>
    </div>
    
    <table class="container" <%= (usuario != null)? "hidden" : ""%>>
		<tr>
            <td>
                <navbar class="navbar navbar-light bg-light" style="padding:10px 50px;">
                    <span style="font-family: Verdadana, sans-serif; font-size: 0.9em; text-align: justify;">
                        <b>¡Bienvenido!</b>. Para consultar los documentos que usted ha firmado o solicitado firmar, 
                        por favor, ingrese la siguiente información:
                    </span>
                </navbar>
            </td>
        </tr>
       
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
                                <span id="error_certificado" class="error" hidden></span>
                            </td>
                    	</tr>
                        
                        <tr>
    						<td>
                                <span class="prompt">Llave Privada (*):</span>
                            </td>
                			<td>
                                <input type="file" id="llavePrivada" name="llavePrivada" class="form-control"
                                    accept=".key, application/pkcs8" title="Llave privada del certificado"
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
                                    title="Contraseña FIEL" maxlength="20"
                                />
                                <span id="error_password" class="error" hidden></span>
                            </td>
    					</tr>
                        
                        <tr>
                            <td colspan="2">
                				<table style="width: 100%">
                                	<tr>
                						<td width="60%">&nbsp;</td>
                						<td width="40%" style="text-align: right;">
                                            <button type="submit" class="btn btn-primary" onclick="javascript: return componente.validar( event);">
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
        
        <tr>
            <td style="padding:10px 50px;">
                <span style="font-family: Verdadana, sans-serif; font-size: 0.9em; text-align: justify;">
                    <b>Usuario: </b><%= (usuario != null)? usuario.getNombre() : "" %>
                    (<i><%= (usuario != null)? usuario.getClave() : "" %></i>)
                </span>
            </td>
        </tr>
        
		<tr>
			<td>
                <div class="card">
                    <div class="card-header" style="color: #0056b3;">
                        <div class="row">
                            <div class="col-sm-10">Documentos Firmados</div>
                            <div class="col-sm-1"><span class="badge badge-info" id="badgeDocumento">0</span></div>
                            <div class="col-sm-1">
                                <a href="#" class="toggle" data-toggle="0" data-target="documento">
                                    <i class="fas fa-chevron-circle-down"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div id="list_documento" class="card toggleable" hidden>
                    <div class="row" style="margin-top: 5px;">
                        <div class="col-sm-1"></div>
                        <div class="float-right col-sm-8" id="container_autoComplete_documento"></div>
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
        
        <tr>
            <td>
                <div class="card">
                    <div class="card-header" style="color: #0056b3;">
                        <div class="row">
                            <div class="col-sm-10">Solicitudes de Firma</div>
                            <div class="col-sm-1"><span class="badge badge-info" id="badgeSolicitud">0</span></div>
                            <div class="col-sm-1">
                                <a href="#" class="toggle" data-toggle="0" data-target="solicitud">
                                    <i class="fas fa-chevron-circle-down"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="list_solicitud" class="card toggleable" hidden>
                    <div class="row" style="margin-top: 5px;">
                        <div class="col-sm-1"></div>
                        <div class="float-right col-sm-8" id="container_autoComplete_solicitud"></div>
                    </div>
                    <div class="card-body" style="padding: 12px;">
                        <table class="table table-striped table-hover">
                            <thead id="header_solicitud" class="thead-dark"></thead>
                            <tbody id="page_solicitud"></tbody>
                            <tfoot id="footer_solicitud"></tfoot>
                        </table>
                    </div>
                </div>
            </td>
        </tr>
    </table>
                
    <br/>
    <br/>
    
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