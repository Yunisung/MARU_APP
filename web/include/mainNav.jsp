<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!-- END SIDEBAR TOGGLER BUTTON -->
<li class="nav-item start" id="nav-board">
    <a href="javascript:redirectToMain();" class="nav-link nav-toggle"><i class="icon-home"></i>
                <span class="title">Dashboard</span></a>
</li>
<!-- 가맹점 관리 시작 -->
<li class="nav-item start " id="nav-mcht">
    <a href="javascript:;" class="nav-link nav-toggle"><i class="icon-basket"></i>
							<span class="title">가맹점 관리</span><span class="arrow"></span></a>
    <ul class="sub-menu">
        <li class="nav-item start ">
            <a href="/mcht/form" class="nav-link ">
                    <span class="title">가맹점 조회</span></a>
        </li>
        <li class="nav-item start ">
            <a href="/mcht/tmn/form.jsp" class="nav-link ">
                      <span class="title">터미널 조회</span></a>
        </li>
        <c:if test="${(CP_SESSION.grade =='본사' || CP_SESSION.grade =='대행사' || CP_SESSION.grade =='에이전시' || CP_SESSION.grade =='지사') && CP_SESSION.role != '일반'}">
            <li class="nav-item start ">
                <a href="/mcht/add" class="nav-link ">
                      <span class="title">가맹점 등록</span></a>
            </li>
        </c:if>
        <c:if test="${CP_SESSION.grade =='본사' && CP_SESSION.role != '일반'}">
            <li class="nav-item start ">
                <a href="/mcht/feeTemplate/form.jsp" class="nav-link ">
                      <span class="title">가맹점 수수료 템플릿</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/mcht/interTemplate/form.jsp" class="nav-link ">
                      <span class="title">무이자 수수료 템플릿</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/mcht/van/form.jsp" class="nav-link ">
                      <span class="title">VAN ID 조회</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/mcht/van/fee/form.jsp" class="nav-link ">
                    <span class="title">VAN 수수료 조회</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/mcht/van/interFee/form.jsp" class="nav-link ">
                    <span class="title">VAN 상점부담 무이자 수수료 조회</span></a>
            </li>
        </c:if>
        <li class="nav-item start ">
            <a href="/mcht/nontran/form" class="nav-link ">
                    <span class="title">미실적 가맹점 조회</span></a>
        </li>
	        <c:if test="${CP_SESSION.grade =='본사' && CP_SESSION.role != '일반'}">
	        <li class="nav-item start ">
	        	<a href="/mcht/accntSearch/form" class="nav-link ">
	                <span class="title">비대면계좌개설 조회</span></a>
	        </li>
	        <li class="nav-item start ">
	        	<a href="/mcht/noti/form.jsp" class="nav-link ">
	                <span class="title">노티 등록</span></a>
	        </li>
        </c:if>
    </ul>
