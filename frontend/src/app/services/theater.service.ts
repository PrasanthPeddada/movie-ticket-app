import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Theater } from '../models/theater.model';

@Injectable({
  providedIn: 'root'
})
export class TheaterService {
  private apiUrl = 'http://localhost:8080/api/theaters';

  constructor(private http: HttpClient) { }

  // Get all active theaters
  getAllTheaters(): Observable<Theater[]> {
    return this.http.get<Theater[]>(this.apiUrl);
  }

  // Get all theaters including inactive (admin)
  getAllTheatersAdmin(): Observable<Theater[]> {
    return this.http.get<Theater[]>(`${this.apiUrl}/admin/all`);
  }

  // Get theater by ID
  getTheaterById(id: number): Observable<Theater> {
    return this.http.get<Theater>(`${this.apiUrl}/${id}`);
  }

  // Get theaters by city
  getTheatersByCity(city: string): Observable<Theater[]> {
    return this.http.get<Theater[]>(`${this.apiUrl}/city/${city}`);
  }

  // Get theaters by state
  getTheatersByState(state: string): Observable<Theater[]> {
    return this.http.get<Theater[]>(`${this.apiUrl}/state/${state}`);
  }

  // Search theaters with filters
  searchTheaters(filters: {
    name?: string;
    city?: string;
    state?: string;
  }): Observable<Theater[]> {
    let params = new HttpParams();
    
    if (filters.name) params = params.set('name', filters.name);
    if (filters.city) params = params.set('city', filters.city);
    if (filters.state) params = params.set('state', filters.state);

    return this.http.get<Theater[]>(`${this.apiUrl}/search`, { params });
  }

  // Get all cities
  getAllCities(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/cities`);
  }

  // Get all states
  getAllStates(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/states`);
  }

  // Create new theater
  createTheater(theater: Theater): Observable<any> {
    return this.http.post(this.apiUrl, theater);
  }

  // Update theater
  updateTheater(id: number, theater: Theater): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, theater);
  }

  // Delete theater (soft delete)
  deleteTheater(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  // Permanently delete theater
  permanentlyDeleteTheater(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}/permanent`);
  }

  // Get theaters by location
  getTheatersByLocation(city?: string, state?: string): Observable<Theater[]> {
    let params = new HttpParams();
    
    if (city) params = params.set('city', city);
    if (state) params = params.set('state', state);

    return this.http.get<Theater[]>(`${this.apiUrl}/location`, { params });
  }

  // Get theater statistics (admin)
  getTheaterStats(): Observable<any> {
    return this.http.get(`${this.apiUrl}/admin/stats`);
  }
} 