<template>
  <div class="emp-data-wrapper">
    <div id="genderCount"></div>
    <div id="educationCount"></div>
  </div>
</template>

<script setup>
import * as echarts from 'echarts'; // 导入echarts库
import apiAxios from '@/api/ApiAxios.js'; // 导入接口请求函数
import {onMounted} from 'vue'; // 导入Vue的onMounted钩子函数

let echart = echarts; // 定义echart变量

// 初始化性别统计图表
async function initGenderChart() {
  let label = [];
  let data = [];
  try {
    const res = await apiAxios({
      url: "/student/genderCount",
      method: 'get'
    })
    for (let key in res.data.data) {
      label.push(key);
      data.push({
        value: res.data.data[key],
        name: key
      })
    }
  } catch (err) {
    console.log(err);
  }

  let genderChart = echart.init(document.getElementById("genderCount"));
  let option = {
    title: {
      text: '学员性别信息统计饼状图',
      x: 'center',
      textStyle: {
        fontSize: 30
      }
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      top: '5%',
      left: 'center'
    },
    series: [
      {
        name: 'Access From',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 40,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: data
      }
    ]
  };
  genderChart.setOption(option);
  window.onresize = function () {
    genderChart.resize();
  };
}

// 初始化教育经历统计图表
async function initEducationChart() {
  let label = [];
  let data = [];
  try {
    const res = await apiAxios({
      url: "/student/educationCount",
      method: 'get'
    })
    for (let key in res.data.data) {
      label.push(key);
      data.push(res.data.data[key]);
    }
  } catch (err) {
    console.log(err);
  }
  let educationChart = echart.init(document.getElementById("educationCount"));
  let option = {
    title: {
      text: '学员教育经历统计柱状图',
      x: 'center',
      textStyle: {
        fontSize: 30
      }
    },
    tooltip: {
      trigger: 'item'
    },
    xAxis: {
      data: label,
      axisLabel: {
        inside: true,
        color: '#fff'
      },
      axisTick: {
        show: false
      },
      axisLine: {
        show: false
      },
    },
    yAxis: {
      axisLine: {
        show: false
      },
      axisTick: {
        show: false
      },
      axisLabel: {
        color: '#999'
      }
    },
    dataZoom: [
      {
        type: 'inside'
      }
    ],
    series: [
      {
        type: 'bar',
        showBackground: true,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            {offset: 0, color: '#83bff6'},
            {offset: 0.5, color: '#188df0'},
            {offset: 1, color: '#188df0'}
          ])
        },
        emphasis: {
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              {offset: 0, color: '#2378f7'},
              {offset: 0.7, color: '#2378f7'},
              {offset: 1, color: '#83bff6'}
            ])
          }
        },
        data: data
      }
    ]
  };
  educationChart.setOption(option);
  window.onresize = function () {
    educationChart.resize();
  };
}

// 在组件挂载后调用初始化函数
onMounted(() => {
  initGenderChart();
  initEducationChart();
})
</script>

<style scoped>
.emp-data-wrapper {
  display: flex;
  margin-top: 3rem;
}

#genderCount {
  width: 600px;
  height: 600px;
}

#educationCount {
  width: 800px;
  height: 600px;
  margin-left: 200px;
}
</style>