</li>
<!-- 거래 관리 시작 -->
<li class="nav-item start " id="nav-trx">
    <a href="javascript:;" class="nav-link nav-toggle"><i class="icon-check"></i>
				<span class="title">거래 관리</span><span class="arrow"></span></a>
    <ul class="sub-menu">
        <li class="nav-item start ">
            <a href="/trx/cap/form.jsp" class="nav-link ">
                    <span class="title">매입현황조회</span></a>
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
       	        <span class="title">가상계좌 관리</span>
       	        <span class="arrow"></span>
       	    </a>
            <ul class="sub-menu" style="display: none;">
                <li class="nav-item ">
                    <a href="/vact/dtl/form.jsp" class="nav-link ">발행내역 조회</a>
                </li>
                <li class="nav-item ">
                    <a href="/vact/trx/form.jsp" class="nav-link ">거래내역조회</a>
                </li>

                <c:if test="${CP_SESSION.grade =='본사' && CP_SESSION.role != '일반'}">
                    <li class="nav-item ">
                        <a href="/vact/auth/form.jsp" class="nav-link ">인증수수료조회</a>
                    </li>
	                <li class="nav-item ">
		                <a href="/vact/blackList/add.jsp" class="nav-link ">출금계좌 블랙리스트 등록</a>
		            </li>
		            <li class="nav-item ">
		                <a href="/vact/blackList/form.jsp" class="nav-link ">출금계좌 블랙리스트 조회</a>
		            </li>
		        </c:if>
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
         <c:if test="${CP_SESSION.grade eq '본사'}">
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
            <li class="nav-item start ">
                <a href="/trx/status/form.jsp" class="nav-link ">
                      <span class="title">리스크 관리</span></a>
            </li>
            <li class="nav-item">
                <a href="javascript:;" class="nav-link nav-toggle">
                	        <span class="title">취소내역 관리</span>
                	        <span class="arrow"></span>
                	    </a>
                <ul class="sub-menu" style="display: none;">
                    <li class="nav-item ">
                        <a href="/trx/rfd/form.jsp" class="nav-link ">승인취소내역조회</a>
                    </li>
                    <li class="nav-item ">
                        <a href="/trx/admin_cancel/form.jsp" class="nav-link ">관리자 취소내역조회</a>
                    </li>
                </ul>
            </li>
            <li class="nav-item start ">
                <a href="/trx/io/form.jsp" class="nav-link ">
                      <span class="title">거래 IO 조회</span></a>
            </li>
        </c:if>
        <c:if test="${CP_SESSION.grade ne '본사'}">
            <li class="nav-item start ">
                <a href="/trx/status/form.jsp" class="nav-link ">
                      <span class="title">리스크 관리</span></a>
            </li>
            <li class="nav-item ">
                <a href="/trx/rfd/form.jsp" class="nav-link ">승인취소내역조회</a>
            </li>
        </c:if>
        <c:if test="${CP_SESSION.grade =='본사'}">
            <li class="nav-item start ">
                <a href="/trx/wh/form.jsp" class="nav-link ">
                      <span class="title">미반영 거래</span></a>
            </li>
        </c:if>
        <c:if test="${CP_SESSION.grade =='본사' && (CP_SESSION.role =='마스터' || CP_SESSION.role =='관리자')}">
            <li class="nav-item start ">
                <a href="/trx/capdel/form" class="nav-link ">
                      <span class="title">매입 삭제</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/trxoper/load/form.jsp" class="nav-link ">
                      <span class="title">거래생성 조회</span></a>
            </li>
        </c:if>
    </ul>
