import { Input, Component, OnInit } from "@angular/core";
import { Router } from '@angular/router';

import * as Fuse from "fuse.js";

import "rxjs/add/observable/of";
import "rxjs/add/operator/switchMap";
import "rxjs/add/operator/toPromise";
import Promise from 'bluebird';

import {FacultySearchService} from "../services/faculty-search.service";
import { SubjectSearchService } from "../services/subject-search.service";
import {Faculty} from "../models/faculty";
import { University } from '../models/university';
import { Subject } from '../models/subject';

@Component({
  selector: "fuse-search-bar",
  templateUrl: "./fuse-search-bar.component.html",
  styleUrls: ["./search-bar.component.css"],
  providers: [SubjectSearchService, FacultySearchService]
})
export class FuseSearchBarComponent implements OnInit {
  @Input ()
  university: University;
  faculties: Faculty[];
  selectedFaculty: Faculty;
  subjects: Subject[];
  displaySubjects: Subject[];

  fuse: Fuse;

  term: string

  options = {
    shouldSort: true,
    threshold: 0.4,
    location: 0,
    distance: 10000,
    maxPatternLength: 32,
    minMatchCharLength: 3,
    keys: [
      "code",
      "name",
      "availability",
      "university.name",
      "university.abbreviation",
      "creditPoints",
      "description"
    ]
  };
  constructor(private router: Router, private facultySearchService: FacultySearchService, private subjectSearchService: SubjectSearchService) {
    this.displaySubjects = [];
  }

  search(term: string): void {
    if (!term) return;
    else this.displaySubjects = this.fuse.search(term) as Subject[];
  }

  fetchSubjects(): Promise<Subject[]> {
    return this.subjectSearchService
        .fetch(this.university.id.toString())
  }

  fetchFaculties(): Promise<Faculty[]> {
    return this.facultySearchService.fetch(this.university.id.toString())
  }

  createFuse(subjects): void {
    this.fuse = new Fuse(subjects, this.options);
  }

  filterSubjects(): void {
    const selected = this.selectedFaculty;

    if (!selected) {
        this.createFuse(this.subjects);
        this.displaySubjects = this.subjects;
        this.search(this.term);
    }
    else {
        this.displaySubjects = this.subjects.filter(subject => subject.faculty['id'] === selected.id);
        this.createFuse(this.displaySubjects);
        this.search(this.term);
    }
  }


  ngOnInit(): void {
    Promise.all([
        this.fetchFaculties(),
        this.fetchSubjects()
    ]).spread((faculties, subjects) => {
        this.faculties = faculties;
        this.subjects = subjects;
        this.displaySubjects = subjects.map(s => s);
        this.createFuse(subjects);
        this.search(this.term);
    });
  }

  goToSubjectDetails(subject: Subject): void {
      this.router.navigate([`/university/${this.university.id}/subject/${subject.id}`]);
  }
}
