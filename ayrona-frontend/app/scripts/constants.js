/**
 * Created by gorkemgok on 15/02/16.
 */
var constantsModule = angular.module("ayronaConstants",[]);

constantsModule.constant('REST', {
    //base : 'http://ata1:18050/ayrona/rest/v1'
    base : 'http://localhost:18050/ayrona/rest/v1'
    //base : 'http://aol.ayrona.co/ayrona/rest/v1'
    //base : 'http://atanatro:18050/ayrona/rest/v1'
    //base : 'http://172.16.192.77/ayrona/rest/v1'
});

constantsModule.constant('METRICS', [
    {key:"NET_PROFIT", label:"Net Kar", toFixed:2, percent :
        {key:"NET_PROFIT_PERCENTAGE", label:"Net Kar(%)", toFixed:2, suffix:"%"}},
    {key:"GROSS_PROFIT", label:"Toplam Kar", toFixed:2, percent:
        {key:"GROSS_PROFIT_PERCENTAGE", label:"Toplam Kar(%)", toFixed:2, suffix:"%"}},
    {key:"GROSS_LOSS", label:"Toplam Zarar", toFixed:2, percent:
        {key:"GROSS_LOSS_PERCENTAGE", label:"Toplam Zarar(%)", toFixed:2, suffix:"%"}},
    {key:"PROFIT_FACTOR", label:"Kar Faktörü", toFixed:2},

    {key:"TOTAL_NUMBER_OF_TRADES", label:"Toplam Posizyon", toFixed:0},
    {key:"WINNING_TRADE_COUNT", label:"Kar Eden Posizyon", toFixed:0, defaultColorClass:'positive-metric'},
    {key:"LOSING_TRADE_COUNT", label:"Zarar Eden Posizyon", toFixed:0, defaultColorClass:'negative-metric'},
    {key:"PROFITABLE_PERCENT", label:"Kar eden poz. oranı", toFixed:2, suffix:"%", level1:50, level2:70},

    {key:"AVE_TRADE_NET_PROFIT", label:"Ortalama Kar", toFixed:4, percent:
        {key:"AVE_TRADE_NET_PROFIT_PERCENTAGE", label:"Ortalama Kar(%)", toFixed:2, suffix:"%"}},
    {key:"AVE_WINNING_TRADE", label:"Ortalama Kazanan Kar", toFixed:4, defaultColorClass:'positive-metric', percent:
        {key:"AVE_WINNING_TRADE_PERCENTAGE", label:"Ortalama Kazanan Kar(%)", toFixed:2, suffix:"%", defaultColorClass:'positive-metric'}},
    {key:"AVE_LOSING_TRADE", label:"Ort. Kaybeden Zarar", toFixed:4, defaultColorClass:'negative-metric', percent:
        {key:"AVE_LOSING_TRADE_PERCENTAGE", label:"Ortalama Kaybeden Zarar(%)", toFixed:2, suffix:"%", defaultColorClass:'positive-metric'}},
    {key:"AVE_BARS_IN_WINNING_TRADES", label:"Ortalama Kar eden poz. bar", toFixed:0, defaultColorClass:'positive-metric'},
    {key:"AVE_BARS_IN_LOSING_TRADES", label:"Ortalama Zarar eden poz. bar", toFixed:0, defaultColorClass:'negative-metric'},

    {key:"MAX_TRADE_DRAWDOWN", label:"Max Drawdown", toFixed:2, percent:
        {key:"MAX_TRADE_DRAWDOWN_PERCENTAGE", label:"Max Drawdown(%)", toFixed:2, suffix:"%", defaultColorClass:'negative-metric'}},

    {key:"DAILY_STD", label:"Günlük Standart Sapma", toFixed:4, defaultColorClass:'notr-metric'},
    {key:"DAILY_RSQUARED", label:"Günlük Stabilite", toFixed:4, defaultColorClass:'notr-metric'},
    {key:"WEEKLY_STD", label:"Haftalık Standart Sapma", toFixed:4, defaultColorClass:'notr-metric'},
    {key:"WEEKLY_RSQUARED", label:"Haftalık Stabilite", toFixed:4, defaultColorClass:'notr-metric'},
    {key:"MONTHLY_STD", label:"Aylık Standart Sapma", toFixed:4, defaultColorClass:'notr-metric'},
    {key:"MONTHLY_RSQUARED", label:"Aylık Stabilite", toFixed:4, defaultColorClass:'notr-metric'},
    {key:"YEARLY_STD", label:"Yıllık Standart Sapma", toFixed:4, defaultColorClass:'notr-metric'},
    {key:"YEARLY_RSQUARED", label:"Yıllık Stabilite", toFixed:4, defaultColorClass:'notr-metric'}

]);

constantsModule.constant('PERIODS', [
    {text:'1 Dakika', value:'M1'},
    {text:'5 Dakika', value:'M5'},
    {text:'15 Dakika', value:'M15'},
    {text:'30 Dakika', value:'M30'},
    {text:'1 Saat', value:'H1'},
    {text:'4 Saat', value:'H4'},
    {text:'1 Gün', value:'D1'}
]);

constantsModule.constant('SYMBOLS', [
    {text:'VOB_XAUTRY', value:'VOB_XAUTRY'},
    {text:'VOB_X30', value:'VOB_X30'},
    {text:'VOB_USDTRY', value:'VOB_USDTRY'},
    {text:'FX_EURUSD', value:'FX_EURUSD'},
]);

constantsModule.constant('ACCOUNT_TYPE', {
    ata_custom : 'ATA_CUSTOM',
    mt4:"MT4"
});

constantsModule.constant('STRATEGY_STATE', {
    active : 'ACTIVE',
    inactive : 'INACTIVE'
});

constantsModule.constant('EDR', {
   strategy : {
       name:'STRATEGY',
       types:{
           start : 'START_STRATEGY',
           stop : 'STOP_STRATEGY',
           newSignal : 'NEW_SIGNAL',
           boundAccount : 'BOUND_ACCOUNT',
           unboundAccount : 'UNBOUND_ACCOUNT'
       }
   },
   account : {
       name: 'ACCOUNT',
       types: {
           start: 'START_ACCOUNT',
           stop: 'STOP_ACCOUNT',
           newPosition : 'NEW_POSITION'
       }
   }
});

constantsModule.constant('RESULT_PERIOD', {
    DAY : "Günlük",
    WEEK : "Haftalık",
    MONTH : "Aylık",
    YEAR : "Yıllık"
});
