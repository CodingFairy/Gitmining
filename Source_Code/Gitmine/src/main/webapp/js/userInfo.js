/**
 * Created by yyy on 2016/5/18.
 */

var locationPort = 'http://localhost';
var nowUserList;
var $list = null;
var UserList={
    init:function(){
        this.gridsFather = $("#listStart");
        this.lastGrid = $(".news-grid").eq(0);
        this.clear = $("<div class=\"clearfix\"> </div>" );
    },
    updateData:function(userlist){
        nowUserList = userlist;
        this.gridsFather.empty();
        var _this = this;
        $.each(userlist,function(i,user){
            var tempGrid = _this.lastGrid.clone(true);
            var userName = tempGrid.find(".userName").eq(0);
            userName.text(user.login);
            userName.attr('href','userDetail.html?userName='+user.login);

            var imageUrl = tempGrid.find(".imageUrl").eq(0);
            imageUrl.attr('href','userDetail.html?userName='+user.login);
            var image = tempGrid.find(".header_user").eq(0);
            image.attr('src',user.avatarUrl);
            
            var cherish = tempGrid.find('.cherish');
            cherishPresent(user.isStared,cherish);

            onIdCherishClick(cherish,user.login,'/user/star','/user/unstar');

            tempGrid.find('.create').eq(0).text('create at:  '+user.createAt);
            tempGrid.find('.update').eq(0).text('update at:  '+user.updateAt);
            tempGrid.find('.repo').eq(0).text(user.publicRepo);
            tempGrid.find('.following').eq(0).text(user.following);
            tempGrid.find('.followed').eq(0).text(user.follower);

            _this.gridsFather.append(tempGrid);
            if(i%4==3){
                _this.gridsFather.append(_this.clear.clone(true));
            }

        })
    },
};

$(document).ready(
    function () {
        $list = jQuery('#listStart');
        $().UItoTop({ easingType: 'easeOutQuart' });
        UserList.init();
        var url = "/user/list"+"?pageNum=1";

        var i = 1000000000;
        while(i>0){i--};
        $.get(url,function (object) {
            UserList.updateData(object.userList);

            $.jqPaginator('#pagination1', {
                totalPages: object.totalPage,
                visiblePages: 8,
                currentPage: 1,
                onPageChange: function (current) {


                    jumpPage(current);

                }

            });
        });

        //remove the added cols to the modal
        $('#compareModal').on('hidden.bs.modal',function(){
            $('.newCol').remove();
        });

        //recommend
        RecList.init();
        $.ajax({
            type:'GET',
            url:'/user/recommend',
            success:function(recList){

                RecList.updateList(recList);

            }
        })


        
        
    }
);

function sort(obj){

    var originCheck = findCheckedSortType();
    console.log(originCheck);
    var obj = $(obj);
    if(originCheck.attr("sortType")==obj.attr('sortType')){


        if(obj.attr("isReverse")=="true"){
            obj.attr("isReverse" , "false");
            obj.children("span").attr("class", "glyphicon glyphicon-arrow-up");
        }else{
            obj.attr("isReverse" , "true");
            obj.children("span").attr("class",  "glyphicon glyphicon-arrow-down");
        };

    }else{
        originCheck.attr("ischecked" ,"false");
        originCheck.children("span").attr("class",  "glyphicon glyphicon-minus");

        obj.attr("ischecked" , "true");
        obj.attr("isReverse" , "true");
        obj.children("span").attr("class",  "glyphicon glyphicon-arrow-down");
    }
    // alert("what is wrong");


    search(true);

}

//used to ensure which sortType is choosed
function findCheckedSortType() {
    var type = undefined;
    $('.nav>li>a').each(
        function (i,n){
            var jq =  $(n);
            if(jq.attr("ischecked")=="true"){
                type =  jq;
            }
        }
    );
    if(type==undefined){
        type = $('.nav>li>a');
    }
    return type;
}

function search(keyChange){
    var keyword = $('#keyword').val();
    console.log($('#keyword').val());
    var sortElement= findCheckedSortType();
    var sortBy = sortElement.attr("sortType");
    var isReverse = sortElement.attr("isReverse");
    var page = 1;
    $.ajax({
        type:'GET',
        url:'/user/search',
        data:{
            keyword:keyword,
            sortType:sortBy,
            reverse:isReverse,
            pageNum:page,
            isKeyChanged:keyChange,
        },
        success:function(data){

            UserList.updateData(data.userList);
            console.log(data.totalPage);
            $.jqPaginator('#pagination1', {
                totalPages: data.totalPage,
                visiblePages: 8,
                currentPage: 1,
                onPageChange: function (current) {

                    jumpSearchPage(current);
                    
                }

            });
        },
        error:function(){
            alert("userInfo search wrong!");
        }
    })
    
    
}

function jumpPage(current){
    var url='/user/list?pageNum='+current;
    $.get(url,function(object){
        UserList.updateData(object.userList);
    })
}

function jumpSearchPage(current){
    var keyword = $('#keyword').val();
    var sortElement= findCheckedSortType();
    var sortBy = sortElement.attr("sortType");
    var isReverse = sortElement.attr("isReverse");
    $.ajax({
        type:'GET',
        url:'/user/search',
        data:{
            keyword:keyword,
            sortType:sortBy,
            reverse:isReverse,
            pageNum:current,
            isKeyChanged:false,
        },
        success:function(data){
            UserList.updateData(data.userList);
        },
        error:function(){
            alert("userInfo jumpPage wrong!");
        }
    })
}



