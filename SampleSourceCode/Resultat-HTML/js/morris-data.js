$(function() {

    Morris.Donut({
      element: 'morris-donut-chart',
      data: [
        {value: 61.53846153846154, label: 'Mutants tues'},
        {value: 0.0, label: 'Mutants morts nes'},
        {value: 38.46153846153847, label: 'Mutants non tues'},
      ],
      formatter: function (x) { return x + "%"}
    }).on('click', function(i, row){
      console.log(i, row);
    });

});
