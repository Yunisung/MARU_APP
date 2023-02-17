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
                            <li><span>통합인증 정보 수정</span><i class="fa fa-circle"></i></li>
                        </ul>
                    </div>
                    <!-- END PAGE BAR -->
                    <!-- BEGIN PAGE CONTENT - MARU - INNER -->
                    <div class="page-content-inner">
                        <div class="portlet light">
                            <div class="portlet-title">
                                <div class="caption">
                                    <i class="fa fa-reorder"></i>
                                    <span class="caption-title">
                                        ${DATAMAP.mchtId} 통합인증 정보 수정
                                    </span>
                                </div>
                            </div>
                            <div class="portlet-body form">
                                <form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/mcht/totalAuth/" method="post">
                                        <div class="form-body row">
                                            <input type="hidden" name="action_type" value="update" data-reg="false" />
                                            <input type="hidden" class="form-control input-sm" data-key="true" name="mchtId" value="${DATAMAP.mchtId}">
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4 req-label">정산유형
                                                </label>
                                                <select id="settleType" name="settleType" class="selectpicker col-sm-6">
                                                    <option value="B+1" selected>B+1</option>
                                                    <option value="D+1">D+1</option>
                                                </select>
                                            </div>
                                            <script type="text/javascript">
                                                document.forms.writeFrm.settleType.value = '${DATAMAP.settleType}'
                                            </script>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4 req-label">주민번호<br> 체크
                                                </label>
                                                <select id="identityCheck" name="identityCheck" class="selectpicker col-sm-6">
                                                    <option value="Y" selected>Y</option>
                                                    <option value="N">N</option>
                                                </select>
                                            </div>
                                            <script type="text/javascript">
                                                document.forms.writeFrm.identityCheck.value = '${DATAMAP.identityCheck}'
                                            </script>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4 req-label">실명인증
                                                </label>
                                                <select id="ownerAuth" name="ownerAuth" class="selectpicker col-sm-6">
                                                    <option value="Y">Y</option>
                                                </select>
                                            </div>
                                            <script type="text/javascript">
                                                document.forms.writeFrm.ownerAuth.value = '${DATAMAP.ownerAuth}'
                                            </script>

                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4 req-label">실명인증<br>수수료</label>
                                                <div class="col-sm-6">
                                                    <div class="input-group input-group-sm">
                                                        <input type="text" class="form-control currency ownerAuthFee comma" maxlength="10" data-oper="comma" name="ownerAuthFee" placeholder="" value="${DATAMAP.ownerAuthFee}">
                                                        <span class="input-group-addon"><i class="fa fa-krw"></i></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4 req-label">계좌 1원인증
                                                </label>
                                                <select id="accountAuth" name="accountAuth" class="selectpicker col-sm-6">
                                                    <option value="Y" selected>Y</option>
                                                    <option value="N">N</option>
                                                </select>
                                            </div>
                                            <script type="text/javascript">
                                                document.forms.writeFrm.accountAuth.value = '${DATAMAP.accountAuth}'
                                            </script>

                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4 req-label">계좌 1원인증<br>수수료</label>
                                                <div class="col-sm-6">
                                                    <div class="input-group input-group-sm">
                                                        <input type="text" class="form-control currency accountAuthFee comma" maxlength="10" data-oper="comma" name="accountAuthFee" placeholder="" value="${DATAMAP.accountAuthFee}">
                                                        <span class="input-group-addon"><i class="fa fa-krw"></i></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4 req-label">ARS인증
                                                </label>
                                                <select id="arsAuth" name="arsAuth" class="selectpicker col-sm-6">
                                                    <option value="Y" selected>Y</option>
                                                    <option value="N">N</option>
                                                </select>
                                            </div>
                                            <script type="text/javascript">
                                                document.forms.writeFrm.arsAuth.value = '${DATAMAP.arsAuth}'
                                            </script>

                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4 req-label">ARS인증<br>수수료</label>
                                                <div class="col-sm-6">
                                                    <div class="input-group input-group-sm">
                                                        <input type="text" class="form-control currency arsAuthFee comma" maxlength="10" data-oper="comma" name="arsAuthFee" placeholder="" value="${DATAMAP.arsAuthFee}">
                                                        <span class="input-group-addon"><i class="fa fa-krw"></i></span>
                                                    </div>
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
        submitHandler: function (form) {
            error1.hide();

            var selectIdentity = document.getElementById("identityCheck");
            var identity = selectIdentity.options[selectIdentity.selectedIndex].value;

            var selectOwner = document.getElementById("ownerAuth");
            var owner = selectOwner.options[selectOwner.selectedIndex].value;

            var selectAccount = document.getElementById("accountAuth");
            var account = selectAccount.options[selectAccount.selectedIndex].value;

            var selectArs = document.getElementById("arsAuth");
            var ars = selectArs.options[selectArs.selectedIndex].value;

            $('.confirm_identity').text("주민번호체크 : " + identity);
            $('.confirm_ownerAuth').text("실명인증 : " + owner);
            $('.confirm_accountAuth').text("계좌1원인증 : " + account);
            $('.confirm_arsAuth').text("ARS인증 : " + ars);

            if(ars == 'Y' && identity == 'N') {
                var error = '<button class="close" data-close="alert"></button>';
                error += "ARS인증을 사용할경우 주민번호 체크도 사용해야됩니다.";
                error1.html(error);
                error1.show();
                App.scrollTo(error1, -200);
                $('.identityCheck').focus();
            } else {
                bootbox.confirm($('#bootbox_confirm').html(), function(result) {
                    if (result) {
                        ajaxFormSubmit(form, '/mcht/view/'+ $('input[name="mchtId"]').val()+'/tab_totalAuth'); //PAGE 이동
                    }
                });
            }
        }
    });

    $('#accountAuth').change(function(e) {
        var val = $(this).find("option:selected").val();

        if(val == 'N') {
            $("#arsAuth option:eq(0)").remove();
            $("#arsAuth option:eq(1)").prop("selected", "selected");
            $("#arsAuth").selectpicker('refresh');
        } else {
            $("#arsAuth option:eq(0)").remove();
            $("#arsAuth option:eq(1)").remove();
            $("#arsAuth").append("<option value='Y'>Y</option>");
            $("#arsAuth").append("<option value='N'>N</option>");
            $("#arsAuth").selectpicker('refresh');
        }
    });

    $(document).ready(function() {
        var val = document.forms.writeFrm.accountAuth.value;

        if(val == 'N') {
            $("#arsAuth option:eq(0)").remove();
            $("#arsAuth option:eq(1)").prop("selected", "selected");
            $("#arsAuth").selectpicker('refresh');
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
            <p>다음과 같은 정보로 통합인증정보를 수정하려고 합니다.</p>
            <p>계속 진행하시겠습니까?</p>
        </div>
        <!-- List group -->
        <ul class="list-group">
            <li class="list-group-item confirm_identity"></li>
            <li class="list-group-item confirm_ownerAuth"></li>
            <li class="list-group-item confirm_accountAuth"></li>
            <li class="list-group-item confirm_arsAuth"></li>
        </ul>
    </div>
</div>
</body>

</html>