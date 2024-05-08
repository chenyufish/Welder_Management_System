<template>
  <van-sticky>
    <van-nav-bar
        title="标签"
        style="text-align: left"
        @click-right="onClickRight"
    >
      <template #right>
        跳过
      </template>
    </van-nav-bar>
  </van-sticky>
  <form action="/">
    <van-search
        v-model="searchText"
        placeholder="请输入要搜索的标签"
        @search="onSearch"
    />
    <div v-if="activeIds.length > 0">
      <van-divider content-position="left">已选择标签</van-divider>
      <van-row style="padding: 16px">
        <van-col v-for="tag in activeIds">
          <van-tag closeable size="small" type="primary" @close="close(tag)" style="margin: 5px">
            {{ tag }}
          </van-tag>
        </van-col>
      </van-row>
    </div>
    <van-divider content-position="left">选择标签</van-divider>
    <van-tree-select
        v-model:active-id="activeIds"
        v-model:main-active-index="activeIndex"
        :items="tagList"
    />
    <van-divider content-position="left">自定义标签</van-divider>
    <van-cell-group inset>
      <van-field v-model="userDefinedTag" placeholder="请输入标签" style="padding-right: 5px">
        <template #button>
          <van-button size="small" type="primary" @click="addUserDefinedTag">添加</van-button>
        </template>
      </van-field>
    </van-cell-group>
  </form>
  <div style="margin: 20px">
    <van-button block type="primary" @click="updateTag">完成</van-button>
  </div>
</template>

<script setup>
import {onMounted, ref} from "vue";
import {useRouter} from "vue-router";
import {showFailToast} from "vant";
import myAxios from "../plugins/myAxios.js";

let router = useRouter();
const onClickRight = () => {
  router.replace("/")
}
const userDefinedTag = ref("")
const addUserDefinedTag = () => {
  if (userDefinedTag.value !== "") {
    activeIds.value.push(userDefinedTag.value)
    userDefinedTag.value = ""
  } else {
    showFailToast("请先输入标签名")
  }
}
const originTagList = [
  {
    text: '输入电压',
    children: [
      {text: '直流电压（20V~40V）', id: '直流电压'},
      {text: '交流电压（50V~90V）', id: '交流电压'},
      {text: '变频电压', id: '变频电压'}
    ],
  },
  {
    text: '工作电流',
    children: [
      {text: '25~40', id: '40'},
      {text: '40~60', id: '60'},
      {text: '60~80', id: '80'},
      {text: '100~130', id: '130'},
      {text: '160~210', id: '210'},
      {text: '210~270', id: '270'},
    ],
  },
  {
    text: "焊接方式",
    children: [
      {text: "拉弧式", id: "拉弧式"},
      {text: "氩弧式", id: "氩弧式"},
      {text: "气体保护电弧", id: "气体保护电弧"},
      {text: "熔化极电弧", id: "熔化极电弧"},
    ]
  },
  {
    text: '频率',
    children: [
      {text: '15HZ', id: '15HZ'},
      {text: '20HZ', id: '20HZ'},
      {text: '28HZ', id: '28HZ'},
      {text: '30HZ', id: '30HZ'},
      {text: '40HZ', id: '40HZ'},
    ]
  },
  {
    text: '焊条直径',
    children: [
      {text: '1.6', id: '1.6'},
      {text: '2.0', id: '2.0'},
      {text: '2.5', id: '2.5'},
      {text: '3.2', id: '3.2'},
      {text: '4.0', id: '4.0'},
      {text: '5.0', id: '5.0'},
      {text: '5.8', id: '5.8'},
    ]
  },
  {
    text: '品牌',
    children: [
      {text: '莱驰', id: '莱驰'},
      {text: '斯巴特', id: '斯巴特'},
      {text: '威尔泰克', id: '威尔泰克'},
      {text: '麦格泰克', id: '麦格泰克'},
      {text: '瑞凌', id: '瑞凌'},
      {text: '广为', id: '广为'},
      {text: '佳士', id: '佳士'},
      {text: '林肯', id: '林肯'},
      {text: '时代', id: '时代'},
      {text: '锐龙', id: '锐龙'},
      {text: '松下', id: '松下'},
      {text: '伊萨', id: '伊萨'},
      {text: '奥太', id: '奥太'},
      {text: '米勒', id: '米勒'},
      {text: '通用', id: '通用'},
    ]
  }
];
let tagList = ref(originTagList);
const searchText = ref('');
onMounted(async () => {
  let res = await myAxios.get("/user/tags");
  if (res?.data.code === 0) {
    activeIds.value = res.data.data
  }
})
const onSearch = () => {
  tagList.value = originTagList.map(parentTag => {
    const tempChildren = [...parentTag.children];
    const tempParentTag = {...parentTag};
    tempParentTag.children = tempChildren.filter(item => item.text.includes(searchText.value))
    return tempParentTag;
  })
};
const activeIds = ref([]);
const activeIndex = ref(0);

const close = (tag) => {
  activeIds.value = activeIds.value.filter((item) => {
    return item !== tag;
  })
};
const updateTag = async () => {
  let res = await myAxios.put("/user/update/tags", activeIds.value);
  if (res?.data.code === 0) {
    await router.replace("/user")
  }
}
</script>

<style scoped>

</style>
