<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!-- END SIDEBAR TOGGLER BUTTON -->
<c:if test="${CP_SESSION.webPay != ''}">
<li class="nav-item start" id="nav-board">
    <a href="/wtmn/form.jsp" class="nav-link nav-toggle"><i class="icon-home"></i>
                <span class="title">웹결제</span></a>
</li>
</c:if>
<li class="nav-item start" id="nav-trx">
    <a href="/subMcht/trx/form.jsp" class="nav-link nav-toggle"><i class="icon-home"></i>
        <span class="title">매입 현황</span></a>
</li>
<li class="nav-item start" id="nav-sales">
    <a href="/subMcht/sales/form.jsp" class="nav-link nav-toggle"><i class="icon-home"></i>
        <span class="title">매출 조회</span></a>
</li>
<li class="nav-item start" id="nav-settle">
    <a href="/subMcht/settle/form.jsp" class="nav-link nav-toggle"><i class="icon-home"></i>
        <span class="title">정산 조회</span></a>
</li>
<%-- <li class="nav-item start">
     <a href="/vact/dtl/form.jsp" class="nav-link nav-toggle"><i class="icon-home"></i>
     <span class="title">가상계좌 관리-발행내역 조회</span></a>
 </li>
 <li class="nav-item start">
     <a href="/vact/trx/form.jsp" class="nav-link nav-toggle"><i class="icon-home"></i>
     <span class="title">가상계좌 관리-거래내역조회</span></a>
 </li>--%>
<li class="nav-item start" id="nav-my">
    <a href="/subMcht/myprofile/${CP_SESSION.parentId }" class="nav-link nav-toggle"><i class="icon-home"></i>
        <span class="title">내 정보</span></a>
</li>
<li class="nav-item start" id="nav-logout">
    <a href="/login/out" class="nav-link nav-toggle"><i class="icon-logout"></i>
        <span class="title">Log Out</span></a>
</li>