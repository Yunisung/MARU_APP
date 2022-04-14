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
    <link href="/assets/global/plugins/bootstrap-fileinput/bootstrap-fileinput.css" rel="stylesheet">
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
                                <li><span>test</span><i class="fa fa-circle"></i></li>
                                <li><span>test 내역 업로드</span></li>
                            </ul>
                            <div class="page-toolbar">
                                <div class="btn-group btn-theme-panel">
                                    <a class="btn float-window"><i class="icon-size-fullscreen"></i></a>
                                    <a href="javascript:;" class="btn dropdown-toggle" data-toggle="dropdown">
											<i class="icon-settings"></i>
										</a>
                                    <div class="dropdown-menu theme-panel pull-right dropdown-custom hold-on-click panel-warning">
                                        <div class="panel-heading">도움말</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- END PAGE BAR -->
                        <!-- BEGIN PAGE CONTENT INNER -->
                        <div class="page-content-inner" id="search-container">
                            <!-- 검색 폼 시작 -->
                            <div class="portlet light portlet-form">
                                <div class="portlet-body form light">
                                    <div class="row">
                                        <div class="col-md-6" style="max-width: 480px;">
                                            <div class="fileinput fileinput-new" data-provides="fileinput">
                                                <div class="input-group">
                                                    <div class="form-control uneditable-input input-fixed input-large" data-trigger="fileinput">
                                                        <i class="fa fa-file fileinput-exists"></i>&nbsp;
                                                        <span class="fileinput-filename">
                                                        </span>
                                                    </div>
                                                    <span class="input-group-addon btn blue btn-file">
                                                        <span class="fileinput-new"> Excel 파일 업로드 </span>
                                                        <span class="fileinput-exists"> 변경 </span>
                                                        <input type="hidden">
                                                        <input type="file" name="upload-excel" id="upload-excel" accept=".csv">
                                                    </span>
                                                    <a href="javascript:;" class="input-group-addon btn red fileinput-exists" data-dismiss="fileinput">	제거 </a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="pull-right">
<%-- 																<button type="button" class="btn btn-sm blue-dark right" id="SearchClear">
                                                    <i class="fa fa-eraser" aria-hidden="true"></i> RESET&nbsp;
                                                </button> --%>
                                                <button type="button" class="btn btn-sm green right" id="search_submit" onClick="uploadFile();">
                                                    <i class="fa fa-check" aria-hidden="true"></i> 선택항목 반영
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- 검색 폼 종료 -->
                            <!-- 내용 폼 시작 -->
                            <div class="portlet light portlet-form" id="searchResult">
                                <div class="portlet-title">
                                    <div class="caption font-red-sunglo">
                                        <i class="icon-share font-red-sunglo"></i>
                                        <span class="caption-subject bold uppercase"> Result </span>
                                    </div>
                                </div>
                                <div class="portlet-body form light">
                                    <div class="table-scrollable">
                                        <!-- 리스트 본문 시작 -->
                                        <table class="pg-table table table-hover flip-content" id="sortTable">
                                        <thead><tr></tr></thead>
                                        <tbody></tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                            <!-- 내용 폼 종료 -->
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
    <script src="/assets/global/plugins/bootstrap-fileinput/bootstrap-fileinput.js" type="text/javascript"></script>
    <script src="/assets/global/plugins/xlsx/xlsx.full.min.js"></script>
    <script src="/assets/global/test/test-excel.js" type="text/javascript"></script>
    <script type="text/javascript">
    	$('#nav-collect').addClass('active');
    </script>
    <!-- 모달 생성을 위한 베이스 -->
    <div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>