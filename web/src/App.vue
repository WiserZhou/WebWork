<script setup>
import {RouterView} from 'vue-router'
</script>

<template>
  <RouterView id="router"/>
</template>

<style scoped>
#router {
  width: 100vw;
  height: 100vh;
}
</style>

<!--scoped属性确保这些CSS规则仅应用于当前组件，不会影响到其他组件的样式。-->