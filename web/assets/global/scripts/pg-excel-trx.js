var rABS = true; // T : 바이너리, F : 어레이 버퍼

var resultArray = [];
var vanFormat = {
    DEFAULT: {
        range: 3,
        valid: true,
        amount: {
            name: '금액',
            type: 'number'
        },
        trnType: {
            name: '거래구분',
            type: function(val) {
                var res = '';
                if (val === '승인') res = '승인';
                else if (val === '승인취소') res = '승인취소';
                return res;
            }
        },
        installment: {
            name: '할부',
            type: function(val) {
                return pad(val, 2);
            }
        },
        bin: {
            name: '카드번호',
            type: 'left 6'
        },
        last4: {
            name: '카드번호',
            type: 'right 4'
        },
        authCd: {
            name: '승인번호',
            type: ''
        },
        trxDay: {
            name: '거래일자',
            type: 'date'
        },
        trxTime: {
            name: '거래시간',
            type: 'time'
        },
        trackId: {
            name: '주문번호',
            type: function(val, rowObj) {
                if (val) return val;
                var res = rowObj['거래일자'] + rowObj['거래시간'] + rowObj['승인번호'];
                return res.replace(/[-:]/gi, "");
            }
        },
        vanTrxId: {
            name: '매입사거래번호',
            type: function(val, rowObj) {
                if (val) return val;
                var res = rowObj['거래일자'] + rowObj['거래시간'] + rowObj['승인번호'];
                return res.replace(/[-:]/gi, "");
            }
        },
        cardType: {
            name: '카드구분',
            type: ''
        },
        rootTrxDay: {
            name: '원거래일자',
            type: 'date'
        }
    },
    KCPOFF: {
        range: 0,
        valid: true,
        amount: {
            name: '합계',
            type: 'number'
        },
        trnType: {
            name: '거래구분',
            type: function(val) {
                var res = '';
                if (val === '승인') res = '승인';
                else if (val === '취소' || val === '망취소') res = '승인취소';

                return res;
            }
        },
        installment: {
            name: '할부',
            type: function(val) {
                return pad(val, 2);
            }
        },
        bin: {
            name: '카드번호',
            type: 'left 6'
        },
        last4: {
            name: '카드번호',
            type: 'right 4'
        },
        authCd: {
            name: '승인번호',
            type: ''
        },
        trxDay: {
            name: '거래일시',
            type: 'datetime to date'
        },
        trxTime: {
            name: '거래일시',
            type: 'datetime to time'
        },
        trackId: {
            name: '',
            type: function(rowObj) {
                return $('#tmnId').val() + rowObj['거래일시'].replace(/[. :-]/gi, "") + rowObj['승인번호'];
            }
        },
        vanTrxId: {
            name: '',
            type: function(rowObj) {
                return $('#tmnId').val() + rowObj['거래일시'].replace(/[. :-]/gi, "") + rowObj['승인번호'];
            }
        },
        cardType: {
            name: '카드구분',
            type: function(val) {
                return val.replace('카드', '');
            }
        },
        rootTrxDay: {
            name: '원거래일시',
            type: 'datetime to date'
        },
        /* ,
            vanStlFee: {
              name: '수수료',
              type: ''
            },
            vanDay: {
              name: '입금예정일',
              type: 'date'
            } */
    },
    NICEOFF: {
        range: -1,
        valid: true,
        amount: {
            name: '금액',
            type: 'number'
        },
        trnType: {
            name: '구분',
            type: function(val) {
                val = val.replace(/ /gi, '');
                return (val === '승인') ? '승인' : (val === '취소') ? '취소' : '';
            }
        },
        installment: {
            name: '할부',
            type: function(val) {
                return pad(val, 2);
            }
        },
        bin: {
            name: '카드번호',
            type: 'left 6'
        },
        last4: {
            name: '카드번호',
            type: 'right 4'
        },
        authCd: {
            name: '승인번호',
            type: ''
        },
        trxDay: {
            name: '거래일자',
            type: 'date'
        },
        trxTime: {
            name: '거래시간',
            type: 'time'
        },
        trackId: {
            name: '',
            type: function(rowObj) {
                return rowObj['거래일자'].replace(/[\/]/gi, "") + rowObj['거래시간'].replace(/[:]/gi, "") + rowObj['일련번호'];
            }
        },
        vanTrxId: {
            name: '',
            type: function(rowObj) {
                return rowObj['거래일자'].replace(/[\/]/gi, "") + rowObj['거래시간'].replace(/[:]/gi, "") + rowObj['일련번호'];
            }
        },
        cardType: {
            name: '카드종류',
            type: function(val) {
                return val === 'Normal' ? '신용' : '체크';
            }
        },
        rootTrxDay: {
            name: '원거래일자',
            type: 'date'
        }
    },
    DAOUOFF: { // 다우데이터
        range: 0,
        valid: true,
        amount: {
            name: '요청금액',
            type: 'number'
        },
        trnType: {
            name: '구분',
            type: function(val) {
                var res = '';
                val = val.replace(/ /gi, '');
                if (val === '승인' || val === '승인(취소)') res = '승인';
                else if (val === '취소') res = '승인취소';
                return res;
            }
        },
        installment: {
            name: '할부',
            type: function(val) {
                return pad(val, 2);
            }
        },
        bin: {
            name: '카드번호',
            type: 'left 6'
        },
        last4: {
            name: '카드번호',
            type: 'right 4'
        },
        authCd: {
            name: '승인번호',
            type: function(val, rowObj, formatObj) {
                if (val === '') val = rowObj['원승인번호'];
                return val;
            }
        },
        trxDay: {
            name: '거래일자',
            type: 'date14'
        },
        trxTime: {
            name: '거래시간',
            type: 'time'
        },
        trackId: {
            name: '',
            type: function(rowObj) {
                var res = rowObj['거래일자'] + rowObj['거래시간'] + rowObj['승인번호'];
                return res.replace(/[:|\/]/gi, "");
            }
        },
        vanTrxId: {
            name: '',
            type: function(rowObj) {
                var res = rowObj['거래일자'] + rowObj['거래시간'] + rowObj['승인번호'];
                return res.replace(/[:|\/]/gi, "");
            }
        },
        cardType: {
            name: '카드구분',
            type: ''
        },
        rootTrxDay: {
            name: '원거래일자',
            type: function(val) {
                var res = '';
                val = val.replace(/ /gi, '');
                if (val === '0000/00/00') {
                    res = '';
                } else if (/^\d{1,2}\/\d{1,2}\/\d{1,2}$/.test(val)) {
                    var valSplit = val.split('/');
                    res = '20' + valSplit[2] + '' + pad(valSplit[0], 2) + '' + pad(valSplit[1], 2);
                } else {
                    res = val.replace(/-/gi, "");
                }
                return res;
            }
        }
    },
    KSCOFF: { // 머니가이드
        range: 3,
        valid: true,
        amount: {
            name: '승인금액',
            type: 'number'
        },
        trnType: {
            name: '승인금액',
            type: function(val) {
                var res = '';
                if (typeof val === 'number') {
                    if (val > 0) res = '승인';
                    else if (val < 0) res = '승인취소';
                }
                return res;
            }
        },
        installment: {
            name: '',
            type: function() {
                return '00'
            }
        },
        bin: {
            name: '카드번호',
            type: 'left 6'
        },
        last4: {
            name: '카드번호',
            type: 'right 4'
        },
        authCd: {
            name: '승인번호',
            type: ''
        },
        trxDay: {
            name: '승인일',
            type: ''
        },
        trxTime: {
            name: '승인시간',
            type: function() {
                return '000000'
            } // time
        },
        trackId: {
            name: '',
            type: function(rowObj) {
                return rowObj['승인일'] + rowObj['승인시간'] + rowObj['승인번호'];
            }
        },
        vanTrxId: {
            name: '',
            type: function(rowObj) {
                return rowObj['승인일'] + rowObj['승인시간'] + rowObj['승인번호'];
            }
        },
        cardType: {
            name: '카드종류',
            type: function(val) {
                return val === '일반' ? '신용' : '체크';
            }
        },
        rootTrxDay: {
            name: '원거래일',
            type: ''
        }
    },
    KISOFF: { // 샘플러스
        range: 3,
        valid: true,
        amount: {
            name: '거래금액',
            type: 'number'
        },
        trnType: {
            name: '거래유형',
            type: function(val) {
                return (typeof val === 'string' && val.indexOf('취소') > -1) ? '승인취소' : val === '승인' ? val : '';
            }
        },
        installment: {
            name: '할부',
            type: ''
        },
        bin: {
            name: '카드번호',
            type: 'left 6'
        },
        last4: {
            name: '카드번호',
            type: 'right 4'
        },
        authCd: {
            name: '승인번호',
            type: ''
        },
        trxDay: {
            name: '거래일자',
            type: 'date'
        },
        trxTime: {
            name: '거래시간',
            type: 'time'
        },
        trackId: {
            name: '',
            type: function(rowObj) {
                var res = rowObj['거래일자'] + rowObj['거래시간'] + rowObj['승인번호'];
                return res.replace(/[- :|\/]/gi, "");
            }
        },
        vanTrxId: {
            name: '',
            type: function(rowObj) {
                var res = rowObj['거래일자'] + rowObj['거래시간'] + rowObj['승인번호'];
                return res.replace(/[- :|\/]/gi, "");
            }
        },
        cardType: {
            name: '체크',
            type: function(val) {
                if (val === 'Y') return '체크';
                else return '신용';
            }
        },
        rootTrxDay: {
            name: '원거래일자',
            type: 'date'
        },
        vanDay: {
            name: '입금예정일자',
            type: 'date'
        },
        vanStlFee: {
            name: '수수료',
            type: 'number'
        }
    },
    KICCOFF: { // 이지샵
        range: 0,
        valid: true,
        amount: {
            name: '금액',
            type: 'number'
        },
        trnType: {
            name: '승인구분',
            type: function(val) {
                return (typeof val === 'string' && val.indexOf('취소') > -1) ? '승인취소' : val === '승인' ? val : '';
            }
        },
        installment: {
            name: '할부개월',
            type: function(val) {
                if (val === '일시불') return '00';
                if (typeof val == 'string') val = val.replace('개월', '');
                return pad(val, 2);
            }
        },
        bin: {
            name: '카드번호',
            type: 'left 6'
        },
        last4: {
            name: '카드번호',
            type: 'right 4'
        },
        authCd: {
            name: '승인번호',
            type: ''
        },
        trxDay: {
            name: '거래일시▼',
            type: 'datetime to date'
        },
        trxTime: {
            name: '거래일시▼',
            type: 'datetime to time'
        },
        trackId: {
            name: '',
            type: function(rowObj) {
                return rowObj['거래일시▼'].replace(/[- :|\/]/gi, "") + rowObj['승인번호'];
            }
        },
        vanTrxId: {
            name: '',
            type: function(rowObj) {
                return rowObj['거래일시▼'].replace(/[- :|\/]/gi, "") + rowObj['승인번호'];
            }
        },
        cardType: {
            name: '카드구분',
            type: ''
        },
        rootTrxDay: {
            name: '원승인일자',
            type: 'date'
        }
    },
    KOCESOFF: {
        range: 0,
        valid: true,
        amount: {
            name: '총승인금액',
            type: 'number'
        },
        trnType: {
            name: '거래결과',
            type: function(val) {
                return val === '정상취소' ? '승인취소' : val === '정상승인' ? '승인' : '';
            }
        },
        installment: {
            name: '할부',
            type: ''
        },
        bin: {
            name: '카드번호',
            type: 'left 6'
        },
        last4: {
            name: '카드번호',
            type: 'right 4'
        },
        authCd: {
            name: '승인번호',
            type: ''
        },
        trxDay: {
            name: '승인일시',
            type: 'datetime to date'
        },
        trxTime: {
            name: '승인일시',
            type: 'datetime to time'
        },
        trackId: {
            name: '',
            type: function(rowObj) {
                var res = rowObj['승인일시'] + rowObj['거래일련번호'];
                return res.replace(/[- :|\/]/gi, "");
            }
        },
        vanTrxId: {
            name: '',
            type: function(rowObj) {
                var res = rowObj['승인일시'] + rowObj['거래일련번호'];
                return res.replace(/[- :|\/]/gi, "");
            }
        },
        cardType: {
            name: '카드종류',
            type: function(val) {
                return val.indexOf('체크') > -1 ? '체크' : val.indexOf('신용') > -1 ? '신용' : '';
            }
        },
        rootTrxDay: {
            name: '원거래일',
            type: 'date'
        }
    },
    KSNET: {
        range: 0,
        valid: true,
        tmnId: {
            name: '상점ID',
            type: ''
        },
        amount: {
            name: '금액',
            type: 'number'
        },
        trnType: {
            name: '승인구분',
            type: function(val) {
                var res = '';
                if (val === '승인')
                    res = '승인';
                else if (val === '취소')
                    res = '승인취소';
                return res;
            }
        },
        installment: {
            name: '할부',
            type: function(val) {
                return pad(val, 2);
            }
        },
        bin: {
            name: '카드번호',
            type: 'left 6'
        },
        last4: {
            name: '카드번호',
            type: 'right 4'
        },
        authCd: {
            name: '승인번호',
            type: ''
        },
        trxDay: {
            name: '승인일자',
            type: 'datetime to date'
        },
        trxTime: {
            name: '승인일자',
            type: 'datetime to time'
        },
        trackId: {
            name: '',
            type: function(rowObj) {
                var res = rowObj['승인일자'] + rowObj['승인번호'];
                return res.replace(/[-: ]/gi, "");
            }
        },
        vanTrxId: {
            name: '거래번호',
            type: ''
        },
        cardType: {
            name: '카드구분',
            type: function(val) {
                return val == '신용카드' ? '신용' : '체크';
            }
        },
        rootTrxDay: {
            name: '취소일자',
            type: 'datetime to date'
        }
    },
    CFAOFF: {
        range: 2,
        valid: true,
        amount: {
            name: '승인금액',
            type: 'number'
        },
        trnType: {
            name: '구분',
            type: function(val) {
                var res = '';
                if (val === '승인')
                    res = '승인';
                else if (val === '취소')
                    res = '승인취소';
                return res;
            }
        },
        installment: {
            name: '할부기간',
            type: function(val) {
                if (val === '일시불') val = '00';
                else if (val === '개월') val = '00';
                else val = val.replace('개월', '');
                return val;
            }
        },
        bin: {
            name: '카드번호',
            type: 'left 6'
        },
        last4: {
            name: '카드번호',
            type: 'right 4'
        },
        authCd: {
            name: '승인번호',
            type: ''
        },
        trxDay: {
            name: '거래일자',
            type: 'date'
        },
        trxTime: {
            name: '거래시간',
            type: 'time'
        },
        trackId: {
            name: '',
            type: function(rowObj) {
                var res = rowObj['거래일자'] + rowObj['거래시간'] + rowObj['승인번호'];
                return res.replace(/[-: ]/gi, "");
            }
        },
        vanTrxId: {
            name: '',
            type: function(rowObj) {
                var res = rowObj['거래일자'] + rowObj['거래시간'] + rowObj['승인번호'];
                return res.replace(/[-: ]/gi, "");
            }
        },
        rootTrxDay: {
            name: '거래일자',
            type: 'date'
        }
    },
    SBANK: {
        range: 19,
        valid: true,
        amount: {
            name: '금액',
            type: 'number'
        },
        trnType: {
            name: '거래상태',
            type: function(val, rowObj) {
                if(rowObj['취소일시'] != '') return '승인취소';
                if (val === '주문승인거절') return '';
                else return '승인';
            }
        },
        installment: {
            name: '할부기간',
            type: ''
        },
        bin: {
            name: '카드/계좌/휴대폰번호',
            type: 'left 6'
        },
        last4: {
            name: '카드/계좌/휴대폰번호',
            type: 'right 4'
        },
        authCd: {
            name: '승인번호',
            type: function(val) {
            	return val;
            }
        },
        trxDay: {
            name: '거래일자',
            type: function(val, rowObj) {
                if(rowObj['취소일시'] != '') {
                    return rowObj['취소일시'].replace(/[. :-|\/]/gi, "").substring(0, 8);
                } else {
                    return val.replace(/[. :-|\/]/gi, "").substring(0, 8);
                }
            }
        },
        trxTime: {
            name: '거래시간',
            type: function(val, rowObj) {
                if(rowObj['취소일시'] != '') {
                    return rowObj['취소일시'].replace(/[. :-|\/]/gi, "").substring(8, 14);
                } else {
                    return val.replace(/:/gi, "");
                }
            }
        },
        trackId: {
            name: '',
            type: function(rowObj) {
                var res = rowObj['주문번호'] + rowObj['거래일자'] + rowObj['거래시간'];
                return res.replace(/[- .:|\/]/gi, "");
            }
        },
        vanTrxId: {
            name: '거래번호',
            type: ''
        },
        cardType: {
            name: '카드구분',
            type: ''
        },
        rootTrxDay: {
            name: '거래일자',
            type: function(val, rowObj) {
                if(rowObj['취소일시'] != '') {
                    return val.replace(/[.]/gi, "");
                } else {
                    return '';
                }
            }
        },
        issuer: {
        	name: '카드종류/은행/통신사',
        	type:''
        },
        acquirer: {
        	name: '매입사',
        	type:''
        }
    },
    IDM: {
        range: 39,
        valid: true,
        amount: {
            name: '금액',
            type: 'number'
        },
        trnType: {
            name: '승인구분',
            type: function(val) {
                return val === '취소' ? '승인취소' : val === '승인' ? '승인' : '';
            }
        },
        installment: {
            name: '',
            type: function(rowObj) {
                return '00';
            }
        },
        bin: {
            name: '신용카드번호',
            type: 'left 6'
        },
        last4: {
            name: '신용카드번호',
            type: 'right 4'
        },
        authCd: {
            name: '승인번호',
            type: function(val) {
            	return val;
            }
        },
        trxDay: {
            name: '거래일시',
            type: function(val, rowObj) {
                if(rowObj['취소일시'] != '') {
                    return rowObj['취소일시'].replace(/[. :\-|\/]/gi, "").substring(0, 8);
                } else {
                    return val.replace(/[. :\-|\/]/gi, "").substring(0, 8);
                }
            }
        },
        trxTime: {
            name: '거래일시',
            type: function(val, rowObj) {
                if(rowObj['취소일시'] != '') {
                    return rowObj['취소일시'].replace(/[. :\-|\/]/gi, "").substring(8, 14);
                } else {
                    return val.replace(/[. :\-|\/]/gi, "").substring(8, 14);
                }
            }
        },
        trackId: {
            name: '',
            type: function(rowObj) {
                var res = rowObj['거래번호'] + rowObj['거래일시'];
                return res.replace(/[. :\-|\/]/gi, "");
            }
        },
        vanTrxId: {
            name: '거래번호',
            type: ''
        },
        cardType: {
            name: '',
            type: ''
        },
        rootTrxDay: {
            name: '거래일시',
            type: function(val, rowObj) {
                if(rowObj['취소일시'] != '') {
                    return val.replace(/[. :\-|\/]/gi, "").substring(0, 8);
                } else {
                    return '';
                }
            }
        },
        issuer: {
        	name: '카드발급사',
        	type:''
        },
        acquirer: {
        	name: '카드매입사',
        	type:''
        }
    },
    PAYNURIOFF: {
    	range: 0,
        valid: true,
        amount: {
            name: '거래금액',
            type: 'number'
        },
        trnType: {
            name: '승인구분',
            type: function(val) {
                var res = '';
                if (val === '승인')
                    res = '승인';
                else if (val.match('취소'))
                    res = '승인취소';
                return res;
            }
        },
        installment: {
            name: '할부',
            type: function(val) {
                return pad(val, 2);
            }
        },
        bin: {
            name: '카드번호',
            type: 'left 6'
        },
        last4: {
            name: '카드번호',
            type: 'right 4'
        },
        authCd: {
            name: '승인번호',
            type: ''
        },
        trxDay: {
            name: '거래일시',
            type: 'datetime to date'
        },
        trxTime: {
            name: '거래일시',
            type: 'datetime to time'
        },
        trackId: {
            name: '',
            type: function(rowObj) {
                var res = rowObj['거래일시'] + rowObj['승인번호'];
                return res.replace(/[-: ]/gi, "");
            }
        },
        vanTrxId: {
            name: '',
            type: function(rowObj) {
                var res = rowObj['거래일시'] + rowObj['승인번호'];
                return res.replace(/[-: ]/gi, "");
            }
        },
        rootTrxDay: {
            name: '원거래일자',
            type: 'date'
        },
        cardType: {
            name: '카드유형',
            type: function(val) {
            	return val.replace('카드', '');
            }
        },
        issuer: {
        	name: '카드사',
        	type: function(val) {
        		return val.replace(/ /gi, '');
        	}
        },
        acquirer: {
        	name: '매입사',
        	type: function(val) {
        		return val.replace(/ /gi, '');
        	}
        }
    },
    PAYNURION: {
    	range: 0,
        valid: true,
        amount: {
            name: '거래금액',
            type: 'number'
        },
        trnType: {
            name: '거래구분',
            type: function(val) {
                var res = '';
                if (val === '승인')
                    res = '승인';
                else if (val.match('취소'))
                    res = '승인취소';
                return res;
            }
        },
        installment: {
            name: '할부',
            type: function(val) {
                return pad(val, 2);
            }
        },
        bin: {
            name: '카드번호',
            type: 'left 6'
        },
        last4: {
            name: '카드번호',
            type: 'right 4'
        },
        authCd: {
            name: '승인번호',
            type: ''
        },
        trxDay: {
            name: '거래일자',
            type: 'date'
        },
        trxTime: {
            name: '거래시간',
            type: 'time'
        },
        trackId: {
            name: '',
            type: function(rowObj) {
                var res = rowObj['거래일자'] + rowObj['거래시간'] +rowObj['승인번호'];
                return res.replace(/[-: ]/gi, "");
            }
        },
        vanTrxId: {
            name: '',
            type: function(rowObj) {
                var res = rowObj['거래번호'];
                return res.replace(/[-: ]/gi, "");
            }
        },
        rootTrxDay: {
            name: '원거래일자',
            type: 'date'
        },
        cardType: {
            name: '',
            type: ''
        },
        issuer: {
        	name: '발급사',
        	type: function(val) {
        		return val.replace(/ /gi, '');
        	}
        },
        acquirer: {
        	name: '매입사',
        	type: function(val) {
        		return val.replace(/ /gi, '');
        	}
        }
    },
    DAOUPAYOFF: { // 다우페이
        range: 1,
        valid: true,
        amount: {
            name: '결제금액',
            type: function(val, obj) {
            	return obj['취소금액'] == '' ? Number(val.replace(/[^0-9]/gi, '')) : Number(obj['취소금액'].replace(/[^0-9]/gi, '')); 
            }
        },
        trnType: {
            name: '결제상태',
            type: function(val) {
                var res = '';
                val = val.replace(/ /gi, '');
                if (val === '승인취소') res = '승인취소';
                else if (val === '승인성공') res = '승인';
                return res;
            }
        },
        installment: {
            name: '할부(무이자)',
            type: function(val) {
            	if(val == '일시불') {
            		return '00';
            	} else {
            		return val.substring(0,2);
            	}
            }
        },
        bin: {
            name: '카드번호',
            type: 'left 6'
        },
        last4: {
            name: '카드번호',
            type: 'right 4'
        },
        authCd: {
            name: '승인번호',
            type: ''
        },
        trxDay: {
            name: '결제일시',
            type: function(val, rowObj) {
                if(rowObj['취소일시'] != '') {
                    return rowObj['취소일시'].replace(/[. :\-|\/]/gi, "").substring(0, 8);
                } else {
                	function padLeft(val) {
                		return (Number(val) < 10 && String(val).length < 2) ? '0' + val : val;
                	}
                	var arr = val.split(' ')[0].split('/');
                	var date = '20' + padLeft(arr[2]) + padLeft(arr[0]) + padLeft(arr[1])
                    return date;
                }
            }
        },
        trxTime: {
            name: '결제일시',
            type: function(val, rowObj) {
                if(rowObj['취소일시'] != '') {
                    return rowObj['취소일시'].replace(/[^0-9]/gi, "").substring(8, 14);
                } else {
                	function padLeft(val) {
                		return (Number(val) < 10 && String(val).length < 2) ? '0' + val : val;
                	}
                	var arrTime = val.split(' ')[1].split(':');
                	var time = padLeft(arrTime[0]) + padLeft(arrTime[1]) + '00'
                    return time;
                }
            }
        },
        trackId: {
            name: '주문번호',
            type: ''
        },
        vanTrxId: {
            name: '거래번호',
            type: ''
        },
        cardType: {
            name: '',
            type: ''
        },
        rootTrxDay: {
            name: '결제일시',
            type: function(val, rowObj) {
                if(rowObj['취소일시'] != '') {
                	function padLeft(val) {
                		return (Number(val) < 10 && String(val).length < 2) ? '0' + val : val;
                	}
                	var arr = val.split(' ')[0].split('/');
                	var date = '20' + padLeft(arr[2]) + padLeft(arr[0]) + padLeft(arr[1])
                    return date;
                } else {
                    return '';
                }
            }
        },
        issuer: {
        	name: '카드종류',
        	type:''
        },
        acquirer: {
        	name: '',
        	type:''
        }
    },
    WOORIPAY: { // 우리페이
        range: 0,
        valid: true,
        amount: {
            name: '결제금액',
            type: 'number'
        },
        trnType: {
            name: '결제수단',
            type: function(val) {
                var res = '';
                val = val.replace(/ /gi, '');
                if (val === '카드취소') res = '승인취소';
                else if (val === '카드승인') res = '승인';
                return res;
            }
        },
        installment: {
            name: '할부구분',
            type: function(val) {
            	return pad(val, 2);
            }
        },
        bin: {
            name: '',
            type: ''
        },
        last4: {
            name: '카드번호',
            type: 'right 4'
        },
        authCd: {
            name: '승인번호',
            type: function(val) {
            	return $.trim(val);
            }
        },
        trxDay: {
            name: '승인일',
            type: function(val, rowObj) {
                if(rowObj['취소일'] != '') {
                    return '20' + rowObj['취소일'].replace(/[^0-9]/gi, "");
                } else {
                	return val.replace(/[^0-9]/gi, "").substring(0,8);
                }
            }
        },
        trxTime: {
            name: '승인일',
            type: function(val, rowObj) {
            	if(rowObj['취소일'] != '') {
                    return '000000';
                } else {
                	return val.replace(/[^0-9]/gi, "").substring(8,14);
                }
            }
        },
        trackId: {
            name: '',
            type: function(rowObj) {
                var res = rowObj['승인일'].replace(/[^0-9]/gi, "") + rowObj['승인번호'];
                return res.replace(/[-: ]/gi, "");
            }
        },
        vanTrxId: {
            name: '',
            type: function(rowObj) {
            	var res = rowObj['승인일'].replace(/[^0-9]/gi, "") + rowObj['승인번호'];
                return res.replace(/[-: ]/gi, "");
            }
        },
        cardType: {
            name: '',
            type: ''
        },
        rootTrxDay: {
            name: '승인일',
            type: function(val, rowObj) {
                if(rowObj['취소일'] != '') {
                	return val.replace(/[^0-9]/gi, "").substring(0,8);
                } else {
                	return '';
                }
            }
        },
        issuer: {
        	name: '매입사',
        	type:''
        },
        acquirer: {
        	name: '매입사',
        	type:''
        }
    }
}

