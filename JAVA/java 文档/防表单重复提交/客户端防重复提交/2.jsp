<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP '2.jsp' starting page</title>
    <script>
    /*
     var hasSubmitted=false;
     function Check()
     {
      if(!hasSubmitted)
      {
       hasSubmitted=true;
       return true;
      }
      else
      {
       return false;
      }
     
     }
    */
    function Check()
    {
    var input= document.getElementById("submit");
    input.disabled="disabled";
    
    
    }
    
    
   </script>
   
   
   
   
   
   
  </head>
  
  <body>
  <form method="post" action="/Web02/MyServlet02" onsubmit="return Check()">
              用户名：<input type="text" name="username"/>
             <input id="submit" type="submit" value="提交" >
   </form>
   
  </body>
</html>
