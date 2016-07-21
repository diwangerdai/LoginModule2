	$('.username').on("blur" ,function(){
	  // $(".hint p").text("测试成功");
	  var username = $('.username').val();
	  if(!username){
		  $(".hint p").text("用户名不能为空").css('color','#C33');
		  return;
	  }
	  var regx=/^[a-zA-Z][a-zA-Z0-9_]{5,15}$/;
     if(!regx.test(username)){
          $(".hint p").text("用户名不符合格式").css('color','#C33');
		  return;        
     }
	  var url = "../verifyAjax/checkUsername";
	  var data = {"username" : username};
	  $.post(url,data,function(data){
			console.log("username=  "+username);
			var status = data.status;
			console.log("status=  "+status);
			
			if(status!="该用户名可以使用"){
				$(".hint p").text(status).css('color','#C33');
			}else{
				$(".hint p").text(status).css('color','#CCC');
			}
		   
		   return;
	  });
	});
	
	$('.password').on("blur" ,function(){
	  // $(".hint p").text("测试成功");
	  var password = $('.password').val();
	  var username = $('.username').val();
	  if(!password){
		  $(".hint p").text("密码不能为空").css('color','#C33');
		  return;
	  }
	  var regx=/^[a-zA-Z][a-zA-Z0-9_]{6,22}$/;
     if(!regx.test(password)){
          $(".hint p").text("密码不符合格式").css('color','#C33');
		  return;        
     }
	 
	 if(username.substring(0,5) == password.substring(0,5)){
		 $(".hint p").text("密码和账号不能太相似").css('color','#C33');
		 return;
	 }
	 
	 $(".hint p").text("该密码可以使用").css('color','#CCC');
	 
	});
	
	$('.password2').on("blur" ,function(){
	  // $(".hint p").text("测试成功");
	  var password2 = $('.password2').val();
	  var password = $('.password').val();
	  if(!password2==password){
		  $(".hint p").text("两个密码不一致").css('color','#C33');
		  return;
	  }
	 $(".hint p").text("两个密码一致").css('color','#CCC');
	});
	
	$('.verify input').on("blur" ,function(){
	  // $(".hint p").text("测试成功");
	  var verifyCode = $('.verify input').val();
	  if(!verifyCode){
		 $(".hint p").text('验证码不能为空').css('color','#C33');
		 return;
	  }
	  var url = "../verifyAjax/checkVerifyCode";
	  var data = {"verifyCode" : verifyCode};
	  $.post(url,data,function(data){
			console.log("verifyCode=  "+verifyCode);
			var status = data.status;
			console.log("status=  "+status);
			
			if(status!='验证码正确'){
				$(".hint p").text('验证码错误').css('color','#C33');				
			}else{

				$(".hint p").text('验证码正确').css('color','#CCC');
			}
		   
		   return;
	  });
	});
