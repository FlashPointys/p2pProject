<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/12 0012
  Time: 22:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>疯狂加载中...</title>
</head>
<body>
  <form action="${pay_p2p_return_url}" method="post">
      <input type="text" name="signVerified" value="${signVerified}">
      <c:forEach items="${params}" var="paramMap">
          <input type="text" name="${paramMap.key}" value="${paramMap.value}">
      </c:forEach>
  </form>
<script>document.forms[0].submit();</script>
</body>

</html>
