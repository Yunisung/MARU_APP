<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="printInfo">
	<div class="row">
		<div class="col-sm-12">
			<!-- BEGIN ADD FORM TABLE-->
			<div class="portlet light">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-reorder"></i> <small>가맹점 결제 상세</small>
					</div>
				</div>
				<div class="portlet-body">
					<div class="row">
						<div class="col-sm-12">
							<div class="table-scrollable">
								<table class="table table-striped table-bordered table-hover flip-content">
									<!-- 작게 table-condensed -->
									<thead>
										<tr>
											<th data-sort="string">주문 메뉴 시퀀스</th>
											<th data-sort="string">주문수량</th>
											<th data-sort="string">객단가</th>
											<th data-sort="string">바코드번호</th>
											<th data-sort="string">합계금액</th>
										</tr>
									</thead>
									<tbody id="list">
											<tr>
												<td>1</td>
												<td>1</td>
												<td>60000</td>
												<td>00061</td>
												<td>60000</td>
											</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
