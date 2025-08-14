import { Component, OnInit } from '@angular/core';
import { MovieService } from '../../../services/movie.service';
import { ShowService } from '../../../services/show.service';
import { ScreenService } from '../../../services/screen.service';
import { TheaterService } from '../../../services/theater.service';
import { Movie } from '../../../models/movie.model';
import { Show } from '../../../models/show.model';
import { Theater } from '../../../models/theater.model';
import { Screen } from '../../../models/screen.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-admin-movies',
  templateUrl: './admin-movies.component.html',
  styleUrls: ['./admin-movies.component.scss']
})
export class AdminMoviesComponent implements OnInit {
  movies: Movie[] = [];
  screens: Screen[] = [];
  theatres: Theater[] = [];
  loading = true;
  error = '';

  movieForm: FormGroup;
  showForm: FormGroup;
  theaterForm: FormGroup;
  editingMovie: Movie | null = null;
  selectedMovieForShow: Movie | null = null;
  modalRef: NgbModalRef | null = null;

  constructor(
    private movieService: MovieService,
    private showService: ShowService,
    private screenService: ScreenService,
    private TheaterService: TheaterService,
    private fb: FormBuilder,
    private modalService: NgbModal
  ) {
    this.movieForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      genre: ['', Validators.required],
      durationMinutes: ['', [Validators.required, Validators.min(1)]],
      rating: ['', [Validators.required, Validators.min(0), Validators.max(10)]],
      posterUrl: [''],
      trailerUrl: [''],
      language: ['', Validators.required],
      releaseDate: ['', Validators.required],
      isActive: [true]
    });

    this.showForm = this.fb.group({
      movieId: ['', Validators.required],
      screenId: ['', Validators.required],
      showTime: ['', Validators.required],
      goldSeatPrice: ['', [Validators.required, Validators.min(0)]],
      silverSeatPrice: ['', [Validators.required, Validators.min(0)]],
      vipSeatPrice: ['', [Validators.required, Validators.min(0)]],
      active: [true]
    });
    this.theaterForm = this.fb.group({
      name: ['', Validators.required],
      address: ['', Validators.required],
      city: ['', Validators.required],
      state: ['', Validators.required],
      pincode: ['', Validators.required],
      phoneNumber: ['', Validators.required],
     screens:this.fb.group({ 
      name: ['', Validators.required],
      totalRows: ['', [Validators.required, Validators.min(1)]],
      totalColumns: ['', [Validators.required, Validators.min(1)]]
      })
    });
  }



  ngOnInit() {
    this.loadMovies();
    this.loadScreens();
  }
  createScreenGroup(): FormGroup {
  return this.fb.group({
    name: ['', Validators.required],
    totalRows: ['', [Validators.required, Validators.min(1)]],
    totalColumns: ['', [Validators.required, Validators.min(1)]]
  });
}

  loadMovies() {
    this.loading = true;
    this.movieService.getAllMovies().subscribe({
      next: (movies) => {
        this.movies = movies;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load movies.';
        this.loading = false;
        console.error('Error loading movies:', error);
      }
    });
  }

  loadTheaters() {
    this.loading = true;
    this.TheaterService.getAllTheaters().subscribe({
      next: (theaters) => {
        // Handle the theaters data if needed
        this.theatres= theaters;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load theaters.';
        this.loading = false;
       
        console.error('Error loading theaters:', error);
      }
    });
  }

  loadScreens() {
    this.screenService.getAllScreens().subscribe({
      next: (screens) => {
        this.screens = screens;
      },
      error: (error) => {
        console.error('Error loading screens:', error);
      }
    });
  }

  openAddMovie(modalContent: any) {
    this.editingMovie = null;
    this.movieForm.reset();
    this.modalRef = this.modalService.open(modalContent);
  }

  openEditMovie(movie: Movie, modalContent: any) {
    this.editingMovie = movie;
    this.movieForm.patchValue(movie);
    this.modalRef = this.modalService.open(modalContent);
  }

  openAddShow(movie: Movie, modalContent: any) {
    this.selectedMovieForShow = movie;
    this.showForm.reset();
    this.showForm.patchValue({
      movieId: movie.id,
      active: true
    });
    this.modalRef = this.modalService.open(modalContent);
  }

  submitMovie() {
    if (this.movieForm.invalid) return;
    const movieData = this.movieForm.value;
    
    // Format releaseDate properly for backend
    if (movieData.releaseDate) {
      movieData.releaseDate = new Date(movieData.releaseDate).toISOString();
    }
    
    console.log('Form data being sent:', movieData);
    console.log('Form valid:', this.movieForm.valid);
    console.log('Form errors:', this.movieForm.errors);
    
    if (this.editingMovie) {
      // Edit
      this.movieService.updateMovie(this.editingMovie.id!, movieData).subscribe({
        next: () => {
          this.loadMovies();
          this.modalRef?.close();
        },
        error: (error) => {
          this.error = 'Failed to update movie.';
          console.error('Error updating movie:', error);
          console.error('Error details:', error.error);
        }
      });
    } else {
      // Add
      this.movieService.createMovie(movieData).subscribe({
        next: () => {
          this.loadMovies();
          this.modalRef?.close();
        },
        error: (error) => {
          this.error = 'Failed to add movie.';
          console.error('Error adding movie:', error);
          console.error('Error details:', error.error);
          console.error('Error status:', error.status);
          console.error('Error message:', error.message);
        }
      });
    }
  }

  submitTheater(modal: any) {
  if (this.theaterForm.invalid) return;

  const theaterData = this.theaterForm.value;
  const payload = {
      ...this.theaterForm.value,
      screens: [theaterData.screens] // Convert single object to array
      
    };

  this.TheaterService.createTheater(payload).subscribe({
    next: (response) => {
      console.log('Theater added:', response);
      modal.close();
      this.loadTheaters(); // Reload the list if needed
    },
    error: (err) => {
      console.error('Failed to add theater', err);
    }
  });
}


  submitShow() {
    if (this.showForm.invalid) return;
    const showData = this.showForm.value;
    
    
   if (showData.showTime) {
  const date = new Date(showData.showTime);

  // Format as 'YYYY-MM-DDTHH:mm:ss' in local time
  showData.showTime =
    `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}T` +
    `${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:00`;
}

const showPayload = {
  movie: { id: showData.movieId },
  screen: { id: Number(showData.screenId) },
  showTime: showData.showTime,
  goldSeatPrice: showData.goldSeatPrice,
  silverSeatPrice: showData.silverSeatPrice,
  vipSeatPrice: showData.vipSeatPrice,
  active: showData.active
};

    
    console.log('Show data being sent:', showPayload);
    
    this.showService.createShow(showPayload as any).subscribe({
      next: () => {
        this.modalRef?.close();
        // Optionally reload movies to refresh show data
        this.loadMovies();
      },
      error: (error) => {
        this.error = 'Failed to add show.';
        console.error('Error adding show:', error);
        console.error('Error details:', error.error);
        console.error('Error status:', error.status);
        console.error('Error message:', error.message);
      }
    });
  }

  deleteMovie(movieId: number) {
    if (confirm('Are you sure you want to delete this movie?')) {
      this.movieService.deleteMovie(movieId).subscribe({
        next: () => {
          this.movies = this.movies.filter(movie => movie.id !== movieId);
        },
        error: (error) => {
          this.error = 'Failed to delete movie.';
          console.error('Error deleting movie:', error);
        }
      });
    }
  }
} 