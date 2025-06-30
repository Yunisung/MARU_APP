<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link rel="stylesheet" type="text/css" href="/assets/global/css/jquery.mCustomScrollbar.css" />
    <link rel="stylesheet" type="text/css" href="/assets/global/css/common.css" />
    <link href='/assets/global/css/font.css' rel='stylesheet' type='text/css'>
    <script type="text/javascript" src="/assets/global/scripts/gc.kis.v2.scr.kaspersky-labs.js" charset="UTF-8"></script>
    <script type="text/javascript" src="https://translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script>
    <script type="text/javascript">
        function googleTranslateElementInit() {
            new google.translate.TranslateElement({pageLanguage: 'ko', includedLanguages: 'ar,de,en,es,fr,hi,ja,mn,ms,ru,th,tr,vi,zh-CN', layout: google.translate.TranslateElement.InlineLayout.SIMPLE, autoDisplay: false}, 'google_translate_element');
        }
    </script>
    <script type="text/javascript" src="/assets/global/scripts/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="/assets/global/scripts/jquery.mCustomScrollbar.js"></script>
    <script type="text/javascript" src="/assets/global/scripts/common.js"></script>
    <title>크레디탑 전자결제서비스</title>
</head>
<body>
<section class="receipt_wrap">

    <header class="gnb">
        <h1 class="logo"><img src="/assets/global/img/login/mtouch.png" alt="건흥페이먼츠 전자결제서비스 logo"></h1>
        <div class="kind">

            <span>신용카드 매출전표</span>

        </div>
        <div id="google_translate_element"></div>
    </header>

    <section class="receipt_con">
        <section class="receipt">
            <div class="info">
                <ul class="top">
                    <li>
                        <div class="info_title">거래일시</div>
                        <div class="time blue" id="regDate"></div>
                    </li>
                    <li>
                        <div class="info_title">상품명</div>
                        <div class="p_name" id="p_name"></div>
                    </li>
                    <section class="price_wrap">
                        <li>
                            <div class="info_title">공급금액</div>
                            <div class="price_detail" id="supply_amount">
                                <span>원</span>
                            </div>
                        </li>
                        <li>
                            <div class="info_title">부가세</div>
                            <div class="price_detail" id="vat"><span>원</span></div>
                        </li>
                        <li class="price_sum">
                            <div class="info_title">합계금액</div>
                            <div class="price" id="amount"><span>원</span></div>
                        </li>
                    </section>
                </ul>
                <ul class="body">
                    <li>
                        <div class="info_title">카드종류</div>
                        <div id="issuer"></div>
                    </li>
                    <li>
                        <div class="info_title">카드번호</div>
                        <div id="card_number"></div>
                    </li>
                    <li>
                        <div class="info_title">유효기간</div>
                        <div>**/**</div>
                    </li>
                    <li>
                        <div class="info_title">거래유형</div>
                        <div id="status"></div>
                    </li>
                    <li>
                        <div class="info_title">취소일자</div>
                        <div class="time red" id="cancel_date"> </div>
                    </li>
                    <li>
                        <div class="info_title">할부</div>
                        <div id="installment">

                        </div>
                    </li>
                    <li>
                        <div class="info_title">구매자명</div>
                        <div id="buyer"></div>
                    </li>
                    <li>
                        <div class="info_title">승인번호</div>
                        <div id="auth_code"></div>
                    </li>



                </ul>
                <ul class="bottom">
                    <h3>이용상점 정보</h3>
                    <li class="f_width">
                        <div class="info_title">상호명</div>
                        <div id="mcht_name"></div>
                    </li>
                    <li>
                        <div class="info_title">대표자명</div>
                        <div id="mcht_ceo"></div>
                    </li>
                    <li>
                        <div class="info_title">사업자번호</div>
                        <div id="mcht_number">
                        </div>
                    </li>
                    <li>
                        <div class="info_title">연락처</div>
                        <div id="mcht_tel"></div>
                    </li>
                    <li class="f_width">
                        <div class="info_title">주소</div>
                        <div id="mcht_address"></div>
                    </li>
                </ul>

                <ul class="bottom">
                    <h3>서비스 제공사 정보</h3>
                    <li>
                        <div class="info_title">서비스 제공사</div>
                        <div>건흥페이먼츠(주)</div>
                    </li>
                    <li>
                        <div class="info_title">사업자번호</div>
                        <div>675-86-00152</div>
                    </li>
                    <li class="f_width">
                        <div class="info_title">주소</div>
                        <div>부산 해운대구 센텀중앙로 97 센텀스카이비즈 A동 2510호</div>
                    </li>
                </ul>

            </div>
        </section>

        <section class="notice">

            <p>

                ＊부가세법 제46조에 따라 신용카드 매출전표를 이용하여 매입세액 공제가 가능합니다.(동법 제 33조 2항에 근거하여 신용카드 매출전표를 발행한 경우에는 세금계산서를 발급하지 아니합니다.)

            </p>
        </section>
    </section>

    <form name="mailForm" method="post">
        <input type="hidden" name="TID" value="myvisa000m01012411080224558493" />
        <input type="hidden" name="SvcCd" value="01" />
        <input type="hidden" name="StateCd" value="0" />
        <!-- <section class="payment_input">
            <div>
                <div class="input_section">
                    <label for="e_mail" class="input_title">메일발송</label>
                    <div class="input_type1">
                        <input type="email" id="e_mail" name="e_mail" pattern="[A-Za-z0-9]*" class="e_mail" placeholder="이메일을 입력하세요"/>
                    </div>
                </div>
                <a class="btn_gray btn_s" href="#" onclick="javascript:submitMailForm()">발송</a>
            </div>
        </section> -->
    </form>

    <section class="btn_wrap_multi">
        <div>
            <a class="btn_gray btn" href="javascript:window.close(); ">닫기</a>
            <a class="btn_blue btn" href="javascript:print();">인쇄</a>
        </div>
    </section>


</section>
<img class="print_bg" src="/assets/global/img/print_bg.png">
<iframe name="hiddenFrame" src="" height="0" width="0" frameborder="0" style="display:none;" ></iframe>

</body>
<script type="text/javascript">
    const formatCurrency = (value) => {
        return value.toLocaleString('ko-KR') + '원';
    }

    const formatBusinessNumber = (number) => {
        // 숫자만 추출
        const onlyNumbers = number.toString().replace(/\D/g, '');
        // 정규식을 이용해 사업자 번호 형식으로 변환
        return onlyNumbers.replace(/(\d{3})(\d{2})(\d{5})/, '$1-$2-$3');
    }

    document.getElementById("regDate").textContent = '${DATAMAP.regDate}'.slice(0,19);
    document.getElementById("p_name").textContent = '${DATAMAP.prodName}';
    document.getElementById("supply_amount").textContent = formatCurrency(${DATAMAP.supplyAmount});
    document.getElementById("vat").textContent = formatCurrency(${DATAMAP.vat});
    document.getElementById("amount").textContent = formatCurrency(${DATAMAP.amount});
    document.getElementById("issuer").textContent = '${DATAMAP.issuer}';
    document.getElementById("card_number").textContent = '${DATAMAP.bin}' + '******' + '${DATAMAP.last4}';
    document.getElementById("status").textContent = '${DATAMAP.status}';
    document.getElementById("installment").textContent = ${DATAMAP.installment} == '00' ? '일시불' : '${DATAMAP.installment}'+'개월';
    document.getElementById("buyer").textContent = '${DATAMAP.payerName}';
    document.getElementById("auth_code").textContent = '${DATAMAP.authCd}';

    document.getElementById("mcht_name").textContent = '${DATAMAP.nick}';
    document.getElementById("mcht_ceo").textContent = '${DATAMAP.ceoName}';
    document.getElementById("mcht_number").textContent = formatBusinessNumber(${DATAMAP.identity});
    document.getElementById("mcht_tel").textContent = '${DATAMAP.tel1}';
    document.getElementById("mcht_address").textContent = '${DATAMAP.addr1}' +' '+ '${DATAMAP.addr2}';

    document.getElementById("cancel_date").textContent = '${DATAMAP.rfdDate}'.slice(0,19);

    function submitMailForm(){
        var frm = document.forms['mailForm'];
        var email = frm.e_mail.value;
        if(email == null || email == ""){
            alert("이메일주소를 입력해 주세요");
            return false;
        }else if(!chkEmail(email)){
            alert("올바른 이메일주소를 입력해 주세요");
            return false;
        }else{
            frm.target = "hiddenFrame";
            frm.action = "SendMail.jsp";
            frm.submit();
        }

    }
</script>
</html>