function pad(n, width) {
    n = n + '';
    return n.length >= width ? n : new Array(width - n.length + 1).join('0') + n;
}

// 어레이 버퍼를 처리한다 ( 오직 readAsArrayBuffer 데이터만 가능하다 )
function fixdata(data) {
    var o = "",
        l = 0,
        w = 10240;
    for (; l < data.byteLength / w; ++l) o += String.fromCharCode.apply(null, new Uint8Array(data.slice(l * w, l * w +
        w)));
    o += String.fromCharCode.apply(null, new Uint8Array(data.slice(l * w)));
    return o;
}

// 데이터를 바이너리 스트링으로 얻는다.
function getConvertDataToBin($data) {
    var arraybuffer = $data;
    var data = new Uint8Array(arraybuffer);
    var arr = new Array();
    for (var i = 0; i != data.length; ++i) arr[i] = String.fromCharCode(data[i]);
    var bstr = arr.join("");

    return bstr;
}

function handleFile(e) {
    var files = e.target.files;
    var i, f;
    for (i = 0; i != files.length; ++i) {
        f = files[i];
        var reader = new FileReader();
        var name = f.name;

        reader.onload = function(e) {
            var data = e.target.result;
            var json = {};
            var formatObj = '';
            var selectVan = $('select.van').val();
            var formatObj = vanFormat[selectVan];
            console.log(selectVan, formatObj);
            if (!selectVan || !formatObj) {
                bootbox.alert('VAN을 선택해야 합니다.');
                return;
            }
            if (name.indexOf('.csv') > -1) {
                if(selectVan != 'KSNET') {
                    bootbox.alert('사용 할 수 없는 포맷입니다.');
                    return;
                }
                json = hnadleCSV(data);
                
            } else if(selectVan == 'SBANK' || selectVan == 'IDM') {
                var orgRows = $(data)[formatObj.range] // 19, 39
                orgRows = $(orgRows).find('tr');
                var header = [];
                $(orgRows).each(function(i , e) {
                    var row = {};
                    $(orgRows[i]).children('th, td').each(function(j , e) {
                        if(i == 0) {
                            header.push($(e).text());
                        } else {
                            row[header[j]] = $(e).text().replace(/\n/g, '').replace(/\t/g, '').replace(/ /g, '');
                        }
                    });
                    if(i > 0) {
                        json[i-1] = row;
                    }
                });
            } else {
                var workbook;
                if (rABS) {
                    /* if binary string, read with type 'binary' */
                    workbook = XLSX.read(data, {
                        type: 'binary'
                    });
                } else {
                    /* if array buffer, convert to base64 */
                    var arr = fixdata(data);
                    workbook = XLSX.read(btoa(arr), {
                        type: 'base64'
                    });
                } //end. if
                
                /* 워크북 처리 */
                workbook.SheetNames.forEach(function(item, index, array) {
                    if (index == 0) {
                        var sheet = workbook.Sheets[item];

                        if (selectVan === 'NICEOFF') {
                            $.each(sheet, function(key, value) {
                                if (value.v === 'No.') {
                                    formatObj.range = Number(key.replace('A', '')) - 1;
                                    return false;
                                }
                            });
                        }

                        json = XLSX.utils.sheet_to_json(sheet, {
                            range: formatObj.range,
                            raw: false,
                            defval: ''
                        });
                    }
                }); //end. forEach
            }
            parseExcelJson(json, formatObj);
        }; //end onload

        if ($('select.van').val() === 'KSNET' || $('select.van').val() === 'IDM') reader.readAsText(f, 'EUC-KR');
        else if ($('select.van').val() === 'SBANK') reader.readAsText(f, 'UTF-8');
        else if (rABS) reader.readAsBinaryString(f);
        else reader.readAsArrayBuffer(f);

    } //end. for
}

