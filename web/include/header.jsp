<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript">
</script>
<!-- BEGIN HEADER -->
<div class="page-header navbar navbar-fixed-top">
    <!-- BEGIN HEADER INNER -->
    <div class="page-header-inner">
        <!-- BEGIN LOGO -->
        <div class="page-logo">
            <a href="/">
              <img src="/assets/pages/img/logo_top.png" alt="logo" class="logo-default" style="margin-top: 10px;height: 25px;"/> </a>
            <div class="menu-toggler sidebar-toggler">
                <span></span>
            </div>
        </div>
        <!-- END LOGO -->
        <!-- BEGIN RESPONSIVE MENU TOGGLER -->
        <a href="javascript:;" class="menu-toggler responsive-toggler" data-toggle="collapse" data-target=".navbar-collapse">
            <span></span>
        </a>
        <!-- END RESPONSIVE MENU TOGGLER -->
        <!-- BEGIN TOP NAVIGATION MENU -->
        <div class="top-menu">
            <ul class="nav navbar-nav pull-right">
                <li class="dropdown dropdown-extended dropdown-notification" id="header_notification_bar">
                    <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
											<i class="icon-bell"></i>
											<span class="badge badge-default" id="alert-count"></span>
									</a>
                    <ul class="dropdown-menu">
                        <li class="external">
                            <h3>
                                <span class="bold">공지사항 및 알림</span>
                            </h3>
                            <li>
                                <ul class="dropdown-menu-list scroller" id="alert-menu" style="height: 100px;" data-handle-color="#637283">
                                    <c:if test="${CP_SESSION.danalFailCnt > 0 }">
                                        <li><a href="javascript:;">
																					<span class="label label-icon label-success">
																						<i class="icon-speech"></i>
																					</span>
																					&nbsp;&nbsp; 거래 실패 내역이 있습니다.
																				</a></li>
                                    </c:if>
                                    <c:if test="${CP_SESSION.pwYn == '아니오' }">
                                        <li><a href="javascript:openPassword('/member/user/password/${CP_SESSION.userId }');">
																					<span class="label label-icon label-success">
																						<i class="icon-key"></i>
																					</span>
																					&nbsp;&nbsp; 패스워드 변경이 필요합니다.
																				</a></li>
                                    </c:if>
                                    <c:forEach var="entryMap" items="${CP_SESSION.newNoticeList}">
                                        <li><a href="/system/notice/view/${entryMap.idx }">
																					<span class="label label-icon label-success">
																						<i class="icon-speech"></i>
																					</span>
																					&nbsp;&nbsp; ${entryMap.title }
																				</a></li>
                                    </c:forEach>
                                </ul>
                            </li>
                    </ul>
                    </li>
                    <li class="dropdown dropdown-user dropdown-dark"><a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
									<img alt="" class="img-circle" src="/assets/layouts/layout/img/avatar.png" />
                                    <c:choose>
                                        <c:when test="${CP_SESSION.grade == '하위가맹점'}">
                                            <span class="username username-hide-on-mobile"> ${CP_SESSION.userId}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="username username-hide-on-mobile"> ${CP_SESSION.name}</span>
                                        </c:otherwise>
                                    </c:choose>
								</a>
                        <ul class="dropdown-menu dropdown-menu-default" style="width: 200px;">
                            <c:if test="${!empty CP_DEBUG}">
                                <li><a href="javascript:cpDebug();">
												<i class="fa fa-bug"></i> DEBUG SET ${!CP_DEBUG}
											</a></li>
                            </c:if>
                            <li><a href="/member/user/myprofile/${CP_SESSION.userId }">
											<i class="icon-user"></i> My Profile
										</a></li>
                            <li><a href="/login/out">
											<i class="icon-logout"></i> Log Out
										</a></li>
                            <li><a href="javascript:;" style="cursor: default">
											<i class="icon-clock"></i> ${fn:substring(CP_SESSION.lastAccessDate, 0, 19) }
										</a></li>
                        </ul>
                    </li>
                    <!-- END QUICK SIDEBAR TOGGLER -->
            </ul>
        </div>
        <!-- END TOP NAVIGATION MENU -->
    </div>
    <!-- END HEADER INNER -->
</div>
<!-- BEGIN HEADER & CONTENT DIVIDER -->
<div class="clearfix"> </div>
<!-- END HEADER & CONTENT DIVIDER -->