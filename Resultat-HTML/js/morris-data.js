$(function() {

    Morris.Donut({
      element: 'morris-donut-chart',
      data: [
        {value: 50.0, label: 'Mutants tués'},
        {value: 0, label: 'Mutants morts nés'},
        {value: 50.0, label: 'Mutants non tués'},
      ],
      formatter: function (x) { return x + "%"}
    }).on('click', function(i, row){
      console.log(i, row);
    });

});
