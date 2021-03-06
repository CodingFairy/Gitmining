/**
 * Created by Harry on 2016/8/5.
 */

var ownerName = $("#js-repo-pjax-container > div.pagehead.repohead.instapaper_ignore.readability-menu.experiment-repo-nav > div.container.repohead-details-container > h1 > span.author > a")[0].innerHTML;
var repoName = $("#js-repo-pjax-container > div.pagehead.repohead.instapaper_ignore.readability-menu.experiment-repo-nav > div.container.repohead-details-container > h1 > strong > a")[0].innerHTML;

var watch_ratio = -1.0;
var star_ratio = -1.0;
var fork_ratio = -1.0;

// function getLoadSvg() {
//     return '<div class="loading_area"><svg class="loader" version="1.1" xmlns="http://www.w3.org/2000/svg" x="0px" y="0px"width="20px" height="20px" viewBox="0 0 20 20" style="enable-background:new 0 0 20 20;" xml:space="preserve"> <path fill="rgb(53,150,227)" d="M25.251,6.461c-10.318,0-18.683,8.365-18.683,18.683h4.068c0-8.071,6.543-14.615,14.615-14.615V6.461z"> <animateTransform attributeType="xml"attributeName="transform"type="rotate"from="0 25 25"to="360 25 25"dur="0.6s"repeatCount="indefinite"/></path></svg></div>';
// }

function getLoadSvg() {
    return '<div class="loading_area"><svg class="loader" version="1.1" xmlns="http://www.w3.org/2000/svg" x="0px" y="0px"width="24px" height="30px" viewBox="0 0 24 30" style="enable-background:new 0 0 50 50;" xml:space="preserve"> <rect x="0" y="13" width="4" height="5" fill="rgb(53,150,227)"><animate attributeName="height" attributeType="XML"values="5;21;5"begin="0s" dur="0.6s" repeatCount="indefinite" /><animate attributeName="y" attributeType="XML"values="13; 5; 13"begin="0s" dur="0.6s" repeatCount="indefinite" /> </rect> <rect x="10" y="13" width="4" height="5" fill="rgb(53,150,227)"><animate attributeName="height" attributeType="XML"values="5;21;5"begin="0.15s" dur="0.6s" repeatCount="indefinite" /> <animate attributeName="y" attributeType="XML"values="13; 5; 13"begin="0.15s" dur="0.6s" repeatCount="indefinite" /> </rect><rect x="20" y="13" width="4" height="5" fill="rgb(53,150,227)"> <animate attributeName="height" attributeType="XML"values="5;21;5"begin="0.3s" dur="0.6s" repeatCount="indefinite" /> <animate attributeName="y" attributeType="XML"values="13; 5; 13"begin="0.3s" dur="0.6s" repeatCount="indefinite" /></rect></svg></div>';
}

function addPopupToPage() {
    var repohead = $(".repohead-details-container");
    //this is not a repository!!!
    if (repohead.length == 0) {
        return;
    }
    //the page is a github repository
    else {
        //console.log("find a github repository!");
        var lis = $(".repohead-details-container ul.pagehead-actions li");
        var watch = lis[0];
        var star = lis[1];
        var fork = lis[2];
        $(watch).append("<div class='popup' id='watch_tip'> <div class='tip_prompt'>watch count beats :</div>"+getLoadSvg()+"</div>");
        $(star).append("<div class='popup' id='star_tip'> <div class='tip_prompt'>star count beats :</div>"+getLoadSvg()+"</div>");
        $(fork).append("<div class='popup' id='fork_tip'> <div class='tip_prompt'>fork count beats :</div>"+getLoadSvg()+"</div>");

    }
}


