/**
 * Created by gorkemgok on 15/02/16.
 */
var constantsModule = angular.module("ayronaConstants",[]);

constantsModule.constant('REST', {
    //base : 'http://ata1:18050/ayrona/rest/v1'
    //base : 'http://localhost:18050/ayrona/rest/v1'
    //base : 'http://aol.ayrona.co/ayrona/rest/v1'
    base : 'http://atanatro:18050/ayrona/rest/v1'
});

constantsModule.constant('METRICS', [
    {key:"NET_PROFIT", label:"Net Kar"},
    {key:"GROSS_PROFIT", label:"Toplam Kar"},
    {key:"GROSS_LOSS", label:"Toplam Zarar"},
    {key:"MAX_TRADE_DRAWDOWN", label:"Max Drawdown"},
    {key:"TOTAL_NUMBER_OF_TRADES", label:"Toplam Posizyon"},
    {key:"WINNING_TRADE_COUNT", label:"Kar Eden Posizyon"},
    {key:"LOSING_TRADE_COUNT", label:"Zarar Eden Posizyon"},
    {key:"PROFITABLE_PERCENT", label:"Kar eden poz. oranı"},
    {key:"AVE_BARS_IN_WINNING_TRADES", label:"Ort. Kar eden poz. bar"},
    {key:"AVE_BARS_IN_LOSING_TRADES", label:"Ort. Zarar eden poz. bar"}
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