function hnadleCSV(data) {
    var row = data.split('\n');
    var header = [];
    var resObj = [];
    for (i in row) {
        var columns = row[i].split(',');
        if (i == 0) {
            for (j in columns) {
                header.push(columns[j]);
            }
        } else if (columns.length > 1) {
            var rowObj = {};
            for (j in columns) {
                var val = columns[j];
                if (/^="[0-9]*"$/.test(val)) {
                    val = val.replace('="', '');
                    val = val.replace('"', '');
                }
                rowObj[header[j]] = val;
            }
            resObj.push(rowObj);
        }
    }
    return resObj;
}

function parseExcelJson(orgJson, formatObj) {
    resultArray = [];
	console.log(orgJson, formatObj);
    var validResult = validExcelObject(formatObj, orgJson);
    if (!validResult || !validResult.result) {
        bootbox.alert(validResult.msg);
        return;
    }

    $.each(orgJson, function(i, rowObj) {
        if (($('select.van').val() === 'NICEOFF' || $('select.van').val() === 'KOCESOFF') && i === 0) {
            //console.log('skip', rowObj);
            return true;
        }
        var eachObj = parseRow(formatObj, rowObj);
        if (eachObj != undefined) {
            // 승인취소 에 대한 승인 내역 생성
            if($('select.van').val() === 'SBANK' || $('select.van').val() === 'IDM' 
            	|| $('select.van').val() === 'DAOUPAYOFF' || $('select.van').val() === 'WOORIPAY') {
            	if(eachObj.trnType == '승인취소') {
            		resultArray.push(makeOrgTran(eachObj, rowObj));
            	}
            }
            resultArray.push(eachObj);
        }
    });

    if ($('select.van').val() === 'KSNET') {
        checkTmnId(resultArray);
    }
    console.log(resultArray);
    
    importTable(resultArray);
    importSUM(resultArray);
    textMask();
}

