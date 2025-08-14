import { User } from './user.model';
import { Show } from './show.model';

export interface Booking {
  id?: number;
  user: User;
  show: Show;
  totalAmount: number;
  status: BookingStatus;
  paymentMethod: PaymentMethod;
  bookingDate: Date;
  createdAt?: Date;
  updatedAt?: Date;
}

export enum BookingStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  CANCELLED = 'CANCELLED'
}

export enum PaymentMethod {
  CREDIT_CARD = 'CREDIT_CARD',
  DEBIT_CARD = 'DEBIT_CARD',
  UPI = 'UPI',
  NET_BANKING = 'NET_BANKING'
} 