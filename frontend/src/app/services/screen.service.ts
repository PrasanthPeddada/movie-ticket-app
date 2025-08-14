import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Screen } from '../models/screen.model';

@Injectable({
  providedIn: 'root'
})
export class ScreenService {
  private apiUrl = 'http://localhost:8080/api/screens';

  constructor(private http: HttpClient) { }

  // Get all active screens
  getAllScreens(): Observable<Screen[]> {
    return this.http.get<Screen[]>(this.apiUrl);
  }

  // Get all screens including inactive (admin)
  getAllScreensAdmin(): Observable<Screen[]> {
    return this.http.get<Screen[]>(`${this.apiUrl}/admin/all`);
  }

  // Get screen by ID
  getScreenById(id: number): Observable<Screen> {
    return this.http.get<Screen>(`${this.apiUrl}/${id}`);
  }

  // Get screens by theater
  getScreensByTheater(theaterId: number): Observable<Screen[]> {
    return this.http.get<Screen[]>(`${this.apiUrl}/theater/${theaterId}`);
  }

  // Search screens
  searchScreens(filters: {
    name?: string;
    theaterId?: number;
  }): Observable<Screen[]> {
    let params = new HttpParams();
    
    if (filters.name) params = params.set('name', filters.name);
    if (filters.theaterId) params = params.set('theaterId', filters.theaterId.toString());

    return this.http.get<Screen[]>(`${this.apiUrl}/search`, { params });
  }

  // Create new screen
  createScreen(screen: Screen): Observable<any> {
    return this.http.post(this.apiUrl, screen);
  }

  // Update screen
  updateScreen(id: number, screen: Screen): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, screen);
  }

  // Delete screen (soft delete)
  deleteScreen(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  // Permanently delete screen
  permanentlyDeleteScreen(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}/permanent`);
  }

  // Get total seats in screen
  getScreenSeats(screenId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${screenId}/seats`);
  }

  // Get screen count by theater
  getScreenCountByTheater(theaterId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/theater/${theaterId}/count`);
  }

  // Get screen statistics (admin)
  getScreenStats(): Observable<any> {
    return this.http.get(`${this.apiUrl}/admin/stats`);
  }
} 