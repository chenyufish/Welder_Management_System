import Index from "../pages/Index.vue";
import User from "../pages/UserPage.vue";
import Search from "../pages/SearchPage.vue";
import EditUser from "../pages/UserEditPage.vue";
import SearchResult from "../pages/SearchResultPage.vue";
import UserLogin from "../pages/UserLoginPage.vue";
import UserUpdatePage from "../pages/UserUpdatePage.vue";
import UserTeamJoinPage from "../pages/UserTeamJoinPage.vue";
import UserTeamCreatePage from "../pages/UserTeamCreatePage.vue";
import SignUpPage from "../pages/SignUpPage.vue";
import BlogEditPage from "../pages/BlogEditPage.vue";
import MessagePage from "../pages/MassagePage.vue";
import UserBlogPage from "../pages/UserBlogPage.vue";
import BlogPage from "../pages/BlogPage.vue";
import UserTagPage from "../pages/UserTagPage.vue";
import ForgetPasswordPage from "../pages/ForgetPasswordPage.vue";
import AfterSignUp from "../pages/AfterSignUp.vue";
import ChatPage from "../pages/ChatPage.vue";
import UserDetailPage from "../pages/UserDetailPage.vue";
import UserFollowPage from "../pages/UserFollowPage.vue";
import UserBlogCommentPage from "../pages/UserBlogCommentPage.vue";
import UserFansPage from "../pages/UserFansPage.vue";
import UserLikePage from "../pages/UserLikePage.vue";
import UpdatePasswordPage from "../pages/UpdatePasswordPage.vue";
import UserFollowUserPage from "../pages/UserFollowUserPage.vue";
import MachineAddPage from "../pages/MachineAddPage.vue";
import AfterMachineAdd from "../pages/AfterMachineAdd.vue";
import FaultsEditPage from "../pages/FaultsEditPage.vue";
import MachinePage from "../pages/MachinePage.vue";
import MachineDetailPage from "../pages/MachineDetailPage.vue";
import MachineUpdatePage from "../pages/MachineUpdatePage.vue";
import UserMachineCreatePage from "../pages/UserMachineCreatePage.vue";

const routes = [
    {path: '/', title: "焊接管理系统", component: Index},
    {path: '/search', title: "搜索设备", component: Search},
    {path: '/search/userList', title: "搜索到的设备", component: SearchResult},
    // {path: '/team', title: "使用记录", component: Team},
    {path: '/machine', title: "使用记录", component: MachinePage},
    {path: '/machine/detail', title: '设备详情', component: MachineDetailPage},
    // {path: '/team/detail', title: '设备详情', component: TeamDetailPage},
    {path: '/machine/add', title: "添加设备", component: MachineAddPage},
    // {path: '/team/add', title: "添加设备", component: MachineAddPage},
    {path: '/machine/update', title: "更新设备", component: MachineUpdatePage},

    // {path: '/team/update', title: "更新设备", component: TeamUpdatePage},
    {path: '/user', title: "个人", component: User},
    {path: '/user/tag', title: "标签", component: UserTagPage},
    {path: '/user/detail', title: '用户详情', component: UserDetailPage},
    {path: '/user/signup', component: SignUpPage},

    {path: '/after/machine', title: '标签', component: AfterMachineAdd, meta: {layout: 'after-machine'}},
    {path: '/after/user', title: '标签', component: AfterSignUp, meta: {layout: 'after-user'}},

    {path: '/user/login', title: "用户登录", component: UserLogin},
    {path: '/user/edit', title: "修改用户", component: EditUser},
    {path: '/user/update', title: "修改用户", component: UserUpdatePage},
    {path: '/user/team/join', title: "我使用的设备", component: UserTeamJoinPage},
    // {path: '/user/team/create', title: "我添加到设备", component: UserTeamCreatePage},
    {path: '/user/machine/create', title: "我添加的设备", component: UserMachineCreatePage},

    {path: '/blog', component: BlogPage, meta: {layout: 'blog'}},
    {path: '/blog/edit', title: '故障报告', component: FaultsEditPage, meta: {layout: 'blog-edit'}},
    {path: '/user/blog', title: '我的故障报告', component: UserBlogPage},
    {path: '/message', title: '消息中心', component: MessagePage},
    {path: '/forget', title: '找回密码', component: ForgetPasswordPage, meta: {layout: 'forget'}},
    {path: '/chat', component: ChatPage, meta: {layout: 'chat'}},
    {path: '/user/follow', title: '我关注的用户', component: UserFollowPage},
    {path: '/user/comment', title: '我评论的', component: UserBlogCommentPage},
    {path: '/fans', title: '我的粉丝', component: UserFansPage},
    {path: '/user/like', title: '赞', component: UserLikePage},
    {path: '/user/follow/blog', title: '关注', component: UserFollowUserPage},
    {path: '/update/password', title: '修改密码', component: UpdatePasswordPage, meta: {layout: 'password'}}
]
export default routes;
