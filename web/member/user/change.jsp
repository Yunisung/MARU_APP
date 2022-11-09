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
                            <li><span>맴버 관리</span><i class="fa fa-circle"></i></li>
                            <li><span>사용자 소속 변경</span></li>
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
                                    <span class="caption-title"> 사용자 소속 변경 </span>
                                </div>
                            </div>

                            <div class="portlet-body form">
                                <form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/member/user/change/" method="post">
                                    <input type="hidden" name="action_type" value="update" data-reg="false" />
                                    <div class="form-body row">
                                        <div class="form-group col-sm-12 form-subtitle">
                                            <label><i class="fa fa-reorder"></i> 기본 정보 입력</label>
                                        </div>
                                        <c:if test="${empty isAdmin && empty isMcht}">
                                            <c:import url="/common/selectGrade.jsp" />
                                        </c:if>
                                        <div class="form-group col-sm-6">
                                            <label class="control-label col-sm-4 req-label">아이디 </label>
                                            <div class="col-sm-6">
                                                <input type="text" class="form-control input-sm" maxlength="50" name="id" data-key="true" value="${DATAMAP.id }" readonly>
                                            </div>
                                        </div>
                                        <div class="form-group col-sm-6">
                                            <label class="control-label input-sm col-sm-4 req-label">이름</label>
                                            <div class="col-sm-6">
                                                <input type="text" class="form-control input-sm name" maxlength="100" name="name" data-reg="false" value="${DATAMAP.name }" readonly>
                                            </div>
                                        </div>
                                        <div class="form-group col-sm-6">
                                            <label class="control-label input-sm col-sm-4 req-label">현재 소속</label>
                                            <div class="col-sm-6">
                                                <input type="text" class="form-control input-sm name" maxlength="100" name="name" data-reg="false" value="${parentName}" readonly>
                                            </div>
                                        </div>
                                        <input type="hidden" name="grade" value="<c:if test="${not empty isAdmin}">본사</c:if><c:if test="${not empty isMcht}">가맹점</c:if>" />
                                        <c:if test="${empty isAdmin}">
                                            <input type="hidden" class="ptid" name="parentId" value="<c:if test="${not empty isMcht}">${DATAMAP.mchtId }</c:if>" />
                                            <input type="hidden" name="parentName" data-reg="false" value="<c:if test="${not empty isMcht}">${DATAMAP.name }</c:if>" />
                                        </c:if>
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
        rules : {
            salesId : {
                equals: "${DATAMAP.salesId}"
            }
        },
        invalidHandler: function (event, validator) { //display error alert on form submit
            var error1Str = '<button class="close" data-close="alert"></button>';
            error1Str += "등록 중 잘못된 입력값이 있습니다. 기존과 동일한 소속을 선택할 수 없습니다.";
            error1.html(error1Str);
            error1.show();
            App.scrollTo(error1, -200);
        },
        submitHandler: function (form) {
            error1.hide();
            bootbox.confirm("입력하신 정보로 사용자 소속을 변경하시겠습니까?", function(result) {
                if (result) {
                    ajaxFormSubmit(form, '/member/user/view/'+ form1.find('input[name="id"]').val()); //PAGE 이동
                }
            });
        }
    });

    <c:if test="${empty isAdmin && empty isMcht}">
    gradeSelector('writeFrm', '${CP_SESSION.grade}', '${DATAMAP.grade}');
    $.validator.addClassRules({ ptid : { required : true } });
    </c:if>
    $('#nav-member').addClass('active');
</script>
<!-- END FORM JAVASCRIPT -->
<!-- 모달 생성을 위한 베이스 -->
<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>