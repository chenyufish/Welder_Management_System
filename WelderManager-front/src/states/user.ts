import {userType} from "../model/user";

let currentUser:userType;
const setCurrentUserState=(user:userType)=>{
    currentUser=user;
}
const getCurrentUserState=():userType=>{
    return currentUser;
}
export {
    setCurrentUserState,
    getCurrentUserState,
}