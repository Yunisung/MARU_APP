<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <meta charset="utf-8"/>
    <title>CREDITOP</title>
    <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
	<link rel="icon" href="favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="/assets/global/css/login.css?V=1.0">
    <link href="/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/bootstrap-select/css/bootstrap-select.min.css" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/bootstrap-toastr/toastr.css" rel="stylesheet"/>
</head>
<body>
   <table cellpadding="0" cellspacing="0" width="100%" height="100%">
        <tbody>
            <tr>
                <td>
                    <table cellpadding="0" cellspacing="0" align="center" class="loginbox">
                        <tbody>
                            <tr>
                                <td>
                                    <div class="image"></div>
                                </td>
                                <td>
                                    <form name="login-form" class="login-form">
                                    	<div class="logo">
                                            <img src="/assets/global/img/login/mtouch.png?V=1.0">
                                        </div>
                                        <div class="input-container" style="margin: 0 45px;">
                                            <div class="input-wrapper id">
                                                <input id="userId" type="text" name="memberId" placeholder="아이디 입력"  tabindex="1">
                                            </div>
                                            <div class="input-wrapper pw">
                                                <input id="userPw" type="password" name="memberPw" placeholder="비밀번호 입력"  tabindex="2">
                                            </div>
                                            <div class="input-wrapper smsGroup" style="display:none;">
                                                <input type="text" placeholder="인증번호" name="smsKey" maxlength="6">
                                            </div>
                                        </div>
                                        <div class="btn">
                                            <button id="submit" type="submit" id="btn-submit" class="">로그인</button>
                                        </div>
                                        <div class="chk_box">
                                            <input type="checkbox" id="chk" name="remember">
                                            <label for="chk" style="font-weight:400;">Remember me</label>
                                        </div>
                                    </form>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
        </tbody>
    </table>
    <script src="/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
    <script src="/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="/assets/global/plugins/js.cookie.min.js" type="text/javascript"></script>
    <script src="/assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
    <script src="/assets/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>
    <script src="/assets/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js" type="text/javascript"></script>
    <script src="/assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>

    <script src="/assets/global/plugins/jquery-validation/js/jquery.validate.min.js" type="text/javascript"></script>
    <script src="/assets/global/plugins/jquery-validation/js/localization/messages_ko.min.js" type="text/javascript"></script>
    <script src="/assets/global/plugins/jquery-validation/js/jquery-validate.bootstrap-tooltip.min.js" type="text/javascript"></script>
    <script>
        var authtorizeMode = false;
        $(document).ready(function() {
            $('input[name="memberid"]').focus();
            // 쿠키에서 id 정보 가져오기
            var id = Cookies.get('memberId');
            if (id == '' || id == 'null' || typeof id == 'undefined') {
                $('input[name="remember"]').attr("checked", false);
            } else {
                $('input[name="remember"]').attr("checked", true);
                $('input[name="memberId"]').val(id);
            }
        });

        $('.login-form').validate({
            errorElement: 'span', //default input error message container
            errorClass: 'help-block', // default input error message class
            focusInvalid: false, // do not focus the last invalid input
            rules: {
                memberId: {
                    required: true
                },
                memberPw: {
                    required: true
                },
            },
            messages: {
                memberId: {
                    required: "아이디를 입력하여주세요."
                },
                memberPw: {
                    required: "패스워드를 입력하여 주세요."
                }
            },
            invalidHandler: function(event, validator) { //display error alert on form submit   
                $('.alert-danger', $('.login-form')).show();
            },
            highlight: function(element) { // hightlight error inputs
                $(element).closest('.form-group').addClass('has-error'); // set error class to the control group
            },
            success: function(label) {
                label.closest('.form-group').removeClass('has-error');
                label.remove();
            },
            errorPlacement: function(error, element) {
                error.insertAfter(element.closest('.input-icon'));
            },
            submitHandler: function(form) {
                if ($(".login-form input[name='remember']").is(":checked")) {
                    Cookies.set('memberId', $('.login-form input[name="memberId"]').val());
                } else {
                    Cookies.remove('memberId');
                }
                var datas = $('.login-form').serialize();
                console.log('submit:', datas);
                $.ajax({
                    url: '/login/in',
                    type: 'POST',
                    data: datas,
                    success: loginSuccess,
                    error: loginError
                });
            }
        });

        function loginSuccess(msg) {
            var res = msg.split("||");
            if (res[0] == "OK") {
                location.href = res[1];
            } else if (res[0] == 'UNAUTHORIZED') {
                $('#btn-submit').addClass('sendSMSKey');
                $('#btn-submit').text('인증번호 발송');
                $('.smsGroup').css('display', '');
                $('input[name="memberId"]').css('display', '');
                $('input[name="memberPw"]').css('display', '');
                $.ajax({
                    url: '/login/send/' + $('.login-form').find('input[name="memberId"]').val()+'/'+res[2],
                    type: 'POST',
                    success: function(res) {
                        if( res != 'OK') {
                            bootbox.alert('인증번호 발송에 실패했습니다.<br>관리자에게 문의해주세요.');
                        } else {
                            bootbox.alert(res[1] + '<br>등록된 휴대폰으로 인증번호가 발송되었습니다.<br>SMS 발송에 최대 20초가 소요될 수 있습니다.', function() {
                                $('.sendSMSKey').text('인증번호 확인');
                                setTimeout(function() {
                                    $('input[name="smsKey"]').focus();
                                }, 1500);
                                authtorizeMode = 'checkNumber';
                            });
                        }
                    },
                    error: loginError
                });
            } else if (res[0] == 'INVALIDKEY') {
                bootbox.alert(res[1]);
                $('input[name="smsKey"]').val('');
            } else {
                bootbox.alert(res[1]);
            }
        }
        function loginError(msg) {
            bootbox.alert("SYSTEM ERROR");
            console.log('LOGIN ERROR', msg);
        }
        // $(document).ready(function() { 
        //     <c:if test = "${!empty message}" > bootbox.alert('${message}');</c:if>
        //     Login.init();
        // });
    </script>
</body>
</html>