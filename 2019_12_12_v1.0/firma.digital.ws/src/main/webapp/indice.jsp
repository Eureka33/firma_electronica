<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Firma de Archivo</title>
</head>
<body>
	<br /><br /><br />
	<center>
	
	<form enctype="multipart/form-data" action="firmarArchivo" method="post">
	
	<table border="0" bgcolor="#ccFDDEE">
		<tr>
    		<td colspan="2" align="center"><B>UPLOAD THE FILE</B></td>
    	</tr>
	    <tr>
    		<td colspan="2" align="center"></td>
   		</tr>
   		<tr>
   			<td><b>Choose the file To Upload:</b></td>
   			<td><INPUT NAME="file" TYPE="file"></td>
   		</tr>
   		<tr>
   			<td colspan="2" align="center"></td>
	   	</tr>
   		<tr>
   			<td colspan="2" align="center">
   				<input type="submit" value="Send File">
   			</td>
   		</tr>
   	</table>
    <div id="result">
    	<h3>${requestScope["message"]}</h3>
    </div>
    
    </FORM>
    </center>
</body>
</html>
