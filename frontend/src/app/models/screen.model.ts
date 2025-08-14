import { Theater } from './theater.model';

export interface Screen {
  id?: number;
  name: string;
  theater: Theater;
  totalRows: number;
  totalColumns: number;
  isActive: boolean;
  createdAt?: Date;
  updatedAt?: Date;
  theaterName?: string; // Optional field for displaying theater name in UI
} 