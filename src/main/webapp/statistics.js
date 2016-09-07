/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var statistics = [];

(function()
{
    loadStatistics();
}());

function loadStatistics()
{
    $.ajax({
        url: './ActionServlet',
        type: 'GET',
        data: {
            action: 'list',
            type: "statistics"
        },
        async:false,
        dataType: 'json'
    })
    .done(function(data) {
        statistics = data.list;

        var html = "", html_plus = "<div class='row'>";
        var tot = 0, tot_first = 0; tot_second = 0, tot_third = 0, tot_more = 0;

        for (var i = 0; i < statistics.length; i++)
        {
            var sum = statistics[i].number_first + statistics[i].number_second + statistics[i].number_third + statistics[i].number_more;
            
            var first = Math.floor(statistics[i].number_first / sum * 10000) / 100;
            var second = Math.floor(statistics[i].number_second / sum * 10000) / 100;
            var third = Math.floor(statistics[i].number_third / sum * 10000) / 100;
            var more = Math.floor(statistics[i].number_more / sum * 10000) / 100;
            
            tot_first += statistics[i].number_first;
            tot_second += statistics[i].number_second;
            tot_third += statistics[i].number_third;
            tot_more += statistics[i].number_more;
            
            html_plus += "<div class='col-md-6'>";
            html_plus += "<div class='well well-sm' style='padding: 0px 0px 0px 25px; margin-bottom: 15px;'><div class='row' style='display: table; width: 100%'>";
            html_plus += "<div class='col-xs-1' style='display: table-cell; float: none; vertical-align: middle; text-align: center;'><h2 style='margin-top: 0px; margin-bottom: -20px;'>" + statistics[i].letter + "</h2><br/><h4 style='margin: 0px;'>(" + sum + ")</h4></div>";
            html_plus += "<div class='col-xs-11' style='display: table-cell; float: none; vertical-align: middle;'>";
            html_plus += "<div class='row' style='margin-top: 10px; margin-bottom: 0px;'><div class='col-xs-4'><h4>1<sup>st</sup>: " + first + "%</h4></div><div class='col-xs-8'><div class='progress'><div class='progress-bar progress-bar-success' style='width: " + first + "%'></div></div></div></div>";
            html_plus += "<div class='row' style='margin-top: 0px; margin-bottom: 0px;'><div class='col-xs-4'><h4>2<sup>nd</sup>: " + second + "%</h4></div><div class='col-xs-8'><div class='progress'><div class='progress-bar progress-bar-info' style='width: " + second + "%'></div></div></div></div>";
            html_plus += "<div class='row' style='margin-top: 0px; margin-bottom: 0px;'><div class='col-xs-4'><h4>3<sup>rd</sup>: " + third + "%</h4></div><div class='col-xs-8'><div class='progress'><div class='progress-bar progress-bar-warning' style='width: " + third + "%'></div></div></div></div>";
            html_plus += "<div class='row' style='margin-top: 0px; margin-bottom: 10px;'><div class='col-xs-4'><h4>more: " + more + "%</h4></div><div class='col-xs-8'><div class='progress'><div class='progress-bar progress-bar-danger' style='width: " + more + "%'></div></div></div></div>";
            html_plus += "</div>";
            html_plus += "</div></div></div>";
        }
        
        html_plus += "</div>";
        
        tot = tot_first + tot_second + tot_third + tot_more;
        
        tot_first = Math.floor(tot_first / tot * 10000) / 100;
        tot_second = Math.floor(tot_second / tot * 10000) / 100;
        tot_third = Math.floor(tot_third / tot * 10000) / 100;
        tot_more = Math.floor(tot_more / tot * 10000) / 100;
        
        html += "<div class='row'><div class='col-md-6 col-md-push-3'>";
        html += "<div class='well well-sm' style='padding: 0px 0px 0px 25px; margin-bottom: 15px;'><div class='row' style='display: table; width: 100%'>";
        html += "<div class='col-xs-1' style='display: table-cell; float: none; vertical-align: middle; text-align: center;'><h2 style='margin-top: 0px; margin-bottom: -20px;'>All</h2><br/><h4 style='margin: 0px;'>(" + tot + ")</h4></div>";
        html += "<div class='col-xs-11' style='display: table-cell; float: none; vertical-align: middle;'>";
        html += "<div class='row' style='margin-top: 10px; margin-bottom: 0px;'><div class='col-xs-4'><h4>1<sup>st</sup>: " + tot_first + "%</h4></div><div class='col-xs-8'><div class='progress'><div class='progress-bar progress-bar-success' style='width: " + tot_first + "%'></div></div></div></div>";
        html += "<div class='row' style='margin-top: 0px; margin-bottom: 0px;'><div class='col-xs-4'><h4>2<sup>nd</sup>: " + tot_second + "%</h4></div><div class='col-xs-8'><div class='progress'><div class='progress-bar progress-bar-info' style='width: " + tot_second + "%'></div></div></div></div>";
        html += "<div class='row' style='margin-top: 0px; margin-bottom: 0px;'><div class='col-xs-4'><h4>3<sup>rd</sup>: " + tot_third + "%</h4></div><div class='col-xs-8'><div class='progress'><div class='progress-bar progress-bar-warning' style='width: " + tot_third + "%'></div></div></div></div>";
        html += "<div class='row' style='margin-top: 0px; margin-bottom: 10px;'><div class='col-xs-4'><h4>more: " + tot_more + "%</h4></div><div class='col-xs-8'><div class='progress'><div class='progress-bar progress-bar-danger' style='width: " + tot_more + "%'></div></div></div></div>";
        html += "</div>";
        html += "</div></div>";
        html += "</div></div>";
        
        html += html_plus;
        
        $('#letters').html(html);
    })
    .fail(function() {
        console.log("Couldn't load the list.");
    })
    .always(function() {
        //
    });
}