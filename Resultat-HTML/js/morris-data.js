$(function() {

    Morris.Donut({
      element: 'morris-donut-chart',
      data: [
        {value: 50.0, label: 'Mutants tu�s'},
        {value: 0, label: 'Mutants morts n�s'},
        {value: 50.0, label: 'Mutants non tu�s'},
      ],
      formatter: function (x) { return x + "%"}
    }).on('click', function(i, row){
      console.log(i, row);
    });

});
