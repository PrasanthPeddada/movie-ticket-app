import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { MovieListComponent } from './components/movies/movie-list/movie-list.component';
import { MovieDetailComponent } from './components/movies/movie-detail/movie-detail.component';
import { BookingComponent } from './components/booking/booking.component';
import { SeatSelectionComponent } from './components/booking/seat-selection/seat-selection.component';
import { PaymentComponent } from './components/payment/payment.component';
import { ProfileComponent } from './components/profile/profile.component';
import { AdminDashboardComponent } from './components/admin/admin-dashboard/admin-dashboard.component';
import { AdminMoviesComponent } from './components/admin/admin-movies/admin-movies.component';
import { AdminUsersComponent } from './components/admin/admin-users/admin-users.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { AuthGuard } from './guards/auth.guard';
import { AdminGuard } from './guards/admin.guard';
import { EmailVerificationComponent } from './email/email-verification/email-verification.component';
import { ForgotPasswordComponent } from './components/auth/forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './components/auth/reset-password/reset-password/reset-password.component';
import { AdminFeedbackComponent } from './components/admin/admin-feedback/admin-feedback.component';

@NgModule({
    declarations: [
        AppComponent,
        HeaderComponent,
        FooterComponent,
        HomeComponent,
        LoginComponent,
        RegisterComponent,
        MovieListComponent,
        MovieDetailComponent,
        BookingComponent,
        SeatSelectionComponent,
        PaymentComponent,
        ProfileComponent,
        AdminDashboardComponent,
        AdminMoviesComponent,
        AdminUsersComponent,
        EmailVerificationComponent,
        ForgotPasswordComponent,
        ResetPasswordComponent,
        AdminFeedbackComponent
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        FormsModule,
        NgbModule,
        RouterModule.forRoot([
            { path: '', component: HomeComponent },
            { path: 'login', component: LoginComponent },
            { path: 'register', component: RegisterComponent },
            { path: 'movies', component: MovieListComponent },
            { path: 'movies/:id', component: MovieDetailComponent },
            { path: 'booking/:showId', component: BookingComponent, canActivate: [AuthGuard] },
            { path: 'payment', component: PaymentComponent, canActivate: [AuthGuard] },
            { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
            { path: 'admin', component: AdminDashboardComponent, canActivate: [AuthGuard, AdminGuard] },
            { path: 'admin/movies', component: AdminMoviesComponent, canActivate: [AuthGuard, AdminGuard] },
            { path: 'admin/users', component: AdminUsersComponent, canActivate: [AuthGuard, AdminGuard] },
            {path:'admin/feedbacks',component:AdminFeedbackComponent,canActivate:[AuthGuard,AdminGuard]},
            { path: 'verify', component: EmailVerificationComponent },
            {path: 'forgot-password', component: ForgotPasswordComponent },
            {path:'reset-password',component:ResetPasswordComponent},
            { path: '**', redirectTo: '' }
            
            


        ])
    ],
    providers: [
        { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
        AuthGuard,
        AdminGuard
    ],
    bootstrap: [AppComponent]
})
export class AppModule { } 