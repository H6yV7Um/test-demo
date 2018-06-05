<%@ page language="java" contentType="text/html; charset=US-ASCII"
         pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
  <title>Login Success Page</title>
</head>

<body>
<%
  //allow access only if session exists
  String user = (String) session.getAttribute("user");
  String userName = null;
  String sessionID = null;
  Cookie[] cookies = request.getCookies();
  if(cookies !=null){
    for(Cookie cookie : cookies){
      if(cookie.getName().equals("user")) userName = cookie.getValue();
      if(cookie.getName().equals("JSESSIONID")) sessionID = cookie.getValue();
    }
  }
%>
<h3>Hi <%=userName %>, Login successful. Your Session ID=<%=sessionID %></h3>
<br>
User=<%=user %>
<br>
<a href="CheckoutPage.jsp">Checkout Page</a>
<form action="LogoutServlet" method="post">
  <input type="submit" value="Logout" >
</form>


<script type="text/javascript">
  function loadPayEgisDidJs() {
    var _protocol = (("https:" == document.location.protocol) ? "https://" :
            "http://");
    var element = document.createElement("script");
    element.src = _protocol + "pws.payegis.com.cn/did/js/dp.js?appId=<%=
"5464701"%>&sessionId=<%=session.getId()%>&ts=<%=System.currentTimeMillis()%>";
    document.body.appendChild(element);
  }

  if (window.addEventListener) {
    window.addEventListener("load", loadPayEgisDidJs, false);
  } else if (window.attachEvent) {
    window.attachEvent("onload", loadPayEgisDidJs);
  } else {
    window.onload = loadPayEgisDidJs;
  }
</script>

<!-Begin ThreatMetrix profiling tags below -->
<!- note: replace 'UNIQUE_SESSION_ID' with a uniquely generated handle
note: for production, replace 'h.online-metrics.net' with a local
URL and configure your web server to redirect to
'h.online-metrics.net' -->

<script type="text/javascript" src="https://h.online-metrix.net/fp/tags.js?org_id=z0jkxli1&session_id=<%=session.getId()%>"></script>
<noscript>
  <iframe style="width: 100px; height: 100px; border: 0; position: absolute; top: -5000px;" src="https://h.online-metrix.net/tags?org_id=z0jkxli1&session_id=<%=session.getId()%>"></iframe>
</noscript>

<!- End profiling tags -->
</body>
</html>