/**
 * @function renderBarChart will Render a Bar Chart.
 * @param {JSON} data - JSON data as input to the chart, JSON format {key, value}
 * @param {HTML ID} chartDiv - Division which contains the rendered chart.
 * @return {chart} - chart will be returned by the function after rendering.
 */
function renderBarChart(data, chartDiv){
  nv.addGraph(function() {
    var chart = nv.models.discreteBarChart()
                .x(function(d) { return d.label })
                .y(function(d) { return d.value })
                .staggerLabels(true)
                .tooltips(true)
                .showValues(true)

    d3.select(chartDiv + ' svg')
      .datum(data)
      .transition().duration(1000)
      .call(chart)
      ;

    nv.utils.windowResize(chart.update);

    return chart;
  });
}

/**
 * @function renderPieChart will Render a Pie Chart.
 * @param {JSON} data - JSON data as input to the chart, JSON format {key, value}
 * @param {HTML ID} chartDiv - Division which contains the rendered chart.
 * @return {chart} chart- Rendered Chart.
 */
function renderPieChart(data, chartDiv){
  nv.addGraph(function() {
    var chart = nv.models.pieChart()
                  .x(function(d) { return d.label })
                  .y(function(d) { return d.value })
                  .showLabels(true)     //Display pie labels
                  .labelThreshold(.05)  //Configure the minimum slice size for labels to show up
                  .labelType("percent") //Configure what type of data to show in the label. Can be "key", "value" or "percent"
                  .donut(true)          //Turn on Donut mode. Makes pie chart look tasty!
                  .donutRatio(0.35)     //Configure how big you want the donut hole size to be.
                  ;

      d3.select(chartDiv+ ' svg')
        .datum(data)
        .transition().duration(1000)
        .call(chart)
        ;

    return chart;
  });
}

/**
 * @function renderBarChart will Render a Bar Chart.
 * @param {JSON} plottingData - JSON data as input to the chart, JSON format {key, value}
 * @param {HTML ID} chartDiv - Division which contains the rendered chart.
 * @return {chart} - chart will be returned by the function after rendering.
 */
function renderLinePlot(chartDiv){
  function plottingData() {
    var sin = [],
        cos = [];

    for (var i = 0; i < 100; i++) {
      sin.push({x: i, y: Math.sin(i/10)});
      cos.push({x: i, y: .5 * Math.cos(i/10)});
    }

    return [
      {
        values: sin,
        key: 'Sine Wave',
        color: '#ff7f0e'
      },
      {
        values: cos,
        key: 'Cosine Wave',
        color: '#2ca02c'
      }
    ];
  }

  nv.addGraph(function() {
    var chart = nv.models.lineChart()
      .useInteractiveGuideline(true)
      ;

    chart.xAxis
      .axisLabel('Label')
      .tickFormat(d3.format(',r'))
      ;

    chart.yAxis
      .axisLabel('Value')
      .tickFormat(d3.format('.02f'))
      ;

    d3.select(chartDiv+' svg')
      .datum(plottingData())
      .transition().duration(500)
      .call(chart)
      ;

    nv.utils.windowResize(chart.update);

    return chart;
  });
}

/**
 * @function renderBarChart will Render a Bar Chart.
 * @param {JSON} data - JSON data as input to the chart, JSON format {key, value}
 * @param {HTML ID} chartDiv - Division which contains the rendered chart.
 * @return {chart} - chart will be returned by the function after rendering.
 */
function renderScatterPlot(chartDiv){
  function data(groups, points) {
    var data = [],
        shapes = ['circle', 'cross', 'triangle-up', 'triangle-down', 'diamond', 'square'],
        random = d3.random.normal();

    for (i = 0; i < groups; i++) {
      data.push({
        key: 'Group ' + i,
        values: []
      });

      for (j = 0; j < points; j++) {
        data[i].values.push({
          x: random()
        , y: random()
        , size: Math.random()
        //, shape: shapes[j % 6]
        });
      }
    }

    return data;
  }

  nv.addGraph(function() {
    var chart = nv.models.scatterChart()
                  .showDistX(true)
                  .showDistY(true)
                  .color(d3.scale.category10().range());

    chart.xAxis.tickFormat(d3.format('.02f'));
    chart.yAxis.tickFormat(d3.format('.02f'));

    d3.select(chartDiv+' svg')
        .datum(data(4,40))
      .transition().duration(500)
        .call(chart);

    nv.utils.windowResize(chart.update);

    return chart;
  });
}
