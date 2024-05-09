import { UserType } from "./user";

export type MachineType = {
    id: number;
    machineName: string;
    description: string,
    coverImage: string,
    serialNumber: number;
    machineStatus: number;
    userId: number;
    leaderName: string,
    hasUsageNum: number;
    createTime: Date;
    updateTime: Date;
    createUser?: UserType;
    tags:string[];
    usageHours: number;
    usageUserAvatars?: string[];
}
