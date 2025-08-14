import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BookingService } from '../../services/booking.service';
import { Show } from '../../models/show.model';
import { BookedSeat, SeatStatus } from '../../models/booked-seat.model';

@Component({
  selector: 'app-booking',
  templateUrl: './booking.component.html',
  styleUrls: ['./booking.component.scss']
})
export class BookingComponent implements OnInit {
  show: Show | null = null;
  availableSeats: BookedSeat[] = [];
  selectedSeats: { row: number, col: number }[] = [];
  loading = true;
  error = '';
  totalRows: number = 0;
  totalColumns: number = 0;
  allSeats: BookedSeat[] = [];
  goldSeatPrice: number = 0;
  silverSeatPrice: number = 0;
  vipSeatPrice: number = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private bookingService: BookingService
  ) { }

  ngOnInit() {
    const showId = this.route.snapshot.paramMap.get('showId');
    if (showId) {
      this.loadShowDetails(+showId);
      this.loadSeatLayout(+showId);
    }
  }

  loadShowDetails(showId: number) {
    this.bookingService.getShowById(showId).subscribe({
      next: (show: Show) => {
        this.show = show;
        console.log('Show loaded:', show);
      },
      error: (error: any) => {
        console.error('Error loading show:', error);
        this.error = 'Failed to load show details.';
      }
    });
  }

  loadSeatLayout(showId: number) {
    this.bookingService.getSeatLayout(showId).subscribe({
      next: (layout) => {
        this.totalRows = layout.totalRows;
        this.totalColumns = layout.totalColumns;
        this.allSeats = layout.seats;
        this.goldSeatPrice = layout.goldSeatPrice || 300; // Default fallback
        this.silverSeatPrice = layout.silverSeatPrice || 200; // Default fallback
        this.vipSeatPrice = layout.vipSeatPrice || 500; // Default fallback
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load seat layout.';
        this.loading = false;
        console.error('Error loading seat layout:', error);
      }
    });
  }

  onSeatSelected(seatData: { row: number, col: number }) {
    console.log('Seat selected:', seatData);
    const index = this.selectedSeats.findIndex(seat => seat.row === seatData.row && seat.col === seatData.col);
    if (index > -1) {
      this.selectedSeats.splice(index, 1);
      console.log('Seat deselected. Total selected seats:', this.selectedSeats.length);
    } else {
      this.selectedSeats.push(seatData);
      console.log('Seat selected. Total selected seats:', this.selectedSeats.length);
    }
    console.log('Selected seats array:', this.selectedSeats);
  }

  isSeatSelected(row: number, col: number): boolean {
    return this.selectedSeats.some(seat => seat.row === row && seat.col === col);
  }

  isSeatBooked(seat: BookedSeat): boolean {
    return seat.status === SeatStatus.BOOKED;
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

  getSeatPrice(row: number, col: number): number {
    const seat = this.allSeats.find(s => s.rowNumber === row && s.columnNumber === col);
    if (seat) return seat.price;
    
    // For virtual seats, determine price based on row category
    const category = this.getSeatCategory(row);
    switch (category) {
      case 'VIP': return this.vipSeatPrice;
      case 'GOLD': return this.goldSeatPrice;
      case 'SILVER': return this.silverSeatPrice;
      default: return this.silverSeatPrice;
    }
  }

  getTotalPrice(): number {
    return this.selectedSeats.reduce((total, seatData) => {
      return total + this.getSeatPrice(seatData.row, seatData.col);
    }, 0);
  }
  getSelectedSeatNumbers(): string {
    return this.selectedSeats.length > 0
      ? this.selectedSeatNumbers.join(', ')
      : 'None';
  }
  

  get selectedSeatNumbers(): string[] {
    return this.selectedSeats.map(seatData => {
      const rowLetter = String.fromCharCode(64 + seatData.row); // 1 -> A, 2 -> B, etc.
      return `${rowLetter}${seatData.col}`;
    });
  }

  getSeatLabel(seat: BookedSeat): string {
    // Example: Convert rowNumber to letter (A, B, C, ...) and append columnNumber
    const rowLetter = String.fromCharCode(64 + seat.rowNumber); // 1 -> A, 2 -> B, etc.
    return `${rowLetter}${seat.columnNumber}`;
  }

  getSeatDisplayInfo(seatData: { row: number, col: number }): { label: string, category: string, price: number } {
    const seat = { rowNumber: seatData.row, columnNumber: seatData.col } as BookedSeat;
    return {
      label: this.getSeatLabel(seat),
      category: this.getSeatCategory(seatData.row),
      price: this.getSeatPrice(seatData.row, seatData.col)
    };
  }

  proceedToPayment() {
    console.log('Proceed to payment clicked');
    console.log('Selected seats length:', this.selectedSeats.length);
    console.log('Selected seats:', this.selectedSeats);
    console.log('Show:', this.show);
    
    if (this.selectedSeats.length > 0 && this.show) {
      const bookingData = {
        showId: this.show.id,
        selectedSeats: this.selectedSeats, // This now contains {row, col} objects
        totalAmount: this.getTotalPrice()
      };
      
      console.log('Booking data to store:', bookingData);
      
      // Store booking data in session storage for payment component
      sessionStorage.setItem('bookingData', JSON.stringify(bookingData));
      this.router.navigate(['/payment']);
    } else {
      console.log('Cannot proceed: no seats selected or no show data');
    }
  }
} 