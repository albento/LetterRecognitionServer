/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var images = [];
var administrator = sessionStorage.getItem("administrator");

$(document).ajaxComplete(function(event,xhr,options)
{
    if (xhr.status == 401)
    {
        document.location.href = "login.html";
    }
});

(function()
{
    if (administrator == null)
    {
        window.location.href = "login.html";
    }
    init();
}());

function init()
{
    listImages();
}

function listImages()
{
    $.ajax({
        url: './ActionServlet',
        type: 'GET',
        data: {
            action: 'list',
            type: "images_to_be_analyzed"
        },
        async:false,
        dataType: 'json'
    })
    .done(function(data) {
        images = data.list;
        var html = "";

        for (var i = 0; i < images.length; i++)
        {
            html += "<div class='panel panel-default'><div class='panel-heading'>" + images[i].letter + "</div>";
            html += "<div class='panel-body' style='padding: 15px 0px 0px 0px;'>";
            
            for (var j = 0; j < images[i].drawings.length; j++)
            {
                console.log(images[i].drawings[j]);
                var index = images[i].drawings[j].name.split("-")[2].split("_")[0];
                
                html += "<div class='col-md-6'>";
                html += "<div class='well well-sm' style='padding: 15px 0px 15px 15px; margin-bottom: 15px;'><div class='row' style='display: table; width: 100%'>";
                html += "<div class='col-xs-11' style='display: table-cell; float: none;'><div class='col-xs-6' style='padding: 0px 7px 0px 0px;'><img src='" + images[i].drawings[j].image + "' style='border: 2px solid black;'/></div>";
                html += "<div class='col-xs-6' style='padding: 0px 0px 0px 7px;'><img src='" + images[i].computer_vision[j].image + "' style='border: 2px solid black;'></div></div>";
                html += "<div class='col-xs-1' style='vertical-align: middle; display: table-cell; float: none; text-align: center;'><div class='row' style='margin-bottom: 10px;'><i id='approve_" + images[i].letter + "_" + index + "' class='clickable fa fa-thumbs-o-up fa-2x' onclick='approve(\"" + images[i].letter + "\", \"" + index + "\", \"" + images[i].drawings[j].name + "\")'></i></div>";
                html += "<div class='row' style='margin-top: 10px;'><i id='discard_" + images[i].letter + "_" + index + "' class='clickable fa fa-thumbs-o-down fa-2x' onclick='discard(\"" + images[i].letter + "\", \"" + index + "\", \"" + images[i].drawings[j].name + "\")'></i></div></div>";
                html += "</div></div></div>";
            }
            
            html += "</div></div>";
        }
        
        $('#list_images').html(html);
    })
    .fail(function() {
        console.log("Couldn't load the list.");
    })
    .always(function() {
        //
    });
}

function approve(letter, index, name)
{
    $('[id^="approve"]').attr("class", "fa");
    $('[id^="discard"]').attr("class", "fa");
    $('#approve_' + letter + '_' + index).attr("class", "fa fa-cog fa-spin fa-2x fa-fw");
    
    $.ajax({
        url: './ActionServlet',
        type: 'GET',
        data: {
            action: 'training',
            image: name
        },
        async:false,
        dataType: 'json'
    })
    .always(function() {
        window.location.reload();
    });
}

function discard(letter, index, name)
{
    $('[id^="approve"]').attr("class", "fa");
    $('[id^="discard"]').attr("class", "fa");
    $('#discard_' + letter + '_' + index).attr("class", "fa fa-cog fa-spin fa-2x fa-fw");
    
    $.ajax({
        url: './ActionServlet',
        type: 'GET',
        data: {
            action: 'discard',
            image: name
        },
        async:false,
        dataType: 'json'
    })
    .always(function() {
        window.location.reload();
    });
}