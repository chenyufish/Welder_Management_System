import { UserType } from "./user";

export type MachineUsageType = {
    id: number;
    machineId: number;
    employeeId: number;
    machineStatus: number;
    createUser?: UserType;
}
