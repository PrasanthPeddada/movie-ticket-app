import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MovieService } from '../../../services/movie.service';
import { BookingService } from '../../../services/booking.service';
import { Movie } from '../../../models/movie.model';
import { Show } from '../../../models/show.model';

@Component({
  selector: 'app-movie-detail',
  templateUrl: './movie-detail.component.html',
  styleUrls: ['./movie-detail.component.scss']
})
export class MovieDetailComponent implements OnInit {
  movie: Movie | null = null;
  shows: Show[] = [];
  loading = true;
  error = '';
  selectedShow: Show | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private movieService: MovieService,
    private bookingService: BookingService
  ) { }

  ngOnInit() {
    const movieId = this.route.snapshot.paramMap.get('id');
    if (movieId) {
      this.loadMovieDetails(+movieId);
    }
  }

  loadMovieDetails(movieId: number) {
    this.movieService.getMovieByIdWithShows(movieId).subscribe({
      next: (movie) => {
        this.movie = movie;
        this.shows = movie.shows ? movie.shows.filter(show => show.active) : [];
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load movie details.';
        this.loading = false;
        console.error('Error loading movie:', error);
      }
    });
  }

  selectShow(show: Show) {
    this.selectedShow = show;
  }

  bookShow() {
    if (this.selectedShow) {
      this.router.navigate(['/booking', this.selectedShow.id]);
    }
  }

  formatShowTime(date: Date): string {
    return new Date(date).toLocaleString('en-US', {
      weekday: 'short',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  getPosterUrl(movie: Movie): string {
    if (movie.posterUrl && movie.posterUrl.trim() !== '') {
      return movie.posterUrl;
    }
    // Return a placeholder image URL or a data URL for a simple placeholder
    return 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMzAwIiBoZWlnaHQ9IjQ1MCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjZjBmMGYwIi8+CiAgPHRleHQgeD0iNTAlIiB5PSI1MCUiIGZvbnQtZmFtaWx5PSJBcmlhbCwgc2Fucy1zZXJpZiIgZm9udC1zaXplPSIxOCIgZmlsbD0iIzk5OSIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZHk9Ii4zZW0iPk5vIFBvc3RlcjwvdGV4dD4KPC9zdmc+';
  }

  onImageError(event: any) {
    // Set a simple placeholder when image fails to load
    event.target.src = 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMzAwIiBoZWlnaHQ9IjQ1MCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjZjBmMGYwIi8+CiAgPHRleHQgeD0iNTAlIiB5PSI1MCUiIGZvbnQtZmFtaWx5PSJBcmlhbCwgc2Fucy1zZXJpZiIgZm9udC1zaXplPSIxOCIgZmlsbD0iIzk5OSIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZHk9Ii4zZW0iPk5vIFBvc3RlcjwvdGV4dD4KPC9zdmc+';
  }
} 