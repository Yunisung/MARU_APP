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
                					<li><span>사용자 정보</span></li>
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
						<!-- 전자계약서 설정 -->										
						<form class="form-horizontal form" role="form" id="writeFrm" name="writeFrm">
							<input type="hidden" id="id" name="id" value="${DATAMAP.id}">
							<input type="hidden" id="name" name="name" value="${DATAMAP.name}">
							<input type="hidden" id="grade" name="grade" value="${DATAMAP.grade}">
							<input type="hidden" id="parentId" name="parentId" value="${DATAMAP.parentId}">
							<input type="hidden" id="form_id" name="form_id" value="">
							<input type="hidden" id="doc_name" name="doc_name" value="">
						</form>
						<!-- 전자계약서 설정 -->
						<div class="page-content-inner">
							<div class="portlet light">
								<div class="portlet-body light">
									<div class="tabbable-line">
										<ul class="nav nav-tabs">
											<li class="active"><a href="#tab_basic" data-toggle="tab" aria-expanded="false"> 기본 정보 </a></li>
											<li class=""><a href="#tab_access" data-toggle="tab" aria-expanded="true"> 접속 정보 </a></li>
											<li class=""><a href="#tab_eform" data-toggle="tab" aria-expanded="false"> 전자계약 관리 </a></li>
										</ul>
										
										<div class="tab-content">
											<!-- 기본 정보 탭 시작 -->
											<div class="tab-pane active" id="tab_basic">
												<!-- BEGIN FORM-->
												<form class="form-horizontal" role="form">
													<div class="form-body">
														<!-- <h3 class="form-section">사용자 기본 정보</h3> -->
														<div class="row">
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">아이디:</label>
																	<div class="col-md-9">
																		<p class="form-control-static">${DATAMAP.id}</p>
																	</div>
																</div>
															</div>
															<!--/span-->
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">전화번호:</label>
																	<div class="col-md-9">
																		<p class="form-control-static">${DATAMAP.phone}</p>
																	</div>
																</div>
															</div>
															<!--/span-->
														</div>
														<!--/row-->
														<div class="row">
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">이름:</label>
																	<div class="col-md-9">
																		<p class="form-control-static">${DATAMAP.name}</p>
																	</div>
																</div>
															</div>
															<!--/span-->
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">상태:</label>
																	<div class="col-md-9">
																		<p class="form-control-static">${DATAMAP.status}</p>
																	</div>
																</div>
															</div>
															<!--/span-->
														</div>
														<!--/row-->
														<div class="row">
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">소속:</label>
																	<div class="col-md-9">
																		<p class="form-control-static">${DATAMAP.grade}</p>
																	</div>
																</div>
															</div>
															<!--/span-->
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">권한:</label>
																	<div class="col-md-9">
																		<p class="form-control-static">${DATAMAP.role}</p>
																	</div>
																</div>
															</div>
															<!--/span-->
														</div>
														<!--/row-->
														<div class="row">
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">기타거래:</label>
																	<div class="col-md-9">
																		<p class="form-control-static">
																			<c:if test="${DATAMAP.showOthTrns eq 'Y'}">
																				사용
																			</c:if>
																			<c:if test="${DATAMAP.showOthTrns ne 'Y'}">
																				미사용
																			</c:if>
																		</p>
																	</div>
																</div>
															</div>
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">전자계약:</label>
																	<div class="col-md-9">
																		<p class="form-control-static">
																			<c:choose>
																				<c:when test="${DATAMAP.eformStatus eq '사용'}">사용</c:when>
																				<c:when test="${DATAMAP.eformStatus eq '중지'}">중지</c:when>
																				<c:when test="${DATAMAP.eformStatus eq '예비'}">예비</c:when>
																			</c:choose>
																		</p>
																	</div>
																</div>
															</div>
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">대출정산:</label>
																	<div class="col-md-9">
																		<p class="form-control-static">
																			<c:if test="${DATAMAP.loanSettleStatus eq 'Y'}">
																				사용
																			</c:if>
																			<c:if test="${DATAMAP.loanSettleStatus ne 'Y'}">
																				미사용
																			</c:if>
																		</p>
																	</div>
																</div>
														</div>
														<!--/row-->
														<div class="row">
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">등록일시:</label>
																	<div class="col-md-9">
																		<p class="form-control-static">${DATAMAP.regDate}</p>
																	</div>
																</div>
															</div>
															<!--/span-->
															<c:if test="${CP_SESSION.role != '일반' }">
															<div class="col-md-6">
																<div class="form-group pg-view-group">
																	<label class="control-label col-md-3">비밀번호 재설정</label>
																	<div class="col-md-9">
																		<button type="button" class="btn btn-sm green " onclick="javascript:resetPassword('user','${DATAMAP.id}');">
																			<i class="fa fa-refresh"></i> 임시 비밀번호 발급
																		</button>
																	</div>
																</div>
															</div>
															</c:if>
															<!--/span-->
														</div>
													</div>
												</div>
												</form>
												<c:if test="${CP_SESSION.role != '일반' }">
												<div class="form-actions">
													<div class="row">
														<div class="col-md-12">
															<button type="button" class="btn btn-sm green pull-right" onclick="location.href='/member/user/modify/${DATAMAP.id}';">
																<i class="fa fa-pencil"></i> Edit
															</button>
														</div>
														<div class="col-md-6"></div>
													</div>
												</div>
												</c:if>
											</div>
											<!-- END FORM-->
											<!-- 기본 정보 탭 종료 -->
											<!-- 접속 정보 탭 시작 -->
											<div class="tab-pane" id="tab_access">
												<div class="portlet light portlet-form">
													<div class="portlet-title">
														<div class="caption font-red-sunglo">
															<i class="icon-share font-red-sunglo"></i>
															<span class="caption-subject bold uppercase"> Access Info </span>
														</div>
														<div class="actions">
															<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" data-original-title="" title=""> </a>
														</div>
													</div>
													<div class="portlet-body form light">
														<div class="table-scrollable">
															<!-- 리스트 본문 시작 -->
															<table class="pg-table table table-striped table-hover flip-content" id="sortTable">
																<!-- table-bordered -->
																<thead>
																	<tr>
																		<th>No</th>
																		<th data-sort="string">아이디</th>
																		<th data-sort="string">접속 아이피</th>
																		<th data-sort="string">User Agent</th>
																		<th data-sort="string">접속일시</th>
																	</tr>
																</thead>
																<tbody id="list">
																	<c:forEach var="entry" items="${DATAACCESSMAP}" varStatus="status">
																		<tr>
																			<td>${status.count+1}</td>
																			<td>${entry.id}</td>
																			<td>${entry.ipAddr}</td>
																			<td>${entry.userAgent}</td>
																			<td>${entry.regDate}</td>
																		</tr>
																	</c:forEach>
																</tbody>
															</table>
														</div>
													</div>
												</div>
											</div>
											<!-- 접속 정보 탭 종료 -->
									<!-- 전자계약 관리 탭 -->
									<div class="tab-pane" id="tab_eform">
										<!-- BEGIN FORM-->
										<form class="form-horizontal form" role="form">
											<div class="form-body row">
												<!--/span-->
												<div class="col-md-6">
													<div class="form-group pg-view-group">
														<label class="control-label col-md-3">전자계약서 발송 권한</label>
														<div class="col-md-9">
															<select name="eformStatus" id='eformStatus' class="selectpicker col-sm-6">
																<option value="예비" <c:if test="${DATAMAP.eformStatus == '예비'}">selected</c:if>>예비</option>
																<option value="사용" <c:if test="${DATAMAP.eformStatus == '사용'}">selected</c:if>>사용</option>
																<option value="중지" <c:if test="${DATAMAP.eformStatus == '중지'}">selected</c:if>>중지</option>
															</select>
															<button type="button" class="btn btn-sm green" onclick="formAuth();">
																<i class="fa fa-pencil"></i> 권한 수정
															</button>
														</div>
													</div>
												</div>
										</div>	
										<c:if test="${DATAMAP.eformStatus == '사용'}">
										<div class="row">
											<div class="col-md-6">
												<div class="form-group pg-view-group">
													<label class="control-label col-md-3">계약서 추가</label>
													<div class="col-md-9">
														<select class="selectpicker col-sm-6" id='selectForm' onchange="listChange(this)">
															<option selected>-- 계약서 선택 --</option>
														</select>
														<button type="button" class="btn btn-sm green" onclick="formSave();">
															<i class="fa fa-pencil"></i> 추가
														</button>
													</div>
												</div>
											</div>
											<!--/span-->
											<div class="col-md-6">
												<div class="form-group pg-view-group">
													<label class="control-label col-md-3">저장된 계약서</label>
													<div class="col-md-9">
														<select class="selectpicker col-sm-6 " id='selectList' onchange="listChange(this)">
															<option selected>-- 계약서 목록 --</option>
															<c:forEach var="entryMap" items="${DATAMAPFORM}">
																	<option value="${entryMap['idx']}">${entryMap['doc_name']}</option>
																</c:forEach>
														</select>
														<button type="button" class="btn btn-sm green" onclick="formDelete();">
															<i class="fa fa-pencil"></i> 삭제
														</button>
													</div>
												</div>
											</div>
										</div>
									</c:if>
								</form>
							<!-- END FORM-->
							</div>
						<!-- 전자계약 관리 탭 -->
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
		$('#nav-member').addClass('active');
		
		//전자계약서 목록 설정---------------------
		var resJson = '${resJson}';
		var token = '${token}';
		
		var resParse = JSON.parse(resJson);
		var title = resParse.result;
		
		for (var i=0; i<title.length; i++){
			var form = document.getElementById("selectForm");
				form.innerHTML += "<option value='" + title[i].id + "' data-val='" + title[i].title + "'>" + title[i].title + "</option>";
		}
		
		var formId = "";
		var formName = "";
		
		function listChange(obj){
			formId = $("#selectForm option:selected").val();
			formName = $("#selectForm option:selected").text();

			$("#form_id").val(formId);
			$("#doc_name").val(formName);
		}
		//전자계약서 목록 설정---------------------
		
		//권한 설정 : 사용/중지---------------------
		function formAuth(){
			var eformStatus = $("#eformStatus option:selected").val();
			var id = '${DATAMAP.id}';
			var grade = '${DATAMAP.grade}';
			var parentId = '${DATAMAP.parentId}';
			$.ajax({
				type:"POST",
				url:"/eform/auth/authModify/" + id,
				dataType:'text',
				async: false,
				data: ({
					eformStatus : eformStatus,
					grade : grade,
					parentId : parentId
				}),
				success:function(data){
					if(data == 'false'){
						alert("상위 대행사 '중지' 상태입니다. (상위 대행사 '사용' 우선 변경해주세요.)");
						$('#eformStatus').val('default').selectpicker("refresh");
					}else{
						alert("권한 수정되었습니다.");
						location.reload();
					}
				},
				error:function(data){
					alert("권한 설정 실패. 관리자 문의.");
				}
			});
		}
		//권한 설정 : 사용/중지---------------------

		//계약서 저장---------------------
		function formSave(){
			$.ajax({
				type:"POST",
				url:"/eform/auth/add",
				dataType:'text',
				data: $("#writeFrm").serializeObject(),
				success:function(data){
					alert("계약서가 등록되었습니다.");
					location.reload();
				},
				error:function(){
					alert("계약서 등록 실패. 관리자 문의.");
				}
			});
		}
		//계약서 저장---------------------
		
		//계약서 삭제---------------------
		function formDelete(){
			var idx = $("#selectList option:selected").val();
			$.ajax({
				type:"POST",
				url:"/eform/auth/delete/" + idx,
				dataType:'text',
				data: ({
					idx : idx
				}),
				success:function(data){
					alert("계약서가 삭제되었습니다.");
					location.reload();
				},
				error:function(){
					alert("계약서 삭제 실패. 관리자 문의.");
				}
			});
		}
		//계약서 삭제---------------------
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>