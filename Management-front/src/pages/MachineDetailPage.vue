<template>
  <van-image
      width="100%"
      height="120"
      :src="machine?.coverImage ||　defaultImg"
  />
  <van-cell-group inset>
    <van-cell v-if="currentUser?.role==1 || currentUser?.id==machine?.userId" title="修改封面" icon="brush-o">
      <template #value>
        <van-uploader :max-count="1" :after-read="upload" v-model="fileList" :preview-image="false">
          <van-button v-if="uploading===false" icon="plus" type="primary" size="small">上传图片</van-button>
        </van-uploader>
        <van-button v-if="uploading===true" icon="plus" type="primary" size="small" uploading
                    uploading-text="上传中...">
          上传图片
        </van-button>
      </template>
    </van-cell>
    <van-cell title="设备名" icon="flag-o" :value="machine.machineName"/>
    <van-cell title="设备描述" icon="label-o" :value="machine.description"/>
    <van-cell title="管理员" icon="manager-o" :value="machine.leaderName"/>
    <van-cell title="设备状态" icon="shield-o" :value="teamStatusEnum[machine.machineStatus]"/>
    <van-cell title="使用人数" icon="friends-o" :value="machine.hasUsageNum"/>
    <van-cell title="标签">
      <template #value>
        <van-tag v-if="tagsList!=null" v-for="tag in tagsList" plain type="danger"
                 style="margin-right: 8px">
          {{ tag }}
        </van-tag>
        <span v-else>该设备暂未拥有标签</span>
      </template>
    </van-cell>
  </van-cell-group>
  <van-divider
      :style="{ color: '#1989fa', borderColor: '#1989fa', padding: '0 16px' }"
  >
    使用人员
  </van-divider>
  <team-member-card-list :login-user="currentUser" :user-list="usageMemberList" :team="machine" @refresh="onRefresh"/>
  <van-empty
      v-if="(!usageMemberList || usageMemberList.length===0) && loading===false"
      image="https://fastly.jsdelivr.net/npm/@vant/assets/custom-empty-image.png"
      image-size="80"
      description="暂时还没有使用人员"
  />
</template>

<script setup lang="ts">
import defaultImg from "../../public/defalutTeamImg.jpg"
import {useRoute, useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import myAxios from "../plugins/myAxios.js";
import {showFailToast} from "vant";
import {teamStatusEnum} from "../constants/team.ts";
import {getCurrentUser} from "../services/user.ts";

const loading = ref(true)
const fileList = ref([])
const usageMemberList = ref()
const uploading = ref(false)
let route = useRoute();
const machine = ref([]);
const tagsList=ref();
const currentUser = ref()
onMounted(async () => {
  currentUser.value = await getCurrentUser();
  let res = await myAxios.get("/machine/" + route.query.id);
  if (res?.data.machineStatus === 0) {
    if (res.data.data.tags) {
      res.data.data.tags = JSON.parse(res.data.data.tags)
      tagsList.value=res.data.data.tags;
      }
    machine.value = res.data.data
    let res_ = await myAxios.get("/usage/member/" + route.query.id);
    if (res_.data.machineStatus === 0) {
      res_.data.data.forEach((user) => {
        if (user.tags) {
          user.tags = JSON.parse(user.tags)
        }
      })
      usageMemberList.value = res_.data.data
    } else {
      showFailToast("获取使用人员失败" + (res.data.description ? `,${res.data.description}` : ''))
    }
  } else {
    showFailToast("人员查询失败" + (res.data.description ? `,${res.data.description}` : ''))
  }

  loading.value = false
})
const upload = async (file) => {
  uploading.value = true
  let formData = new FormData();
  formData.append("file", file.file)
  formData.append("id", machine.value.id)
  await myAxios.put("/machine/cover", formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
  uploading.value = false
  location.reload()
}
const onRefresh = () => {
  location.reload()
}
</script>

<style scoped>

</style>
