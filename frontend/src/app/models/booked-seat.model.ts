import { Booking } from './booking.model';

export interface BookedSeat {
  id?: number;
  booking: Booking;
  seatNumber: string;
  seatCategory: SeatCategory;
  status: SeatStatus;
  price: number;
  rowNumber: number;
  columnNumber: number;
  createdAt?: Date;
  updatedAt?: Date;
}

export enum SeatCategory {
  PREMIUM = 'PREMIUM',
  EXECUTIVE = 'EXECUTIVE',
  ECONOMY = 'ECONOMY'
}

export enum SeatStatus {
  AVAILABLE = 'AVAILABLE',
  BOOKED = 'BOOKED',
  BLOCKED = 'BLOCKED'
} 