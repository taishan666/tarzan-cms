
$(function() {
    chartB();
});


/* Area Spaline Chart  */
function chartB() {
    var options = {
        chart: {
            height: 350,
            type: 'area',
            fontFamily: 'Poppins, sans-serif',
            toolbar: {
                show: false
            },
            zoom: {
                enabled: false
            },
        },
        dataLabels: {
            enabled: false
        },
        stroke: {
            curve: 'smooth'
        },
        series: [{
            name: 'Profit',
            data: [31, 40, 28, 51, 42, 109, 100]
        }, {
            name: 'Loss',
            data: [11, 32, 45, 32, 34, 52, 41]
        }],

        xaxis: {
            type: 'datetime',
            categories: ["2020-09-19T00:00:00", "2020-09-19T01:30:00", "2020-09-19T02:30:00", "2020-09-19T03:30:00", "2020-09-19T04:30:00", "2020-09-19T05:30:00", "2020-09-19T06:30:00"],
            labels: {
                style: {
                    colors: '#10163a',
                    background: '#10163a',
                    fontFamily: 'Poppins, sans-serif',
                }
            }
        },
        yaxis: {
            labels: {
                style: {
                    color: '#10163a',
                    fontFamily: 'Poppins, sans-serif',
                }
            }
        },
        tooltip: {
            x: {
                format: 'dd/MM/yy HH:mm'
            },
        },
        colors: ['#ffa000', '#11a0fd']
    }

    var chart = new ApexCharts(
        document.querySelector("#chartB"),
        options
    );

    chart.render();
}
