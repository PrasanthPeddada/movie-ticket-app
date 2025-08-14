export interface User {
  id?: number;
  name: string;
  email: string;
  phoneNumber: string;
  role: UserRole;
  active: boolean;
  verified: boolean;
  createdAt?: Date;
  updatedAt?: Date;
  lastLogin?: Date;
  feedback?:string;
}

export enum UserRole {
  USER = 'USER',
  ADMIN = 'ADMIN'
} 