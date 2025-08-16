import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Movie } from '../models/movie.model';

@Injectable({
  providedIn: 'root'
})
export class MovieService {
  private apiUrl = 'http://localhost:8080/api/movies';

  constructor(private http: HttpClient) { }

  // Get all active movies
  getAllMovies(): Observable<Movie[]> {
    return this.http.get<Movie[]>(this.apiUrl);
  }

  
  getAllMoviesAdmin(): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.apiUrl}/admin/all`);
  }

  // Get movie by ID
  getMovieById(id: number): Observable<Movie> {
    return this.http.get<Movie>(`${this.apiUrl}/${id}`);
  }

  getMovieByIdWithShows(id: number): Observable<Movie> {
    return this.http.get<Movie>(`${this.apiUrl}/${id}/with-shows`);}

  // Search movies with filters
  searchMovies(filters: {
    title?: string;
    genre?: string;
    language?: string;
    minRating?: number;
    director?: string;
  }): Observable<Movie[]> {
    let params = new HttpParams();
    
    if (filters.title) params = params.set('title', filters.title);
    if (filters.genre) params = params.set('genre', filters.genre);
    if (filters.language) params = params.set('language', filters.language);
    if (filters.minRating) params = params.set('minRating', filters.minRating.toString());
    if (filters.director) params = params.set('director', filters.director);

    return this.http.get<Movie[]>(`${this.apiUrl}/search`, { params });
  }

  // Get all genres
  getAllGenres(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/genres`);
  }

  // Get all languages
  getAllLanguages(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/languages`);
  }

  // Get all directors
  getAllDirectors(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/directors`);
  }

  // Get all certificates
  getAllCertificates(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/certificates`);
  }

  // Get all formats
  getAllFormats(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/formats`);
  }

  // Create new movie
  createMovie(movie: Movie): Observable<any> {
    return this.http.post(this.apiUrl, movie);
  }

  // Update movie
  updateMovie(id: number, movie: Movie): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, movie);
  }

  // Delete movie (soft delete)
  deleteMovie(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  // Permanently delete movie
  permanentlyDeleteMovie(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}/permanent`);
  }

  // Get upcoming movies
  getUpcomingMovies(): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.apiUrl}/upcoming`);
  }

  // Get now showing movies
  getNowShowingMovies(): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.apiUrl}/now-showing`);
  }

  // Get top rated movies
  getTopRatedMovies(limit: number = 10): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.apiUrl}/top-rated?limit=${limit}`);
  }

  // Get movies by genre
  getMoviesByGenre(genre: string): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.apiUrl}/genre/${genre}`);
  }

  // Get movies by language
  getMoviesByLanguage(language: string): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.apiUrl}/language/${language}`);
  }

  // Get movies by rating
  getMoviesByRating(minRating: number): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.apiUrl}/rating/${minRating}`);
  }

  
} 