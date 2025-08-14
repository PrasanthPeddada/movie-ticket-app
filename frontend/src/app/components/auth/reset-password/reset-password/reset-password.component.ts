import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { HomeComponent } from 'src/app/components/home/home.component';
import { UserService } from 'src/app/services/user.service';


@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent implements OnInit{
   newPassword:string=""
   dupPassword:string=""
   message=""

   constructor(private router: Router, private route: ActivatedRoute,private userService:UserService) {}

  ngOnInit(): void {
     
  }

  resetPassword(){
    const token = this.route.snapshot.queryParamMap.get('token'); 
    if(token){
    this.userService.resetPassword(token,this.newPassword).subscribe({
      next:(response)=>{
          this.message="password reset is successful"
          this.router.navigate([''])
        
      },
      error:(response)=>{
        this.message="Invalid or missing token"
      }

    })
    
  
  }

  }
  


}
