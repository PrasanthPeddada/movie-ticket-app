import { Component, OnInit } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { MovieService } from '../../../services/movie.service';
import { BookingService } from '../../../services/booking.service';
import { AuthService } from '../../../services/auth.service';
import { User } from '../../../models/user.model';
import { Movie } from '../../../models/movie.model';
import { Booking } from '../../../models/booking.model';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {
  totalUsers = 0;
  totalMovies = 0;
  totalBookings = 0;
  recentBookings: Booking[] = [];
  loading = true;
  error = '';
  userErrors: string[] = [];
  movieErrors: string[] = [];
  bookingErrors: string[] = [];

  constructor(
    private userService: UserService,
    private movieService: MovieService,
    private bookingService: BookingService,
    private authService: AuthService
  ) { }

  ngOnInit() {
    // Check if user is admin
    if (!this.authService.isAdmin()) {
      this.error = 'Access denied. Admin privileges required.';
      return;
    }
    
    this.loadDashboardData();
  }

  loadDashboardData() {
    this.loading = true;
    this.error = '';
    this.userErrors = [];
    this.movieErrors = [];
    this.bookingErrors = [];
    
    console.log('Loading admin dashboard data...');
    
    // Load users count
    this.userService.getAllUsers().subscribe({
      next: (users) => {
        console.log('Users loaded:', users.length);
        this.totalUsers = users.length;
      },
      error: (error) => {
        console.error('Error loading users:', error);
        this.userErrors.push('Failed to load users: ' + (error.error?.message || error.message || 'Unknown error'));
      }
    });

    // Load movies count
    this.movieService.getAllMovies().subscribe({
      next: (movies) => {
        console.log('Movies loaded:', movies.length);
        this.totalMovies = movies.length;
      },
      error: (error) => {
        console.error('Error loading movies:', error);
        this.movieErrors.push('Failed to load movies: ' + (error.error?.message || error.message || 'Unknown error'));
      }
    });

    // Load recent bookings
    this.bookingService.getAllBookings().subscribe({
      next: (bookings) => {
        console.log('Bookings loaded:', bookings.length);
        this.totalBookings = bookings.length;
        this.recentBookings = bookings.slice(0, 5); // Get last 5 bookings
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading bookings:', error);
        this.bookingErrors.push('Failed to load bookings: ' + (error.error?.message || error.message || 'Unknown error'));
        this.loading = false;
      }
    });

    // Set loading to false after a timeout in case some requests don't complete
    setTimeout(() => {
      if (this.loading) {
        this.loading = false;
        if (this.userErrors.length === 0 && this.movieErrors.length === 0 && this.bookingErrors.length === 0) {
          this.error = 'Some data failed to load. Please check your connection.';
        }
      }
    }, 10000);
  }

  getErrorMessage(): string {
    const allErrors = [...this.userErrors, ...this.movieErrors, ...this.bookingErrors];
    if (allErrors.length > 0) {
      return allErrors.join('; ');
    }
    return this.error;
  }
} 