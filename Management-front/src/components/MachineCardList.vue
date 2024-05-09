<template>
  <van-card
      v-for="machine in props.machineList"
      :desc="machine.description"
      :thumb="machine?.coverImage ||　defaultImg"
      @click-thumb="getMachineDetail(machine.id)"
  >
    <template #title>
      <div class="machine-name" @click="getMachineDetail(machine.id)">{{ machine.machineName }}</div>
    </template>
    <template #tags>
      <van-tag plain type="danger" style="margin-right: 8px;margin-top: 8px" @click="getMachineDetail(machine.id)">
        {{ teamStatusEnum[machine.machineStatus] }}
      </van-tag>
    </template>
    <template #bottom>
      <div class="row avatar-group">
        <div class="avatar" v-for="avatar in machine.usageUserAvatars">
          <img :src="avatar" alt="">
        </div>
      </div>
    </template>
    <template #footer>
      <van-button v-if="!machine.machineStatus" size="small" plain type="primary" @click="doBorrowMachine(machine)">
        借用
      </van-button>
      <van-button v-if="machine.machineStatus && machine.userId!==currentUser?.id" size="small" plain
                  @click="doReturnMachine(machine.id)">
        归还
      </van-button>
      <van-button v-if="machine.userId===currentUser?.id || currentUser?.role===1" size="small" plain
                  @click="doUpdateMachine(machine.id)">
        更新
      </van-button>
      <van-button v-if="machine.userId===currentUser?.id || currentUser?.role===1" size="small" plain type="danger"
                  @click="doDeleteMachine(machine.id)">
        删除
      </van-button>
    </template>
  </van-card>
</template>

<script setup lang="ts">
import { teamStatusEnum } from "../constants/machine.ts";
import defaultImg from "/public/defalutTeamImg.jpg";
import myAxios from "../plugins/myAxios.js";
import { showConfirmDialog, showFailToast, showSuccessToast } from "vant";
import { getCurrentUser } from "../services/user.ts";
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import {MachineType} from "../model/machine";
import {MachineUsageType} from "../model/machineUsage";


const showPasswordDialog = ref(false);
const teamPassword = ref("");
let currentUser = ref();
const borrowMachineId = ref();
let emits = defineEmits(["refresh"]);

interface MachineCardListProps {
  machineList: MachineType[];
}

const props = withDefaults(defineProps<MachineCardListProps>(), {});

onMounted(async () => {
  currentUser.value = await getCurrentUser();
});
const router = useRouter();

const borrowMachine = async (machineId, employeeId) => {
  const res = await myAxios.post("/usage/borrow", {
    machineId,
    employeeId,
  });
  if (res?.data.code === 0) {
    showSuccessToast("借用设备成功");
    onRefresh();
  } else {
    showFailToast("借用设备失败" + (res.data.description ? `,${res.data.description}` : ""));
  }
  doClear();
};
const doBorrowMachine = async (machine: MachineUsageType) => {
  borrowMachineId.value = machine.id;
  if (machine.machineStatus === 2) {
    showPasswordDialog.value = true;
  } else {
    await borrowMachine(machine.id,currentUser.value.id);
  }
};
const doUpdateMachine = (id: number) => {
  router.push({
    path: "/machine/update",
    query: {
      id,
    },
  });
};

const doReturnMachine = async (id: number) => {
  const res = await myAxios.post("/usage/return", {
    teamId: id,
  });
  if (res?.data.code === 0) {
    showSuccessToast("归还设备成功");
    onRefresh();
  } else {
    showFailToast("归还设备失败" + (res.data.description ? `,${res.data.description}` : ""));
  }
};

const doDeleteMachine = async (id: number) => {
  showConfirmDialog({
    title: "确定要删除设备吗",
    message:
        "此操作无法撤回",
  })
      .then(async () => {
        const res = await myAxios.post("/machine/delete", {
          id,
        });
        if (res?.data.code === 0) {
          showSuccessToast("删除设备成功");
          onRefresh();
        } else {
          showFailToast("删除设备失败" + (res.data.description ? `,${res.data.description}` : ""));
        }
      })
      .catch(() => {
      });
};

const onRefresh = () => {
  emits("refresh");
};
const doClear = () => {
  joinTeamId.value = "";
  teamPassword.value = "";
};
const getMachineDetail = (id) => {
  router.push({
    path: "/machine/detail",
    query: {
      id,
    },
  });
};
</script>

<style scoped>
:deep(.van-image__img) {
  height: 128px;
  object-fit: unset;
}

.team-title {
  color: rgb(50, 50, 51);
  display: -webkit-box;
  font-family: -apple-system, BlinkMacSystemFont, "Helvetica Neue", Helvetica, "Segoe UI", Arial, Roboto, "PingFang SC", miui, "Hiragino Sans GB", "Microsoft Yahei", sans-serif;
  font-size: 12px;
  font-weight: 600;
  height: 16px;
  line-height: 16px;
  max-height: 32px;
  overflow-x: hidden;
  overflow-y: hidden;
  text-overflow: ellipsis;
  width: 247px;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
}

:deep(.van-icon__image) {
  border-radius: 50%;
}

.row {
  display: flex;
  align-items: center;
}

.avatar {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  position: relative;
  font-size: 20px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.avatar img {
  width: 100%;
  height: 100%;
  border-radius: 20px;
  object-fit: cover;
}

.avatar-group .avatar {
  border: 2px solid #fff;
  margin-left: calc(-0.3em);
  box-shadow: unset;
}

.row {
  display: flex;
  align-items: center;
  margin-left: 12px;
  margin-top: 30px;
  margin-bottom: 10px;
}

.avatar {
  width: 25px;
  height: 25px;
  border-radius: 50%;
  position: relative;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.avatar span {
  color: #fff;
  font-size: 0.5em;
}

.avatar img {
  width: 100%;
  height: 100%;
  border-radius: 25px;
  object-fit: cover;
}

.avatar-group .avatar {
  border: 2px solid #fff;
  margin-left: calc(-0.8em);
  box-shadow: unset;
}

</style>
