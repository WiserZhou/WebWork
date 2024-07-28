<template>
  <div class="mainBox">
    <!-- 内容标题 -->
    <ContentHeader content="课程管理"></ContentHeader>

    <!-- 查询表单 -->
    <div class="query">
      <el-form :inline="true" :model="queryForm" class="demo-form-inline">
        <el-form-item label="课程名称">
          <el-input v-model="queryForm.name" placeholder="请输入课程名称" style="width: 130px;" clearable/>
        </el-form-item>
        <el-form-item label="上课地点">
          <el-input v-model="queryForm.classRoom" placeholder="请输入上课地点" @input="handleChange1(form.classRoom)"
                    style="width: 130px;"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 添加课程按钮 -->
    <el-button type="primary" @click="handleAdd">+&nbsp;添加课程</el-button>

    <!-- 批量删除按钮 -->
    <el-button type="primary" @click="handleBatchDelete">-&nbsp;批量删除</el-button>

    <!-- 课程信息表格 -->
    <div class="info-table">
      <el-table :data="tableData" style="width: 100%;height: calc(100vh - 320px);" :fit="true"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column property="courseId" label="课程编号" align="center"/>
        <el-table-column property="name" label="课程名称" align="center"/>
        <el-table-column property="classRoom" label="上课地点" align="center"/>
        <el-table-column property="attribute" label="课程性质" align="center"/>
        <el-table-column property="totalTime" label="总学时" align="center"/>
        <el-table-column property="score" label="学分" align="center"/>
        <el-table-column label="操作" align="center" width="250">
          <template #default="scope">
            <el-button size="small" type="primary" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页器 -->
    <div class="foot-pagination">
      <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="slot, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange">
        <template #default>
          <span class="el-pagination__total">共{{ total }}条数据</span>
        </template>
        <template #jumper>
          <span>前往{{ jumper }}页</span>
        </template>
      </el-pagination>
    </div>

    <!-- 批量删除对话框 -->
    <el-dialog v-model="dialogBatchVisible" width="500">
      <ContentHeader content="批量删除课程"></ContentHeader>
      <div class="info-message">您确定要批量删除这些课程的信息吗 ?</div>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="confirmDelete(1)">
            确定
          </el-button>
          <el-button @click="dialogBatchVisible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 删除课程对话框 -->
    <el-dialog v-model="dialogVisible" width="500">
      <ContentHeader content="删除课程"></ContentHeader>
      <div class="info-message">您确定要删除该课程的信息吗？</div>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="confirmDelete(0)">
            确定
          </el-button>
          <el-button @click="dialogVisible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 添加/编辑课程对话框 -->
    <el-dialog v-model="dialogFormVisible" width="500">
      <ContentHeader :content="dialogContent"></ContentHeader>
      <el-form :model="form" style="margin-top: 15px;" ref="ruleFormRef" :rules="rules">
        <el-form-item label="课程编号" :label-width="formWidth" prop="courseId">
          <el-input v-model="form.courseId" placeholder="请输入课程代码" style="width: 280px;"/>
        </el-form-item>
        <el-form-item label="课程名称" :label-width="formWidth" prop="name">
          <el-input v-model="form.name" placeholder="请输入课程名称" style="width: 280px;"/>
        </el-form-item>
        <el-form-item label="课程性质" :label-width="formWidth" prop="attribute">
          <el-select
              v-model="form.attribute"
              filterable
              clearable
              placeholder="请选择"
              style="width: 280px;">
            <el-option
                v-for="item in attributeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="上课地点" :label-width="formWidth" prop="classRoom">
          <el-input v-model="form.classRoom" placeholder="请输入上课地点" style="width: 280px;"/>
        </el-form-item>
        <el-form-item label="学分" :label-width="formWidth" prop="score">
          <el-input v-model="form.score" placeholder="请输入课程学分" @input="handleChange2(form.score)"
                    style="width: 280px;"/>
        </el-form-item>
        <el-form-item label="总学时" :label-width="formWidth" prop="totalTime">
          <el-input v-model="form.totalTime" placeholder="请输入总学时" @input="handleChange1(form.totalTime)"
                    style="width: 280px;"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="handleSave(ruleFormRef)">
            保存
          </el-button>
          <el-button @click="dialogFormVisible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import ContentHeader from '../../components/ContentHeader.vue';
import apiAxios from '@/api/ApiAxios.js';
import {ElMessage} from 'element-plus';
import {reactive, ref} from 'vue';

// 处理输入改变事件，限制输入内容
const handleChange1 = (value) => {
  if (!/^[0-9]*$/.test(value)) {
    ElMessage.error("请输入数字！");
    form.value.totalTime = value.replace(/[^0-9]/g, '');
  } else {
    if (parseInt(value) > 160) {
      ElMessage.error("学时不能大于160！");
      form.value.totalTime = "160";
    }
  }
}

// 处理输入改变事件，限制输入内容
const handleChange2 = (value) => {
  if (!/^\d+(\.\d+)?$/.test(value) && value != '') {
    ElMessage.error('请输入小数或整数！');
    form.value.score = value.replace(/[^\d.]/g, '');
  } else {
    const floatValue = parseFloat(value);
    if (floatValue > 10) {
      ElMessage.error('学分不能大于10！');
      form.value.score = '10';
    }
  }
}

