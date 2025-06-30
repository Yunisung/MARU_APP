<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.pgmate.lib.util.lang.CommonUtil" %>
<%
	Date nowTime = new Date();
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String current = sdf.format(nowTime);
	   
	cal.set(Calendar.YEAR, Integer.parseInt(current.substring(0,4)));
	cal.add(Calendar.MONTH, -1);
	cal.set(Calendar.DATE, 1);
	String startDay = sdf.format(cal.getTime());
	   
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
	SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-");
	String getEndDay = sdf2.format(cal.getTime());
	String setEndDay = sdf3.format(cal.getTime());
	String endDay = String.valueOf(CommonUtil.getLastDayOfMonth(getEndDay));
%>
<div>
	<table style="margin: 0 auto; padding: 0px; width: 780px;" cellspacing="0" cellpadding="0">
		<tbody>
			<tr>
				<td>
					<img style="border: 0px currentColor; border-image: none; width: 780px; vertical-align: top; object-fit:cover;"
					src="https://svc.mtouch.com/assets/global/img/emailTop.jpg">
				</td>
			</tr>
			<tr>
				<td>
					<table cellpadding="0" cellspacing="0" border="0" style="width: 780px; border-left: 1px #c4c4c4 solid; border-right: 1px #c4c4c4 solid; box-sizing: border-box; padding-left: 45px;">
						<tbody>
							<tr>
								<td style="width: 690px;">
									<table style="width: 690px;">
										<tbody>
											<tr>
												<td></td>
											</tr>
											<tr>
												<td style="font-family: 맑은고딕, Malgun Gothic; font-size: 14px; color: #666666; letter-spacing: -0.07em; line-height: 20px;">
													(주)건흥페이먼츠 서비스를 이용해 주셔서 감사드립니다.</td>
											</tr>
											<tr>
												<td style="height: 20px;">&nbsp;</td>
											</tr>
										</tbody>
									</table>
									<table style="margin: 0px; width: 690px; border-collapse: collapse;">
										<tbody>
											<tr>
												<td height="1" colspan="4" style="background:#e5e5e5"></td>
												<td></td>
											</tr>
											<tr>
												<td width="100" style="padding-bottom:9px;padding-top:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;vertical-align:top">
                                  			      	가맹점
                                   		 		</td>
			                                    <td style="padding-bottom:9px;padding-top:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#333;vertical-align:top">
			                                    	${DATAMCHT.nick} (아이디 : ${DATAMCHT.mchtId})
			                                    </td>
		                                    </tr>
		                                    <tr>
												<td width="100" style="padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;vertical-align:top">
                                  			      	${DATAMCHT.idType}
                                   		 		</td>
			                                    <td style="padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#333;vertical-align:top">
			                                    	${DATAMCHT.identity}
			                                    </td>
		                                    </tr>
		                                    <tr>
												<td width="100" style="padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;vertical-align:top">
                                  			      	계좌정보
                                   		 		</td>
			                                    <td style="padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#333;vertical-align:top">
			                                    	${DATAMCHT.bankName}
			                                    	
			                                    	${fn:substring(DATAMCHT.account, 0, 4) }
			                                    	*****
			                                    	<c:set var="cnt" value="${fn:length(DATAMCHT.account) }" />
			                                    	${fn:substring(DATAMCHT.account, cnt-4, cnt-0) }
			                                    	
			                                    	(예금주:${DATAMCHT.accntHolder}) 
			                                    </td>
		                                    </tr>
		                                    <tr>
												<td height="1" colspan="4" style="background:#e5e5e5"></td>
												<td></td>
											</tr>
										</tbody>
									</table>
								</td>
							</tr>
							<tr>
								<td style="height: 10px;">&nbsp;</td>
							</tr>
							<tr>
								<td>
									<table style="margin: 0px; width: 690px; border-collapse: collapse;">
										<tbody>
											<tr>
												<td style="font-family: 맑은고딕, Malgun Gothic; font-size: 18px; color: #333333; letter-spacing: -0.1em; margin: 0; padding-bottom: 6px;">
													정산내역</td>
											</tr>
											<tr>
												<td colspan="2" style="padding-bottom: 6px; text-align: left; color: #000000; font-family: 맑은고딕, Malgun Gothic; font-size: 12px;">
													* 기간 : <span style="font-weight: bold;">
													<%=startDay %> ~ <%=setEndDay+endDay %>
													</span>
												</td>
												<td style="padding-bottom: 6px; text-align: right; color: #000000; font-family: 맑은고딕, Malgun Gothic; font-size: 12px;">
													(단위 : 원)
												</td>
											</tr>
											<tr></tr>
											<tr style="height: 2px; background: #005874;">
												<td colspan="3" height="1" style="background:#e5e5e5"></td>
											</tr>
											<tr>
												<td style="background: #F3F9FA; padding: 10px; width: 33%; text-align: center; color: #000000; font-family: 맑은고딕, Malgun Gothic; font-size: 12px; border-bottom: 1px solid #dbdbdb; border-right: 1px solid #dbdbdb;">
													지급일자</td>
												<td style="background: #F3F9FA; width: 33%; text-align: center; color: #000000; padding-left: 10px; font-family: 맑은고딕, Malgun Gothic; font-size: 12px; border-bottom: 1px solid #dbdbdb; border-right: 1px solid #dbdbdb;">
													정산 대상 거래금액</td>
												<td style="background: #F3F9FA; padding: 10px; width: 33%; text-align: center; color: #000000; font-family: 맑은고딕, Malgun Gothic; font-size: 12px; border-bottom: 1px solid #dbdbdb;">
													실제 지급금액</td>
											</tr>
										<c:forEach var="DATAMAP" items="${DATAMAP}" varStatus="status">
											<tr>
												<td style="padding: 10px; width: 20%; text-align: center; color: #000000; font-family: 맑은고딕, Malgun Gothic; font-size: 12px; border-bottom: 1px solid #dbdbdb; border-right: 1px solid #dbdbdb;">
													<fmt:parseDate value="${DATAMAP.stlDay }" var="stlDay" pattern="yyyyMMdd"/>
													<fmt:formatDate value="${stlDay }" pattern="yyyy-MM-dd" var="stlDay"/>
													${stlDay }
												</td>
												<td style="width: 30%; text-align: right; color: #000000; padding-right: 10px; font-family: 맑은고딕, Malgun Gothic; font-size: 12px; border-bottom: 1px solid #dbdbdb; border-right: 1px solid #dbdbdb;">
													<fmt:formatNumber value="${DATAMAP.amt }" />
												</td>
												<td style="padding: 10px; width: 20%; text-align: right; color: #000000; font-family: 맑은고딕, Malgun Gothic; font-size: 12px; border-bottom: 1px solid #dbdbdb;">
													<fmt:formatNumber value="${DATAMAP.stlAmount }" />
												</td>
												<c:set var="sumAll" value="${sumAll + DATAMAP.amt}"/>	
												<c:set var="sumStl" value="${sumStl + DATAMAP.stlAmount}"/>
											</tr>
										</c:forEach>
											<tr>
												<td style="padding: 10px; text-align: center; color: #000000; font-family: 맑은고딕, Malgun Gothic; font-size: 12px; border-bottom: 1px solid #dbdbdb; border-right: 1px solid #dbdbdb;">
													합계</td>
												<td style="text-align: right; color: #000; padding-right: 10px; font-family: 맑은고딕, Malgun Gothic; font-size: 12px; font-weight: bold; border-bottom: 1px solid #dbdbdb; border-right: 1px solid #dbdbdb;">
													<fmt:formatNumber value="${sumAll }" />
												</td>
												<td style="text-align: right; color: #000; padding-right: 10px; font-family: 맑은고딕, Malgun Gothic; font-size: 12px; font-weight: bold; border-bottom: 1px solid #dbdbdb;">
													<fmt:formatNumber value="${sumStl }" />
												<td>
											</td>
											</tr>
										</tbody>
									</table>
								</td>
							</tr>
							<tr>
								<td style="height: 10px;">&nbsp;</td>
							</tr>
							<tr>
								<td>
									<table style="font-family: 맑은고딕, Malgun Gothic; font-size: 12px; color: #666666; line-height: 17px;">
										<tbody>
											<tr>
												<td colspan="2" style="font-family: 맑은고딕, Malgun Gothic; font-size: 18px; color: #333333; letter-spacing: -0.1em; margin: 0; padding-bottom: 10px;">
													안내사항</td>
											</tr>
											<tr>
												<td style="vertical-align: top;">*</td>
												<td>해당 일자별 상세내역은 <a target="_blank" style="color: red; text-decoration: underline;"
													href="https://admin.bkwinners.kr/" >매입현황조회</a>를 이용해 주시면 빠른 확인 가능합니다.
												</td>
											</tr>
											<tr>
												<td style="vertical-align: top;">*</td>
												<td>정산 대상 거래금액 : 해당 일자에 정산된 총 거래 금액 (승인금액 – 취소금액)<br>
											</tr>
											<tr>
												<td style="vertical-align: top;">*</td>
												<td>지급금액 : 해당 일자에 실제로 상점에 지급된 금액 (정산 대상 거래금액 – (수수료+부가세+지급보류 등))<br>
											</tr>
											<tr>
												<td style="vertical-align: top;">*</td>
												<td>지급 일자에 정산 대상 건이 없을 경우 표기되지 않습니다.</td>
											</tr>
										</tbody>
									</table>
								</td>
							</tr>
							<tr>
								<td style="height: 10px;">&nbsp;</td>
							</tr>
							<tr>
								<td>
									<table style="font-family: 맑은고딕, Malgun Gothic; font-size: 12px; line-height: 22px;"
										border="0" margin:="" 0px;="" width:="" 690px;="" border-collapse:="" collapse;"="">
										<tbody>
											<tr>
												<td style="vertical-align: top;"><img
													src="https://admin.bkwinners.kr/assets/global/img/emailDot.gif"></td>
												<td>메일 수신을 원하지 않으시면, <a target="_blank"
													href="https://admin.bkwinners.kr/emailStatus?mchtId=${DATAMCHT.mchtId}"
													style="color: #555; text-decoration: underline; font-weight: bold;">
													이메일 수신거부</a>를 클릭해 주시기 바랍니다.
												</td>
											</tr>
											<tr>
												<td style="vertical-align: top;"><img
													src="https://admin.bkwinners.kr/assets/global/img/emailDot.gif"></td>
												<td>기타 문의가 있으신 분은 ☎ 1855-1838로 문의주시기 바랍니다.</td>
											</tr>
											<tr>
												<td style="vertical-align: top;"><img
													src="https://admin.bkwinners.kr/assets/global/img/emailDot.gif"></td>
												<td>(주)건흥페이먼츠는 앞으로도 더 나은 서비스 제공을 위해 최선을 다하겠습니다.</td>
											</tr>
										</tbody>
									</table>
								</td>
							</tr>
							<tr>
								<td style="height: 10px;">&nbsp;</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<img style="border: 0px currentColor; border-image: none; width: 780px; vertical-align: top; object-fit:cover;"
					src="https://admin.bkwinners.kr/assets/global/img/emailBottom.jpg">
				</td>
			</tr>
		</tbody>
	</table>
</div>