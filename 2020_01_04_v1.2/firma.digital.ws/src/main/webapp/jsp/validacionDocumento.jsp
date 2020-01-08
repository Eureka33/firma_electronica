<%@page import="mx.eureka.firma.digital.bean.BeanInfoDocumento"%>

<%@ page language="java" contentType="text/html"%>

<%
	BeanInfoDocumento info = (BeanInfoDocumento) request.getAttribute( "info");
    String resultado = (String) request.getAttribute( "resultado");
%>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Descarga y Validación de Documentos Firmados</title>
    
    <link rel="stylesheet" href="./libs/fontawesome/css/all.min.css"     />
    <link rel="stylesheet" href="./libs/bootstrap/css/bootstrap.min.css" />
    
    <script type="text/javascript" src="./libs/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="./libs/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		
		function inicializa() {
			document.getElementById( 'formValidacion').style.display='none';
			document.getElementById( 'mensajeResultado').style.display= <% if ( resultado == null) {%> 'none'; <%} else { %> ''; <% }%>
		}
		
		function descargar() {
			var link = document.getElementById( 'download');
			link.href= './descargaDocumento?folio=<%= info.getFolio() %>&nombre=<%= info.getNombre() %>';
			link.click();
		}
	
		function mostrarFormulario() {
			var formulario = document.getElementById( 'formValidacion');
			formulario.style.display = '';
			
			var mensaje = document.getElementById( 'mensajeResultado');
			if ( mensaje) {	mensaje.style.display = 'none'; }
			
		} 
        
        function validar(event) {
            return false;
        } 
	</script>
    
</head>
<body onload="javascript: inicializa();">
	
    <table style="border: none; margin-left: auto; margin-right:auto;">
		<tr>
            <td>
 				<img src="./images/logo_organizacion.jpg" style="height: 150px;" />
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
						<td width="20%" style="text-align: right;">
							<button type="button" class="btn btn-secondary" onclick="javascript: mostrarFormulario();"><i class="fas fa-clipboard-check"></i>&nbsp;Validar</button>
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
                    <form action="validacionDocumento?&folio=<%= info.getFolio() %>&nombre=<%= info.getNombre() %>"
                        method="post" enctype="multipart/form-data"
                    >
                        <table style="margin: 20px; width: 95%;">
                            <tr>
                                <td>
                                    <span style="font-size: 1.2em;">Seleccione el documento para validar</span>
                                </td>
    						</tr>
                            <tr><td><span id="error"></span></td></tr>
                    		<tr>
                                <td>
                                    <input type="file" id="archivo" name="archivo"/>
    							</td>
        					</tr>
            				<tr><td>&nbsp;</td></tr>
                            <tr>
                                <td style="text-align: right;">
                                    <button type="submit" class="btn btn-info" onclick="javascript return validar(event);">
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
	
</body>
</html>