</li>
<!-- 매출 관리 시작 -->
<li class="nav-item start " id="nav-sales">
    <a href="javascript:;" class="nav-link nav-toggle"><i class="icon-paper-clip"></i>
							<span class="title">매출 관리</span><span class="arrow"></span></a>
    <ul class="sub-menu">
        <c:if test="${CP_SESSION.grade == '본사'}">
           <%-- <li class="nav-item start ">
                <a href="/sales/cap/mcht/form.jsp" class="nav-link ">
                    	<span class="title">매입일 기준 매출조회</span></a>
            </li> --%>
            <li class="nav-item start ">
            	<a href="/sales/dailysByStlDay/form.jsp" class="nav-link ">
                <span class="title">일마감 - 정산거래기준</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/sales/dailysByCapDay/form.jsp" class="nav-link ">
                <span class="title">일마감 - 승인거래기준</span></a>
            </li>
            <%--
            <li class="nav-item start ">
                <a href="/sales/dailys/form.jsp" class="nav-link ">
                    	<span class="title">일마감</span></a>
            </li>
             --%>
            <li class="nav-item start ">
                <a href="/sales/settle/calendar.jsp" class="nav-link ">
                    	<span class="title">정산일정</span></a>
            </li>
            <%--
            <li class="nav-item start ">
                <a href="/sales/settle/form.jsp" class="nav-link ">
                    	<span class="title">지급정산내역</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/sales/collect/form.jsp" class="nav-link ">
                    	<span class="title">입금정산내역</span></a>
            </li>
            --%>
            <li class="nav-item start ">
                <a href="/sales/collect/mcht/form.jsp" class="nav-link ">
                    	<span class="title">가맹점별 입금정산내역</span></a>
            </li>
        </c:if>
        <%--
        <li class="nav-item start ">
            <a href="/sales/daily/form.jsp" class="nav-link ">
                   	<span class="title">기간별 매출관리</span></a>
        </li>
         --%>
        <li class="nav-item start ">
            <a href="/sales/monthly/form.jsp" class="nav-link ">
                   	<span class="title">월별 매출관리</span></a>
        </li>
        <%--
        <li class="nav-item start ">
            <a href="/sales/issuer/form.jsp" class="nav-link ">
                   	<span class="title">매입사별조회</span></a>
        </li>
         --%>
        <c:if test="${CP_SESSION.grade == '본사' || CP_SESSION.grade == '대행사'}">
            <li class="nav-item start ">
                <a href="/sales/dist/form.jsp" class="nav-link ">
                	   	<span class="title">대행사 매출관리</span></a>
            </li>
        </c:if>
        <c:if test="${CP_SESSION.grade == '본사' || CP_SESSION.grade == '대행사' || CP_SESSION.grade == '에이전시'}">
            <li class="nav-item start ">
                <a href="/sales/agency/form.jsp" class="nav-link ">
                	   	<span class="title">에이전시 매출관리</span></a>
            </li>
        </c:if>
        <c:if test="${CP_SESSION.grade == '본사' || CP_SESSION.grade == '대행사' || CP_SESSION.grade == '에이전시' || CP_SESSION.grade == '지사'}">
            <li class="nav-item start ">
                <a href="/sales/sales/form.jsp" class="nav-link ">
                	   	<span class="title">지사 매출관리</span></a>
            </li>
        </c:if>
        <li class="nav-item start ">
            <a href="/sales/mcht/form.jsp" class="nav-link ">
                   	<span class="title">가맹점 매출관리</span></a>
        </li>
        <li class="nav-item start ">
            <a href="/sales/mchtMonthly/form.jsp" class="nav-link ">
                   	<span class="title">월별 가맹점 매출관리</span></a>
        </li>
         <c:if test="${CP_SESSION.grade == '대행사' || CP_SESSION.grade == '에이전시'}">
         	<li class="nav-item start ">
                <a href="/settle/mcht/form.jsp" class="nav-link ">
        	      <span class="title">가맹점 정산 조회</span></a>
            </li>
         </c:if>
    </ul>
</li>

<c:if test="${CP_SESSION.grade == '본사'}">
    <!-- 가맹점 정산 시작 -->
    <li class="nav-item start " id="nav-settle-mcht">
        <a href="javascript:;" class="nav-link nav-toggle" id="nav-sales"><i class="icon-notebook"></i>
						<span class="title">가맹점 정산</span><span class="arrow"></span></a>
        <ul class="sub-menu">
            <li class="nav-item start ">
                <a href="/settle/make/form.jsp" class="nav-link ">
        	      <span class="title">가맹점 정산 생성</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/settle/mcht/form.jsp" class="nav-link ">
        	      <span class="title">가맹점 정산 조회</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/settle/ddct/form" class="nav-link ">
        	      <span class="title">가맹점 차감정산 조회</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/settle/aggregator/form.jsp" class="nav-link ">
        	      <span class="title">대표가맹점 정산 조회</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/settle/tmn/form.jsp" class="nav-link ">
        	      <span class="title">터미널상점 정산 조회</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/settle/hold/form.jsp" class="nav-link ">
        	      <span class="title">지급 보류 조회</span></a>
            </li>
            <li class="nav-item ">  
                <a href="/settle/pisp/form" class="nav-link ">지급대행 정산</a>
            </li>
        </ul>
    </li>
