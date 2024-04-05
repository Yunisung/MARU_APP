<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<!DOCTYPE html>
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>

</head>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">×</button>
		<h4>세금계산서 발행</h4>
	</div>
	<div class="modal-body form">
		<form role="form">
			<div class="form-body" style="padding: 5px 35px;">
				<div class="row">
					<div class="form-group">
						<label>출력 기준월</label>
						<div class="input-icon">
							<i class="fa fa-calendar font-blue"></i><input id="monthlydatepicker" class="datepicker form-control" name="trxDay">
						</div>
					</div>
					<div class="form-group">
						<label class="mt-checkbox mt-checkbox-outline">
							선정산 가맹점
              <input type="checkbox" value="1" name="distType">
            	<span></span>
            </label>
					</div>
					<div class="form-group">
						<label>사업자명</label> <input class="form-control" name="compName"
							value="㈜사업자">
					</div>
					<div class="form-group">
						<label>사업자 번호</label>
							<input class="form-control" name="identity" value="">
					</div>
					<div class="form-group">
						<label>대표자 성명</label>
							<input class="form-control" name="ceoName1" value="">
					</div>
					<div class="form-group">
						<label>기본 주소</label>
							<input class="form-control" name="addr1" value="">
					</div>
					<div class="form-group">
						<label>상세 주소</label>
							<input class="form-control" name="addr2" value="">
					</div>
					<div class="form-group">
						<label>업종</label>
							<input class="form-control" name="bizCategory" value="서비스">
					</div>
					<div class="form-group">
						<label>업태</label>
							<input class="form-control" name="bizType" value="전자금융업외">
					</div>
					<div class="form-group">
						<label>이메일</label>
							<input class="form-control" name="email" value="">
					</div>
				</div>
			</div>
		</form>
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn btn-sm">Close</button>
		<button id="tax-download" class="btn btn-success" type="button">엑셀 출력</button>
	</div>
	<script>
		var today = new Date();
		today.setMonth(today.getMonth() -1);
	  var nowMonth = today.getFullYear() + '-' + ((today.getMonth()+1) > 9 ? (today.getMonth()+1) : '0' + (today.getMonth()+1));
	  $("#monthlydatepicker").val(nowMonth);
		$("#monthlydatepicker").datepicker({
			format : "yyyy-mm",
			viewMode : "months",
			minViewMode : "months",
			locale : 'ko'
		});
	</script>
</body>
</html>