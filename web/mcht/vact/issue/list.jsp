<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- BEGIN PAGE CONTENT - MARU - INNER -->
<div class="portlet light">
								
	<div class="portlet-body form light">
		<div class="row">
			<div class="col-md-6">
				<div style="font-size:15px">
					발급받은 총 가상계좌: <span class="font-blue">${CPR.page.total}</span>
				</div>
			<br>
			<form class="form-inline" role="form">
				<div class="form-group">
					<select id="cnt-ex-bank" style="width:80px;">
						<c:forEach var="entry" items="${UNUSED_ACCNT_MAP}">
							<option value="${entry.bankCd}">${entry.issuerBank}</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<input type="text" class="form-control input-sm" style="width:180px;" value="${MCHT_MAP.name}" id="cnt-ex-holder">
				</div>
				<div class="form-group">
					<input type="text" class="form-control input-sm numberOnly" maxlength="20" style="width:80px;" value="1" id="cnt-ex-accnt">
				</div>
				<a class="btn btn-sm btn-default" id="btn-ex-accnt" data-org-cnt="${ISSUED_ACCNT}">계좌 추가발행</a>
			</form>
		</div>
		<div class="col-md-6" style="font-size:15px">
			추가 발급 가능 가상계좌
			<c:forEach var="entry" items="${UNUSED_ACCNT_MAP}" varStatus="status">
				<div>${entry.issuerBank} : <span class="font-blue digits">${entry.cnt}</span></div>
			</c:forEach>
		</div>
	</div>
	</div>
</div>
<div class="portlet-title">
	<div class="caption font-red-sunglo">
		<i class="icon-share font-red-sunglo"></i>
		<span class="caption-subject bold uppercase"> Result </span>
		<span class="caption-helper"><span id="page-total">${CPR.page.total}</span> 건</span> 
	</div>
	<div class="actions">
		<a class="btn btn-circle btn-icon-only btn-default" href="javascript:searchForExcel();">
			<i class="fa fa-file-excel-o" aria-hidden="true"></i>
		</a>
		<a class="btn btn-circle btn-icon-only btn-default" href="javascript:searchForPDF();">
			<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
		</a>
		<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" data-original-title="" title=""> </a>
	</div>
</div>
<div class="portlet-body form light">
	<div class="table-scrollable">
		<!-- 리스트 본문 시작 -->
		<table class="pg-table table table-striped table-hover flip-content" id="sortTable">
			<!-- table-bordered -->
			<thead>
				<tr>
					<th>No</th>
					<th>발행번호</th>
					<th>은행명</th>
					<th>계좌번호</th>
					<th>예금주명</th>
					<th>거래추적번호</th>
					<th>사용자 정의1</th>
					<th>사용자 정의2</th>
					<th>상태</th>
					<th>Action</th>
					<th>최종일자</th>
				</tr>
			</thead>
			<tbody id="list">
				<c:if test="${CPR.result.code != 200}">
					<tr>
						<td colspan="9">${CPR.result.code}:&nbsp;${CPR.result.message}:&nbsp;${CPR.result.error}</td>
					</tr>
				</c:if>
				<c:set value="0" var="amount_sum"/>
				<c:forEach var="entry" items="${CPR.data}" varStatus="status">
					<tr>
						<td>${status.count}</td>
						<td>${entry.issueId}</td>
						<td>${entry.issuerBank}</td>
						<td>${entry.account}</td>

						<td>${entry.holderName}</td>
						<td><input type="text" name="trackId" value="${entry.trackId}"></td>
						<td><input type="text" name="udf1" value="${entry.udf1}"></td>
						<td><input type="text" name="udf2" value="${entry.udf2}"></td>

						<td>
							<c:choose>
								<c:when test="${entry.status eq '대기'}">
									<select name="status" class=" input-xsmall">
										<option value="대기" selected>대기</option>
										<option value="발행">발행</option>
									</select>
								</c:when>
								<c:when test="${entry.status eq '발행'}">
									<select name="status" class=" input-xsmall">
										<option value="대기">대기</option>
										<option value="발행" selected>발행</option>
									</select>
								</c:when>
								<c:otherwise>
									<span class="font-red">${entry.status}</span>
								</c:otherwise>
							</c:choose>
						</td>
						<td>
							<c:if test="${entry.status ne '만료'}">
								<button onClick="setRowInfo(this, '${entry.issueId}');" class="btn btn-sm btn-default">Set</button>
								<button onClick="setRowInfo(this, '${entry.issueId}', 'Y');" class="btn btn-sm yellow">expiry</button>
							</c:if>
						</td>
						<td class="date">${entry.regDate}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div class="row">
		<div class="col-sm-4 col-xs-12">
			<select name="pageSize" id="pageSize" class="input-sm pull-left col-lg-4 col-sm-5 col-xs-12">
				<option value="20">20개씩 보기</option>
				<option value="50">50개씩 보기</option>
				<option value="100">100개씩 보기</option>
				<option value="200">200개씩 보기</option>
			</select>
			<script type="text/javascript">$('#pageSize').find('option[value="${CPR.page.size}"]').attr("selected", "selected");</script>
		</div>
		<div class="col-sm-4 col-xs-12">
			<div class="paging">
				<span>
					<a href="javascript:void(0);" class="mif-chevron-left mif-lg" id="pagePrev" onClick="pagePrev()">
						<i class="fa fa-angle-left" aria-hidden="true"></i>
					</a>
				</span>
				<span class="paging_num">
					Viewing
					<span>${CPR.page.current}</span>
					of ${CPR.page.totalPage}
				</span>
				<span>
					<a href="javascript:void(0);" class="mif-chevron-right mif-lg" id="pageNext" onClick="pageNext()">
						<i class="fa fa-angle-right" aria-hidden="true"></i>
					</a>
				</span>
				<input type="hidden" name="totalPage" id="totalPage" value="${CPR.page.totalPage}" />
			</div>
		</div>
		<div class="col-sm-4 col-xs-12">
			<a href="javascript:void(0);" id="pageMove" onClick="pageMove()" class="btn blue-dark btn-sm col-sm-4 col-xs-4 pull-right">페이지 이동</a>
			<span class="paging_num col-sm-4 col-xs-8 pull-right">
				<input type="text" class="form-control input-sm input_paging" name="paging" id="currentPage" value="${CPR.page.current}" />
			</span>
		</div>
	</div>
