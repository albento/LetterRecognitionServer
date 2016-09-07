var mousePressed = false;
var lastX, lastY;
var ctx;
var thickness = '6';

var results = [];

(function()
{
	InitThis();
}());

function InitThis() {
    
    ctx = document.getElementById('myCanvas').getContext("2d");

    $('#myCanvas').on("mousedown", function (e) {
        mousePressed = true;
        Draw(e.pageX - $(this).offset().left, e.pageY - $(this).offset().top, false);
    });
    
    $('#myCanvas').on("touchstart", function (e) {
        e = e.originalEvent;
        Draw(e.changedTouches[0].pageX - $(this).offset().left, e.changedTouches[0].pageY - $(this).offset().top, false);
    });

    $('#myCanvas').on("mousemove", function (e) {
        if (mousePressed) {
            Draw(e.pageX - $(this).offset().left, e.pageY - $(this).offset().top, true);
        }
    });
    
    $('#myCanvas').on("touchmove", function (e) {
        e.preventDefault();
        e = e.originalEvent;
        Draw(e.changedTouches[0].pageX - $(this).offset().left, e.changedTouches[0].pageY - $(this).offset().top, true);
    });

    $('#myCanvas').on("mouseup", function (e) {
        mousePressed = false;
    });
        
    $('#myCanvas').mouseleave(function (e) {
        mousePressed = false;
    });
    
    $.material.init();
}

function Draw(x, y, isDown) {
    
    if (isDown) {
        ctx.beginPath();
        ctx.strokeStyle = 'black';
        ctx.lineWidth = thickness;
        ctx.lineJoin = "round";
        ctx.moveTo(lastX, lastY);
        ctx.lineTo(x, y);
        ctx.closePath();
        ctx.stroke();
    }
    lastX = x; lastY = y;
}
	
function clearArea() {
    // Use the identity matrix while clearing the canvas
    ctx.setTransform(1, 0, 0, 1, 0, 0);
    ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
    
    var canvas_computer = document.getElementById("computer_vision");
    
    if (canvas_computer != null)
    {
        var ctx_computer = canvas_computer.getContext("2d");
        ctx_computer.setTransform(1, 0, 0, 1, 0, 0);
        ctx_computer.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
    }
    
    $('#row_results').html("");
    results = [];
}

function identify()
{
    $('#loading_icon').attr("class", "fa fa-cog fa-spin fa-lg fa-fw");
    
    $.ajax({
        url: './ActionServlet',
        type: 'POST',
        data: {
            action: 'test',
            image: document.getElementById("myCanvas").toDataURL("image/png").split(",")[1]
        },
        async:false,
        dataType: 'json'
    })
    .done(function(data) {
        results = data.results;

        results.sort(function(a, b) {
            return b.value - a.value;
        });
        
        displayComputerVision(data.computer_vision);
        
        displayResults();
        
        $('#loading_icon').attr("class", "fa");
    })
    .fail(function() {
        console.log('Upload error');
        $('#loading_icon').attr("class", "fa fa-times");
    })
    .always(function() {
        //
    });
}

function displayComputerVision(computer_vision)
{
    $('#drawing_area').attr("class", "col-sm-6");
    $('#display_area').attr("class", "col-sm-6");
    
    var html = '<div align="center"><h3>How the system sees it:</h3><canvas id="computer_vision" width ="200" height="200" style="border:2px solid black"></canvas></div>';
    
    $('#display_area').html(html);
    
    var c = document.getElementById("computer_vision");
    var ctx_computer = c.getContext("2d");
    
    for (var i = 0; i < computer_vision.points.length; i++)
    {
        var p = computer_vision.points[i];
        var dim = computer_vision.dimension;
        
        var x = 20 + p.x * 180 / dim;
        var y = 20 + p.y * 180 / dim;
        
        ctx_computer.beginPath();
        ctx_computer.arc(x+2, y+2, 4, 0, 2 * Math.PI, true);
        ctx_computer.fill();
    }
}

