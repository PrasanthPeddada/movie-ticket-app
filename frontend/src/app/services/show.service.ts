import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Show } from '../models/show.model';

@Injectable({
  providedIn: 'root'
})
export class ShowService {
  private apiUrl = 'http://localhost:8080/api/shows';

  constructor(private http: HttpClient) { }
  

  // Get all shows
  getAllShows(): Observable<Show[]> {
    return this.http.get<Show[]>(this.apiUrl);
  }

  // Get shows by movie ID
  getShowsByMovie(movieId: number): Observable<Show[]> {
    return this.http.get<Show[]>(`${this.apiUrl}/movie/${movieId}`);
  }

  // Create a new show
  createShow(show: Show): Observable<Show> {
    return this.http.post<Show>(this.apiUrl, show);
  }

  // Update a show
  updateShow(id: number, show: Show): Observable<Show> {
    return this.http.put<Show>(`${this.apiUrl}/${id}`, show);
  }

  // Delete a show
  deleteShow(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  // Get show by ID
  getShowById(id: number): Observable<Show> {
    return this.http.get<Show>(`${this.apiUrl}/${id}`);
  }

  // Get active shows
  getActiveShows(): Observable<Show[]> {
    return this.http.get<Show[]>(`${this.apiUrl}/active`);
  }
} 