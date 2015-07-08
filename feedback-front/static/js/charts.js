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
  //Donut chart example
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
        .call(chart);

  return chart;
});
}

$(document).ready(function(){
    $("#flip").click(function(){
        $("#chart1").slideToggle("slow");
    });
  });


  