function makeOrgTran(eachObj, rowObj) {
        var orgObj = $.extend(true, {}, eachObj);
        orgObj.trnType = '승인';
        orgObj.rootTrxDay = '';
        if($('select.van').val() === 'SBANK') {
            orgObj.trxDay = rowObj['거래일자'].replace(/[.]/gi, '')
            orgObj.trxTime = rowObj['거래시간'].replace(/:/gi, '');
        } else if($('select.van').val() === 'DAOUPAYOFF') {
        	var val = rowObj['결제일시'];
        	function padLeft(val) {
        		return (Number(val) < 10 && String(val).length < 2) ? '0' + val : val;
        	}
        	var arr = val.split(' ')[0].split('/');
        	var arrTime = val.split(' ')[1].split(':');
        	orgObj.trxDay = '20' + padLeft(arr[2]) + padLeft(arr[0]) + padLeft(arr[1])
        	orgObj.trxTime = padLeft(arrTime[0]) + padLeft(arrTime[1]) + '00'
        } else if($('select.van').val() === 'WOORIPAY') {
        	orgObj.trxDay = rowObj['승인일'].replace(/[^0-9]/gi, "").substring(0, 8);
        	orgObj.trxTime = rowObj['승인일'].replace(/[^0-9]/gi, "").substring(8, 14);
        } else {
            orgObj.trxDay = rowObj['거래일시'].replace(/[. :\-|\/]/gi, "").substring(0, 8);
            orgObj.trxTime = rowObj['거래일시'].replace(/[. :\-|\/]/gi, "").substring(8, 14);
        }
        return orgObj;
}

