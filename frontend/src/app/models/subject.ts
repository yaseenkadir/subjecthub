import { Faculty } from './faculty';
import { Assessment } from './assessment';
export class Subject {
  assessments: Assessment[];
  availability: boolean;
  description: string;
  creditPoints: number;
  courseNumber: number;
  studentType: string;
  faculty: Faculty;
  id: number;
  minRequirements: string;
  name: string;
  numRatings: number;
  postgrad: boolean;
  rating: number;
  spring: boolean;
  summer: boolean;
  undergrad: boolean;
}
