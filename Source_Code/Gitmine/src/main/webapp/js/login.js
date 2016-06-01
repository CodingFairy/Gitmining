var interestLabel = {
	web:0, app:0, api:0, framework:0, cms:0, django:0, emacs:0,
	management:0, linux:0, windows:0, interFace:0, os:0, server:0, tool:0,
	plugin:0,   json:0,  tempLate:0, library:0, ui:0, dataBase:0
};
var labelFather = $('.radio-toolbar');


$(function(){
	
	$('#switch_qlogin').click(function(){
		$('#switch_login').removeClass("switch_btn_focus").addClass('switch_btn');
		$('#switch_qlogin').removeClass("switch_btn").addClass('switch_btn_focus');
		$('#switch_bottom').animate({left:'-8px',width:'70px'});
		$('#qlogin').css('display','none');    <!--隐藏属性-->
		$('#web_qr_login').css('display','block');
		
		});
	$('#switch_login').click(function(){
		
		$('#switch_login').removeClass("switch_btn").addClass('switch_btn_focus');
		$('#switch_qlogin').removeClass("switch_btn_focus").addClass('switch_btn');
		$('#switch_bottom').animate({left:'140px',width:'70px'});
		
		$('#qlogin').css('display','block');
		$('#web_qr_login').css('display','none');
		});
		if(getParam("a")=='0')
		{
			$('#switch_login').trigger('click');
		}

	});
	
function logintab(){
	scrollTo(0);
	$('#switch_qlogin').removeClass("switch_btn_focus").addClass('switch_btn');
	$('#switch_login').removeClass("switch_btn").addClass('switch_btn_focus');
	$('#switch_bottom').animate({left:'154px',width:'96px'});
	$('#qlogin').css('display','none');
	$('#web_qr_login').css('display','block');
	
}


//根据参数名获得该参数 pname等于想要的参数名 
function getParam(pname) { 
    var params = location.search.substr(1); // 获取参数 平且去掉？ 
    var ArrParam = params.split('&'); 
    if (ArrParam.length == 1) { 
        //只有一个参数的情况 
        return params.split('=')[1]; 
    } 
    else { 
         //多个参数参数的情况 
        for (var i = 0; i < ArrParam.length; i++) { 
            if (ArrParam[i].split('=')[0] == pname) { 
                return ArrParam[i].split('=')[1]; 
            } 
        } 
    } 
}  


var pwdmin = 6;

$(document).ready(function() {

	$.ajax({
		type:"GET",
		url:"/login/name",
		success:function (result) {
			if (result.length>2) {
				$("#login-href").html(result);
				$('#login-href').attr('href','#interestModal');
			}
		}
	});

	$('#reg').click(function() {

		if ($('#user').val() == "") {
			$('#user').focus().css({
				border: "1px solid red",
				boxShadow: "0 0 2px red"
			});
			$('#userCue').html("<font color='red'><b>×User name cannot be empty.</b></font>");
			return false;
		}



		if ($('#user').val().length < 4 || $('#user').val().length > 16) {

			$('#user').focus().css({
				border: "1px solid red",
				boxShadow: "0 0 2px red"
			});
			$('#userCue').html("<font color='red'><b>×username should have 4-16 characters.</b></font>");
			return false;

		}
		
		if ($('#passwd').val().length < pwdmin) {
			$('#passwd').focus();
			$('#userCue').html("<font color='red'><b>×the password cannot be less than " + pwdmin + "bits</b></font>");
			return false;
		}
		if ($('#passwd2').val() != $('#passwd').val()) {
			$('#passwd2').focus();
			$('#userCue').html("<font color='red'><b>×the two passwords are not same！</b></font>");
			return false;
		}

		$.ajax({
			type: "GET",
			url: "/login/register",
			data: { username:$("#user").val(),password:$("#passwd").val(),email:$("#email_input").val()}, <!--要传递的数据--> 			<!--接受数据的格式-->
			success: function(result) {
				alert("succeed! please login again !");
				location.href = "";
			},
			error: function () {
				alert("unknown error.");
			}
		});

		// var sqq = /^[1-9]{1}[0-9]{4,9}$/;
		// if (!sqq.test($('#qq').val()) || $('#qq').val().length < 5 || $('#qq').val().length > 12) {
		// 	$('#qq').focus().css({
		// 		border: "1px solid red",
		// 		boxShadow: "0 0 2px red"
		// 	});
		// 	$('#userCue').html("<font color='red'><b>×the format of email is not correct!</b></font>");return false;
		// } else {
		// 	$('#qq').css({
		// 		border: "1px solid #D7D7D7",
		// 		boxShadow: "none"
		// 	});
		//
		// }

	})
	
	$("#login_btn").click(function () {
		$.ajax({
			type:"GET",
			url:"/login/login",
			data: { username:$("#u").val(),password:$("#p").val()},
			success:function (result) {
				if (result.length>0) {
					location.href = "";
				} else {
					alert("name or password error!");
				}
			},
			error:function () {
				alert("error!");
			}
		});
	});


	//for interest labels
	initInterestLabel();
	
	// $('#finishInterest').click(finishChosen(interestLabel));

});

//initailize the interest Labels



function initInterestLabel(){
	var i=0;
	console.log(labelFather.attr("isMe"));
	$.each(interestLabel,function(key,value){

		interestLabel[i] = 0;i++;

		var check = "<input type=\"checkbox\" id=\"" +key+ "\" class=\"checkLabel\" value=\"false\">";
		var label = "<label class=\"label\" for=\"" +key + "\">" +key + "</label>";

		// console.log(check);
		// console.log(label);


		labelFather.append(check);
		labelFather.append(label);

		if(i==19)
			console.log(labelFather.find('.checkLabel').length);
		// console.log("anythingWrong?");
	})
};

function finishChosen(interestLabel){
	var checkList = labelFather.find('.checkLabel');
	$.each(checkList,function(i,check_i){
		var checkbox = labelFather.find('.checkLabel').eq(i);
		if(checkbox.is(":checked")){
			console.log(interestLabel[i]);
			interestLabel[i] = 1;
		}
	});
	
}

