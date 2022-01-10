<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- 리스트 본문 시작 -->
<div class="table-scrollable">
    <table class="table table-striped table-hover flip-content" id="sortTable"> <!-- table-bordered -->
        <thead>
            <tr>
                <th>No</th>
                <th data-sort="string">거래번호</th>
                <th data-sort="string">승인/취소</th>
                <th data-sort="string">가맹점명</th>
                <th data-sort="string">법정동</th>
                <th data-sort="string">사업자번호</th> 
                <th data-sort="string">터미널 ID</th>
                <th data-sort="string">합계금액</th>
                <th data-sort="string">승인번호</th>
                <th data-sort="string">금액</th>
                <th data-sort="string">ANY 승인번호</th>
                <th data-sort="string">ANY 금액</th>
                <th data-sort="string">처리상태</th>
                <th data-sort="string">등록일자</th>
            </tr>
        </thead>
        <tbody id="list">
                <tr>
                    <td>1</td>
                    <td class="link">12345678</td>
                    <td class="link_modal">모달 링크</td>
                    <td>내용4</td>
                    <td>내용5</td>
                    <td>내용6</td>
                    <td>내용7</td>
                    <td>내용8</td>
                    <td>내용9</td>
                    <td>내용10</td>
                    <td>내용11</td>
                    <td>내용12</td>
                    <td>내용13</td>
                    <td>내용14</td>
                </tr>
                <tr>
                    <td>2</td>
                    <td>내용2</td>
                    <td>내용3</td>
                    <td>내용4</td>
                    <td>내용5</td>
                    <td>내용6</td>
                    <td>내용7</td>
                    <td>내용8</td>
                    <td>내용9</td>
                    <td>내용10</td>
                    <td>내용11</td>
                    <td>내용12</td>
                    <td>내용13</td>
                    <td>내용14</td>
                </tr>
                <tr>
                    <td>3</td>
                    <td>내용2</td>
                    <td>내용3</td>
                    <td>내용4</td>
                    <td>내용5</td>
                    <td>내용6</td>
                    <td>내용7</td>
                    <td>내용8</td>
                    <td>내용9</td>
                    <td>내용10</td>
                    <td>내용11</td>
                    <td>내용12</td>
                    <td>내용13</td>
                    <td>내용14</td>
                </tr>
                <tr>
                    <td>4</td>
                    <td>내용2</td>
                    <td>내용3</td>
                    <td>내용4</td>
                    <td>내용5</td>
                    <td>내용6</td>
                    <td>내용7</td>
                    <td>내용8</td>
                    <td>내용9</td>
                    <td>내용10</td>
                    <td>내용11</td>
                    <td>내용12</td>
                    <td>내용13</td>
                    <td>내용14</td>
                </tr>
                <tr>
                    <td>5</td>
                    <td>내용2</td>
                    <td>내용3</td>
                    <td>내용4</td>
                    <td>내용5</td>
                    <td>내용6</td>
                    <td>내용7</td>
                    <td>내용8</td>
                    <td>내용9</td>
                    <td>내용10</td>
                    <td>내용11</td>
                    <td>내용12</td>
                    <td>내용13</td>
                    <td>내용14</td>
                </tr>
                <tr>
                    <td>6</td>
                    <td>내용2</td>
                    <td>내용3</td>
                    <td>내용4</td>
                    <td>내용5</td>
                    <td>내용6</td>
                    <td>내용7</td>
                    <td>내용8</td>
                    <td>내용9</td>
                    <td>내용10</td>
                    <td>내용11</td>
                    <td>내용12</td>
                    <td>내용13</td>
                    <td>내용14</td>
                </tr>
                <tr>
                    <td>7</td>
                    <td>내용2</td>
                    <td>내용3</td>
                    <td>내용4</td>
                    <td>내용5</td>
                    <td>내용6</td>
                    <td>내용7</td>
                    <td>내용8</td>
                    <td>내용9</td>
                    <td>내용10</td>
                    <td>내용11</td>
                    <td>내용12</td>
                    <td>내용13</td>
                    <td>내용14</td>
                </tr>
                <tr>
                    <td>8</td>
                    <td>내용2</td>
                    <td>내용3</td>
                    <td>내용4</td>
                    <td>내용5</td>
                    <td>내용6</td>
                    <td>내용7</td>
                    <td>내용8</td>
                    <td>내용9</td>
                    <td>내용10</td>
                    <td>내용11</td>
                    <td>내용12</td>
                    <td>내용13</td>
                    <td>내용14</td>
                </tr>
                <tr>
                    <td>9</td>
                    <td>내용2</td>
                    <td>내용3</td>
                    <td>내용4</td>
                    <td>내용5</td>
                    <td>내용6</td>
                    <td>내용7</td>
                    <td>내용8</td>
                    <td>내용9</td>
                    <td>내용10</td>
                    <td>내용11</td>
                    <td>내용12</td>
                    <td>내용13</td>
                    <td>내용14</td>
                </tr>
                <tr>
                    <td>10</td>
                    <td>내용2</td>
                    <td>내용3</td>
                    <td>내용4</td>
                    <td>내용5</td>
                    <td>내용6</td>
                    <td>내용7</td>
                    <td>내용8</td>
                    <td>내용9</td>
                    <td>내용10</td>
                    <td>내용11</td>
                    <td>내용12</td>
                    <td>내용13</td>
                    <td>내용14</td>
                </tr>
        </tbody>
    </table>
</div>
<div class="row">
    <div class="col-sm-4 col-xs-12" style="padding-left:0;">
        <select name="pageSize" id="pageSize" class="selectpicker pull-left col-md-4 col-sm-9 col-xs-12">
			<option value="20">20개씩 보기</option>
			<option value="50">50개씩 보기</option>
			<option value="100">100개씩 보기</option>
			<option value="200">200개씩 보기</option>
		</select>
    </div>
    <div class="col-sm-4 col-xs-12">
        <div class="paging">
            <span>
			<a href="javascript:void(0);" id="pagePrev" onClick="pagePrev()"><i class="fa fa-angle-left" aria-hidden="true"></i></a>
		</span>
            <span class="paging_num">
			Viewing
			<span>${CPR.page.current }</span> of ${CPR.page.totalPage }
            </span>
            <span>
			<a href="javascript:void(0);" class="mif-chevron-right mif-lg" id="pageNext" onClick="pageNext()"><i class="fa fa-angle-right" aria-hidden="true"></i></a>
		</span>
            <input type="hidden" name="totalPage" id="totalPage" value="${CPR.page.totalPage }" />
        </div>
    </div>
    <div class="col-sm-4 col-xs-12">
        <a href="javascript:void(0);" id="pageMove" onClick="pageMove()" class="btn blue-dark btn-sm col-sm-4 col-xs-4 pull-right">페이지 이동</a>
        <span class="paging_num col-sm-4 col-xs-8 pull-right">
            <input type="text" class="form-control input-sm input_paging" name="paging" id="currentPage" value="1" />
        </span>
    </div>
    
</div>
<!-- 리스트 페이징 종료 -->
