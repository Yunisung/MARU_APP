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

<body class="page-header-fixed page-sidebar-closed-hide-logo page-content-white page-sidebar-fixed">
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
                            <li><span>가상계좌관리</span><i class="fa fa-circle"></i></li>
                            <li><span>인증수수료조회</span></li>
                        </ul>
                        <div class="page-toolbar">
                            <div class="btn-group btn-theme-panel">
                                <a class="btn float-window"><i class="icon-size-fullscreen"></i></a>
                                <a href="javascript:;" class="btn dropdown-toggle" data-toggle="dropdown">
                                    <i class="icon-settings"></i>
                                </a>
                                <div class="dropdown-menu theme-panel pull-right dropdown-custom hold-on-click panel-warning">
                                    <div class="panel-heading">도움말</div>
                                    <div class="panel-body">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- BEGIN PAGE CONTENT INNER -->
                    <div class="page-content-inner" id="search-container">
                        <!-- 검색 폼 시작 -->
                        <div class="portlet light portlet-form">
                            <div class="portlet-body form light">
                                <form class="form-horizontal" role="form" data-form="true" id="searchForm" name="searchForm" action="/vact/auth/list"
                                      method="post">
                                    <input type="hidden" data-reg="false" name="reason" value="가상계좌 인증수수료내역">
                                    <input type="hidden" data-reg="false" name="thead" value="regDate:인증일시,authId:인증수수료ID,mchtName:가맹점명,mchtId:가맹점ID,vactAccount:가상계좌,authType:인증수단,bankName:은행,holderName:예금주,resultMsg:인증결과,authFee:인증수수료,authFeeVat:인증수수료VAT,stlStatus:인증수수료정산결과,stlId:정산번호,stlDay:정산예정일,stlType:정산유형, summary:비고">

                                    <div class="form-body">
                                        <div class="row">
                                            <div class="form-group pg-form-group">
                                                <div class="col-lg-4" style="padding:0;">
                                                    <select class="selectpicker col-lg-12" name="" id="date-selector" data-reg="false">
                                                        <option value="regDay" selected>인증일자</option>
                                                        <option value="stlDay">정산예정일</option>
                                                    </select>
                                                </div>
                                                <div class="col-lg-8">
                                                    <div class="input-group input-group-sm input-daterange" data-date-format="yyyy-mm-dd">
                                                        <input type="text" class="form-control now-date date-selector-target" name="regDay" value="" data-oper="ge">
                                                        <span class="input-group-addon">~</span>
                                                        <input type="text" class="form-control now-date date-selector-target" name="regDay" value="" data-oper="le">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">

                                        </div>
                                        <div class="row">
                                            <div class="form-group pg-form-group">
                                                <label class="control-label col-lg-4">인증수단</label>
                                                <select class="selectpicker col-lg-8" name="authType" data-oper="eq">
                                                    <option value="">-- 전체 -- </option>
                                                    <option value="실명인증">실명인증</option>
                                                    <option value="1원인증">1원인증</option>
                                                    <option value="ARS인증">ARS인증</option>
                                                </select>
                                            </div>
                                            <div class="form-group pg-form-group">
                                                <label class="control-label col-lg-4">가맹점ID</label>
                                                <div class="col-lg-8">
                                                    <input type="text" class="form-control input-sm" name="mchtId" data-oper="eq" placeholder="가맹점 아이디">
                                                </div>
                                            </div>
                                            <div class="form-group pg-form-group">
                                                <label class="control-label col-lg-4">가맹점명</label>
                                                <div class="col-lg-8">
                                                    <input type="text" class="form-control input-sm typeahead mchtName" name="mchtName" data-oper="lk" placeholder="가맹점명" data-search="mchtName">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-group pg-form-group">
                                                <label class="control-label col-lg-4">인증결과</label>
                                                <select class="selectpicker col-lg-8" name="resultStatus" data-oper="eq">
                                                    <option value="">-- 전체 -- </option>
                                                    <option value="Y">성공</option>
                                                    <option value="N">실패</option>
                                                </select>
                                            </div>
                                            <div class="form-group pg-form-group">
                                                <label class="control-label col-lg-4">인증수수료정산결과</label>
                                                <select class="selectpicker col-lg-8" name="stlStatus" data-oper="eq">
                                                    <option value="">-- 전체 -- </option>
                                                    <option value="정산대기">정산대기</option>
                                                    <option value="정산완료">정산완료</option>
                                                </select>
                                            </div>
                                            <div class="form-group pg-form-group">
                                                <label class="control-label col-lg-4">정산번호</label>
                                                <div class="col-lg-8">
                                                    <input type="text" class="form-control input-sm" name="stlId" data-oper="eq" placeholder="정산번호">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-group pg-form-group">
                                                <label class="control-label col-lg-4">가상계좌발행은행</label>
                                                <select class="selectpicker col-lg-8" name="vactBankCd" data-oper="eq">
                                                    <option value="">-- 전체 -- </option>
                                                    <option value="039">경남은행</option>
                                                    <option value="089">케이뱅크</option>
                                                    <option value="034">광주은행</option>
                                                </select>
                                            </div>
                                            <div class="form-group pg-form-group">
                                                <label class="control-label col-lg-4">예금주</label>
                                                <div class="col-lg-8">
                                                    <input type="text" class="form-control input-sm" name="holderName" data-oper="lk" placeholder="예금주">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-actions nobg right">
                                        <div class="btn folding-search-btn icon-arrow-down"></div>
                                        <div class="">
                                            <button type="button" class="btn btn-sm blue-dark" id="SearchClear">
                                                <i class="fa fa-eraser" aria-hidden="true"></i> RESET&nbsp;
                                            </button>
                                            <button type="button" class="btn btn-sm green" id="search_submit" onClick="searchForList()">
                                                <i class="fa fa-search" aria-hidden="true"></i> SEARCH
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <!-- 검색 폼 종료 -->
                        <!-- 내용 폼 시작 -->
                        <div class="portlet light portlet-form" id="searchResult">

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

