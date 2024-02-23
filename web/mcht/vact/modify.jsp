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
								<li><span>가상계좌 정보 수정</span></li>
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
								<div class="portlet-title">
									<div class="caption">
										<i class="fa fa-reorder"></i>
										<span class="caption-title"> ${DATAMAP.name} 가상계좌 정보 수정 </span>
									</div>
								</div>
								<div class="portlet-body form">
									<form class="form-horizontal form-bordered" role="form" data-form="true" id="writeFrm" name="form" action="/mcht/vact/" method="post">
										<input type="hidden" name="action_type" value="update" data-reg="false" />
										<div class="form-body row">
											<div class="form-group col-sm-6">
												<label class="control-label input-sm col-sm-4 req-label">가맹점 ID</label>
												<div class="col-sm-6">
													<input type="text" class="form-control input-sm" name="mchtId" data-key="true" value="${MCHT_MAP.mchtId}"
													 readonly>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label input-sm col-sm-4 req-label">기본 예금주명</label>
												<div class="col-sm-6">
													<input type="text" class="form-control input-sm holderName" maxlength="100" name="holderName" value="${DATAMAP.holderName}">
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label input-sm col-sm-4 req-label">상태</label>
												<select name="status" class="selectpicker col-sm-6">
													<option value="중지" selected>중지</option>
													<option value="사용">사용</option>
												</select>
												<script type="text/javascript">
													document.forms.writeFrm.status.value = '${DATAMAP.status}'
												</script>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label input-sm col-sm-4 req-label">가상계좌 은행</label>
												<select name="vactBankCd" class="selectpicker col-sm-6">
													<option value="089" selected>케이뱅크</option>
													<option value="039">경남은행</option>
													<option value="034">광주은행</option>
													<option value="007">수협은행</option>
												</select>
												<script type="text/javascript">
													document.forms.writeFrm.vactBankCd.value = '${DATAMAP.vactBankCd}'
												</script>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label input-sm col-sm-4 req-label">발행유형</label>
												<select name="issueType" class="selectpicker col-sm-6">
													<option value="임시" selected>임시</option>
													<option value="영구">영구</option>
												</select>
												<script type="text/javascript">
													document.forms.writeFrm.issueType.value = '${DATAMAP.issueType}'
												</script>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4 req-label">만료일 지정</label>
												<div class="col-sm-6">
													<input type="text" class="form-control input-sm numberOnly" maxlength="3" name="expireSet" placeholder=""
													 value="${DATAMAP.expireSet}">
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4 req-label">거래시작일</label>
												<div class="col-sm-6">
													<input type="text" class="form-control input-sm" name="startDay" value="${DATAMAP.startDay}" readonly>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4 req-label">정산대상여부</label>
												<select name="settleTarget" class="selectpicker col-sm-6">
													<option value="Y">Y</option>
													<option value="N">N</option>
												</select>
												<script type="text/javascript">
													document.forms.writeFrm.settleTarget.value = '${DATAMAP.settleTarget}'
												</script>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4 req-label">수수료유형</label>
												<select name="feeType" class="selectpicker col-sm-6">
													<option value="0" selected>정액</option>
													<option value="1">정률</option>
													<option value="2">혼합</option>
												</select>
											</div>
											<script type="text/javascript">
												document.forms.writeFrm.feeType.value = '${DATAMAP.feeType}'
											</script>
											
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4">거래전달 프로토콜</label>
												<select name="hookType" class="selectpicker col-sm-6">
													<option value="HTTPS" selected>HTTPS</option>
													<option value="HTTP">HTTP</option>
													<option value="TCP">TCP</option>
												</select>
												<script type="text/javascript">
													document.forms.writeFrm.hookType.value = '${DATAMAP.hookType}'
												</script>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4">거래전달 주소(URL)</label>
												<div class="col-sm-6">
													<input type="text" class="form-control input-sm" maxlength="100" name="hookAddr" placeholder="api.example.com"
													 value="${DATAMAP.hookAddr}">
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4">가상계좌 상태값 전달 주소(URL)</label>
												<div class="col-sm-6">
													<input type="text" class="form-control input-sm" maxlength="100" name="statusHookAddr" placeholder="api.example.com"
														   value="${DATAMAP.statusHookAddr}">
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4 req-label">1회한도</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control limitOnce currency" data-oper="comma" maxlength="10" name="limitOnce" placeholder="" value="${DATAMAP.limitOnce }">
														<span class="input-group-addon"><i class="fa fa-krw"></i></span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4 req-label">1일한도(가맹점별)</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control limitDay currency" data-oper="comma" maxlength="14" name="limitDay" placeholder="" value="${DATAMAP.limitDay }">
														<span class="input-group-addon"><i class="fa fa-krw"></i></span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4 req-label">가상계좌별<br>1일입금 제한횟수</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control currency limitDayCnt" maxlength="3" name="limitDayCnt" placeholder="" value="${DATAMAP.limitDayCnt}"> 
														<span class="input-group-addon">회</span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4 req-label">입금단위제한</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control currency limitAmount comma" maxlength="14" data-oper="comma" name="limitAmount" placeholder="" value="${DATAMAP.limitAmount}">
														<span class="input-group-addon"><i class="fa fa-krw"></i></span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4 req-label">동일출금계좌<br>1일입금 제한횟수</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control currency eqAccntDepositLimitCnt" maxlength="3" name="eqAccntDepositLimitCnt" placeholder="" value="${DATAMAP.eqAccntDepositLimitCnt}">
														<span class="input-group-addon">회</span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4 req-label">동일출금계좌<br>발급제한횟수</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control currency eqAccntIssueLimitCnt" maxlength="3" name="eqAccntIssueLimitCnt" placeholder="" value="${DATAMAP.eqAccntIssueLimitCnt}">
														<span class="input-group-addon">회</span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4 req-label">동일출금계좌<br>입금 1회한도</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control currency eqAccntLimitOnce comma" maxlength="10" data-oper="comma" name="eqAccntLimitOnce" placeholder="" value="${DATAMAP.eqAccntLimitOnce}">
														<span class="input-group-addon"><i class="fa fa-krw"></i></span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4 req-label">동일출금계좌<br>입금 1일한도</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control currency eqAccntLimitDay comma" maxlength="10" data-oper="comma" name="eqAccntLimitDay" placeholder="" value="${DATAMAP.eqAccntLimitDay}">
														<span class="input-group-addon"><i class="fa fa-krw"></i></span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4 req-label">예금주 확인</label>
												<select name="holderCheck" class="selectpicker col-sm-6">
													<option value="Y">Y</option>
													<option value="N" selected="selected">N</option>
												</select>
												<script type="text/javascript">
													document.forms.writeFrm.holderCheck.value = '${DATAMAP.holderCheck}'
												</script>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4 req-label">개인 가상계좌 사용</label>
												<select name="personalAccountCheck" class="selectpicker col-sm-6">
													<option value="Y">Y</option>
													<option value="N" selected="selected">N</option>
												</select>
												<script type="text/javascript">
													document.forms.writeFrm.personalAccountCheck.value = '${DATAMAP.personalAccountCheck}'
												</script>
											</div>

											<div class="form-group col-sm-6">
												<div style="padding-top:45px; border-left:none;"></div>
											</div>
											<div class="form-group col-sm-12 form-subtitle">
												<label><i class="fa fa-reorder"></i> 가맹점 정보 입력</label>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4 req-label">가맹점 정산유형</label>
												<select name="settleType" class="selectpicker col-sm-6">
													<c:if test="${DATASVCMAP.settle != '충전정산'}">
														<option value="A+0">당일정산(영업일)</option>
														<option value="A+2">당일정산(365)</option>
														<option value="A+1">자동정산</option>
														<option value="D+0">실시간정산</option>
														<option value="D+1">1일 후 정산</option>
														<option value="D+2">2일 후 정산</option>
														<option value="D+3">3일 후 정산</option>
														<option value="D+4">4일 후 정산</option>
														<option value="D+5">5일 후 정산</option>
													</c:if>
													<c:if test="${DATASVCMAP.settle == '충전정산'}">
														<option value="C+0">실시간 충전정산</option>
														<option value="B+1">1일 후 자동충전정산</option>
														<option value="C+1">1일 후 충전정산</option>
														<option value="C+2">2일 후 충전정산</option>
														<option value="C+3">3일 후 충전정산</option>
														<option value="C+4">4일 후 충전정산</option>
														<option value="C+5">5일 후 충전정산</option>
													</c:if>
												</select>
												<script type="text/javascript">
													document.forms.writeFrm.settleType.value = '${DATAMAP.settleType}'
												</script>
											</div>
											<div class="form-group col-sm-6 noneDiv">
												<div style="padding-top:45px; border-left:none;"></div>
											</div>
											<div class="form-group col-sm-6" id="feeDiv">
												<label class="control-label col-sm-4">가맹점 정산수수료(VAT별도)</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control fee comma" data-oper="comma" maxlength="20" name="fee" placeholder="" value="${DATAMAP.fee }">
														<span class="input-group-addon"><i class="fa fa-krw"></i></span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-6" id="rateDiv">
												<label class="control-label col-sm-4">가맹점 정산수수료율</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control rate percent" data-oper="percent" maxlength="9" name="rate" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="${DATAMAP.rate }">
														<span class="input-group-addon"> % (VAT 별도)</span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-12 form-subtitle">
												<label><i class="fa fa-reorder"></i> 대행사 정보 입력</label>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label input-sm col-sm-4">대행사 정산유형</label> 
												<select name="distNum" class="selectpicker col-sm-6" id="distType">
													<c:forEach items="${DISTMNGTYPE }" var="entry">
														<option value="${entry.distNum}">${entry.distSettleName} (${entry.distPayStatus})</option>
													</c:forEach>
												</select>
												<script type="text/javascript">document.forms.writeFrm.distNum.value = '${DATAMAP.distNum}'</script>
												<input type="hidden" name="distSettleType" value="${DATAMAP.distSettleType}">
