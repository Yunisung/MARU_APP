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
									<li><span>거래관리</span><i class="fa fa-circle"></i></li>
                  <li><span>거래 내역 업로드</span></li>
							</ul>
							<div class="page-toolbar">
									<div class="btn-group btn-theme-panel">
										<a class="btn float-window"><i class="icon-size-fullscreen"></i></a>
										<a href="javascript:;" class="btn dropdown-toggle" data-toggle="dropdown">
											<i class="icon-settings"></i>
										</a>
										<div class="dropdown-menu theme-panel pull-right dropdown-custom hold-on-click panel-warning">
											<div class="panel-heading">도움말</div>
											<div class="panel-body"></div>
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
													<div class="row">
														<div class="fileinput fileinput-new pull-right" data-provides="fileinput" style="width:40%;">
															<div class="input-group input-group-sm">
																<div class="form-control uneditable-input input-fixed " data-trigger="fileinput">
																	<i class="fa fa-file fileinput-exists"></i>&nbsp;
																	<span class="fileinput-filename">
																	</span>
																</div>
																<span class="input-group-addon btn blue btn-file">
																	<span class="fileinput-new"> Excel 업로드 </span>
																	<span class="fileinput-exists"> 변경 </span>
																	<input type="hidden">
																	<input type="file" name="upload-excel" id="upload-excel">
																</span>
																<a href="javascript:;" class="input-group-addon btn red fileinput-exists" data-dismiss="fileinput"> 제거 </a>
															</div>
														</div>
														<div class="form-group pull-right" style="width:20%;">
															<select name="tmnId" id="tmnId" class="selectpicker tmnId col-sm-12">
																<option value="">-- 터미널 선택 --</option>
															</select>
														</div>
														<div class="form-group pull-right" style="width:20%;">
															<select name="mchtId" id="mchtId" class="selectpicker mchtId col-sm-12">
																<option value="">-- 가맹점 선택 --</option>
															</select>
														</div>
														<div class="form-group pull-right" style="width:20%;">
															<select class="selectpicker van" name="van" id="van" data-oper="eq">
																<option value="">-- VAN 선택 --</option>
																<option value="SBANK">세틀뱅크</option>
																<option value="IDM">갤럭시아</option>
																<option value="CFAOFF">여신협회</option>
																<option value="PAYNURIOFF">페이누리 오프</option>
																<option value="PAYNURION">페이누리 온</option>
																<option value="DAOUPAYOFF">다우페이</option>
																<option value="WOORIPAY">우리페이</option>
															</select>
														</div>
													</div>
													<div class="row">
														<div class="pull-right">
															<button type="button" class="btn btn-sm green right" onClick="importServer();">
																<i class="fa fa-check" aria-hidden="true"></i> 업로드
															</button>
														</div>
													</div>
												</div>
											</div>
										</div>
										<!-- 검색 폼 종료 -->
										<!-- 내용 폼 시작 -->
										<div class="portlet light portlet-form" id="searchResult">
											<div class="portlet-title">
												<div class="caption font-red-sunglo">
													<i class="icon-share font-red-sunglo"></i>
													<span class="caption-subject bold uppercase"> Result </span>
													<span class="caption-helper font-dark"> 승인: <span class="digits" id="cap-cnt"></span> 건 / <span class="digits" id="cap-amt"></span> 원, </span>
													<span class="caption-helper font-dark"> 취소: <span class="digits" id="rfd-cnt"></span> 건 / <span class="digits" id="rfd-amt"></span> 원, </span>
													<span class="caption-helper font-dark"> 제외: <span class="digits" id="exp-cnt"></span> 건 / <span class="digits" id="exp-amt"></span> 원 </span>
												</div>
											</div>
											<div class="portlet-body form light">
												<div class="table-scrollable">
													<!-- 리스트 본문 시작 -->
													<table class="pg-table table table-striped table-hover flip-content" id="sortTable">
														<thead>
															<tr>
																<th>No</th>
																<th>터미널ID</th>
																<th>가맹점ID</th>
																<th>거래구분</th><%-- 승인/승인취소 --%>
																<th>금액</th>
																<th>할부</th>
																<th>BIN</th>
																<th>LAST4</th>
																
																<th>승인번호</th><%-- 원승인번호 --%>
																<th>거래일자</th>
																<th>거래시간</th>
																
																<th>원거래일자</th>
																<th>일련번호</th>
															</tr>
														</thead>
														<tbody id="list">
															<tr class="hide">
																<td class="no"></td>
																<td class="tmnId"></td>
																<td class="mchtId"></td>
																<td class="trnType"></td><%-- 승인/승인취소 --%>
																<td class="amount digits"></td>
																<td class="installment"></td>
																<td class="bin"></td>
																<td class="last4"></td>
																<td class="authCd"></td><%-- 원승인번호 --%>
																<td class="trxDay date"></td>
																<td class="trxTime time"></td>
																<td class="rootTrxDay date"></td>	<%-- 원거래일자가 없으면 거래일자 --%>
																<td class="trackId"></td>		 <%-- 주문번호 - 없으면 VAN 거래번호 입력 --%>
															<tr>
														</tbody>
													</table>
												</div>
											</div>
										</div>
										<!-- 내용 폼 종료 -->
									</div>
									<!-- END PAGE CONTENT INNER -->
								</div>
							</div>
						</div>
					<!-- BEGIN CONTAINER -->
				</div>
			<c:import url="/include/footer.jsp" />
			<c:import url="/include/javascript.jsp" />
			<script src="/assets/global/plugins/bootstrap-fileinput/bootstrap-fileinput.js" type="text/javascript"></script>
			<script src="/assets/global/plugins/xlsx/xlsx.full.min.js"></script>
			<script src="/assets/global/scripts/pg-excel-trx.js?v=14" type="text/javascript"></script>
			<script type="text/javascript">
				$('select.van').on('change', function() {
					var van = $(this).val();
					$.get( "/common/mchtList/byvan/"+van, function( data ) {
							var datas =  JSON.parse(data);
							if(datas.length < 1){
								alert('소속 가맹점이 없습니다.');
								}
							$('#mchtId').empty();
							$('#tmnId').empty();
							$('#mchtId').append('<option value=""></option>');
							for(i in datas){
								$('#mchtId').append('<option value="'+datas[i].mchtId+'">'+datas[i].name+" ["+datas[i].mchtId+']</option>');
							}
							$('#mchtId').selectpicker("refresh");
					});
				});

				$(document).on('change','#mchtId',function(e) {
					var mchtId = $( "#mchtId option:selected" ).val();
					if(mchtId !=''){
						$.get( "/common/tmnList/"+mchtId, function( data ) {
							var datas =  JSON.parse(data);
							if(datas.length < 1){
								alert('소속 터미널이 없습니다.');
								}
							$('#tmnId').empty();
							for(i in datas){
								if(datas[i].van == $('select.van').val()) {
									$('#tmnId').append('<option value="'+datas[i].tmnId+'">'+datas[i].tmnId+" ["+datas[i].van+']'+datas[i].description+'</option>');	
								}
							}
							$('#tmnId').selectpicker("refresh");
						});	
					}
				});
			</script>
			<!-- 모달 생성을 위한 베이스 -->
			<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
		</body>

		</html>