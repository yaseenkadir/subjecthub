import { Faculty } from './faculty';
import { Assessment } from './assessment';
export class Subject {
  assessments: Assessment[];
  availability: boolean;
  description: string;
  creditPoints: number;
  code: number;
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

  constructor(code: string, name: string, creditPoints: string, description: string, minRequirements: string, postgrad: boolean, undergrad: boolean) {
    this.code = Number(code);
    this.name = name;
    this.creditPoints = Number(creditPoints);
    this.description = description;
    this.minRequirements = minRequirements;
    this.postgrad = postgrad;
    this.undergrad = undergrad;
  }
}
