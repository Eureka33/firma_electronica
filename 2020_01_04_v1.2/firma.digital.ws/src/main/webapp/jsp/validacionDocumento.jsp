<%@page import="mx.eurk.firma.digital.bean.BeanInfoDocumento"%>

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
	
	<link href="./css/sigmadoc.css" rel="stylesheet" type="text/css" />
	<link href="./css/menu.css"     rel="stylesheet" type="text/css" />
	
	<style type="text/css">
		@import "./js/dojo/dijit/themes/tundra/tundra.css";
		@import "./js/dojo/dojo/resources/dojo.css";
	</style>
	
	<script type="text/javascript">
		
		function inicializa() {
			document.getElementById('menuHelp').style.display='none';
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
	</script>
</head>
<body onload="javascript: inicializa();">
	<table style="border-spacing: 0px; width: 100%; border-collapse: collapse; border: 0px;">
	
		<tr align="right">
    		<td height="18px;" align="right">
    		
    			<!--  barra superior con documentos de ayuda -->
      		
      			<table style="border-spacing: 0px; border-collapse: collapse;" border="0">
       				<tr align="right">
       					<td class="header" width="75px">
							<a class="header" style="display: inline-block;" 
								onmouseover="document.getElementById('menuHelp').style.display='block'"
								onmouseout="document.getElementById('menuHelp').style.display='none'">
								<img style="vertical-align: middle;" border="0" src="./Imagenes/answer.png" />Ayuda
							</a> 
							<div id="menuHelp" class="anylinkcssHelp" style="width: 120px; background-color: #FFFFFF"
								onmouseover="document.getElementById('menuHelp').style.display='block'"
								onmouseout="document.getElementById('menuHelp').style.display='none'">
								<a href="./ayuda/Anexo_15_Fto.13_ManualUsuario_RolTurnado.pdf" download>Recepci&oacute;n y Turnado</a>												
								<a href="./ayuda/Anexo_15_Fto.13_ManualUsuario_RolDeAtencion.pdf" download>Atenci&oacute;n</a>
								<a href="./ayuda/Anexo_15_Fto.13_ManualUsuario_RolSupervisorDeArea.pdf" download>Supervisor</a>			
							</div>   
	           			</td>
					</tr>
				</table>
	
			</td>
		</tr>
		<tr>
			<td style="border: 1px; border-style: solid; border-color: #63666A;">
				
				<!--  logo de la organización -->
 				
 				<img src="./Imagenes/header_segob.png" border="1px" style="border-style: solid;background-color: white;">
			
			</td>
		</tr>
		<tr>
    		<td>
    			<table style="border-spacing: 0px; border: 0px; border-collapse: collapse; width: 100%">
        			<tbody>
        				<tr>
        					<td align="left" valign="middle" nowrap="nowrap" style="vertical-align: middle; border-top: 1px; border-top-style: solid; border-top-color: #63666A;">
	        				</td>
        				</tr>
					</tbody>
				</table>
			</td>
		</tr>
	</table>
	
	<br />

	<table width="100%"> 
		<tr>
			<td width="30%"></td>
			<td>
				<table width="100%">
					<tr>
						<td><span style="font-size: 1.2em; font-weight: bold;">Folio:</span></td>
						<td><span style="font-size: 1.2em;"><%= info.getFolio() %></span></td>
					</tr>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr>
						<td><span style="font-size: 1.2em; font-weight: bold;">Nombre:</span></td>
						<td><span style="font-size: 1.2em;"><%= info.getNombre() %></span></td>
					</tr>
				</table>
			</td>
			<td width="30%"></td>
		</tr>
		
		<tr>
			<td>&nbsp;</td>
		</tr>
		
		<tr>
			<td width="30%"></td>
			<td>
				<table width="100%">
					<tr>
						<td width="40%">&nbsp;</td>
						<td width="30%" style="text-align: right;">
							<button type="button" onclick="javascript: descargar();"><img src="./Imagenes/filePDF.png" />&nbsp;Descargar</button>
							<a id="download" href=""></a>
						</td>
						<td width="30%" style="text-align: right;">
							<button type="button" onclick="javascript: mostrarFormulario();"><img src="./Imagenes/check.png" />&nbsp;Validar</button>						
						</td>
					</tr>
				</table>
			</td>
			<td width="30%"></td>
		</tr>
		
		<tr>
			<td>&nbsp;</td>
		</tr>
	
		<tr id="mensajeResultado">
			<td width="30%"></td>
			<td>
				<table width="100%">
					<tr>
						<td style="text-align: center;" width="100%">
							<span style="font-size: 1.4em; font-weight: bold; color: red;" ><%= resultado %></span>
						</td>
					</tr>
				</table>
			</td>
			<td width="30%"></td>
		</tr>
		
		<tr id="formValidacion">
			<td width="30%"></td>
			<td>
				<form action="validacionDocumento?&folio=<%= info.getFolio() %>&nombre=<%= info.getNombre() %>"
					method="post" enctype="multipart/form-data"
				>
					<table width="100%" style="border: solid black 1px; padding: 15px;">
						<tr>
							<td colspan="3">
								<span style="font-size: 1.2em;">Seleccione el documento para validar</span>
							</td>
						</tr>
						<tr>
							<td colspan="3">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="3">
								<input type="file" id="archivo" name="archivo"/>
							</td>
						</tr>
						<tr>
							<td colspan="3">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="2">&nbsp;</td>
							<td width="25%" style="text-align: right;">
								<button type="submit"><img src="./Imagenes/eye.png" />&nbsp;Comparar</button>
							</td>
						</tr>
					</table>
				</form>
			</td>
			<td width="30%"></td>
		</tr>
	</table>
	
</body>
</html>