/**
 * Created by darxan on 2016/5/19.
 */

var location_port = '';
$(document).ready(
    function () {
        
        var fullName = location.search;
        var index = location.search.indexOf("?");

        if(index==-1){
            wrongUrl();
        }else{
            var params = fullName.substr(index+1);
            var names = params.split("/");

            
            if(names.length<2){
                wrongUrl();
            }else{
                var url = location_port+"/repo/"+params;
                var radar_repo =echarts.init(document.getElementById("repoRadar"));
                radar_repo.showLoading();
                $.get(url,function (object) {
                    setBasicInfo(object.basicInfo);
                    setRecommend(object.relatedRepo);
                    setRadar(radar_repo,object.radarChart);
                });
                
                setContributorsCommit(params);
                setCodeFrequency(params);


            }

        }
    }
);

function setBasicInfo(basicInfo) {
    var numbers = $("#numbers");
    var descriptions = $("#descriptions");
    var header = $("#ownerAvatarUrl");


    descriptions.find("#reponame").text(basicInfo.reponame);
    descriptions.find("#ownerName").text(basicInfo.ownerName);
    descriptions.find("#description").text(basicInfo.description);
    descriptions.find("#language").text(basicInfo.language);
    descriptions.find("#createAt").text(basicInfo.createAt);
    descriptions.find("#updateAt").text(basicInfo.updateAt);
    descriptions.find("#url").text(basicInfo.url);
    descriptions.find("#url").attr("href",basicInfo.url);

    numbers.find("#numStar").text(basicInfo.numStar);
    numbers.find("#numFork").text(basicInfo.numFork);
    numbers.find("#numSubscriber").text(basicInfo.numSubscriber);
    numbers.find("#size").text(basicInfo.size);

    header.attr("src",basicInfo.ownerAvatarUrl);
}

function setRecommend(recommend) {
    // console.log(recommend);
    // var fatherNode = $('#recommendRepos');
    // var recommendNode = fatherNode.find('#recommend-item');
    // fatherNode.empty();
    // $.each(recommend,function (i,item) {
    //     var newNode = recommendNode.clone();
    //     newNode.find("#recommend_ownerAvatarUrl").attr('src',item.ownerAvatarUrl);
    //     newNode.find("#recommend_updateAt").attr('src',item.updateAt);
    //     newNode.find("#recommend_description").attr('src',item.description);
    //     newNode.find("#recommend_numStar").text(item.numStar);
    //     newNode.find("#recommend_numFork").text(item.numFork);
    //     newNode.find("#recommend_numSubscriber").text(item.numSubscriber);
    //
    //     fatherNode.add(newNode);
    // });
}

function setRadar(radar_repo,radarChart) {
    
    var lineStyle = {
        normal: {
            width: 1,
            opacity: 0.5
        }
    }
    option = {
        // backgroundColor: 'white',
        tooltip:{
        },
        radar:{
            indicator:[
                {name: radarChart.field[0], max:1},
                {name: radarChart.field[1], max:1},
                {name: radarChart.field[2], max:1},
                {name: radarChart.field[3], max:1},
                {name: radarChart.field[4], max:1}

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
                name: 'repoRadar',
                type: 'radar',
                lineStyle: lineStyle,
                data: [radarChart.value],
                symbol: 'none',
                itemStyle: {
                    normal: {
                        color: '#F9713C'
                    }
                },
                areaStyle: {
                    normal: {
                        opacity: 0.3
                    }
                }
            },
        ]
    };
    radar_repo.setOption(option);
    radar_repo.hideLoading();
    window.onresize = radar_repo.resize;
}


function setContributorsCommit(params) {
    
    var fatherNode = document.getElementById('contributorsCommit');
    var url_contributrorsCommit = "/repo/"+params+"/graph/commit_by_contributor";
    function addNode(item) {
        var node = document.createElement("div");
        node.style.height = "20em";
        fatherNode.appendChild(node);
        var chart = echarts.init(node);

        option = {
            tooltip: {
                trigger: 'axis',
                position: function (pt) {
                    return [pt[0], '10%'];
                }
            },
            title: {
                left: 'center',
                text: item.contributorName,
            },
            legend: {
                top: 'bottom',
                data:['意向']
            },
            toolbox: {
                show: true,
                feature: {
                    dataView: {show: true, readOnly: false},
                    magicType: {show: true, type: ['line', 'bar', 'stack', 'tiled']},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: item.field
            },
            yAxis: {
                type: 'value',
                boundaryGap: [0, '100%']
            },
            dataZoom: [{
                type: 'inside',
                start: 0,
                end: 100
            }, {
                start: 0,
                end: 100
            }],
            series: [
                {
                    name:'commits time',
                    type:'line',
                    smooth:true,
                    symbol: 'none',
                    sampling: 'average',
                    itemStyle: {
                        normal: {
                            color: 'rgb(255, 70, 131)'
                        }
                    },
                    areaStyle: {
                        normal: {
                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                offset: 0,
                                color: 'rgb(255, 158, 68)'
                            }, {
                                offset: 1,
                                color: 'rgb(255, 70, 131)'
                            }])
                        }
                    },
                    data: item.value
                }
            ]
        };
        chart.setOption(option);
        window.onresize = chart.resize;
    }


    $.get(url_contributrorsCommit,function (list) {
        addNode(list['all']);
        for (var index in list){
            if(index!='all'){
                addNode(list[index]);
            }
        }
    });

}

function setCodeFrequency(params) {
    var url_codeFrequency = "/repo/"+params+"/graph/code_frequency";
    var node = document.getElementById("codeFrequency");
    var chart = echarts.init(node);
    chart.showLoading();
    $.get(url_codeFrequency,function (codeFrequency) {
        option = {
            tooltip: {
                trigger: 'axis',
                position: function (pt) {
                    return [pt[0], '10%'];
                }
            },
            title: {
                left: 'center',
                text: 'codeFrequency',
            },
            legend: {
                top: 'bottom',
                data:['意向']
            },
            toolbox: {
                show: true,
                feature: {
                    dataView: {show: true, readOnly: false},
                    magicType: {show: true, type: ['line', 'bar', 'stack', 'tiled']},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: codeFrequency.field
            },
            yAxis: {
                type: 'value',
                boundaryGap: [0, '100%']
            },
            dataZoom: [{
                type: 'inside',
                start: 0,
                end: 100
            }, {
                start: 0,
                end: 100
            }],
            series: [
                {
                    name:'commits time',
                    type:'line',
                    smooth:true,
                    symbol: 'none',
                    sampling: 'average',
                    itemStyle: {
                        normal: {
                            color: 'rgb(255, 70, 131)'
                        }
                    },
                    areaStyle: {
                        normal: {
                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                offset: 0,
                                color: 'rgb(255, 158, 68)'
                            }, {
                                offset: 1,
                                color: 'rgb(255, 70, 131)'
                            }])
                        }
                    },
                    data: codeFrequency.add
                },
                {
                    name:'commits time',
                    type:'line',
                    smooth:true,
                    symbol: 'none',
                    sampling: 'average',
                    itemStyle: {
                        normal: {
                            color: 'rgb(31, 58, 147)'
                        }
                    },
                    areaStyle: {
                        normal: {
                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                offset: 0,
                                color: 'rgb(255, 158, 68)'
                            }, {
                                offset: 1,
                                color: 'rgb(255, 70, 131)'
                            }])
                        }
                    },
                    data: codeFrequency.delete
                },
            ]
        };
        chart.setOption(option);
        chart.hideLoading();
        window.onresize = chart.resize;
    });


}

function punchCard() {
    
}

function wrongUrl() {
    alert("wrong url");
}