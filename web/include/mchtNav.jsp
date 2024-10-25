<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!-- END SIDEBAR TOGGLER BUTTON -->
<li class="nav-item start" id="nav-board">
    <a href="javascript:redirectToMain();" class="nav-link nav-toggle"><i class="icon-home"></i>
                <span class="title">Dashboard</span></a>
</li>

<%--가맹점 웹결제 사용시에만 웹결제 메뉴 선택 가능하게--%>
<c:if test="${CP_SESSION.webPay != '' && CP_SESSION.mchtWebPay == '사용'}">

<li class="nav-item start" id="nav-board">
 	<a href="/wtmn/form.jsp" class="nav-link nav-toggle"><i class="icon-home"></i>
		<span class="title">웹결제</span></a>
</li>
</c:if>
<!-- 거래 관리 시작 -->
<li class="nav-item start " id="nav-trx">
    <a href="javascript:;" class="nav-link nav-toggle"><i class="icon-check"></i>
				<span class="title">거래 관리</span><span class="arrow"></span></a>
    <ul class="sub-menu">
        <li class="nav-item start ">
            <a href="/trx/cap/form.jsp" class="nav-link ">
                    <span class="title">매입현황조회</span></a>
        </li>
        <li class="nav-item">
                <a href="javascript:;" class="nav-link nav-toggle">
                	        <span class="title">가상계좌 관리</span>
                	        <span class="arrow"></span>
                	    </a>
                <ul class="sub-menu" style="display: none;">
                    <li class="nav-item ">
                        <a href="/vact/dtl/form.jsp" class="nav-link ">발행내역 조회</a>
                    </li>
                    <li class="nav-item ">
                        <a href="/vact/trx/form.jsp" class="nav-link ">거래내역 조회</a>
                    </li>
					<li class="nav-item ">
						<a href="/vact/auth/form.jsp" class="nav-link ">인증수수료 조회</a>
					</li>
					<li class="nav-item ">
						<a href="/vact/noti/form.jsp" class="nav-link ">노티내역 조회</a>
					</li>
                </ul>
		</li>
		<li class="nav-item">
			<a href="javascript:;" class="nav-link nav-toggle">
				<span class="title">정기결제 관리</span>
				<span class="arrow"></span>
			</a>
			<ul class="sub-menu" style="display: none;">
				<li class="nav-item ">
					<a href="/rebill/reg/form.jsp" class="nav-link ">등록내역 조회</a>
				</li>
				<li class="nav-item ">
					<a href="/rebill/trx/form.jsp" class="nav-link ">거래내역 조회</a>
				</li>
				<li class="nav-item ">
					<a href="/rebill/err/form.jsp" class="nav-link ">오류내역 조회</a>
				</li>
			</ul>
		</li>
		<li class="nav-item start ">
            <a href="/trx/status/form.jsp" class="nav-link ">
                <span class="title">리스크 관리</span></a>
        </li>
        <li class="nav-item start ">
            <a href="/trx/pay/form.jsp" class="nav-link ">
                      <span class="title">승인내역조회</span></a>
        </li>
        <li class="nav-item start ">
            <a href="/trx/err/form.jsp" class="nav-link ">
                      <span class="title">승인실패조회</span></a>
        </li>
         <li class="nav-item">
             <a href="javascript:;" class="nav-link nav-toggle">
             	        <span class="title">지급대행 관리</span> 
             	        <span class="arrow"></span>
             	    </a>
             <ul class="sub-menu" style="display: none;">
                 <li class="nav-item ">
                     <a href="/trx/pisp/form.jsp" class="nav-link ">거래내역 조회</a>
                 </li>
                 <li class="nav-item ">  
                     <a href="/trx/pisp/fcs/form.jsp" class="nav-link ">인증내역 조회</a>
                 </li>
             </ul>
         </li> 
    </ul>
</li>
<!-- 매출 관리 시작 -->
<li class="nav-item start " id="nav-sales">
		<a href="javascript:;" class="nav-link nav-toggle"><i class="icon-paper-clip"></i>
									<span class="title">매출 및 정산 내역</span><span class="arrow"></span></a>
		<ul class="sub-menu">
				<li class="nav-item start ">
						<a href="/settle/mcht/form.jsp" class="nav-link ">
													<span class="title">신용카드 정산 조회</span></a>
				</li>
				<li class="nav-item start ">
						<a href="/vactSettle/mcht/form.jsp" class="nav-link ">
													<span class="title">가상계좌 정산 조회</span></a>
				</li>
				<li class="nav-item start ">
						<a href="/sales/daily/form.jsp" class="nav-link ">
													<span class="title">기간별 매출관리</span></a>
				</li>
				<li class="nav-item start ">
						<a href="/sales/monthly/form.jsp" class="nav-link ">
							<span class="title">월별 매출관리</span></a>
				</li>
		</ul>
