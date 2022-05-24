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
									<li><span>가맹점 페이지</span></li>
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
						<!-- BEGIN PAGE CONTENT - KWON - INNER -->
								<div class="page-content-inner">
									<div class="portlet light">
										<div class="portlet-body light">
											<div class="tabbable-line">
												<ul class="nav nav-tabs">
													<li class="tab_basic"><a href="#tab_basic" data-toggle="tab" aria-expanded="false"> 기본 </a></li>
													<li class="tab_additional"><a href="#tab_additional" data-toggle="tab" aria-expanded="false"> 관리자 </a></li>
													<c:if test="${CP_SESSION.grade ne '터미널' && fn:length(DATAMNGMAP) > 0 }">
														<li class="tab_mng"><a href="#tab_mng" data-toggle="tab" aria-expanded="true"> 지불 및 정산 </a></li>
														<c:if test="${DATAMNGMAP.interType eq '사용' }">
															<li class="tab_interest"><a href="#tab_interest" data-toggle="tab" aria-expanded="true"> 상점 부담 무이자 </a></li>
														</c:if>
														<c:if test="${CP_SESSION.grade eq '본사'}">
															<li class="tab_diff"><a href="#tab_diff" data-toggle="tab" aria-expanded="false"> 영중소가맹점 </a></li>
														</c:if>
													</c:if>
													<c:if test="${fn:length(DATATAXMAP) > 0 }"> 
														<li class="tab_tmn"><a href="#tab_tmn" data-toggle="tab" aria-expanded="false"> 터미널 </a></li>
													</c:if>
													<li class="tab_tax"><a href="#tab_tax" data-toggle="tab" aria-expanded="false"> Tax </a></li>
													<c:if test="${CP_SESSION.grade eq '본사' || CP_SESSION.grade eq '가맹점'}">
													<li class="tab_ddct"><a href="#tab_ddct" data-toggle="tab" aria-expanded="false"> 차감정산 </a></li>
														<c:if test="${DATASVCMAP.settle eq '충전정산'}">
															<li class="tab_chargeMng"><a href="#tab_chargeMng" data-toggle="tab" aria-expanded="false"> 충전정산 </a></li>
														</c:if>
													</c:if>
													<c:if test="${CP_SESSION.grade eq '본사'}">
														<li class="tab_svc"><a href="#tab_svc" data-toggle="tab" aria-expanded="false"> 서비스 </a></li>
														
														<li class="tab_phone"><a href="#tab_phone" data-toggle="tab" aria-expanded="false"> 휴대폰결제 </a></li>
														
														<c:if test="${DATASVCMAP.virAccount eq '사용'}">
															<li class="tab_virAccount"><a href="#tab_virAccount" data-toggle="tab" aria-expanded="false"> 가상계좌 </a></li>
														</c:if>
														<c:if test="${DATASVCMAP.pisp eq '사용'}">
															<li class="tab_pisp"><a href="#tab_pisp" data-toggle="tab" aria-expanded="false"> 지급대행 </a></li>
														</c:if>
													</c:if>
													<c:if test="${CP_SESSION.grade ne '터미널' }">
														<li class="tab_trx"><a href="#tab_trx" data-toggle="tab" aria-expanded="false"> 최근 매입 </a></li>
														<li class="tab_tot"><a href="#tab_tot" data-toggle="tab" aria-expanded="false"> 일별 매출</a></li>
													</c:if>
													<li class="tab_doc"><a href="#tab_doc" data-toggle="tab" aria-expanded="false"> 서류 관리</a></li>
													<li class="tab_map"><a href="#tab_map" data-toggle="tab" aria-expanded="false"> 지도 보기 </a></li>
													<c:if test="${CP_SESSION.grade =='본사'}">
													<li class="tab_his"><a href="#tab_his" data-toggle="tab" aria-expanded="false"> 이력 정보 </a></li>
													</c:if>
												</ul>
												<div class="tab-content">
													<!-- 기본 정보 탭 시작 -->
													<div class="tab-pane active" id="tab_basic">
														<!-- BEGIN FORM-->
														<form class="form-horizontal form" role="form">
															<div class="form-body row">
																<!--/span-->
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">아이디</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${DATAMAP.mchtId}</p>
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
																<%-- <c:if test="${CP_SESSION.grade eq '본사' }">
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">선정산 업체</label>
																			<div class="col-md-9">
																				<p class="form-control-static">
																					<c:if test="${not empty DATAMAP.loanId}">${DATAMAP.loanName}</c:if>
																				</p>
																			</div>
																		</div>
																	</div>
																</c:if> --%>
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
																		<label class="control-label col-md-3">가맹점명</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${DATAMAP.nick}</p>
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
																<div class="col-md-12">
																	<div class="form-group pg-view-group">
																	</div>
																</div>
																<c:if test="${CP_SESSION.grade eq '본사' || CP_SESSION.grade eq '대행사'}">
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">소속 대행사</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMAP.distName} </p>
																			</div>
																		</div>
																	</div>
																</c:if>
																<c:if test="${CP_SESSION.grade eq '본사' || CP_SESSION.grade eq '대행사' || CP_SESSION.grade eq '에이전시'}">
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">소속 에이전시</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMAP.agencyName} </p>
																			</div>
																		</div>
																	</div>
																</c:if>
																<c:if test="${CP_SESSION.grade eq '본사' || CP_SESSION.grade eq '대행사' || CP_SESSION.grade eq '에이전시' || CP_SESSION.grade eq '지사'}">
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">소속 지사</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${DATAMAP.salesName} </p>
																			</div>
																		</div>
																	</div>
																</c:if>
															</div>
															<div class="form-actions right def-action">
																<c:if test="${DATAMAP.status eq '사용' }">
																<c:if test="${CP_SESSION.grade eq '본사' || ((CP_SESSION.grade eq '대행사' || CP_SESSION.grade eq '에이전시' || CP_SESSION.grade eq '지사') && CP_SESSION.loanSettleStatus == 'Y')}">
																	<c:if test="${DATALOANMAP == null || DATALOANMAP.lastPayDay != ''}">
																		<button type="button" class="btn btn-sm green" onclick="location.href='/mcht/loanSettle/add/${DATAMAP.mchtId}';">
																			<i class="fa fa-pencil"></i> 대출 등록
																		</button>
																	</c:if>
																	<jsp:useBean id="now" class="java.util.Date" />
																	<fmt:formatDate value="${now}" pattern="yyyyMMdd" var="today" />
																	<c:if test="${DATALOANMAP != null && DATALOANMAP.loanDay > today }">
																		<button type="button" class="btn btn-sm green" onclick="location.href='/mcht/loanSettle/modify/${DATALOANMAP.loanId}';">
																			<i class="fa fa-pencil"></i> 대출 수정
																		</button>
																	</c:if>
																</c:if>
																</c:if>
																<c:if test="${CP_SESSION.grade eq '본사' && CP_SESSION.role != '일반'}">
																	<c:if test="${fn:length(DATAMNGMAP) < 1 }">
																		<button type="button" class="btn btn-sm red-haze" onclick="linkToMng('${DATAMAP.mchtId}', 'add');">
																			<i class="fa fa-pencil"></i> 지불 및 정산정보 등록
																		</button>
																	</c:if>
																</c:if>
																<c:if test="${(CP_SESSION.grade eq '본사' || CP_SESSION.grade eq '대행사' || CP_SESSION.grade eq '에이전시' || CP_SESSION.grade eq '지사') && CP_SESSION.role != '일반'}">
																	<button type="button" class="btn btn-sm green" onclick="location.href='/mcht/modify/${DATAMAP.mchtId}';">
																		<i class="fa fa-pencil"></i> 정보 수정
																	</button>
																</c:if>
															</div>
														</form>
														
														
														<!-- END FORM-->
													</div>
													<!-- 기본 정보 탭 종료 -->
													<!-- 추가 정보 탭 시작 -->
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
															<form class="form-horizontal" role="form">
																<div class="form-body">
																	<div class="form-group col-sm-12 form-subtitle">
																		<label><i class="fa fa-reorder"></i> 기본 정보</label>
																	</div>
																	<div class="row">
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
																				<label class="control-label col-md-3"></label>
																				<div class="col-md-9">
																					<p class="form-control-static"></p>
																				</div>
																			</div>
																		</div>
																		<!--/span-->
																		<c:if test="${CP_SESSION.grade == '본사'}">
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">터미널정산사용여부</label>
																				<div class="col-md-9">
																					<p class="form-control-static">${DATAMNGMAP.settleTmnStatus}</p>
																				</div>
																			</div>
																		</div>
																		</c:if>
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">영중소구분</label>
																				<div class="col-md-9">
																					<p class="form-control-static">${DATAMNGMAP.diffType}</p>
																				</div>
																			</div>
																		</div>
																		<c:if test="${CP_SESSION.grade eq '본사' || DATAMNGMAP.interType eq '사용' }">
																			<div class="col-md-6">
																				<div class="form-group pg-view-group">
																					<label class="control-label col-md-3">상점부담 무이자</label>
																					<div class="col-md-9">
																						<p class="form-control-static">${DATAMNGMAP.interType}</p>
																					</div>
																				</div>
																			</div>
																		</c:if>
																		<c:if test="${CP_SESSION.grade == '본사'}">
																			<div class="col-md-6">
																				<div class="form-group pg-view-group">
																					<label class="control-label col-md-3">대출정산 사용여부</label>
																					<div class="col-md-9">
																						<p class="form-control-static">${DATAMNGMAP.loanSettleStatus}</p>
																						<c:if test="${DATAMNGMAP.loanSettleStatus ne '미사용'}">(아이디 : ${DATALOANMAP.loanId})</c:if>
																					</div>
																				</div>
																			</div>
																		</c:if>
																	</div>
																	<!--/row-->
																	<div class="row">
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">
																					가맹점 <c:if test="${DATAMAP.distId eq '00'}">입금</c:if>정산유형
																				</label>
																				<div class="col-md-9">
																					<p class="form-control-static">${DATAMNGMAP.settleType}</p>
																				</div>
																			</div>
																		</div>
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">
																					<c:if test="${DATAMAP.distId eq '00'}">입금정산</c:if>
																					<c:if test="${DATAMAP.distId ne '00'}">가맹점</c:if>
																					 수수료</label>
																				<div class="col-md-9">
																					<c:if test="${CP_SESSION.grade == '본사'}">
																						<p class="form-control-static"><fmt:formatNumber value="${DATAMNGMAP.rate * 100}" pattern="0.000"/> %</p>
																						<a class="btn btn-sm" href="/member/rate/add/mcht/${DATAMAP.mchtId }">수수료 변경 예약</a>
																					</c:if>
																					<c:if test="${CP_SESSION.grade != '본사'}">
																						<p class="form-control-static"><fmt:formatNumber value="${DATAMNGMAP.rate * 100}" pattern="0.000"/> %</p>
																					</c:if>
																				</div>
																			</div>
																		</div>
																		<%-- 
																		<c:if test="${CP_SESSION.grade == '본사'}">
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">선정산 수수료</label>
																				<div class="col-md-9">
																					<p class="form-control-static loanRate"><fmt:formatNumber value="${DATAMNGMAP.loanRate * 100}" pattern="0.000"/> %</p>
																				</div>
																			</div>
																		</div>
																		</c:if>
																		--%>
																		<!--/span-->
																		<c:if test="${CP_SESSION.grade == '본사' && DATAMAP.distId ne '00' || CP_SESSION.grade == '대행사'}">
																			<div class="col-md-6">
																				<div class="form-group pg-view-group">
																					<label class="control-label col-md-3">대행사 정산유형</label>
																					<div class="col-md-9">
																						<p class="form-control-static distNum">${DATADISTMAP.settleName} (${DATADISTMAP.payStatus})</p>
																						<c:if test="${DATADISTMAP.settleType == 'M+10'}">
																							<span>매월 10일</span>
																						</c:if>
																						<c:if test="${DATADISTMAP.settleType == 'M+25'}">
																							<span>매월 25일</span>
																						</c:if>
																						<c:if test="${DATADISTMAP.settleType == 'M+15'}">
																							<span>매월 15일</span>
																						</c:if>
																						<c:if test="${DATADISTMAP.settleType == 'W+3'}">
																							<span>매주 수요일</span>
																						</c:if>
																					</div>
																				</div>
																			</div>
																			<div class="col-md-6">
																				<div class="form-group pg-view-group">
																					<label class="control-label col-md-3">대행사 수수료율</label>
																					<div class="col-md-9">
																						<p class="form-control-static distRate"><fmt:formatNumber value="${DATAMNGMAP.distRate * 100}" pattern="0.000"/> % (VAT별도)</p>
																					</div>
																				</div>
																			</div>
																		</c:if>
																		<!--/span-->
																		<c:if test="${CP_SESSION.grade == '본사' || CP_SESSION.grade == '대행사' || CP_SESSION.grade == '에이전시'}">
																			<div class="col-md-6">
																				<div class="form-group pg-view-group">
																					<label class="control-label col-md-3">에이전시 정산유형</label>
																					<div class="col-md-9">
																						<p class="form-control-static agencyNum">${DATAAGENCYMAP.settleName} (${DATAAGENCYMAP.payStatus})</p>
																						<c:if test="${DATAAGENCYMAP.settleType == 'M+10'}">
																							<span>매월 10일</span>
																						</c:if>
																						<c:if test="${DATAAGENCYMAP.settleType == 'M+25'}">
																							<span>매월 25일</span>
																						</c:if>
																						<c:if test="${DATAAGENCYMAP.settleType == 'M+15'}">
																							<span>매월 15일</span>
																						</c:if>
																						<c:if test="${DATAAGENCYMAP.settleType == 'W+3'}">
																							<span>매주 수요일</span>
																						</c:if>
																					</div>
																				</div>
																			</div>
																			<div class="col-md-6">
																				<div class="form-group pg-view-group">
																					<label class="control-label col-md-3">에이전시 수수료율</label>
																					<div class="col-md-9">
																						<p class="form-control-static agencyRate"><fmt:formatNumber value="${DATAMNGMAP.agencyRate * 100}" pattern="0.000"/> % (VAT별도)</p>
																					</div>
																				</div>
																			</div>
																		</c:if>
																		<!--/span-->
																		<c:if test="${CP_SESSION.grade ne '가맹점'}">
																			<div class="col-md-6">
																				<div class="form-group pg-view-group">
																					<label class="control-label col-md-3">지사 정산유형</label>
																					<div class="col-md-9">
																						<p class="form-control-static salesNum">${DATASALESMAP.settleName} (${DATASALESMAP.payStatus})</p>
																						<c:if test="${DATASALESMAP.settleType == 'M+10'}">
																							<span>매월 10일</span>
																						</c:if>
																						<c:if test="${DATASALESMAP.settleType == 'M+25'}">
																							<span>매월 25일</span>
																						</c:if>
																						<c:if test="${DATASALESMAP.settleType == 'M+15'}">
																							<span>매월 15일</span>
																						</c:if>
																						<c:if test="${DATASALESMAP.settleType == 'W+3'}">
																							<span>매주 수요일</span>
																						</c:if>
																					</div>
																				</div>
																			</div>
																			<div class="col-md-6">
																				<div class="form-group pg-view-group">
																					<label class="control-label col-md-3">지사 수수료율</label>
																					<div class="col-md-9">
																						<p class="form-control-static salesRate"><fmt:formatNumber value="${DATAMNGMAP.salesRate * 100}" pattern="0.000"/> % (VAT별도)</p>
																					</div>
																				</div>
																			</div>
																		</c:if>
																		<%--
																		<!--/span-->
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">이체 건당 수수료 <br>(즉시결제시만 사용)
																				</label>
																				<div class="col-md-9">
																					<p class="form-control-static digits">${DATAMNGMAP.wireFee}</p>
																				</div>
																			</div>
																		</div>
																		 --%>
																		<!--/span-->
																	</div>
																	<!--/row-->
																	<div class="row">
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">1회한도</label>
																				<div class="col-md-9">
																					<p class="form-control-static digits">${DATAMNGMAP.limitOnce}</p> 원
																				</div>
																			</div>
																		</div>
																		<!--/span-->
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">1일한도</label>
																				<div class="col-md-9">
																					<p class="form-control-static digits">${DATAMNGMAP.limitDay}</p> 원
																				</div>
																			</div>
																		</div>
																		<!--/span-->
																	</div>
																	<!--/row-->
																	<div class="row">
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">1개월한도</label>
																				<div class="col-md-9">
																					<p class="form-control-static digits">${DATAMNGMAP.limitMonth}</p> 원
																				</div>
																			</div>
																		</div>
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">연한도</label>
																				<div class="col-md-9">
																					<p class="form-control-static digits">${DATAMNGMAP.limitYear}</p> 원
																				</div>
																			</div>
																		</div>
																	</div>
																	<!--/row-->
																	<div class="row">
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
																	<c:if test="${CP_SESSION.grade == '본사'}">
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">고액거래 기준</label>
																				<div class="col-md-9">
																					<p class="form-control-static digits">${DATAMNGMAP.largeAmount}</p> 원
																				</div>
																			</div>
																		</div>
																		<%--
																		<!--/span-->
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">선정산 한도</label>
																				<div class="col-md-9">
																					<p class="form-control-static digits">${DATAMNGMAP.maxLoan}</p>
																				</div>
																			</div>
																		</div>
																		 --%>
																		<!--/span-->
																		</c:if>
																	</div>
																	<c:if test="${(CP_SESSION.grade == '본사' || CP_SESSION.grade == '대행사') && DATAMAP.distId ne '00' }">
																		<div class="row">
																			<div class="col-md-6">
																				<div class="form-group pg-view-group">
																					<label class="control-label col-md-3 font-blue">계산된 수수료율</label>
																					<div class="col-md-9">
																						<p class="form-control-static">
																							에이전시:
																							<span class="rate"><fmt:formatNumber value="${(DATAMNGMAP.rate - DATAMNGMAP.agencyRate) * 100}" pattern="0.000"/> %</span>
																							, 대행사:
																							<span class="rate"><fmt:formatNumber value="${(DATAMNGMAP.agencyRate - DATAMNGMAP.distRate) * 100}" pattern="0.000"/> %</span>
																						</p>
																					</div>
																				</div>
																			</div>
																			<c:if test="${CP_SESSION.grade == '본사' && DATAMAP.distId ne '00'}">
																				<div class="col-md-6">
																					<div class="form-group pg-view-group">
																						<label class="control-label col-md-3 font-blue">VAT 포함 수수료율</label>
																						<div class="col-md-9">
																							<p class="form-control-static">
																								가맹점:
																								<span class="rate"><fmt:formatNumber value="${DATAMNGMAP.rate * 1.1 * 100}" pattern="0.000"/> %</span>
																								(<span class="rate"><fmt:formatNumber value="${(DATAMNGMAP.rate) * 1.1 * 100}" pattern="0.000"/> %</span>)
																								, 에이전시:
																								<span class="rate"><fmt:formatNumber value="${(DATAMNGMAP.rate - DATAMNGMAP.agencyRate) * 1.1 * 100}" pattern="0.000"/> %</span>
																								, 대행사:
																								<span class="rate"><fmt:formatNumber value="${(DATAMNGMAP.agencyRate - DATAMNGMAP.distRate) * 1.1 * 100}" pattern="0.000"/> %</span>
																							</p>
																						</div>
																					</div>
																				</div>
																			</c:if>
																		</div>
																	</c:if>
																	<c:if test="${CP_SESSION.grade eq '본사'}">
																		<c:if test="${DATASVCMAP.settle == '실시간정산' || DATASVCMAP.settle == '자동정산'}">
																			<div class="form-group col-sm-12 form-subtitle">
																				<label><i class="fa fa-reorder"></i> 실시간 정산 정보</label>
																			</div>
																			<div class="row">
																				<div class="col-md-6">
																					<div class="form-group pg-view-group">
																						<label class="control-label col-md-3">실시간 정산<br>출금 수수료 납부자</label>
																						<div class="col-md-9">
																							<p class="form-control-static">${VACT_MAP.payOutType}</p>
																						</div>
																					</div>
																				</div>
																				<div class="col-md-6">
																					<div class="form-group pg-view-group">
																						<label class="control-label col-md-3">실시간 정산<br>출금 수수료</label>
																						<div class="col-md-9">
																							<p class="form-control-static digits">${VACT_MAP.payOutFee}</p> 원
																						</div>
																					</div>
																				</div>
																				<div class="col-md-6">
																					<div class="form-group pg-view-group">
																						<label class="control-label col-md-3">실시간 정산<br>전산 전송 간격</label>
																						<div class="col-md-9">
																							<p class="form-control-static digits">${VACT_MAP.transferInterval}</p>
																						</div>
																					</div>
																				</div>
																			</div>
																			<div class="row">
																				<div class="col-md-6">
																					<div class="form-group pg-view-group">
																						<label class="control-label col-md-3">실시간 정산 출금<br>수수료 분배</label>
																						<div class="col-md-9">
																							<p class="form-control-static">${VACT_MAP.payInStatus}</p>
																						</div>
																					</div>
																				</div>
																			</div>
																			<div class="row">
																				<div class="col-md-6">
																					<div class="form-group pg-view-group">
																						<label class="control-label col-md-3">대행사 지급</label>
																						<div class="col-md-9">
																							<p class="form-control-static digits">${DATAMNGMAP.distPayInFee}</p> 원
																						</div>
																					</div>
																				</div>
																				<div class="col-md-6">
																					<div class="form-group pg-view-group">
																						<label class="control-label col-md-3">에이전시 지급</label>
																						<div class="col-md-9">
																							<p class="form-control-static digits">${DATAMNGMAP.agencyPayInFee}</p> 원
																						</div>
																					</div>
																				</div>
																				<div class="col-md-6">
																					<div class="form-group pg-view-group">
																						<label class="control-label col-md-3">지사 지급</label>
																						<div class="col-md-9">
																							<p class="form-control-static digits">${DATAMNGMAP.salesPayInFee}</p> 원
																						</div>
																					</div>
																				</div>
																			</div>
																		</c:if>
																	</c:if>
																</div>
															</form>
															<c:if test="${CP_SESSION.grade eq '본사' && CP_SESSION.role != '일반'}">
															<div class="form-actions">
																<div class="row">
																	<div class="col-md-12">
																		<button type="button" class="btn btn-sm green pull-right" onclick="linkToMng('${DATAMAP.mchtId}','modify');">
																			<i class="fa fa-pencil"></i> 지불 및 정산정보 수정
																		</button>
																	</div>
																	<div class="col-md-6"></div>
																</div>
															</div>
															</c:if>
														</div>
													</c:if>
													<!-- 지불 및 정산정보 탭 종료 -->
													
													<!-- 상점부담 무이자 탭 시작 -->
													<c:if test="${fn:length(DATAMNGMAP) > 0 }">
														<div class="tab-pane" id="tab_interest">
															<div class="portlet light portlet-form">
															<div class="portlet-title">
																
																<div class="actions">
																	<c:if test="${CP_SESSION.grade eq '본사' && CP_SESSION.role ne '일반' }">
																		<c:choose>
																			<c:when test="${fn:length(DATAINTERMAP) > 0 }">
																				<button type="button" class="btn btn-sm green" style="margin-right:10px;" onclick="location.href='/mcht/inter/modify/${DATAMAP.mchtId}';">
																					<i class="fa fa-pencil"></i> 상점 부담 무이자 수정
																				</button>
																			</c:when>
																			<c:otherwise>
																				<button type="button" class="btn btn-sm green" style="margin-right:10px;" onclick="location.href='/mcht/inter/add/${DATAMAP.mchtId}';">
																					<i class="fa fa-pencil"></i> 상점 부담 무이자 등록
																				</button>
																			</c:otherwise>
																		</c:choose>
																	</c:if>
																			<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" data-original-title="" title=""> </a>		
																	
																</div>
															</div>
															<div class="portlet-body form light">
																<div class="table-scrollable">
																	<!-- 리스트 본문 시작 -->
																	<table class="pg-table table table-striped table-hover flip-content">
																		<!-- table-bordered -->
																		<thead>
																			<tr>
																				<th>No</th>
																				<th data-sort="string">매입사</th>
																				<th data-sort="string">2개월</th>
																				<th data-sort="string">3개월</th>
																				<th data-sort="string">4개월</th>
																				<th data-sort="string">5개월</th>
																				<th data-sort="string">6개월</th>
																				<th data-sort="string">7개월</th>
																				<th data-sort="string">8개월</th>
																				<th data-sort="string">9개월</th>
																				<th data-sort="string">10개월</th>
																				<th data-sort="string">11개월</th>
																				<th data-sort="string">12개월</th>
																				<th data-sort="string">13개월</th>
																				<th data-sort="string">14개월</th>
																				<th data-sort="string">15개월</th>
																				<th data-sort="string">16개월</th>
																				<th data-sort="string">17개월</th>
																				<th data-sort="string">18개월</th>
																				<th data-sort="string">19개월</th>
																				<th data-sort="string">20개월</th>
																				<th data-sort="string">21개월</th>
																				<th data-sort="string">22개월</th>
																				<th data-sort="string">23개월</th>
																				<th data-sort="string">24개월</th>
																			</tr>
																		</thead>
																		<tbody id="list">
																			<c:forEach var="entry" items="${DATAINTERMAP}" varStatus="status">
																				<tr>
																					<td>${status.count}</td>
																					<td>${entry.acqName}</td>
																					<td><fmt:formatNumber value="${entry.m02 * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber value="${entry.m03 * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber value="${entry.m04 * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber value="${entry.m05 * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber value="${entry.m06 * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber value="${entry.m07 * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber value="${entry.m08 * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber value="${entry.m09 * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber value="${entry.m10 * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber value="${entry.m11 * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber value="${entry.m12 * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber value="${entry.m13 * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber value="${entry.m14 * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber value="${entry.m15 * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber value="${entry.m16 * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber value="${entry.m17 * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber value="${entry.m18 * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber value="${entry.m19 * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber value="${entry.m20 * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber value="${entry.m21 * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber value="${entry.m22 * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber value="${entry.m23 * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber value="${entry.m24 * 100}" pattern="0.000"/> %</td>
																				</tr>
																			</c:forEach>
																		</tbody>
																	</table>
																</div>
															</div>
															
														</div>
														</div>
													</c:if>
													<!-- 상점부담 무이자 탭 종료 -->

													<!-- 터미널 탭 시작 -->
													<div class="tab-pane" id="tab_tmn">
														<div class="portlet light portlet-form">
															<div class="portlet-title">
																<div class="caption font-red-sunglo">
																	<i class="icon-share font-red-sunglo"></i>
																	<span class="caption-subject bold uppercase"> Terminal Info </span>
																</div>
																<div class="actions">
																	<button type="button" class="btn btn-sm green" style="margin-right:10px;" onclick="location.href='/mcht/tmn/add/${DATAMAP.mchtId}';">
																			<i class="fa fa-pencil"></i> 터미널 등록
																		</button>
																	<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" data-original-title="" title=""> </a>
																</div>
															</div>
															<div class="portlet-body form light">
																<div class="table-scrollable">
																	<!-- 리스트 본문 시작 -->
																	<table class="pg-table table table-striped table-hover flip-content">
																		<!-- table-bordered -->
																		<thead>
																			<tr>
																				<th>No</th>
																				<th data-sort="string">아이디</th>
																				<th data-sort="string">Tax</th>
																				<th data-sort="string">상태</th>
																				<th data-sort="string">일련번호</th>
																				<th data-sort="string">온라인 결제 Key</th>
																				<th data-sort="string">웹결제창</th>
																				<th data-sort="string">할부가능기간</th>
																				<th data-sort="string">수기유형</th>
																				<th data-sort="string">정산후취소</th>
																				<th data-sort="string">시작일자</th>
																				<c:if test="${(CP_SESSION.grade == '본사' || CP_SESSION.grade == '대행사' || CP_SESSION.grade == '에이전시' || CP_SESSION.grade == '가맹점') && CP_SESSION.role != '일반'}">
																					<th data-sort="string">추가정보</th>
																				</c:if>
																			</tr>
																		</thead>
																		<tbody id="list">
																			<c:forEach var="entry" items="${DATATMNMAP}" varStatus="status">
																				<tr>
																					<td>${status.count}</td>
																					<td class="link" data-url="/mcht/tmn/modify/${entry.tmnId}">${entry.tmnId}</td>
																					<td>${entry.taxName}</td>
																					<td>${entry.status}</td>
																					<td>${entry.serial}</td>
																					<td>${entry.payKey}</td>
																					<td>${entry.webPay}</td>
																					<td>${entry.apiMaxInstall}</td>
																					<td>
																						<c:if test="${entry.semiAuth == 'Y'}">비생</c:if>
																						<c:if test="${entry.semiAuth == 'N'}">일반</c:if>
																					</td>
																					<td>${entry.refundType}</td>
																					<td class="date">${entry.activeDate}</td>
																					<c:if test="${(CP_SESSION.grade == '본사' || CP_SESSION.grade == '대행사' || CP_SESSION.grade == '에이전시' || CP_SESSION.grade == '가맹점') && CP_SESSION.role != '일반'}">
																						<c:if test="${not empty entry.dtlName }">
																							<td class="link" data-url="/mcht/tmnDtl/modify/${entry.tmnId}">추가정보</td>
																						</c:if>
																						<c:if test="${empty entry.dtlName }">
																							<td class="link" data-url="/mcht/tmnDtl/add/${entry.tmnId}">추가정보</td>
																						</c:if>
																					</c:if>
																				</tr>
																			</c:forEach>
																		</tbody>
																	</table>
																</div>
															</div>
															
														</div>
													</div>
													<!-- 터미널 정보 탭 종료 -->
													<!-- Tax 탭 시작 -->
													<div class="tab-pane" id="tab_tax">
														<div class="portlet light portlet-form">
															<div class="portlet-title">
																<div class="caption font-red-sunglo">
																	<i class="icon-share font-red-sunglo"></i>
																	<span class="caption-subject bold uppercase"> Tax Info </span>
																</div>
																<div class="actions">
																	<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" data-original-title="" title=""> </a>
																</div>
															</div>
															<div class="portlet-body form light">
																<div class="table-scrollable">
																	<!-- 리스트 본문 시작 -->
																	<table class="pg-table table table-striped table-hover flip-content">
																		<!-- table-bordered -->
																		<thead>
																			<tr>
																				<th>No</th>
																				<th data-sort="string">이름</th>
																				<th data-sort="string">사용 상태</th>
																				<th data-sort="string">사용금액</th>
																				<th data-sort="string">TAX 한도</th>
																				<th data-sort="string">등록일자</th>
																			</tr>
																		</thead>
																		<tbody id="list">
																			<c:forEach var="entry" items="${DATATAXMAP}" varStatus="status">
																				<tr>
																					<td>${status.count}</td>
																					<td class="link_modal" data-url="/mcht/tax/modal/${entry.taxId}">${entry.name}</td>
																					<td>${entry.taxStatus}</td>
																					<td class="digits">${entry.usedAmt}</td>
																					<c:if test="${entry.taxLimit ne 0}">
																						<td class="digits">${entry.taxLimit}</td>
																					</c:if>
																					<c:if test="${entry.taxLimit eq 0}">
																						<td>제한 없음</td>
																					</c:if>
																					<td class="date">${entry.regDay}</td>
																				</tr>
																			</c:forEach>
																		</tbody>
																	</table>
																</div>
															</div>
															<div class="form-actions">
																<div class="row">
																	<div class="col-md-12">
																		<button type="button" class="btn btn-sm green pull-right" onclick="location.href='/mcht/tax/add/${DATAMAP.mchtId}';">
																			<i class="fa fa-pencil"></i> Tax 등록
																		</button>
																	</div>
																	<div class="col-md-6"></div>
																</div>
															</div>
														</div>
													</div>
													<!-- Tax 정보 탭 종료 -->
													<!-- 차감정산 탭 시작 -->
													<div class="tab-pane" id="tab_ddct">
														<div class="portlet light portlet-form">
															<div class="portlet-title">
																<div class="caption font-red-sunglo">
																	<i class="icon-share font-red-sunglo"></i>
																	<span class="caption-subject bold uppercase"> 차감정산 정보 </span>
																</div>
																<div class="actions">
																	<c:if test="${CP_SESSION.grade eq '본사' && CP_SESSION.role ne '일반' }">
																	<button type="button" class="btn btn-sm green" style="margin-right:10px;" onclick="location.href='/mcht/ddct/add/${DATAMAP.mchtId}';">
																			<i class="fa fa-pencil"></i> 차감정산 생성
																		</button>
																	</c:if>
																	<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" data-original-title="" title=""> </a>
																</div>
															</div>
															<div class="portlet-body form light">
																<div class="table-scrollable">
																	<!-- 리스트 본문 시작 -->
																	<table class="pg-table table table-striped table-hover flip-content">
																		<!-- table-bordered -->
																		<thead>
																			<tr>
																				<th>No</th>
																				<th data-sort="string">차감정산아이디</th>
																				<th data-sort="string">시작월</th>
																				<th data-sort="string">종료월</th>
																				<th data-sort="string">상태</th>
																				<th data-sort="string">정기차감항목</th>
																				<th data-sort="string">등록자아이디</th>
																				<th data-sort="string">등록일</th>
																			</tr>
																		</thead>
																		<tbody id="list">
																			<c:forEach var="entry" items="${DATADDCTMAP}" varStatus="status">
																				<tr>
																					<td>${status.count}</td>
																					<td class="link" data-url="/mcht/ddct/modify/${entry.ddctId}">${entry.ddctId}</td>
																					<td class="date">${entry.startMonth}</td>
																					<td class="date">${entry.endMonth}</td>
																					<td>${entry.status}</td>
																					<td>${entry.ddctName}</td>
																					<td>${entry.regId}</td>
																					<td class="date">${entry.regDay}</td>
																				</tr>
																			</c:forEach>
																		</tbody>
																	</table>
																</div>
															</div>
															
														</div>
													</div>
													<!-- 차감정산 탭 종료 -->
													<!-- 지도 탭 시작 -->
													<div class="tab-pane" id="tab_map">
														<div class="portlet light portlet-form">
															<div class="portlet-title">
																<div class="caption font-red-sunglo">
																	<i class="icon-pointer font-red-sunglo"></i>
																	<span class="caption-subject bold uppercase"> 지도 보기 </span>
																</div>
															</div>
															<c:if test="${not empty DATAMAP.lat}">
																<div class="portlet-body form light">
																	<div class="row">
																		<div class="col-md-12">
																			<div id="map_container" style="width: 100%; height: 500px;"></div>
																		</div>
																	</div>
																</div>
															</c:if>
															<c:if test="${empty DATAMAP.lat}">
																<div class="portlet-body form light">
																	<div class="row">
																		<div class="col-md-12">주소 정보가 정확하지 않아 지도 정보를 표시할 수 없습니다. 주소를 업데이트 해주세요.</div>
																	</div>
																</div>
															</c:if>
														</div>
													</div>
													<!-- 지도 정보 탭 종료 -->
													<!-- HISTORY 탭 시작 -->
													<div class="tab-pane" id="tab_his">
														<div class="portlet light portlet-form">
															<div class="portlet-title">
																<div class="caption font-red-sunglo">
																	<i class="icon-pointer font-red-sunglo"></i>
																	<span class="caption-subject bold uppercase">기본 정보 변경 이력  </span>
																</div>
															</div>
															<div class="portlet-body form light">
																<div class="table-scrollable">
																	<!-- 리스트 본문 시작 -->
																	<table class="pg-table table table-striped table-hover flip-content">
																		<!-- table-bordered -->
																		<thead>
																			<tr>
																				<th>No</th>
																				<th data-sort="string">이름</th>
																				<th data-sort="string">가맹점명</th>
																				<th data-sort="string">개시상태</th>
																				<th data-sort="string">업종</th>
																				<th data-sort="string">업태</th>
																				<th data-sort="string">대행사</th>
																				<th data-sort="string">에이전시</th>
																				<th data-sort="string">지사</th>
																				<th data-sort="string">식별정보</th>
																				<th data-sort="string">식별번호</th>
																				<th data-sort="string">전화번호</th>
																				<th data-sort="string">전화번호2</th>
																				<th data-sort="string">팩스</th>
																				<th data-sort="string">우편번호</th>
																				<th data-sort="string">사업장 주소</th>
																				<th data-sort="string">상세주소</th>
																				<th data-sort="string">대표자</th>
																				<th data-sort="string">대표 주민번호</th>
																				<th data-sort="string">대표 휴대폰</th>
																				<th data-sort="string">대표 전화번호</th>
																				<th data-sort="string">대표</th>
																				<th data-sort="string">대표 주소</th>
																				<th data-sort="string">대표 상세주소</th>
																				<th data-sort="string">담당자</th>
																				<th data-sort="string">담당 전화번호</th>
																				<th data-sort="string">summary</th>
																				<th data-sort="string">변경자</th>
																				<th data-sort="string">변경일시</th>
																			</tr>
																		</thead>
																		<tbody id="list">
																			<tr>
																				<td>${fn:length(HT_MAP) + 1}</td>
																				<td>${PG_MAP.name}</td>
																				<td>${PG_MAP.nick}</td>
																				<td>${PG_MAP.status}</td>
																				<td>${PG_MAP.bizType}</td>
																				<td>${PG_MAP.bizCategory}</td>
																				<td>${PG_MAP.distId}</td>
																				<td>${PG_MAP.agencyId}</td>
																				<td>${PG_MAP.salesId}</td>
																				<td>${PG_MAP.idType}</td>
																				<td>${PG_MAP.identity}</td>
																				<td>${PG_MAP.tel1}</td>
																				<td>${PG_MAP.tel2}</td>
																				<td>${PG_MAP.fax}</td>
																				<td>${PG_MAP.zip}</td>
																				<td>${PG_MAP.addr1}</td>
																				<td>${PG_MAP.addr2}</td>
																				<td>${PG_MAP.ceoName}</td>
																				<td>${PG_MAP.ceoIdentity}</td>
																				<td>${PG_MAP.ceoPhone}</td>
																				<td>${PG_MAP.ceoTel}</td>
																				<td>${PG_MAP.ceoZip}</td>
																				<td>${PG_MAP.ceoAddr1}</td>
																				<td>${PG_MAP.ceoAddr2}</td>
																				<td>${PG_MAP.managerName}</td>
																				<td>${PG_MAP.managerPhone}</td>
																				<td>${PG_MAP.summary}</td>
																				<td>${PG_MAP.regId}</td>
																				<td>${PG_MAP.regDate}</td>
																			</tr>
																			<c:forEach var="entry" items="${HT_MAP}" varStatus="status">
																				<tr>
																					<td>${fn:length(HT_MAP) - status.index}</td>
																					<td>${entry.name}</td>
																					<td>${entry.nick}</td>
																					<td>${entry.status}</td>
																					<td>${entry.bizType}</td>
																					<td>${entry.bizCategory}</td>
																					<td>${entry.distId}</td>
																					<td>${entry.agencyId}</td>
																					<td>${entry.salesId}</td>
																					<td>${entry.idType}</td>
																					<td>${entry.identity}</td>
																					<td>${entry.tel1}</td>
																					<td>${entry.tel2}</td>
																					<td>${entry.fax}</td>
																					<td>${entry.zip}</td>
																					<td>${entry.addr1}</td>
																					<td>${entry.addr2}</td>
																					<td>${entry.ceoName}</td>
																					<td>${entry.ceoIdentity}</td>
																					<td>${entry.ceoPhone}</td>
																					<td>${entry.ceoTel}</td>
																					<td>${entry.ceoZip}</td>
																					<td>${entry.ceoAddr1}</td>
																					<td>${entry.ceoAddr2}</td>
																					<td>${entry.managerName}</td>
																					<td>${entry.managerPhone}</td>
																					<td>${entry.summary}</td>
																					<td>${entry.regId}</td>
																					<td>${entry.regDate}</td>
																				</tr>
																			</c:forEach>
																		</tbody>
																	</table>
																</div>
															</div>
														</div>
														<div class="portlet light portlet-form">
															<div class="portlet-title">
																<div class="caption font-red-sunglo">
																	<i class="icon-pointer font-red-sunglo"></i>
																	<span class="caption-subject bold uppercase"> 지불 및 정산 정보 변경 이력</span>
																</div>
															</div>
															<div class="portlet-body form light">
																<div class="table-scrollable">
																	<!-- 리스트 본문 시작 -->
																	<table class="pg-table table table-striped table-hover flip-content">
																		<!-- table-bordered -->
																		<thead>
																			<tr>
																				<th>No</th>
																				<th data-sort="string">지불사용여부</th>
																				<th data-sort="string">정산유형</th>
																				<th data-sort="string">가맹점 수수료</th>
																				<%-- <th data-sort="string">선정산 수수료</th>--%>
																				<th data-sort="string">대행사 수수료</th>
																				<th data-sort="string">에이전시 수수료</th>
																				<th data-sort="string">이체 건당 수수료</th>
																				<th data-sort="string">관리 수수료</th>
																				<th data-sort="string">1회한도</th>
																				<th data-sort="string">1일한도</th>
																				<th data-sort="string">1개월한도</th>
																				<th data-sort="string">고액거래 기준</th>
																				<%--<th data-sort="string">선정산 한도</th> --%>
																				<th data-sort="string">summary</th>
																				<th data-sort="string">변경자</th>
																				<th data-sort="string">변경일시</th>
																			</tr>
																		</thead>
																		<tbody id="list">
																			<tr>
																				<td>${fn:length(HT_MNG_MAP) + 1}</td>
																				<td>${PG_MNG_MAP.payStatus}</td>
																				<td>${PG_MNG_MAP.settleType}</td>
																				<td><fmt:formatNumber value="${PG_MNG_MAP.rate * 100}" pattern="0.000"/> %</td>
																				<td><fmt:formatNumber value="${PG_MNG_MAP.distRate * 100}" pattern="0.000"/> %</td>
																				<td><fmt:formatNumber value="${PG_MNG_MAP.agencyRate * 100}" pattern="0.000"/> %</td>
																				<td><fmt:formatNumber type="number" value="${PG_MNG_MAP.wireFee}" pattern="#,##0" /></td>
																				<td><fmt:formatNumber type="number" value="${PG_MNG_MAP.manageFee}" pattern="#,##0" /></td>
																				<td><fmt:formatNumber type="number" value="${PG_MNG_MAP.limitOnce}" pattern="#,##0" /></td>
																				<td><fmt:formatNumber type="number" value="${PG_MNG_MAP.limitDay}" pattern="#,##0" /></td>
																				<td><fmt:formatNumber type="number" value="${PG_MNG_MAP.limitMonth}" pattern="#,##0" /></td>
																				<td><fmt:formatNumber type="number" value="${PG_MNG_MAP.largeAmount}" pattern="#,##0" /></td>
																				<td>${PG_MNG_MAP.summary}</td>
																				<td>${PG_MNG_MAP.regId}</td>
																				<td>${PG_MNG_MAP.regDate}</td>
																			</tr>
																			<c:forEach var="entry" items="${HT_MNG_MAP}" varStatus="status">
																				<tr>
																					<td>${fn:length(HT_MNG_MAP) - status.index}</td>
																					<td>${entry.payStatus}</td>
																					<td>${entry.settleType}</td>
																					<td><fmt:formatNumber value="${entry.rate * 100}" pattern="0.000"/> %</td>
																					<%--<td><fmt:formatNumber value="${entry.loanRate * 100}" pattern="0.000"/> %</td>--%>
																					<td><fmt:formatNumber value="${entry.distRate * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber value="${entry.agencyRate * 100}" pattern="0.000"/> %</td>
																					<td><fmt:formatNumber type="number" value="${entry.wireFee}" pattern="#,##0" /></td>
																					<td><fmt:formatNumber type="number" value="${entry.manageFee}" pattern="#,##0" /></td>
																					<td><fmt:formatNumber type="number" value="${entry.limitOnce}" pattern="#,##0" /></td>
																					<td><fmt:formatNumber type="number" value="${entry.limitDay}" pattern="#,##0" /></td>
																					<td><fmt:formatNumber type="number" value="${entry.limitMonth}" pattern="#,##0" /></td>
																					<td><fmt:formatNumber type="number" value="${entry.largeAmount}" pattern="#,##0" /></td>
																					<%--<td><fmt:formatNumber type="number" value="${entry.maxLoan}" pattern="#,##0" /></td>--%>
																					<td>${entry.summary}</td>
																					<td>${entry.regId}</td>
																					<td>${entry.regDate}</td>
																				</tr>
																			</c:forEach>
																		</tbody>
																	</table>
																</div>
															</div>
														</div>
														<div class="portlet light portlet-form">
															<div class="portlet-title">
																<div class="caption font-red-sunglo">
																	<i class="icon-pointer font-red-sunglo"></i>
																	<span class="caption-subject bold uppercase"> TAX 정보 변경 이력</span>
																</div>
															</div>
															<div class="portlet-body form light">
																<div class="table-scrollable">
																	<!-- 리스트 본문 시작 -->
																	<table class="pg-table table table-striped table-hover flip-content">
																		<!-- table-bordered -->
																		<thead>
																			<tr>
																				<th>No</th>
																				<th data-sort="string">Tax 아이디</th>
																				<th data-sort="string">이름</th>
																				<th data-sort="string">회사명</th>
																				<th data-sort="string">대표자 성명</th>
																				<th data-sort="string">식별번호 구분</th>
																				<th data-sort="string">주민/사업자번호</th>
																				<th data-sort="string">사용 상태</th>
																				<th data-sort="string">TAX 결제 한도</th>
																				<th data-sort="string">은행코드</th>
																				<th data-sort="string">은행명</th>
																				<th data-sort="string">계좌번호</th>
																				<th data-sort="string">예금주</th>
																				<th data-sort="string">우편번호</th>
																				<th data-sort="string">주소</th>
																				<th data-sort="string">상세주소</th>
																				<th data-sort="string">이메일</th>
																				<th data-sort="string">변경자</th>
																				<th data-sort="string">변경일시</th>
																			</tr>
																		</thead>
																		<tbody id="list">
																			<tr>
																				<td>${fn:length(HT_TAX_MAP) + 1}</td>
																				<td>${PG_TAX_MAP.taxId}</td>
																				<td>${PG_TAX_MAP.name}</td>
																				<td>${PG_TAX_MAP.compName}</td>
																				<td>${PG_TAX_MAP.ceoName}</td>
																				<td>${PG_TAX_MAP.idType}</td>
																				<td>${PG_TAX_MAP.identity}</td>
																				<td>${PG_TAX_MAP.taxStatus}</td>
																				<td><fmt:formatNumber type="number" value="${PG_TAX_MAP.taxLimit}" pattern="#,##0" /></td>
																				<td>${PG_TAX_MAP.bankCd}</td>
																				<td>${PG_TAX_MAP.bankName}</td>
																				<td>${PG_TAX_MAP.account}</td>
																				<td>${PG_TAX_MAP.accntHolder}</td>
																				<td>${PG_TAX_MAP.zip}</td>
																				<td>${PG_TAX_MAP.addr1}</td>
																				<td>${PG_TAX_MAP.addr2}</td>
																				<td>${PG_TAX_MAP.email}</td>
																				<td>${PG_TAX_MAP.regId}</td>
																				<td>${PG_TAX_MAP.regDate}</td>
																			</tr>
																			<c:forEach var="entry" items="${HT_TAX_MAP}" varStatus="status">
																				<tr>
																					<td>${fn:length(HT_TAX_MAP) - status.index}</td>
																					<td>${entry.taxId}</td>
																					<td>${entry.name}</td>
																					<td>${entry.compName}</td>
																					<td>${entry.ceoName}</td>
																					<td>${entry.idType}</td>
																					<td>${entry.identity}</td>
																					<td>${entry.taxStatus}</td>
																					<td><fmt:formatNumber type="number" value="${entry.taxLimit}" pattern="#,##0" /></td>
																					<td>${entry.bankCd}</td>
																					<td>${entry.bankName}</td>
																					<td>${entry.account}</td>
																					<td>${entry.accntHolder}</td>
																					<td>${entry.zip}</td>
																					<td>${entry.addr1}</td>
																					<td>${entry.addr2}</td>
																					<td>${entry.email}</td>
																					<td>${entry.regId}</td>
																					<td>${entry.regDate}</td>
																				</tr>
																			</c:forEach>
																		</tbody>
																	</table>
																</div>
															</div>
														</div>
														<div class="portlet light portlet-form">
															<div class="portlet-title">
																<div class="caption font-red-sunglo">
																	<i class="icon-pointer font-red-sunglo"></i>
																	<span class="caption-subject bold uppercase"> 터미널 정보 변경 이력</span>
																</div>
															</div>
															<div class="portlet-body form light">
																<div class="table-scrollable">
																	<!-- 리스트 본문 시작 -->
																	<table class="pg-table table table-striped table-hover flip-content">
																		<!-- table-bordered -->
																		<thead>
																			<tr>
																				<th>No</th>
																				<th>터미널ID</th>
																				<th>가맹점ID</th>
																				<th>tax ID</th>
																				<th>상태</th>
																				<th>Serial</th>
																				<th>payKey</th>
																				<th>활성일자</th>
																				<th>VAN</th>
																				<th>VAN Idx</th>
																				<th>취급품목</th>
																				<th>summary</th>
																				<th>변경자</th>
																				<th>변경일시</th>
																			</tr>
																		</thead>
																		<tbody>
																			<tr>
																				<td>${fn:length(HT_TMN_MAP) + 1}</td>
																				<td>${PG_TMN_MAP.tmnId}</td>
																				<td>${PG_TMN_MAP.mchtId}</td>
																				<td>${PG_TMN_MAP.taxId}</td>
																				<td>${PG_TMN_MAP.status}</td>
																				<td>${PG_TMN_MAP.serial}</td>
																				<td>${PG_TMN_MAP.payKey}</td>
																				<td>${PG_TMN_MAP.activeDate}</td>
																				<td>${PG_TMN_MAP.van}</td>
																				<td>${PG_TMN_MAP.vanIdx}</td>
																				<td>${PG_TMN_MAP.description}</td>
																				<td>${PG_TMN_MAP.summary}</td>
																				<td>${PG_TMN_MAP.regId}</td>
																				<td>${PG_TMN_MAP.regDate}</td>
																			</tr>
																			<c:forEach var="entry" items="${HT_TMN_MAP}" varStatus="status">
																				<tr>
																					<td>${fn:length(HT_TMN_MAP) - status.index}</td>
																					<td>${entry.tmnId}</td>
																					<td>${entry.mchtId}</td>
																					<td>${entry.taxId}</td>
																					<td>${entry.status}</td>
																					<td>${entry.serial}</td>
																					<td>${entry.payKey}</td>
																					<td>${entry.activeDate}</td>
																					<td>${entry.van}</td>
																					<td>${entry.vanIdx}</td>
																					<td>${entry.description}</td>
																					<td>${entry.summary}</td>
																					<td>${entry.regId}</td>
																					<td>${entry.regDate}</td>
																				</tr>
																			</c:forEach>
																		</tbody>
																	</table>
																</div>
															</div>
														</div>
													</div>
													<!-- HISTORY 탭 종료-->
													<!-- Trx 탭 시작 -->
													<div class="tab-pane" id="tab_trx">
														<div class="portlet light portlet-form">
															<div class="portlet-body form light">
																<div class="table-scrollable">
																	<!-- 리스트 본문 시작 -->
																	<table class="pg-table table table-striped table-hover flip-content">
																		<!-- table-bordered -->
																		<thead>
																			<tr>
																				<th>No</th>
																				<th data-sort="string">매입번호</th>
																				<th data-sort="string">터미널ID</th>
																				<th data-sort="string">매입 구분</th>
																				<th data-sort="string">금액</th>
																				<th data-sort="string">등록일자</th>
																			</tr>
																		</thead>
																		<tbody id="list">
																			<c:forEach var="entry" items="${DATATRXMAP}" varStatus="status">
																				<tr>
																					<td>${status.count}</td>
																					<td class="link_modal" data-url="/trx/cap/view/${entry.capId}">${entry.capId}</td>
																					<td>${entry.tmnId}</td>
																					<td>${entry.capType}</td>
																					<td class="digits">${entry.amount}</td>
																					<td>${entry.regDate}</td>
																				</tr>
																			</c:forEach>
																		</tbody>
																	</table>
																</div>
															</div>
														</div>
													</div>
													<!-- Trx 정보 탭 종료 -->
													<!-- 통계 탭 시작 -->
													<div class="tab-pane" id="tab_tot">
														<div class="portlet light portlet-form">
															<div class="portlet-body form light">
																<div class="table-scrollable">
																	<!-- 리스트 본문 시작 -->
																	<table class="pg-table table table-bordered table-hover flip-content">
																		<!-- table-bordered -->
																		<thead>
																			<tr>
																				<th rowspan="2">No</th>
																				<th rowspan="2">거래일시</th>
																				<th colspan="4">매입현황</th>
																				<th colspan="4">매입취소현황</th>
																				<th colspan="4">정산취소현황</th>
																				<!-- <th rowspan="2">수익</th> -->
																			</tr>
																			<tr>
																				<th>금액</th>
																				<th>건수</th>
																				<th>정산금액</th>
																				<th>수수료</th>
																				<!-- <th>수익</th> -->
																				<th>금액</th>
																				<th>건수</th>
																				<th>정산금액</th>
																				<th>수수료</th>
																				<!-- <th>수익</th> -->
																				<th>금액</th>
																				<th>건수</th>
																				<th>정산금액</th>
																				<th>수수료</th>
																				<!-- <th>수익</th> -->
																			</tr>
																		</thead>
																		<tbody id="list">
																			<c:forEach var="entry" items="${DATATOTMAP}" varStatus="status">
																				<tr>
																					
																					<td>${status.count}</td>
																					<td class="date">${entry.capDay}</td>
																					<td class="text-right">
																						<fmt:formatNumber type="number" value="${entry.saleAmount}" pattern="#,##0" />
																					</td>
																					<td class="text-right">
																						<fmt:formatNumber type="number" value="${entry.saleCount}" pattern="#,##0" />
																					</td>
																					<td>
																						<fmt:formatNumber type="number" value="${entry.saleStlAmount}" pattern="#,##0" />
																					</td>
																					<td>
																						<fmt:formatNumber type="number" value="${entry.saleStlFee}" pattern="#,##0" />
																					</td>
																					<%-- <td>
																						<fmt:formatNumber type="number" value="${entry.saleBenefit}" pattern="#,##0" />
																					</td> --%>
																					<td>
																						<fmt:formatNumber type="number" value="${entry.rfdAmount}" pattern="#,##0" />
																					</td>
																					<td>
																						<fmt:formatNumber type="number" value="${entry.rfdCount}" pattern="#,##0" />
																					</td>
																					<td>
																						<fmt:formatNumber type="number" value="${entry.rfdStlAmount}" pattern="#,##0" />
																					</td>
																					<td>
																						<fmt:formatNumber type="number" value="${entry.rfdStlFee}" pattern="#,##0" />
																					</td>
																					<%-- <td>
																						<fmt:formatNumber type="number" value="${entry.rfdBenefit}" pattern="#,##0" />
																					</td> --%>
																					<td>
																						<fmt:formatNumber type="number" value="${entry.rfdedAmount}" pattern="#,##0" />
																					</td>
																					<td>
																						<fmt:formatNumber type="number" value="${entry.rfdedCount}" pattern="#,##0" />
																					</td>
																					<td>
																						<fmt:formatNumber type="number" value="${entry.rfdedStlAmount}" pattern="#,##0" />
																					</td>
																					<td>
																						<fmt:formatNumber type="number" value="${entry.rfdedStlFee}" pattern="#,##0" />
																					</td>
																					<%-- <td>
																						<fmt:formatNumber type="number" value="${entry.rfdedBenefit}" pattern="#,##0" />
																					</td>
																					<td>
																						<fmt:formatNumber type="number" value="${entry.benefit}" pattern="#,##0" />
																					</td> --%>
																				</tr>
																			</c:forEach>
																		</tbody>
																	</table>
																</div>
															</div>
														</div>
													</div>
													<!-- 통계 정보 탭 종료 -->
													<!-- 서류 탭 시작 -->
													<div class="tab-pane" id="tab_doc">
														<div class="portlet light portlet-form">
															<div class="portlet-title">
																<div class="actions">
																	<a class="btn btn-sm green" href="/mcht/doc/upload/${DATAMAP.mchtId }">파일 등록</a>
																	<c:if test="${CP_SESSION.grade eq '본사'}">
																	<a class="btn btn-sm green" href="javascript:statusChangeAll('본사')">본사뷰 권한으로 설정</a>
																	</c:if>
																</div>
															</div>
															<div class="portlet-body form light">

																<div class="table-scrollable">
																	<!-- 리스트 본문 시작 -->
																	<table class="pg-table table table-striped table-hover flip-content">
																		<!-- table-bordered -->
																		<thead>
																			<tr>
																				<th>No</th>
																				<th>파일명</th>
																				<th>용량</th>
																				<th>등록자</th>
																				<th>등록일</th>
																				<th>다운로드</th>
																				<c:if test="${CP_SESSION.grade eq '본사'}">
																				<th>뷰 권한</th>
																				<th>Action</th>
																				</c:if>
																			</tr>
																		</thead>
																		<tbody id="list">
																			<c:if test="${fn:length(DATADOCMAP) < 1}">
																				<tr><td colspan="9">조회 가능한 서류목록이 없습니다.</td></tr>
																			</c:if>
																			<c:if test="${fn:length(DATADOCMAP) > 0}">
																			<c:forEach var="entry" items="${DATADOCMAP}" varStatus="status">
																				<tr>
																					<td>${status.count}</td>
																					<td>${entry.fileNm}</td>
																					<td>${entry.fileSize}</td>
																					<td>${entry.regId}</td>
																					<td class="date">${entry.regDay}</td>
																					<td class="link download-link" data-url="/uploadFile/get/${entry.idx}">DOWNLOAD</td>
																					<c:if test="${CP_SESSION.grade eq '본사'}">
																						<td>${entry.status}</td>
																						<td>
																							<c:if test="${entry.status eq '본사'}">
																								<a class="btn btn-sm btn-info" href="javascript:statusChange('${entry.idx}','사용')">전체 뷰</a>
																							</c:if>
																							<c:if test="${entry.status eq '사용'}">
																								<a class="btn btn-sm btn-success" href="javascript:statusChange('${entry.idx}','본사')">본사 뷰</a>
																							</c:if>
																							<a class="btn btn-sm btn-danger" href="javascript:statusChange('${entry.idx}','폐기')">폐기</a>
																						</td>
																					</c:if>
																				</tr>
																			</c:forEach>
																			</c:if>
																		</tbody>
																	</table>
																</div>
															</div>
														</div>
													</div>
													<!-- 서류 정보 탭 종료 -->
													<!-- 서비스 탭 시작 -->
													<div class="tab-pane" id="tab_svc">
														<!-- BEGIN FORM-->
														<form class="form-horizontal form" role="form">
															<div class="form-body row">
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">카드일반</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${DATASVCMAP.cardRegular}</p>
																		</div>
																	</div>
																</div>
																<!--/span-->
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">카드3D</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${DATASVCMAP.card3D}</p>
																		</div>
																	</div>
																</div>
																<!--/span-->
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">가상계좌</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${DATASVCMAP.virAccount}</p>
																		</div>
																	</div>
																</div>
																<!--/span-->
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">실시간계좌이체</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${DATASVCMAP.bankCollect}</p>
																		</div>
																	</div>
																</div>
																<!--/span-->
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">월렛서비스</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${DATASVCMAP.walletSvc}</p>
																		</div>
																	</div>
																</div>
																<!--/span-->
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">휴대폰소액결제</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${DATASVCMAP.phoneBill}</p>
																		</div>
																	</div>
																</div>
																<!--/span-->
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">KSNET 빠른현장결제</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${DATASVCMAP.recurring}</p>
																		</div>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">지급대행</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${DATASVCMAP.pisp}</p>
																		</div>
																	</div>
																</div>
																<!--/span-->
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">정산구분</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${DATASVCMAP.settle}</p>
																		</div>
																	</div>
																</div>
																<!--/span-->
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">현금영수증</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${DATASVCMAP.cashReceipt}</p>
																		</div>
																	</div>
																</div>
																<!--/span-->
															</div>
															<div class="form-actions">
																<div class="row">
																	<div class="col-md-12">
																		<button type="button" class="btn btn-sm green pull-right" onclick="location.href='/mcht/svc/modify/${DATAMAP.mchtId}';">
																			<i class="fa fa-pencil"></i> 서비스 수정
																		</button>
																	</div>
																	<div class="col-md-6"></div>
																</div>
															</div>
														</form>
														<!-- END FORM-->
													</div>
													<!-- 서비스 탭 종료 -->
													<!-- 휴대폰 결제 시작 -->
													<div class="tab-pane" id="tab_phone">
													<!-- BEGIN FORM-->
														<form class="form-horizontal form" role="form">
															<div class="form-body row">
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">지불사용여부</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${DATAPHONEMAP.payStatus}</p>
																		</div>
																	</div>
																</div>
																<!--/span-->
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">상품유형</label>
																		<div class="col-md-9">
																		<p class="form-control-static">
																			<c:if test="${DATAPHONEMAP.prodType == 1}">실물</c:if>
																			<c:if test="${DATAPHONEMAP.prodType == 2}">컨텐츠</c:if>
																		</p>
																		</div>
																	</div>
																</div>														
																<div class="col-md-6">
										                            <div class="form-group pg-view-group">
										                                <label class="control-label col-md-3">
										                                    <c:if test="${DATAMAP.distId ne '00'}">가맹점</c:if> 수수료</label>
										                                <div class="col-md-9">
										                                    <c:if test="${CP_SESSION.grade == '본사'}">
										                                    	<c:if test="${DATAPHONEMAP eq null }">
										                                    		<p class="form-control-static"></p>
										                                    	</c:if>
										                                    	<c:if test="${DATAPHONEMAP ne null }">
										                                        	<p class="form-control-static"><fmt:formatNumber value="${DATAPHONEMAP.rate * 100}" pattern="0.000"/> %</p>
										                                        </c:if>
										                                    </c:if>
										                                </div>
										                            </div>
										                        </div>
																<!--/span-->
																<c:if test="${CP_SESSION.grade == '본사' || CP_SESSION.grade == '대행사' || CP_SESSION.grade == '에이전시'}">
										                        <div class="col-md-6">
										                            <div class="form-group pg-view-group">
										                                <label class="control-label col-md-3">에이전시 수수료</label>
										                                <div class="col-md-9">
										                                	<c:if test="${DATAPHONEMAP eq null }">
										                                    		<p class="form-control-static"></p>
										                                    	</c:if>
									                                    	<c:if test="${DATAPHONEMAP ne null }">
										                                    	<p class="form-control-static agencyRate"><fmt:formatNumber value="${DATAPHONEMAP.agencyRate * 100}" pattern="0.000"/> %</p>
										                                    </c:if>
										                                </div>
										                            </div>
										                        </div>
										                        </c:if>
										                        <!--/span-->
										                        <c:if test="${CP_SESSION.grade == '본사' && DATAMAP.distId ne '00' || CP_SESSION.grade == '대행사'}">
										                            <div class="col-md-6">
										                                <div class="form-group pg-view-group">
										                                    <label class="control-label col-md-3">대행사 수수료</label>
										                                    <div class="col-md-9">
										                                    	<c:if test="${DATAPHONEMAP eq null }">
										                                    		<p class="form-control-static"></p>
										                                    	</c:if>
									                                    		<c:if test="${DATAPHONEMAP ne null }">
										                                        	<p class="form-control-static distRate"><fmt:formatNumber value="${DATAPHONEMAP.distRate * 100}" pattern="0.000"/> %</p>
										                                        </c:if>
										                                    </div>
										                                </div>
										                            </div>
										                        </c:if>
										                        <!--/span-->
										                        <c:if test="${CP_SESSION.grade ne '가맹점'}">
										                            <div class="col-md-6">
										                                <div class="form-group pg-view-group">
										                                    <label class="control-label col-md-3">지사 수수료</label>
										                                    <div class="col-md-9">
										                                    	<c:if test="${DATAPHONEMAP eq null }">
										                                    		<p class="form-control-static"></p>
										                                    	</c:if>
									                                    		<c:if test="${DATAPHONEMAP ne null }">
										                                        	<p class="form-control-static salesRate"><fmt:formatNumber value="${DATAPHONEMAP.salesRate * 100}" pattern="0.000"/> %</p>
										                                        </c:if>
										                                    </div>
										                                </div>
										                            </div>
										                        </c:if>
										                        <!--/span-->
										                        <div class="col-md-6">
										                            <div class="form-group pg-view-group">
										                                <label class="control-label col-md-3">1회한도</label>
										                                <div class="col-md-9">
										                                    <p class="form-control-static digits">${DATAPHONEMAP.limitOnce}</p>
										                                </div>
										                            </div>
										                        </div>
										                        <!--/span-->
										                        <div class="col-md-6">
										                            <div class="form-group pg-view-group">
										                                <label class="control-label col-md-3">1일한도</label>
										                                <div class="col-md-9">
										                                    <p class="form-control-static digits">${DATAPHONEMAP.limitDay}</p>
										                                </div>
										                            </div>
										                        </div>
										                        <!--/span-->
											                    <!--/row-->
										                        <div class="col-md-6">
										                            <div class="form-group pg-view-group">
										                                <label class="control-label col-md-3">1개월한도</label>
										                                <div class="col-md-9">
										                                    <p class="form-control-static digits">${DATAPHONEMAP.limitMonth}</p>
										                                </div>
										                            </div>
										                        </div>
										                        <div class="col-md-6">
										                            <div class="form-group pg-view-group">
										                                <label class="control-label col-md-3">연한도</label>
										                                <div class="col-md-9">
										                                    <p class="form-control-static digits">${DATAPHONEMAP.limitYear}</p>
										                                </div>
										                            </div>
										                        </div>
										                        <!--/span-->
										                    	<c:if test="${CP_SESSION.grade == '본사'}">
										                        <div class="col-md-6">
										                            <div class="form-group pg-view-group">
										                                <label class="control-label col-md-3">고액거래 기준</label>
										                                <div class="col-md-9">
										                                    <p class="form-control-static digits">${DATAPHONEMAP.largeAmount}</p>
										                                </div>
										                            </div>
										                        </div>
										                        <!--/span-->
										                    	</c:if>
										                    	<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">거래시작일</label>
																		<div class="col-md-9">
																		<p class="form-control-static">
																			<fmt:parseDate value="${DATAPHONEMAP.openDay}" var="openDay" pattern="yyyyMMdd"/>
																			<fmt:formatDate value="${openDay}" pattern="yyyy-MM-dd"/>
																		</p>
																		</div>
																	</div>
																</div>
															</div>
															<c:if test="${CP_SESSION.grade == '본사'}">
																<div class="form-actions">
																	<div class="row">
																		<div class="col-md-12">
																			<c:if test="${fn:length(DATAPHONEMAP) > 1 }">
																				<button type="button" class="btn btn-sm green pull-right" onclick="location.href='/phone/mng/modify/${DATAMAP.mchtId}';">
																					<i class="fa fa-pencil"></i> 휴대폰 결제 정보 수정
																				</button>
																			</c:if>
																			&nbsp;&nbsp;&nbsp;
																			<c:if test="${fn:length(DATAPHONEMAP) < 1 }">
																				<button type="button" class="btn btn-sm red pull-right" onclick="location.href='/phone/mng/add/${DATAMAP.mchtId}';">
																					<i class="fa fa-pencil"></i> 휴대폰 결제 정보 등록
																				</button>
																			</c:if>
																		</div>
																		<div class="col-md-6"></div>
																	</div>
																</div>
															</c:if>
														</form>
													</div>
													<!-- 휴대폰 결제 끝 -->
													<!-- 가상계좌 정보 탭 시작 -->
													<div class="tab-pane" id="tab_virAccount">
														<form class="form-horizontal form" role="form">
															<c:if test="${not empty VACT_MAP}">
															<div class="form-body">
																<div class="form-group col-sm-12 form-subtitle">
																	<label><i class="fa fa-reorder"></i> 기본 정보</label>
																</div>
																<div class="row">
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">기본 예금주명</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${VACT_MAP.holderName}</p>
																		</div>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">상태</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${VACT_MAP.status}</p>
																		</div>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">발행유형</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${VACT_MAP.issueType}</p>
																		</div>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">만료일 지정</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${VACT_MAP.expireSet}</p>
																		</div>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">거래시작일</label>
																		<div class="col-md-9">
																			<p class="form-control-static date">${VACT_MAP.startDay}</p>
																		</div>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">정산여부</label>
																		<div class="col-md-9">
																			<p class="form-control-static">
																				<c:if test="${VACT_MAP.settleTarget == 'Y'}">사용</c:if>
																				<c:if test="${VACT_MAP.settleTarget == 'N'}">중지</c:if>
																			</p>
																		</div>
																	</div>
																</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">가맹점 수수료유형</label>
																		<div class="col-md-9">
																			<p class="form-control-static">
																				<c:if test="${VACT_MAP.feeType == '0'}">정액</c:if>
																				<c:if test="${VACT_MAP.feeType == '1'}">정률</c:if>
																				<c:if test="${VACT_MAP.feeType == '2'}">혼합</c:if>
																			</p>
																		</div>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3"></label>
																		<div class="col-md-9">
																			<p class="form-control-static"></p>
																		</div>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">가맹점 정산유형</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${VACT_MAP.settleType}</p>
																		</div>
																	</div>
																</div>
																<c:if test="${VACT_MAP.feeType == '2'}">
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3"></label>
																			<div class="col-md-9">
																				<p class="form-control-static"></p>
																			</div>
																		</div>
																	</div>
																</c:if>
																<c:if test="${VACT_MAP.feeType == '0' || VACT_MAP.feeType == '2'}">
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">가맹점 정산수수료</label>
																			<div class="col-md-9">
																				<p class="form-control-static digits">${VACT_MAP.fee}</p> 원
																			</div>
																		</div>
																	</div>
																</c:if>
																<c:if test="${VACT_MAP.feeType == '1' || VACT_MAP.feeType == '2'}">
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">가맹점 정산수수료율</label>
																			<div class="col-md-9">
																				<p class="form-control-static"><fmt:formatNumber value="${VACT_MAP.rate * 100}" pattern="0.000"/> % (VAT별도)</p>
																			</div>
																		</div>
																	</div>
																</c:if>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">대행사 정산유형명</label>
																		<div class="col-md-9">
																			<c:if test="${VACTDISTMAP ne null }">
																				<p class="form-control-static">${VACTDISTMAP.settleName} (${VACTDISTMAP.payStatus})</p>
																			</c:if>
																			<c:if test="${empty VACTDISTMAP}">
																				<p class="form-control-static">${VACTDISTMAP.settleName} (${VACTDISTMAP.payStatus})</p>
																			</c:if>
																			<c:if test="${VACT_MAP.distSettleType == 'M+25'}">
																				<span>매월 25일</span>
																			</c:if>
																			<c:if test="${VACT_MAP.distSettleType == 'M+15'}">
																				<span>매월 15일</span>
																			</c:if>
																			<c:if test="${VACT_MAP.distSettleType == 'W+3'}">
																				<span>매주 수요일</span>
																			</c:if>
																		</div>
																	</div>
																</div>
																<c:if test="${VACT_MAP.feeType == '2'}">
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3"></label>
																			<div class="col-md-9">
																				<p class="form-control-static"></p>
																			</div>
																		</div>
																	</div>
																</c:if>
																<c:if test="${VACT_MAP.feeType == '0' || VACT_MAP.feeType == '2'}">
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">대행사 수수료</label>
																			<div class="col-md-9">
																				<p class="form-control-static digits">${VACT_MAP.distFee}</p> 원
																			</div>
																		</div>
																	</div>
																</c:if>
																<c:if test="${VACT_MAP.feeType == '1' || VACT_MAP.feeType == '2'}">
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">대행사 수수료율</label>
																			<div class="col-md-9">
																				<p class="form-control-static distRate"><fmt:formatNumber value="${VACT_MAP.distRate * 100}" pattern="0.000"/> % (VAT별도)</p>
																			</div>
																		</div>
																	</div>
																</c:if>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">에이전시 정산유형명</label>
																		<div class="col-md-9">
																			<c:if test="${VACTAGENCYMAP ne null }">
																				<p class="form-control-static">${VACTAGENCYMAP.settleName} (${VACTAGENCYMAP.payStatus})</p>
																			</c:if>
																			<c:if test="${empty VACTAGENCYMAP}">
																				<p class="form-control-static">${VACTAGENCYMAP.settleName} (미사용)</p>
																			</c:if>
																			<c:if test="${VACT_MAP.agencySettleType == 'M+25'}">
																				<span>매월 25일</span>
																			</c:if>
																			<c:if test="${VACT_MAP.agencySettleType == 'M+15'}">
																				<span>매월 15일</span>
																			</c:if>
																			<c:if test="${VACT_MAP.agencySettleType == 'W+3'}">
																				<span>매주 수요일</span>
																			</c:if>
																		</div>
																	</div>
																</div>
																<c:if test="${VACT_MAP.feeType == '2'}">
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3"></label>
																			<div class="col-md-9">
																				<p class="form-control-static"></p>
																			</div>
																		</div>
																	</div>
																</c:if>
																<c:if test="${VACT_MAP.feeType == '0' || VACT_MAP.feeType == '2'}">
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">에이전시 수수료</label>
																			<div class="col-md-9">
																				<p class="form-control-static digits">${VACT_MAP.agencyFee}</p> 원
																			</div>
																		</div>
																	</div>
																</c:if>
																<c:if test="${VACT_MAP.feeType == '1' || VACT_MAP.feeType == '2'}">
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">에이전시 수수료율</label>
																			<div class="col-md-9">
																				<p class="form-control-static agencyRate"><fmt:formatNumber value="${VACT_MAP.agencyRate * 100}" pattern="0.000"/> % (VAT별도)</p>
																			</div>
																		</div>
																	</div>
																</c:if>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">지사 정산유형명</label>
																		<div class="col-md-9">
																			<c:if test="${VACTSALESMAP ne null }">
																				<p class="form-control-static">${VACTSALESMAP.settleName} (${VACTSALESMAP.payStatus})</p>
																			</c:if>
																			<c:if test="${empty VACTSALESMAP}">
																				<p class="form-control-static">${VACTSALESMAP.settleName} (미사용)</p>
																			</c:if>	
																			<c:if test="${VACT_MAP.salesSettleType == 'M+10'}">
																				<span>매월 10일</span>
																			</c:if>
																			<c:if test="${VACT_MAP.salesSettleType == 'M+25'}">
																				<span>매월 25일</span>
																			</c:if>
																			<c:if test="${VACT_MAP.salesSettleType == 'M+15'}">
																				<span>매월 15일</span>
																			</c:if>
																			<c:if test="${VACT_MAP.salesSettleType == 'W+3'}">
																				<span>매주 수요일</span>
																			</c:if>
																		</div>
																	</div>
																</div>
																<c:if test="${VACT_MAP.feeType == '2'}">
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3"></label>
																			<div class="col-md-9">
																				<p class="form-control-static"></p>
																			</div>
																		</div>
																	</div>
																</c:if>
																<c:if test="${VACT_MAP.feeType == '0' || VACT_MAP.feeType == '2'}">
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">지사 수수료</label>
																			<div class="col-md-9">
																				<p class="form-control-static digits">${VACT_MAP.salesFee}</p> 원
																			</div>
																		</div>
																	</div>
																</c:if>
																<c:if test="${VACT_MAP.feeType == '1' || VACT_MAP.feeType == '2'}">
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">지사 수수료율</label>
																			<div class="col-md-9">
																				<p class="form-control-static salesRate"><fmt:formatNumber value="${VACT_MAP.salesRate * 100}" pattern="0.000"/> % (VAT별도)</p>
																			</div>
																		</div>
																	</div>
																</c:if>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">거래전달 프로토콜</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${VACT_MAP.hookType}</p>
																		</div>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">거래전달 주소(URL)</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${VACT_MAP.hookAddr}</p>
																		</div>
																	</div>
																</div>
																<c:if test="${VACT_MAP.issueType eq '영구'}">
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">발급계좌수</label>
																			<div class="col-md-9">
																				<p class="form-control-static">${VACT_MAP.quantity} 계좌 보유</p> 
																				<button type="button" class="btn btn-sm btn-default" onclick="location.href='/mcht/vact/issue/form/${DATAMAP.mchtId}';">
																					<i class="fa fa-check"></i> 가상계좌 발급정보 관리
																				</button>
																			</div>
																		</div>
																	</div>
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3"></label>
																			<div class="col-md-9">
																				<p class="form-control-static"></p>
																			</div>
																		</div>
																	</div>
																</c:if>
																<div class="row">
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">1회한도</label>
																			<div class="col-md-9">
																				<p class="form-control-static digits">${VACT_MAP.limitOnce}</p> 원
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">1일한도</label>
																			<div class="col-md-9">
																				<p class="form-control-static digits">${VACT_MAP.limitDay}</p> 원
																			</div>
																		</div>
																	</div>
																	<!--/span-->
																</div>
																<div class="row">
																	<div class="col-md-6">
																		<div class="form-group pg-view-group">
																			<label class="control-label col-md-3">가상계좌별<br>1일입금 제한횟수</label>
																			<div class="col-md-9">
																				<p class="form-control-static digits">${VACT_MAP.limitDayCnt}</p>
																			</div>
																		</div>
																	</div>
																</div>
																<c:if test="${CP_SESSION.grade eq '본사'}">
																	<c:if test="${VACT_MAP.settleType eq 'D+0' || VACT_MAP.settleType eq 'A+0' || VACT_MAP.settleType eq 'A+1' || VACT_MAP.settleType eq 'A+2'}">
																		<div class="form-group col-sm-12 form-subtitle">
																			<label><i class="fa fa-reorder"></i> 실시간 정산 정보</label>
																		</div>
																		<div class="row">
																			<div class="col-md-6">
																				<div class="form-group pg-view-group">
																					<label class="control-label col-md-3">실시간 정산<br>출금 수수료 납부자</label>
																					<div class="col-md-9">
																						<p class="form-control-static">${VACT_MAP.payOutType}</p>
																					</div>
																				</div>
																			</div>
																			<div class="col-md-6">
																				<div class="form-group pg-view-group">
																					<label class="control-label col-md-3">실시간 정산<br>출금 수수료</label>
																					<div class="col-md-9">
																						<p class="form-control-static digits">${VACT_MAP.payOutFee}</p> 원
																					</div>
																				</div>
																			</div>
																			<div class="col-md-6">
																				<div class="form-group pg-view-group">
																					<label class="control-label col-md-3">실시간 정산<br>전산 전송 간격</label>
																					<div class="col-md-9">
																						<p class="form-control-static digits">${VACT_MAP.transferInterval}</p>
																					</div>
																				</div>
																			</div>
																		</div>
																		<div class="row">
																			<div class="col-md-6">
																				<div class="form-group pg-view-group">
																					<label class="control-label col-md-3">실시간 정산 출금<br>수수료 분배</label>
																					<div class="col-md-9">
																						<p class="form-control-static">${VACT_MAP.payInStatus}</p>
																					</div>
																				</div>
																			</div>
																		</div>
																		<div class="row">
																			<div class="col-md-6">
																				<div class="form-group pg-view-group">
																					<label class="control-label col-md-3">대행사 지급</label>
																					<div class="col-md-9">
																						<p class="form-control-static digits">${VACT_MAP.distPayInFee}</p> 원
																					</div>
																				</div>
																			</div>
																			<div class="col-md-6">
																				<div class="form-group pg-view-group">
																					<label class="control-label col-md-3">에이전시 지급</label>
																					<div class="col-md-9">
																						<p class="form-control-static digits">${VACT_MAP.agencyPayInFee}</p> 원
																					</div>
																				</div>
																			</div>
																			<div class="col-md-6">
																				<div class="form-group pg-view-group">
																					<label class="control-label col-md-3">지사 지급</label>
																					<div class="col-md-9">
																						<p class="form-control-static digits">${VACT_MAP.salesPayInFee}</p> 원
																					</div>
																				</div>
																			</div>
																		</div>
																	</c:if>
																	<div class="form-group col-sm-12 form-subtitle">
																		<label><i class="fa fa-reorder"></i>가상계좌 인증 서비스 정보</label>
																	</div>
																	<div class="row">
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">인증유형</label>
																				<div class="col-md-9">
																					<p id="authType" name="authType" class="form-control-static">
																						<c:if test="${VACT_MAP.authType == '0'}">미사용</c:if>
																						<c:if test="${VACT_MAP.authType == '1'}">API인증</c:if>
																					</p>
																				</div>
																			</div>
																		</div>
																	</div>
																	
																	<div class="row">
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">실명인증<br>원가수수료</label>
																				<div class="col-md-9">
																					<c:forEach var="entry" items="${ORGFEEMAP}" varStatus="status">
																						<c:if test="${entry['codeName'] == 'OWNER'}">
																							<p class="form-control-static digits">${entry['code']}</p> 원
																						</c:if>
																					</c:forEach>
																				</div>
																			</div>
																		</div>
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">실명인증<br>수수료</label>
																				<div class="col-md-9">
																					<p class="form-control-static digits">${VACT_MAP.ownerAuthFee}</p> 원
																				</div>
																			</div>
																		</div>
																	</div>
																	
																	<div class="row">
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">1원인증<br>원가수수료</label>
																				<div class="col-md-9">
																					<c:forEach var="entry" items="${ORGFEEMAP}" varStatus="status">
																						<c:if test="${entry['codeName'] == 'ACCOUNT'}">
																							<p class="form-control-static digits">${entry['code']}</p> 원
																						</c:if>
																					</c:forEach>
																				</div>
																			</div>
																		</div>
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">1원인증<br>수수료</label>
																				<div class="col-md-9">
																					<p class="form-control-static digits">${VACT_MAP.accountAuthFee}</p> 원
																				</div>
																			</div>
																		</div>
																	</div>
																	
																	<div class="row">
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">ARS인증<br>원가수수료</label>
																				<div class="col-md-9">
																					<c:forEach var="entry" items="${ORGFEEMAP}" varStatus="status">
																						<c:if test="${entry['codeName'] == 'ARS'}">
																							<p class="form-control-static digits">${entry['code']}</p> 원
																						</c:if>
																					</c:forEach>
																				</div>
																			</div>
																		</div>
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">ARS인증<br>수수료</label>
																				<div class="col-md-9">
																					<p class="form-control-static digits">${VACT_MAP.arsAuthFee}</p> 원
																				</div>
																			</div>
																		</div>
																	</div>
																	<div class="row">
																		<div class="col-md-6">
																			<div id="respiteCntDiv" class="form-group pg-view-group">
																				<label class="control-label col-md-3">API인증<br>인증유예횟수</label>
																				<div class="col-md-9">
																					<p class="form-control-static digits">${VACT_MAP.respiteCnt}</p> 회
																				</div>
																			</div>
																		</div>
																	</div>
																</c:if>
															</div>
															<div class="form-actions">
																<div class="row">
																	<div class="col-md-12">
																		<button type="button" class="btn btn-sm green pull-right" onclick="location.href='/mcht/vact/modify/${DATAMAP.mchtId}';">
																			<i class="fa fa-pencil"></i> 가상계좌 수정
																		</button>
																	</div>
																	<div class="col-md-6"></div>
																</div>
															</div>
															</c:if>
															<c:if test="${empty VACT_MAP}">
																<div class="form-actions">
																	<div class="row">
																		<div class="col-md-12">
																			<button type="button" class="btn btn-sm green pull-right" onclick="location.href='/mcht/vact/add/${DATAMAP.mchtId}';">
																				<i class="fa fa-pencil"></i> 가상계좌 설정
																			</button>
																		</div>
																		<div class="col-md-6"></div>
																	</div>
																</div>
															</c:if>
														</form>
													</div>
													<!-- 가상계좌 정보 끝 시작 -->
													
													<div class="tab-pane" id="tab_pisp">
														<form class="form-horizontal form" role="form">
															<c:if test="${not empty PISP_MAP}">
															<div class="form-body row">
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">가맹점아이디</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${PISP_MAP.mchtId}</p>
																		</div>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">상태</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${PISP_MAP.status}</p>
																		</div>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">서비스개시일자</label>
																		<div class="col-md-9">
																			<p class="form-control-static date">${PISP_MAP.startDay}</p>
																		</div>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">정산주기</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${PISP_MAP.settleType}</p>
																		</div>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">이체수수료</label>
																		<div class="col-md-9">
																			<p class="form-control-static digits">${PISP_MAP.netFee}</p>
																		</div>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">입금수수료</label>
																		<div class="col-md-9">
																			<p class="form-control-static digits">${PISP_MAP.vactFee}</p>
																		</div>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">계좌인증수수료</label>
																		<div class="col-md-9">
																			<p class="form-control-static digits">${PISP_MAP.fcsFee}</p>
																		</div>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">지급이체접속KEY</label>
																		<div class="col-md-9">
																			<p class="form-control-static digits">${PISP_MAP.authKey}</p>
																		</div>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">입금은행</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${PISP_MAP.bankCd}</p>
																		</div>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">입금가상계좌</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${PISP_MAP.account}</p>
																		</div>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">입금계좌발급번호</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${PISP_MAP.issueId}</p>
																		</div>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">거래전달 프로토콜</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${PISP_MAP.hookType}</p>
																		</div>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group pg-view-group">
																		<label class="control-label col-md-3">거래전달 주소(URL)</label>
																		<div class="col-md-9">
																			<p class="form-control-static">${PISP_MAP.hookAddr}</p>
																		</div>
																	</div>
																</div>
																
															</div>
															<div class="form-actions">
																<div class="row">
																	<div class="col-md-12">
																		<button type="button" class="btn btn-sm green pull-right" onclick="location.href='/mcht/pisp/modify/${DATAMAP.mchtId}';">
																			<i class="fa fa-pencil"></i> 지급이체 수정 
																		</button>
																	</div>
																	<div class="col-md-6"></div>
																</div>
															</div>
															</c:if>
															<c:if test="${empty PISP_MAP}">
																<div class="form-actions"> 
																	<div class="row">
																		<div class="col-md-12">
																			<button type="button" class="btn btn-sm green pull-right" onclick="location.href='/mcht/pisp/add/${DATAMAP.mchtId}';">
																				<i class="fa fa-pencil"></i> 지급이체 설정
																			</button>
																		</div>
																		<div class="col-md-6"></div> 
																	</div>
																</div>  
															</c:if>
														</form>
													</div>
												
													<!-- 차액정산 탭 시작 -->
													<div class="tab-pane" id="tab_diff">
														<div class="portlet light portlet-form">
															
															<div class="portlet-body form light">
																<!-- BEGIN FORM-->
																<form class="form-horizontal form" role="form">
																	<div class="form-body row">
																		<!--/span-->
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">가맹점이름</label>
																				<div class="col-md-9">
																					<p class="form-control-static">${DATADIFFMAP.mchtName}</p>
																				</div>
																			</div>
																		</div>
																		<!--/span-->
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">사업자번호</label>
																				<div class="col-md-9">
																					<p class="form-control-static">${DATADIFFMAP.mchtCompNo}</p>
																				</div>
																			</div>
																		</div>
																		<!--/span-->
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">업종</label>
																				<div class="col-md-9">
																					<p class="form-control-static">${DATADIFFMAP.bizType}</p>
																				</div>
																			</div>
																		</div>
																		<!--/span-->
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">웹사이트URL</label>
																				<div class="col-md-9">
																					<p class="form-control-static">${DATADIFFMAP.mchtUrl}</p>
																				</div>
																			</div>
																		</div>
																		<!--/span-->
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">대표자명</label>
																				<div class="col-md-9">
																					<p class="form-control-static">${DATADIFFMAP.mchtCeo}</p>
																				</div>
																			</div>
																		</div>
																		<!--/span-->
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">가맹점전화번호</label>
																				<div class="col-md-9">
																					<p class="form-control-static">${DATADIFFMAP.mchtPhone}</p>
																				</div>
																			</div>
																		</div>
																		<!--/span-->
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">이메일주소</label>
																				<div class="col-md-9">
																					<p class="form-control-static">${DATADIFFMAP.mchtEmail}</p>
																				</div>
																			</div>
																		</div>
																		<!--/span-->
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">우편번호</label>
																				<div class="col-md-9">
																					<p class="form-control-static">${DATADIFFMAP.zip}</p>
																				</div>
																			</div>
																		</div>
																		<!--/span-->
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">주소</label>
																				<div class="col-md-9">
																					<p class="form-control-static">${DATADIFFMAP.addr1}</p>
																				</div>
																			</div>
																		</div>
																		<!--/span-->
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">상세주소</label>
																				<div class="col-md-9">
																					<p class="form-control-static">${DATADIFFMAP.addr2}</p>
																				</div>
																			</div>
																		</div>
																		<!--/span-->
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">차액정산 VAN</label>
																				<div class="col-md-9">
																					<p class="form-control-static">${DATADIFFMAP.vanName}</p>
																				</div>
																			</div>
																		</div>
																	</div>
																	<c:choose>
																		<c:when test="${empty DATADIFFMAP}">
																			<div class="actions">
																				<button type="button" class="btn btn-sm green pull-right" style="margin-right:10px;" onclick="location.href='/mcht/diff/add/${DATAMAP.mchtId}';">
																						<i class="fa fa-pencil"></i> 영중소 가맹점 정보 등록
																				</button>
																			</div>
																		</c:when>
																		<c:when test="${DATADIFFMAP.recordType eq 'A' }">
																			<div class="actions">
																				<button type="button" class="btn btn-sm green pull-right" style="margin-right:10px;" onclick="location.href='/mcht/diff/modify/${DATAMAP.mchtId}';">
																						<i class="fa fa-pencil"></i> 영중소 가맹점 정보 수정
																				</button>
																			</div>
																		</c:when>
																		<c:when test="${not empty DATADIFFLIST}">
																			<div class="table-scrollable">
																				<!-- 리스트 본문 시작 -->
																				<table class="pg-table table table-striped table-hover flip-content">
																					<!-- table-bordered -->
																					<thead>
																						<tr>
																							<th>No</th>
																							<th data-sort="string">카드사</th>
																							<th data-sort="string">카드사요청일자</th>
																							<th data-sort="string">결과일자</th>
																							<th data-sort="string">등록성공여부</th>
																							<th data-sort="string" style="min-width:200px;">실패사유</th>
																							
																						</tr>
																					</thead>
																					<tbody id="list">
																						<c:forEach var="entry" items="${DATADIFFLIST}" varStatus="status">
																							<tr>
																								<td>${status.count}</td>
																								<td>${entry.cardName}</td>
																								<td class="date">${entry.cardReqDay}</td>
																								<td class="date">${entry.resultDay}</td>
																								<td>${entry.regResult}</td>
																								<td>${entry.failMsg}</td>
																							</tr>
																						</c:forEach>
																					</tbody>
																				</table>
																			</div>
																		</c:when>
																	</c:choose>
																</form>
																<!-- END FORM-->
															</div>
														</div>
													</div>
													<!-- 차액정산 정보 탭 종료 -->
													<!-- 충전정산 탭 시작 -->
													<div class="tab-pane" id="tab_chargeMng">
														<div class="portlet light portlet-form">
															
															<div class="portlet-body form light">
																<!-- BEGIN FORM-->
																<form class="form-horizontal form" role="form">
																	<div class="form-body row">
																		<!--/span-->
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">출금설정</label>
																				<div class="col-md-9">
																					<p class="form-control-static">${DATACHARGEMAP.status}</p>
																				</div>
																			</div>
																		</div>
																		<!--/span-->
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">출금고객적요</label>
																				<div class="col-md-9">
																					<p class="form-control-static">${DATACHARGEMAP.recordInfo}</p>
																				</div>
																			</div>
																		</div>
																		<!--/span-->
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">출금거래전달 주소(URL)</label>
																				<div class="col-md-9">
																					<p class="form-control-static digits">${DATACHARGEMAP.hookAddr}</p>
																				</div>
																			</div>
																		</div>
																		<!--/span-->
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">현재잔액</label>
																				<div class="col-md-9">
																					<p class="form-control-static digits">${DATABALMAP.balance}</p>
																					<c:if test="${CP_SESSION.grade eq '본사' && CP_SESSION.role eq '마스터'}">
																						&nbsp;&nbsp;<button type="button" class="btn btn-sm red" onclick="location.href='/mcht/balance/modify/${DATAMAP.mchtId}';">수기등록</button>
																					</c:if>
																				</div>
																			</div>
																		</div>
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">출금수수료 납부자</label>
																				<div class="col-md-9">
																					<p class="form-control-static">${DATACHARGEMAP.payOutType}</p>
																				</div>
																			</div>
																		</div>
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">출금수수료(VAT별도)</label>
																				<div class="col-md-9">
																					<p class="form-control-static digits">${DATACHARGEMAP.withdrawFee}</p>
																				</div>
																			</div>
																		</div>
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">출금수수료 분배</label>
																				<div class="col-md-9">
																					<p class="form-control-static">${DATACHARGEMAP.payInStatus}</p>
																				</div>
																			</div>
																		</div>
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3"></label>
																				<div class="col-md-9">
																					<p class="form-control-static"></p>
																				</div>
																			</div>
																		</div>
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">대행사 지급</label>
																				<div class="col-md-9">
																					<p class="form-control-static digits">${DATACHARGEMAP.distPayInFee}</p>
																				</div>
																			</div>
																		</div>
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">에이전시 지급</label>
																				<div class="col-md-9">
																					<p class="form-control-static digits">${DATACHARGEMAP.agencyPayInFee}</p>
																				</div>
																			</div>
																		</div>
																		<div class="col-md-6">
																			<div class="form-group pg-view-group">
																				<label class="control-label col-md-3">지사 지급</label>
																				<div class="col-md-9">
																					<p class="form-control-static digits">${DATACHARGEMAP.salesPayInFee}</p>
																				</div>
																			</div>
																		</div>
																	</div>
																	<c:if test="${CP_SESSION.grade eq '본사'}">
																		<c:choose>
																			<c:when test="${empty DATACHARGEMAP}">
																				<div class="actions">
																					<button type="button" class="btn btn-sm red pull-right" style="margin-right:10px;" onclick="location.href='/mcht/chargeMng/add/${DATAMAP.mchtId}';">
																							<i class="fa fa-pencil"></i> 충전정산 정보 등록
																					</button>
																				</div>
																			</c:when>
																			<c:otherwise>
																				<div class="actions">
																					<button type="button" class="btn btn-sm green pull-right" style="margin-right:10px;" onclick="location.href='/mcht/chargeMng/modify/${DATAMAP.mchtId}';">
																							<i class="fa fa-pencil"></i> 충전정산 정보 수정
																					</button>
																				</div>
																			</c:otherwise>
																		</c:choose>
																	</c:if>
																</form>
																<!-- END FORM-->
															</div>
														</div>
													</div>
													<!-- 차액정산 정보 탭 종료 -->
													
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
	<%--<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=525bdd42a1b50641247881ae3f07e6cb&libraries=services"></script> --%>
	<script type="text/javascript">
		$(document).ready(function() {
			$('.nav-tabs').find('li.${TAB} a').tab('show');

			if(${VACT_MAP.authType == '0'}){
				$('#respiteCntDiv').hide();
				$('#stateInitCntDiv').hide();
				$('#authBankCdDiv').hide();
			}else if(${VACT_MAP.authType == '1'}){
				$('#respiteCntDiv').show();
				$('#stateInitCntDiv').hide();
				$('#authBankCdDiv').hide();
			}else if(${VACT_MAP.authType == '2'}){
				$('#respiteCntDiv').hide();
				$('#stateInitCntDiv').show();
				$('#authBankCdDiv').show();
			}
		});
				
		$('.nav-tabs a[href="#tab_map"]').click(function(){
			var options = {
					center: new daum.maps.LatLng('${DATAMAP.lat}', '${DATAMAP.lng}'),
					level: 4
			};
			var map = new daum.maps.Map(document.getElementById('map_container'), options);
						
			// 마커가 표시될 위치입니다 
			var markerPosition  = new daum.maps.LatLng('${DATAMAP.lat}', '${DATAMAP.lng}'); 
			
			// 마커를 생성합니다
			var marker = new daum.maps.Marker({
			    position: markerPosition
			});

			// 마커가 지도 위에 표시되도록 설정합니다
			marker.setMap(map);
			setTimeout(function(){ 
				map.relayout(); 
				var moveLatLon = new daum.maps.LatLng('${DATAMAP.lat}', '${DATAMAP.lng}');
			    // 지도 중심을 이동 시킵니다
			    map.setCenter(moveLatLon);
			}, 100);
		});
		
		$('.form-actions.def-action-mirror').html($('.form-actions.def-action').html());
		<c:if test="${CP_SESSION.grade eq '본사'}">
		function statusChange(idx, status) {
			$.ajax({
	            type: "POST",
	            url: "/mcht/doc/status",
	            data: "idx="+idx+'&status='+status,
	            success: function(res, textStatus){
	    			if(res == "OK"){
	    				bootbox.alert("파일 상태 변경에 성공했습니다.", function() {
	    					location.href = "/mcht/view/${DATAMAP.mchtId}/tab_doc";
	    				});
	    			}else{
	    				bootbox.alert("파일 상태 변경에 실패했습니다.", function() {
	    					location.href = "/mcht/view/${DATAMAP.mchtId}/tab_doc";
	    				});
	    			}
	    		},
	    		error: function(xhr, textStatus, errorThrown){
	    			bootbox.alert('Error ' + errorThrown);
	    		}
	        });
		}
		
		function statusChangeAll(status) {
			$.ajax({
	            type: "POST",
	            url: "/mcht/doc/status/all",
	            data: "mchtId=${DATAMAP.mchtId}&status=" + status,
	            success: function(res, textStatus){
	    			if(res == "OK"){
	    				bootbox.alert("파일 상태 변경에 성공했습니다.", function() {
	    					location.href = "/mcht/view/${DATAMAP.mchtId}/tab_doc";
	    				});
	    			}else{
	    				bootbox.alert("파일 상태 변경에 실패했습니다.", function() {
	    					location.href = "/mcht/view/${DATAMAP.mchtId}/tab_doc";
	    				});
	    			}
	    		},
	    		error: function(xhr, textStatus, errorThrown){
	    			bootbox.alert('Error ' + errorThrown);
	    		}
	        });
		}
		</c:if>
		
		function linkToMng(mchtId, type)  {
			
			var str = "";
			
			$.ajax({
	            type: "POST",
	            url: "/mcht/mng/check/" + mchtId,
	            data: "mchtId=" + mchtId,
	            success: function(res){
	    			if(res.distRes == "OK" && res.agencyRes == "OK"){
	    				if(type == 'add') {
	    					location.href='/mcht/mng/add/' + mchtId;
	    				} else {
	    					location.href='/mcht/mng/modify/' + mchtId;
	    				}
	    			} else {
	    				if(res.distRes == "NOK" && res.agencyRes == "NOK") {
	    					str = res.agencyMsg + "와 " + res.distMsg;
	    				} else if (res.distRes == "NOK") {
	    					str = res.distMsg; 
	    				} else {
	    					str = res.agencyMsg;
	    				}
	    				
	    				bootbox.alert(str + "'정산정보'를 입력하세요.");
	    				return false;
	    			}
	    		},
	    		error: function(xhr, textStatus, errorThrown){
	    			bootbox.alert('Error ' + errorThrown);
	    		}
	        });
		}
		
		function linkToVact(mchtId, type)  {
			
			var str = "";
			
			$.ajax({
	            type: "POST",
	            url: "/mcht/vact/check/" + mchtId,
	            data: "mchtId=" + mchtId,
	            success: function(res){
	    			if(res.distRes == "OK" && res.agencyRes == "OK"){
	    				if(type == 'add') {
	    					location.href='/mcht/vact/add/' + mchtId;
	    				} else {
	    					location.href='/mcht/vact/modify/' + mchtId;
	    				}
	    			} else {
	    				if(res.distRes == "NOK" && res.agencyRes == "NOK") {
	    					str = res.agencyMsg + "와 " + res.distMsg;
	    				} else if (res.distRes == "NOK") {
	    					str = res.distMsg; 
	    				} else {
	    					str = res.agencyMsg;
	    				}
	    				
	    				bootbox.alert(str + "'가상계좌 정산정보'를 입력하세요.");
	    				return false;
	    			}
	    		},
	    		error: function(xhr, textStatus, errorThrown){
	    			bootbox.alert('Error ' + errorThrown);
	    		}
	        });
		}

		$('#nav-mcht').addClass('active');
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>