</c:if>   
    <!-- 대행사 정산 시작 -->
    <li class="nav-item start " id="nav-settle">
        <a href="javascript:;" class="nav-link nav-toggle" id="nav-sales"><i class="icon-calculator"></i>
						<span class="title">대행사 정산</span><span class="arrow"></span></a>
        <ul class="sub-menu">
           <%-- <li class="nav-item start ">
                <a href="/settle/payout/form.jsp" class="nav-link ">
        	      <span class="title">은행 지급</span></a>
            </li> --%>
            <c:if test="${CP_SESSION.grade == '본사' || CP_SESSION.grade == '대행사'}">
            <li class="nav-item start ">
                <a href="/settle/dist/form.jsp" class="nav-link ">
        	      <span class="title">대행사 정산 조회</span></a>
            </li>
            </c:if>
            <c:if test="${CP_SESSION.grade == '본사' || CP_SESSION.grade == '에이전시'}">
            <li class="nav-item start ">
                <a href="/settle/agency/form.jsp" class="nav-link ">
        	      <span class="title">에이전시 정산 조회</span></a>
            </li>
            </c:if>
            <c:if test="${CP_SESSION.grade == '본사' || CP_SESSION.grade == '지사'}">
            <li class="nav-item start ">
                <a href="/settle/sales/form.jsp" class="nav-link ">
        	      <span class="title">지사 정산 조회</span></a>
            </li>
            </c:if>
            
        </ul>
    </li>
<c:if test="${CP_SESSION.grade == '본사'}">   
    <!-- 입금 정산 시작 -->
    <li class="nav-item start " id="nav-collect">
        <a href="javascript:;" class="nav-link nav-toggle" id="nav-sales"><i class="icon-wallet"></i>
						<span class="title">입금 정산</span><span class="arrow"></span></a>
        <ul class="sub-menu">
            <li class="nav-item start ">
                <a href="/collect/temp/form" class="nav-link ">
        	      <span class="title">입금 예정내역 조회</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/collect/excel/form" class="nav-link ">
        	      <span class="title">입금정산 내역 업로드</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/collect/collect/form" class="nav-link ">
        	      <span class="title">입금정산</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/collect/mcht/form.jsp" class="nav-link ">
        	      <span class="title">가맹점별 입금정산 내역</span></a>
            </li>
        </ul>
    </li>
    <!-- 영중소 차액 정산 시작 -->
    <li class="nav-item start " id="nav-diff">
        <a href="javascript:;" class="nav-link nav-toggle" id="nav-sales"><i class="icon-credit-card"></i>
						<span class="title">영중소 차액 정산</span><span class="arrow"></span></a>
        <ul class="sub-menu">
            <li class="nav-item start ">
                <a href="/diff/temp/form" class="nav-link ">
        	      <span class="title">차액 예정내역 조회</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/diff/excel/form" class="nav-link ">
        	      <span class="title">차액 입금 내역 업로드</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/diff/collect/form" class="nav-link ">
        	      <span class="title">차액정산</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/diff/mcht/form.jsp" class="nav-link ">
        	      <span class="title">가맹점별 차액정산 내역</span></a>
            </li>
            <!-- <li class="nav-item start ">
	            <a href="/diff/kcp/form.jsp" class="nav-link ">
	    	      <span class="title">KCP 차액정산 실패내역</span></a>
    	    </li> -->
        </ul>
    </li>
 </c:if> 
    <!-- 실시산 정산 시작 -->
    <li class="nav-item start " id="nav-realtime">
        <a href="javascript:;" class="nav-link nav-toggle" id="nav-sales"><i class="icon-wallet"></i>
						<span class="title">실시간 정산</span><span class="arrow"></span></a>
        <ul class="sub-menu">
        	<c:if test="${CP_SESSION.grade == '본사'}">
	            <li class="nav-item start ">
	                <a href="/realtimeSettle/realtime/form.jsp" class="nav-link ">
	        	      <span class="title">즉시 정산내역 조회</span></a>
	            </li>
	        </c:if>
            <li class="nav-item start ">
	            <a href="/realtimeSettle/auto/form.jsp" class="nav-link ">
	    	      <span class="title">자동 정산내역 조회</span></a>
	        </li>
        </ul>
    </li> 