</div>
<!-- 리스트 페이징 종료 -->
<script>
$('#btn-ex-accnt').click(function () {
	var bankCd = $('#cnt-ex-bank').val();
	var holderName = $('#cnt-ex-holder').val();
	var bankName = $(document).find('[data-id="cnt-ex-bank"]').text();
	var cnt = Number($('#cnt-ex-accnt').val());
	var orgCnt = $(this).attr('data-org-cnt');

	if (cnt <= 0 || !bankCd) {
		bootbox.alert('발행하려는 총 계좌수가 올바르지 않습니다.');
	} else {
		bootbox.confirm(bankName + '은행 계좌를 ' + cnt + '개 추가발급 하시겠습니까?', function (res) {
			if (res) {
				$.ajax({
					type: "get",
					url: "/mcht/vact/exissue/${MCHT_MAP.mchtId}/" + bankCd + "/" + holderName + "/" + cnt,
					success: function (res) {
						console.log(res);
						if (res.result == 'OK') {
							bootbox.alert('가상계좌 추가발급에 성공했습니다.', function () {
								window.location.reload();
							});
						} else {
							bootbox.alert(res.msg, function () {
								window.location.reload();
							});
						}
					},
					error: function (res, stats) {
						console.log(res, status);
						bootbox.alert('가상계좌 추가발급에 실패했습니다.' + status);
					}
				});
			}
		});
	}
});

function setRowInfo(t, issueId, expiry) {
	var trEle = $(t).closest('tr');
	var status = trEle.find('[name="status"]').val();
	var trackId = trEle.find('[name="trackId"]').val();
	var udf1 = trEle.find('[name="udf1"]').val();
	var udf2 = trEle.find('[name="udf2"]').val();
	console.log(trackId, status, udf1, udf2, issueId, expiry);

	bootbox.confirm('가상계좌 정보를 수정하시겠습니까?', function (res) {
		var sendObj = {
			issueId: issueId,
			status: status,
			trackId: trackId,
			udf1: udf1,
			udf2: udf2
		};
		if (expiry == 'Y') sendObj.status = '만료';

		if (res) {
			$.ajax({
				type: "post",
				url: "/mcht/vact/row/update",
				beforeSend: function (xhr) {
					xhr.setRequestHeader("Content-type", "application/json;charset=utf-8");
				},
				data: JSON.stringify(sendObj),
				success: function (res) {
					if (res.result == 'OK') {
						bootbox.alert('가상계좌 정보 변경에 성공했습니다.', function () {
							window.location.reload();
						});
					} else {
						bootbox.alert(res.msg, function () {
							window.location.reload();
						});
					}
				},
				error: function (xhr, status, error) {
					console.log("변경에 실패했습니다. 관리자에게 문의하세요.");
				}
			});
		}
	});
}
</script>
