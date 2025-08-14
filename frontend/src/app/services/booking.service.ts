import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Booking, BookingStatus, PaymentMethod } from '../models/booking.model';
import { BookedSeat } from '../models/booked-seat.model';
import { Show } from '../models/show.model';

@Injectable({
  providedIn: 'root'
})
export class BookingService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  getShowsByMovie(movieId: number): Observable<Show[]> {
    return this.http.get<Show[]>(`${this.apiUrl}/shows/movie/${movieId}`);
  }

  getShowsByTheater(theaterId: number): Observable<Show[]> {
    return this.http.get<Show[]>(`${this.apiUrl}/shows/theater/${theaterId}`);
  }

  getShowById(showId: number): Observable<Show> {
    return this.http.get<Show>(`${this.apiUrl}/shows/${showId}`);
  }

  getSeatLayout(showId: number): Observable<{ 
    totalRows: number, 
    totalColumns: number, 
    seats: BookedSeat[],
    goldSeatPrice: number,
    silverSeatPrice: number,
    vipSeatPrice: number
  }> {
    return this.http.get<{ 
      totalRows: number, 
      totalColumns: number, 
      seats: BookedSeat[],
      goldSeatPrice: number,
      silverSeatPrice: number,
      vipSeatPrice: number
    }>(`${this.apiUrl}/shows/${showId}/seats`);
  }

  createBooking(booking: any): Observable<Booking> {
    return this.http.post<Booking>(`${this.apiUrl}/bookings`, booking);
  }

  getUserBookings(): Observable<Booking[]> {
    return this.http.get<Booking[]>(`${this.apiUrl}/bookings/user`);
  }

  getAllBookings(): Observable<Booking[]> {
    return this.http.get<Booking[]>(`${this.apiUrl}/bookings/all`);
  }

  getBookingById(id: number): Observable<Booking> {
    return this.http.get<Booking>(`${this.apiUrl}/bookings/${id}`);
  }

  cancelBooking(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/bookings/${id}`);
  }

  updateBookingStatus(id: number, status: BookingStatus): Observable<Booking> {
    return this.http.put<Booking>(`${this.apiUrl}/bookings/${id}/status`, { status });
  }

  processPayment(bookingId: number, paymentMethod: PaymentMethod): Observable<any> {
    return this.http.post(`${this.apiUrl}/bookings/${bookingId}/payment`, { paymentMethod });
  }

  
  createPaymentOrder(bookingId: number): Observable<any> {
  return this.http.post(`${this.apiUrl}/payments/create-order/${bookingId}`, {});
}

  
  

  verifyPayment(paymentData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/payments/verify`, paymentData);
  }
} 