<c:if test="${(CP_SESSION.grade =='본사' || CP_SESSION.grade =='대행사' || CP_SESSION.grade =='에이전시')}">
    <!-- 가상계좌 정산 시작 -->
    <li class="nav-item start " id="nav-vact-settle">
        <a href="javascript:;" class="nav-link nav-toggle" id="nav-sales"><i class="icon-calculator"></i>
						<span class="title">가상계좌 정산</span><span class="arrow"></span></a>
        <ul class="sub-menu">
            <c:if test="${CP_SESSION.grade == '본사'}">
                <li class="nav-item start ">
                    <a href="/vactSettle/mcht/form.jsp" class="nav-link ">
                      <span class="title">가맹점 정산 조회</span></a>
               </li>
                <li class="nav-item start ">
                <a href="/vactSettle/dist/form.jsp" class="nav-link ">
        	      <span class="title">대행사 정산 조회</span></a>
                </li>
                <li class="nav-item start ">
                    <a href="/vactSettle/agency/form.jsp" class="nav-link ">
                      <span class="title">에이전시 정산 조회</span></a>
                </li>
                <li class="nav-item start ">
                    <a href="/vactSettle/sales/form.jsp" class="nav-link ">
                      <span class="title">지사 정산 조회</span></a>
                </li>
            </c:if>
            <c:if test="${CP_SESSION.grade == '대행사'}">
                <li class="nav-item start ">
                    <a href="/vactSettle/dist/form.jsp" class="nav-link ">
                        <span class="title">대행사 정산 조회</span></a>
                </li>
            </c:if>
            <c:if test="${CP_SESSION.grade == '에이전시'}">
                <li class="nav-item start ">
                    <a href="/vactSettle/agency/form.jsp" class="nav-link ">
                        <span class="title">에이전시 정산 조회</span></a>
                </li>
            </c:if>
        </ul>
    </li>
</c:if>
<c:if test="${CP_SESSION.grade == '본사'}">
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
	        <li class="nav-item start ">
	        	<a href="/chargeSettle/auto/form.jsp" class="nav-link ">
	            <span class="title">자동정산 내역 조회</span></a>
	    	</li>
	    </ul>
	</li>
</c:if>
<c:if test="${CP_SESSION.grade eq '본사' || ((CP_SESSION.grade eq '대행사' || CP_SESSION.grade eq '에이전시' || CP_SESSION.grade eq '지사') && CP_SESSION.loanSettleStatus == 'Y')}">
	<li class="nav-item start " id="nav-loan">
	    <a href="javascript:;" class="nav-link nav-toggle"><i class="icon-notebook"></i>
			<span class="title">대출 정산</span><span class="arrow"></span></a>
			
		<ul class="sub-menu">
	        <li class="nav-item start ">
	        	<a href="/loanSettle/form.jsp" class="nav-link ">
	            <span class="title">대출정보조회</span></a>
	    	</li>
	    	<li class="nav-item start ">
	        	<a href="/loanSettle/prepay/form" class="nav-link ">
	            <span class="title">중도상환등록</span></a>
	    	</li>
	        <li class="nav-item start ">
	        	<a href="/loanSettle/status/form.jsp" class="nav-link ">
	            <span class="title">대출현황조회</span></a>
	    	</li>
	    	<li class="nav-item start ">
	        	<a href="/loanSettle/settleList/form.jsp" class="nav-link ">
	            <span class="title">대출정산내역조회</span></a>
	    	</li>
	    	<li class="nav-item start ">
	        	<a href="/loanSettle/settle/form.jsp" class="nav-link ">
	            <span class="title">대출정산실행</span></a>
	    	</li>
	    </ul>
	</li>
