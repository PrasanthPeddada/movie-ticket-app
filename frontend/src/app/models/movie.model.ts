import { Show } from './show.model';

export interface Movie {
  id?: number;
  title: string;
  description: string;
  durationMinutes: number;
  genre: string;
  language: string;
  releaseDate: Date;
  posterUrl: string;
  trailerUrl?: string;
  rating: number;
  isActive: boolean;
  createdAt?: Date;
  updatedAt?: Date;
  shows?: Show[];
} 