<%--
  Created by IntelliJ IDEA.
  User: shanov
  Date: 10.09.2023
  Time: 21:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<p style="text-align: center">
    <button onclick="getRequest()" >Кнопка с текстом</button>
    <button>Кнопка с рисунком</button></p>
<div id = "divTextChange"> ((</div>

Пиздец3765654
<script>
    async  function getRequest(){
        //const test = fetch("http://localhost:8080/simple/simple")

        let user = {};

        let response = await fetch('http://localhost:8080/simple/simple2/simple2', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: "{}"
        });

        let result = await response.json();
        //alert(result.message)


        const divTextChange = document.getElementById("divTextChange");
        divTextChange.innerHTML = "<b> get result: "+ result.message + "</b>"
    }
</script>
</body>
</html>