</c:if> 
<c:if test="${CP_SESSION.grade == '본사'}">
    <!-- 현금영수증 시작 -->
    <li class="nav-item start " id="nav-cash-receipt">
        <a href="javascript:;" class="nav-link nav-toggle" id="nav-cash"><i class="icon-calculator"></i>
						<span class="title">현금영수증</span><span class="arrow"></span></a>
        <ul class="sub-menu">
           <li class="nav-item start ">
                <a href="/cashReceipt/add.jsp" class="nav-link ">
        	    <span class="title">현금영수증 등록</span></a>
           </li>
           <li class="nav-item start ">
                <a href="/cashReceipt/form.jsp" class="nav-link ">
        	    <span class="title">현금영수증 조회</span></a>
           </li>
        </ul>
    </li>
    <!-- 휴대폰 정산 시작 -->
    <li class="nav-item start " id="nav-phone-settle">
        <a href="javascript:;" class="nav-link nav-toggle" id="nav-phone"><i class="icon-calculator"></i>
						<span class="title">휴대폰 결제</span><span class="arrow"></span></a>
        <ul class="sub-menu">
		    <li class="nav-item start ">
		        <a href="/phone/form.jsp" class="nav-link ">
			      <span class="title">거래내역 조회</span></a>
		    </li>
            <li class="nav-item start ">
                <a href="/phone/settle/form.jsp" class="nav-link ">
        	      <span class="title">정산내역 조회</span></a>
            </li>
            <li class="nav-item start ">
	            <a href="/phone/excel/form.jsp" class="nav-link ">
	    	      <span class="title">수납정산 내역 업로드</span></a>
	        </li>
	        <li class="nav-item start ">
	            <a href="/phone/sunab/form.jsp" class="nav-link ">
	    	      <span class="title">수납정산 조회</span></a>
	        </li>
        </ul>
    </li>
</c:if>
<!-- 이폼 전자계약서 시작 -->
<c:if test="${CP_SESSION.grade == '본사' || (CP_SESSION.eformStatus eq '사용')}">
	<li class="nav-item start " id="nav-eform">
        <a href="javascript:;" class="nav-link nav-toggle" id="nav-eform"><i class="icon-notebook"></i>
						<span class="title">전자계약서</span><span class="arrow"></span></a>
        <ul class="sub-menu">
         <c:if test="${CP_SESSION.grade == '본사'}">
        	<li class="nav-item start ">
                <a href="/eform/auth/form.jsp" class="nav-link ">
        	      <span class="title">전자계약서 권한 조회</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/eform/form.jsp" class="nav-link ">
        	      <span class="title">전자계약서 전체 내역 조회</span></a>
            </li>
         </c:if>
           	<li class="nav-item start ">
                <a href="/eform/main" class="nav-link ">
        	      <span class="title">전자계약서 발송</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/eform/member/form.jsp" class="nav-link ">
        	      <span class="title">전자계약서 내역 조회</span></a>
            </li>
        </ul>
    </li>
