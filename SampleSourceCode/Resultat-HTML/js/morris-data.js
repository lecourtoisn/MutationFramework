$(function() {

    Morris.Donut({
      element: 'morris-donut-chart',
      data: [
        {value: 100.0, label: 'Mutants tues'},
        {value: 60.0, label: 'Mutants morts nes'},
        {value: 0.0, label: 'Mutants non tues'},
      ],
      formatter: function (x) { return x + "%"}
    }).on('click', function(i, row){
      console.log(i, row);
    });

});