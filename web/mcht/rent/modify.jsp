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
                            <li><span>월세앱 정보 수정</span><i class="fa fa-circle"></i></li>
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
                                        ${MCHT_MAP.mchtId} 월세앱 정보 수정
                                    </span>
                                </div>
                            </div>
                            <div class="portlet-body form">
                                <form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/mcht/rent/" method="post">
                                    <div class="form-body row">
                                        <input type="hidden" name="action_type" value="update" data-reg="false" />
                                        <input type="hidden" class="form-control input-sm" name="mchtId" data-key="true" value="${MCHT_MAP.mchtId}">
                                        <div class="form-body row">
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4 req-label">월세 원금</label>
                                                <div class="col-sm-6">
                                                    <div class="input-group input-group-sm">
                                                        <input type="text" class="form-control currency amount comma" maxlength="10" data-oper="comma" name="rentAmount" placeholder="" value="${RENTMAP.rentAmount}">
                                                        <span class="input-group-addon"><i class="fa fa-krw"></i></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4 req-label">보증금 원금</label>
                                                <div class="col-sm-6">
                                                    <div class="input-group input-group-sm">
                                                        <input type="text" class="form-control currency amount comma" maxlength="10" data-oper="comma" name="depositAmount" placeholder="" value="${RENTMAP.depositAmount}">
                                                        <span class="input-group-addon"><i class="fa fa-krw"></i></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4 req-label">가맹점 정산유형</label>
                                                <select name="settleType" class="selectpicker col-sm-6">
                                                    <option value="C+0">실시간 충전정산</option>
                                                    <option value="B+1">1일 후 자동충전정산</option>
                                                    <option value="C+1">1일 후 충전정산</option>
                                                    <option value="C+2">2일 후 충전정산</option>
                                                    <option value="C+3">3일 후 충전정산</option>
                                                    <option value="C+4">4일 후 충전정산</option>
                                                    <option value="C+5">5일 후 충전정산</option>
                                                </select>
                                                <script type="text/javascript"> document.forms.writeFrm.settleType.value = '${RENTMAP.settleType}' </script>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4">계약구분</label>
                                                <select name="contractType" class="selectpicker col-sm-6">
                                                    <option value="임대인" selected>임대인</option>
                                                    <option value="임차인" selected>임차인</option>
                                                </select>
                                                <script type="text/javascript"> document.forms.writeFrm.contractType.value = '${RENTMAP.contractType}' </script>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label input-sm col-sm-4">이체예정일</label>
                                                <div class="col-sm-6">
                                                    <input type="text" class="form-control input-sm transferDay" maxlength="100" name="transferDay" onkeyup="commas(this)" value="${RENTMAP.transferDay}">
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4">계약검토상태</label>
                                                <select name="contractStatus" class="selectpicker col-sm-6">
                                                    <option value="대기" selected>대기</option>
                                                    <option value="완료" selected>완료</option>
                                                </select>
                                                <script type="text/javascript"> document.forms.writeFrm.contractStatus.value = '${RENTMAP.contractStatus}' </script>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4 req-label">월세 1회한도</label>
                                                <div class="col-sm-6">
                                                    <div class="input-group input-group-sm">
                                                        <input type="text" class="form-control currency limitOnce" maxlength="12" name="rentLimitOnce" placeholder="" value="${RENTMAP.rentLimitOnce}">
                                                        <span class="input-group-addon"><i class="fa fa-krw"></i></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4 req-label">월세 1개월한도</label>
                                                <div class="col-sm-6">
                                                    <div class="input-group input-group-sm">
                                                        <input type="text" class="form-control input-sm limitMonth currency" maxlength="15" name="rentLimitMonth" placeholder="" value="${RENTMAP.rentLimitMonth}">
                                                        <span class="input-group-addon"><i class="fa fa-krw"></i></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4 req-label">보증금 1회한도</label>
                                                <div class="col-sm-6">
                                                    <div class="input-group input-group-sm">
                                                        <input type="text" class="form-control currency limitOnce" maxlength="12" name="depositLimitOnce" placeholder="" value="${RENTMAP.depositLimitOnce}">
                                                        <span class="input-group-addon"><i class="fa fa-krw"></i></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4 req-label">보증금 1개월한도</label>
                                                <div class="col-sm-6">
                                                    <div class="input-group input-group-sm">
                                                        <input type="text" class="form-control input-sm limitMonth currency" maxlength="15" name="depositLimitMonth" placeholder="" value="${RENTMAP.depositLimitMonth}">
                                                        <span class="input-group-addon"><i class="fa fa-krw"></i></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4">월세 이용수수료율</label>
                                                <div class="col-sm-6">
                                                    <div class="input-group input-group-sm">
                                                        <input type="text" class="form-control rate percent" maxlength="9" data-oper="percent" name="rentRate" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="${RENTMAP.rentRate}">
                                                        <span class="input-group-addon"> % (VAT 별도)</span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4">보증금 이용수수료율</label>
                                                <div class="col-sm-6">
                                                    <div class="input-group input-group-sm">
                                                        <input type="text" class="form-control rate percent" maxlength="9" data-oper="percent" name="depositRate" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="${RENTMAP.depositRate}">
                                                        <span class="input-group-addon"> % (VAT 별도)</span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label col-sm-4">대행사 월세 수수료율</label>
                                                <div class="col-sm-6">
                                                    <div class="input-group input-group-sm">
                                                        <input type="text" class="form-control rate percent" maxlength="9" data-oper="percent" name="distRentRate" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="${RENTMAP.distRentRate}">
                                                        <span class="input-group-addon"> % (VAT 별도)</span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-6" id="rateDiv">
                                                <label class="control-label col-sm-4">대행사 보증금 수수료율</label>
                                                <div class="col-sm-6">
                                                    <div class="input-group input-group-sm">
                                                        <input type="text" class="form-control rate percent" maxlength="9" data-oper="percent" name="distDepositRate" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="${RENTMAP.distDepositRate}">
                                                        <span class="input-group-addon"> % (VAT 별도)</span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label input-sm col-sm-4">송금자명</label>
                                                <div class="col-sm-6">
                                                    <input type="text" class="form-control input-sm sender" maxlength="100" name="sender" value="${RENTMAP.sender}">
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label input-sm col-sm-4">보류노티 주소</label>
                                                <div class="col-sm-6">
                                                    <input type="text" class="form-control input-sm riskChangeNotiAddr" maxlength="100" name="riskChangeNotiAddr" placeholder="api.example.com" value="${RENTMAP.riskChangeNotiAddr}">
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-6">
                                                <label class="control-label input-sm col-sm-4">가맹점정산 노티 주소</label>
                                                <div class="col-sm-6">
                                                    <input type="text" class="form-control input-sm mchtSettleNotiAddr" maxlength="100" name="mchtSettleNotiAddr" placeholder="api.example.com" value="${RENTMAP.mchtSettleNotiAddr}">
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

    function commas(t) {
        // 콤마 빼고
        var x = t.value;
        x = x.replace(/,/gi, '');

        // 숫자 정규식 확인
        var regexp = /^[0-9]*$/;

        if(!regexp.test(x)){
            $(t).val("");
            alert("숫자만 입력 가능합니다.");
        } else {
            x = x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");

            $(t).val(x);
        }
    }

    var form1 = $('#writeFrm');
    var error1 = $('.alert-danger', form1);
    form1.validate({
        submitHandler: function (form) {
            error1.hide();

            bootbox.confirm("입력하신 정보로 ${MCHT_MAP.name} 가맹점 월세 정보를 수정 하시겠습니까?", function(result) {
                if (result) {
                    ajaxFormSubmit(form, '/mcht/view/${DATAMAP.mchtId}/tab_rent'); //PAGE 이동
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