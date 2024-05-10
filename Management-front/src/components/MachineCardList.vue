<template>
  <van-card v-for="machine in machineList"
            :title="machine.machineName"
            :desc="machine.description"
  >
    <template #thumb>
      <van-image :src="machine?.coverImage" width="88" height="88" @click="showUserDetail(machine?.id)"/>
    </template>
    <template #tags>
      <van-tag v-for="tag in machine?.tags" plain type="danger" style="margin-right: 8px;margin-top: 8px"
               @click="showUserDetail(machine?.id)">
        {{ tag }}
      </van-tag>
    </template>
    <template #num>
      <van-button size="mini" plain type="primary"
                  style="width: 48px;height: 28px;margin-top: 10px" @click="toChat(machine)">
        借用
      </van-button>
    </template>
  </van-card>
</template>

<script setup lang="ts">
import myAxios from "../plugins/myAxios.js";
import {useRouter} from "vue-router";
import {MachineType} from "../model/machine";

interface MachienCardListProps {
  machineList: MachineType[]
}

const props = defineProps<MachienCardListProps>()

const followUser = async (machine) => {
  let res = await myAxios.post("/follow/" + machine.id);
  if (res?.data.code === 0) {
    let res_ = await myAxios.get("/user/" + machine.id);
    if (res_.data.code === 0) {
      machine.isFollow = res_.data.data.isFollow
    }
  }
}
let router = useRouter();
const showUserDetail = (id) => {
  router.push({
    path: "/machine/detail",
    query: {
      id: id
    }
  })
}
const toChat = (machine) => {
  router.push({
    path: "/chat",
    query: {
      id: machine.id,
      username: machine.username,
      userType: 1
    }
  })
}
</script>

<style scoped>

</style>
