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
									<li><span>월세앱 관리</span><i class="fa fa-circle"></i></li>
									<li><span>가맹점조회</span></li>
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
								<div class="page-content-inner" id="search-container">
									<!-- 검색 폼 시작 -->
									<div class="portlet light portlet-form">
										<div class="portlet-body form light">
											<form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/rent/mcht/list" method="post">
												<input type="hidden" data-reg="false" name="reason" value="가맹점 리스트">
												<c:if test="${CP_SESSION.grade == '본사'}">
												<input type="hidden" data-reg="false" name="thead" value="mchtId:가맹점ID,name:가맹점,nick:가맹점명,mchtActiveDate:등록일자,diffType:영중소구분,distId:대행사ID,distName:대행사명,agencyId:에이전시ID,agencyName:에이전시명,salesId:지사ID,salesName:지사명,status:상태,decidentity:사업자/주민번호,bizType:업태,bizCategory:업종,settleType:정산주기,rate:가맹점 수수료,payOutFee:실시간 수수료,distRate:대행사수수료,agencyRate:에이전시수수료,salesRate:지사수수료,diff0DistRate:대행사 영세 차액정산 수수료율(신용),diff1DistRate:대행사 중소1 차액정산 수수료율(신용),diff2DistRate:대행사 중소2 차액정산 수수료율(신용),diff3DistRate:대행사 중소3 차액정산 수수료율(신용),diff0CheckDistRate:대행사 영세 차액정산 수수료율(체크),diff1CheckDistRate:대행사 중소1 차액정산 수수료율(체크),diff2CheckDistRate:대행사 중소2 차액정산 수수료율(체크),diff3CheckDistRate:대행사 중소3 차액정산 수수료율(체크),diff0AgencyRate:에이전시 영세 차액정산 수수료율(신용),diff1AgencyRate:에이전시 중소1 차액정산 수수료율(신용),diff2AgencyRate:에이전시 중소2 차액정산 수수료율(신용),diff3AgencyRate:에이전시 중소3 차액정산 수수료율(신용),diff0CheckAgencyRate:에이전시 영세 차액정산 수수료율(체크),diff1CheckAgencyRate:에이전시 중소1 차액정산 수수료율(체크),diff2CheckAgencyRate:에이전시 중소2 차액정산 수수료율(체크),diff3CheckAgencyRate:에이전시 중소3 차액정산 수수료율(체크),diff0SalesRate:지사 영세 차액정산 수수료율(신용),diff1SalesRate:지사 중소1 차액정산 수수료율(신용),diff2SalesRate:지사 중소2 차액정산 수수료율(신용),diff3SalesRate:지사 중소3 차액정산 수수료율(신용),diff0CheckSalesRate:지사 영세 차액정산 수수료율(체크),diff1CheckSalesRate:지사 중소1 차액정산 수수료율(체크),diff2CheckSalesRate:지사 중소2 차액정산 수수료율(체크),diff3CheckSalesRate:지사 중소3 차액정산 수수료율(체크),limitOnce:1회한도,tel1:연락처,tel2:연락처2,fax:팩스,zip:우편번호,addr1:주소,addr2:상세주소,ceoName:대표자,ceoPhone:대표자 연락처,ceoTel:대표자 전화번호,ceoZip:대표자 우편번호,ceoAddr1:대표자 주소,ceoAddr2:대표자 상세주소,managerName:관리자,managerPhone:관리자연락처,bankName:은행,accntHolder:예금주,account:계좌번호,email:이메일,regDay:등록일,holderName:가상계좌 기본 예금주명,vactStatus:가상계좌 상태,issueType:가상계좌 발행유형,expireSet:가상계좌 만료일 지정,startDay:가상계좌 거래시작일,settleTarget:가상계좌 정산여부,feeType:가상계좌 수수료유형,vactSettleType:가상계좌 정산유형,fee:가상계좌 정산수수료,vactRate:가상계좌 정산수수료율,autyType:가상계좌 인증서비스,distSettleType:가상계좌 대행사정산유형,distFee:가상계좌 대행사수수료,vactDistRate:가상계좌 대행사수수료율,agencySettleType:가상계좌 에이전시정산유형,agencyFee:가상계좌 에이전시수수료,vactAgencyRate:가상계좌 에이전시수수료율,salesSettleType:가상계좌 지사정산유형,salesFee:가상계좌 지사수수료,vactSalesRate:가상계좌 지사수수료율,hookType:가상계좌 거래전달프로토콜,hookAddr:가상계좌 거래전달주소,vactPayOutFee:가상계좌 실시간정산출금수수료,transferInterval:가상계좌 실시간정산전송간격">
												</c:if>
												<c:if test="${CP_SESSION.grade eq '대행사'}">
												<input type="hidden" data-reg="false" name="thead" value="mchtId:가맹점ID,name:가맹점,nick:가맹점명,mchtActiveDate:등록일자,diffType:영중소구분,status:상태,bizType:업태,bizCategory:업종,settleType:정산주기,sumRate:가맹점 수수료,distId:대행사ID,agencyId:에이전시ID,salesId:지사ID,tel1:연락처,tel2:연락처2,fax:팩스,zip:우편번호,addr1:주소,addr2:상세주소,ceoName:대표자,ceoPhone:대표자 연락처,ceoTel:대표자 전화번호,ceoZip:대표자 우편번호,ceoAddr1:대표자 주소,ceoAddr2:대표자 상세주소,managerName:관리자,managerPhone:관리자연락처,bankName:은행,accntHolder:예금주,account:계좌번호">
												</c:if>
												<c:if test="${CP_SESSION.grade eq '에이전시'}">
												<input type="hidden" data-reg="false" name="thead" value="mchtId:가맹점ID,name:가맹점,nick:가맹점명,mchtActiveDate:등록일자,diffType:영중소구분,status:상태,bizType:업태,bizCategory:업종,settleType:정산주기,sumRate:가맹점 수수료,agencyId:에이전시ID,salesId:지사ID,tel1:연락처,tel2:연락처2,fax:팩스,zip:우편번호,addr1:주소,addr2:상세주소,ceoName:대표자,ceoPhone:대표자 연락처,ceoTel:대표자 전화번호,ceoZip:대표자 우편번호,ceoAddr1:대표자 주소,ceoAddr2:대표자 상세주소,managerName:관리자,managerPhone:관리자연락처,bankName:은행,accntHolder:예금주,account:계좌번호">
												</c:if>
												<c:if test="${CP_SESSION.grade eq '지사'}">
												<input type="hidden" data-reg="false" name="thead" value="mchtId:가맹점ID,name:가맹점,nick:가맹점명,mchtActiveDate:등록일자,status:상태,bizType:업태,bizCategory:업종,settleType:정산주기,sumRate:가맹점 수수료,salesId:지사ID,tel1:연락처,tel2:연락처2,fax:팩스,zip:우편번호,addr1:주소,addr2:상세주소,ceoName:대표자,ceoPhone:대표자 연락처,ceoTel:대표자 전화번호,ceoZip:대표자 우편번호,ceoAddr1:대표자 주소,ceoAddr2:대표자 상세주소,managerName:관리자,managerPhone:관리자연락처,bankName:은행,accntHolder:예금주,account:계좌번호">
												</c:if>
												<div class="form-body">
													<div class="row">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">아이디</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm mchtId" name="mchtId" data-oper="lk" placeholder="가맹점 ID">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">가맹점명</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm name typeahead" name="name" data-oper="lk" data-search="mchtName" placeholder="이름">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">사업자(주민)번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm identity" name="identity" data-oper="eq" placeholder="사업자(주민)번호">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">상태</label>
															<select class="selectpicker btn-sm col-lg-8 col-xs-12" name="status" data-oper="eq">
																<option value="">상태</option>
																<option value="예비">예비</option>
																<option value="대기">승인대기</option>
																<option value="사용" selected>사용</option>
																<option value="중지">중지</option>
															</select>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">계약구분</label>
															<select class="selectpicker btn-sm col-lg-8 col-xs-12" name="contractType" data-oper="eq">
																<option value="" selected>전체</option>
																<option value="임차인">임차인</option>
																<option value="임대인">임대인</option>
																<option value="중개인">중개인</option>
															</select>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">계약검토상태</label>
															<select class="selectpicker btn-sm col-lg-8 col-xs-12" name="contractStatus" data-oper="eq">
																<option value="" selected>전체</option>
																<option value="대기">대기</option>
																<option value="완료">완료</option>
															</select>
														</div>
														
													</div>
													<div class="row search-opt">
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">대표자이름</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm ceoName" name="ceoName" data-oper="lk" placeholder="대표자이름">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">전화번호</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="tel1" data-oper="lk" placeholder="대표전화번호">
															</div>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">정산주기</label>
															<select class="selectpicker col-lg-8" name="settleType" data-oper="eq">
																<option value="">-- 전체 -- </option>
																	<option value="D+0">D+0</option>
																	<option value="A+1">A+1</option>
																	<option value="A+2">A+2</option>
																	<option value="D+1">D+1</option>
																	<option value="D+5">D+5</option>
																</select>
														</div>
														<div class="form-group pg-form-group">
															<label class="control-label col-lg-4">담당자이름</label>
															<div class="col-lg-8">
																<input type="text" class="form-control input-sm" name="managerName" data-oper="lk" placeholder="담당자이름">
															</div>
														</div>
														
														<c:import url="/common/selectGrade.jsp" />
														<input type="hidden" id="grade_search" name="parentId" data-oper="eq" value=""/>
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
									<div class="portlet light portlet-form" id="searchResult">

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
	</div>
	<c:import url="/include/javascript.jsp" />

	<script type="text/javascript">
		
		
		gradeSelector('searchForm', '${CP_SESSION.grade}');
		searchForList();//검색 실행
		
		$('#nav-rent').addClass('active');
		
		<%--
		$('select[name="pgOrFactoring"]').on('change', function() {
			var hidden = $('#pgOrFactoring');
			console.log('pgOrFactoring', $(this).val());
			hidden.data('reg', 'true');
			hidden.attr('data-reg', 'true');
			hidden.val('00');
			if($(this).val() == '선정산') {
				hidden.data('oper', 'eq');
			} else if($(this).val() == 'PG') {
				hidden.data('oper', 'ne');
			} else {
				hidden.data('reg', 'false');
				hidden.attr('data-reg', 'false');
			}
		});		--%>
		
	</script>
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>