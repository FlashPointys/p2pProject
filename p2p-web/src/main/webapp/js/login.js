var referrer = "";//登录后返回页面
referrer = document.referrer;
if (!referrer) {
	try {
		if (window.opener) {                
			// IE下如果跨域则抛出权限异常，Safari和Chrome下window.opener.location没有任何属性              
			referrer = window.opener.location.href;
		}  
	} catch (e) {
	}
}

//按键盘Enter键即可登录
$(document).keyup(function(event){
	if(event.keyCode == 13){
		login();
	}
});


//页面加载完毕之后
$(function () {
	$.ajax({
		url:"loan/loadStat",
		type:"get",
		success:function(json){
			$(".historyAverageRate").html(json.historyAverageRate);
			$("#allUserCount").html(json.allUserCount);
			$("#allBidMoney").html(json.bidAllMoney);
		},
		error:function () {

        }
	});
});

//验证手机号码
function checkPhone() {
	var phone=$.trim($("#phone").val());
	if(phone==""){
		$("#showId").html("请输入手机号码");
		return false;
	}else if (!/^1[1-9]\d{9}$/.test(phone)){
		$("#showId").html("请输入格式正确的手机号码");
		return false;
	}else {
		$("#showId").html("");
		return true;
	}
}

//验证登录密码
function checkPassword() {
	var loginPassword=$.trim($("#loginPassword").val());
	if(loginPassword==""){
		$("#showId").html("请输入密码");
		return false;
	}else {
		$("#showId").html("");
		return true;
	}
}
//验证图形验证码
function checkCaptcha() {
	var captcha=$.trim($("#captcha").val());
	var flag=true;
	if(captcha==""){
		$("#showId").html("请输入验证码");
		return false;
	}else{
		$.ajax({
			url:"loan/checkCaptcha",
			data:{
				"captcha":captcha
			},
			type:"get",
			success:function (json) {
				if(json.errorMessage=="OK"){
					$("#showId").html("");
				}else{
					$("#showId").html(json.errorMessage);
					flag=false;
				}
            },
			error:function () {
				$("#showId").html("系统繁忙,请稍后再试");
				flag=false;
            }
		});
	}
	return flag;
}

//登录
function Login() {
	var phone= $.trim($("#phone").val());
	var loginPassword= $.trim($("#loginPassword").val());

	if(checkPhone() && checkCaptcha() && checkPassword()){
		$("#loginPassword").html($.md5(loginPassword));
		$.ajax({
			url:"loan/login",
			data:{
			"phone":phone,
			"loginPassword":$.md5(loginPassword)
			},
			type:"post",
			success:function (json) {
				if(json.errorMessage=="OK"){
					window.location.href=referrer;
				}else {
					$("#showId").html(json.errorMessage);

				}
            },
			error:function () {
				$("#showId").html("登录人数过多,请稍后再试..");
            }
		});
	}

}