</c:if>
<!-- 이폼 전자계약서 끝 -->
<!-- 멤버 관리 -->
<c:if test="${CP_SESSION.grade != '가맹점'}">
    <li class="nav-item start " id="nav-member">
        <a href="javascript:;" class="nav-link nav-toggle"><i class="icon-users"></i>
					<span class="title">멤버 관리</span><span class="arrow"></span></a>
        <ul class="sub-menu">
            <li class="nav-item">
                <a href="javascript:;" class="nav-link nav-toggle">
                  <span class="title">로그인 ID관리</span>
                  <span class="arrow"></span>
              </a>
                <ul class="sub-menu" style="display: none;">
                    <li class="nav-item ">
                        <a href="/member/user/form" class="nav-link ">ID 조회</a>
                    </li>
                    <c:if test="${CP_SESSION.grade ne '지사' &&CP_SESSION.role != '일반'}">
                        <li class="nav-item ">
                            <a href="/member/user/add" class="nav-link ">
                                <c:if test="${CP_SESSION.grade == '본사' || CP_SESSION.grade == '대행사'}">
                                    대행사/에이전시 등록
                                </c:if>
                                <c:if test="${CP_SESSION.grade == '에이전시'}">
                                    지사 ID등록
                                </c:if>
                            </a>
                        </li>
                    </c:if>
                </ul>
            </li>
            <c:if test="${CP_SESSION.grade == '본사' && CP_SESSION.role != '일반'}">
                <li class="nav-item start ">
                    <a href="/member/user/add/admin" class="nav-link ">
        	    	<span class="title">임직원 등록</span></a>
                </li>
            </c:if>
            <c:if test="${(CP_SESSION.grade == '본사' || CP_SESSION.grade == '대행사' || CP_SESSION.grade == '에이전시' || CP_SESSION.grade == '지사') && CP_SESSION.role != '일반'}">
                <li class="nav-item start ">
                    <a href="/member/user/add/mcht" class="nav-link ">
        	    	<span class="title">가맹점 등록</span></a>
                </li>
            </c:if>

            <c:if test="${CP_SESSION.grade == '본사'}">
                <li class="nav-item">
                    <a href="javascript:;" class="nav-link nav-toggle">
															<span class="title">대행사관리</span>
															<span class="arrow"></span>
													</a>
                    <ul class="sub-menu" style="display: none;">
                        <li class="nav-item ">
                            <a href="/member/dist/form" class="nav-link ">대행사조회</a>
                        </li>
                        <c:if test="${CP_SESSION.role != '일반'}">
                            <li class="nav-item ">
                                <a href="/member/dist/add" class="nav-link ">대행사등록</a>
                            </li>
                        </c:if>
                    </ul>
                </li>
            </c:if>

            <c:if test="${CP_SESSION.grade == '본사' || CP_SESSION.grade == '대행사' }">
                <li class="nav-item">
                    <a href="javascript:;" class="nav-link nav-toggle">
															<span class="title">에이전시관리</span>
															<span class="arrow"></span>
													</a>
                    <ul class="sub-menu" style="display: none;">
                        <li class="nav-item ">
                            <a href="/member/agency/form" class="nav-link ">에이전시조회</a>
                        </li>
                        <c:if test="${CP_SESSION.role != '일반'}">
                            <li class="nav-item ">
                                <a href="/member/agency/add" class="nav-link ">에이전시등록</a>
                            </li>
                        </c:if>
                    </ul>
                </li>
            </c:if>
            <c:if test="${CP_SESSION.grade == '본사' || CP_SESSION.grade == '대행사' || CP_SESSION.grade == '에이전시'}">
                <li class="nav-item">
                    <a href="javascript:;" class="nav-link nav-toggle">
															<span class="title">지사관리</span>
															<span class="arrow"></span>
													</a>
                    <ul class="sub-menu" style="display: none;">
                        <li class="nav-item ">
                            <a href="/member/sales/form" class="nav-link ">지사조회</a>
                        </li>
                        <c:if test="${CP_SESSION.role != '일반'}">
                            <li class="nav-item ">
                                <a href="/member/sales/add" class="nav-link ">지사등록</a>
                            </li>
                        </c:if>
                    </ul>
                </li>
            </c:if>
            <c:if test="${CP_SESSION.grade == '본사'}">
            	<li class="nav-item start ">
                    <a href="/member/rate/form.jsp" class="nav-link ">
        	    	<span class="title">수수료 변경 예약</span></a>
                </li>
                <li class="nav-item start ">
                    <a href="/member/vactRate/form.jsp" class="nav-link ">
                        <span class="title">가상계좌 수수료 변경 예약</span></a>
                </li>
            </c:if>
        </ul>
    </li>