// 自定义校验规则：用户名只能包含数字和字母
function validateId(rule, value) {
  const pattern = /^[A-Za-z0-9]+$/;
  if (!pattern.test(value)) {
    return Promise.reject('用户名只能包含数字和字母');
  } else {
    return Promise.resolve();
  }
}

// 自定义校验规则：姓名只能包含汉字
function validateName(rule, value) {
  const pattern = /^[\u4e00-\u9fa5]+$/;
  if (!pattern.test(value)) {
    return Promise.reject('姓名只能包含汉字');
  } else {
    return Promise.resolve();
  }
}

// 表单校验规则
const rules = reactive({
  courseId: [
    {required: true, message: '请输入课程代码', trigger: 'blur'},
    {min: 2, max: 10, message: '课程代码应为2-10位', trigger: 'blur'},
    {validator: validateId, trigger: 'blur'}
  ],
  name: [
    {required: true, message: '请输入课程名称', trigger: 'blur'},
    {min: 2, max: 20, message: '课程名称应为2-20位', trigger: 'blur'},
    {validator: validateName, trigger: 'blur'}
  ],
  attribute: [
    {required: true, message: '请选择课程性质', trigger: 'change'},
  ],
  classRoom: [
    {required: true, message: '请选择上课地点', trigger: 'change'},
  ],
  score: [
    {required: true, message: '请输入学分', trigger: 'blur'},
  ],
  totalTime: [
    {required: true, message: '请输入总学时', trigger: 'blur'},
  ],
})

const ruleFormRef = reactive({
  id: '',
  courseId: '',
  name: '',
  attribute: '',
  classRoom: '',
  score: '',
  totalTime: ''
})

// 初始化数据
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(1);
let attributeOptions = [
  {
    label: "必修",
    value: 1,
  },
  {
    label: "选修",
    value: 2,
  }
];
let attributeMap = {
  1: "必修",
  2: "选修"
};
const queryForm = reactive({
  name: "",
  attribute: null,
  classRoom: null,
})
const tableData = ref([]);

// 获取课程列表
function getCourseList() {
  let params = {
    page: currentPage.value,
    pageSize: pageSize.value,
    name: queryForm.name,
    attribute: queryForm.attribute,
    classRoom: queryForm.classRoom
  }
  apiAxios({
    url: '/course',
    method: 'get',
    params
  }).then(res => {
    console.log(res.data)
    tableData.value = res.data.data.rows;
    tableData.value.forEach(x => {
      x.attribute = attributeMap[x.attribute];
    });
    total.value = res.data.data.total;
  }).catch(err => {
    console.log(err.message);
  })
}

// 初始化数据
getCourseList();

const dialogBatchVisible = ref(false)
const dialogVisible = ref(false)
const dialogContent = ref("添加课程");
const form = ref({});
const dialogFormVisible = ref(false)
const formWidth = '140px'

// 保存课程信息
async function handleSave(formEl) {
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      let method = '';
      if (dialogContent.value === "添加课程") {
        method = 'post';
      } else if (dialogContent.value === "编辑课程") {
        method = 'put';
      }
      console.log(method)
      console.log(form.value)
      apiAxios({
        url: "/course",
        method: method,
        data: form.value,
      }).then(res => {
        console.log(res.data)
        if (res.data.code === 200) {
          dialogFormVisible.value = false;
          ElMessage({
            message: '保存成功！',
            type: 'success',
          })
          getCourseList();
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

// 提交查询
const onSubmit = () => {
  getCourseList();
}

// 添加课程
function handleAdd() {
  form.value = {
    courseId: "",
    name: "",
    attribute: null,
    classRoom: null,
    score: null,
    totalTime: null,
  }
  dialogContent.value = "添加课程";
  dialogFormVisible.value = true;
}

// 批量删除课程
function handleBatchDelete() {
  dialogBatchVisible.value = true;
}

// 选择项变化
const multipleSelection = ref([])
const handleSelectionChange = (val) => {
  multipleSelection.value = val;
}

// 编辑课程
const handleEdit = (index, row) => {
  apiAxios({
    url: "/course/" + row.id,
    method: 'get',
  }).then(res => {
    console.log(res.data)
    form.value = res.data.data;
  }).catch(err => {
    console.log(err.message);
  })
  dialogContent.value = "编辑课程";
  dialogFormVisible.value = true;
}

// 删除课程
const temp_id = ref(0);
const handleDelete = (index, row) => {
  temp_id.value = row.id;
  dialogVisible.value = true;
}

// 确认删除
function confirmDelete(type) {
  let ids = "";
  if (type) {
    multipleSelection.value.forEach(x => {
      if (ids === "") ids = x.id;
      else ids += "," + x.id;
    })
  } else {
    ids = temp_id.value;
  }
  apiAxios({
    url: "/course/" + ids,
    method: "delete",
  }).then(res => {
    if (res.data.code === 200) {
      ElMessage({
        message: '删除成功！',
        type: 'success',
      })
      getCourseList();
      dialogVisible.value = false;
      dialogBatchVisible.value = false;
    }
  })
}

// 分页器事件
const handleSizeChange = (val) => {
  getCourseList();
}
const handleCurrentChange = (val) => {
  getCourseList();
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

.query {
  height: 60px;
  margin-bottom: 20px;
}

.demo-form-inline {
  display: flex;
  padding-top: 20px;
}

.info-table {
  margin-top: 20px;
}

.foot-pagination {
  position: absolute;
  bottom: 5px;
  right: 10px;
}
</style>
