<template>
  <!-- 班级管理页面头部 -->
  <ContentHeader content="班级管理"></ContentHeader>

  <!-- 查询表单区域 -->
  <div class="queryForm">
    <el-form :inline="true" :model="formQuery" class="demo-form-inline">
      <!-- 班级名称输入框 -->
      <el-form-item label="班级名称">
        <el-input v-model="formQuery.name" placeholder="请输入班级名称" style="width: 180px;"/>
      </el-form-item>
      <!-- 课程时间选择器 -->
      <el-form-item label="课程时间">
        <el-date-picker
            v-model="formQuery.period"
            type="daterange"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            :default-value="[new Date(), new Date()]"
            value-format="YYYY-MM-DD"/>
      </el-form-item>
      <!-- 查询按钮 -->
      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
      </el-form-item>
    </el-form>
  </div>

  <!-- 新增班级按钮 -->
  <el-button type="primary" @click="handleAdd" style="margin-bottom: 10px">新增班级</el-button>

  <!-- 班级信息表格区域 -->
  <div class="tableInfo">
    <el-table :data="tableData" :fit="true">
      <!-- 序号列，动态计算序号 -->
      <el-table-column type="index" label="序号" width="100" align="center">
        <template #default="scope">
          {{ (currentPage - 1) * pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <!-- 班级名称列 -->
      <el-table-column property="name" label="班级名称" align="center"/>
      <!-- 班级教室列 -->
      <el-table-column property="classroom" label="班级教室" align="center"/>
      <!-- 开课时间列 -->
      <el-table-column property="startTime" label="开课时间" align="center"/>
      <!-- 结课时间列 -->
      <el-table-column property="endTime" label="结课时间" align="center"/>
      <!-- 班主任列 -->
      <el-table-column property="teacher" label="班主任" align="center"/>
      <!-- 操作列，包含编辑和删除按钮 -->
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button size="small" type="primary" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <!-- 添加/编辑班级对话框 -->
  <el-dialog v-model="isFormDialogVisible" width="500">
    <!-- 对话框标题 -->
    <ContentHeader :content="dialogContent"></ContentHeader>
    <!-- 表单内容，用于添加或编辑班级信息 -->
    <el-form :model="form" style="margin-top: 15px;" ref="ruleFormRef" :rules="rules">
      <!-- 班级名称输入 -->
      <el-form-item label="班级名称" :label-width="formWidth" prop="name">
        <el-input v-model="form.name" placeholder="请输入班级名称，如：2024第01期10班" style="width: 280px;"/>
      </el-form-item>
      <!-- 班级教室输入 -->
      <el-form-item label="班级教室" :label-width="formWidth" prop="classroom">
        <el-input v-model="form.classroom" placeholder="请填写班级教室" style="width: 280px;"/>
      </el-form-item>
      <!-- 开课时间选择 -->
      <el-form-item label="开课时间" :label-width="formWidth" prop="startTime">
        <el-date-picker
            v-model="form.startTime"
            type="date"
            placeholder="请选择开课时间"
            size="default"
            style="width: 280px;"
            value-format="YYYY-MM-DD"/>
      </el-form-item>
      <!-- 结课时间选择 -->
      <el-form-item label="结课时间" :label-width="formWidth" prop="endTime">
        <el-date-picker
            v-model="form.endTime"
            type="date"
            placeholder="请选择结课时间"
            size="default"
            style="width: 280px;"
            value-format="YYYY-MM-DD"/>
      </el-form-item>
      <!-- 班主任下拉选择 -->
      <el-form-item label="班主任" :label-width="formWidth" prop="teacherId">
        <el-select
            v-model="form.teacherId"
            filterable
            clearable
            placeholder="请选择"
            style="width: 280px;">
          <el-option
              v-for="item in empOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
        </el-select>
      </el-form-item>
    </el-form>
    <!-- 对话框底部操作按钮 -->
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="handleSave(ruleFormRef)">保存</el-button>
        <el-button @click="isFormDialogVisible = false">取消</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 删除确认对话框 -->
  <el-dialog v-model="dialogVisible" width="500">
    <!-- 对话框标题 -->
    <ContentHeader content="删除班级"></ContentHeader>
    <!-- 删除提示信息 -->
    <div class="info-message">确定要删除该班级吗？</div>
    <!-- 对话框底部操作按钮 -->
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="confirmDelete">确定</el-button>
        <el-button @click="dialogVisible = false">取消</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 分页组件 -->
  <el-pagination class="foot-pagination"
                 v-model:current-page="currentPage"
                 v-model:page-size="pageSize"
                 :page-sizes="[10, 20, 50, 100]"
                 layout="slot, sizes, prev, pager, next, jumper"
                 :total="total"
                 @size-change="handleSizeChange"
                 @current-change="handleCurrentChange">
    <!-- 自定义分页信息显示 -->
    <template #default>
      <span class="el-pagination__total">共{{ total }}条数据</span>
    </template>
    <!-- 自定义跳转输入框文字 -->
    <template #jumper>
      <span>前往{{ jumper }}页</span>
    </template>
  </el-pagination>
</template>
<script setup>
// 引入必要的组件和函数
import ContentHeader from '../../components/ContentHeader.vue';
import {ElMessage} from 'element-plus';
import {reactive, ref} from 'vue'
import apiAxios from '@/api/ApiAxios.js';

// 表单和页面控制的数据
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(1);
const dialogVisible = ref(false)
const dialogContent = ref("新增班级");
const form = ref({});
const isFormDialogVisible = ref(false)
const formWidth = '140px'
let empOptions = [];
let empMap = {};

// 使用 reactive 创建响应式对象
const ruleFormRef = reactive({
  name: '',
  classroom: '',
  startTime: '',
  endTime: '',
  teacherId: ''
});

// 表单校验规则
const rules = reactive({
  name: [
    {required: true, message: '请输入班级名称', trigger: 'blur'},
    {min: 4, max: 30, message: '班级名称长度应为4-30字符', trigger: 'blur'},
    {validator: validateX, trigger: 'blur'}
  ],
  classroom: [
    {required: true, message: '请输入班级教室', trigger: 'blur'},
    {min: 4, max: 30, message: '班级名称长度应为4-30字符', trigger: 'blur'},
    {validator: validateX, trigger: 'blur'}
  ],
  startTime: [
    {required: true, message: '请选择开课时间', trigger: 'change'}
  ],
  endTime: [
    {required: true, message: '请选择结课时间', trigger: 'change'}
  ],
  teacherId: [
    {required: true, message: '请选择班主任', trigger: 'change'}
  ]
});

function validateX(rule, value) {
  const pattern = /^[A-Za-z0-9\u4e00-\u9fa5]+$/;
  return pattern.test(value) ? Promise.resolve() : Promise.reject('用户名只能包含汉字、字母和数字');
}


// 获取员工列表的函数
function getEmpList() {
  apiAxios({
    url: '/emp/list',
    method: 'get',
  }).then(res => {
    console.log(res.data);
    const tempData = res.data.data;
    tempData.forEach(x => {
      empOptions.push({
        label: x.name,
        value: x.id
      });
      empMap[x.id] = x.name;
    })
  }).catch(err => {
    console.log(err.message);
  })
}

getEmpList();

// 查询表单的数据和班级列表数据
const formQuery = reactive({
  name: "",
  period: [null, null]
})

const tableData = ref([]);

function getClassList() {
  if (!formQuery.period) {
    formQuery.period = [null, null];
  }
  const params = {
    page: currentPage.value,
    pageSize: pageSize.value,
    name: formQuery.name,
    begin: formQuery.period[0],
    end: formQuery.period[1],
  }
  console.log(params);
  apiAxios({
    url: '/class',
    method: 'get',
    params
  }).then(res => {
    console.log(res.data);
    tableData.value = res.data.data.rows;
    tableData.value.forEach(x => {
      x.teacher = empMap[x.teacherId];
    });
    total.value = res.data.data.total;
  }).catch(err => {
    console.log(err.message);
  })
}

getClassList();

// 查询班级列表的提交函数
const onSubmit = () => {
  getClassList();
}

// 处理保存按钮的函数
async function handleSave(formEl) {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      console.log('submit!')
      let method = '';
      if (dialogContent.value === "新增班级") {
        method = 'post';
      } else if (dialogContent.value === "修改班级") {
        method = 'put';
      }
      console.log(form.value);
      apiAxios({
        url: "/class",
        method: method,
        data: form.value,
      }).then(res => {

        console.log(res.data);
        if (res.data.code === 200) {
          isFormDialogVisible.value = false;
          ElMessage({
            message: '保存成功！',
            type: 'success',
          })
          getClassList();
        } else {
          ElMessage.error(res.data.msg);
        }
      }).catch(err => {
        console.log(err.message);
      })
    } else {
      console.log('error submit!', fields)
    }
  })

}

