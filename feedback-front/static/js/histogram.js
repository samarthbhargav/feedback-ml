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
          
function renderPieChart(data, chartDiv){
  nv.addGraph(function() {
  var chart = nv.models.pieChart()
      .x(function(d) { return d.label })
      .y(function(d) { return d.value })
      .showLabels(true);

    d3.select(chartDiv + ' svg')
        .datum(data)
      .transition().duration(1200)
        .call(chart);

  return chart;
  });
}