function displayResults()
{
    var html = "";
    
    for (var i = 0; i < 3; i++)
    {
        var value = results[i].value;

        html += "<div class='col-sm-4'>";
        html += "<div class='well well-sm' style='padding: 0px 0px 0px 20px; margin-top: 20px;'><div class='row'><h2 style='margin-left: 14px;'>" + results[i].letter + ": " + value + "%</h2></div>";
        html += "<div class='row'><div class='col-xs-10'><div class='progress'>";

        if (value >= 70)
        {
            html += "<div class='progress-bar progress-bar-success' style='width: " + value + "%'></div>";
        }
        else if (value >= 40)
        {
            html += "<div class='progress-bar progress-bar-warning' style='width: " + value + "%'></div>";
        }
        else
        {
            html += "<div class='progress-bar progress-bar-danger' style='width: " + value + "%'></div>";
        }

        html += "</div></div>";
        
        html += "<i class='clickable col-xs-2 fa fa-2x fa-thumbs-o-up' id='approve_" + results[i].letter + "' style='top: -30px; left: -15px;' data-toggle='tooltip' data-placement='top' title='This is the letter I wrote!' onclick='approve(\"" + results[i].letter + "\")'></i>";
        
        html += "</div></div></div>";
    }
    
    html += "<div class='row'><div class='text-center'><button class='btn btn-default btn-raised' id='btn_other_results' onclick='showOtherResults()'>Show more results</button></div></div>";
    html += "<div id='other_results'></div>";
    
    $('#row_results').html(html);
    
    $('[data-toggle="tooltip"]').tooltip();
}

function showOtherResults()
{
    var html = "";
    
    for (var i = 3; i < results.length; i++)
    {        
        var value = results[i].value;
        
        html += "<div class='col-sm-4'>";
        html += "<div class='well well-sm' style='padding: 0px 0px 0px 20px; margin-top: 20px;'><div class='row'><h2 style='margin-left: 14px;'>" + results[i].letter + ": " + value + "%</h2></div>";
        html += "<div class='row'><div class='col-xs-10'><div class='progress'>";

        if (value >= 70)
        {
            html += "<div class='progress-bar progress-bar-success' style='width: " + value + "%'></div>";
        }
        else if (value >= 40)
        {
            html += "<div class='progress-bar progress-bar-warning' style='width: " + value + "%'></div>";
        }
        else
        {
            html += "<div class='progress-bar progress-bar-danger' style='width: " + value + "%'></div>";
        }

        html += "</div></div>";
        
        html += "<i class='clickable col-xs-2 fa fa-2x fa-thumbs-o-up' id='approve_" + results[i].letter + "' style='top: -30px; left: -15px;' data-toggle='tooltip' data-placement='top' title='This is the letter I wrote!' onclick='approve(\"" + results[i].letter + "\")'></i>";
        
        html += "</div></div></div>";
    }
    
    $('#other_results').html(html);
    
    $('[data-toggle="tooltip"]').tooltip();
    
    $('#btn_other_results').html("Hide other results").attr("onclick", "hideOtherResults()");
}

function hideOtherResults()
{
    $('#other_results').html("");
    
    $('#btn_other_results').html("Show more results").attr("onclick", "showOtherResults()");
}

function approve(letter)
{
    var rank;
    
    for (var i = 0; i < results.length; i++)
    {
        if (letter == results[i].letter)
        {
            rank = i+1;
            break;
        }
    }
    
    $.ajax({
        url: './ActionServlet',
        type: 'POST',
        data: {
            action: 'upload',
            image: document.getElementById("myCanvas").toDataURL("image/png").split(",")[1],
            image_computer: document.getElementById("computer_vision").toDataURL("image/png").split(",")[1],
            letter: letter,
            rank: rank
        },
        async:false,
        dataType: 'json'
    })
    .always(function() {
        clearArea();
    });
}