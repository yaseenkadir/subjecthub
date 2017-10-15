import { Faculty } from './faculty';
import { Assessment } from './assessment';
export class Subject {
  assessments: Assessment[];
  comments: any[];
  availability: boolean;
  description: string;

  creditPoints: string;
  code:  string;

  studentType: string;
  faculty: Faculty;
  id: number;
  minRequirements: string;
  name: string;
  numRatings: number;
  postgrad: boolean;
  rating: number;
  autumn: boolean;
  spring: boolean;
  summer: boolean;
  undergrad: boolean;

  constructor() {
    this.name = '';
    this.description = '';
    this.creditPoints = '';
    this.code = '';
    this.minRequirements = '';
    this.postgrad = false;
    this.undergrad = false;
    this.summer = false;
    this.spring = false;
    this.autumn = false;
  }
}
