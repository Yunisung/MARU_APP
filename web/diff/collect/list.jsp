<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="portlet-title">
	<div class="caption font-red-sunglo">
		<i class="icon-share font-red-sunglo"></i>
		<span class="caption-subject bold uppercase"> Result </span>
		<span class="caption-helper"><span id="page-total">${fn:length(CPR.data)}</span> 건 </span>
	</div>
	
	<div class="actions">
		<a class="btn btn-circle btn-default" href="javascript:deleteCollectSelected();">
			선택 입금정산 삭제
		</a>
		<a class="btn btn-circle btn-default" id="excel-click" href="" style="display:none;">Excel Download</a>
		<a class="btn btn-circle btn-icon-only btn-default" id="excel-export" href="javascript:fnExcelReport('sortTable', '차액정산 내역 생성');">
			<i class="fa fa-file-excel-o" aria-hidden="true"></i>
		</a>
		<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" data-original-title="" title=""> </a>
	</div>
</div>
<div class="portlet-body form light">
	<c:set var="SALES_GRADE" value="에이전시"></c:set> <%-- ${CP_SESSION.grade} --%>
	<div class="table-scrollable">
		<!-- 리스트 본문 시작 -->
		<table class="pg-table table table-bordered table-hover flip-content" id="sortTable">
			<!-- table-bordered -->
			<thead>
				<tr>
					<th class="excel-hide"><input type="checkbox" class="all-check excel-hide" id="check_all" class="checkbox-style" /><label for="check_all"></label></th>
					<th>No</th>
					<th>입금일자</th>
					<th>입금ID</th>
					<th>이름</th>
					<th>상태</th>
					<th>입금대상 분류</th>
					<th>입금대상</th>
					<th>입금예정액</th>
					<th>입금금액</th>
					<th>입금차액</th>
					<th>기재내용 매칭값</th>
					<th>생성자</th>
					<th>생성일시</th>
					<th class="excel-hide">Action</th>
				</tr>
			</thead>
			<tbody id="list">
				<c:set var="var1" value="0"/>
				<c:set var="var2" value="0"/>
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="14">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr> 
				</c:if>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<c:if test="${entry.status eq '생성'}">
					<tr data-collectId="${entry.collectId}">
						<td class="btn-td excel-hide">
							<input type="checkbox" class="row-check" id="${entry.collectId}_check" class="checkbox-style " /><label for="${entry.collectId}_check"></label>
						</td>
					</c:if>
					<c:if test="${entry.status ne '생성'}">
					<tr>
						<td class="btn-td excel-hide"></td>
					</c:if>
						<td>${status.count}</td>
						<td class="date">${entry.collectDay}${entry.collectTime}</td>
						<td class="link" data-url="/diff/collect/view/${entry.collectId}">${entry.collectId}</td>
						<td class="link" data-url="/diff/collect/view/${entry.collectId}">${entry.name}</td>
						<td style="mso-number-format:'\@'">${entry.status}</td>
						<td style="mso-number-format:'\@'">${entry.category}</td>
						<td style="mso-number-format:'\@'">${entry.categoryId}</td>
						<td class="digits">${entry.stlVanAmount}</td>
						<td class="digits">${entry.collectAmt}</td>
						<td class="digits">${entry.deductAmount}</td>
						<td style="mso-number-format:'\@'">${entry.sender}</td>
						<td style="mso-number-format:'\@'">${entry.regId}</td>
						<td class="date">${entry.regDate}</td>
						<td class="excel-hide">
						<c:if test="${entry.status eq '생성'}">
							<button class="btn btn-sm btn-default btn-delete-collect excel-hide" data-id="${entry.collectId}">삭제</button>
						</c:if>
						</td>
					</tr>
					<c:set var="var1"  value="${var1 +  entry.stlVanAmount}"/>
					<c:set var="var2"  value="${var2 +  entry.collectAmt}"/>
					<c:set var="var3"  value="${var3 +  entry.deductAmount}"/>
				</c:forEach>
				<tr>
					<td class="excel-hide"></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td>합계</td>
					<td class="text-right digits"><c:out value="${var1}" /></td> 
					<td class="text-right digits"><c:out value="${var2}" /></td> 
					<td class="text-right digits"><c:out value="${var3}" /></td> 
					<td></td> 
					<td></td>
					<td></td>
					<td class="excel-hide"></td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<!-- 리스트 페이징 종료 -->
