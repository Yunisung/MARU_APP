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
                                <li><span>고객센터</span><i class="fa fa-circle"></i></li>
                                <li><span>자주 묻는 질문</span></li>
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
                    </div>
                    <div class="page-content-inner">
                        <div class="faq-page faq-content-1">
                            <div class="faq-content-container ">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="faq-section ">
                                            <h2 class="faq-title uppercase font-blue">General</h2>
                                            <div class="panel-group accordion faq-content" id="accordion1">
                                                <c:forEach var="entry" items="${DATAMAP}" varStatus="status">
                                                    <c:if test="${entry.category eq '일반'}">
                                                        <div class="panel panel-default">
                                                            <div class="panel-heading">
                                                                <h4 class="panel-title">
                                                                    <!-- <i class="fa fa-circle"></i> -->
                                                                    <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion1" href="#general_${status.count}" aria-expanded="false"> ${entry.title}</a>
                                                                </h4>
                                                            </div>
                                                            <div id="general_${status.count}" class="panel-collapse collapse" aria-expanded="false" style="height: 0px;">
                                                                <div class="panel-body">${entry.summary}</div>
                                                            </div>
                                                        </div>
                                                    </c:if>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- BEGIN CONTAINER -->
        </div>
    </div>
    <c:import url="/include/footer.jsp" />
    </div>
    <c:import url="/include/javascript.jsp" />
    <script type="text/javascript">
        $('#nav-customer').addClass('active');
    </script>
    <!-- 모달 생성을 위한 베이스 -->
    <div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>