function parseRow(formatObj, rowObj) {
    var resultObj = {};
    //console.log('rowObj:', rowObj);
    var emptyFlag = false;
    $.each(rowObj, function(k, v) {
        if (v != '' && v != undefined) {
            emptyFlag = true;
        }
    });

    if (!emptyFlag) {
        return undefined;
    }

    $.each(formatObj, function(key, value) {
        var eachVal = rowObj[value.name];
        resultObj.valid = formatObj.valid;
        if (resultObj.tmnId) {

        } else {
            resultObj.mchtId = $('#mchtId').val();
            resultObj.tmnId = $('#tmnId').val();
        }

        if (eachVal) {
            if (value.type === '') {
                eachVal = eachVal;
            } else if (jQuery.isFunction(value.type)) {
                eachVal = value.type(eachVal, rowObj, formatObj, $('select.van').val());
            } else if (value.type === 'number') {
                if (typeof eachVal === 'string')
                    eachVal = Number(eachVal.replace(/[, ]/gi, ""));
            } else if (value.type === 'datetime to date') {
                eachVal = eachVal.replace(/[. :\-|\/]/gi, "").substring(0, 8);
            } else if (value.type === 'datetime to time') {
                eachVal = eachVal.replace(/[. :\-|\/]/gi, "").substring(8, 14);
            } else if (value.type === 'date') {
                eachVal = eachVal.replace(/[. :\-]|[\/]/gi, "");
                if (eachVal.length == 6) {
                    eachVal = '20' + eachVal;
                }
            } else if (value.type === 'date14') {
                var valArray = eachVal.split('/');
                eachVal = '20' + valArray[2] + pad(valArray[0], 2) + pad(valArray[1], 2);
            } else if (value.type === 'time') {
                eachVal = eachVal.replace(/[. :-]/gi, "");
                if (eachVal.length == 5) {
                    eachVal = '0' + eachVal;
                }
            } else if (value.type === 'left 6') {
                eachVal = eachVal.replace(/[-]/gi, "").substring(0, 6);
            } else if (value.type === 'right 4') {
                eachVal = $.trim(eachVal.replace(/[-]/gi, ""));
                eachVal = eachVal.substr(eachVal.length - 4);
            }
            resultObj[key] = eachVal;
        } else if (jQuery.isFunction(value.type)) {
            resultObj[key] = value.type(rowObj);
        }
    });
    //console.log('EACH: ', resultObj);
    resultObj = validExcelRow(resultObj);
    return resultObj;
}