<%--												<span class="distSettleType"></span>--%>
											</div>
											<div class="form-group col-sm-6 noneDiv">
												<div style="padding-top:45px; border-left:none;"></div>
											</div>
											<div class="form-group col-sm-6" id="distFeeDiv">
												<label class="control-label col-sm-4">대행사수수료(VAT별도)</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control distFee comma" data-oper="comma" maxlength="20" name="distFee" placeholder="" value="${DATAMAP.distFee }">
														<span class="input-group-addon"><i class="fa fa-krw"></i></span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-6" id="distRateDiv">
												<label class="control-label col-sm-4">대행사수수료율</label> 
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control distRate percent" data-oper="percent" maxlength="9" name="distRate" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="${DATAMAP.distRate }">
														<span class="input-group-addon"> % (VAT 별도)</span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-12 form-subtitle">
												<label><i class="fa fa-reorder"></i> 에이전시 정보 입력</label>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label input-sm col-sm-4">에이전시 정산유형</label> 
												<select name="agencyNum" class="selectpicker col-sm-6" id="agencyType">
													<c:forEach items="${AGENCYMNGTYPE }" var="entry">
														<option value="${entry.agencyNum}">${entry.agencySettleName} (${entry.agencyPayStatus})</option>
													</c:forEach>
												</select>
												<script type="text/javascript">document.forms.writeFrm.agencyNum.value = '${DATAMAP.agencyNum}'</script>
												<input type="hidden" name="agencySettleType" value="${DATAMAP.agencySettleType}">