<script type="text/javascript">
    gradeSelector('searchForm', '가맹점');
    setTimeout(function () {
        searchForList();
    }, 100); //검색 실행
    $('#nav-trx').addClass('active');

    $('#date-selector').on('change', function() {
        $('.date-selector-target').attr('name', $(this).val());
    })
    var trxArr = '';
    $(document).on('click', '.changeStlDay', function() {
        var authId = '';
        $('table.pg-table>tbody>tr').each(function(i, e) {
            if ($(e).find('input[type="checkbox"]').is(':checked')) {
                authId += "'" + $(e).attr('data-authId') + "',";
            }
        });

        if (authId.length < 1) {
            bootbox.alert("대상을 체크하세요.");
        } else {
            trxArr = authId.substring(0, authId.length - 1);
            var $modal = $('#pgmate-modal');
            if ($modal.children().length < 1) {
                $modal.empty();
            }
            var url = '/vact/auth/changeStlDay.jsp';

            $modal.load(url, '', function(responseTxt, statusTxt, xhr) {
                if (statusTxt == "success") {
                    $modal.modal();
                    textMask();
                }
            });
        }
    });
    $(document).on('click', '.changeSummary', function() {
        var trxId = '';
        $('table.pg-table>tbody>tr').each(function(i, e) {
            if ($(e).find('input[type="checkbox"]').is(':checked')) {
                trxId += "'" + $(e).attr('data-authId') + "',";
            }
        });

        if (trxId.length < 1) {
            bootbox.alert("대상을 체크하세요.");
        } else {
            trxArr = trxId.substring(0, trxId.length - 1);
            var $modal = $('#pgmate-modal');
            if ($modal.children().length < 1) {
                $modal.empty();
            }
            var url = '/vact/auth/changeSummary.jsp';

            $modal.load(url, '', function(responseTxt, statusTxt, xhr) {
                if (statusTxt == "success") {
                    $modal.modal();
                    textMask();
                }
            });
        }
    });
    $(document).on('click', '.changeStlStatus', function() {
        var trxId = '';
        $('table.pg-table>tbody>tr').each(function(i, e) {
            if ($(e).find('input[type="checkbox"]').is(':checked')) {
                trxId += "'" + $(e).attr('data-authId') + "',";
            }
        });

        if (trxId.length < 1) {
            bootbox.alert("대상을 체크하세요.");
        } else {
            trxArr = trxId.substring(0, trxId.length - 1);
            bootbox.confirm("선택항목의 정산을 완료처리 하시겠습니까?",function(result){
                if(result){
                    var json = new Object();
                    json.trxId = trxArr;

                    $.ajax({
                        type: "post",
                        url: "/vact/auth/changeStlStatusUpdate",
                        data: JSON.stringify(json),
                        contentType: "application/json",
                        success: function (json) {
                            if(json.resultCd == '0000'){
                                bootbox.alert(json.resultMsg,function(){
                                    pageMove();
                                });
                            }else{
                                bootbox.alert(json.resultMsg);
                            }

                        },
                        error: function (xhr, textStatus, errorThrown) {
                            bootbox.alert('Error ' + errorThrown);
                        }
                    });
                }

            });
        }
    });
</script>
<!-- 모달 생성을 위한 베이스 -->
<div id="pgmate-modal" class="modal fade container" data-backdrop="static" data-keyboard="false" tabindex="-1"></div>
</body>

</html>