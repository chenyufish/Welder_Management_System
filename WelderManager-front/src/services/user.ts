// @ts-ignore
import myAxios from "../plugins/myAxios.js";

export const getCurrentUser = async () => {
    let response = await myAxios.get("/user/current");
    if (response.data.code===0 && response.data.data)
        // setLoginUser(response.data.data)
        return response.data.data
}