<%--												<span class="agencySettleType"></span>--%>
											</div>
											<div class="form-group col-sm-6 noneDiv">
												<div style="padding-top:45px; border-left:none;"></div>
											</div>
											<div class="form-group col-sm-6" id="agencyFeeDiv">
												<label class="control-label col-sm-4">에이전시수수료(VAT별도)</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control agencyFee comma" data-oper="comma" maxlength="20" name="agencyFee" placeholder="" value="${DATAMAP.agencyFee }">
														<span class="input-group-addon"><i class="fa fa-krw"></i></span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-6" id="agencyRateDiv">
												<label class="control-label col-sm-4">에이전시수수료율</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control agencyRate percent" data-oper="percent" maxlength="9" name="agencyRate" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="${DATAMAP.agencyRate }">
														<span class="input-group-addon"> % (VAT 별도)</span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-12 form-subtitle">
												<label><i class="fa fa-reorder"></i> 지사 정보 입력</label>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label input-sm col-sm-4">지사 정산유형</label> 
												<select name="salesNum" class="selectpicker col-sm-6" id="salesType">
													<c:forEach items="${SALESMNGTYPE }" var="entry">
														<option value="${entry.salesNum}">${entry.salesSettleName} (${entry.salesPayStatus})</option>
													</c:forEach>
												</select>
												<script type="text/javascript">document.forms.writeFrm.salesNum.value = '${DATAMAP.salesNum}'</script>
												<input type="hidden" name="salesSettleType" value="${DATAMAP.salesSettleType}">