function importTable(array) {
    var template = $('#list').children('tr')[0];
    $.each(array, function(i, row) {
        var tr = $(template).clone();
        $.each($(tr).children('td'), function(key, value) {
            $(value).text(row[value.className.split(' ')[0]]);
        });
        $(tr).find('.no').text(i + 1);
        $(tr).removeClass('hide');
        if (row.valid === false) {
            $(tr).addClass('font-red');
            $(tr).css('font-weight', '800');
        }
        $('#list').append(tr);
    });


}

function importSUM(array) {
    var capAmt = 0;
    var capCnt = 0;
    var rfdAmt = 0;
    var rfdCnt = 0;
    var expAmt = 0;
    var expCnt = 0;

    $.each(array, function(i, row) {
        if (row.valid && row.trnType === '승인') {
            capAmt += row.amount;
            capCnt++;
        } else if (row.valid && row.trnType === '승인취소') {
            rfdAmt += row.amount;
            rfdCnt++;
        } else {
            expAmt += row.amount;
            expCnt++;
        }
    });

    $('#cap-amt').text(capAmt);
    $('#cap-cnt').text(capCnt);
    $('#rfd-amt').text(rfdAmt);
    $('#rfd-cnt').text(rfdCnt);
    $('#exp-amt').text(expAmt);
    $('#exp-cnt').text(expCnt);
}

