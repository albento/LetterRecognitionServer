/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

(function()
{
    $.material.init();
    $('#login_modal').modal();
    $('#login_form').on('submit', function(e) {
        e.preventDefault();
    });
}());

function login()
{
    var id = $('#email').val();
    var pw = $('#password').val();
    
    $.ajax({
        url: './ActionServlet',
        type: 'POST',
        data: {
            action: 'login',
            email: id,
            password: pw
        },
        async:false,
        dataType: 'json'
    })
    .done(function(data) {
           
           var result = data.result;
           var link = result.link;
   
           document.location.href = link;
           console.log(link);
           sessionStorage.setItem("administrator", result.administrator);
    })
    .fail(function() {
        console.log('Login error.');
    })
    .always(function() {
        //
    });
}