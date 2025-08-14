import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { BookingService } from '../../services/booking.service';
import { AuthService } from '../../services/auth.service';
import { PaymentMethod } from '../../models/booking.model';

declare var Razorpay: any;

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.scss']
})
export class PaymentComponent implements OnInit {
  paymentForm: FormGroup;
  bookingData: any = null;
  loading = false;
  error = '';
  success = '';
  razorpayLoaded = false;

  paymentMethods = [
    { value: PaymentMethod.CREDIT_CARD, label: 'Credit Card' },
    { value: PaymentMethod.DEBIT_CARD, label: 'Debit Card' },
    { value: PaymentMethod.UPI, label: 'UPI' },
    { value: PaymentMethod.NET_BANKING, label: 'Net Banking' }
  ];

  constructor(
    private formBuilder: FormBuilder,
    private bookingService: BookingService,
    private authService: AuthService,
    private router: Router
  ) {
    this.paymentForm = this.formBuilder.group({
      paymentMethod: ['', Validators.required]
    });
  }

  ngOnInit() {
    // Check if user is logged in
    if (!this.authService.isAuthenticated()) {
      console.log('User not authenticated, redirecting to login');
      this.router.navigate(['/login']);
      return;
    }

    const storedData = sessionStorage.getItem('bookingData');
    if (storedData) {
      this.bookingData = JSON.parse(storedData);
      console.log('Booking data:', this.bookingData);
    } else {
      this.router.navigate(['/movies']);
    }

    // Load Razorpay script
    this.loadRazorpayScript();
  }

  loadRazorpayScript() {
    if (typeof Razorpay !== 'undefined') {
      this.razorpayLoaded = true;
      return;
    }

    const script = document.createElement('script');
    script.src = 'https://checkout.razorpay.com/v1/checkout.js';
    script.onload = () => {
      this.razorpayLoaded = true;
      console.log('Razorpay script loaded');
    };
    script.onerror = () => {
      console.error('Failed to load Razorpay script');
      this.error = 'Failed to load payment gateway. Please refresh the page.';
    };
    document.head.appendChild(script);
  }

  onSubmit() {
    console.log('Payment form submitted');
    console.log('User authenticated:', this.authService.isAuthenticated());
    console.log('Token:', this.authService.getToken());
    console.log('Form valid:', this.paymentForm.valid);
    console.log('Form values:', this.paymentForm.value);
    console.log('Booking data:', this.bookingData);
    
    // Check authentication
    if (!this.authService.isAuthenticated()) {
      this.error = 'Please login to complete your booking.';
      return;
    }
    
    if (this.paymentForm.invalid) {
      console.log('Form is invalid');
      return;
    }
    
    if (!this.bookingData) {
      console.log('No booking data');
      return;
    }

    if (!this.razorpayLoaded) {
      this.error = 'Payment gateway is still loading. Please wait a moment and try again.';
      return;
    }

    this.loading = true;
    this.error = '';

    // First create the booking
    const bookingRequest = {
      showId: this.bookingData.showId,
      selectedSeats: this.bookingData.selectedSeats,
      totalAmount: this.bookingData.totalAmount
    };

    console.log('Creating booking with request:', bookingRequest);

    this.bookingService.createBooking(bookingRequest).subscribe({
      next: (bookingResponse: any) => {
        console.log('Booking created successfully:', bookingResponse);
        
        // Create Razorpay payment order
        this.createRazorpayOrder(bookingResponse.bookingId);
      },
      error: (bookingError) => {
        console.error('Booking error:', bookingError);
        this.loading = false;
        this.error = bookingError.error?.message || bookingError.error?.error || 'Booking creation failed. Please try again.';
      }
    });
  }

  createRazorpayOrder(bookingId: number) {
    // Call backend to create payment order
    this.bookingService.createPaymentOrder(bookingId).subscribe({
      next: (orderResponse: any) => {
        console.log('Payment order created:', orderResponse);
        this.initiateRazorpayPayment(orderResponse, bookingId);
      },
      error: (orderError) => {
        console.error('Payment order error:', orderError);
        this.loading = false;
        this.error = 'Failed to create payment order. Please try again.';
      }
    });
  }

  initiateRazorpayPayment(orderData: any, bookingId: number) {
    const options = {
      key: orderData.keyId,
      amount: orderData.amount,
      currency: orderData.currency,
      name: 'Movie Ticket Booking',
      description: 'Movie Ticket Payment',
      order_id: orderData.orderId,
      handler: (response: any) => {
        console.log('Payment successful:', response);
        this.verifyPayment(response, bookingId);
      },
      prefill: {
        name: this.authService.currentUserValue?.name || '',
        email: this.authService.currentUserValue?.email || '',
        contact: this.authService.currentUserValue?.phoneNumber || ''
      },
      notes: {
        booking_id: bookingId.toString(),
        movie_name: this.bookingData.movieTitle || 'Movie Ticket'
      },
      theme: {
        color: '#3399cc'
      },
      modal: {
        ondismiss: () => {
          console.log('Payment modal dismissed');
          this.loading = false;
        }
      }
    };

    try {
      const rzp = new Razorpay(options);
      rzp.open();
    } catch (error) {
      console.error('Error opening Razorpay:', error);
      this.loading = false;
      this.error = 'Failed to open payment gateway. Please try again.';
    }
  }

  verifyPayment(paymentResponse: any, bookingId: number) {
    const verificationData = {
      razorpay_payment_id: paymentResponse.razorpay_payment_id,
      razorpay_order_id: paymentResponse.razorpay_order_id,
      razorpay_signature: paymentResponse.razorpay_signature,
      booking_id: bookingId.toString()
    };

    this.bookingService.verifyPayment(verificationData).subscribe({
      next: (verificationResponse: any) => {
        console.log('Payment verified successfully:', verificationResponse);
        this.success = 'Payment successful! Your booking has been confirmed.';
        this.error = '';
        this.loading = false;
        sessionStorage.removeItem('bookingData');
        setTimeout(() => {
          this.router.navigate(['/profile']);
        }, 2000);
      },
      error: (verificationError) => {
        console.error('Payment verification error:', verificationError);
        this.loading = false;
        this.error = 'Payment verification failed. Please contact support.';
      }
    });
  }

  get f() {
    return this.paymentForm.controls;
  }

  getSeatLabel(seatData: { row: number, col: number }): string {
    const rowLetter = String.fromCharCode(64 + seatData.row); // 1 -> A, 2 -> B, etc.
    return `${rowLetter}${seatData.col}`;
  }
} 

