


//错误提示
function showError(id,msg) {
	$("#"+id+"Ok").hide();
	$("#"+id+"Err").html("<i></i><p>"+msg+"</p>");
	$("#"+id+"Err").show();
	$("#"+id).addClass("input-red");
}
//错误隐藏
function hideError(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id).removeClass("input-red");
}
//显示成功
function showSuccess(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id+"Ok").show();
	$("#"+id).removeClass("input-red");
}

//注册协议确认
$(function() {
	$("#agree").click(function(){
		var ischeck = document.getElementById("agree").checked;
		if (ischeck) {
			$("#btnRegist").attr("disabled", false);
			$("#btnRegist").removeClass("fail");
		} else {
			$("#btnRegist").attr("disabled","disabled");
			$("#btnRegist").addClass("fail");
		}
	});
});

//打开注册协议弹层
function alertBox(maskid,bosid){
	$("#"+maskid).show();
	$("#"+bosid).show();
}
//关闭注册协议弹层
function closeBox(maskid,bosid){
	$("#"+maskid).hide();
	$("#"+bosid).hide();
}

/**
 * 进行手机号码的验证
 */
function  checkPhone() {
	//手机号码是否为空
	var phone=$.trim($("#phone").val());
	var flag=true;
	if(phone==""){
		showError("phone","手机号码不能为空");
		return false;
	}
	else if(!/^1[1-9]\d{9}$/.test(phone)){
		showError("phone","手机号码格式不正确");
		return false;
	}
	else{
		$.ajax({
			url:"loan/checkPhone",
			data:{
			"phone":phone
			},
			async:true,
			type:"get",
			dataType:"json",
			success:function(json){
				if(json.errorMessage=="OK"){
					showSuccess("phone");
					flag=true;
				}else{
					showError("phone",json.errorMessage);
					flag=false;
				}
		},
		   error:function() {
			   showError("phone","系统繁忙,请稍后再试....")
			   flag=false;
           }
		});
	}
	return flag;
	//格式是否正确
	//手机号码是否存在
}


//验证密码
function  checkPassword() {
	var loginPassword=$.trim($("#loginPassword").val());
	var replayLoginPassword= $.trim($("#replayLoginPassword").val());

	if(loginPassword==""){
		showError("loginPassword","请输入密码");
		return false;
	}else if (!/^[0-9a-zA-Z]+$/.test(loginPassword)){
		showError("loginPassword","密码字符只可使用数字和大小写英文字母");
		return false;
	}else if (!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(loginPassword)){
		showError("loginPassword","密码应同时包含英文或数字");
		return false;
	} else if(loginPassword.length<6 || loginPassword.length>16){
		showError("loginPassword","密码长度必须在6-16为之间");
		return false;
	} else {
		showSuccess("loginPassword");
	}


	if (loginPassword!=replayLoginPassword){
		showError("replayLoginPassword","请输入确认密码");
	}
	return true;
}

//验证确认密码选项
function checkPasswordEqu() {

    var loginPassword=$.trim($("#loginPassword").val());
    var replayLoginPassword= $.trim($("#replayLoginPassword").val());
    if(loginPassword==""){
    	showError("loginPassword","请输入密码");
    	return false;
	}else if(replayLoginPassword==""){
    	showError("replayLoginPassword","请输入确认密码");
    	return false;
	}else if(loginPassword!=replayLoginPassword){
    	showError("replayLoginPassword","两次输入密码不一致,请重新输入");
    	return false;
	}else {
    	showSuccess("replayLoginPassword");
	}

	return true;
}
//验证 验证码
function checkCaptcha() {
	var captcha= $.trim($("#captcha").val());

	var flag=true;
	if(captcha==""){
		showError("captcha","请输入验证码");
		return false;
	}else {
		$.ajax({
			url:"loan/checkCaptcha",
			data:"captcha="+captcha,
			type:"post",
			dataType:"json",
			success:function(json){
				if(json.errorMessage=="OK"){
					showSuccess("captcha");

				}else{
					showError("captcha",json.errorMessage);
					flag=false;
				}
			},
			error:function () {
				showError("captcha","系统繁忙,请稍后再试...")
				flag=false;
            }
		});
	}

	return flag;
}

//注册
function register() {
	//获取表单元素
   var phone=$.trim($("#phone").val());
   var loginPassword=$.trim($("#loginPassword").val());
   var replayLoginPassword  = $.trim($("#replayLoginPassword").val());

   if(checkPhone()&&checkPassword&&checkPasswordEqu()&&checkCaptcha()){
   	$("#loginPassword").val($.md5(loginPassword));
   	$("#replayLoginPassword").val($.md5(replayLoginPassword));



  	 $.ajax({
		 url:"loan/register",
		 data:{
		 	"phone":phone,
			 "loginPassword":$("#loginPassword").val(),
			 "replayLoginPassword":$("#replayLoginPassword").val()
		 },
		 type:"post",
		 dataType:"json",
		 success:function (json) {
			 if (json.errorMessage=="OK"){
			 	window.location.href="realName.jsp";
			 } else{
			 	showError("captcha",json.errorMessage);
			 }
         },
		 error:function () {
              showError("captcha","系统繁忙,请稍后再试...")

         }
	 });
   }
}