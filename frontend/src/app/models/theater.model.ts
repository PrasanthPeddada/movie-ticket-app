export interface Theater {
  id?: number;
  name: string;
  address: string;
  city: string;
  state: string;
  pincode: string;
  phoneNumber?: string;
  isActive: boolean;
  createdAt?: Date;
  updatedAt?: Date;
} 