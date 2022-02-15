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
									<li><span>현금영수증</span><i class="fa fa-circle"></i></li>
									<li><span>현금영수증 등록/취소 요청</span></li>
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
										<i class="fa fa-reorder"></i> <span class="caption-title">
											현금영수증 등록/취소 </span>
									</div>
								</div>
								<div class="portlet-body form">
									<form class="form-horizontal form-bordered" id="writeFrm" name="writeFrm" method="POST">
										<input type="hidden" name="regInfo" value="ADMIN">
										<div class="form-body row">
										<div class="form-group col-sm-12 form-subtitle">
							                <label><i class="fa fa-reorder"></i>요청 정보 입력</label>
							            </div>
											<input type="hidden" name="regInfo" value="ADMIN">
											<div class="form-group col-sm-12">
												<input type="radio" name="assort" value="0" checked>
													<label for="add">현금영수증 등록</label>
												<input type="radio" name="assort" value="1">
													<label for="cancel">현금영수증 취소</label>
											</div>
										<div id="insertOpt">
											<div class="form-group col-sm-12">
												<label class="control-label col-sm-2"></label>
													<div class="col-sm-6" id="insertInfo"></div>
											</div>
										</div>
										<div id="insert">	
											<div class="form-group col-sm-6 mchtId">
												<label class="control-label col-sm-4 req-label">가맹점 아이디</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" name="mchtId" maxlength="10">
												</div>
											</div>
											<div class="form-group col-sm-6 transNo">
												<label class="control-label col-sm-4 req-label">주문번호</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" name="transNo" maxlength="50">
												</div>
											</div>
											<div class="form-group col-sm-6 trDt">
												<label class="control-label col-sm-4 req-label">거래일시</label> 
												<div class="col-sm-6">
													<input type="text" class="form-control" maxlength="14" name="trDt" placeholder="yyyyMMddhh24miss">
												</div>
											</div>
											<div class="form-group col-sm-6 bizRegNo">
												<label class="control-label col-sm-4 req-label">가맹점 사업자번호</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" maxlength="10" name="bizRegNo">
												</div>
											</div>
											<div class="form-group col-sm-6 cashType">
												<label class="control-label col-sm-4 req-label">현금영수증 결제수단</label>
												<select name="cashType" id="cashType" class="selectpicker col-sm-6">
													<option>-- 선택 --</option>
													<option value="0">가상계좌</option>
													<option value="1">기타/단 건</option>
												</select>
											</div>
											<div class="form-group col-sm-6">
												<label class="control-label col-sm-4">가상계좌 발급번호</label>
												<div class="col-sm-6 required">
													<input type="text" class="form-control" name="trxId" maxlength="20">
													<div id="trxIdCheck"></div>
												</div>
											</div>
											<div class="form-group col-sm-6 purpose">
												<label class="control-label col-sm-4 req-label">용도 구분</label>
												<select name="purpose" id="purpose" class="selectpicker col-sm-6">
													<option value="" selected>-- 선택 --</option>
													<option value="0">소득공제</option>
													<option value="1">지출증빙</option>
												</select>
											</div>
											<div class="form-group col-sm-6 taxYn">
												<label class="control-label col-sm-4">과세구분</label>
												<select name="taxYn" id="taxYn" class="selectpicker col-sm-6">
													<option value="" selected>-- 선택 --</option>
													<option value="N">과세</option>
													<option value="Y">면세</option>
													<option value="G">복합과세</option>
												</select>
											</div>
											<div class="form-group col-sm-6 amt">
												<label class="control-label col-sm-4 req-label">공급가액</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" maxlength="10" name="amt">
													<div id="amtSpan">과세구분 미지정시 과세금액 입력</div>
												</div>
											</div>
											<div class="form-group col-sm-6 vat">
												<label class="control-label col-sm-4 req-label">부가세</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" maxlength="10" name="vat">
													<div id="vatSpan">부가세 입력</div>
												</div>
											</div>
											<div class="form-group col-sm-6 identityGb">
												<label class="control-label col-sm-4 req-label">현금영수증 등록번호 구분</label>
												<select name="identityGb" id="identityGb" class="selectpicker col-sm-6">
													<option value="" selected>-- 선택 --</option>
													<option value="1">국세청 현금영수증 카드</option>
													<option value="3">사업자번호</option>
													<option value="4">휴대전화번호</option>
												</select>
											</div>
											<div class="form-group col-sm-6 identity" id="identity">
												<label class="control-label col-sm-4 req-label">현금영수증 등록번호</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" maxlength="18" id="identity"
														name="identity" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');">
												</div>
											</div>
											<div class="form-group col-sm-6 ordNm">
												<label class="control-label col-sm-4">주문자명</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" name="ordNm" maxlength="30">
												</div>
											</div>
											<div class="form-group col-sm-6 svcAmt">
												<label class="control-label col-sm-4">봉사료</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" maxlength="10" name="svcAmt">
												</div>
											</div>
											<div class="form-group col-sm-6 deductionType">
												<label class="control-label col-sm-4">추가공제 구분</label>
												<select name="deductionType" id="deductionType" class="selectpicker col-sm-6">
													<option value="" selected>-- 선택 --</option>
													<option value="Y">대중교통</option>
													<option value="C">도서, 공연비</option>
												</select>
											</div>
											<div class="form-group col-sm-6 cashKey">
												<label class="control-label col-sm-4 req-label">온라인 결제 Key 입력</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" name="cashKey">
												</div>
											</div>
										</div>
										<div id="cancel">
											<div class="form-group col-sm-6 mchtId">
												<label class="control-label col-sm-4 req-label">가맹점 아이디</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" name="mchtId" maxlength="10">
												</div>
											</div>
											<div class="form-group col-sm-6 transNo">
												<label class="control-label col-sm-4 req-label">새로 채번된 취소 주문번호</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" name="transNo" maxlength="50">
												</div>
											</div>
											<div class="form-group col-sm-6 orgAuthNo">
												<label class="control-label col-sm-4 req-label">원거래 승인번호</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" maxlength="9" name="orgAuthNo">
												</div>
											</div>
											<div class="form-group col-sm-6 orgTrDt">
												<label class="control-label col-sm-4 req-label">원거래 거래일시</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" maxlength="14" placeholder="yyyyMMddhh24miss" name="orgTrDt">
												</div>
											</div>
											<div class="form-group col-sm-6 cashKey">
												<label class="control-label col-sm-4 req-label">온라인 결제 Key 입력</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" name="cashKey">
												</div>
											</div>
											<div class="form-group col-sm-6 bizRegNo">
												<label class="control-label col-sm-4">가맹점 사업자번호</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" maxlength="10" name="bizRegNo">
												</div>
											</div>
											<div class="form-group col-sm-6 purpose">
												<label class="control-label col-sm-4">용도 구분</label>
												<div class="col-sm-6">
													<select name="purpose" id="purpose" class="form-control">
														<option value="" selected>-- 선택 --</option>
														<option value="0">소득공제</option>
														<option value="1">지출증빙</option>
													</select>
												</div>
											</div>
											<div class="form-group col-sm-6 taxYn">
												<label class="control-label col-sm-4">과세구분</label>
												<div class="col-sm-6">
													<select name="taxYn" id="taxYn" class="form-control">
														<option value="" selected>-- 선택 --</option>
														<option value="N">과세</option>
														<option value="Y">면세</option>
														<option value="G">복합과세</option>
													</select>
												</div>
											</div>
											<div class="form-group col-sm-6 amt">
												<label class="control-label col-sm-4">공급가액</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" maxlength="13" name="amt">
												</div>
											</div>
											<div class="form-group col-sm-6 vat">
												<label class="control-label col-sm-4">부가세</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" maxlength="13" name="vat">
												</div>
											</div>
											<div class="form-group col-sm-6 identityGb">
												<label class="control-label col-sm-4">현금영수증 등록번호 구분</label>
												<div class="col-sm-6">
													<select name="identityGb" id="identityGb" class="form-control">
														<option value="" selected>-- 선택 --</option>
														<option value="1">국세청 현금영수증 카드</option>
														<option value="3">사업자번호</option>
														<option value="4">휴대전화번호</option>
													</select>
												</div>
											</div>
											<div class="form-group col-sm-6 identity" id="identity">
												<label class="control-label col-sm-4">현금영수증 등록번호</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" maxlength="18" id="identity"
														name="identity" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');">
												</div>
											</div>
											<div class="form-group col-sm-6 ordNm">
												<label class="control-label col-sm-4">주문자명</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" name="ordNm" maxlength="30">
												</div>
											</div>
											<div class="form-group col-sm-6 svcAmt">
												<label class="control-label col-sm-4">봉사료</label>
												<div class="col-sm-6">
													<input type="text" class="form-control" maxlength="13" name="svcAmt">
												</div>
											</div>
										</div>
									</div>	
										<div class="alert alert-danger display-hide"></div>
										<div class="form-actions right">
											<div class="">
												<button type="button" id="cashReceipt" class="btn green btn-sm loading-btn"
													data-loading-text="Loading...">
													<i class="fa fa-search"></i>&nbsp;Submit
												</button>
											</div>
										</div>
									</form>
								</div>
								<!-- END ADD FORM TABLE-->
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<c:import url="/include/footer.jsp" />
	</div>
	<c:import url="/include/javascript.jsp" />
	
	<script>
	var copyCa;
	var copyIn;
	copyCa = $("div#cancel").detach();
	copyIn = $("div#insert").detach();
	$("#insertOpt").append(copyIn);
	document.getElementById("insertInfo").innerHTML = '자진발급번호 요청시 소득공제, 휴대전화번호(0100001234) 지정';
	document.getElementById("trxIdCheck").innerHTML = '';
	var radioValue = "0";
	$("input[name=assort]:radio").change(function(){
		radioValue = this.value;
		if(radioValue == "0"){
			$("#insertOpt").append(copyIn);
			copyCa = $("div#cancel").detach();
			document.getElementById("insertInfo").innerHTML = '자진발급번호 요청시 소득공제, 휴대전화번호(0100001234) 지정';
		}else if(radioValue == "1"){
			$("#insertOpt").append(copyCa);
			copyIn = $("div#insert").detach();
			document.getElementById("insertInfo").innerHTML = '`현금영수증 등록번호` 미입력시 원거래 승인일시·승인번호로 전체 취소';
		}
	});
	
	$("select[name=taxYn]").change(function(){
		var taxValue = this.value;
		document.getElementById("amtSpan").style = 'color:red';
		document.getElementById("vatSpan").style = 'color:red';
		if(taxValue == "N"){
			$("#amtSpan").empty();
			$("#vatSpan").empty();
			var form = document.getElementById("amtSpan");
			form.innerHTML += "<span>과세금액 입력</span>";
			var form = document.getElementById("vatSpan");
			form.innerHTML += "<span>부가세 입력</span>";
		}else if(taxValue == "Y"){
			$("#amtSpan").empty();
			$("#vatSpan").empty();
			var form = document.getElementById("amtSpan");
			form.innerHTML += "<span>면세금액 입력</span>";
			var form = document.getElementById("vatSpan");
			form.innerHTML += "<span>'0'으로 입력</span>";
		}else if(taxValue == "G"){
			$("#amtSpan").empty();
			$("#vatSpan").empty();
			var form = document.getElementById("amtSpan");
			form.innerHTML += "<span>(과세+면세)금액 입력</span>";
			var form = document.getElementById("vatSpan");
			form.innerHTML += "<span>과세금액의 부가세 입력</span>";
		}else{
			$("#amtSpan").empty();
			$("#vatSpan").empty();
			var form = document.getElementById("amtSpan");
			form.innerHTML += "<span>과세금액 입력</span>";
			var form = document.getElementById("vatSpan");
			form.innerHTML += "<span>부가세 입력</span>";
		}
	});
	
	$("select[name=cashType]").change(function(){
		var cashValue = this.value;
		if(cashValue == "0"){
			var form = document.getElementById("trxIdCheck");
			document.getElementById("trxIdCheck").innerHTML = '가상계좌 issueId 입력';
			document.getElementById("trxIdCheck").style = 'color:red';
		}else{
			$("#trxIdCheck").hide();
		}
	});

	$("#cashReceipt").on("click", function(){
		$('input[name="amt"]').val(String($('input[name="amt"]').val()).replace(/,/g, ''));
		$('input[name="vat"]').val(String($('input[name="vat"]').val()).replace(/,/g, ''));
		$('input[name="svcAmt"]').val(String($('input[name="svcAmt"]').val()).replace(/,/g, ''));
        send();
    });
	
	$('input[name="amt"]').keyup(function(){
		$('input[name="amt"]').val(addComma(String($('input[name="amt"]').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
   	});
	
	$('input[name="vat"]').keyup(function(){
		$('input[name="vat"]').val(addComma(String($('input[name="vat"]').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
   	});
	
	$('input[name="svcAmt"]').keyup(function(){
		$('input[name="svcAmt"]').val(addComma(String($('input[name="svcAmt"]').val()).replace(/,/g, '').replace(/[^(-?)0-9]/g,"")));
   	});

	function addComma(data) {
	    return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}
	
	function send(){
		//Validation
		var ckMchtId = $("input[name='mchtId']").val();
		var ckTransNo = $("input[name='transNo']").val();
		var ckTrDt = $("input[name='trDt']").val();
		var ckAmt = $("input[name='amt']").val();
		var ckVat = $("input[name='vat']").val();
		var ckCashType = $("select[name=cashType]").val();
		var ckTrxId = $("input[name='trxId']").val();
		var ckPurpose = $("select[name=purpose]").val();
		var ckIdentityGb = $("select[name=identityGb]").val();
		var ckIdentity = $("input[name='identity']").val();
		var ckBizRegNo = $("input[name='bizRegNo']").val();
		var ckCashKey = $("input[name='cashKey']").val();
		var ckOrgAuthNo = $("input[name='orgAuthNo']").val();
		var ckOrgTrDt = $("input[name='orgTrDt']").val();
		
		if(radioValue == "0"){
			if (ckMchtId == "" || ckTransNo == "" || ckAmt == "" || ckVat == "" || ckIdentity == ""
				|| ckCashType == "" || ckPurpose == "" || ckIdentityGb == "" || ckCashKey == "") {
				alert("승인 요청 필수값 입력해주세요.");
				return false;
			}else if(ckTrDt.length != 14){
				alert("거래일시 14자리 (형식 : yyyyMMddhh24miss) 맞춰주세요.");
				document.writeFrm.trDt.focus();
				return false;
			}else if(ckCashType == 0 && ckTrxId == ""){
				alert("가상계좌 발급번호를 입력해주세요.");
				document.writeFrm.trxId.focus();
				return false;
			}else if(ckBizRegNo == "" || ckBizRegNo.length != 10){
				alert("사업자 번호 10자리 입력해주세요.");
				document.writeFrm.bizRegNo.focus();
				return false;
			}
		}else if(radioValue == "1"){
			if (ckOrgAuthNo == "" || ckMchtId == "" || ckTransNo == ""  || ckCashKey == "" || ckOrgTrDt == ""){
				alert("취소 요청 필수값 입력해주세요.");
				return false;
			}else if(ckOrgTrDt.length != 14){
				alert("거래일시 14자리 (형식 : yyyyMMddhh24miss) 맞춰주세요.");
				document.writeFrm.orgTrDt.focus();
				return false;
			}
		}
		//Validation
		
		if(confirm("현금영수증을 등록하시겠습니까?") == true){
			$.ajax({
				type : "POST",
				url : "/cashReceipt/insert",
				data : $('#writeFrm').serialize(),
				beforeSend : function(xhr){
					xhr.setRequestHeader("Authorization", $("input[name='cashKey']").val());
				},
				success : function(data) {
					console.log("CASH RECEIPT API CALLED ..." + JSON.stringify(data));
					  if(data.resultCd == '0000'){
			    		alert("현금영수증 등록 완료되었습니다.");  
			    	  }else if(!data.resultCd == '0000'){
			    		alert("현금영수증 등록 실패되었습니다. 입력 값을 확인해주세요.");
			    	  }
					$("input:radio[name='assort']").removeAttr("checked");
					$("input:radio[name='assort']:radio[value='0']").attr('checked', true);
					$('#cashType').val('default').selectpicker("refresh");
					$('#purpose').val('default').selectpicker("refresh");
					$('#identityGb').val('default').selectpicker("refresh");
					$('#deductionType').val('default').selectpicker("refresh");
					$('#taxYn').val('default').selectpicker("refresh");
					$("#writeFrm")[0].reset();
				},
				error : function(request,status,error) {
					alert("현금영수증 등록 실패되었습니다. 입력 값을 확인해주세요.");
					console.log("CASH RECEIPT FAILED ...");
					console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				}
			});
		}else{
			return false;
		}
	}
	
	$('#nav-cash-receipt').addClass('active');
	</script>
	<!-- END FORM JAVASCRIPT -->
</body>
</html>