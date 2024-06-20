<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>영수증 조회</title>
	<style>
		#receipt_error1 {
			padding: 40% 0;
		}
		#receipt_error2 {
			border: 10px solid #444444; 
			background-color: #444444;
		}
		h1 {
			text-align: center;
			color: #ffffff;
		}
		#receipt_error3 {
			text-align: center;
		}
	</style>
</head>

<body>
	<c:choose>
		<c:when test = "${fn:startsWith(DATAMAP.van, 'KSPAY')}">
			<c:if test = "${!fn:startsWith(DATAMAP.vanTrxId, 'TX')}">
				<script>
					document.location="https://pgims.ksnet.co.kr/pg_infoc/src/bill/new_credit_view.jsp?tr_no=${DATAMAP.vanTrxId}";
				</script>
			</c:if>
		</c:when>
		
		<c:when test = "${fn:startsWith(DATAMAP.van, 'GALAXIA')}">
			<script>
					document.location="https://cpadmin.billgate.net/billgate/common/authCardReceipt.jsp?mid=${DATAMAP.vanId}&transNm=${DATAMAP.vanTrxId}&currTp=0000";
			</script>
		</c:when>
		
		
		<c:when test = "${fn:startsWith(DATAMAP.van, 'ALLAT')}">
			<script>
				document.location="http://www.allatpay.com/servlet/AllatBizPop/member/pop_card_receipt.jsp?${DATAMAP.allatParam}";
			</script>
		</c:when>

		<c:when test="${fn:startsWith(DATAMAP.van, 'WELCOME')}">
			<c:choose>
				<c:when test="${(DATAMAP.van eq 'WELCOME') || (DATAMAP.van eq 'WELCOME영중소')}">
					<script>
						document.location="https://wbiz.paywelcome.co.kr/mCmReceipt_head.jsp?noTid=${DATAMAP.vanTrxId}&noMethod=1";
					</script>
				</c:when>
				<c:when test="${DATAMAP.van eq 'WELCOMESUB'}">
					<script>
						document.location="https://payapi.welcomepayments.co.kr/api/receipt/print?tid=${DATAMAP.vanTrxId}&hash_value=${DATAMAP.hash_value}";
					</script>
				</c:when>
			</c:choose>
		</c:when>

		
		<c:otherwise>
			<c:if test = "${DATAMAP eq null || DATAMAP eq ''}">
				<div id="receipt_error1">
		    		<div id="receipt_error2">
		    			<h1>Page Not Found</h1>
		    		</div>
			    		<div id="receipt_error3">
			    		<p>해당 거래에 관한 정보를 찾을 수 없습니다.<br>파라미터 값을 확인해 주십시오.</p>
		    		</div>
	    		</div>
			</c:if>
		</c:otherwise>
	</c:choose>
</body>

</html>
