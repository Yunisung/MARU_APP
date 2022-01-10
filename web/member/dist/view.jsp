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
									<li><span>멤버관리</span><i class="fa fa-circle"></i></li>
									<li><span>대행사 페이지</span></li>
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
								<div class="portlet-body light">
									<div class="tabbable-line">
										<ul class="nav nav-tabs">
											<li class="active"><a href="#tab_basic" data-toggle="tab" aria-expanded="false"> 기본 </a></li>
											<li class=""><a href="#tab_additional" data-toggle="tab" aria-expanded="false"> 관리자 </a></li>
											<c:if test="${fn:length(DATAMNGMAP) > 0 }">
												<li class=""><a href="#tab_mng" data-toggle="tab" aria-expanded="true"> 지불 및 정산 </a></li>
											</c:if>
										</ul>
										<div class="tab-content">
											<!-- 기본 정보 탭 시작 -->
											<div class="tab-pane active" id="tab_basic">
												<!-- BEGIN FORM-->
												<form class="form-horizontal form" role="form">
													<div class="form-body row">
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">아이디</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.distId}</p>
																</div>
															</div>
														</div>
														<!--/span-->
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">이름</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.name}</p>
																</div>
															</div>
														</div>
														<!--/span-->
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">${DATAMAP.idType}</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.identity}</p>
																</div>
															</div>
														</div>
														<!--/span-->
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">대표이메일</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.email}</p>
																</div>
															</div>
														</div>
														<!--/span-->

														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">업종</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.bizType}</p>
																</div>
															</div>
														</div>
														<!--/span-->
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">업태</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.bizCategory}</p>
																</div>
															</div>
														</div>
														<!--/span-->
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">상태</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.status}</p>
																</div>
															</div>
														</div>
														<!--/span-->
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">등록일시</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.regDate}</p>
																</div>
															</div>
														</div>
														<!--/span-->
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">사업장연락처</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.tel1}</p>
																</div>
															</div>
														</div>
														<!--/span-->
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">사업장연락처2</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.tel2}</p>
																</div>
															</div>
														</div>
														<!--/span-->
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">팩스</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.fax}</p>
																</div>
															</div>
														</div>
														<!--/span-->
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">우편번호</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.zip}</p>

																</div>
															</div>
														</div>
														<!--/span-->
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">주소</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.addr1}</p>
																</div>
															</div>
														</div>
														<!--/span-->
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">상세주소</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.addr2}</p>
																</div>
															</div>
														</div>
														<!--/span-->
													</div>
													<div class="form-actions right def-action">
														<c:if test="${fn:length(DATAMNGMAP) < 1 }">
															<button type="button" class="btn btn-sm red-haze" onclick="location.href='/member/dist/mng/add/${DATAMAP.distId}';">
																<i class="fa fa-pencil"></i> 지불 및 정산정보 등록
															</button>
														</c:if>
														<button type="button" class="btn btn-sm green" onclick="location.href='/member/dist/modify/${DATAMAP.distId}';">
															<i class="fa fa-pencil"></i> 정보 수정
														</button>
													</div>
												</form>
												<!-- END FORM-->
											</div>
											<!-- 기본 정보 탭 종료 -->
											<!-- 관리자 탭 시작 -->
											<div class="tab-pane" id="tab_additional">
												<!-- BEGIN FORM-->
												<form class="form-horizontal form" role="form">
													<div class="form-body row">
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">대표자 이름</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.ceoName}</p>
																</div>
															</div>
														</div>
														<!--/span-->
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">대표자 식별번호</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.ceoIdentity}</p>
																</div>
															</div>
														</div>
														<!--/span-->
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">대표자 휴대폰</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.ceoPhone}</p>
																</div>
															</div>
														</div>
														<!--/span-->
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">대표자 집전화</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.ceoTel}</p>
																</div>
															</div>
														</div>
														<!--/span-->
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">대표자 우편번호</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.ceoZip}</p>
																</div>
															</div>
														</div>
														<!--/span-->
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">대표자 주소</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.ceoAddr1}</p>
																</div>
															</div>
														</div>
														<!--/span-->
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">대표자 상세주소</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.ceoAddr2}</p>
																</div>
															</div>
														</div>
														<!--/span-->
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">담당자 이름</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.managerName}</p>
																</div>
															</div>
														</div>
														<!--/span-->
														<div class="col-md-6">
															<div class="form-group pg-view-group">
																<label class="control-label col-md-3">담당자 전화번호</label>
																<div class="col-md-9">
																	<p class="form-control-static">${DATAMAP.managerPhone}</p>
																</div>
															</div>
														</div>
														<!--/span-->
													</div>
													<div class="form-actions right def-action-mirror"></div>
												</form>
												<!-- END FORM-->
											</div>
											<!-- 지불 및 정산정보 탭 시작 -->
											<c:if test="${fn:length(DATAMNGMAP) > 0 }">
												<div class="tab-pane" id="tab_mng">
													<!-- BEGIN FORM-->
													<form class="form-horizontal form" role="form">
														<div class="form-body row">
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">지불사용여부</label>
																	<div class="col-md-9">
																		<p class="form-control-static">${DATAMNGMAP.payStatus}</p>
																	</div>
																</div>
															</div>
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">대출정산사용여부</label>
																	<div class="col-md-9">
																		<p class="form-control-static">${DATAMNGMAP.loanSettleStatus}</p>
																	</div>
																</div>
															</div>
															<!--/span-->
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">정산유형</label>
																	<div class="col-md-9">
																		<p class="form-control-static">${DATAMNGMAP.settleType}</p>
																	</div>
																</div>
															</div>
															<!--/span-->
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">대행사 수수료</label>
																	<div class="col-md-9">
																		<p class="form-control-static rate">${DATAMNGMAP.rate}<%-- <fmt:formatNumber value="${DATAMNGMAP.rate * 100}" pattern="0.000"/> --%>
																		</p>
																		<%-- <a class="btn btn-sm" href="/member/rate/add/dist/${DATAMAP.distId }">수수료 변경 예약</a> --%>
																	</div>
																</div>
															</div>
															<!--/span-->
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">이체 건당 수수료 <br>(즉시결제시만 사용)
																	</label>
																	<div class="col-md-9">
																		<p class="form-control-static currency"><fmt:formatNumber type="number" value="${DATAMNGMAP.wireFee}" pattern="#,##0" /></p>
																	</div>
																</div>
															</div>
															<!--/span-->
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">1회한도</label>
																	<div class="col-md-9">
																		<p class="form-control-static currency"><fmt:formatNumber type="number" value="${DATAMNGMAP.limitOnce}" pattern="#,##0" /></p>
																	</div>
																</div>
															</div>
															<!--/span-->
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">1일한도</label>
																	<div class="col-md-9">
																		<p class="form-control-static currency"><fmt:formatNumber type="number" value="${DATAMNGMAP.limitDay}" pattern="#,##0" /></p>
																	</div>
																</div>
															</div>
															<!--/span-->
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">1개월한도</label>
																	<div class="col-md-9">
																		<p class="form-control-static currency"><fmt:formatNumber type="number" value="${DATAMNGMAP.limitMonth}" pattern="#,##0" /></p>
																	</div>
																</div>
															</div>
															<!--/span-->
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">은행코드</label>
																	<div class="col-md-9">
																		<p class="form-control-static">${DATAMNGMAP.bankCd}</p>
																	</div>
																</div>
															</div>
															<!--/span-->
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">은행이름</label>
																	<div class="col-md-9">
																		<p class="form-control-static">${DATAMNGMAP.bankName}</p>
																	</div>
																</div>
															</div>
															<!--/span-->
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">계좌번호</label>
																	<div class="col-md-9">
																		<p class="form-control-static">${DATAMNGMAP.account}</p>
																	</div>
																</div>
															</div>
															<!--/span-->
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">예금주</label>
																	<div class="col-md-9">
																		<p class="form-control-static">${DATAMNGMAP.accntHolder}</p>
																	</div>
																</div>
															</div>
															<!--/span-->
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">계좌확인</label>
																	<div class="col-md-9">
																		<p class="form-control-static">${DATAMNGMAP.accntCheck}</p>
																	</div>
																</div>
															</div>
															<!--/span-->
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">계좌확인시간</label>
																	<div class="col-md-9">
																		<p class="form-control-static">${DATAMNGMAP.accntDate}</p>
																	</div>
																</div>
															</div>
															<!--/span-->
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3"> </label>
																	<div class="col-md-9">
																		<p class="form-control-static"></p>
																	</div>
																</div>
															</div>
															<!--/span-->
														</div>
													</form>
													<div class="form-actions">
														<div class="row">
															<div class="col-md-12">
																<button type="button" class="btn btn-sm green pull-right" onclick="location.href='/member/dist/mng/modify/${DATAMAP.distId}';">
																	<i class="fa fa-pencil"></i> 지불 및 정산정보 수정
																</button>
															</div>
															<div class="col-md-6"></div>
														</div>
													</div>
												</div>
											</c:if>
											<!-- 지불 및 정산정보 탭 종료 -->
										</div>
									</div>
								</div>
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
	<c:import url="/include/javascript.jsp" />
	<script type="text/javascript">
		$('.form-actions.def-action-mirror').html(
				$('.form-actions.def-action').html());
		
		var rate = '${DATAMNGMAP.rate}';
		rate = rate.split(",");
		var rate2 = "";
		for(var i=0; i<rate.length; i++){
			if(i < rate.length-1){
				rate2 += (rate[i]*100).toFixed(3)+',';
			} else {
				rate2 += (rate[i]*100).toFixed(3);
			}
		}
		$('.rate').text(rate2+'%');
		
		$('#nav-member').addClass('active');
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>