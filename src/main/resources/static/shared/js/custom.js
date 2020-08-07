let signed = false
const preloaderHide = function () {
    $('.preloader-wrapper').css('display', 'none')
}
const preloaderShow = function () {
    $('.preloader-wrapper').css('display', 'block')
}
//
const onSignIn = function (accountInfo) {
    $("a[href='#!home:out']").text('Sign Out (' + accountInfo.name + ')')
    $("a[href='#!home:out']").css('display', 'block')
    $("a[href='#!feedback']").css('display', 'block')
    $("a[href='#!signin']").css('display', 'none')
    $("a[href='#!signup']").css('display', 'none')
    $(".cart").css('display', 'block')
    signed = true
}
const onSignOut = function () {
    $("a[href='#!home:out']").text('')
    $("a[href='#!home:out']").css('display', 'none')
    $("a[href='#!signin']").css('display', 'block')
    $("a[href='#!signup']").css('display', 'block')
    $(".cart").css('display', 'none')
    signed = false
    $("#admin").html('')
}
$(document).ready(function () {
    $('.sidenav').sidenav()
    $('.modal').modal()
    $.get("api/auth/user/check")
        .done(function (resp) {
            if (resp && resp.status && resp.status === 'success') {
                if (resp.data) {
                    onSignIn(resp.data)
                }
            }
        })
        .fail(function (xhr, status, error) {
            if (xhr.status != 401) {
                alert("Fatal error: " + error)
            }
        })
    const showCart = function () {
        $.ajax({
            url: "api/cart"
            , type: 'GET'
            , cache: false
        }).done(function (resp) {
            let template
            if (resp && resp.status === 'success') {
                template = Hogan.compile(
                    `<table class="table">
                                <thead>
                                    <tr>
                                        <th>id</th>
                                        <th>name</th>
                                        <th>count</th>
                                        <th>price</th>
                                    </tr>                                  
                                </thead>
                                <tbody>
                                    {{#data}}
                                        <tr>
                                            <th scope="row">{{id}}</th>
                                            <td>{{name}}</td>
                                            <td class="cartItemQuantity">{{count}}</td>
                                            <td class="cartItemPrice">{{price}}</td>
                                            <td>
                                                <div class="row">
                                                    <a class="col s3 offset-s1 waves-effect waves-light btn negBtn"><i class="material-icons">exposure_neg_1</i></a>
                                                    <a class="col s3 offset-s1 waves-effect waves-light btn plusBtn"><i class="material-icons">exposure_plus_1</i></a>
                                                    <a class="col s3 offset-s1 waves-effect waves-light btn remBtn"><i class="material-icons">clear</i></a>
                                                </div>
                                            </td>
                                        </tr>                                                          
                                    {{/data}}
                                    <tr>  
                                        <td></td>
                                        <td></td>
                                        <td></td>                             
                                        <th></th>
                                    </tr> 
                                    {{^data}}
                                        <span>Your cart is empty</span>
                                    {{/data}}
                                </tbody>
                            </table>`
                )
                //Заполняем шаблон данными и помещаем на веб-страницу
                resp = JSON.parse(decodeURIComponent(JSON.stringify(resp)))
                $('.modal-content').html(template.render(resp))
                let cartTableDataRows =
                    document.querySelectorAll('.modal-content tbody tr:not(:last-child)')
                console.log('cartTableDataRows = ', cartTableDataRows)
                let totalPrice = 0
                cartTableDataRows.forEach((row, idx, rows) => {
                    const cartItemQuantity =
                        $(row).find('.cartItemQuantity').text()
                    const cartItemPrice =
                        $(row).find('.cartItemPrice').text()
                    console.log(cartItemQuantity, cartItemPrice)
                    totalPrice += cartItemQuantity * cartItemPrice

                })
                console.log('totalPrice = ', totalPrice)
                $('.modal-content tbody tr:last-child > th').text(`Total price: ${totalPrice.toFixed(2)}`)
                $(".plusBtn").unbind("click")
                $('.plusBtn').click(function (ev) {
                    ev.preventDefault()
                    const productId = $(this).parent().parent().parent().children('th').text()
                    $.ajax({
                        url: "api/cart/" + productId
                        , type: 'POST'
                    }).done(function (resp) {
                        if (resp && resp.status === 'success') {
                            showCart()
                        } else {
                            alert("error: " + resp.message)
                        }
                    }).fail(function (jqXHR, textStatus, errorThrown) {
                        alert("Error: " + jqXHR)
                    })
                })
                $(".negBtn").unbind("click")
                $('.negBtn').click(function (ev) {
                    ev.preventDefault()
                    const productId = $(this).parent().parent().parent().children('th').text()
                    $.ajax({
                        url: "api/cart/" + productId
                        , type: 'PUT'
                    }).done(function (resp) {
                        if (resp && resp.status === 'success') {
                            showCart()
                        } else {
                            alert("error: " + resp.message)
                        }
                    }).fail(function (jqXHR, textStatus, errorThrown) {
                        alert("Error: " + jqXHR)
                    })
                })
                $(".remBtn").unbind("click")
                $('.remBtn').click(function (ev) {
                    if (confirm("Remove all the items?")) {
                        ev.preventDefault()
                        const productId =
                            $(this).parent().parent().parent()
                                .children('th').text()
                        $.ajax({
                            url: "api/cart/" + productId
                            , type: 'DELETE'
                        }).done(function (resp) {
                            if (resp && resp.status === 'success') {
                                showCart()
                            } else {
                                alert("error: " + resp.message)
                            }
                        }).fail(function (jqXHR, textStatus, errorThrown) {
                            alert("Error: " + jqXHR)
                        })
                    }
                })
            } else {
                template = Hogan.compile(
                    '<span>Error: {{message}}</span>'
                )
            }
        }).fail(function (jqXHR, textStatus, message) {
            alert("Error: " + jqXHR)
        })
    }
    //Привязка обработчика события - клик по кнопке "Показать корзину"
    $('.cart').click(showCart)
})