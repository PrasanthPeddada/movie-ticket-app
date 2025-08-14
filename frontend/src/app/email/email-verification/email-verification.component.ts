import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-email-verification',
  templateUrl: './email-verification.component.html',
  styleUrls: ['./email-verification.component.scss']
})
export class EmailVerificationComponent implements OnInit {
  message = 'Verifying...';
  error=""

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient
  ) {}

  ngOnInit() {
    const token = this.route.snapshot.queryParamMap.get('token');

    if (token) {
      this.http.get(`http://localhost:8080/api/users/verify?token=${token}`, { responseType: 'text' })
        .subscribe({
          next: (response) => {
            this.message = 'Email verified successfully!';
            this.error=""
          },
          error: (error) => {
            this.error = 'Verification failed or token is invalid.';
            this.message=""
          }
        });
    } else {
      this.error = 'Invalid or missing token in the URL.';
    }
  }
}

