// Single Page Application Framework
(function ($, window) {
    const pageHandlers = {}
    let currentPage
    // show the "page" with optional parameter
    function show (pageName, param) {
        //preloaderShow();
        // invoke page handler
        const ph = pageHandlers[pageName]
        if (ph) {
            const $page = $("section#" + pageName)
            ph.call($page.length ? $page[0] : null, param) // call "page" handler
        }
        // activate the page  
        $(document.body).attr("page", pageName)
            .find("section").fadeOut(600).removeClass("active")
            .filter("section#" + pageName).fadeIn(600).addClass("active")
    }
    // Page Fetcher
    function app (pageName, param) {
        // select current page section
        const $page = $(document.body).find("section#" + pageName)
        // get value from 'data-src' attribute
        const src = $page.data("src")
        if (src && $page.find(">:first-child").length === 0) {
            // it has src and is empty - load it
            $.get(src)
                .done(function (html) {
                    if (html) {
                        currentPage = pageName
                        $page.html(html)
                        show(pageName, param)
                    }
                })
                .fail(function () {
                    $page.html("failed to get:" + src)
                });
        } else
            show(pageName, param);
    }
    // register page handler  
    app.handler = function (handler) {
        const $page = $(document.body).find("section#" + currentPage)
        pageHandlers[currentPage] = handler.call($page[0])
    }
    function onhashchange() {
        let hash = location.hash || "#!home"
        let re = /#!([-0-9A-Za-z]+)(\:(.+))?/
        const match = re.exec(hash)
        if (match){
            hash = match[1]
            const param = match[3]
            app(hash, param) // navigate to the page
        } else {
            re = /#([-0-9A-Za-z]+)(\:(.+))?/
            const match = re.exec(hash)
            if (match){
                hash = match[1]
                const param = match[3]
                app(hash, param) // navigate to the page
            }
        }
    }
    $(window).hashchange(onhashchange) // attach hashchange handler
    window.app = app // setup the app as global object
    $(function () { $(window).hashchange() }) // initial state setup
})(jQuery, this);






