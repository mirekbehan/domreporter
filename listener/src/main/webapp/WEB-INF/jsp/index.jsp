<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Administration area</title>
<script src="datetimepicker"></script>
</head>
<body>
<h1>Admin area</h1>
<hr>
<form name=f action="http://localhost:8080/Server/report/html" method="POST">
<label for="page_url">Page URL:</label><input type="text" name="page_url">
<br>
<label for="start_date">Start date:</label><input id="dp1" type="text" name="start_date" onclick="javascript:NewCssCal ('dp1','MMddyyyy','dropdown',true,'24',true)"/>
<br>
<label for="end_date">End date:</label><input id="dp2" type="text" name="end_date" onclick="javascript:NewCssCal ('dp2','MMddyyyy','dropdown',true,'24',true)"/><br>
<input type="submit" value="HTML" onclick="f.action='http://localhost:8080/Server/report/html'; return true;"/><input type="submit" value="CSV" onclick="f.action='http://localhost:8080/Server/report/csv'; return true;"/>
</form>

</body>
</html>