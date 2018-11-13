<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>正在加载...</title>
</head>
<body>
<form action="${p2p_pay_alipay_url}" method="post">
	<input type="hidden" id="out_trade_no" name="out_trade_no" value="${out_trade_no}"/>
	<input type="hidden" id="total_amount" name="total_amount" value="${rechargeMoney}"/>
	<input type="hidden" id="subject" name="subject" value="${subject}"/>
	<input type="hidden" id="body" name="body" value="${body}"/>
</form>
<script>document.forms[0].submit();</script>
</body>
</html>