<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<!DOCTYPE html>
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<c:import url="/include/head.jsp" />
</head>
<body style="background-color: white;">
	<div class="mtouch-container">
		<h4>${DATAMAP.name} 비밀번호 변경</h4>
		<!-- BEGIN FORM-->
		<form class="form-horizontal form-bordered" role="form" id="smsCheckFrm" data-form="true" name="form" action="/member/user/smsSend/${DATAMAP.id}" method="post">
			<div class="form-body row">
				<div class="form-group col-sm-6">
					<label class="control-label col-sm-4 req-label">비밀번호를 변경 하시겠습니까?<br>SMS 인증이 필요합니다.</label>
				</div>
			</div>
			<div class="alert alert-danger display-hide"></div>
			<div class="form-actions right">
				<div class="">
					<button type="submit" class="btn btn-sm green loading-btn" data-loading-text="Loading...">
						<i class="fa fa-send"></i>&nbsp;인증번호 발송
					</button>
				</div>
			</div>
		</form>
		<form class="form-horizontal form-bordered" role="form" id="writeFrm" data-form="true" name="form" action="/member/user/updatePassword/${DATAMAP.id}" method="post">
			<div class="form-body row">
				<div class="form-group col-sm-6">
					<label class="control-label col-sm-4 req-label">기존 비밀번호 </label>
					<div class="col-sm-6">
						<input type="password" class="form-control input-sm" maxlength="50" name="check" id="check" value="">
					</div>
				</div>
				<div class="form-group col-sm-6">
					<label class="control-label col-sm-4 req-label">새 비밀번호 (영문, 숫자를 포함한 5자리 이상) </label>
					<div class="col-sm-6">
						<input type="password" class="form-control input-sm" maxlength="50" name="pw" id="pw" value="">
					</div>
				</div>
				<div class="form-group col-sm-6">
					<label class="control-label col-sm-4 req-label">비밀번호 확인 </label>
					<div class="col-sm-6">
						<input type="password" class="form-control input-sm" maxlength="50" name="pw2" id="pw2" value="">
					</div>
				</div>
			</div>
			<div class="alert alert-danger display-hide"></div>
			<div class="form-actions right">
				<div class="">
					<button type="submit" class="btn btn-sm green loading-btn" data-loading-text="Loading...">
						<i class="fa fa-send">비밀번호 변경</i>&nbsp;
					</button>
				</div>
			</div>
		</form>
		<form class="form-horizontal form-bordered" role="form" id="submitFrm" data-form="true" name="form" action="/member/user/smsCheck/${DATAMAP.id}" method="post">
			<div class="form-body row">
				<div class="form-group col-sm-6">
					<label class="control-label col-sm-4 req-label">인증번호 </label>
					<div class="col-sm-6">
						<input type="text" class="form-control input-sm" maxlength="6" name="smsNumber" id="smsNumber" value="">
					</div>
				</div>
			</div>
			<div class="alert alert-danger display-hide"></div>
			<div class="form-actions right">
				<div class="">
					<button type="submit" class="btn btn-sm green loading-btn" data-loading-text="Loading...">
						<i class="fa fa-send"></i>&nbsp;인증번호 확인
					</button>
				</div>
			</div>
		</form>
		<!-- END FORM-->
	</div>
	<c:import url="/include/javascript.jsp" />
	<!-- BEGIN FORM JAVASCRIPT -->
	<script type="text/javascript">
		var form0 = $('#smsCheckFrm');
		var form2 = $('#writeFrm');
		var form1 = $('#submitFrm');
		form1.hide();
		form2.hide();


		var error1 = $('.alert-danger', form1);
		jQuery.validator.addMethod("notEqualTo", function(value, element, param) {
			  return this.optional(element) || value != param;
			}, "기존 비밀번호와 같은 값을 사용할 수 없습니다.");
		jQuery.validator.addMethod("passwordCk",  function( value, element ) {
			return this.optional(element) ||  /^[a-zA-Z0-9!@#$%^*()?_~]{5,}$/.test(value);
			}, "영문, 숫자를 포함한 비밀 번호를 입력해 주세요.");

		form0.validate({
			submitHandler: function (form) {
				$.ajax({
					type:"POST",
					url: form0.attr("action"),
					data: {},
					success : function(data) {
						if(data.indexOf("OK") > -1) {
							successSms("인증번호를 전송 했습니다.");
						}else {
							failSms(data);
						}
					},
					error: function(xhr, status, error) {
						finishWin("비밀번호 변경에 실패했습니다. 관리자에게 문의하세요.");
					}
				});
			}
		});

		form2.validate({
			rules : {
				check : {
					required : true,
					remote : {
						url : "/member/user/pwCheck/${DATAMAP.id}", //make sure to return true or false with a 200 status code
						type : "post",
						data : {
							pw : function() {
								return form2.find('input[name="check"]').val();
							}
						}
					}
				},
				pw : {
					notEqualTo: function() {
						return form2.find('input[name="check"]').val();
					},
					passwordCk: true,
                    minlength : 5,
                    maxlength : 20
                },
                pw2 : {
                    minlength : 5,
                    maxlength : 20,
                    equalTo : "#pw"
                }
			},
			invalidHandler: function (event, validator) { //display error alert on form submit              
               	var error1Str = '<button class="close" data-close="alert"></button>필수 입력값을 모두 입력하셔야 합니다.';
               	error1.html(error1Str);
				error1.show();
                App.scrollTo(error1, -200);
            },
            submitHandler: function (form) {
                error1.hide();
				
                $.ajax({
                	type:"POST",
                	url: form2.attr("action"),
                	data: {"pw": $("#pw").val(), "check": $("#check").val()},
					success : function(data) {
						if(data.indexOf("OK") > -1) {
							finishWin("비밀번호 변경에 성공했습니다.");
						}else if(data.indexOf("ERR1") > -1) {
							failSms("예전에 사용한 비밀번호 입니다.");
						}
						else {
							failSms("비밀번호 변경에 실패했습니다. 관리자에게 문의하세요.");
						}
					},
					error: function(xhr, status, error) {
						failSms("비밀번호 변경에 실패했습니다. 관리자에게 문의하세요.");
					}
                });
			}
		});

		form1.validate({
			rules : {
				smsNumber: {
					required : true,
					minlength : 6,
					maxlength : 6
				}
			},
			invalidHandler: function (event, validator) { //display error alert on form submit
				var error1Str = '<button class="close" data-close="alert"></button>인증번호는 6자리 입니다.';
				error1.html(error1Str);
				error1.show();
				App.scrollTo(error1, -200);
			},
			submitHandler: function (form) {
				error1.hide();

				$.ajax({
					type:"POST",
					url: form1.attr("action"),
					data: {"smsNumber": $("#smsNumber").val()},
					success : function(data) {
						console.log(data);
						if(data.indexOf("OK") > -1) {
							// finishWin("비밀번호를 변경 했습니다.");
							successSmsCheck("인증번호를 확인 완료");
						}else {
							failSms(data);
						}
					},
					error: function(xhr, status, error) {
						finishWin("비밀번호 변경에 실패했습니다. 관리자에게 문의하세요.");
					}
				});
			}
		});

		function finishWin(msg) {
			bootbox.alert(msg, function() {
				window.open('about:blank','_self').self.close();  // IE에서 묻지 않고 창 닫기
			});
		}

		function successSms(msg) {
			bootbox.alert(msg, function (){
				form0.hide();
				form1.show();
				form2.hide();
			});
		}

		function successSmsCheck(msg) {
			bootbox.alert(msg, function (){
				form0.hide();
				form1.hide();
				form2.show();
			});
		}

		function failSms(msg) {
			bootbox.alert(msg);
		}
	</script>
</body>
</html>