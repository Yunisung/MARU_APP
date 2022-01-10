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
                                <li><span>충전정산</span><i class="fa fa-circle"></i></li>
                                <li><span>수기동록</span></li>
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
                                        <span class="caption-title"> ${MCHTMAP.name } 가맹점 충전정산 수기등록 </span>
                                    </div>
                                </div>

                                <div class="portlet-body form">
                                    <form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/mcht/balance/" method="post">
                                        <input type="hidden" name="action_type" value="insert" data-reg="false" />
                                        <!-- 업데이트 구문 -->
										<input type="hidden" name="mchtId" value="${MCHTMAP.mchtId}"/>
											<div class="form-group">
												<label for="balance" class="col-md-2 control-label">현재잔액</label>
												<div class="col-md-4">
													<p id="balance" class="form-control-static digits">${DATAMAP.balance }</p> 
												</div>
											</div>
											<div class="form-group">
												<label for="trxType" class="col-md-2 control-label">거래구분</label>
												<select name="trxType" class="selectpicker col-sm-2">
													<option value="">선택</option>
													<option value="입금">입금</option>
													<option value="출금">출금</option>
												</select>
											</div>
											<div class="form-group">
												<label for="amount" class="col-md-2 control-label">금액</label>
												<div class="col-md-4">
													<input type="text" name="amount" class="form-control" id="amount" value="0">
												</div>
											</div>
											<div class="form-group">
												<label for="comment" class="col-md-2 control-label">기재내역 </label>
												<div class="col-md-8">
													<textarea rows="4" name="comment" id="lsummary" placeholder="Write comment here ..." class="form-control input-md"></textarea>
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
                        <!-- END PAGE CONTENT INNER -->
                    </div>
                </div>
            </div>
            <!-- BEGIN CONTAINER -->
        </div>
        <c:import url="/include/footer.jsp" />
    </div>
    <script src="https://ssl.daumcdn.net/dmaps/map_js_init/postcode.v2.js"></script>
    <c:import url="/include/javascript.jsp" />
    
    <!-- BEGIN FORM JAVASCRIPT -->
    <script type="text/javascript">
        var form1 = $('#writeFrm');
        var error1 = $('.alert-danger', form1);
        form1.validate({
            rules: {
                trxType:{
                	required: true	
                },
                amount: {
                    money: true,
                    min:1
                },
                comment: {
                    required: true
                }
                
            },
            invalidHandler: function(event, validator) { //display error alert on form submit              
                var error1Str = '<button class="close" data-close="alert"></button>';
                error1Str += "등록 중 잘못된 입력값이 있습니다. 위의 입력 값을 다시 확인하여 주시기 바랍니다.";
                error1.html(error1Str);
                error1.show();
                //App.scrollTo(error1, -200);
            },
            submitHandler: function(form) {
                error1.hide();
                bootbox.confirm("입력하신 정보로 ${DATAMAP.name} 충전정산 수기등록을 하시겠습니까?", function(result) {
                    if (result) {
                        ajaxFormSubmit(form, '/mcht/view/${DATAMAP.mchtId}/tab_chargeMng'); //PAGE 이동
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