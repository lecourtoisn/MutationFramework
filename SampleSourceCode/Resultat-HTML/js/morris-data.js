$(function() {

    Morris.Donut({
      element: 'morris-donut-chart',
      data: [
        {value: 10.0, label: 'Mutants tues'},
        {value: 0.0, label: 'Mutants morts nes'},
        {value: 90.0, label: 'Mutants non tues'},
      ],
      formatter: function (x) { return x + "%"}
    }).on('click', function(i, row){
      console.log(i, row);
    });

});
