import { Component } from '@angular/core';

@Component({
    selector: 'app-root',
    template: `
    <app-header></app-header>
    <main class="main-content">
      <router-outlet></router-outlet>
    </main>
    <app-footer></app-footer>
  `,
    styles: [`
    .main-content {
      min-height: calc(100vh - 160px);
      padding-top: 80px;
    }
  `]
})
export class AppComponent {
    title = 'Movie Ticket Booking';
} 