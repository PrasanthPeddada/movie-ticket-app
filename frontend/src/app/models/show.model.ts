import { Movie } from './movie.model';
import { Screen } from './screen.model';

export interface Show {
  id?: number;
  movie: Movie;
  screen: Screen;
  showTime: Date;
  goldSeatPrice: number;
  silverSeatPrice: number;
  vipSeatPrice: number;
  active: boolean;
  createdAt?: Date;
  updatedAt?: Date;
} 