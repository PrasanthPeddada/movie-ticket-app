import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { BookingService } from '../../services/booking.service';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/user.model';
import { Booking } from '../../models/booking.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  profileForm: FormGroup;
  passwordForm: FormGroup;
  currentUser: User | null = null;
  userBookings: Booking[] = [];
  loading = true;
  error = '';
  success = '';

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private bookingService: BookingService,
    private authService: AuthService
  ) {
    this.profileForm = this.formBuilder.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]]
    });

    this.passwordForm = this.formBuilder.group({
      currentPassword: ['', Validators.required],
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit() {
    this.loadUserProfile();
    this.loadUserBookings();
  }

  loadUserProfile() {
    this.currentUser = this.authService.currentUserValue;
    if (this.currentUser) {
      this.profileForm.patchValue({
        name: this.currentUser.name,
        email: this.currentUser.email,
        phoneNumber: this.currentUser.phoneNumber
      });
    }
  }

  loadUserBookings() {
    this.bookingService.getUserBookings().subscribe({
      next: (bookings) => {
        this.userBookings = bookings;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load bookings.';
        this.loading = false;
        console.error('Error loading bookings:', error);
      }
    });
  }

  passwordMatchValidator(form: FormGroup) {
    const newPassword = form.get('newPassword');
    const confirmPassword = form.get('confirmPassword');
    
    if (newPassword && confirmPassword && newPassword.value !== confirmPassword.value) {
      confirmPassword.setErrors({ passwordMismatch: true });
      return { passwordMismatch: true };
    }
    
    return null;
  }

  updateProfile() {
    console.log('Profile update button clicked');
    if (this.profileForm.invalid || !this.currentUser) {
      console.log('Profile form is invalid or current user is not set');
    }

    const updatedUser = {
      ...this.currentUser,
      ...this.profileForm.value
    };
    const id = this.currentUser?.id;

    if (typeof id === 'number') {
      this.userService.updateProfile( updatedUser).subscribe({
        next: (user) => {
          this.success = 'Profile updated successfully!';
          this.currentUser = user;
          // Update the current user in auth service
          localStorage.setItem('currentUser', JSON.stringify(user));
          setTimeout(() => this.success = '', 3000);
        },
        error: (error) => {
          this.error = error.error?.error || 'Failed to update profile.';
          setTimeout(() => this.error = '', 3000);
        }
      });
      this.success = "button clicked";
    } else {
      this.error = 'User ID is missing. Cannot update profile.';
      setTimeout(() => this.error = '', 3000);
    }
  }

  changePassword() {
    if (this.passwordForm.invalid) {
      return;
    }

    const passwordData = {
      userId: this.currentUser?.id,
      oldPassword: this.passwordForm.value.currentPassword,
      newPassword: this.passwordForm.value.newPassword
    };

    this.userService.changePassword(passwordData).subscribe({
      next: () => {
        this.success = 'Password changed successfully!';
        this.passwordForm.reset();
        setTimeout(() => this.success = '', 3000);
      },
      error: (error) => {
        console.log(error);
        this.error = error.error?.error || 'Failed to change password.';
        setTimeout(() => this.error = '', 3000);
      }
    });
  }

  get f() {
    return this.profileForm.controls;
  }

  get pf() {
    return this.passwordForm.controls;
  }

  getSeatNumbers(bookedSeats: any[]): string {
  return bookedSeats.map(seat => {
    const rowLetter = String.fromCharCode(64 + seat.rowNumber); // 1 -> A, 2 -> B, etc.
    return `${rowLetter}${seat.columnNumber}`;
  }).join(', ');
}
} 