</c:if>
<c:if test="${CP_SESSION.grade == '본사' && CP_SESSION.role !='일반'}">
    <li class="nav-item start " id="nav-firm">
        <a href="javascript:;" class="nav-link nav-toggle"><i class="icon-target"></i>
			<span class="title">펌뱅킹 관리</span><span class="arrow"></span></a>
        <ul class="sub-menu">
            <li class="nav-item start ">
                <a href="/firm/trx/form" class="nav-link ">
				<span class="title">출금현황조회</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/firm/master/form.jsp" class="nav-link ">
				<span class="title"> 뱅킹 마스터</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/firm/err/form" class="nav-link ">
				<span class="title">타행불능명세</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/firm/action/form.jsp" class="nav-link ">
				<span class="title">개시,잔액조회</span></a>
            </li>
            <c:if test="${CP_SESSION.grade == '본사' && (CP_SESSION.role =='마스터')}">
	            <li class="nav-item start ">
		            <a href="/firm/transfer/form.jsp" class="nav-link ">
					<span class="title">모계좌이체</span></a>
		        </li>
		    </c:if>
        </ul>
    </li>
</c:if>
<!-- TMS -->
<c:if test="${CP_SESSION.grade == '본사'}">
    <li class="nav-item start " id="nav-tms">
        <a href="javascript:;" class="nav-link nav-toggle"><i class="icon-credit-card"></i>
			<span class="title">TMS</span><span class="arrow"></span></a>
        <ul class="sub-menu">
            <li class="nav-item start ">
                <a href="/tms/io/form.jsp" class="nav-link ">
				<span class="title">거래요청내역 조회</span></a>
            </li>
            <!-- <li class="nav-item start ">
                <a href="/tms/key/form.jsp" class="nav-link ">
				<span class="title"> Key 관리</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/tms/app/form.jsp" class="nav-link ">
				<span class="title">APP 개시 현황</span></a>
            </li> -->
        </ul>
    </li>
</c:if> 
<!-- 시스템 관리 -->
<c:if test="${(CP_SESSION.grade == '본사') && CP_SESSION.role != '일반' }">
    <li class="nav-item start " id="nav-system">
        <a href="javascript:;" class="nav-link nav-toggle"><i class="icon-briefcase"></i>
			<span class="title">시스템 관리</span><span class="arrow"></span></a>
        <ul class="sub-menu">
            <li class="nav-item start ">
	            <a href="/system/send/add" class="nav-link ">
				<span class="title">공지 사항 전송</span></a>
	        </li>
	        <li class="nav-item start ">
	            <a href="/system/send/form.jsp" class="nav-link ">
				<span class="title">공지 사항 전송 내역</span></a>
	        </li>
            <li class="nav-item start ">
                <a href="/system/faq/form" class="nav-link ">
				<span class="title"> FAQ 관리</span></a>
            </li>
            <li class="nav-item start ">
                <a href="/system/iqr/form.jsp" class="nav-link ">
				<span class="title">상담이력 관리</span></a>
            </li>
            <%-- <li class="nav-item start ">
                <a href="/system/risk/form.js" class="nav-link ">
				<span class="title">리스크 관리</span></a>
            </li> --%>
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
        <%-- 
        <c:if test="${CP_SESSION.grade == '본사' }">
            <li class="nav-item start ">
                <a href="javascript:popup(400, 400, -580, 280);" class="nav-link ">
				<span class="title">SMS</span></a>
            </li>
            <script>
                function popup(width, height, px, py) {
                    var sw = screen.availWidth;
                    var sh = screen.availHeight;
                    var set = 'top=' + py + ',left=' + px;
                    set += ',width=' + width + ',height=' + height + ',toolbar=0,resizable=0,status=0,scrollbars=yes,location=0,menubar=0,directories=0,copyhistory=no';
                    window.open('/system/sms.jsp', 'sms', set);
                }
            </script>
        </c:if>
        --%>
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
