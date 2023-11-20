<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <c:import url="/include/head.jsp" />
</head>
<!-- END HEAD -->
<body class="page-header-fixed page-sidebar-closed-hide-logo page-content-white">
<div class="page-wrapper">
    <c:import url="/include/header.jsp" />
    <!-- BEGIN CONTAINER -->
    <div class="page-container">
        <c:import url="/include/nav.jsp" />
        <!-- BEGIN CONTENT -->
        <div class="page-content-wrapper">
            <div class="page-content">
                <div class="mtouch-container">
                    <!-- BEGIN PAGE BAR -->
                    <div class="page-bar">
                        <ul class="page-breadcrumb">
                            <li><a href="/">Home</a><i class="fa fa-circle"></i></li>
                            <li><span>가맹점 관리</span><i class="fa fa-circle"></i></li>
                            <li><span>노티 등록 수정</span><i class="fa fa-circle"></i></li>
                        </ul>
                        <div class="page-toolbar">
                            <div class="btn-group btn-theme-panel">
                                <a class="btn float-window"><i class="icon-size-fullscreen"></i></a>
                                <a href="javascript:;" class="btn dropdown-toggle" data-toggle="dropdown">
                                    <i class="icon-settings"></i>
                                </a>
                                <div class="dropdown-menu theme-panel pull-right dropdown-custom hold-on-click panel-warning">
                                    <div class="panel-heading">도움말</div>
                                    <div class="panel-body">TEXT</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- END PAGE BAR -->
                    <!-- BEGIN PAGE CONTENT - MARU - INNER -->
                    <div class="page-content-inner">
                        <div class="portlet light">
                            <div class="portlet-title">
                                <div class="caption">
                                    <i class="fa fa-reorder"></i>
                                    <span class="caption-title">
                                        노티 수정
                                    </span>
                                </div>
                            </div>
                            <div class="portlet-body form">
                                <form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/mcht/noti/" method="post">
                                    <div class="form-body row">
                                        <input type="hidden" name="action_type" value="update" data-reg="false" />
                                        <div class="form-body row">
                                            <input type="hidden" class="idx" name="idx" data-key="true" value="${DATAMAP.idx}"/>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label input-sm col-sm-4 req-label">아이디
                                                </label>
                                                <div class="col-sm-6">
                                                    <input type="text" class="form-control input-sm" maxlength="50" name="id"  value="${DATAMAP.id}" readonly>
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label input-sm col-sm-4 req-label">상태</label>
                                                <select id="status" name="status" class="selectpicker col-sm-6">
                                                    <option value="Y" selected>Y</option>
                                                    <option value="N">N</option>
                                                </select>
                                                <script type="text/javascript"> document.forms.writeFrm.status.value = '${DATAMAP.status}'</script>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4 req-label">전달주소</label>
                                                <div class="col-sm-6">
                                                    <input type="text" class="form-control input-sm phone" maxlength="100" name="hookUrl" placeholder="https://" value="${DATAMAP.hookUrl}">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="alert alert-danger display-hide"></div>
                                        <div class="form-actions right">
                                            <div class="">
                                                <button type="submit" class="btn green btn-sm loading-btn" data-loading-text="Loading...">
                                                    <i class="fa fa-search"></i>&nbsp;Submit
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <!-- END ADD FORM TABLE-->
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <c:import url="/include/footer.jsp" />
</div>
<c:import url="/include/javascript.jsp" />
<!-- BEGIN FORM JAVASCRIPT -->
<script type="text/javascript">
    var form1 = $('#writeFrm');
    var error1 = $('.alert-danger', form1);
    form1.validate({
        rules : {
            hookUrl : {
                required: true
            }
        },
        invalidHandler: function (event, validator) { //display error alert on form submit
            var error1Str = '<button class="close" data-close="alert"></button>';
            error1Str += "등록 중 잘못된 입력값이 있습니다. 위의 입력 값을 다시 확인하여 주시기 바랍니다.";
            error1.html(error1Str);
            error1.show();
            App.scrollTo(error1, -200);
        },
        submitHandler: function (form) {
            error1.hide();
            $('.confirm_grade').text("전달주소 : " + form1.find('input[name="hookUrl"]').val());
            $('.confirm_id').text("아이디 : " + form1.find('input[name="id"]').val());

            bootbox.confirm($('#bootbox_confirm').html(), function(result) {
                if (result) {
                    ajaxFormSubmit(form, '/mcht/noti/form.jsp'); //PAGE 이동
                }
            });
        }
    });
    $('#nav-mcht').addClass('active');
</script>
<!-- END FORM JAVASCRIPT -->
<!-- 모달 생성을 위한 베이스 -->
<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
<!-- 확인창 생성을 위한 베이스 -->
<div style="display: none;">
    <div class="panel panel-warning" id="bootbox_confirm">
        <!-- Default panel contents -->
        <div class="panel-heading">
            <h3 class="panel-title">입력정보 확인</h3>
        </div>
        <div class="panel-body">
            <p>다음과 같은 정보로 노티정보를 수정하려고 합니다.</p>
            <p>계속 진행하시겠습니까?</p>
        </div>
        <!-- List group -->
        <ul class="list-group">
            <li class="list-group-item confirm_id"></li>
            <li class="list-group-item confirm_grade"></li>
        </ul>
    </div>
</div>
</body>

</html>