<%--												<span class="salesSettleType"></span>--%>
											</div>
											<div class="form-group col-sm-6 noneDiv">
												<div style="padding-top:45px; border-left:none;"></div>
											</div>
											<div class="form-group col-sm-6" id="salesFeeDiv">
												<label class="control-label col-sm-4">지사 수수료(VAT별도)</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control salesFee comma" data-oper="comma" maxlength="20" name="salesFee" placeholder="" value="${DATAMAP.salesFee }">
														<span class="input-group-addon"><i class="fa fa-krw"></i></span>
													</div>
												</div>
											</div>
											<div class="form-group col-sm-6" id="salesRateDiv">
												<label class="control-label col-sm-4">지사 수수료율</label>
												<div class="col-sm-6">
													<div class="input-group input-group-sm">
														<input type="text" class="form-control salesRate percent" data-oper="percent" maxlength="9" name="salesRate" placeholder="% 단위로 입력하세요. (10% = 0.1)" value="${DATAMAP.salesRate }">
														<span class="input-group-addon"> % (VAT 별도)</span>
													</div>
												</div>
											</div>
											<div id="payOutFeeDiv">
												<div class="form-group col-sm-12 form-subtitle">
													<label><i class="fa fa-reorder"></i> 실시간 정산 정보 입력</label>
												</div>
												<div class="form-group col-sm-6">
													<label class="control-label input-sm col-sm-4 req-label">실시간 정산<br>출금 수수료 납부자</label> 
													<select name="payOutType" id="payOutType" class="selectpicker col-sm-6">
														<option value="가맹점" selected>가맹점</option>
														<option value="대행사">대행사</option>
														<option value="에이전시">에이전시</option>
														<option value="지사">지사</option>
													</select>
													<script type="text/javascript"> document.forms.writeFrm.payOutType.value = '${DATAMAP.payOutType}' </script>
												</div>
												<div class="form-group col-sm-6">
													<label class="control-label col-sm-4 req-label">실시간정산<br>출금 수수료</label>
													<div class="col-sm-6">
														<div class="input-group input-group-sm">
															<input type="text" class="form-control payOutFee comma" data-oper="comma" maxlength="9" name="payOutFee" placeholder="" value="${DATAMAP.payOutFee }">
															<span class="input-group-addon"><i class="fa fa-krw"></i></span>
														</div>
													</div>
												</div>
												<div class="form-group col-sm-6">
													<label class="control-label col-sm-4 req-label">실시간정산<br>전송간격</label>
													<div class="col-sm-6">
														<div class="input-group input-group-sm">
															<input type="text" class="form-control currency transferInterval" maxlength="3" name="transferInterval" placeholder="" value="${DATAMAP.transferInterval}"> 
															<span class="input-group-addon">분</span>
														</div>
													</div>
												</div>
												<div class="form-group col-sm-6">
													<div style="padding-top:45px; border-left:none;"></div>
												</div>
												<div id="payIn"> 
													<div class="form-group col-sm-6">
														<label class="control-label input-sm col-sm-4 req-label">실시간 정산<br>출금 수수료 분배</label> 
														<select name="payInStatus" class="selectpicker col-sm-6">
															<option value="중지" selected>중지</option>
															<option value="사용">사용</option>
														</select>
														<script type="text/javascript"> document.forms.writeFrm.payInStatus.value = '${DATAMAP.payInStatus}' </script>
													</div>
													<div class="form-group col-sm-6">
														<div style="padding-top:45px; border-left:none;"></div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">대행사 지급</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control distPayInFee comma" data-oper="comma" maxlength="9" name="distPayInFee" placeholder="" value="${DATAMAP.distPayInFee }">
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">에이전시 지급</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control agencyPayInFee comma" data-oper="comma" maxlength="9" name="agencyPayInFee" placeholder="" value="${DATAMAP.agencyPayInFee }">
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
													<div class="form-group col-sm-6">
														<label class="control-label col-sm-4 req-label">지사 지급</label>
														<div class="col-sm-6">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control salesPayInFee comma" data-oper="comma" maxlength="9" name="salesPayInFee" placeholder="" value="${DATAMAP.salesPayInFee }">
																<span class="input-group-addon"><i class="fa fa-krw"></i></span>
															</div>
														</div>
													</div>
												</div>
											</div>
											<div class="form-group"></div>
										</div>
										<div class="form-group col-sm-12 form-subtitle">
											<label><i class="fa fa-reorder"></i>가상계좌 인증 서비스 정보</label>
										</div>
										<div class="form-group col-sm-6">
											<label class="control-label col-sm-4 req-label">인증유형</label>
											<select name="authType" class="selectpicker col-sm-6">
												<option value="0" selected>미사용</option>
												<option value="1">API인증</option>
											</select>
										</div>
										<script type="text/javascript">
											document.forms.writeFrm.authType.value = '${DATAMAP.authType}'
										</script>
										<%--<div class="form-group col-sm-6">
											<label class="control-label col-sm-4 req-label">실명인증<br>수수료</label>
											<div class="col-sm-6">
												<div class="input-group input-group-sm">
													<input type="text" class="form-control currency ownerAuthFee comma" data-oper="comma" maxlength="10" name="ownerAuthFee" placeholder="" value="${DATAMAP.ownerAuthFee }">
													<span class="input-group-addon"><i class="fa fa-krw"></i></span>
												</div>
											</div>
										</div>
										<div class="form-group col-sm-6">
											<label class="control-label col-sm-4 req-label">1원인증<br>수수료</label>
											<div class="col-sm-6">
												<div class="input-group input-group-sm">
													<input type="text" class="form-control currency accountAuthFee comma" data-oper="comma" maxlength="10" name="accountAuthFee" placeholder="" value="${DATAMAP.accountAuthFee }">
													<span class="input-group-addon"><i class="fa fa-krw"></i></span>
												</div>
											</div>
										</div>
										<div class="form-group col-sm-6">
											<label class="control-label col-sm-4 req-label">ARS인증<br>수수료</label>
											<div class="col-sm-6">
												<div class="input-group input-group-sm">
													<input type="text" class="form-control currency arsAuthFee comma" data-oper="comma" maxlength="10" name="arsAuthFee" placeholder="" value="${DATAMAP.arsAuthFee }">
													<span class="input-group-addon"><i class="fa fa-krw"></i></span>
												</div>
											</div>
										</div>
										<div class="form-group col-sm-6">
											<label class="control-label col-sm-4 req-label">통합인증<br>수수료</label>
											<div class="col-sm-6">
												<div class="input-group input-group-sm">
													<input type="text" class="form-control currency totalAuthFee comma" data-oper="comma" maxlength="10" name="totalAuthFee" placeholder="" value="${DATAMAP.totalAuthFee }">
													<span class="input-group-addon"><i class="fa fa-krw"></i></span>
												</div>
											</div>
										</div>--%>
										<div id="respiteCntDiv" class="form-group col-sm-6">
											<label class="control-label col-sm-4 req-label">API인증<br>인증유예횟수</label>
											<div class="col-sm-6">
												<div class="input-group input-group-sm">
													<input type="text" class="form-control currency respiteCnt" maxlength="3" name="respiteCnt" placeholder="" value="${DATAMAP.respiteCnt}">
													<span class="input-group-addon">회</span>
												</div>
											</div>
										</div>
										<div id="depositLimitCntDiv" class="form-group col-sm-6">
											<label class="control-label col-sm-4 req-label">입금<br>제한횟수</label>
											<div class="col-sm-6">
												<div class="input-group input-group-sm">
													<input type="text" class="form-control currency depositLimitCnt" maxlength="3" name="depositLimitCnt" placeholder="" value="${DATAMAP.depositLimitCnt}">
													<span class="input-group-addon">회</span>
												</div>
											</div>
										</div>
										<script type="text/javascript">
											document.forms.writeFrm.authBankCd.value = '${DATAMAP.authBankCd}'
										</script>
										<div class="form-group"></div>
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
	<c:import url="/include/javascript.jsp" />

	<!-- BEGIN FORM JAVASCRIPT -->
	<script type="text/javascript">
		var form1 = $('#writeFrm');
		var error1 = $('.alert-danger', form1);
		form1.validate({
			rules: {
				mchtId : {
					required : true
				},
				holderName : {
					minlength : 2,
					required : true
				},
				issueType : {
					required : true
				},
				expireSet : {
					required : true
				},
				fee : {
					required : true
				},
				payOutFee: {
					minMoney: function(element){
						if($('#payOutType').find("option:selected").val() == '가맹점'){
							return Number($("input[name='distPayInFee']").val().replace(/,/g, '')) + Number($("input[name='agencyPayInFee']").val().replace(/,/g, '')) + Number($("input[name='salesPayInFee']").val().replace(/,/g, '')) - 1;
						}else {
							return false;
						}
					}
				}
			},
			invalidHandler: function (event, validator) { //display error alert on form submit              
				var error1Str = '<button class="close" data-close="alert"></button>';
				error1Str += "등록 중 잘못된 입력값이 있습니다. 위의 입력 값을 다시 확인하여 주시기 바랍니다.";
				error1.html(error1Str);
				error1.show();
				App.scrollTo(error1, -200);
			},
			submitHandler: function (form) {
				// 수수료 유형이 정액일 때
				if($('select[name="feeType"]').val()=='0'){
					if(Number($('.fee').val()) < Number($('.distFee').val()) + Number($('.agencyFee').val()) + Number($('.salesFee').val())){
						var error1Str = '<button class="close" data-close="alert"></button>';
						error1Str += "대행사, 에이전시, 지사 수수료의 합이 가맹점 수수료 보다 큽니다. 확인해 주시기 바랍니다.";
						error1.html(error1Str);
						error1.show();
						App.scrollTo(error1, -200);
						$('.fee').focus();
					} else {
						error1.hide();
						bootbox.confirm("입력하신 정보로 가상계좌를 설정하시겠습니까?", function(result) {
							if (result) {
								ajaxFormSubmit(form, '/mcht/view/'+ $('input[name="mchtId"]').val() + '/tab_virAccount'); //PAGE 이동
							}
						});
					}
					// 수수료 유형이 정률일 때
				} else if($('select[name="feeType"]').val()=='1'){
					if(Number($('.distRate').val()) > Number($('.agencyRate').val())){
						var error1Str = '<button class="close" data-close="alert"></button>';
						error1Str += "대행사 수수료율이 에이전시 수수료율 보다 큽니다. 확인해 주시기 바랍니다.";
						error1.html(error1Str);
						error1.show();
						App.scrollTo(error1, -200);
						$('.agencyRate').focus();
					} else if(Number($('.agencyRate').val()) > Number($('.rate').val())){
						var error1Str = '<button class="close" data-close="alert"></button>';
						error1Str += "에이전시 수수료율이 가맹점 수수료율 보다 큽니다. 확인해 주시기 바랍니다.";
						error1.html(error1Str);
						error1.show();
						App.scrollTo(error1, -200);
						$('.rate').focus();
					} else {
						error1.hide();
						bootbox.confirm("입력하신 정보로 가상계좌를 설정하시겠습니까?", function(result) {
							if (result) {
								ajaxFormSubmit(form, '/mcht/view/'+ $('input[name="mchtId"]').val() + '/tab_virAccount'); //PAGE 이동
							}
						});
					}
				} else {
					if(Number($('.distRate').val()) > Number($('.agencyRate').val())){
						var error1Str = '<button class="close" data-close="alert"></button>';
						error1Str += "대행사 수수료율이 에이전시 수수료율 보다 큽니다. 확인해 주시기 바랍니다.";
						error1.html(error1Str);
						error1.show();
						App.scrollTo(error1, -200);
						$('.agencyRate').focus();
					} else if(Number($('.agencyRate').val()) > Number($('.rate').val())){
						var error1Str = '<button class="close" data-close="alert"></button>';
						error1Str += "에이전시 수수료율이 가맹점 수수료율 보다 큽니다. 확인해 주시기 바랍니다.";
						error1.html(error1Str);
						error1.show();
						App.scrollTo(error1, -200);
						$('.rate').focus();
					} else if(Number($('.fee').val()) < Number($('.distFee').val()) + Number($('.agencyFee').val()) + Number($('.salesFee').val())) {
						var error1Str = '<button class="close" data-close="alert"></button>';
						error1Str += "대행사, 에이전시, 지사 수수료의 합이 가맹점 수수료 보다 큽니다. 확인해 주시기 바랍니다.";
						error1.html(error1Str);
						error1.show();
						App.scrollTo(error1, -200);
						$('.fee').focus();
					} else {
						error1.hide();
						bootbox.confirm("입력하신 정보로 가상계좌를 설정하시겠습니까?", function(result) {
							if (result) {
								ajaxFormSubmit(form, '/mcht/view/'+ $('input[name="mchtId"]').val() + '/tab_virAccount'); //PAGE 이동
							}
						});
					}
				}
			}
		});

		$('[name="issueType"]').change(function (e) {
			onIssueType(this);
		});

		function onIssueType(t) {
			var val = $(t).val();
			var expireSet = $('[name="expireSet"]');
			if (val == '임시') {
				
				if( expireSet.val() == '365' ){
					expireSet.val(3);
				}
				
				expireSet.removeAttr('disabled');
			} else {
				expireSet.val(365);
				expireSet.attr('disabled', 'disabled');
			}
		}

		function addComma(data) {
			return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		}

		$('#distType').on('change', function() {
			var selected = $(this).find("option:selected").val();
			if(selected == '') return false;
		  	// $.ajax({
		  	// 	url:'/mcht/distRate/get/'+selected,
		  	// 	method: 'GET',
		  	// 	dataType: 'json',
		  	// 	success: function(data){
			//
	  		// 		$('input[name="distFee"]').val(addComma(String(data.fee)).replace(/[^0-9]/g,""));
	  		// 		$('input[name="distRate"]').val((data.rate*100).toFixed(3));
		  	// 		// $('.distSettleType').html(data.settleType);
	  		// 	}
		  	// })
		});

		$('#agencyType').on('change', function() {
			var selected = $(this).find("option:selected").val();
			if(selected == '') return false;
		  	// $.ajax({
		  	// 	url:'/mcht/agencyRate/get/'+selected,
		  	// 	method: 'GET',
		  	// 	dataType: 'json',
		  	// 	success: function(data){
	  		// 		$('input[name="agencyFee"]').val(addComma(String(data.fee)).replace(/[^0-9]/g,""));
	  		// 		$('input[name="agencyRate"]').val((data.rate*100).toFixed(3));
		  	// 		$('.agencySettleType').html(data.settleType);
	  		// 	}
		  	// })
		});

		$('#salesType').on('change', function() {
			var selected = $(this).find("option:selected").val();
			if(selected == '') return false;
		  	// $.ajax({
		  	// 	url:'/mcht/salesRate/get/'+selected,
		  	// 	method: 'GET',
		  	// 	dataType: 'json',
		  	// 	success: function(data){
	  		// 		$('input[name="salesFee"]').val(addComma(String(data.fee)).replace(/[^0-9]/g,""));
	  		// 		$('input[name="salesRate"]').val((data.rate*100).toFixed(3));
		  	// 		// $('.salesSettleType').html(data.settleType);
	  		// 	}
		  	// })
		});
		
		$('#payOutType').on('change', function(){
			var selected = $(this).find("option:selected").val();
			if(selected == '') return false;
			if(selected != '가맹점'){
				$('select[name="payInStatus"]').val('중지');
				$('input[name="distPayInFee"]').val('0');
				$('input[name="agencyPayInFee"]').val('0');
				$('input[name="salesPayInFee"]').val('0');
				
				$('select[name="payInStatus"]').attr("disabled", true);
				$('input[name="distPayInFee"]').attr("disabled", true);
				$('input[name="agencyPayInFee"]').attr("disabled", true);
				$('input[name="salesPayInFee"]').attr("disabled", true);
			} else {
				$('select[name="payInStatus"]').attr("disabled", false);
				$('input[name="distPayInFee"]').attr("disabled", false);
				$('input[name="agencyPayInFee"]').attr("disabled", false);
				$('input[name="salesPayInFee"]').attr("disabled", false);
			}
		});
		
		// 수수료 유형
		$('select[name="feeType"]').on('change', function() {
			var selected = $(this).find("option:selected").val();
			
			if(selected == '0'){
				$('.rate').val('0.0');
				$('#rateDiv').hide();
				$('.distRate').val('0');
				$('#distRateDiv').hide();
				$('.agencyRate').val('0');
				$('#agencyRateDiv').hide();
				$('.salesRate').val('0');
				$('#salesRateDiv').hide();
				
				$('#feeDiv').show();
				$('#distFeeDiv').show();
				$('#agencyFeeDiv').show();
				$('#salesFeeDiv').show();
				
				$('.noneDiv').hide();
			} else if(selected == '1'){
				$('.fee').val('0');
				$('#feeDiv').hide();
				$('.distFee').val('0');
				$('#distFeeDiv').hide();
				$('.agencyFee').val('0');
				$('#agencyFeeDiv').hide();
				$('.salesFee').val('0');
				$('#salesFeeDiv').hide();
				
				$('#rateDiv').show();
				$('#distRateDiv').show();
				$('#agencyRateDiv').show();
				$('#salesRateDiv').show();
				
				$('.noneDiv').hide();
			} else {
				$('#feeDiv').show();
				$('#distFeeDiv').show();
				$('#agencyFeeDiv').show();
				$('#salesFeeDiv').show();
				$('#rateDiv').show();
				$('#distRateDiv').show();
				$('#agencyRateDiv').show();
				$('#salesRateDiv').show();
				
				$('.noneDiv').show();
			}
		});
		
		$('select[name="settleType"]').on('change', function() {
			var selected = $(this).find("option:selected").val();
			
			if(selected == 'A+0' || selected == 'A+2' || selected == 'A+1' || selected == 'D+0'){
				$('#payOutFeeDiv').show();
			} else {
				$('#payOutType').val('가맹점');
				$('#payInStatus').val('중지');
				$('.transferInterval').val('10');
				$('.payOutFee').val('0');
				$('.distPayInFee').val('0');
				$('.agencyPayInFee').val('0');
				$('.salesPayInFee').val('0');
				$('#payOutFeeDiv').hide();
			}
		});
		
		$('select[name="authType"]').on('change', function() {
			var selected = $(this).find("option:selected").val();
			
			if(selected == '0'){
				$('#depositLimitCntDiv').hide();
				$('#respiteCntDiv').hide();
				$('#stateInitCntDiv').hide();
				$('#authBankCdDiv').hide();
			}else if(selected == '1'){
				$('#depositLimitCntDiv').show();
				$('#respiteCntDiv').show();
				$('#stateInitCntDiv').hide();
				$('#authBankCdDiv').hide();
			}else if(selected == '2'){
				$('#depositLimitCntDiv').hide();
				$('#respiteCntDiv').hide();
				$('#stateInitCntDiv').show();
				$('#authBankCdDiv').show();
			}
		});
		
		$(document).ready(function(){
			
			onIssueType($('[name="issueType"]'));
			
			var selected = $('#distType').find("option:selected").val();
			
			if(selected === undefined || selected == "" || selected === null ){
				$('#distType').empty();
				$('#distType').append('<option value="" id="distSelect" selected>--- 선택 (기본 M+10) ---</option>');
				$('#distType').selectpicker('refresh');
			} else {
				// $.ajax({
			  	// 	url:'/mcht/distRate/get/'+selected,
			  	// 	method: 'GET',
			  	// 	dataType: 'json',
			  	// 	success: function(data){
			  	// 		$('.distSettleType').html(data.settleType);
		  		// 	}
			  	// });
			}
			
			
			var selected = $('#agencyType').find("option:selected").val();
			if(selected === undefined || selected == "" || selected === null ){
				$('#agencyType').empty();
				$('#agencyType').append('<option value="" id="agencySelect" selected>--- 선택 (기본 M+10) ---</option>');
				$('#agencyType').selectpicker('refresh');
			} else {
				// $.ajax({
			  	// 	url:'/mcht/agencyRate/get/'+selected,
			  	// 	method: 'GET',
			  	// 	dataType: 'json',
			  	// 	success: function(data){
			  	// 		$('.agencySettleType').html(data.settleType);
		  		// 	}
			  	// });
			}
			
			var selected = $('#salesType').find("option:selected").val();
			if(selected === undefined || selected == "" || selected === null ){
				$('#salesType').empty();
				$('#salesType').append('<option value="" id="salesSelect" selected>--- 선택 (기본 M+10) ---</option>');
				$('#salesType').selectpicker('refresh');
			} else {
				// $.ajax({
			  	// 	url:'/mcht/salesRate/get/'+selected,
			  	// 	method: 'GET',
			  	// 	dataType: 'json',
			  	// 	success: function(data){
			  	// 		$('.salesSettleType').html(data.settleType);
		  		// 	}
			  	// });
			}
			
			var selected = $('select[name="feeType"]').find("option:selected").val();
			if(selected == '0'){
				$('#rateDiv').hide();
				$('#distRateDiv').hide();
				$('#agencyRateDiv').hide();
				$('#salesRateDiv').hide();
				$('.noneDiv').hide();
			} else if(selected == '1'){
				$('#feeDiv').hide();
				$('#distFeeDiv').hide();
				$('#agencyFeeDiv').hide();
				$('#salesFeeDiv').hide();
				$('.noneDiv').hide();
			}
			
			var selected = $('#payOutType').find("option:selected").val();
			if(selected == '') return false;
			if(selected != '가맹점'){
				$('select[name="payInStatus"]').val('중지');
				$('input[name="distPayInFee"]').val('0');
				$('input[name="agencyPayInFee"]').val('0');
				$('input[name="salesPayInFee"]').val('0');
				
				$('select[name="payInStatus"]').attr("disabled", true);
				$('input[name="distPayInFee"]').attr("disabled", true);
				$('input[name="agencyPayInFee"]').attr("disabled", true);
				$('input[name="salesPayInFee"]').attr("disabled", true);
			} else {
				$('select[name="payInStatus"]').attr("disabled", false);
				$('input[name="distPayInFee"]').attr("disabled", false);
				$('input[name="agencyPayInFee"]').attr("disabled", false);
				$('input[name="salesPayInFee"]').attr("disabled", false);
			}
			
			var selected = $('select[name="settleType"]').find("option:selected").val();
			if(selected == 'A+0' || selected == 'A+2' || selected == 'A+1' || selected == 'D+0'){
				$('#payOutFeeDiv').show();
			} else {
				$('#payOutFeeDiv').hide();
			}
			
			var selected = $('select[name="authType"]').find("option:selected").val();
			
			if(selected == '0'){
				$('#depositLimitCntDiv').hide();
				$('#respiteCntDiv').hide();
				$('#stateInitCntDiv').hide();
				$('#authBankCdDiv').hide();
			}else if(selected == '1'){
				$('#depositLimitCntDiv').show();
				$('#respiteCntDiv').show();
				$('#stateInitCntDiv').hide();
				$('#authBankCdDiv').hide();
			}else if(selected == '2'){
				$('#depositLimitCntDiv').hide();
				$('#respiteCntDiv').hide();
				$('#stateInitCntDiv').show();
				$('#authBankCdDiv').show();
			}
		});
	
		$('#nav-mcht').addClass('active');
	</script>
	<!-- END FORM JAVASCRIPT -->
	<!-- 모달 생성을 위한 베이스 -->
	<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>