function addCol(){
    //get the chosen users' name
    var gridList = $(".news-grid");
    var compList = new Array();
    $.each(gridList,function(i,grid){
        var gridItem = $(".news-grid").eq(i);
        var checkbox = gridItem.find('.checkbox').eq(0);
        if(checkbox.is(':checked')){
            var len = compList.length;
            compList[len] = gridItem.find('.userName').eq(0).text();
        }
    });

    if(compList.length<=1){
        alert('please choose at least two users to compare!');
        return;
    }

    $.each(compList,function(i,user){
        var userDetail = null;
        $.each(nowUserList,function(k,nowUser){
            if(nowUser.login == user){
                userDetail = nowUser;
                return false;
            }
        });
        var attrList = new Array();
        attrList[0] = userDetail.login;
        attrList[1] = userDetail.publicRepo;
        attrList[2] = userDetail.publicGist;
        attrList[3] = userDetail.follower;
        attrList[4] = userDetail.following;
        attrList[5] = userDetail.bio;
        attrList[6] = userDetail.type;
        attrList[7] = userDetail.location;
        attrList[8] = userDetail.company;
        attrList[9] = userDetail.createAt;
        addColCommon($('#compareTable tr'),attrList);

    })

    //prepare for radar chart
    var compInfo = new Array();
    var field = new Array();
    var len = compList.length;
    var count=0;
    console.log(compList.length);
    $.each(compList,function(i,compUser){

        $.ajax({
            method:'GET',
            url:'/user/'+compUser+'/basic',
            success:function(userResult){
                count++;
                compInfo.push(
                    
                        {   name: compList[i],
                            value:userResult.radarChart.value,
                        }
                    
                );

                if(count == len) {
                    field = userResult.radarChart.field;
                    var obj = document.getElementById('compareRadar');
                    drawRadarChart(obj,compList,field,compInfo);

                }
                
            },
            error: function(){
                alert("userInfo compare addCol wrong!");
            }
        })
    })

    $('#compareModal').modal();




}
function addColCommon(obj,dataList){
    obj.each(function(j,attr){
        var trHtml = $(this).html();
        if(j==0){
            trHtml += '<th class="newCol">'+dataList[j]+'</th>'
        }else {
            trHtml += '<td class="newCol">'+dataList[j]+'</td>';
        }
        $(this).html(trHtml);
    });
}

function drawRadarChart(obj,legendArea,field,data){
    //radar chart really begins!
    var radar_compare =echarts.init(obj);
    var lineStyle = {
        normal: {
            width: 1,
            opacity: 0.5,
            color:'#F9713C'
        }
    }
    option = {
        backgroundColor: '#161627',
        legend:{
            data: legendArea,
        },
        tooltip:{

        },
        radar:{
            indicator:[
                {name: field[0], max:1},
                {name: field[1], max:1},
                {name: field[2], max:1},
                {name: field[3], max:1},
                {name: field[4], max:1}

            ],
            shape: "circle",
            splitNumber:5,
            name:{
                textStyle:{
                    color:'rgb(238,197,102)'
                }
            },
            splitLine:{
                lineStyle:{
                    color:[
                        'rgba(238, 197, 102, 0.1)', 'rgba(238, 197, 102, 0.2)',
                        'rgba(238, 197, 102, 0.4)', 'rgba(238, 197, 102, 0.6)',
                        'rgba(238, 197, 102, 0.8)', 'rgba(238, 197, 102, 1)'
                    ].reverse()
                }
            },
            splitArea:{
                show:false
            },
            axisLine:{
                lineStyle:{
                    color:'rgba(238,197,102,0.5)'
                }
            }
        },
        series:[
            {
                name: 'userRadar',
                type: 'radar',
                lineStyle: lineStyle,
                itemStyle: {normal: {areaStyle: {type: 'default'}}},
                data: data,
                symbol: 'none',
                // itemStyle: {
                //     normal: {
                //         color: '#F9713C'
                //     }
                // },
                // areaStyle: {
                //     normal: {
                //         opacity: 0.3
                //     }
                // }
            },
        ]
    };

    radar_compare.hideLoading();
    radar_compare.setOption(option);
    return radar_compare;

    //radar chart ends!
}


var RecList={
    init:function(){
        this.gridsFather = $("#recPart");
        this.lastGrid = $(".recItem").eq(0);
        this.clear = $("<div class=\"clearfix\"> </div>" );
    },
    updateList:function(recList){
        this.gridsFather.empty();
        var _this = this;
        $.each(recList,function(i,user){
            var tempGrid = _this.lastGrid.clone(true);
            tempGrid.find('.repo').eq(0).text(user.publicRepo);
            tempGrid.find('.following').eq(0).text(user.following);
            tempGrid.find('.followed').eq(0).text(user.follower);
            
            var cherish_rec = tempGrid.find('.cherish').eq(0);
            cherishPresent(user.isStared,cherish_rec);
            onIdCherishClick(cherish_rec,user.login,'/user/star','/user/unstar');

            var background = tempGrid.find('.ch-img-1').eq(0);
            background.css('background-image','url('+user.avatarUrl+')');
            background.css('background-size','150px');

            tempGrid.find('.userName').eq(0).text(user.login);
            tempGrid.find('.createAt').eq(0).text('Create At '+user.createAt);
            tempGrid.find('.detail').eq(0).attr('href','userDetail.html?userName='+user.login);

            _this.gridsFather.append(tempGrid);

        })
    }
}
