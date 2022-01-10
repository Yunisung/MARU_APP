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
									<li><span>차감정산 내역</span></li>
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
												<i class="fa fa-reorder"></i> <span class="caption-title">
													차감정산 내역 </span>
											</div>
										</div>

										<div class="portlet-body form">
											<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/mcht/ddct/" method="post">
												
												<div class="form-body row">
													<input type="hidden" name="ddctId" data-key="true" value="${DATAMAP.ddctId }"/>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">정기 차감 항목</label>
														<div class="col-sm-6">
															<p class="form-control-static">${DATAMAP.ddctName }</p> 
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">정기 차감 금액</label>
														<div class="col-sm-6">
															<p class="form-control-static digits">${DATAMAP.monthlyAmt }</p>
														</div>
													</div>
													
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">정기 차감 시작월</label>
														<div class="col-sm-6">
															<p class="form-control-static">${DATAMAP.startMonth}</p>
														</div>
													</div>
													
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">정기 차감 종료월</label>
														<div class="col-sm-6">
															<p class="form-control-static">${DATAMAP.endMonth}</p>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">정기 차감 적용일자</label>
														<div class="col-sm-6">
															<p class="form-control-static">${DATAMAP.settleType}</p>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4">차감정산상태</label>
														<div class="col-sm-6">
															<p class="form-control-static">${DATAMAP.status }</p> 
														</div>
													</div>
												</div>
												<div class="row">
													<div class="form-group col-sm-12 text-center">
														<h5>* 차감현황  총 금액 : <fmt:setLocale value="ko_KR"/><fmt:formatNumber type="currency" value="${DATAMAP.totalAmt }"/>
														<c:set var="var1" value="0"/>
														<c:forEach items="${SCHELIST }" var="entry">
															<c:if test="${entry.stlStatus eq '정산완료'}">
																<c:set var="var1"  value="${var1 +  entry.ddctAmt}"/>
															</c:if>
														</c:forEach>
														차감금액 : <fmt:formatNumber type="currency" value="${var1 }"/>
														잔여금액 : <fmt:formatNumber type="currency" value="${DATAMAP.totalAmt - var1 }"/>
														</h5>
													</div>
												</div>
												<div class="alert alert-danger display-hide"></div>
												
											</form>
										</div>
										<!-- END ADD FORM TABLE-->
										<div class="portlet-title">
											<div class="caption">
												<i class="fa fa-reorder"></i> <span class="caption-title">
													차감정산 일정 </span>
											</div>
										</div>
										<div class="table-scrollable">
										<!-- 스케쥴 시작 -->
											<table class="pg-table table table-bordered table-hover flip-content">
												<!-- table-bordered -->
												<thead>
													<tr>
														<th>No.</th>
														<th>차감정산예정일</th>
														<th>차감금액</th>
														<th>정산여부</th>
													</tr>
												</thead>
												<tbody id="list">
													<c:forEach var="entry" items="${SCHELIST}" varStatus="status">
													<tr>
														<td>${status.count}</td>
														<td class="date">${entry.stlDay}</td>
														<td class="text-right"><fmt:formatNumber type="number" value="${entry.ddctAmt}" pattern="#,##0" /></td>
														<td class="text-right">${entry.stlStatus}</td>
													</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>
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
		$('#nav-mcht').addClass('active');
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container"
		data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>