function validExcelObject(formatObj, obj) {
    var keys = [];
    var result = {
        result: true
    };

    if (!formatObj) return {
        result: false,
        msg: '선택하신 VAN의 Excel 정보가 지정되지 않았습니다.'
    };
    if (!obj) return {
        result: false,
        msg: '엑셀 정보를 가져오는데 실패했습니다.'
    };

    $.each(obj[0], function(key, value) {
        keys.push(key);
    });

    $.each(formatObj, function(key, value) {
        if (value.name) {
            if (keys.indexOf(value.name) == -1) {
                result = {
                    result: false,
                    msg: 'Excel 파일의 항목(헤더)이 올바르지 않습니다. (' + value.name + ')'
                };
                return false;
            }
        }
    });

    return result;
}

function validExcelRow(obj) {
    if (obj.trnType === '') {
        obj.valid = false;
    } else if (!obj.trnType || !obj.trackId || !obj.amount || !obj.installment || !obj.last4 || !obj.authCd || !obj.trxDay || !obj.trxTime) {
        obj.valid = false;
    } else if (obj.trnType == '' || obj.trackId == '' || obj.amount == '' || obj.installment == '' || obj.last4 == '' || obj.authCd == '' || obj.trxDay == '' || obj.trxTime == '') {
        obj.valid = false;
    }

    if ($('select.van').val() === 'KSNET') {
        if (!obj.tmnId || obj.tmnId.length < 5) {
            obj.valid = false;
        }
    }

    // 삭제 대상
    if ($('select.van').val() === 'KSCOFF') {
        if (obj.trackId === '총합계') return undefined;
    } else if ($('select.van').val() === 'KICCOFF') {
        if (obj.trackId === '') return undefined;
    }

    /* if(!obj.valid) {
      console.log('제외대상:', obj);
    } */
    return obj;
}

