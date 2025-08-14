import { Component } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent {
    email:string="";
    message=""

    constructor(private userService: UserService, router:Router) {}

    forgotPassword(email: string) {

     
      this.userService.forgotPassword(email).subscribe({
        next:Response => {
          console.log("Password reset link sent to email:", email);
          this.message="Password reset link sent to email "+email;
          
        },
        error: error => {
          console.error("Error sending password reset link:", error);
        }
      })
    }


}
