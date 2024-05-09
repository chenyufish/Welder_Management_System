<template>
  <van-notice-bar color="#1989fa" background="#ecf9ff" left-icon="info-o">
    若不设置状态则默认空闲
  </van-notice-bar>
  <van-form @submit="onSubmit">
    <van-cell-group inset>
      <van-field
          v-model="updateTeamData.machineName"
          name="name"
          label="名称"
          placeholder="请输入设备名称"
          :rules="[{ required: true, message: '请输入设备名称' }]"
      />
      <van-field
          v-model="updateTeamData.notes"
          rows="1"
          autosize
          label="描述"
          type="textarea"
          placeholder="请输入设备描述"
      />
      <van-field
          v-model="updateTeamData.serialNumber"
          rows="2"
          autosize
          label="序列号"
          type="textarea"
          placeholder="请输入设备序列号"
      />

      <van-field name="radio" label="状态">
        <template #input>
          <van-radio-group v-model="updateTeamData.machineStatus" direction="horizontal">
            <van-radio name="0">空闲</van-radio>
            <van-radio name="1">使用</van-radio>
            <van-radio name="2">维修</van-radio>
          </van-radio-group>
        </template>
      </van-field>

    </van-cell-group>
    <div style="margin: 16px;">
      <van-button round block type="primary" native-type="submit">
        更新
      </van-button>
    </div>
  </van-form>
</template>

<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import myAxios from "../plugins/myAxios.js";
import {showFailToast, showSuccessToast} from "vant";

let router = useRouter();
let route = useRoute();
const updateTeamData = ref({})
const showCalendar = ref(false);
onMounted(async () => {
  const res = await myAxios.get("/machine/" + route.query.id)
  if (res?.data.code === 0) {
    res.data.data.status = String(res.data.data.status)
    updateTeamData.value = res.data.data
  } else {
    showFailToast("队伍查询失败" + (res.data.description ? `,${res.data.description}` : ''))
  }
})
const onSubmit = async () => {
  const postData = {
    ...updateTeamData.value,
    status: Number(updateTeamData.value.machineStatus),
  }
  //todo 前端校验
  const res = await myAxios.post("/machine/update", postData)
  if (res?.data.code === 0) {
    showSuccessToast("更新成功")
    router.replace("/machine")
  } else {
    showFailToast("更新失败" + (res.data.description ? `,${res.data.description}` : ''))
  }
}

</script>

<style scoped>

</style>
