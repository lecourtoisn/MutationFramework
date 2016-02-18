$(function() {

    Morris.Donut({
      element: 'morris-donut-chart',
      data: [
        {value: 70, label: 'Mutants tués'},
        {value: 20, label: 'Mutants morts nés'},
        {value: 10, label: 'Mutants non tués'},
      ],
      formatter: function (x) { return x + "%"}
    }).on('click', function(i, row){
      console.log(i, row);
    });

});
