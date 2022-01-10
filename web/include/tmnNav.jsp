<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<li class="nav-item start" id="nav-board">
    <a href="javascript:redirectToMain();" class="nav-link nav-toggle"><i class="icon-home"></i>
                <span class="title">Dashboard</span></a>
</li>
<li class="nav-item start" id="nav-board">
    <a href="/mcht/form" class="nav-link nav-toggle"><i class="icon-mcht"></i>
    <span class="title">가맹점 조회</span></a>
</li>
<li class="nav-item start " id="nav-customer">
    <a href="javascript:;" class="nav-link nav-toggle"><i class="icon-bell"></i>
		<span class="title">고객센터</span><span class="arrow"></span></a>
    <ul class="sub-menu">
        <li class="nav-item start ">
            <a href="/system/notice/form" class="nav-link "><i class="icon-note"></i>
			<span class="title">공지 사항</span></a>
        </li>
        <li class="nav-item start ">
            <a href="/system/faq/view" class="nav-link "><i class="icon-note"></i>
			<span class="title">자주 묻는 질문</span></a>
        </li>
    </ul>
</li>





<%-- <ul class="nav navbar-nav">
	<li aria-haspopup="true" class="menu-dropdown classic-menu-dropdown" id="nav-board"><a href="/">
			Dashboard
			<span class="arrow"></span>
		</a></li>
	<li aria-haspopup="true" class="menu-dropdown classic-menu-dropdown" id="nav-mcht nav-link"><a href="/mcht/form">
			가맹점 조회
			<span class="arrow"></span>
		</a></li>
	<li aria-haspopup="true" class="menu-dropdown classic-menu-dropdown" id="nav-customer"><a href="javascript:;">
			고객센터
			<span class="arrow"></span>
		</a>
		<ul class="dropdown-menu pull-left">
			<li aria-haspopup="true" class=" "><a href="/system/notice/form" class="nav-link" title="">
					<i class="icon-bell"></i>공지 사항
				</a></li>
			<li aria-haspopup="true" class=" "><a href="/system/faq/view" class="nav-link">
					<i class="icon-magnifier"></i>자주 묻는 질문
				</a></li>
		</ul></li>
</ul> --%>
