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
})