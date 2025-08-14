import { Component } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent {
  currentYear = new Date().getFullYear();
  showFeedback = false;
feedbackText = '';

constructor(private userService: UserService, private auth: AuthService) {}

openFeedbackForm() {
  this.showFeedback = true;
}

closeFeedbackForm() {
  this.showFeedback = false;
}

submitFeedback() {
  if (!this.feedbackText.trim()) {
    alert('Please enter your feedback.');
    return;
  }

  // Send feedback to backend
  const userid = this.auth?.currentUserValue?.id || null;
  if (userid){
  this.userService.sendfeedback(userid,this.feedbackText).subscribe({
    next: () => {
        alert('Thank you for your feedback!');
        this.feedbackText = '';
        this.showFeedback = false;
      },
      error: (err) => {
        console.error('Error submitting feedback', err);
        alert('Something went wrong. Please try again later.');}
  })

}
  

  // Reset form
  this.feedbackText = '';
  this.showFeedback = false;
}

} 