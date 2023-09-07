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
                            <li><span>정기결제 정보 수정</span><i class="fa fa-circle"></i></li>
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
                                        ${MCHT_MAP.mchtId} 정기결제 정보 수정
                                    </span>
                                </div>
                            </div>
                            <div class="portlet-body form">
                                <form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/mcht/rebill/" method="post">
                                    <div class="form-body row">
                                        <input type="hidden" name="action_type" value="update" data-reg="false" />
                                        <input type="hidden" class="form-control input-sm" name="mchtId" data-key="true" value="${MCHT_MAP.mchtId}">
                                        <div class="form-body row">
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4 req-label">상태</label>
                                                <select id="status" name="status" class="selectpicker col-sm-6">
                                                    <option value="사용" selected>사용</option>
                                                    <option value="중지">중지</option>
                                                </select>
                                                <script type="text/javascript"> document.forms.writeFrm.status.value = '${REBILLMAP.status}'</script>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4 req-label">금액</label>
                                                <div class="col-sm-6">
                                                    <div class="input-group input-group-sm">
                                                        <input type="text" class="form-control currency amount comma" maxlength="10" data-oper="comma" name="amount" placeholder="" value="${REBILLMAP.amount}">
                                                        <span class="input-group-addon"><i class="fa fa-krw"></i></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4 req-label">정기결제일
                                                </label>
                                                <select id="rebillCycle" name="rebillCycle" class="selectpicker col-sm-6">
                                                    <option value="1" selected>1일</option>
                                                    <option value="2">2일</option>
                                                    <option value="3">3일</option>
                                                    <option value="4">4일</option>
                                                    <option value="5">5일</option>
                                                    <option value="6">6일</option>
                                                    <option value="7">7일</option>
                                                    <option value="8">8일</option>
                                                    <option value="9">9일</option>
                                                    <option value="10">10일</option>
                                                    <option value="11">11일</option>
                                                    <option value="12">12일</option>
                                                    <option value="13">13일</option>
                                                    <option value="14">14일</option>
                                                    <option value="15">15일</option>
                                                    <option value="16">16일</option>
                                                    <option value="17">17일</option>
                                                    <option value="18">18일</option>
                                                    <option value="19">19일</option>
                                                    <option value="20">20일</option>
                                                    <option value="21">21일</option>
                                                    <option value="22">22일</option>
                                                    <option value="23">23일</option>
                                                    <option value="24">24일</option>
                                                    <option value="25">25일</option>
                                                    <option value="26">26일</option>
                                                    <option value="27">27일</option>
                                                    <option value="28">28일</option>
                                                    <option value="29">29일</option>
                                                    <option value="30">30일</option>
                                                    <option value="31">31일</option>
                                                </select>
                                                <script type="text/javascript"> document.forms.writeFrm.rebillCycle.value = '${REBILLMAP.rebillCycle}'</script>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4">거래전달주소</label>
                                                <div class="col-sm-6">
                                                    <input type="text" class="form-control input-sm" maxlength="100" name="hookAddr" placeholder="http://api.example.com" value="${REBILLMAP.hookAddr}">
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4 req-label">시작일자</label>
                                                <div class="col-md-4 col-sm-6">
                                                    <input type="text" class="form-control input-sm datepicker activeDate now-date" maxlength="10" name="activeDate" placeholder="" value="">
                                                </div>
                                                <script type="text/javascript"> document.forms.writeFrm.activeDate.value = '${REBILLMAP.activeDate}' </script>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4 req-label">만료일자</label>
                                                <div class="col-md-4 col-sm-6">
                                                    <input type="text" class="form-control input-sm datepicker expireDate rebill-expire-date" maxlength="10" name="expireDate" placeholder="" value="">
                                                </div>
                                                <script type="text/javascript"> document.forms.writeFrm.expireDate.value = '${REBILLMAP.expireDate}' </script>
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
        submitHandler: function (form) {
            error1.hide();

            bootbox.confirm("입력하신 정보로 ${MCHT_MAP.name} 가맹점 정기결제 정보를 수정 하시겠습니까?", function(result) {
                if (result) {
                    ajaxFormSubmit(form, '/mcht/view/${DATAMAP.mchtId}/tab_rebill'); //PAGE 이동
                }
            });
        }
    });


    $('#nav-mcht').addClass('active');
</script>
<!-- END FORM JAVASCRIPT -->
<!-- 모달 생성을 위한 베이스 -->
<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>