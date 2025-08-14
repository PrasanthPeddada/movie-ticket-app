import { Component, OnInit } from '@angular/core';
import { MovieService } from '../../../services/movie.service';
import { Movie } from '../../../models/movie.model';

@Component({
  selector: 'app-movie-list',
  templateUrl: './movie-list.component.html',
  styleUrls: ['./movie-list.component.scss']
})
export class MovieListComponent implements OnInit {
  movies: Movie[] = [];
  filteredMovies: Movie[] = [];
  loading = true;
  error = '';
  searchQuery = '';
  selectedGenre = '';
  selectedLanguage = '';

  genres: string[] = [];

  constructor(private movieService: MovieService) { }

  ngOnInit() {
    this.loadMovies();
  }

  loadMovies() {
    this.loading = true;
    this.movieService.getAllMovies().subscribe({
      next: (movies) => {
        console.log('Received movies:', movies);
        this.movies = movies.filter(movie => movie.isActive);
        console.log('Filtered active movies:', this.movies);
        this.filteredMovies = [...this.movies];
        this.extractGenres();
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load movies. Please try again later.';
        this.loading = false;
        console.error('Error loading movies:', error);
      }
    });
  }

  extractGenres() {
    const genreSet = new Set<string>();
    this.movies.forEach(movie => {
      if (movie.genre) {
        genreSet.add(movie.genre);
      }
    });
    this.genres = Array.from(genreSet).sort();
  }

  extractLanguages() {
    const languageSet = new Set<string>();
    this.movies.forEach(movie => {
      if (movie.language) {
        languageSet.add(movie.language);
      }
    });
    return Array.from(languageSet).sort();
  }

  onSearch() {
    this.filterMovies();
  }

  onGenreChange() {
    this.filterMovies();
  }

  onLanguageChange() {
    this.filterMovies();
  }

  filterMovies() {
    this.filteredMovies = this.movies.filter(movie => {
      const matchesSearch = !this.searchQuery || 
        movie.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        movie.description.toLowerCase().includes(this.searchQuery.toLowerCase());
      
      const matchesGenre = !this.selectedGenre || movie.genre === this.selectedGenre;
      const matchesLanguage = !this.selectedLanguage || movie.language === this.selectedLanguage;
      
      return matchesSearch && matchesGenre && matchesLanguage;
    });
  }

  clearFilters() {
    this.searchQuery = '';
    this.selectedGenre = '';
    this.filteredMovies = [...this.movies];
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