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
									<li><span>대출 정산</span><i class="fa fa-circle"></i></li>
                  <li><span>대출 정산 실행</span></li>
							</ul>
							<div class="page-toolbar">
									<div class="btn-group btn-theme-panel">
										<a class="btn float-window"><i class="icon-size-fullscreen"></i></a>
										<a href="javascript:;" class="btn dropdown-toggle" data-toggle="dropdown">
											<i class="icon-settings"></i>
										</a>
										<div class="dropdown-menu theme-panel pull-right dropdown-custom hold-on-click panel-warning">
											<div class="panel-heading">도움말</div>
											<div class="panel-body">
											</div>
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
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/loanSettle/settle/list" method="post">
												<input type="hidden" data-reg="false" name="reason" value="가맹점 정산 리스트">
												<c:if test="${CP_SESSION.grade eq '본사' }">
												<input type="hidden" data-reg="false" name="thead" value="loanStlId:대출정산번호,payStatus:지급상태,payOutDay:지급일,grade:소속,memberId:소속ID,name:소속명,ceoName:대표자명,payAmt:지급금액,bankName:은행이름,account:지급계좌,accntHolder:예금주명">
												</c:if>
												<div class="form-body">
													<c:if test="${CP_SESSION.grade eq '본사' }">
														<div class="row">
																<div class="form-group pg-form-group">
																	<label class="control-label col-lg-4">소속</label>
																	<select class="selectpicker col-lg-8 grade" name="grade" data-oper="eq">
																		<option value="">-- 전체 --</option>
																		<option value="대행사">대행사</option>
																		<option value="에이전시">에이전시</option>
																		<option value="지사">지사</option>
																	</select>
																</div>
															
															<div class="form-group pg-form-group">
																<label class="control-label col-lg-4">소속ID</label>
																<div class="col-lg-8">
																	<input type="text" class="form-control input-sm memberId" name="memberId" data-oper="lk" placeholder="소속ID">
																</div>
															</div>
															<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">소속명</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm name typeahead" name="name" data-oper="lk" placeholder="소속명">
															</div>
														</div>
															<div class="form-group pg-form-group">
																<label class="control-label col-lg-4">소속 대표자</label>
																<div class="col-lg-8">
																	<input type="text" class="form-control input-sm ceoName" name="ceoName" data-oper="lk" placeholder="소속 대표자">
																</div>
															</div>
														</div>
													</c:if>
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">정산일(지급일)</label>
															<div class=" col-lg-8">
																<div class="input-group input-group-sm input-daterange" data-date-format="yyyy-mm-dd">
																	<input type="text" class="form-control now-date" name="payOutDay" value="" data-oper="ge">
																	<span class="input-group-addon">~</span>
																	<input type="text" class="form-control now-date" name="payOutDay" value="" data-oper="le">
																</div>
															</div>
														</div>
														<c:if test="${CP_SESSION.grade eq '본사' }">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">대출정산번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm loanStlId" name="loanStlId" data-oper="eq" placeholder="대출정산번호">
															</div>
														</div>
														</c:if>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">지급 상태</label>
															<select class="selectpicker col-lg-8 payStatus" name="payStatus" data-oper="eq">
																<option value="">-- 전체 --</option>
																<option value="지급대기">지급대기</option>
																<option value="지급완료">지급완료</option>
																<option value="지급보류">지급보류</option>
															</select>
														</div>
													</div>
												</div>
												<div class="form-actions nobg right">
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
		<iframe id="txtArea1" style="display:none"></iframe>
		<c:import url="/include/footer.jsp" />
	</div>
	<c:import url="/include/javascript.jsp" />
	<script type="text/javascript">
		setTimeout(function(){ 
			searchForList(); 
		}, 100); //검색 실행
		$('#SearchForWaiting').click(function() {
			//$('#pageSize').val(100); 200개로 Controller 에서 설정됨.
			//$('#SearchClear').trigger('click');
			$('.selectpicker.status').selectpicker('refresh');
			searchForList();
		});
		
		$('#nav-loan').addClass('active');

		
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>