function loadCompareData() {
    var watchCount = $.trim($(".repohead-details-container > ul > li:nth-child(1) a.social-count").html()).replace(',', '');
    var starCount = $.trim($(".repohead-details-container > ul > li:nth-child(2) a.social-count").html()).replace(',', '');
    var forkCount = $.trim($(".repohead-details-container > ul > li:nth-child(3) a.social-count").html()).replace(',', '');

    //console.log("starCount is: " + starCount);

    var port = chrome.runtime.connect({name: ownerName+"/"+repoName});
    port.onMessage.addListener(function (msg) {
        ////console.log("back msg: "+msg.ratio);
        if (msg.theme == "back_compare") {
            if (msg.dataType == "watch"){
                watch_ratio = msg.ratio;
                $("#watch_tip > .loading_area").text(msg.ratio+"%");
                $("#watch_tip > .loading_area").css("font-size","19px");
            }
            else if (msg.dataType == "star"){
                star_ratio = msg.ratio;
                $("#star_tip > .loading_area").text(msg.ratio+"%");
                $("#star_tip > .loading_area").css("font-size","19px");
            }
            else if (msg.dataType == "fork"){
                fork_ratio = msg.ratio;
                $("#fork_tip > .loading_area").text(msg.ratio+"%");
                $("#fork_tip > .loading_area").css("font-size","19px");
            }
        }
    });
    if ( (watch_ratio<0) && (star_ratio<0) && (fork_ratio<0) ){
        port.postMessage({
            theme: "compare",
            watch: watchCount,
            star: starCount,
            fork: forkCount
        });
    }
    else {
        $("#watch_tip > .loading_area").text(watch_ratio+"%");
        $("#star_tip > .loading_area").text(star_ratio+"%");
        $("#fork_tip > .loading_area").text(fork_ratio+"%");
    }
}

function addSubscribe() {
    //console.log("enter add subscribe");
    var timeNow = new Date();
    var id = ownerName+"/"+repoName;
    var obj = {};
    var inf = {
        time: timeNow.getTime(),
        isUpdate: false
    };
    obj[id] = inf;
    chrome.storage.sync.set(obj);
    ////console.log(obj);
}

function removeSubscribe() {
    //console.log("enter remove subscribe");
    chrome.storage.sync.remove(ownerName+"/"+repoName);
}

function isSubscribe() {
    //console.log("enter isSubscribe");
    var id = ownerName+"/"+repoName;
    chrome.storage.sync.get(id, function (item) {
        ////console.log(item);
        if (!jQuery.isEmptyObject(item)) {
            //is subscribed
            //console.log("prepare to change subscribe state");
            $("#subscribe-btn").html('Unsubscribe');
        }
    });
}

// <svg aria-hidden="true" class="octicon octicon-eye" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path d="M8.06 2C3 2 0 8 0 8s3 6 8.06 6C13 14 16 8 16 8s-3-6-7.94-6zM8 12c-2.2 0-4-1.78-4-4 0-2.2 1.8-4 4-4 2.22 0 4 1.8 4 4 0 2.22-1.78 4-4 4zm2-4c0 1.11-.89 2-2 2-1.11 0-2-.89-2-2 0-1.11.89-2 2-2 1.11 0 2 .89 2 2z"></path></svg>

function addSubscribeBtn() {
    $("#js-repo-pjax-container > div.pagehead.repohead.instapaper_ignore.readability-menu.experiment-repo-nav > div.container.repohead-details-container > ul").append('<li id="subscribe-btn-li"><button type="button" class="btn btn-primary btn-default" id="subscribe-btn" title="(un)subscribe to news about the repository" data-toggle="button" autocomplete="off" aria-pressed="false">Subscribe</button></li>');

    isSubscribe();

    $("#subscribe-btn").on("click", function () {
        ////console.log("click the subscribe button");
        if ($(this).text() == "Subscribe") {
            $(this).html('Unsubscribe');
            addSubscribe();

        }
        else {
            $(this).html('Subscribe');
            removeSubscribe();
        }
    });
}



chrome.runtime.onConnect.addListener(function (port) {
    //console.log("port " + port.name + " connect to content script.");
    //console.log("the sender url is: " + port.sender.url);
    port.onMessage.addListener(function (msg) { //msg is a json

        if (msg.theme == "query_owner_and_repo") {
            port.postMessage({
                theme: "back_owner_and_repo",
                owner:ownerName,
                name:repoName
            });
        }

    });
});

chrome.runtime.onMessage.addListener(function (message, sender, sendResponse) {
    //console.log("get message");
    //console.log(message.theme);
    if (message.theme == "reinsert tip window"){
        $(".popup").remove();
        $("#subscribe-btn-li").remove();
        addPopupToPage();
        loadCompareData();
        addSubscribeBtn();
    }
});

$(function () {
    addPopupToPage();
    loadCompareData();
    addSubscribeBtn();
});