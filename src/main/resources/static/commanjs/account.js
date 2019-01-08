//注册
function register(){
	var email = jQuery("#email").val();
	var password = jQuery("#password").val();

	
		jQuery.ajax({
			url:"/account/zhuce",
			data:{"email":email,"password":password},
			success:function(data){
				if(data.success == false){
					alert(data.information)
					window.location.href="/login-register"
				}else{
					if(confirm(data.information+" 点击直接登录")){
						loginSS(email,password)
					}
					
				}
			}
		});
}

//登录
function loginSS(email,password){
		jQuery.ajax({
			url:"/account/login",
			data:{"email":email,"password":password},
			success:function(data){
				if(data.success == false){
					alert(data.information);
				}else{
					//如果登录成功,显示退出按钮,隐藏登录注册按钮
					alert(data.information);
					window.location.href="/";
				}
			}
		});
	}










//登录
function login(){
	var email = jQuery("#login-email").val();
	var password = jQuery("#login-password").val();
	
	if(email == ''|| password ==''){
		alert("账号或者密码不能为空");
		return;
	}else{
		jQuery.ajax({
			url:"/account/login",
			data:{"email":email,"password":password},
			success:function(data){
				if(data.success == false){
					alert(data.information);
				}else{
					//如果登录成功,显示退出按钮,隐藏登录注册按钮
					alert(data.information);
					window.location.href="/";
				}
			}
		});
	}
}

//登录
function loginByHeader(){
	var email = jQuery("#email1").val();
	var password = jQuery("#password1").val();
	
	if(email == ''|| password ==''){
		alert("账号或者密码不能为空");
		return;
	}else{
		jQuery.ajax({
			url:"/account/login",
			data:{"email":email,"password":password},
			success:function(data){
				if(data.success == false){
					alert(data.information);
				}else{
					//如果登录成功,显示退出按钮,隐藏登录注册按钮
					alert(data.information);
					window.location.href="/";
				}
			}
		});
	}
}

/**
 * 退出登录
 * @returns
 */
function exit(){
	jQuery.ajax({
		url:"/account/exit",
		success:function(){
			window.location.href="/"
		}
	})
}



//$(document).ready(function(){
//	$("img").addClass("img-rounded");
//	if($("#accountInformation").text()!=='游客'&&$("#accountInformation").text()!==null){
//		$("#main-signup-button").fadeOut(1500);
//		$("#main-login-button").fadeOut(1500);
//		$("#account-information").fadeIn(1500);
//		$("#exit").fadeIn(1500);
//	}
//	
//});

//跳转到视频观看的页面
function goVideoPlay(element){
	var id=$(element).attr('data-id');
	if(id !== '' && id !== null){
		window.location="/goVideoPlay?id="+id;
	}
}


//跳转到个人信息界面
function information(){
	var information = $("#accountInformation").text();
	console.log(information);
	if(information == '游客'){
		$("#main-login-button > a").click()
	}else{
		window.location.href = "/information"
	}
}
//跳转到论坛界面
function bbs(){
	var information = $("#accountInformation").text();
	console.log(information);
	if(information == '游客'){
		$("#main-login-button > a").click()
	}else{
		window.location.href = "/bbs"
	}
}

//跳转到发布论坛界面
function goPublishArticle(){
	window.location.href = "/bbs/goPublishArticle";
}

//跳转到注册界面
function goRegister(){
	window.location.href = "/login-register";
}

//注册时间格式化
jQuery("#registertime").text(formatDateTime(jQuery("#registertime").attr("registertime")*1))