function checkTmnId(array) {
    var tmnIds = [];
    for (i in array) {
        if (tmnIds.indexOf(array[i].tmnId) == -1) {
            tmnIds.push(array[i].tmnId);
        }
    }
}

var input_dom_element;
$(function() {
    input_dom_element = document.getElementById('upload-excel');
    if (input_dom_element.addEventListener) {
        input_dom_element.addEventListener('change', handleFile, false);
    }
});

function importServer() {
    if (!resultArray || jQuery.isEmptyObject(resultArray)) {
        bootbox.alert('Excel 데이터를 불러와야 합니다.');
        return;
    }
    var requestArray = [];
    $.each(resultArray, function(index, value) {
        if (value.valid === true) {
            requestArray.push(value);
        }
    });
    console.log('Request Array:', requestArray);
    bootbox.confirm('Excel 거래 데이터를 업로드 하시겠습니까?', function(result) {
        if (result) {
            $.ajax({
                url: ($('select.van').val() != 'KSNET') ? '/trxoper/load/insert' : '/trxoper/load/multi/insert',
                dataType: "text",
                beforeSend: function(xhr) {
                    xhr.setRequestHeader("Content-type",
                        "application/json;charset=utf-8");
                },
                method: 'post',
                data: JSON.stringify(requestArray),
                success: function(res, stat) {
                    res = JSON.parse(res);
                    if (res.result == 'OK') {
                        bootbox.alert("Excel 거래 데이터 업로드에 성공하였습니다.", function() {
                            document.location.href = '/trxoper/load/form.jsp';
                        });
                    } else {
                        bootbox.alert("Excel 거래 데이터 업로드에 실패하였습니다! " + res.msg);
                    }
                },
                error: function(xhr, status, error) {
                    bootbox.alert("Excel 거래 데이터 업로드에 실패하였습니다.");
                }
            });
        }
    });
}
//http://sheetjs.com/
//https://github.com/SheetJS/js-xls