// function renderBarChart will render the bar chart
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
      .transition().duration(500)
      .call(chart)
      ;

    nv.utils.windowResize(chart.update);

    return chart;
  });
}
          
// function renderPieChart will render Pie Chart          
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
        .transition().duration(350)
        .call(chart)
        ;

    return chart;
  });
}

// function to render simple line plot
function renderLinePlot(plottingData, chartDiv){
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
      .datum(plottingData)
      .transition().duration(500)
      .call(chart)
      ;

    nv.utils.windowResize(chart.update);

    return chart;
  });
}


//function to render scatter plots
function renderScatterPlot(plottingData, chartDiv){
  nv.addGraph(function() {
    var chart = nv.models.scatterChart()
                  .showDistX(true)
                  .showDistY(true)
                  .color(d3.scale.category10().range());

    chart.xAxis.tickFormat(d3.format('.02f'));
    chart.yAxis.tickFormat(d3.format('.02f'));

    d3.select(chartDiv+' svg')
        .datum(plottingData)
      .transition().duration(500)
        .call(chart);

    nv.utils.windowResize(chart.update);

    return chart;
  });
}