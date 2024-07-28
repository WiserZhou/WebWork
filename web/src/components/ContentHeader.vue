<template>
  <!-- 主要内容显示区域 -->
  <div class="main">
    <!-- 使用props中的内容 -->
    <span>{{ props.content }}</span>
  </div>
</template>

<script setup>
// 导入defineProps函数来定义props
import {defineProps} from 'vue';

// 定义props内容为content
const props = defineProps(["content"]);
</script>

<style scoped>
.main {
  /* 主要内容显示区域样式 */
  display: flex;
  align-items: center;
  height: 1cm;
  /* 左边框样式 */
  border-left: 7px solid #02A7F0FF;
}

.main span {
  /* 文字样式 */
  font: 400 18px 'Leto';
  /* 左右边距 */
  margin: 0 10px;
  /* 字体颜色 */
  color: #02A7F0;
}
</style>
