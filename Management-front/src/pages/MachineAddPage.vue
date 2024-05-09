<template>
  <van-notice-bar color="#1989fa" background="#ecf9ff" left-icon="info-o">
    若不设置状态则默认空闲
  </van-notice-bar>
  <van-form @submit="onSubmit">
    <van-cell-group inset>
      <van-field
          v-model="addMachineData.machineName"
          name="name"
          label="名称"
          placeholder="请输入设备名称"
          :rules="[{ required: true, message: '请输入设备名称' }]"
      />
      <van-field
          v-model="addMachineData.notes"
          rows="1"
          autosize
          label="描述"
          type="textarea"
          placeholder="请输入设备描述"
      />
      <van-field
          v-model="addMachineData.serialNumber"
          rows="2"
          autosize
          label="序列号"
          type="textarea"
          placeholder="请输入设备序列号"
      />
      <van-field name="radio" label="状态">
        <template #input>
          <van-radio-group
              v-model="addMachineData.machineStatus"
              direction="horizontal"
          >
            <van-radio name="0">空闲</van-radio>
            <van-radio name="1">使用</van-radio>
            <van-radio name="2">维修</van-radio>
          </van-radio-group>
        </template>
      </van-field>
    </van-cell-group>
    <div style="margin: 16px">
      <van-button round block type="primary" native-type="submit">
        添加
      </van-button>
    </div>
  </van-form>
</template>

<script setup lang="ts">
import myAxios from "../plugins/myAxios.js";
import {showFailToast, showSuccessToast} from "vant";
import {ref} from "vue";


const initFormData = {
  machineName: "",
  notes: "",
  machineStatus: "0",
  serialNumber: ""
};
const addMachineData = ref({ ...initFormData });
const onSubmit = async () => {
  const postData = {
    ...addMachineData.value,
    status: Number(addMachineData.value.machineStatus),
  };
  //todo 前端校验
  const res = await myAxios.post("/machine/add", postData);
  if (res?.data.code === 0) {
    showSuccessToast("添加成功");
    const machineIdStr=String(res.data.data);
    location.href=`/after/machine?id=${machineIdStr}`;
  } else {
    showFailToast(
        "添加失败" +
        (res.data.description ? `,${res.data.description}` : ""),
    );
  }
};
</script>

<style scoped>

</style>
