<template>
  <div class="mainBox">
    <!-- 内容标题 -->
    <ContentHeader content="部门管理"></ContentHeader>

    <!-- 新增部门按钮 -->
    <el-button type="primary" @click="handleAdd" style="margin-top: 20px;">+ 新增部门</el-button>

    <!-- 部门信息表格 -->
    <div class="info-table">
      <el-table :data="tableData" style="width: 100%" :fit="true">
        <el-table-column type="index" label="序号" width="100" align="center"/>
        <el-table-column property="name" label="部门名称" align="center"/>
        <el-table-column property="updateTime" label="最后操作时间" :formatter="dateFormatter" align="center"/>
        <el-table-column label="操作" align="center">
          <template #default="scope">
            <!-- 编辑按钮 -->
            <el-button size="small" type="primary" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <!-- 删除按钮 -->
            <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 删除部门对话框 -->
    <el-dialog v-model="dialogVisible" width="500">
      <ContentHeader content="删除部门"></ContentHeader>
      <div class="info-message">您确定要删除该部门吗？</div>
      <template #footer>
        <div class="dialog-footer">
          <!-- 确认删除按钮 -->
          <el-button type="primary" @click="confirmDelete()">确定</el-button>
          <!-- 取消按钮 -->
          <el-button @click="dialogVisible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 新增/编辑部门对话框 -->
    <el-dialog v-model="dialogFormVisible" width="500">
      <ContentHeader :content="dialogContent"></ContentHeader>
      <div class="info-message" style="display: flex;font-size: 14px;align-items: center;">
        <span style="width: 100px;">* 部门名称</span>
        <!-- 部门名称输入框 -->
        <el-input v-model="name" placeholder="请输入部门名称，长度为2-10位" clearable/>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <!-- 保存按钮 -->
          <el-button type="primary" @click="handleSave">保存</el-button>
          <!-- 取消按钮 -->
          <el-button @click="dialogFormVisible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import ContentHeader from '../../components/ContentHeader.vue';
import {ElMessage} from 'element-plus';
import {ref} from 'vue';
import apiAxios from '@/api/ApiAxios.js';
import moment from 'moment';

// 引入组件和依赖库后，设置初始变量和函数
const temp_id = ref(0); // 临时id
const dialogVisible = ref(false); // 对话框可见性
const dialogContent = ref("新增部门"); // 对话框内容
const name = ref(""); // 部门名称
const dialogFormVisible = ref(false); // 对话框表单可见性
const tableData = ref([]); // 表格数据

// 日期格式化函数
const dateFormatter = (row) => {
  const updateTime = row.updateTime;
  const date = moment(updateTime);
  return date.format("YYYY-MM-DD HH:mm:ss");
}

// 获取部门列表函数
async function getDeptList() {
  try {
    const res = await apiAxios({
      url: '/dept',
      method: 'get',
    });
    console.log(res.data)
    tableData.value = res.data.data;
  } catch (err) {
    console.log(err.message);
  }
}

// 初始化部门列表数据
getDeptList();

// 保存部门信息函数
function handleSave() {
  if (!(name.value.length >= 2 && name.value.length <= 10)) {
    ElMessage.error('请输入长度为2-10位的名称！');
  } else {
    let data;
    let method;
    if (dialogContent.value === "新增部门") {
      method = 'post';
      data = {name: name.value};
    } else if (dialogContent.value === "编辑部门") {
      method = 'put';
      data = {id: temp_id.value, name: name.value};
    }
    apiAxios({
      url: '/dept',
      method: method,
      data: data,
    }).then(res => {
      if (res.data.code === 200) {
        dialogFormVisible.value = false;
        ElMessage({message: '保存成功！', type: 'success'});
        getDeptList();
      } else {
        ElMessage.error(res.data.msg);
      }
    }).catch(err => {
      console.log(err.message);
    });
  }
}

// 新增部门函数
function handleAdd() {
  name.value = "";
  dialogContent.value = "新增部门";
  dialogFormVisible.value = true;
}

// 编辑部门函数
const handleEdit = (index, row) => {
  temp_id.value = row.id;
  name.value = row.name;
  dialogContent.value = "编辑部门";
  dialogFormVisible.value = true;
};

// 删除部门函数
const handleDelete = (index, row) => {
  temp_id.value = row.id;
  dialogVisible.value = true;
};

// 确认删除函数
function confirmDelete() {
  apiAxios({
    url: '/dept/' + temp_id.value,
    method: 'delete',
  }).then(res => {
    console.log(res.data)
    if (res.data.code === 200) {
      ElMessage({message: "删除成功!", type: 'success'});
      dialogVisible.value = false;
      getDeptList();
    }
  });
}
</script>

<style scoped>
.mainBox {
  position: relative;
  width: 100%;
  height: 100%;
}

.info-message {
  font-size: 18px;
  margin: 20px 20px 10px;
}

.info-table {
  margin-top: 20px;
}
</style>
