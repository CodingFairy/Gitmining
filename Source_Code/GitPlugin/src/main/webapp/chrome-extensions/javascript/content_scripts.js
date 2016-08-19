/**
 * Created by Harry on 2016/8/5.
 */

var ownerName = $("#js-repo-pjax-container > div.pagehead.repohead.instapaper_ignore.readability-menu.experiment-repo-nav > div.container.repohead-details-container > h1 > span.author > a")[0].innerHTML;
var repoName = $("#js-repo-pjax-container > div.pagehead.repohead.instapaper_ignore.readability-menu.experiment-repo-nav > div.container.repohead-details-container > h1 > strong > a")[0].innerHTML;

var watch_ratio = -1.0;
var star_ratio = -1.0;
var fork_ratio = -1.0;

function getLoadSvg() {
    return '<div class="loading_area"><svg class="loader" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px"width="40px" height="40px" viewBox="0 0 50 50" style="enable-background:new 0 0 50 50;" xml:space="preserve"> <path fill="coral" d="M25.251,6.461c-10.318,0-18.683,8.365-18.683,18.683h4.068c0-8.071,6.543-14.615,14.615-14.615V6.461z"> <animateTransform attributeType="xml"attributeName="transform"type="rotate"from="0 25 25"to="360 25 25"dur="0.6s"repeatCount="indefinite"/></path></svg></div>';
}

function addPopupToPage() {
    var repohead = $(".repohead-details-container");
    //this is not a repository!!!
    if (repohead.length == 0) {
        return;
    }
    //the page is a github repository
    else {
        console.log("find a github repository!");
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
    var watchCount = $("#js-repo-pjax-container > div.pagehead.repohead.instapaper_ignore.readability-menu.experiment-repo-nav > div.container.repohead-details-container > ul > li:nth-child(1) > form > div.select-menu.js-menu-container.js-select-menu > a.social-count.js-social-count")[0].innerHTML.trim().replace(',', '');
    var starCount = $("#js-repo-pjax-container > div.pagehead.repohead.instapaper_ignore.readability-menu.experiment-repo-nav > div.container.repohead-details-container > ul > li:nth-child(2) > div > form.unstarred > a")[0].innerHTML.trim().replace(',', '');
    var forkCount = $("#js-repo-pjax-container > div.pagehead.repohead.instapaper_ignore.readability-menu.experiment-repo-nav > div.container.repohead-details-container > ul > li:nth-child(3) > a")[0].innerHTML.trim().replace(',', '');

    var port = chrome.runtime.connect({name: ownerName+"/"+repoName});
    port.onMessage.addListener(function (msg) {
        //console.log("back msg: "+msg.ratio);
        if (msg.theme == "back_compare") {
            if (msg.dataType == "watch"){
                watch_ratio = msg.ratio;
                $("#watch_tip > .loading_area").text(msg.ratio+"%");
            }
            else if (msg.dataType == "star"){
                star_ratio = msg.ratio;
                $("#star_tip > .loading_area").text(msg.ratio+"%");
            }
            else if (msg.dataType == "fork"){
                fork_ratio = msg.ratio;
                $("#fork_tip > .loading_area").text(msg.ratio+"%");
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

chrome.runtime.onConnect.addListener(function (port) {
    console.log("port " + port.name + " connect to content script.");
    console.log("the sender url is: " + port.sender.url);
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
    console.log("get message");
    console.log(message.theme);
    if (message.theme == "reinsert tip window"){
        $(".popup").remove();
        addPopupToPage();
        loadCompareData();
    }
});

$(function () {
    addPopupToPage();
    loadCompareData();
});