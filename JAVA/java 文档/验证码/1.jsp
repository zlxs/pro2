<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>注册页面</title>
    <script type="text/javascript">
     function changeImage(img)
     {
      img.src=img.src+"?"+new Date().getTime();
     }
    
    </script>
    
  </head>
  
  <body>
   <div>
    <form action="" method="post">
                 用户名：<input type="text" name="username"><br/>
                 密  码：   <input type="password" name="username"><br/>
                 验证码：<img src="/Web01/YanZhenMa" id="image" title="换一张" style="cursor:hand" onclick="changeImage(this)"></img><br/>         
           <input type="submit"  value="提交">
    </form>
   </div>
  </body>
</html>
