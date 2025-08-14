import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/user.model';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-admin-feedback',
  templateUrl: './admin-feedback.component.html',
  styleUrls: ['./admin-feedback.component.scss']
})
export class AdminFeedbackComponent implements OnInit{

  constructor(private userservice:UserService){}
  users:User[]=[]

  ngOnInit(): void {
      this.userservice.getAllUsers().subscribe({
         next: (users) => {
        this.users = users;
        
      },
      error: (error) => {
      
        console.error('Error loading users:', error);
      }
      })
  }


  
  



  

}
