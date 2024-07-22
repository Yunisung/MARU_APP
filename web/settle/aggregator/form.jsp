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
									<li><span>정산관리</span><i class="fa fa-circle"></i></li>
                  <li><span>대표가맹점 정산 조회</span></li>
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
								<div class="page-content-inner" id="search-container">
									<!-- 검색 폼 시작 -->
									<div class="portlet light portlet-form">
										<div class="portlet-body form light">
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/settle/aggregator/list" method="post">
												<input type="hidden" data-reg="false" name="reason" value="정산 리스트">
												<c:if test="${CP_SESSION.grade == '본사'}">
												<input type="hidden" data-reg="false" name="thead" value="stlId:정산번호,tmnId:아이디,tmnRate:수수료율,dtlName:상호,mchtId:가맹점ID,mchtName:가맹점,status:확정상태,payStatus:지급상태,startDay:거래시작일,endDay:거래종료일,payAmt:승인금액,payFee:승인수수료,payVat:승인VAT,payCnt:승인거래량,rfdAmt:취소금액,rfdFee:취소수수료,rfdVat:취소VAT,rfdCnt:취소거래량,stlAmt:정산금액,stlDay:정산예정일,payOutDay:지급일,bankCd:은행번호,bankName:은행,account:계좌번호,accntHolder:예금주,regDate:등록일시">
												</c:if>
												<c:if test="${CP_SESSION.grade != '본사'}">
												<input type="hidden" data-reg="false" name="thead" value="stlId:정산번호,tmnId:아이디,dtlName:상호,mchtId:가맹점ID,mchtName:가맹점,status:확정상태,payStatus:지급상태,startDay:거래시작일,endDay:거래종료일,payAmt:승인금액,payFee:승인수수료,payVat:승인VAT,payCnt:승인거래량,rfdAmt:취소금액,rfdFee:취소수수료,rfdVat:취소VAT,rfdCnt:취소거래량,stlAmt:정산금액,stlDay:정산예정일,payOutDay:지급일,bankCd:은행번호,bankName:은행,account:계좌번호,accntHolder:예금주,regDate:등록일시">
												</c:if>
												<div class="form-body">
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점ID</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm mchtId" name="mchtId" data-oper="lk" placeholder="아이디">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점명</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm mchtName" name="mchtName" data-oper="lk" placeholder="이름">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">정산번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm stlId" name="stlId" data-oper="eq" placeholder="정산번호">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">정산예정일</label>
															<div class=" col-lg-8">
																<div class="input-group input-group-sm input-daterange" data-date-format="yyyy-mm-dd">
																	<input type="text" class="form-control now-date" name="stlDay" value="" data-oper="ge">
																	<span class="input-group-addon">~</span>
																	<input type="text" class="form-control now-date" name="stlDay" value="" data-oper="le">
																</div>
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">확정 상태</label>
															<select class="selectpicker col-lg-8" name="status" data-oper="eq">
																<option value="">-- 전체 --</option>
																<option value="대기">대기</option>
																<option value="확정">확정</option>
																<option value="보류">보류</option>
															</select>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">지급 상태</label>
															<select class="selectpicker col-lg-8" name="payStatus" data-oper="eq">
																<option value="">-- 전체 --</option>
																<option value="대기">대기</option>
																<option value="지급완료">지급완료</option>
																<option value="지급실패">지급실패</option>
															</select>
														</div>
													</div>
												</div>
												<div class="form-actions nobg right">
													<div class="btn folding-search-btn icon-arrow-down"></div>
													<div class="">
														<button type="button" class="btn btn-sm blue-dark" id="SearchClear">
															<i class="fa fa-eraser" aria-hidden="true"></i> RESET&nbsp;
														</button>
														<button type="button" class="btn btn-sm green" id="search_submit" onClick="searchForList()">
															<i class="fa fa-search" aria-hidden="true"></i> SEARCH
														</button>
													</div>
												</div>
											</form>
										</div>
									</div>
									<!-- 검색 폼 종료 -->
									<!-- 내용 폼 시작 -->
									<div class="portlet light portlet-form" id="searchResult"></div>
									<!-- 내용 폼 종료 -->
								</div>
								<!-- END PAGE CONTENT INNER -->
							</div>
						</div>
					</div>
				<!-- BEGIN CONTAINER -->
		</div>
		<c:import url="/include/footer.jsp" />
	</div>
	<c:import url="/include/javascript.jsp" />

	<script type="text/javascript">
		gradeSelector('searchForm', '${CP_SESSION.grade}');
		setTimeout(function(){ searchForList(); }, 100); //검색 실행
		$('#nav-settle-mcht').addClass('active');

		function saveSettle(stlId){
			var outColList = [];

			$('#searchResult tr').each(function() {
				if($(this).find('.out-col-stlId').text() == '') {
					// EMPTY ROW
				} else if(stlId == '' || $(this).find('.out-col-stlId').text() == stlId) {
					var outColObj = {};
					outColObj.stlId = $(this).find('.out-col-stlId').text();
					outColObj.payOutAmt = String($(this).find('.collect-input').val()).replace(/,/g, '');
					outColObj.summary = $(this).find('.summary').val();
					outColList.push(outColObj);
					return;
				}
			});
			console.log(outColList);
			var msg = (stlId == '' ? '전체 항목을 저장 하시겠습니까?' : '해당 항목을 저장 하시겠습니까?');

			bootbox.confirm(msg, function(result) {
				if (result) {
					$.ajax({
						url: '/settle/aggregator/save',
						dataType : "text",
						beforeSend : function(xhr) {
							xhr.setRequestHeader("Content-type",
									"application/json;charset=utf-8");
						},
						method:'post',
						data: JSON.stringify(outColList),
						success: function(res, stat) {
							res = JSON.parse(res);
							if(res.result == 'OK') {
								bootbox.alert("수정이 완료되었습니다.",function(){
									// location.reload();
								});

							} else {
								bootbox.alert("수정에 실패하였습니다.\n" + res.msg);
							}
						},
						error : function(xhr, status, error) {
							bootbox.alert("수정에 실패하였습니다.");
						}
					});
				}
			});
		}
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>