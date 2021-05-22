function zkChart(title, data) {
    return {
        title: {
            text: title
        },
        grid: {
            left: '4%',
            right: '9%',
            bottom: 40,
            containLabel: true
        },
        toolbox: {
            feature: {
                dataZoom: {
                    yAxisIndex: 'none'
                },
                restore: {},
                saveAsImage: {}
            }
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                animation: false,
                label: {
                    backgroundColor: '#505765'
                }
            }
        },
        dataZoom: [
            {
                show: true,
                realtime: true,
                start: 0,
                end: 100
            },
            {
                type: 'inside',
                realtime: true,
                start: 0,
                end: 100
            }
        ],
        yAxis: {
            type: 'value'
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            axisLine: {onZero: false},
            data: data.times.map(function (str) {
                return str.replace(' ', '\n')
            })
        },
        series: data.series
    };
}