// 处理添加按钮的函数
function handleAdd() {
  form.value = {
    "id": null,
    "name": "",
    "classroom": "",
    "startTime": null,
    "endTime": null,
    "teacherId": null
  }
  dialogContent.value = "新增班级";
  isFormDialogVisible.value = true;
}

// 处理编辑按钮的函数
const handleEdit = (index, row) => {
  console.log(index, row)
  apiAxios({
    url: "/class/" + row.id,
    method: 'get',
  }).then(res => {
    console.log("handleEdit")
    console.log(res.data);
    form.value = res.data.data;
  }).catch(err => {
    console.log(err.message);
  })
  dialogContent.value = "修改班级";
  isFormDialogVisible.value = true;
}

// 临时存放删除对象ID的变量和处理删除按钮的函数
const tempId = ref(0);
const handleDelete = (index, row) => {
  console.log(index, row)
  tempId.value = row.id;
  dialogVisible.value = true;
}

// 确认删除的函数
function confirmDelete() {
  apiAxios({
    url: "/class/" + tempId.value,
    method: "delete",
  }).then(res => {
    console.log(res.data);
    if (res.data.code === 200) {
      ElMessage({
        message: '删除成功！',
        type: 'success',
      })
      getClassList();
      dialogVisible.value = false;
    } else {
      ElMessage.error(res.data.msg);
    }
  })
}

// 处理分页变化的函数
const handleSizeChange = (val) => {
  console.log(`${val} items per page`)
  getClassList();
}
const handleCurrentChange = (val) => {
  console.log(`current page: ${val}`)
  getClassList();
}

</script>

<style scoped>
.demo-form-inline {
  --el-input-width: 100px;
}

.demo-form-inline {
  --el-select-width: 100px;
}

.info-message {
  font-size: 18px;
  margin: 20px 20px 10px;
}

.queryForm {
  height: 60px;
  margin-bottom: 10px;
}

.demo-form-inline {
  display: flex;
  padding-top: 20px;
}

.tableInfo {
  margin-top: 20px;
}

.foot-pagination {
  position: absolute;
  bottom: 5px;
  right: 10px;
}
</style>
