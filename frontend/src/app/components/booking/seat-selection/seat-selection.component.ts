import { Component, Input, Output, EventEmitter } from '@angular/core';
import { BookedSeat, SeatStatus } from '../../../models/booked-seat.model';

@Component({
  selector: 'app-seat-selection',
  templateUrl: './seat-selection.component.html',
  styleUrls: ['./seat-selection.component.scss']
})
export class SeatSelectionComponent {
  @Input() availableSeats: BookedSeat[] = [];
  @Input() selectedSeats: { row: number, col: number }[] = [];
  @Input() totalRows: number = 0;
  @Input() totalColumns: number = 0;
  @Input() allSeats: BookedSeat[] = [];
  @Input() goldSeatPrice: number = 0;
  @Input() silverSeatPrice: number = 0;
  @Input() vipSeatPrice: number = 0;
  @Output() seatSelected = new EventEmitter<{ row: number, col: number }>();

  onSeatClick(row: number, col: number) {
    this.seatSelected.emit({ row, col });
  }

  isSeatSelected(row: number, col: number): boolean {
    return this.selectedSeats.some(seat => seat.row === row && seat.col === col);
  }

  isSeatBooked(seat: BookedSeat): boolean {
    return seat.status === 'BOOKED';
  }

  getSeatCategory(row: number): string {
    const totalRows = this.totalRows;
    const vipRows = Math.floor(totalRows * 0.3); // First 30% of rows are VIP
    const goldRows = Math.floor(totalRows * 0.5); // Next 50% of rows are Gold
    
    if (row <= vipRows) {
      return 'VIP';
    } else if (row <= vipRows + goldRows) {
      return 'GOLD';
    } else {
      return 'SILVER';
    }
  }

  getSeat(row: number, col: number): BookedSeat {
    const seat = this.allSeats.find(seat => seat.rowNumber === row && seat.columnNumber === col);
    if (seat) return seat;
    // Return a virtual available seat if not found
    const category = this.getSeatCategory(row);
    let price = 0;
    switch (category) {
      case 'VIP': price = this.vipSeatPrice; break;
      case 'GOLD': price = this.goldSeatPrice; break;
      case 'SILVER': price = this.silverSeatPrice; break;
    }
    return {
      id: undefined,
      booking: undefined as any, // Not needed for available seat
      seatNumber: `${String.fromCharCode(64 + row)}${col}`,
      seatCategory: category as any,
      status: SeatStatus.AVAILABLE,
      price: price,
      rowNumber: row,
      columnNumber: col
    };
  }

  getSeatLabel(seat: BookedSeat): string {
    // Example: Convert rowNumber to letter (A, B, C, ...) and append columnNumber
    const rowLetter = String.fromCharCode(64 + seat.rowNumber); // 1 -> A, 2 -> B, etc.
    return `${rowLetter}${seat.columnNumber}`;
  }
} 