</li>
<li class="nav-item start " id="nav-charge">
    <a href="javascript:;" class="nav-link nav-toggle"><i class="icon-check"></i>
		<span class="title">충전 정산</span><span class="arrow"></span></a>
    <ul class="sub-menu">
        <li class="nav-item start ">
        	<a href="/chargeSettle/form.jsp" class="nav-link ">
            <span class="title">거래내역 조회</span></a>
    	</li>
    	<li class="nav-item start ">
	        	<a href="/chargeSettle/err/form.jsp" class="nav-link ">
	            <span class="title">오류내역 조회</span></a>
	   </li>
    </ul>
</li>
<c:if test="${CP_SESSION.loanSettleStatus == 'Y'}">
	<li class="nav-item start " id="nav-loan">
	    <a href="javascript:;" class="nav-link nav-toggle"><i class="icon-notebook"></i>
			<span class="title">대출 정산</span><span class="arrow"></span></a>
			
		<ul class="sub-menu">
	        <li class="nav-item start ">
	        	<a href="/loanSettle/form.jsp" class="nav-link ">
	            <span class="title">대출정보조회</span></a>
	    	</li>
	        <li class="nav-item start ">
	        	<a href="/loanSettle/status/form.jsp" class="nav-link ">
	            <span class="title">대출현황조회</span></a>
	    	</li>
	    	<li class="nav-item start ">
	        	<a href="/loanSettle/settleList/form.jsp" class="nav-link ">
	            <span class="title">대출정산내역조회</span></a>
	    	</li>
	    </ul>
	</li>
</c:if> 
<c:if test="${CP_SESSION.aggregator == 'Y'}">
<li class="nav-item start " id="nav-aggregator">
		<a href="javascript:;" class="nav-link nav-toggle"><i class="icon-basket"></i>
				<span class="title">대표가맹점 관리</span><span class="arrow"></span></a>
		<ul class="sub-menu">
				<li class="nav-item start ">
						<a href="/mcht/tmn/form.jsp" class="nav-link ">
						<span class="title">터미널 조회</span></a>
				</li>
				<li class="nav-item start ">
						<a href="/sales/aggregator/form.jsp" class="nav-link ">
						<span class="title">매출 조회</span></a>
				</li>
				<li class="nav-item start ">
						<a href="/settle/aggregator/form.jsp" class="nav-link ">
							<span class="title">정산 조회</span></a>
				</li>
				<li class="nav-item start ">
						<a href="/settle/payoutsub/form.jsp" class="nav-link ">
							<span class="title">정산 지급 데이터 생성</span></a>
				</li>
		</ul>
</li>

</c:if>
<li class="nav-item start " id="nav-customer">
    <a href="javascript:;" class="nav-link nav-toggle"><i class="icon-bell"></i>
		<span class="title">고객센터</span><span class="arrow"></span></a>
    <ul class="sub-menu">
        <li class="nav-item start ">
            <a href="/system/notice/form" class="nav-link ">
			<span class="title">공지 사항</span></a>
        </li>
        <li class="nav-item start ">
            <a href="/system/faq/view" class="nav-link ">
			<span class="title">자주 묻는 질문</span></a>
        </li>
        <li class="nav-item start ">
            <a href="/system/task/view" class="nav-link ">
			<span class="title">작업 관리</span></a>
        </li>
    </ul>
</li>
<c:if test="${(CP_SESSION.showOthTrns eq 'Y')}">
    <li class="nav-item start " id="nav-oth">
        <a href="javascript:;" class="nav-link nav-toggle"><i class="icon-check"></i>
			<span class="title">기타거래</span><span class="arrow"></span></a>
        <ul class="sub-menu">
            <li class="nav-item start ">
            	<a href="/othTrns/van/form.jsp" class="nav-link ">
                <span class="title">VAN거래 조회</span></a>
        	</li>
        </ul>
    </li>
</c:if>

